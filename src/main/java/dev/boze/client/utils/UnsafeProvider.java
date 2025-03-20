package dev.boze.client.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeProvider {
    private static final Unsafe unsafe;

    public static Unsafe get() {
        return unsafe;
    }

    static {
        try {
            Field var11 = Unsafe.class.getDeclaredField("theUnsafe");
            var11.setAccessible(true);
            unsafe = (Unsafe) var11.get(null);
        } catch (IllegalAccessException | NoSuchFieldException var12) {
            throw new RuntimeException("Error initializing UnsafeProvider", var12);
        }
    }
}
