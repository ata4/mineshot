/*
 ** 2012 March 9
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.EntityRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class to access some of EntityRenderer's private fields.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class EntityRendererAccessor {

    private static final Logger L = LogManager.getLogger();

    private static final String[] FIELD_CAMERA_ZOOM = new String[] {"cameraZoom", "field_78503_V"};
    private static final String[] FIELD_CAMERA_YAW = new String[] {"cameraYaw", "field_78502_W"};
    private static final String[] FIELD_CAMERA_PITCH = new String[] {"cameraPitch", "field_78509_X"};
    
    private EntityRendererAccessor() {
    }

    public static void setCameraZoom(EntityRenderer renderer, double zoom) {
        try {
            ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, zoom, FIELD_CAMERA_ZOOM);
        } catch (Exception ex) {
            L.error("setCameraZoom() failed", ex);
        }
    }

    public static double getCameraZoom(EntityRenderer renderer) {
        try {
            return ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, FIELD_CAMERA_ZOOM);
        } catch (Exception ex) {
            L.error("getCameraZoom() failed", ex);
            return 0;
        }
    }

    public static void setCameraOffsetX(EntityRenderer renderer, double offset) {
        try {
            ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, offset, FIELD_CAMERA_YAW);
        } catch (Exception ex) {
            L.error("setCameraOffsetX() failed", ex);
        }
    }

    public static double getCameraOffsetX(EntityRenderer renderer) {
        try {
            return ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, FIELD_CAMERA_YAW);
        } catch (Exception ex) {
            L.error("getCameraOffsetX() failed", ex);
            return 0;
        }
    }

    public static void setCameraOffsetY(EntityRenderer renderer, double offset) {
        try {
            ReflectionHelper.setPrivateValue(EntityRenderer.class, renderer, offset, FIELD_CAMERA_PITCH);
        } catch (Exception ex) {
            L.error("setCameraOffsetY() failed", ex);
        }
    }

    public static double getCameraOffsetY(EntityRenderer renderer) {
        try {
            return ReflectionHelper.getPrivateValue(EntityRenderer.class, renderer, FIELD_CAMERA_PITCH);
        } catch (Exception ex) {
            L.error("getCameraOffsetY() failed", ex);
            return 0;
        }
    }
}
