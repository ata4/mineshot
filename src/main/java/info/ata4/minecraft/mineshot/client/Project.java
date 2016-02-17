/*
** 2016 February 15
**
** The author disclaims copyright to this source code. In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class Project {
    
    public static double offsetX;
    public static double offsetY;
    public static double zoom = 1;
    
    public static void transform() {
        GL11.glTranslated(offsetX, -offsetY, 0);
        GL11.glScaled(zoom, zoom, 1);
    }
    
    public static void glOrtho(double left, double right, double bottom, double top, double zNear, double zFar) {
        transform();
        GL11.glOrtho(left, right, bottom, top, zNear, zFar);
    }
    
    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        transform();
        org.lwjgl.util.glu.Project.gluPerspective(fovy, aspect, zNear, zFar);
    }

    private Project() {
    }
}
