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
import info.ata4.minecraft.mineshot.client.util.ChatUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.Minecraft;
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
    
    private Path taskFile;
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
            taskFile = getScreenshotFile();
            if (config.captureTiled.get()) {
                task = new CaptureTiledTask(config, taskFile);
            } else {
                task = new CaptureTask(config, taskFile);
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
                ChatUtils.printFileLink("screenshot.success", taskFile.toFile());
            }
        } catch (Exception ex) {
            L.error("Screenshot capture failed", ex);
            ChatUtils.print("screenshot.failure", ex.getMessage());
            task = null;
        }
    }

    private Path getScreenshotFile() {
        Path dir = MC.mcDataDir.toPath().resolve("screenshots");
            
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        
        // loop though suffixes while the file exists
        int i = 0;
        Path file;
        do {
            file = dir.resolve(String.format("huge_%s_%04d.tga",
                    DATE_FORMAT.format(new Date()), i++));
        } while (Files.exists(file));
        
        return file;
    }
}
