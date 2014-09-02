/*
 ** 2013 April 15
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import info.ata4.minecraft.mineshot.client.util.ChatUtils;
import info.ata4.minecraft.mineshot.util.reflection.EntityRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;

import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

/**
 * Key handler for keys that control the orthographic camera.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class OrthoViewHandler {
    
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final String KEY_CATEGORY = "key.categories.mineshot";
    private static final float ZOOM_STEP = 0.5f;
    private static final float ROTATE_STEP = 15;
    private static final int CLIP_STEP = 4;
    private static final int UPDATE_STEPS_PER_SECOND = 2;
    
    private final KeyBinding keyToggle = new KeyBinding("key.mineshot.ortho.toggle", Keyboard.KEY_NUMPAD5, KEY_CATEGORY);
    private final KeyBinding keyZoomIn = new KeyBinding("key.mineshot.ortho.zoom_in", Keyboard.KEY_ADD, KEY_CATEGORY);
    private final KeyBinding keyZoomOut = new KeyBinding("key.mineshot.ortho.zoom_out", Keyboard.KEY_SUBTRACT, KEY_CATEGORY);
    private final KeyBinding keyRotateL = new KeyBinding("key.mineshot.ortho.rotate_l", Keyboard.KEY_NUMPAD4, KEY_CATEGORY);
    private final KeyBinding keyRotateR = new KeyBinding("key.mineshot.ortho.rotate_r", Keyboard.KEY_NUMPAD6, KEY_CATEGORY);
    private final KeyBinding keyRotateU = new KeyBinding("key.mineshot.ortho.rotate_u", Keyboard.KEY_NUMPAD8, KEY_CATEGORY);
    private final KeyBinding keyRotateD = new KeyBinding("key.mineshot.ortho.rotate_d", Keyboard.KEY_NUMPAD2, KEY_CATEGORY);
    private final KeyBinding keyRotateT = new KeyBinding("key.mineshot.ortho.rotate_t", Keyboard.KEY_NUMPAD7, KEY_CATEGORY);
    private final KeyBinding keyRotateF = new KeyBinding("key.mineshot.ortho.rotate_f", Keyboard.KEY_NUMPAD1, KEY_CATEGORY);
    private final KeyBinding keyRotateS = new KeyBinding("key.mineshot.ortho.rotate_s", Keyboard.KEY_NUMPAD3, KEY_CATEGORY);
    private final KeyBinding keyClip = new KeyBinding("key.mineshot.ortho.clip", Keyboard.KEY_MULTIPLY, KEY_CATEGORY);
    
    private boolean enable;
    private boolean freeCam;
    private float zoom = 8;
    private float clip = 512;
    private float xRot = 30;
    private float yRot = -45;
    
    private long lastframe = 0;
    
    public OrthoViewHandler() {
        ClientRegistry.registerKeyBinding(keyToggle);
        ClientRegistry.registerKeyBinding(keyZoomIn);
        ClientRegistry.registerKeyBinding(keyZoomOut);
        ClientRegistry.registerKeyBinding(keyRotateL);
        ClientRegistry.registerKeyBinding(keyRotateR);
        ClientRegistry.registerKeyBinding(keyRotateU);
        ClientRegistry.registerKeyBinding(keyRotateD);
        ClientRegistry.registerKeyBinding(keyRotateT);
        ClientRegistry.registerKeyBinding(keyRotateF);
        ClientRegistry.registerKeyBinding(keyRotateS);
        ClientRegistry.registerKeyBinding(keyClip);
    }
    
    @SubscribeEvent
    public void renderWorldLastEvent(RenderTickEvent evt) {
        if (evt.phase != RenderTickEvent.Phase.START) {
            return;
        }
        
        double elapsed = getElapsedTime() * UPDATE_STEPS_PER_SECOND * 0.001;
        if (!modifierKeyPressed()) {
            updateZoomAndRotation(elapsed);
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent evt) {
        boolean mod = modifierKeyPressed();
        
        if (keyToggle.getIsKeyPressed()) {
            if (mod) {
                freeCam = !freeCam;
            } else {
                enable = !enable;
                if (enable) {
                    xRot = 30;
                    yRot = -45;
                    zoom = 8;
                    clip = 512;
                }
            } 
        } else if (keyRotateT.getIsKeyPressed()) {
            xRot = mod ? -90 : 90;
            yRot = 0;
        } else if (keyRotateF.getIsKeyPressed()) {
            xRot = 0;
            yRot = mod ? -90 : 90;
        } else if (keyRotateS.getIsKeyPressed()) {
            xRot = 0;
            yRot = mod ? 180 : 0;
        } else if (keyClip.getIsKeyPressed()) {
            clip += mod ? CLIP_STEP : -CLIP_STEP;
            if (clip <= 0) {
                clip = CLIP_STEP;
            }
        }

        if (mod) {
            // snap values to step units
            xRot -= xRot % ROTATE_STEP;
            yRot -= yRot % ROTATE_STEP;
            zoom -= zoom % ZOOM_STEP;
            
            updateZoomAndRotation(1);
        }
    }
    
    private boolean modifierKeyPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }

    private long getElapsedTime() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastframe;
        lastframe = now;
        if (elapsed > 2000) {
            return 0;
        }
        return elapsed;
    }
    
    private void updateZoomAndRotation(double multi) {
        if (keyZoomIn.getIsKeyPressed()) {
            zoom *= 1 - ZOOM_STEP * multi;
        }
        if (keyZoomOut.getIsKeyPressed()) {
            zoom *= 1 + ZOOM_STEP * multi;
        }
        if (keyRotateL.getIsKeyPressed()) {
            yRot += ROTATE_STEP * multi;
        }
        if (keyRotateR.getIsKeyPressed()) {
            yRot -= ROTATE_STEP * multi;
        }
        if (keyRotateU.getIsKeyPressed()) {
            xRot += ROTATE_STEP * multi;
        }
        if (keyRotateD.getIsKeyPressed()) {
            xRot -= ROTATE_STEP * multi;
        }
    }
    
    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity evt) {
        if (!enable) {
            return;
        }
        
        // disable in multiplayer
        // Of course, programmers could just delete this check and abuse the
        // orthographic camera, but at least the official build won't support it
        if (!MC.isSingleplayer()) {
            enable = false;
            ChatUtils.print("mineshot.orthomp");
            return;
        }

        float width = zoom * (MC.displayWidth / (float) MC.displayHeight);
        float height = zoom;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        double cameraZoom = EntityRendererAccessor.getCameraZoom(MC.entityRenderer);
        double cameraOfsX = EntityRendererAccessor.getCameraOffsetX(MC.entityRenderer);
        double cameraOfsY = EntityRendererAccessor.getCameraOffsetY(MC.entityRenderer);

        if (cameraZoom != 1) {
            glTranslated(cameraOfsX, -cameraOfsY, 0);
            glScaled(cameraZoom, cameraZoom, 1);
        }

        glOrtho(-width, width, -height, height, -clip, clip);

        if (freeCam) {
            // rotate the orthographic camera with the player view
            xRot = MC.thePlayer.rotationPitch;
            yRot = MC.thePlayer.rotationYaw - 180;
        }

        // set camera rotation
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(xRot, 1, 0, 0);
        glRotatef(yRot, 0, 1, 0);

        // fix particle rotation
        if (!freeCam) {
            float pitch = xRot;
            float yaw = yRot + 180;
            ActiveRenderInfo.rotationX = MathHelper.cos(yaw * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationZ = MathHelper.sin(yaw * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationYZ = -ActiveRenderInfo.rotationZ * MathHelper.sin(pitch * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationXY = ActiveRenderInfo.rotationX * MathHelper.sin(pitch * (float) Math.PI / 180f);
            ActiveRenderInfo.rotationXZ = MathHelper.cos(pitch * (float) Math.PI / 180f);
        }
    }
}
