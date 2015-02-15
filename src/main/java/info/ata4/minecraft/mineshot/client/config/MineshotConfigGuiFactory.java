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
public class MineshotConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
        // What is the point of this method? I can't imagine any possible scenario
        // where minecraftInstance != Minecraft.getMinecraft(). Why not let me
        // call the constructor myself with the GuiScreen object instead?
        // Ugh! Just... ugh!
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return MineshotConfigGui.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        // unused/unimplemented by Forge at time this was written
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        // unused/unimplemented by Forge at time this was written
        return null;
    }
    
}
