package io.github.leekin2.xgoogle;

import android.graphics.Point;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Writes final fields. Reflection still does it for instance fields; Android 17 refuses static
 * finals, so those are written through Unsafe at the field's ART offset. Approach from
 * Howard20181/HyperPasskey (GPL-3.0), rewritten for XGoogle.
 */
final class FinalFields {
    private static Object unsafe;
    private static Method getInt, putInt, putBoolean, putObject, objectFieldOffset;
    private static long offsetOfOffset = -1;

    static void set(Field field, Object target, Object value) throws ReflectiveOperationException {
        field.setAccessible(true);
        try {
            field.set(target, value);
            return;
        } catch (IllegalAccessException | RuntimeException refused) {
            if (!Modifier.isStatic(field.getModifiers())) throw refused;
        }
        field.get(null); // initializes the class and resolves the field before its offset is read
        // unsafe() fills the Method fields; call it before reading them.
        Object u = unsafe();
        long at = offsetOfOffset();
        long offset = ((Number) getInt.invoke(u, field, at)).longValue();
        if (field.getType() == boolean.class) putBoolean.invoke(u, field.getDeclaringClass(), offset, value);
        else if (field.getType() == int.class) putInt.invoke(u, field.getDeclaringClass(), offset, value);
        else putObject.invoke(u, field.getDeclaringClass(), offset, value);
    }

    private static synchronized Object unsafe() throws ReflectiveOperationException {
        if (unsafe != null) return unsafe;
        Class<?> type = Class.forName("sun.misc.Unsafe");
        Field the = type.getDeclaredField("theUnsafe");
        the.setAccessible(true);
        getInt = type.getMethod("getInt", Object.class, long.class);
        putInt = type.getMethod("putInt", Object.class, long.class, int.class);
        putBoolean = type.getMethod("putBoolean", Object.class, long.class, boolean.class);
        putObject = type.getMethod("putObject", Object.class, long.class, Object.class);
        objectFieldOffset = type.getMethod("objectFieldOffset", Field.class);
        unsafe = the.get(null);
        return unsafe;
    }

    /** Where java.lang.reflect.Field keeps the ART offset: its "offset" field, or found by probing. */
    private static synchronized long offsetOfOffset() throws ReflectiveOperationException {
        if (offsetOfOffset >= 0) return offsetOfOffset;
        Object u = unsafe();
        try {
            offsetOfOffset = ((Number) objectFieldOffset.invoke(u, Field.class.getDeclaredField("offset"))).longValue();
            return offsetOfOffset;
        } catch (ReflectiveOperationException | RuntimeException hidden) {
            // Hidden from reflection: find the int that holds a known field's offset and check it moves.
        }
        Field probe = Point.class.getDeclaredField("x");
        int real = ((Number) objectFieldOffset.invoke(u, probe)).intValue();
        for (long at = 8; at < 256; at += 4) {
            if ((int) getInt.invoke(u, probe, at) != real) continue;
            putInt.invoke(u, probe, at, ~real);
            int seen = ((Number) objectFieldOffset.invoke(u, probe)).intValue();
            putInt.invoke(u, probe, at, real);
            if (seen == ~real) return offsetOfOffset = at;
        }
        throw new NoSuchFieldException("Field.offset");
    }

    private FinalFields() { }
}
