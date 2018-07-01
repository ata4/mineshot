/*
** 2016 Juni 19
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public interface PrivateAccessor {
    
    static final Logger L = LogManager.getLogger();
    
    static final String[] MINECRAFT_TIMER = new String[] {"timer", "field_71428_T"};
    static final String[] ACTIVERENDERINFO_ROTATIONX = new String[]{"rotationX", "field_74588_d"};
    static final String[] ACTIVERENDERINFO_ROTATIONZ = new String[]{"rotationZ", "field_74586_f"};
    static final String[] ACTIVERENDERINFO_ROTATIONYZ = new String[]{"rotationYZ", "field_74587_g"};
    static final String[] ACTIVERENDERINFO_ROTATIONXZ = new String[]{"rotationXZ", "field_74589_e"};
    static final String[] ACTIVERENDERINFO_ROTATIONXY = new String[]{"rotationXY", "field_74596_h"};
    
    default Timer getTimer(Minecraft mc) {
        try {
            return ReflectionHelper.getPrivateValue(Minecraft.class, mc, MINECRAFT_TIMER);
        } catch (Exception ex) {
            L.error("getTimer() failed", ex);
            return null;
        }
    }
    
    default void setRotationX(float rotationX) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationX, ACTIVERENDERINFO_ROTATIONX);
        } catch (Exception ex) {
            L.error("setRotationX() failed", ex);
        }
    }

    default void setRotationXZ(float rotationXZ) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationXZ, ACTIVERENDERINFO_ROTATIONXZ);
        } catch (Exception ex) {
            L.error("setRotationXZ() failed", ex);
        }
    }

    default void setRotationZ(float rotationZ) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationZ, ACTIVERENDERINFO_ROTATIONZ);
        } catch (Exception ex) {
            L.error("setRotationZ() failed", ex);
        }
    }

    default void setRotationYZ(float rotationYZ) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationYZ, ACTIVERENDERINFO_ROTATIONYZ);
        } catch (Exception ex) {
            L.error("setRotationYZ() failed", ex);
        }
    }

    default void setRotationXY(float rotationXY) {
        try {
            ReflectionHelper.setPrivateValue(ActiveRenderInfo.class, null, rotationXY, ACTIVERENDERINFO_ROTATIONXY);
        } catch (Exception ex) {
            L.error("setRotationXY() failed", ex);
        }
    }
}
