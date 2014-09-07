/*
 ** 2013 April 15
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.ata4.minecraft.mineshot.client.OrthoViewHandler;
import info.ata4.minecraft.mineshot.client.ScreenshotHandler;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

/**
 * Main control class for Forge.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
@Mod(
    modid = Mineshot.ID,
    name = Mineshot.NAME,
    version = Mineshot.VERSION,
    useMetadata = true,
    guiFactory = "info.ata4.minecraft.mineshot.client.config.MineshotConfigGuiFactory"
)
public class Mineshot {
    
    public static final String NAME = "Mineshot";
    public static final String ID = NAME;
    public static final String VERSION = "@VERSION@";
    
    @Instance(ID)
    public static Mineshot instance;
    
    private ModMetadata metadata;
    private MineshotConfig config;
    
    public MineshotConfig getConfig() {
        return config;
    }

    public ModMetadata getMetadata() {
        return metadata;
    }
    
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equals(ID)) {
            config.update(false);
        }
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent evt) {
        config = new MineshotConfig(new Configuration(evt.getSuggestedConfigurationFile()));
        metadata = evt.getModMetadata();
    }
    
    @EventHandler
    public void onInit(FMLInitializationEvent evt) {
        ScreenshotHandler sch = new ScreenshotHandler(config);
        FMLCommonHandler.instance().bus().register(sch);
        
        OrthoViewHandler ovh = new OrthoViewHandler();
        FMLCommonHandler.instance().bus().register(ovh);
        MinecraftForge.EVENT_BUS.register(ovh);
        
        FMLCommonHandler.instance().bus().register(this);
    }
}
