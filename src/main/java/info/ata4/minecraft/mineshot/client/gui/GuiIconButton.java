package info.ata4.minecraft.mineshot.client.gui;

import info.ata4.minecraft.mineshot.Mineshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIconButton extends GuiButton
{
    private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation(Mineshot.MODID, "textures/widgets.png");
    private int iconId;
    private boolean hasBorder;

    /**
     * Use negative iconIds to make the buttons only half their normal size.
     */
    public GuiIconButton(int buttonId, int x, int y, int iconId, boolean hasBorder)
    {
        super(buttonId, x, y, 20, Math.round((iconId + Math.abs(iconId)) / (float) (iconId + Math.abs(iconId) + 1)) * 10 + 10, "");
        this.iconId = iconId;
        this.hasBorder = hasBorder;
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            if (hasBorder) {
                drawRect(this.x - 1, this.y - 1, this.x + 21, this.y + 21, -6250336);
            }
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            this.drawTexturedModalRect(this.x, this.y, Math.abs(iconId) * 20, i * 20, this.width, this.height);
        }
    }

    /**
     * Gets whether the outline of this button should be drawn (true if so).
     */
    public boolean getHasBorder()
    {
        return this.hasBorder;
    }

    /**
     * Sets whether or not the outline of this button should be drawn.
     */
    public void setHasBorder(boolean hasBorderIn)
    {
        this.hasBorder = hasBorderIn;
    }
}