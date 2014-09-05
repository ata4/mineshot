/*
 ** 2014 September 05
 **
 ** The author disclaims copyright to this source code. In place of
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
public class ConfigEnum<T extends Enum> extends ConfigValue<T> {
    
    private final Class<T> type;
    private final String[] validValues;

    public ConfigEnum(T value) {
        super(value);
        
        this.type = (Class<T>) value.getClass();
        
        T[] values = type.getEnumConstants();
        validValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            validValues[i] = enumToString(values[i]);
        }
    }
    
    private String enumToString(T e) {
        return e.name().toLowerCase();
    }
    
    private T stringToEnum(String name) {
        return (T) T.valueOf(type, name.toUpperCase());
    }
    
    @Override
    public Property.Type getPropType() {
        return Property.Type.STRING;
    }

    @Override
    public void importProp(Property prop) {
        try {
            set(stringToEnum(prop.getString()));
        } catch (IllegalArgumentException ex) {
            set(getDefault());
        }
    }

    @Override
    public void exportProp(Property prop) {
        prop.set(enumToString(get()));
        prop.setDefaultValue(enumToString(getDefault()));
        prop.setValidValues(validValues);
    }

}
