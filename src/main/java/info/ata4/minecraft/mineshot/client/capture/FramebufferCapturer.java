/*
 ** 2014 July 28
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.capture;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import org.lwjgl.util.Dimension;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class FramebufferCapturer {
    
    private static final int BPP = 3; // bytes per pixel
    private static final int TYPE = GL_UNSIGNED_BYTE;
    private static final Minecraft MC = Minecraft.getMinecraft();
    
    private final ByteBuffer bb;
    private final Dimension dim;
    private final byte[] line1;
    private final byte[] line2;
    private boolean flipColors = false;
    private boolean flipLines = false;

    public FramebufferCapturer() {
        dim = getCurrentDimension();
        bb = ByteBuffer.allocateDirect(dim.getWidth() * dim.getHeight() * BPP);
        line1 = new byte[MC.displayWidth * BPP];
        line2 = new byte[MC.displayWidth * BPP];
    }
    
    public void setFlipColors(boolean flipColors) {
        this.flipColors = flipColors;
    }
    
    public boolean isFlipColors() {
        return flipColors;
    }
    
    public void setFlipLines(boolean flipLines) {
        this.flipLines = flipLines;
    }
    
    public boolean isFlipLines() {
        return flipLines;
    }

    public int getBytesPerPixel() {
        return BPP;
    }
    
    public ByteBuffer getByteBuffer() {
        bb.rewind();
        return bb.duplicate();
    }
    
    public Dimension getCaptureDimension() {
        return dim;
    }
    
    private Dimension getCurrentDimension() {
        return new Dimension(MC.displayWidth, MC.displayHeight);
    }
    
    public void capture() {
        // check if the dimensions are still the same
        Dimension dim1 = getCurrentDimension();
        Dimension dim2 = getCaptureDimension();
        if (!dim1.equals(dim2)) {
            throw new IllegalStateException(String.format(
                    "Display size changed! %dx%d != %dx%d",
                    dim1.getWidth(), dim1.getHeight(),
                    dim2.getWidth(), dim2.getHeight()));
        }
        
        // set alignment flags
        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        
        int format = flipColors ? GL_BGR : GL_RGB;
        
        // read texture from framebuffer if enabled, otherwise use slower glReadPixels
        if (OpenGlHelper.isFramebufferEnabled()) {
            Framebuffer fb = MC.getFramebuffer();
            glBindTexture(GL_TEXTURE_2D, fb.framebufferTexture);
            glGetTexImage(GL_TEXTURE_2D, 0, format, TYPE, bb);
        } else {
            glReadPixels(0, 0, MC.displayWidth, MC.displayHeight, format, TYPE, bb);
        }
        
        if (!flipLines) {
            return;
        } 
        
        // flip buffer vertically
        for (int i = 0; i < MC.displayHeight / 2; i++) {
            int ofs1 = i * MC.displayWidth * BPP;
            int ofs2 = (MC.displayHeight - i - 1) * MC.displayWidth * BPP;

            // read lines
            bb.position(ofs1);
            bb.get(line1);
            bb.position(ofs2);
            bb.get(line2);

            // write lines at swapped positions
            bb.position(ofs2);
            bb.put(line1);
            bb.position(ofs1);
            bb.put(line2);
        }
    }
}
