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
import info.ata4.minecraft.mineshot.client.wrapper.Projection;
import info.ata4.minecraft.mineshot.client.wrapper.ToggleableClippingHelper;
import info.ata4.minecraft.mineshot.util.reflection.PrivateAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

/**
 * Key handler for keys that control the orthographic camera.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class OrthoViewHandler implements PrivateAccessor {
    
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final String KEY_CATEGORY = "key.categories.mineshot";
    private static final float ZOOM_STEP = 0.5f;
    private static final float ROTATE_STEP = 15;
    private static final float ROTATE_SPEED = 4;
    private static final float SECONDS_PER_TICK = 1f/20f;
    
    private final KeyBinding keyToggle = new KeyBinding("key.mineshot.ortho.toggle", Keyboard.KEY_F7, KEY_CATEGORY);
    private final KeyBinding keyFree = new KeyBinding("key.mineshot.ortho.free", Keyboard.KEY_SEMICOLON, KEY_CATEGORY);
    private final KeyBinding keyClip = new KeyBinding("key.mineshot.ortho.clip", Keyboard.KEY_APOSTROPHE, KEY_CATEGORY);
    private final KeyBinding keyPreset = new KeyBinding("key.mineshot.ortho.preset", Keyboard.KEY_LBRACKET, KEY_CATEGORY);
    private final KeyBinding keyZoomIn = new KeyBinding("key.mineshot.ortho.zoom_in", Keyboard.KEY_RBRACKET, KEY_CATEGORY);
    private final KeyBinding keyZoomOut = new KeyBinding("key.mineshot.ortho.zoom_out", Keyboard.KEY_BACKSLASH, KEY_CATEGORY);
    private final KeyBinding keyRotateLeft = new KeyBinding("key.mineshot.ortho.rotate_left", Keyboard.KEY_LEFT, KEY_CATEGORY);
    private final KeyBinding keyRotateRight = new KeyBinding("key.mineshot.ortho.rotate_right", Keyboard.KEY_RIGHT, KEY_CATEGORY);
    private final KeyBinding keyRotateUp = new KeyBinding("key.mineshot.ortho.rotate_up", Keyboard.KEY_UP, KEY_CATEGORY);
    private final KeyBinding keyRotateDown = new KeyBinding("key.mineshot.ortho.rotate_down", Keyboard.KEY_DOWN, KEY_CATEGORY);
    
    private final ToggleableClippingHelper clippingHelper = ToggleableClippingHelper.getInstance();
    private boolean clippingEnabled;
    
    private boolean enabled;
    private boolean freeCam;
    private boolean clip;
    
    private float zoom;
    private float xRot;
    private float yRot;
    
    private int tick;
    private int tickPrevious;
    private int counter;
    private double partialPrevious;

    int[][] angles = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 0}, {3, 0}};
    
    public OrthoViewHandler() {
        ClientRegistry.registerKeyBinding(keyToggle);
        ClientRegistry.registerKeyBinding(keyZoomIn);
        ClientRegistry.registerKeyBinding(keyZoomOut);
        ClientRegistry.registerKeyBinding(keyRotateLeft);
        ClientRegistry.registerKeyBinding(keyRotateRight);
        ClientRegistry.registerKeyBinding(keyRotateUp);
        ClientRegistry.registerKeyBinding(keyRotateDown);
        ClientRegistry.registerKeyBinding(keyPreset);
        ClientRegistry.registerKeyBinding(keyClip);
        ClientRegistry.registerKeyBinding(keyFree);
        
        reset();
    }
 
    private void reset() {
        freeCam = false;
        clip = false;
        
        zoom = 8;
        xRot = 30;
        yRot = 315;
        tick = 0;
        tickPrevious = 0;
        counter = 0;
        partialPrevious = 0;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public void enable() {
        // disable in multiplayer
        // Of course, programmers could just delete this check and abuse the
        // orthographic camera, but at least the official build won't support it
        if (!MC.isSingleplayer()) {
            ChatUtils.print("mineshot.orthomp");
            return;
        }
        
        if (!enabled) {
            clippingEnabled = clippingHelper.isEnabled();
            clippingHelper.setEnabled(false);
            reset();
        }
        
        enabled = true;
    }
    
    public void disable() {
        if (enabled) {
            clippingHelper.setEnabled(clippingEnabled);
        }
        
        enabled = false;
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
        
        // change perspecives, using modifier key for opposite sides
        if (keyToggle.isPressed()) {
            toggle();
        } else if (keyFree.isPressed()) {
            freeCam = !freeCam;
        } else if (keyClip.isPressed()) {
            clip = !clip;
        } else if (keyPreset.isPressed()) {

            // snap to preset depending on current values, doesn't trigger if a preset is already set
            if (yRot / 90f - Math.floor(yRot / 90f) > 0 || xRot / 90f - Math.floor(xRot / 90f) > 0) {
                counter = Math.round(yRot / 90f) - 1 - 4 * (int) (Math.round(yRot / 90f) / 4f);
            }

            // change counter, direction depends on modifier key, includes checks to ensure array angles doesn't run out of bounds
            counter = mod ? counter - 1 + 6 * (int) (1 - counter / 6f) : counter + 1 - 6 * (int) (counter / 5f);
            xRot = angles[counter][0] * 90f;
            yRot = angles[counter][1] * 90f;
        }

        // update stepped rotation/zoom controls
        // note: the smooth controls are handled in onFogDensity, since they need
        // to be executed on every frame
        if (mod) {            
            updateZoomAndRotation(1);
            
            // snap values to step units
            xRot = Math.round(xRot / ROTATE_STEP) * ROTATE_STEP;
            yRot = Math.round(yRot / ROTATE_STEP) * ROTATE_STEP;
            zoom = Math.round(zoom / ZOOM_STEP) * ZOOM_STEP;
        }
    }
    
    private void updateZoomAndRotation(double multi) {
        if (keyZoomIn.isKeyDown()) {
            zoom *= 1 - ZOOM_STEP * multi;
        }
        if (keyZoomOut.isKeyDown()) {
            zoom *= 1 + ZOOM_STEP * multi;
        }
        
        if (keyRotateLeft.isKeyDown()) {
            yRot += ROTATE_STEP * multi;
        }
        if (keyRotateRight.isKeyDown()) {
            yRot -= ROTATE_STEP * multi;
        }

        if (keyRotateUp.isKeyDown()) {
            xRot += ROTATE_STEP * multi;
        }
        if (keyRotateDown.isKeyDown()) {
            xRot -= ROTATE_STEP * multi;
        }

        // don't let rotation exceed 360 degress or take negative values
        yRot -= 360f * (int) Math.floor(yRot / 360f);
        xRot -= 360f * (int) Math.floor(xRot / 360f);
    }
    
    @SubscribeEvent
    public void onTick(ClientTickEvent evt) {
        if (!enabled || evt.phase != Phase.START) {
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
            double partial = evt.getRenderPartialTicks();
            double elapsed = ticksElapsed + (partial - partialPrevious);
            elapsed *= SECONDS_PER_TICK * ROTATE_SPEED;
            updateZoomAndRotation(elapsed);
            
            tickPrevious = tick;
            partialPrevious = partial;
        }

        float width = zoom * (MC.displayWidth / (float) MC.displayHeight);
        float height = zoom;

        // override projection matrix
        GlStateManager.matrixMode(GL_PROJECTION);
        GlStateManager.loadIdentity();

        Projection.ortho(-width, width, -height, height, clip ? 0 : -9999, 9999);

        // rotate the orthographic camera with the player view
        if (freeCam) {
            xRot = MC.player.rotationPitch;
            yRot = MC.player.rotationYaw - 180;
        }
        
        // override camera view matrix
        GlStateManager.matrixMode(GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.rotate(xRot, 1, 0, 0);
        GlStateManager.rotate(yRot, 0, 1, 0);
        
        // fix particle rotation if the camera isn't following the player view
        if (!freeCam) {
            float pitch = xRot;
            float yaw = yRot + 180;
            setRotationX(MathHelper.cos(yaw * (float) Math.PI / 180f));
            setRotationZ(MathHelper.sin(yaw * (float) Math.PI / 180f));
            setRotationYZ(-ActiveRenderInfo.getRotationZ() * MathHelper.sin(pitch * (float) Math.PI / 180f));
            setRotationXY(ActiveRenderInfo.getRotationX() * MathHelper.sin(pitch * (float) Math.PI / 180f));
            setRotationXZ(MathHelper.cos(pitch * (float) Math.PI / 180f));
        }
    }

    @SubscribeEvent
    public void onTextRender(RenderGameOverlayEvent.Text textEvent)
    {
        if (textEvent.getType() != RenderGameOverlayEvent.ElementType.TEXT)
            return;

        // display various stats in debug menu, only active when the ortographic camera is
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.gameSettings.showDebugInfo && isEnabled())
        {
            textEvent.getRight().add("");
            textEvent.getRight().add("\u00A7nMineshot\u00A7r");
            textEvent.getRight().add("Zoom: " + String.format("%.3f", zoom));
            textEvent.getRight().add("Y-Rotation: " + String.format("%.3f", yRot));
            textEvent.getRight().add("X-Rotation: " + String.format("%.3f", xRot));
        }
    }
}
