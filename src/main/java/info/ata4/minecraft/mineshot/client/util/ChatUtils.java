/*
** 2014 January 21
**
** The author disclaims copyright to this source code.  In place of
** a legal notice, here is a blessing:
**    May you do good and not evil.
**    May you find forgiveness for yourself and forgive others.
**    May you share freely, never taking more than you give.
*/
package info.ata4.minecraft.mineshot.client.util;

import java.io.File;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.event.ClickEvent;
import static net.minecraft.event.ClickEvent.Action.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

/**
 * Simple and easy chat log utility class.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ChatUtils {
    
    private static final Minecraft MC = Minecraft.getMinecraft();
    
    private ChatUtils() {
    }
    
    public static void print(String msg, EnumChatFormatting format, Object... args) {
        if (MC.ingameGUI == null) {
            return;
        }
        
        GuiNewChat chat = MC.ingameGUI.getChatGUI();
        ChatComponentTranslation ret = new ChatComponentTranslation(msg, args);
        ret.getChatStyle().setColor(format);
        
        chat.printChatMessage(ret);
    }
    
    public static void print(String msg, Object... args) {
        print(msg, null, args);
    }
    
    public static void printFileLink(String msg, File file) {
        ChatComponentText text = new ChatComponentText(file.getName());
        String path;
        
        try {
            path = file.getAbsoluteFile().getCanonicalPath();
        } catch (IOException ex) {
            path = file.getAbsolutePath();
        }
        
        text.getChatStyle().setChatClickEvent(new ClickEvent(OPEN_FILE, path));
        text.getChatStyle().setUnderlined(true);
        
        print(msg, text);
    }
}
