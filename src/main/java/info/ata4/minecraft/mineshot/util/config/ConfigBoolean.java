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
public class ConfigBoolean extends ConfigValue<Boolean> {

    public ConfigBoolean(Boolean value) {
        super(value);
    }

    @Override
    protected Property.Type getPropType() {
        return Property.Type.BOOLEAN;
    }

    @Override
    public Boolean get() {
        return getProp().getBoolean();
    }

    @Override
    public void set(Boolean value) {
        getProp().set(value);
    }

}
