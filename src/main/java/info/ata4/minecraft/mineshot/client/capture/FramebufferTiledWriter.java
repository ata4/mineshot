/*
 ** 2014 August 19
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.capture;

import info.ata4.minecraft.mineshot.client.wrapper.Projection;
import info.ata4.minecraft.mineshot.client.wrapper.ToggleableClippingHelper;
import info.ata4.minecraft.mineshot.util.reflection.MinecraftAccessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Timer;
import org.apache.commons.io.IOUtils;
import org.lwjgl.util.Dimension;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class FramebufferTiledWriter extends FramebufferWriter {
    
    private static final Minecraft MC = Minecraft.getMinecraft();

    private final int widthTiled;
    private final int heightTiled;
    
    public FramebufferTiledWriter(File file, FramebufferCapturer fbc, int width, int height) throws FileNotFoundException, IOException {
        super(file, fbc);
        
        this.widthTiled = width;
        this.heightTiled = height;
    }

    private void modifySettings() {
        ToggleableClippingHelper.getInstance().setEnabled(false);
    }

    private void restoreSettings() {
        ToggleableClippingHelper.getInstance().setEnabled(true);
    }
    
    @Override
    public void write() throws IOException {
        Dimension dim = fbc.getCaptureDimension();
        int widthViewport = dim.getWidth();
        int heightViewport = dim.getHeight();
        int bpp = fbc.getBytesPerPixel();
              
        double tilesX = widthTiled / (double) widthViewport;
        double tilesY = heightTiled / (double) heightViewport;

        int numTilesX = (int) Math.ceil(tilesX);
        int numTilesY = (int) Math.ceil(tilesY);
        
        Projection.zoom = tilesX <= tilesY ? tilesY : tilesX;
        
        EntityRenderer entityRenderer = MC.entityRenderer;
        Timer timer = MinecraftAccessor.getTimer(MC);
        
        fbc.setFlipColors(true);
        fbc.setFlipLines(false);
        
        modifySettings();
        
        long fileSize = (long) widthTiled * heightTiled * bpp + HEADER_SIZE;
        ByteBuffer bbHeader = buildTargaHeader(widthTiled, heightTiled, bpp * 8);
        
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(fileSize);
            
            FileChannel fc = raf.getChannel();
            fc.write(bbHeader);
            
            long nanoTime = System.nanoTime();
            float partialTicks = timer == null ? 0 : timer.renderPartialTicks;
            
            for (int y = 0; y < numTilesY; y++) {
                for (int x = 0; x < numTilesX; x++) {
                    // clip the captured frame if too big
                    int tileWidth = Math.min(widthViewport, widthTiled - (widthViewport * x));
                    int tileHeight = Math.min(heightViewport, heightTiled - (heightViewport * y));

                    // update camera offset and zoom
                    Projection.offsetX = (widthTiled - widthViewport - (widthViewport * x) * 2) / (double) widthViewport;
                    Projection.offsetY = (heightTiled - heightViewport - (heightViewport * (tilesY - y - 1)) * 2) / (double) heightViewport;
                    
                    // render the tile
                    entityRenderer.updateCameraAndRender(partialTicks, nanoTime);

                    // get framebuffer
                    fbc.capture();
                    ByteBuffer frameBuffer = fbc.getByteBuffer();

                    // copy viewport buffer into the tile buffer, row by row
                    for (int i = 0; i < tileHeight; i++) {
                        // read row from viewport buffer
                        frameBuffer.clear();
                        frameBuffer.position(i * widthViewport * bpp);
                        frameBuffer.limit((i * widthViewport + tileWidth) * bpp);

                        // write row at tiled position
                        long o1 = (long) widthTiled * i;
                        long o2 = (long) widthTiled * heightViewport * y;
                        long o3 = (long) widthViewport * x;
                        fc.position((o1 + o2 + o3) * bpp + HEADER_SIZE);
                        fc.write(frameBuffer);
                    }
                }
            }
        } finally {
            // restore camera settings
            Projection.zoom = 1;
            Projection.offsetX = 0;
            Projection.offsetY = 0;
            
            // restore game settings
            restoreSettings();
            
            // close file
            IOUtils.closeQuietly(raf);
        }
    }
}
