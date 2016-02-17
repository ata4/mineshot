/*
 ** 2015 Mai 20
 **
 ** The author disclaims copyright to this source code. In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ActiveRenderInfoAccessor implements PrivateFields {
    
    private static final Logger L = LogManager.getLogger();
    
    private ActiveRenderInfoAccessor() {
    }

    public static void setRotationX(float rotationX) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationX, ACTIVERENDERINFO_ROTATIONX);
        } catch (Exception ex) {
            L.error("setRotationX() failed", ex);
        }
    }

    public static void setRotationXZ(float rotationXZ) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationXZ, ACTIVERENDERINFO_ROTATIONXZ);
        } catch (Exception ex) {
            L.error("setRotationXZ() failed", ex);
        }
    }

    public static void setRotationZ(float rotationZ) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationZ, ACTIVERENDERINFO_ROTATIONZ);
        } catch (Exception ex) {
            L.error("setRotationZ() failed", ex);
        }
    }

    public static void setRotationYZ(float rotationYZ) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationYZ, ACTIVERENDERINFO_ROTATIONYZ);
        } catch (Exception ex) {
            L.error("setRotationYZ() failed", ex);
        }
    }

    public static void setRotationXY(float rotationXY) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationXY, ACTIVERENDERINFO_ROTATIONXY);
        } catch (Exception ex) {
            L.error("setRotationXY() failed", ex);
        }
    }
}
