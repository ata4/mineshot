/*
 ** 2014 August 22
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class RenderGlobalAccessor {
    
    private static final Logger L = LogManager.getLogger();
    private static final String[] FIELD_WORLDRENDERERS = new String[] {"worldRenderers", "field_72765_l"};
    
    private RenderGlobalAccessor() {
    }
    
    public static WorldRenderer[] getWorldRenderers(RenderGlobal rg) {
        try {
            return ReflectionHelper.getPrivateValue(RenderGlobal.class, rg, FIELD_WORLDRENDERERS);
        } catch (Exception ex) {
            L.error("getWordRenderers() failed", ex);
            return new WorldRenderer[]{};
        }
    }
}
