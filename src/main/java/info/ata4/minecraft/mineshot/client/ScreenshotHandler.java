/*
 ** 2014 January 21
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client;

import info.ata4.minecraft.mineshot.client.capture.task.CaptureTask;
import info.ata4.minecraft.mineshot.client.capture.task.CaptureTiledTask;
import info.ata4.minecraft.mineshot.client.capture.task.RenderTickTask;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;
import info.ata4.minecraft.mineshot.client.config.MineshotConfigGuiIngame;
import info.ata4.minecraft.mineshot.client.util.ChatUtils;
import info.ata4.minecraft.mineshot.util.reflection.RenderGlobalAccessor;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ScreenshotHandler {
    
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final Logger L = LogManager.getLogger();
    private static final String KEY_CATEGORY = "key.categories.mineshot";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    
    private final KeyBinding keyCapture = new KeyBinding("key.mineshot.capture", Keyboard.KEY_F9, KEY_CATEGORY);
    private final MineshotConfig config;
    
    private File taskFile;
    private RenderTickTask task;

    public ScreenshotHandler(MineshotConfig config) {
        this.config = config;
        
        ClientRegistry.registerKeyBinding(keyCapture);
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // don't poll keys when there's an active task
        if (task != null) {
            return;
        }
        
        if (keyCapture.isPressed()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                MC.displayGuiScreen(new MineshotConfigGuiIngame(config));
            } else {
                taskFile = getScreenshotFile();
                if (config.captureTiled.get()) {
                    task = new CaptureTiledTask(config, taskFile);
                } else {
                    task = new CaptureTask(config, taskFile);
                }
//                if (config.preloadChunks.get()) {
//                    preloadChunks();
//                }
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderTick(RenderTickEvent evt) {
        if (task == null) {
            return;
        }
        
        try {
            if (task.onRenderTick(evt)) {
                task = null;
                ChatUtils.printFileLink("screenshot.success", taskFile);
            }
        } catch (Exception ex) {
            L.error("Screenshot capture failed", ex);
            ChatUtils.print("screenshot.failure", ex.getMessage());
            task = null;
        }
    }
    
//    private void preloadChunks() {
//        WorldRenderer[] worldRenderers = RenderGlobalAccessor.getWorldRenderers(MC.renderGlobal);
//        for (WorldRenderer worldRenderer : worldRenderers) {
//            if (worldRenderer.isInFrustum && worldRenderer.needsUpdate) {
//                worldRenderer.updateRenderer(MC.renderViewEntity);
//            }
//        }
//    }

    private File getScreenshotFile() {
        File dir = new File(MC.mcDataDir, "screenshots");
        if (!dir.exists()) {
            dir.mkdir();
        }
        
        File file;
        String fileName = "huge_" + DATE_FORMAT.format(new Date());
        String fileExt = "tga";
        
        // loop though suffixes while the file exists
        for (int i = 1; (file = new File(dir, fileName + (i != 1 ? "_" + i : "") + "." + fileExt)).exists(); i++) {
        }
        
        return file;
    }
}
