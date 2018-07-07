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
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class GuiCamera extends GuiScreen implements GuiResponder {

    private static final float ZOOM_DEFAULT = 8f;
    private static final float ZOOM_MIN = 0.5f;
    private static final float ZOOM_MAX = 512f;
    private static final float XROT_DEFAULT = 30f;
    private static final float YROT_DEFAULT = 315f;

    private GuiNumberTextField textZoom;
    private GuiNumberTextField textXRot;
    private GuiNumberTextField textYRot;
    private GuiSlider sliderZoom;
    private GuiSlider sliderXRot;
    private GuiSlider sliderYRot;
    private GuiButton buttonCancel;
    private GuiButton buttonSlider;
    private GuiButton buttonText;
    private GuiIconButton buttonTextPlus1;
    private GuiIconButton buttonTextPlus2;
    private GuiIconButton buttonTextPlus3;
    private GuiIconButton buttonTextMinus1;
    private GuiIconButton buttonTextMinus2;
    private GuiIconButton buttonTextMinus3;
    private GuiIconButton buttonSliderPlus1;
    private GuiIconButton buttonSliderPlus2;
    private GuiIconButton buttonSliderPlus3;
    private GuiIconButton buttonSliderMinus1;
    private GuiIconButton buttonSliderMinus2;
    private GuiIconButton buttonSliderMinus3;
    private GuiIconButton buttonSliderFocus1;
    private GuiIconButton buttonSliderFocus2;
    private GuiIconButton buttonSliderFocus3;
    private GuiIconButton buttonTextFocus1;
    private GuiIconButton buttonTextFocus2;
    private GuiIconButton buttonTextFocus3;
    private GuiIconButton buttonReset1;
    private GuiIconButton buttonReset2;
    private GuiIconButton buttonReset3;

    private GuiScreen old;
    private OrthoViewHandler ovh;

    private final DecimalFormat valueDisplay = new DecimalFormat("0.000");
    private final DecimalFormat incrementerDisplay = new DecimalFormat("0.###");

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
    private boolean focusIsActive;
    private boolean mouseNotOverButton;
    private boolean wasTextZoomFocused;
    private boolean wasTextXRotFocused;
    private boolean wasTextYRotFocused;

    private final int[] buttonsSliderView = {10, 11, 12, 30, 31, 32, 33, 34, 35, 36, 37, 38};
    private final int[] buttonsTextView = {20, 21, 22, 23, 24, 25, 26, 27, 28};
    private final int[] buttonsGeneralUI = {0, 1, 2, 3, 4, 5};
    private final int[] buttonsFreeCam = {11, 12, 22, 23, 24, 25, 27, 28, 32, 33, 34, 35, 37, 38};

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
        buttonCancel = new GuiButton(0, width/2-103, height/6+160, 103, 20, I18n.format("gui.cancel")); //for whatever eason width needs to be -99 and not -100
        buttonList.add(buttonCancel);
        GuiButton buttonDone = new GuiButton(1, width/2+2, height/6+160, 103, 20, I18n.format("gui.done"));
        buttonList.add(buttonDone);
        buttonSlider = new GuiButton(2, width/2-135, height/6-20, 50, 20, "<<");
        buttonList.add(buttonSlider);
        buttonText = new GuiButton(3, width/2+86, height/6-20, 50, 20, ">>");
        buttonList.add(buttonText);
        GuiButton buttonFreeCam = new GuiButton(4, width/2-155, height/6+100, 154, 20, getButtonText(4, freeCam ? 1 : 0));
        buttonList.add(buttonFreeCam);
        GuiButton buttonClip = new GuiButton(5, width/2+2, height/6+100, 154, 20, getButtonText(5, clip ? 1 : 0));
        buttonList.add(buttonClip);

        buttonTextPlus1 = new GuiIconButton(20, width/2+136, height/6+20, new int[] {-1}, true);
        buttonList.add(buttonTextPlus1);
        buttonTextPlus2 = new GuiIconButton(22, width/2+136, height/6+45, new int[] {-1}, true);
        buttonList.add(buttonTextPlus2);
        buttonTextPlus3 = new GuiIconButton(24, width/2+136, height/6+70, new int[] {-1}, true);
        buttonList.add(buttonTextPlus3);
        buttonTextMinus1 = new GuiIconButton(21, width/2+136, height/6+30, new int[] {-2}, false);
        buttonList.add(buttonTextMinus1);
        buttonTextMinus2 = new GuiIconButton(23, width/2+136, height/6+55, new int[] {-2}, false);
        buttonList.add(buttonTextMinus2);
        buttonTextMinus3 = new GuiIconButton(25, width/2+136, height/6+80, new int[] {-2}, false);
        buttonList.add(buttonTextMinus3);

        buttonSliderPlus1 = new GuiIconButton(30, width/2+136, height/6+20, new int[] {4}, false);
        buttonList.add(buttonSliderPlus1);
        buttonSliderPlus2 = new GuiIconButton(32, width/2+136, height/6+45, new int[] {4}, false);
        buttonList.add(buttonSliderPlus2);
        buttonSliderPlus3 = new GuiIconButton(34, width/2+136, height/6+70, new int[] {4}, false);
        buttonList.add(buttonSliderPlus3);
        buttonSliderMinus1 = new GuiIconButton(31, width/2-155, height/6+20, new int[] {3}, false);
        buttonList.add(buttonSliderMinus1);
        buttonSliderMinus2 = new GuiIconButton(33, width/2-155, height/6+45, new int[] {3}, false);
        buttonList.add(buttonSliderMinus2);
        buttonSliderMinus3 = new GuiIconButton(35, width/2-155, height/6+70, new int[] {3}, false);
        buttonList.add(buttonSliderMinus3);

        buttonTextFocus1 = new GuiIconButton(26, width/2+159, height/6+20, new int[] {5, 6}, false);
        buttonList.add(buttonTextFocus1);
        buttonTextFocus2 = new GuiIconButton(27, width/2+159, height/6+45, new int[] {5, 6}, false);
        buttonList.add(buttonTextFocus2);
        buttonTextFocus3 = new GuiIconButton(28, width/2+159, height/6+70, new int[] {5, 6}, false);
        buttonList.add(buttonTextFocus3);
        buttonSliderFocus1 = new GuiIconButton(36, width/2+159, height/6+20, new int[] {5, 6}, false);
        buttonList.add(buttonSliderFocus1);
        buttonSliderFocus2 = new GuiIconButton(37, width/2+159, height/6+45, new int[] {5, 6}, false);
        buttonList.add(buttonSliderFocus2);
        buttonSliderFocus3 = new GuiIconButton(38, width/2+159, height/6+70, new int[] {5, 6}, false);
        buttonList.add(buttonSliderFocus3);

        buttonReset1 = new GuiIconButton(13, width/2-48, height/6+20, new int[] {7}, false);
        buttonList.add(buttonReset1);
        buttonReset2 = new GuiIconButton(14, width/2-48, height/6+45, new int[] {7}, false);
        buttonList.add(buttonReset2);
        buttonReset3 = new GuiIconButton(15, width/2-48, height/6+70, new int[] {7}, false);
        buttonList.add(buttonReset3);

        sliderZoom = new GuiSlider(this, 10, width/2-135, height/6+20, I18n.format("mineshot.gui.zoom"), ZOOM_MIN, ZOOM_MAX, zoomUpdated, (id, name, value) -> {
            zoomUpdated = value;
            checkZoomButtonsEnabled();
            ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated);
            return name+": " + valueDisplay.format(zoomUpdated);
        });
        sliderZoom.width = 271;
        buttonList.add(sliderZoom);
        sliderXRot = new GuiSlider(this, 11, width/2-135, height/6+45, I18n.format("mineshot.gui.xrot"), 0f, 359.999f, xRotUpdated, (id, name, value) -> {
            xRotUpdated = value;
            ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated);
            return name+": " + valueDisplay.format(xRotUpdated);
        });
        sliderXRot.width = 271;
        buttonList.add(sliderXRot);
        sliderYRot = new GuiSlider(this, 12, width/2-135, height/6+70, I18n.format("mineshot.gui.yrot"), 0f, 359.999f, yRotUpdated, (id, name, value) -> {
            yRotUpdated = value;
            ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated);
            return name+": " + valueDisplay.format(yRotUpdated);
        });
        sliderYRot.width = 271;
        buttonList.add(sliderYRot);

        textZoom = new GuiNumberTextField(0, mc.fontRenderer, width/2-25, height/6+20, 160, 20);
        textXRot = new GuiNumberTextField(1, mc.fontRenderer, width/2-25, height/6+45, 160, 20);
        textYRot = new GuiNumberTextField(2, mc.fontRenderer, width/2-25, height/6+70, 160, 20);
        textZoom.setMaxStringLength(7);
        textXRot.setMaxStringLength(7);
        textYRot.setMaxStringLength(7);
        textZoom.setText(valueDisplay.format(zoomUpdated));
        textXRot.setText(valueDisplay.format(xRotUpdated));
        textYRot.setText(valueDisplay.format(yRotUpdated));

        switchDisplay(false); // in case text boxes were active when the gui was last open
        toggleRotation(); // deactivates rotation control elements if freecam is on
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!focusIsActive) {
            drawDefaultBackground();
            drawCenteredString(mc.fontRenderer, I18n.format("mineshot.gui.title"), width / 2, height / 6 - 15, 16777215);
        }
        ctrlKeyAction();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (textIsActive && focusIsActive) {
            if (buttonTextFocus1.getDisplayState() == 1) {
                textZoom.drawTextBox();
            } else if (buttonTextFocus2.getDisplayState() == 1) {
                textXRot.drawTextBox();
            } else if (buttonTextFocus3.getDisplayState() == 1) {
                textYRot.drawTextBox();
            }
        } else if (textIsActive) {
            drawCenteredString(mc.fontRenderer, getButtonText(0, 0), width / 2 - 90, height / 6 + 25, 16777215);
            drawCenteredString(mc.fontRenderer, getButtonText(0, 1), width / 2 - 90, height / 6 + 50, 16777215);
            drawCenteredString(mc.fontRenderer, getButtonText(1, 0), width / 2 - 90, height / 6 + 75, 16777215);
            textZoom.drawTextBox();
            textXRot.drawTextBox();
            textYRot.drawTextBox();
        }
        drawTextOverButton("+" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonSliderPlus1);
        drawTextOverButton("+" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonSliderPlus2);
        drawTextOverButton("+" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonSliderPlus3);
        drawTextOverButton("-" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonSliderMinus1);
        drawTextOverButton("-" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonSliderMinus2);
        drawTextOverButton("-" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonSliderMinus3);
        drawTextOverButton("+" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonTextPlus1);
        drawTextOverButton("+" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonTextPlus2);
        drawTextOverButton("+" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonTextPlus3);
        drawTextOverButton("-" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonTextMinus1);
        drawTextOverButton("-" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonTextMinus2);
        drawTextOverButton("-" + incrementerDisplay.format(getIncrement(false)), mouseX, mouseY, buttonTextMinus3);
    }

    /**
     * Show reset buttons when Ctrl is pressed.
     */
    private void ctrlKeyAction() {
        boolean resetAlreadyActive = buttonReset1.visible || buttonReset2.visible || buttonReset3.visible;
        if (mouseNotOverButton) {
            if (!textIsActive && (buttonReset1.x != width/2-178 || buttonReset2.x != width/2-178 || buttonReset3.x != width/2-178)) {
                buttonReset1.x = width/2-178;
                buttonReset2.x = width/2-178;
                buttonReset3.x = width/2-178;
            } else if (textIsActive && (buttonReset1.x != width/2-48 || buttonReset2.x != width/2-48 || buttonReset3.x != width/2-48)) {
                buttonReset1.x = width/2-48;
                buttonReset2.x = width/2-48;
                buttonReset3.x = width/2-48;
            }
            if (!focusIsActive) {
                buttonReset1.visible = zoomUpdated != ZOOM_DEFAULT;
                buttonReset2.visible = !freeCamUpdated && xRotUpdated != XROT_DEFAULT;
                buttonReset3.visible = !freeCamUpdated && yRotUpdated != YROT_DEFAULT;
            } else {
                buttonReset1.visible = zoomUpdated != ZOOM_DEFAULT && (buttonSliderFocus1.visible || buttonTextFocus1.visible);
                buttonReset2.visible = xRotUpdated != XROT_DEFAULT && (buttonSliderFocus2.visible || buttonTextFocus2.visible);
                buttonReset3.visible = yRotUpdated != YROT_DEFAULT && (buttonSliderFocus3.visible || buttonTextFocus3.visible);
            }
            mouseNotOverButton = GuiScreen.isCtrlKeyDown();
        } else if (resetAlreadyActive) {
            buttonReset1.visible = false;
            buttonReset2.visible = false;
            buttonReset3.visible = false;
        }

    }

    /**
     * Draws a List of strings as a tooltip over a button. Every entry is drawn on a seperate line.
     */
    private void drawTextOverButton(String text, int mouseX, int mouseY, GuiButton button) {
        if (button.isMouseOver() && button.enabled && !text.isEmpty()) {
            drawHoveringText(Arrays.asList(text), mouseX, mouseY);
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
        toggleUIElements(buttonsSliderView, !textIsActive, false);
        toggleUIElements(buttonsTextView, textIsActive, false);
    }

    /**
     * Toggle state of multiple buttons at once.
     */
    private void toggleUIElements(int[] array, boolean state, boolean useEnabled) {
        for ( int i = 0; i <= array.length - 1; i++ ) {
            for ( int j = 0; j <= buttonList.size() - 1; j++ ) {
                int k = buttonList.get(j).id;
                if (k == array[i] && buttonList.get(j).visible != state && !useEnabled) {
                    buttonList.get(j).visible = state;
                } else if (k == array[i] && buttonList.get(j).enabled != state && useEnabled) {
                    buttonList.get(j).enabled = state;
                }
            }
        }
    }

    /**
     * Enable single slider mode.
     */
    private void enableSingleView(int[] array, boolean state, int exclude) {
        for ( int i = 0; i <= array.length - 1; i++ ) {
            boolean exclusionSlider = array[i] != exclude * 2 - 41 && array[i] != exclude * 2 - 42 && array[i] != exclude - 26;
            boolean exclusionText = array[i] != exclude * 2 - 31 && array[i] != exclude * 2 - 32;
            if (exclusionSlider && exclusionText && array[i] != exclude) {
                for (int j = 0; j <= buttonList.size() - 1; j++) {
                    int k = buttonList.get(j).id;
                    if (k == array[i] && buttonList.get(j).visible != state) {
                        buttonList.get(j).visible = state;
                    }
                }
            }
        }
    }

    /**
     * Called when freecam is toggled. Prevents modifying rotation since it's controlled by player view now.
     */
    private void toggleRotation() {
        toggleUIElements(buttonsFreeCam, !freeCamUpdated, true);
        textXRot.setEnabled(!freeCamUpdated);
        textYRot.setEnabled(!freeCamUpdated);
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
                mc.displayGuiScreen(old);
                break;
            case 2: // sliders
                saveTextBoxContents();
                switchDisplay(true);
                updateContainers();
                break;
            case 3: // text
                switchDisplay(true);
                updateContainers();
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
            case 13:
                zoomUpdated = ZOOM_DEFAULT;
                if (textIsActive) {
                    textZoom.setText(valueDisplay.format(zoomUpdated));
                } else {
                    sliderZoom.setSliderValue(zoomUpdated, false);
                }
                break;
            case 14:
                xRotUpdated = XROT_DEFAULT;
                if (textIsActive) {
                    textXRot.setText(valueDisplay.format(xRotUpdated));
                } else {
                    sliderXRot.setSliderValue(xRotUpdated, false);
                }
                break;
            case 15:
                yRotUpdated = YROT_DEFAULT;
                if (textIsActive) {
                    textYRot.setText(valueDisplay.format(yRotUpdated));
                } else {
                    sliderYRot.setSliderValue(yRotUpdated, false);
                }
                break;
            case 20: // plus text zoom
                zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated) + getIncrement(true), ZOOM_MIN, ZOOM_MAX);
                textZoom.setText(valueDisplay.format(zoomUpdated));
                checkZoomButtonsEnabled();
                break;
            case 21: // minus text zoom
                zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated) - getIncrement(true), ZOOM_MIN, ZOOM_MAX);
                textZoom.setText(valueDisplay.format(zoomUpdated));
                checkZoomButtonsEnabled();
                break;
            case 22: // plus text xRot
                xRotUpdated = ovh.fixValue(textXRot.getTextAsFloat(xRotUpdated) + getIncrement(true));
                textXRot.setText(valueDisplay.format(xRotUpdated));
                break;
            case 23: // minus text xRot
                xRotUpdated = ovh.fixValue(textXRot.getTextAsFloat(xRotUpdated) - getIncrement(true));
                textXRot.setText(valueDisplay.format(xRotUpdated));
                break;
            case 24: // plus text yRot
                yRotUpdated = ovh.fixValue(textYRot.getTextAsFloat(yRotUpdated) + getIncrement(true));
                textYRot.setText(valueDisplay.format(yRotUpdated));
                break;
            case 25: // minus text yRot
                yRotUpdated = ovh.fixValue(textYRot.getTextAsFloat(yRotUpdated) - getIncrement(true));
                textYRot.setText(valueDisplay.format(yRotUpdated));
                break;
            case 26: // focus text zoom
                activateFocus(buttonTextFocus1, true);
                break;
            case 27: // focus text xRot
                activateFocus(buttonTextFocus2, true);
                break;
            case 28: // focus text yRot
                activateFocus(buttonTextFocus3, true);
                break;
            case 30: // plus slider zoom
                zoomUpdated = ovh.fixValue(zoomUpdated + getIncrement(true), ZOOM_MIN, ZOOM_MAX);
                sliderZoom.setSliderValue(zoomUpdated, false);
                checkZoomButtonsEnabled();
                break;
            case 31: // minus slider zoom
                zoomUpdated = ovh.fixValue(zoomUpdated - getIncrement(true), ZOOM_MIN, ZOOM_MAX);
                sliderZoom.setSliderValue(zoomUpdated, false);
                checkZoomButtonsEnabled();
                break;
            case 32: // plus slider xRot
                xRotUpdated = ovh.fixValue(xRotUpdated + getIncrement(true));
                sliderXRot.setSliderValue(xRotUpdated, false);
                break;
            case 33: // minus slider xRot
                xRotUpdated = ovh.fixValue(xRotUpdated - getIncrement(true));
                sliderXRot.setSliderValue(xRotUpdated, false);
                break;
            case 34: // plus slider yRot
                yRotUpdated = ovh.fixValue(yRotUpdated + getIncrement(true));
                sliderYRot.setSliderValue(yRotUpdated, false);
                break;
            case 35: // minus slider yRot
                yRotUpdated = ovh.fixValue(yRotUpdated - getIncrement(true));
                sliderYRot.setSliderValue(yRotUpdated, false);
                break;
            case 36: // focus slider zoom
                activateFocus(buttonSliderFocus1, false);
                break;
            case 37: // focus slider xRot
                activateFocus(buttonSliderFocus2, false);
                break;
            case 38: // focus slider yRot
                activateFocus(buttonSliderFocus3, false);
                break;
        }
    }

    /**
     * Get current increment from pressed buttons.
     */
    private float getIncrement(boolean reset) {
        float i = 0;
        if (GuiScreen.isShiftKeyDown()) {
            i = 1;
        }
        if (GuiScreen.isAltKeyDown()) {
            i = 2;
        }
        if (GuiScreen.isCtrlKeyDown()) {
            if (mouseNotOverButton && reset) {
                mouseNotOverButton = false;
            }
            return (float) Math.pow(10, -i - 1);
        } else {
            return (float) Math.pow(10, i);
        }
    }

    /**
     * Disable all gui elements but a single slider / text box and its corresponding controls.
     */
    private void activateFocus(GuiIconButton pressedButton, boolean isTextMode) {
        focusIsActive = !focusIsActive;
        enableSingleView(isTextMode ? buttonsTextView : buttonsSliderView, !focusIsActive, pressedButton.id);
        toggleUIElements(buttonsGeneralUI, !focusIsActive, false);
        pressedButton.setDisplayState(pressedButton.getDisplayState() + 1);
    }

    /**
     * Disables plus or minus button for zoom if clicking them wouldn't be able to change the current value.
     */
    private void checkZoomButtonsEnabled() {
        if (textIsActive) {
            buttonTextPlus1.enabled = (ZOOM_MAX != zoomUpdated);
            buttonTextMinus1.enabled = (ZOOM_MIN != zoomUpdated);
        } else {
            buttonSliderPlus1.enabled = (ZOOM_MAX != zoomUpdated);
            buttonSliderMinus1.enabled = (ZOOM_MIN != zoomUpdated);
        }
    }

    /**
     * Updates the contents of the text boxes when they loose focus.
     */
    private void updateUnfocusedTextBoxes() {
        if (!textZoom.isFocused() && wasTextZoomFocused) {
            zoomUpdated = ovh.fixValue(textZoom.getTextAsFloat(zoomUpdated), ZOOM_MIN, ZOOM_MAX);
            textZoom.setText(valueDisplay.format(zoomUpdated));
            checkZoomButtonsEnabled();
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
     * Set slider / text box content to current internal values.
     */
    private void updateContainers() {
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
        boolean mouseOverSliderPlus1 = buttonSliderPlus1.isMouseOver() && buttonSliderPlus1.enabled;
        boolean mouseOverSliderPlus2 = buttonSliderPlus2.isMouseOver() && buttonSliderPlus2.enabled;
        boolean mouseOverSliderPlus3 = buttonSliderPlus3.isMouseOver() && buttonSliderPlus3.enabled;
        boolean mouseOverSliderMinus1 = buttonSliderMinus1.isMouseOver() && buttonSliderMinus1.enabled;
        boolean mouseOverSliderMinus2 = buttonSliderMinus2.isMouseOver() && buttonSliderMinus2.enabled;
        boolean mouseOverSliderMinus3 = buttonSliderMinus3.isMouseOver() && buttonSliderMinus3.enabled;
        boolean mouseOverTextPlus1 = buttonTextPlus1.isMouseOver() && buttonTextPlus1.enabled;
        boolean mouseOverTextPlus2 = buttonTextPlus2.isMouseOver() && buttonTextPlus2.enabled;
        boolean mouseOverTextPlus3 = buttonTextPlus3.isMouseOver() && buttonTextPlus3.enabled;
        boolean mouseOverTextMinus1 = buttonTextMinus1.isMouseOver() && buttonTextMinus1.enabled;
        boolean mouseOverTextMinus2 = buttonTextMinus2.isMouseOver() && buttonTextMinus2.enabled;
        boolean mouseOverTextMinus3 = buttonTextMinus3.isMouseOver() && buttonTextMinus3.enabled;
        boolean mouseOverSliderPlus = mouseOverSliderPlus1 || mouseOverSliderPlus2 || mouseOverSliderPlus3;
        boolean mouseOverSliderMinus = mouseOverSliderMinus1 || mouseOverSliderMinus2 || mouseOverSliderMinus3;
        boolean mouseOverTextPlus = mouseOverTextPlus1 || mouseOverTextPlus2 || mouseOverTextPlus3;
        boolean mouseOverTextMinus = mouseOverTextMinus1 || mouseOverTextMinus2 || mouseOverTextMinus3;
        boolean textBoxFocused = textIsActive && (textZoom.isFocused() || textXRot.isFocused() || textYRot.isFocused());
        mouseNotOverButton = GuiScreen.isCtrlKeyDown() && !textBoxFocused && !mouseOverSliderPlus && !mouseOverSliderMinus && !mouseOverTextPlus && !mouseOverTextMinus;

        if (textIsActive) {
            textZoom.textboxKeyTyped(typedChar, keyCode);
            if (!freeCamUpdated) {
                textXRot.textboxKeyTyped(typedChar, keyCode);
                textYRot.textboxKeyTyped(typedChar, keyCode);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            ovh.updateFromGui(zoom, xRot, yRot, freeCam, clip, textIsActive);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && textBoxFocused) {
            textZoom.setFocused(false);
            if (!freeCamUpdated) {
                textXRot.setFocused(false);
                textYRot.setFocused(false);
            }
            updateUnfocusedTextBoxes();
            ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated);
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
        if (!this.buttonCancel.isMouseOver()) {
            this.ovh.updateFromGui(this.zoomUpdated, this.xRotUpdated, this.yRotUpdated, this.freeCamUpdated, this.clipUpdated, this.textIsActive);
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