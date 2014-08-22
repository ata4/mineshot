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

    @Override
    public void set(T value) {
        if (min != null && min.compareTo(value) > 0) {
            super.set(min);
        } else if (max != null && max.compareTo(value) < 0) {
            super.set(max);
        } else {
            super.set(value);
        }
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
}
