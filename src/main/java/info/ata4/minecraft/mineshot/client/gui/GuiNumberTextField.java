package info.ata4.minecraft.mineshot.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiNumberTextField extends GuiTextField {

    public GuiNumberTextField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height)
    {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    /**
     * Checks if the given character is allowed to be put into chat.
     */
    private static boolean isAllowedCharacter(char character)
    {
        return character < 58 && character >= ' ' && character > 45 && character != 47;
    }

    /**
     * Filter a string, keeping only characters for which {@link #isAllowedCharacter(char)} returns true.
     *
     * Note that this method strips line breaks, as {@link #isAllowedCharacter(char)} returns false for those.
     * @return A filtered version of the input string
     */
    private static String filterAllowedCharacters(String input)
    {
        StringBuilder stringbuilder = new StringBuilder();

        for (char c0 : input.toCharArray())
        {
            if (isAllowedCharacter(c0))
            {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    /**
     * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
     */
    @Override
    public void writeText(String textToWrite)
    {
        String filteredText = filterAllowedCharacters(textToWrite);
        super.writeText(filteredText);
    }

    /**
     * Returns the contents of the textbox as float
     */
    public float getTextAsFloat(float previousValue)
    {
        String value = this.getText();
        StringBuilder buffer = new StringBuilder(value);
        value = buffer.reverse().toString().replaceFirst("[.]",":");
        value = new StringBuilder(value).reverse().toString();
        value = value.replaceAll("[^0-9:]","");
        value = value.replaceAll("[:]",".");
        if (value.equals(".") || value.equals("")) {
            return previousValue;
        } else {
            return Float.valueOf(value);
        }
    }
}
