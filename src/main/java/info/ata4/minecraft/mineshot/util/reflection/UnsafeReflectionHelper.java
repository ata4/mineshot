/*
 ** 2013 April 16
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.util.reflection;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Reflection helper for unsafe reflection operations, such as changing final
 * fields.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class UnsafeReflectionHelper {
    
    private UnsafeReflectionHelper() {
    }

    public static void unfinalize(Field field) throws ReflectiveOperationException {
        // remove Modifier.FINAL flag if set
        if (Modifier.isFinal(field.getModifiers())) {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        }
    }

    public static <T, E> void setFinalValue(Class<? super T> classToAccess, T instance, E value, int fieldIndex) {
        try {
            Field f = classToAccess.getDeclaredFields()[fieldIndex];
            f.setAccessible(true);
            unfinalize(f);
            f.set(instance, value);
        } catch (Exception e) {
            throw new UnableToAccessFieldException(new String[0], e);
        }
    }

    public static <T, E> void setFinalValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            Field f = ReflectionHelper.findField(classToAccess, fieldNames);
            unfinalize(f);
            f.set(instance, value);
        } catch (Exception e) {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    public static int findFieldIndex(Class clazz, Class type) {
        Exception failed = null;
        
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getType() == type) {
                    return i;
                }
            } catch (Exception e) {
                failed = e;
                break;
            }
        }

        throw new UnableToFindFieldException(failed);
    }
    
    public static class UnableToFindFieldException extends RuntimeException {

        public UnableToFindFieldException(Throwable cause) {
            super(cause);
        }
    }
}
