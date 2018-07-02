package info.ata4.minecraft.mineshot.client.gui;

import info.ata4.minecraft.mineshot.client.OrthoViewHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.resources.I18n;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GuiCameraSingle extends GuiScreen implements GuiPageButtonList.GuiResponder {

    private static final float ZOOM_STEP = 0.5f;
    private static final float ZOOM_MIN = 0.5f;
    private static final float ZOOM_MAX = 512f;
    private static final float ROTATE_STEP = 15f;

    private GuiSlider slider;
    private GuiIconButton buttonPlus;
    private GuiIconButton buttonMinus;

    private GuiScreen old;
    private OrthoViewHandler ovh;
    private final DecimalFormat valueDisplay = new DecimalFormat("0.000");

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

        buttonPlus = new GuiIconButton(30, width/2+125, height/6+50, 4, false);
        buttonList.add(buttonPlus);
        buttonMinus = new GuiIconButton(31, width/2-145, height/6+50, 3, false);
        buttonList.add(buttonMinus);
/**
        slider = new GuiSlider(this, 10, width/2-125, height/6+50, 250, I18n.format("mineshot.gui.zoom"), ZOOM_MIN, ZOOM_MAX, zoomUpdated, (id, name, value) -> {
            zoomUpdated = value;
            checkButtonsEnabled(zoomUpdated);
            ovh.updateFromGui(zoomUpdated, xRotUpdated, yRotUpdated, freeCamUpdated, clipUpdated, textIsActive);
            return name+": " + valueDisplay.format(zoomUpdated);
        }); */
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Disables plus or minus button for zoom if clicking them wouldn't be able to change the current value.
     */
    private void checkButtonsEnabled(float zoomVal) {
        buttonPlus.enabled = (ZOOM_MAX != zoomVal);
        buttonMinus.enabled = (ZOOM_MIN != zoomVal);
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
