/*
 ** 2014 August 22
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
public class ConfigInteger extends ConfigNumber<Integer> {

    public ConfigInteger(Integer value, Integer min, Integer max) {
        super(value, min, max);
    }

    public ConfigInteger(Integer value, Integer min) {
        super(value, min);
    }

    @Override
    protected Property.Type getPropType() {
        return Property.Type.INTEGER;
    }
    
    @Override
    protected Property getProp() {
        Property prop = super.getProp();
        if (getMin() != null) {
            prop.setMinValue(getMin());
        }
        if (getMax() != null) {
            prop.setMaxValue(getMax());
        }
        return prop;
    }

    @Override
    public Integer get() {
        return getProp().getInt();
    }

    @Override
    public void set(Integer value) {
        getProp().set(value);
    }
}
