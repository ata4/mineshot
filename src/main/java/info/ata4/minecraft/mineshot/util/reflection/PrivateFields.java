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
}
