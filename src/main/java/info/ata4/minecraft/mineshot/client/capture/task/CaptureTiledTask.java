/*
 ** 2014 August 20
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.capture.task;

import info.ata4.minecraft.mineshot.client.capture.FramebufferCapturer;
import info.ata4.minecraft.mineshot.client.capture.FramebufferTiledWriter;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class CaptureTiledTask implements RenderTickTask {
    
    private final MineshotConfig config;
    private final File file;
    
    public CaptureTiledTask(MineshotConfig config, File file) {
        this.config = config;
        this.file = file;
    }

    @Override
    public boolean onRenderTick(RenderTickEvent evt) throws FileNotFoundException, IOException {
        int width = config.captureWidth.get();
        int height = config.captureHeight.get();
        
        FramebufferCapturer fbc = new FramebufferCapturer();
        FramebufferTiledWriter fbw = new FramebufferTiledWriter(file, fbc, width, height);
        fbw.write();
        
        return true;
    }
    
}
