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
import info.ata4.minecraft.mineshot.client.capture.FramebufferWriter;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;
import java.io.File;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class CaptureTask implements RenderTickTask {
    
    private static final Minecraft MC = Minecraft.getMinecraft();
    
    private final MineshotConfig config;
    private final Path file;
    
    private int frame;
    private int displayWidth;
    private int displayHeight;
    
    public CaptureTask(MineshotConfig config, Path file) {
        this.config = config;
        this.file = file;
    }

    @Override
    public boolean onRenderTick(RenderTickEvent evt) throws Exception {
        switch (frame) {
            // override viewport size (the following frame will be black)
            case 0:
                displayWidth = MC.displayWidth;
                displayHeight = MC.displayHeight;
                
                int width = config.captureWidth.get();
                int height = config.captureHeight.get();
                
                // resize viewport/framebuffer
                MC.resize(width, height);
                break;

            // capture screenshot and restore viewport size
            case 3:
                try {
                    FramebufferCapturer fbc = new FramebufferCapturer();
                    FramebufferWriter fbw = new FramebufferWriter(file, fbc);
                    fbw.write();
                } finally {
                    // restore viewport/framebuffer
                    MC.resize(displayWidth, displayHeight);
                }
                break;
        }
        
        frame++;
        return frame > 3;
    }
}
