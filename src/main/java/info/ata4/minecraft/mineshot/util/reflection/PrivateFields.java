/*
 ** 2014 July 28
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */

package info.ata4.minecraft.mineshot.util.reflection;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public interface PrivateFields {
    
    public static final String[] MINECRAFT_TIMER = new String[] {"timer", "field_71428_T"};
    public static final String[] ENTITYRENDERER_CAMERA_ZOOM = new String[] {"cameraZoom", "field_78503_V"};
    public static final String[] ENTITYRENDERER_CAMERA_YAW = new String[] {"cameraYaw", "field_78502_W"};
    public static final String[] ENTITYRENDERER_CAMERA_PITCH = new String[] {"cameraPitch", "field_78509_X"};
    public static final String[] CLIPPINGHELPERIMPL_INSTANCE = new String[] {"instance", "field_78563_e"};
    public static final String[] ACTIVERENDERINFO_ROTATIONX = new String[]{"rotationX", "field_74588_d"};
    public static final String[] ACTIVERENDERINFO_ROTATIONZ = new String[]{"rotationZ", "field_74586_f"};
    public static final String[] ACTIVERENDERINFO_ROTATIONYZ = new String[]{"rotationYZ", "field_74587_g"};
    public static final String[] ACTIVERENDERINFO_ROTATIONXZ = new String[]{"rotationXZ", "field_74589_e"};
    public static final String[] ACTIVERENDERINFO_ROTATIONXY = new String[]{"rotationXY", "field_74596_h"};
}
