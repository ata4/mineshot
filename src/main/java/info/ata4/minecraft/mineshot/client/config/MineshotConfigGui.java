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
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MineshotConfigGui extends GuiConfig {
    
    private static String getTitle() {
        Configuration cfg = Mineshot.instance.getConfig().getConfiguration();
        return GuiConfig.getAbridgedConfigPath(cfg.toString());
    }

    public MineshotConfigGui(GuiScreen parentScreen) {
        // telescoping into space while static methods prevent worse.
        // thanks for nothing, IModGui"Factory"...
        super(parentScreen, Mineshot.instance.getConfig().getConfigElements(),
                Mineshot.ID, false, false, getTitle());
    }
    
}
