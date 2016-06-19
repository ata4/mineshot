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
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MineshotConfigGui extends GuiConfig {
    
    private static List<IConfigElement> getConfigElements(Configuration config) {
        // map config elements to their categories, except for CATEGORY_GENERAL
        List<IConfigElement> list = config.getCategoryNames().stream()
            .filter(catName -> !catName.equals(Configuration.CATEGORY_GENERAL))
            .map(catName -> new ConfigElement(config.getCategory(catName)))
            .collect(Collectors.toList());

        // add props in CATEGORY_GENERAL directly to the root of the list
        if (config.hasCategory(Configuration.CATEGORY_GENERAL)) {
            ConfigCategory catGeneral = config.getCategory(Configuration.CATEGORY_GENERAL);
            List<Property> props = catGeneral.getOrderedValues();            
            list.addAll(props.stream()
                .map(prop -> new ConfigElement(prop))
                .collect(Collectors.toList())
            );
        }

        return list;
    }

    public MineshotConfigGui(GuiScreen parentScreen) {
        super(parentScreen,
            getConfigElements(Mineshot.instance.getConfigForge()),
            Mineshot.ID, false, false, GuiConfig.getAbridgedConfigPath(
                Mineshot.instance.getConfigForge().toString()
            )
        );
    }
    
}
