package info.ata4.minecraft.mineshot.client.gui;

import info.ata4.minecraft.mineshot.client.OrthoViewHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.*;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GuiCamera extends GuiScreen implements GuiResponder {

    private static final float ZOOM_STEP = 0.5f;
    private static final float ZOOM_MIN = 0.5f;
    private static final float ZOOM_MAX = 512f;
    private static final float ROTATE_STEP = 15f;

    private GuiNumberTextField textZoom;
    private GuiNumberTextField textXRot;
    private GuiNumberTextField textYRot;
    private GuiSlider sliderZoom;
    private GuiSlider sliderXRot;
    private GuiSlider sliderYRot;
    private GuiButton buttonSlider;
    private GuiButton buttonText;
    private GuiIconButton buttonPlus1;
    private GuiIconButton buttonPlus2;
    private GuiIconButton buttonPlus3;
    private GuiIconButton buttonMinus1;
    private GuiIconButton buttonMinus2;
    private GuiIconButton buttonMinus3;

    private GuiScreen old;
    private OrthoViewHandler ovh;

    private final DecimalFormat valueDisplay = new DecimalFormat("0.000");

    private boolean freeCamUpdated;
    private final boolean freeCam;
    private boolean clipUpdated;
    private final boolean clip;
    private float zoomUpdated;
    private final float zoom;
    private float xRotUpdated;
    private final float xRot;
    private float yRotUpdated;
    private final float yRot;

    private boolean textIsActive;
    private boolean wasTextZoomFocused = false;
    private boolean wasTextXRotFocused = false;
    private boolean wasTextYRotFocused = false;

    //To-do
    //fix layout of the gui
    //implement functional modifier key for sliders ?
    //changeable slider range
    //single setting mode with clear view and slider / text box on the top ?

    public GuiCamera(OrthoViewHandler ovh, GuiScreen old, float zoom, float xRot, float yRot, boolean freeCam, boolean clip, boolean textIsActive) {
        this.ovh = ovh;
        this.old = old;
        this.zoom = zoom;
        this.xRot = xRot;
        this.yRot = yRot;
        this.zoomUpdated = zoom;
        this.xRotUpdated = xRot;
        this.yRotUpdated = yRot;
        this.freeCamUpdated = freeCam;
        this.freeCam = freeCam;
        this.clipUpdated = clip;
        this.clip = clip;
        this.textIsActive = textIsActive;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    @Override
    public void initGui() {
        valueDisplay.setRoundingMode(RoundingMode.HALF_UP);

        GuiButton buttonCancel = new GuiButton(0, width/2-100, height/6+160, 98, 20, I18n.format("gui.cancel"));
        buttonList.add(buttonCancel);
        GuiButton buttonDone = new GuiButton(1, width/2+2, height/6+160, 98, 20, I18n.format("gui.done"));
        buttonList.add(buttonDone);
        buttonSlider = new GuiButton(2, width/2-100, height/6+20, 98, 20, I18n.format("mineshot.gui.sliders"));
        buttonList.add(buttonSlider);
        buttonText = new GuiButton(3, width/2+2, height/6+20, 98, 20, I18n.format("mineshot.gui.text"));
        buttonList.add(buttonText);
        GuiButton buttonFreeCam = new GuiButton(4, width/2-124, height/6+130, 123, 20, getButtonText(4, freeCam ? 1 : 0));
        buttonList.add(buttonFreeCam);
        GuiButton buttonClip = new GuiButton(5, width/2+2, height/6+130, 123, 20, getButtonText(5, clip ? 1 : 0));
        buttonList.add(buttonClip);

        buttonPlus1 = new GuiIconButton(20, width/2+105, height/6+50, -1, true);
        buttonList.add(buttonPlus1);
        buttonPlus2 = new GuiIconButton(22, width/2+105, height/6+75, -1, true);
        buttonList.add(buttonPlus2);
        buttonPlus3 = new GuiIconButton(24, width/2+105, height/6+100, -1, true);
        buttonList.add(buttonPlus3);
        buttonMinus1 = new GuiIconButton(21, width/2+105, height/6+60, -2, false);
        buttonList.add(buttonMinus1);
        buttonMinus2 = new GuiIconButton(23, width/2+105, height/6+85, -2, false);
        buttonList.add(buttonMinus2);
        buttonMinus3 = new GuiIconButton(25, width/2+105, height/6+110, -2, false);
        buttonList.add(buttonMinus3);

        sliderZoom = new GuiSlider(this, 10, width/2-125, height/6+50, I18n.format("mineshot.gui.zoom"), ZOOM_MIN, ZOOM_MAX, zoomUpdated, (id, name, value) -> {
            zoomUpdated = value;
            //ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated, freeCamUpdated, clipUpdated, textIsActive);
            return name+": " + valueDisplay.format(zoomUpdated);
        });
        sliderZoom.width = 250;
        buttonList.add(sliderZoom);
        sliderXRot = new GuiSlider(this, 11, width/2-125, height/6+75, I18n.format("mineshot.gui.xrot"), 0f, 359.999f, xRotUpdated, (id, name, value) -> {
            xRotUpdated = value;
            //ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated, freeCamUpdated, clipUpdated, textIsActive);
            return name+": " + valueDisplay.format(xRotUpdated);
        });
        sliderXRot.width = 250;
        buttonList.add(sliderXRot);
        sliderYRot = new GuiSlider(this, 12, width/2-125, height/6+100, I18n.format("mineshot.gui.yrot"), 0f, 359.999f, yRotUpdated, (id, name, value) -> {
            yRotUpdated = value;
            //ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated, freeCamUpdated, clipUpdated, textIsActive);
            return name+": " + valueDisplay.format(yRotUpdated);
        });
        sliderYRot.width = 250;
        buttonList.add(sliderYRot);

        textZoom = new GuiNumberTextField(13, mc.fontRenderer, width/2-25, height/6+50, 129, 20);
        textXRot = new GuiNumberTextField(14, mc.fontRenderer, width/2-25, height/6+75, 129, 20);
        textYRot = new GuiNumberTextField(15, mc.fontRenderer, width/2-25, height/6+100, 129, 20);
        textZoom.setMaxStringLength(7);
        textXRot.setMaxStringLength(7);
        textYRot.setMaxStringLength(7);
        textZoom.setText(valueDisplay.format(zoomUpdated));
        textXRot.setText(valueDisplay.format(xRotUpdated));
        textYRot.setText(valueDisplay.format(yRotUpdated));

        switchDisplay(false);
        toggleRotation();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRenderer, I18n.format("mineshot.gui.title"), width/2, height/6, -1);

        if (textIsActive) {
            drawCenteredString(mc.fontRenderer, getButtonText(0, 0), width / 2 - 75, height / 6 + 55, -1);
            drawCenteredString(mc.fontRenderer, getButtonText(0, 1), width / 2 - 75, height / 6 + 80, -1);
            drawCenteredString(mc.fontRenderer, getButtonText(1, 0), width / 2 - 75, height / 6 + 105, -1);
            textZoom.drawTextBox();
            textXRot.drawTextBox();
            textYRot.drawTextBox();
        }
    }

    /**
     * Switches from sliders to text boxes. Updates display status of all components involved.
     */
    private void switchDisplay(boolean updateBool) {
        if (updateBool) { // is only false during initGui
            textIsActive = !textIsActive;
        }
        buttonSlider.enabled = textIsActive;
        buttonText.enabled = !textIsActive;
        sliderZoom.visible = !textIsActive;
        sliderXRot.visible = !textIsActive;
        sliderYRot.visible = !textIsActive;
        buttonPlus1.visible = textIsActive;
        buttonPlus2.visible = textIsActive;
        buttonPlus3.visible = textIsActive;
        buttonMinus1.visible = textIsActive;
        buttonMinus2.visible = textIsActive;
        buttonMinus3.visible = textIsActive;
        if (textIsActive) {
            textZoom.setText(valueDisplay.format(zoomUpdated));
            textXRot.setText(valueDisplay.format(xRotUpdated));
            textYRot.setText(valueDisplay.format(yRotUpdated));
        } else {
            sliderZoom.setSliderValue(zoomUpdated, false);
            sliderXRot.setSliderValue(xRotUpdated, false);
            sliderYRot.setSliderValue(yRotUpdated, false);
        }
    }

    /**
     * Called when freecam is toggled. Prevents modifying rotation since it's controlled by player view now.
     */
    private void toggleRotation() {
        textXRot.setEnabled(!freeCamUpdated);
        textYRot.setEnabled(!freeCamUpdated);
        sliderXRot.enabled = !freeCamUpdated;
        sliderYRot.enabled = !freeCamUpdated;
        buttonPlus2.enabled = !freeCamUpdated;
        buttonMinus2.enabled = !freeCamUpdated;
        buttonPlus3.enabled = !freeCamUpdated;
        buttonMinus3.enabled = !freeCamUpdated;
    }

    /**
     * Generate strings for buttons that can be toggled. Strings still need to be applied somewhere else. Current cases 0 to 2 are
     * out of order since drawString doesn't have an id.
     */
    private String getButtonText(int buttonId, int textId) {
        ITextComponent itextcomponent = new TextComponentString("");
        switch (buttonId * 2 + textId) {
            case 0:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.zoom"));
                itextcomponent.appendText(":");
                break;
            case 1:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.xrot"));
                itextcomponent.appendText(":");
                break;
            case 2:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.yrot"));
                itextcomponent.appendText(":");
                break;
            case 8:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.view"));
                itextcomponent.appendText(": ");
                itextcomponent.appendSibling(new TextComponentTranslation("options.off"));
                break;
            case 9:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.view"));
                itextcomponent.appendText(": ");
                itextcomponent.appendSibling(new TextComponentTranslation("options.on"));
                break;
            case 10:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.clipping"));
                itextcomponent.appendText(": ");
                itextcomponent.appendSibling(new TextComponentTranslation("options.off"));
                break;
            case 11:
                itextcomponent.appendSibling(new TextComponentTranslation("mineshot.gui.clipping"));
                itextcomponent.appendText(": ");
                itextcomponent.appendSibling(new TextComponentTranslation("options.on"));
                break;
            default:
                itextcomponent.appendText("missingno");
        }
        return itextcomponent.getFormattedText();

    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: // cancel
                ovh.updateFromGui(zoom, xRot, yRot, freeCam, clip, textIsActive);
                mc.displayGuiScreen(old);
                break;
            case 1: // done
                saveTextBoxContents();
                ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated, freeCamUpdated, clipUpdated, textIsActive);
                mc.displayGuiScreen(old);
                break;
            case 2: // sliders
                saveTextBoxContents();
                switchDisplay(true);
                break;
            case 3: // text
                switchDisplay(true);
                break;
            case 4: // free
                freeCamUpdated = !freeCamUpdated;
                buttonList.get(4).displayString = getButtonText(4, freeCamUpdated ? 1 : 0);
                toggleRotation();
                if (freeCamUpdated) {
                    xRotUpdated = ovh.fixValue(mc.player.rotationPitch);
                    yRotUpdated = ovh.fixValue(mc.player.rotationYaw - 180f);
                    sliderXRot.setSliderValue(xRotUpdated, false);
                    sliderYRot.setSliderValue(yRotUpdated, false);
                    textXRot.setText(valueDisplay.format(xRotUpdated));
                    textYRot.setText(valueDisplay.format(yRotUpdated));
                }
                break;
            case 5: // clipping
                clipUpdated = !clipUpdated;
                buttonList.get(5).displayString = getButtonText(5, clipUpdated ? 1 : 0);
                break;
            case 20: // plus zoom
                zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated), ZOOM_MIN, ZOOM_MAX);
                textZoom.setText(valueDisplay.format(adjustZoomFromButtons(zoomUpdated, true)));
                break;
            case 21: // minus zoom
                zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated), ZOOM_MIN, ZOOM_MAX);
                textZoom.setText(valueDisplay.format(adjustZoomFromButtons(zoomUpdated, false)));
                break;
            case 22: // plus xRot
                xRotUpdated = ovh.fixValue(textXRot.getTextAsFloat(xRotUpdated) + (ovh.modifierKeyPressed() ? ROTATE_STEP : 1f));
                textXRot.setText(valueDisplay.format(xRotUpdated));
                break;
            case 23: // minus xRot
                xRotUpdated = ovh.fixValue(textXRot.getTextAsFloat(xRotUpdated) - (ovh.modifierKeyPressed() ? ROTATE_STEP : 1f));
                textXRot.setText(valueDisplay.format(xRotUpdated));
                break;
            case 24: // plus yRot
                yRotUpdated = ovh.fixValue(textYRot.getTextAsFloat(yRotUpdated) + (ovh.modifierKeyPressed() ? ROTATE_STEP : 1f));
                textYRot.setText(valueDisplay.format(yRotUpdated));
                break;
            case 25: // minus yRot
                yRotUpdated = ovh.fixValue(textYRot.getTextAsFloat(yRotUpdated) - (ovh.modifierKeyPressed() ? ROTATE_STEP : 1f));
                textYRot.setText(valueDisplay.format(yRotUpdated));
                break;
        }
    }

    /**
     * Allows for dynamic zoom steps when modifier key is pressed exactly like without gui.
     */
    private float adjustZoomFromButtons(float zoomVal, boolean isModeAdd) {
        if (ovh.modifierKeyPressed()) {
            zoomVal *= isModeAdd ? (1 + ZOOM_STEP) : (1 - ZOOM_STEP);
            zoomVal = Math.round(zoomVal / ZOOM_STEP) * ZOOM_STEP;
        } else {
            zoomVal = isModeAdd ? (zoomVal + 1f) : (zoomVal - 1f);
        }
        zoomVal = ovh.fixValue(zoomVal, ZOOM_MIN, ZOOM_MAX);
        // Disables plus or minus buttons if clicking them wouldn't be able to change the current value.
        buttonPlus1.enabled = (ZOOM_MAX != zoomVal);
        buttonMinus1.enabled = (ZOOM_MIN != zoomVal);
        return zoomVal;
    }

    /**
     * Updates the contents of the text boxes when they loose focus.
     */
    private void updateUnfocusedTextBoxes() {
        if (!textZoom.isFocused() && wasTextZoomFocused) {
            zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated), ZOOM_MIN, ZOOM_MAX);
            textZoom.setText(valueDisplay.format(zoomUpdated));
            buttonPlus1.enabled = (ZOOM_MAX != zoomUpdated);
            buttonMinus1.enabled = (ZOOM_MIN != zoomUpdated);
        } else if (!textXRot.isFocused() && wasTextXRotFocused) {
            xRotUpdated = ovh.fixValue(textXRot.getTextAsFloat(xRotUpdated));
            textXRot.setText(valueDisplay.format(xRotUpdated));
        } else if (!textYRot.isFocused() && wasTextYRotFocused) {
            yRotUpdated = ovh.fixValue(textYRot.getTextAsFloat(yRotUpdated));
            textYRot.setText(valueDisplay.format(yRotUpdated));
        }
        wasTextZoomFocused = textZoom.isFocused();
        wasTextXRotFocused = textXRot.isFocused();
        wasTextYRotFocused = textYRot.isFocused();
    }

    /**
     * Save contents of the text boxes to variables, similar to updateUnfocusedTextBoxes(), but intended to be used for
     * actions that remove the text boxes since updateUnfocusedTextBoxes() doesn't work then.
     */
    private void saveTextBoxContents() {
        zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated), ZOOM_MIN, ZOOM_MAX);
        xRotUpdated = ovh.fixValue(textXRot.getTextAsFloat(xRotUpdated));
        yRotUpdated = ovh.fixValue(textYRot.getTextAsFloat(yRotUpdated));
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (textIsActive) {
            textZoom.updateCursorCounter();
            if (!freeCamUpdated) {
                textXRot.updateCursorCounter();
                textYRot.updateCursorCounter();
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (textIsActive) {
            textZoom.textboxKeyTyped(typedChar, keyCode);
            if (!freeCamUpdated) {
                textXRot.textboxKeyTyped(typedChar, keyCode);
                textYRot.textboxKeyTyped(typedChar, keyCode);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            ovh.updateFromGui(zoom, xRot, yRot, freeCam, clip, textIsActive);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && textIsActive) {
            textZoom.setFocused(false);
            if (!freeCamUpdated) {
                textXRot.setFocused(false);
                textYRot.setFocused(false);
            }
            updateUnfocusedTextBoxes();
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (textIsActive) {
            textZoom.mouseClicked(mouseX, mouseY, mouseButton);
            if (!freeCamUpdated) {
                textXRot.mouseClicked(mouseX, mouseY, mouseButton);
                textYRot.mouseClicked(mouseX, mouseY, mouseButton);
            }
            updateUnfocusedTextBoxes();
        }
    }

    @Override
    public void setEntryValue(int id, float value) {
    }

    @Override
    public void setEntryValue(int id, boolean value) {
    }

    @Override
    public void setEntryValue(int id, String value) {
    }
}