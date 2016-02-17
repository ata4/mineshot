/*
** 2016 Februar 17
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.wrapper;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;

/**
 * ClippingHelper extension that can be disabled, effectively disabling any
 * clipping.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ToggleableClippingHelper extends ClippingHelperImpl {

    private static final ToggleableClippingHelper INSTANCE = new ToggleableClippingHelper();
    
    public static ToggleableClippingHelper getInstance() {
        INSTANCE.init();
        return INSTANCE;
    }
    
    public static ClippingHelper getInstanceWrapper() {
        INSTANCE.init();
        return INSTANCE;
    }
    
    private boolean enabled;
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isBoxInFrustum(double x1, double y1, double z1, double x2, double y2, double z2) {
        if (enabled) {
            return super.isBoxInFrustum(x1, y1, z1, x2, y2, z2); 
        } else {
            return true;
        }
    }
}
