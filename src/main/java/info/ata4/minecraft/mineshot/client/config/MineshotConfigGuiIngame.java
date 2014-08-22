/*
 ** 2012 June 16
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.apache.commons.io.FileUtils;
import org.lwjgl.util.Dimension;

/**
 * Settings screen for Minema.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MineshotConfigGuiIngame extends GuiScreen {
    
    private final String strTitle = I18n.format("mineshot.gui.title");
    private final String strWidth = I18n.format("mineshot.config.captureWidth");
    private final String strHeight = I18n.format("mineshot.config.captureHeight");
    private final List<String> warnings = new ArrayList<String>();
    private final List<String> errors = new ArrayList<String>();
    private final MineshotConfig config;
    
    private GuiTextField textFieldWidth;
    private GuiTextField textFieldHeight;
    private GuiButton doneButton;
    private String fileSize;
    
    public MineshotConfigGuiIngame(MineshotConfig config) {
        this.config = config;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        doneButton = new GuiButton(0, width / 2 - 84, 192, 150, 20, I18n.format("gui.done"));
        
        buttonList.clear();
        buttonList.add(doneButton);
        
        textFieldWidth = new GuiTextField(fontRendererObj, width / 2 - 80, 64, 64, 20);
        textFieldWidth.setFocused(true);
        textFieldWidth.setMaxStringLength(6);
        textFieldWidth.setText(String.valueOf(config.captureWidth.get()));
        
        textFieldHeight = new GuiTextField(fontRendererObj, width / 2, 64, 64, 20);
        textFieldHeight.setMaxStringLength(6);
        textFieldHeight.setText(String.valueOf(config.captureHeight.get()));
        
        updateValues();
    }
    
    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        textFieldWidth.updateCursorCounter();
        textFieldHeight.updateCursorCounter();
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */    
    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }

        if (button == doneButton) {
            mc.displayGuiScreen(null);
            config.update(true);
        }
    }
    
    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2) {
        // allow numbers and backspace only
        if (par1 > 47 && par1 < 58 || par1 == '\b') {
            textFieldWidth.textboxKeyTyped(par1, par2);
            textFieldHeight.textboxKeyTyped(par1, par2);
            updateValues();
        }
        
        // switch text fields with tab
        if (par1 == '\t') {
            if (textFieldWidth.isFocused()) {
                textFieldWidth.setText(String.valueOf(getWidth()));
                textFieldWidth.setFocused(false);
                textFieldHeight.setFocused(true);
            } else {
                textFieldHeight.setText(String.valueOf(getHeight()));
                textFieldWidth.setFocused(true);
                textFieldHeight.setFocused(false);
            }
        }
    }
    
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        textFieldWidth.mouseClicked(par1, par2, par3);
        textFieldHeight.mouseClicked(par1, par2, par3);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int x, int y, float z) {
        int xs = width / 2;
        
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, strTitle, xs - 10, 32, 0xffffff);

        textFieldWidth.drawTextBox();
        textFieldHeight.drawTextBox();
        
        drawString(fontRendererObj, strWidth, xs - 80, 50, 0xa0a0a0);
        drawString(fontRendererObj, strHeight, xs, 50, 0xa0a0a0);
        drawString(fontRendererObj, "x", xs - 11, 70, 0xffffff);
        
        int ys = 84;
        int line = 12;
        
        int ofs = fontRendererObj.getStringWidth(fileSize) / 2;
        drawString(fontRendererObj, fileSize, xs - ofs, ys += line, 0xa0a0a0);
        
        for (String warning : warnings) {
            ofs = fontRendererObj.getStringWidth(warning) / 2;
            drawString(fontRendererObj, warning, xs - ofs, ys += line, 0xeeee0a);
        }
        
        for (String error : errors) {
            ofs = fontRendererObj.getStringWidth(error) / 2;
            drawString(fontRendererObj, error, xs - ofs, ys += line, 0xee0a0a);
        }
        
        super.drawScreen(x, y, z);
    }
    
    private void updateValues() {
        int captureWidth = getWidth(); 
        int captureHeight = getHeight(); 
        long captureSize = (long) captureWidth * (long) captureHeight * 3L;
        
        fileSize = I18n.format("mineshot.gui.filesize", FileUtils.byteCountToDisplaySize(captureSize));
        
        warnings.clear();
        
        Dimension maxViewportDims = MineshotConfig.getMaxViewportDims();
        
        if (captureWidth > maxViewportDims.getWidth() || captureHeight > maxViewportDims.getHeight()) {
            warnings.add(I18n.format("mineshot.gui.warn.tiled"));
            warnings.add(I18n.format("mineshot.gui.warn.shaders"));
            warnings.add(I18n.format("mineshot.gui.warn.gui"));
            config.captureTiled.set(true);
        } else {
            config.captureTiled.set(false);
        }
        
        errors.clear();
        if (captureWidth > MineshotConfig.MAX_TARGA_SIZE || captureHeight > MineshotConfig.MAX_TARGA_SIZE) {
            errors.add(I18n.format("mineshot.gui.err.toolarge.targa"));
        }
        
        if (errors.isEmpty()) {
            config.captureWidth.set(captureWidth);
            config.captureHeight.set(captureHeight);
            
            doneButton.enabled = true;
        } else {
            doneButton.enabled = false;
        }
    }
    
    public int getWidth() {
        try {
            return Math.max(1, Integer.parseInt(textFieldWidth.getText()));
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    public int getHeight() {
        try {
            return Math.max(1, Integer.parseInt(textFieldHeight.getText()));
        } catch (NumberFormatException ex) {
            return 1;
        }
    }
}
