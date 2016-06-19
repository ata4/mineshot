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

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public abstract class ConfigNumber<T extends Number & Comparable<T>> extends ConfigValue<T> {

    private final T min;
    private final T max;

    public ConfigNumber(T value, T min, T max) {
        super(value);
        this.min = min;
        this.max = max;
    }

    public ConfigNumber(T value, T min) {
        this(value, min, null);
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
    
}
