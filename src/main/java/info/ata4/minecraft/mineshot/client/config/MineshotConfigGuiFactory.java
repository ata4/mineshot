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

import info.ata4.minecraft.mineshot.Mineshot;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MineshotConfigGuiFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft mc) {
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        // unused/unimplemented by Forge at time this was written
        return null;
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen gs) {
        Configuration config = Mineshot.instance.getConfigForge();
        
        // map config elements to their categories, except for CATEGORY_GENERAL
        List<IConfigElement> configElems = config.getCategoryNames().stream()
            .filter(catName -> !catName.equals(Configuration.CATEGORY_GENERAL))
            .map(catName -> new ConfigElement(config.getCategory(catName)))
            .collect(Collectors.toList());

        // add props in CATEGORY_GENERAL directly to the root of the list
        if (config.hasCategory(Configuration.CATEGORY_GENERAL)) {
            ConfigCategory catGeneral = config.getCategory(Configuration.CATEGORY_GENERAL);
            List<Property> props = catGeneral.getOrderedValues();            
            configElems.addAll(props.stream()
                .map(prop -> new ConfigElement(prop))
                .collect(Collectors.toList())
            );
        }
        
        String title = GuiConfig.getAbridgedConfigPath(Mineshot.instance.getConfigForge().toString());
        
        return new GuiConfig(gs, configElems, Mineshot.MODID, false, false, title) {
            @Override
            public void onGuiClosed() {
                super.onGuiClosed();
                if (config.hasChanged()) {
                    config.save();
                }
            }
        };
    }
    
}
