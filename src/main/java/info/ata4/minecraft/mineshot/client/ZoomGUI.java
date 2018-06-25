package info.ata4.minecraft.mineshot.client;

import com.google.common.base.Strings;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.text.DecimalFormat;

public class ZoomGUI extends GuiScreen implements GuiResponder {
    private String prefill;
    private GuiTextField text;
    private GuiSlider size;
    private GuiScreen old;

    OrthoViewHandler ovh= new OrthoViewHandler();
    private float realZoom = ovh.getZoom();

    public ZoomGUI(GuiScreen old, String prefill) {
        this.old = old;
        this.prefill = Strings.nullToEmpty(prefill);
    }

    @Override
    public void initGui() {
        //Keyboard.enableRepeatEvents(true);
        String oldText = (text == null ? prefill : text.getText());

        float oldSize = (size == null ? 512 : size.getSliderValue());

        text = new GuiTextField(0, mc.fontRenderer, width/2-100, height/6+50, 200, 20);
        text.setText(oldText);

        buttonList.add(new GuiButton(2, width/2-100, height/6+120, 98, 20, I18n.format("mineshot.gui.cancel")));
        GuiButton render = new GuiButton(1, width/2+2, height/6+120, 98, 20, I18n.format("mineshot.gui.done"));
        buttonList.add(render);
        DecimalFormat valueDisplay = new DecimalFormat("0.000");
        size = new GuiSlider(this, 3, width/2-100, height/6+80, I18n.format("mineshot.gui.zoomslider"), 500f, 8000f, realZoom * 1000f, (id, name, value) -> {
            //String px = Integer.toString(Math.round(value));
            ovh.setZoom(value / 1000f);
            return name+": " + valueDisplay.format(value / 1000f);
        });
        size.width = 200;
        buttonList.add(size);

        text.setFocused(true);
        text.setCanLoseFocus(false);
        boolean enabled = mc.world != null;
        render.enabled = enabled;
        text.setEnabled(enabled);
        size.enabled = enabled;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRenderer, I18n.format("mineshot.gui.title"), width/2, height/6+30, -1);
        boolean widthCap = (mc.displayWidth < 2048);
        boolean heightCap = (mc.displayHeight < 2048);
        String str = null;
        if (str != null) {
            drawCenteredString(mc.fontRenderer, I18n.format(str, Math.min(mc.displayHeight, mc.displayWidth)), width/2, height/6+104, 0xFFFFFF);
        }
        text.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 1) {
            mc.displayGuiScreen(old);
        } else if (button.id == 2) {
            mc.displayGuiScreen(old);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        text.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        text.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        text.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        //Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void setEntryValue(int id, float value) {
        size.setSliderValue(Math.round(value), false);
    }

    @Override
    public void setEntryValue(int id, boolean value) {
    }

    @Override
    public void setEntryValue(int id, String value) {
    }
}
