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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public abstract class ConfigValue<T> {

    private final T valueDefault;
    private Supplier<Property> propSupplier;

    public ConfigValue(T value) {
        this.valueDefault = value;
    }
    
    public void link(Configuration config, String name, String langKeyPrefix) {
        String[] parts = StringUtils.split(name, '.');
        String catName = parts[0];
        String propName = parts[1];
        
        // set category language key and description
        String catLangKey = langKeyPrefix + "." + catName;
        String catDesc = WordUtils.wrap(I18n.format(catLangKey + ".tooltip"), 128);

        // configure category and add property
        ConfigCategory cat = config.getCategory(catName);
        cat.setLanguageKey(catLangKey);
        cat.setComment(catDesc);

        // set property language key and description
        String propLangKey = langKeyPrefix + "." + propName;
        String propDesc = WordUtils.wrap(I18n.format(propLangKey + ".tooltip"), 128);
        
        // make sure the properties have insertion order instead of being
        // randomly ordered
        List<String> order = new ArrayList<>(cat.getPropertyOrder());
        order.add(propName);

        // create supplier so that later calls don't need all the variables above
        propSupplier = () -> {
            Property prop = config.get(catName, propName, getPropDefault(),
                propDesc, getPropType());
            prop.setLanguageKey(propLangKey);
            return prop;
        };
        
        // initialize prop
        getProp();
        
        // update prop order
        cat.setPropertyOrder(order);
    }
    
    protected abstract Property.Type getPropType();

    protected Property getProp() {
        if (propSupplier == null) {
            throw new IllegalStateException("ConfigValue hasn't been linked yet!");
        }
        return propSupplier.get();
    }
    
    protected String getPropDefault() {
        return String.valueOf(getDefault());
    }
    
    public abstract T get();

    public abstract void set(T value);

    public T getDefault() {
        return valueDefault;
    }

    public void reset() {
        set(getDefault());
    }
}
