/*
 ** 2012 March 30
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class to access private fields and methods from the Minecraft class.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MinecraftAccessor {

    private static final Logger L = LogManager.getLogger();
    
    private static final String[] FIELD_TIMER = new String[] {"timer", "field_71428_T"};
    private static final String[] METHOD_RESIZE = new String[] {"resize", "func_71370_a"};
    
    private MinecraftAccessor() {
    }

    public static Timer getTimer(Minecraft mc) {
        try {
            return ReflectionHelper.getPrivateValue(Minecraft.class, mc, FIELD_TIMER);
        } catch (Exception ex) {
            L.error("getTimer() failed", ex);
            return null;
        }
    }

    public static void resize(Minecraft mc, int width, int height) {
        try {
            Method resize = ReflectionHelper.findMethod(Minecraft.class, mc, METHOD_RESIZE, Integer.TYPE, Integer.TYPE);
            resize.invoke(mc, width, height);
        } catch (Exception ex) {
            L.error("resize() failed", ex);
        }
    }
}
