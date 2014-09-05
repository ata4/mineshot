/*
 ** 2014 August 11
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.Validate;
import scala.actors.threadpool.Arrays;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ConfigStringEnum extends ConfigString {

    private final Set<String> choices;
    private final String[] validValues;
    
    public ConfigStringEnum(String value, Set<String> choices) {
        super(value);
        
        Validate.notEmpty(choices);
        
        if (!choices.contains(value)) {
            throw new IllegalArgumentException();
        }
        
        this.choices = Collections.unmodifiableSet(choices);
        
        validValues = choices.toArray(new String[]{});
    }
    
    public ConfigStringEnum(String value, String... choices) {
        this(value, new HashSet<String>(Arrays.asList(choices)));
    }
    
    public Set<String> getChoices() {
        return choices;
    }
    
    @Override
    public Property.Type getPropType() {
        return Property.Type.STRING;
    }
    
    @Override
    public void set(String value) {
        if (!choices.contains(value)) {
            super.set(getDefault());
        } else {
            super.set(value);
        }
    }
    
    @Override
    public void exportProp(Property prop) {
        super.exportProp(prop);
        prop.setValidValues(validValues);
    }
}
