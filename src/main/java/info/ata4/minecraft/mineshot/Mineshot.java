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

import info.ata4.minecraft.mineshot.client.OrthoViewHandler;
import info.ata4.minecraft.mineshot.client.ScreenshotHandler;
import info.ata4.minecraft.mineshot.client.config.MineshotConfig;
import java.io.File;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Mineshot mod container class.
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
    public static final String ID = "mineshot";
    public static final String VERSION = "1.72";
    
    @Instance(ID)
    public static Mineshot instance;
    
    private ModMetadata metadata;
    private Configuration configForge;
    private MineshotConfig config;
    
    public Configuration getConfigForge() {
        return configForge;
    }

    public MineshotConfig getConfig() {
        return config;
    }

    public ModMetadata getMetadata() {
        return metadata;
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent evt) {
        File file = evt.getSuggestedConfigurationFile();
        configForge = new Configuration(file);
        config = new MineshotConfig(configForge);
        metadata = evt.getModMetadata();
    }
    
    @EventHandler
    public void onInit(FMLInitializationEvent evt) {
        ScreenshotHandler sch = new ScreenshotHandler(config);
        MinecraftForge.EVENT_BUS.register(sch);
        
        OrthoViewHandler ovh = new OrthoViewHandler();
        MinecraftForge.EVENT_BUS.register(ovh);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
}
