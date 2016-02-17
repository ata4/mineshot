/*
 ** 2015 Mai 19
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ClippingHelperAccessor implements PrivateFields {
    
    private static final Logger L = LogManager.getLogger();
    
    private static ClippingHelperImpl clippingHelperOrig;
    
    public static void setCullingEnabled(boolean culling) {
        try {
            if (culling && clippingHelperOrig != null) {
                ReflectionHelper.setPrivateValue(ClippingHelperImpl.class, null, clippingHelperOrig, CLIPPINGHELPERIMPL_INSTANCE);
                clippingHelperOrig = null;
            } else if (!culling && clippingHelperOrig == null) {
                clippingHelperOrig = ReflectionHelper.getPrivateValue(ClippingHelperImpl.class, null, CLIPPINGHELPERIMPL_INSTANCE);
                ReflectionHelper.setPrivateValue(ClippingHelperImpl.class, null, new ClippingHelperDummy(), CLIPPINGHELPERIMPL_INSTANCE);
            }
        } catch (Exception ex) {
            L.error("setCullingEnabled() failed", ex);
        }
    }
    
    public static boolean isCullingEnabled() {
        return clippingHelperOrig == null;
    }

    private ClippingHelperAccessor() {
    }
}
