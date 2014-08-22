/*
 ** 2014 August 10
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.config;

import net.minecraftforge.common.config.Property;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public abstract class ConfigValue<T> {
    
    private T value;
    private final T valueDefault;

    public ConfigValue(T value) {
        this.value = value;
        this.valueDefault = value;
    }
    
    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
    
    public T getDefault() {
        return valueDefault;
    }
    
    public void reset() {
        set(getDefault());
    }
   
    public abstract Property.Type getPropType();
    
    public abstract void importProp(Property prop);
    
    public abstract void exportProp(Property prop);
}
