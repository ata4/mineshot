/*
 ** 2014 August 05
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.config;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public abstract class MineshotConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft mc) {
    }

    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return MineshotConfigGui.class;
    }
}
