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

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class to access private fields and methods from the Minecraft class.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MinecraftAccessor implements PrivateFields {

    private static final Logger L = LogManager.getLogger();
    
    private MinecraftAccessor() {
    }

    public static Timer getTimer(Minecraft mc) {
        try {
            return ReflectionHelper.getPrivateValue(Minecraft.class, mc, MINECRAFT_TIMER);
        } catch (Exception ex) {
            L.error("getTimer() failed", ex);
            return null;
        }
    }
}
