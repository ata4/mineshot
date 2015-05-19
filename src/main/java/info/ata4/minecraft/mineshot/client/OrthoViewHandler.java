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

import info.ata4.minecraft.mineshot.client.util.ChatUtils;
import info.ata4.minecraft.mineshot.util.reflection.ClippingHelperAccessor;
import info.ata4.minecraft.mineshot.util.reflection.EntityRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

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
    private static final float SECONDS_PER_TICK = 1f/20f;
    
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
    
    private boolean enabled;
    private boolean freeCam;
    private boolean clip;
    
    private float zoom;
    private float xRot;
    private float yRot;
    
    private int tick;
    private int tickPrevious;
    private double partialPrevious;
    
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
        
        reset();
    }
 
    private void reset() {
        freeCam = false;
        clip = false;
        
        zoom = 8;
        xRot = 30;
        yRot = -45;
        tick = 0;
        tickPrevious = 0;
        partialPrevious = 0;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public void enable() {
        if (!enabled) {
            reset();
            
            // disable in multiplayer
            // Of course, programmers could just delete this check and abuse the
            // orthographic camera, but at least the official build won't support it
            if (!MC.isSingleplayer()) {
                ChatUtils.print("mineshot.orthomp");
                return;
            }

            ClippingHelperAccessor.setCullingEnabled(false);
        }
        
        enabled = true;
    }
    
    public void disable() {
        enabled = false;
        ClippingHelperAccessor.setCullingEnabled(true);
    }
    
    public void toggle() {
        if (isEnabled()) {
            disable();
        } else {
            enable();
        }
    }
    
    private boolean modifierKeyPressed() {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent evt) {
        boolean mod = modifierKeyPressed();
        
        if (keyToggle.isKeyDown()) {
            if (mod) {
                freeCam = !freeCam;
            } else {
                toggle();
            } 
        } else if (keyClip.isKeyDown()) {
            clip = !clip;
        } else if (keyRotateT.isKeyDown()) {
            xRot = mod ? -90 : 90;
            yRot = 0;
        } else if (keyRotateF.isKeyDown()) {
            xRot = 0;
            yRot = mod ? -90 : 90;
        } else if (keyRotateS.isKeyDown()) {
            xRot = 0;
            yRot = mod ? 180 : 0;
        }

        if (mod) {
            // snap values to step units
            xRot -= xRot % ROTATE_STEP;
            yRot -= yRot % ROTATE_STEP;
            zoom -= zoom % ZOOM_STEP;
            
            updateZoomAndRotation(1);
        }
    }
    
    private void updateZoomAndRotation(double multi) {
        if (keyZoomIn.isKeyDown()) {
            zoom *= 1 - ZOOM_STEP * multi;
        } else if (keyZoomOut.isKeyDown()) {
            zoom *= 1 + ZOOM_STEP * multi;
        }
        
        if (keyRotateL.isKeyDown()) {
            yRot += ROTATE_STEP * multi;
        } else if (keyRotateR.isKeyDown()) {
            yRot -= ROTATE_STEP * multi;
        }

        if (keyRotateU.isKeyDown()) {
            xRot += ROTATE_STEP * multi;
        } else if (keyRotateD.isKeyDown()) {
            xRot -= ROTATE_STEP * multi;
        }
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent evt) {
        if (!enabled) {
            return;
        }
        
        if (evt.phase != Phase.START) {
            return;
        }
        
        tick++;
    }
    
    @SubscribeEvent
    public void onFogDensity(EntityViewRenderEvent.FogDensity evt) {
        if (!enabled) {
            return;
        }
        
        // update zoom and rotation
        if (!modifierKeyPressed()) {
            int ticksElapsed = tick - tickPrevious;
            double elapsed = ticksElapsed + (evt.renderPartialTicks - partialPrevious);
            elapsed *= SECONDS_PER_TICK;
            updateZoomAndRotation(elapsed);
            
            tickPrevious = tick;
            partialPrevious = evt.renderPartialTicks;
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

        glOrtho(-width, width, -height, height, clip ? 0 : -9999, 9999);

        // rotate the orthographic camera with the player view
        if (freeCam) {
            xRot = MC.thePlayer.rotationPitch;
            yRot = MC.thePlayer.rotationYaw - 180;
        }
        
        // set camera rotation
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glRotatef(xRot, 1, 0, 0);
        glRotatef(yRot, 0, 1, 0);
    }
}
