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

/**
 * Dummpy clipping helper used to disable frustum culling.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ClippingHelperDummy extends ClippingHelperImpl {
    
    @Override
    public boolean isBoxInFrustum(double nx, double ny, double nz, double fx, double fy, double fz) {
        return true;
    }
}
