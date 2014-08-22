/*
 ** 2014 January 21
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.profiler;

import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.relauncher.Side;
import info.ata4.minecraft.mineshot.util.reflection.UnsafeReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Profiler Events mod class.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ProfilerEventManager {
    
    private static final Logger L = LogManager.getLogger();
    private static final EventBus eventBus = new EventBus();
    
    private ProfilerEventManager() {
    }
    
    public static EventBus bus() {
        return eventBus;
    }
    
    public static void initClient() {
        Minecraft mc = Minecraft.getMinecraft();
        
        if (mc.mcProfiler == null) {
            L.warn("Client profiler not yet initialized!");
            return;
        }
        
        if (mc.mcProfiler instanceof ProfilerEventProxy) {
            L.warn("Client profiler already modified!");
            return;
        }
        
        try {
            ProfilerEventProxy profiler = new ProfilerEventProxy(mc.mcProfiler, eventBus, Side.CLIENT);
            int profilerFieldIndex = UnsafeReflectionHelper.findFieldIndex(Minecraft.class, Profiler.class);
            UnsafeReflectionHelper.setFinalValue(Minecraft.class, mc, profiler, profilerFieldIndex);
            L.info("Client profiler hooked successfully");
        } catch (Throwable ex) {
            L.error("Client profiler hook error", ex);
        }
    }
    
    public static void initServer() {
        MinecraftServer mcs = MinecraftServer.getServer();
        
        if (mcs == null) {
            return;
        }
        
        if (mcs.theProfiler == null) {
            L.warn("Server profiler not yet initialized!");
            return;
        }
        
        if (mcs.theProfiler instanceof ProfilerEventProxy) {
            L.warn("Server profiler already modified!");
            return;
        }
        
        try {
            ProfilerEventProxy profiler = new ProfilerEventProxy(mcs.theProfiler, eventBus, Side.SERVER);
            int profilerFieldIndex = UnsafeReflectionHelper.findFieldIndex(MinecraftServer.class, Profiler.class);
            UnsafeReflectionHelper.setFinalValue(MinecraftServer.class, mcs, profiler, profilerFieldIndex);
            L.info("Server profiler hooked successfully");
        } catch (Throwable ex) {
            L.error("Server profiler hook error", ex);
        }
    }
}
