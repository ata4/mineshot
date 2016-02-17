/*
 ** 2012 June 16
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.client.config;

import info.ata4.minecraft.mineshot.util.config.ConfigBoolean;
import info.ata4.minecraft.mineshot.util.config.ConfigContainer;
import info.ata4.minecraft.mineshot.util.config.ConfigInteger;
import net.minecraftforge.common.config.Configuration;

/**
 * Mineshot configuration.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class MineshotConfig extends ConfigContainer {
    
    // maximum size that the Targa format supports
    public static final int MAX_TARGA_SIZE = 0xffff;
    
    public final ConfigInteger captureWidth = new ConfigInteger(3840, 1, MAX_TARGA_SIZE);
    public final ConfigInteger captureHeight = new ConfigInteger(2160, 1, MAX_TARGA_SIZE);
    public final ConfigBoolean captureTiled = new ConfigBoolean(false);
   
    public MineshotConfig(Configuration config) {
        super(config);
        
        setLangKeyPrefix("mineshot.config");
        
        register(captureWidth, "captureWidth", Configuration.CATEGORY_GENERAL);
        register(captureHeight, "captureHeight", Configuration.CATEGORY_GENERAL);
        register(captureTiled, "captureTiled", Configuration.CATEGORY_GENERAL);

        update(true);
    }
}
