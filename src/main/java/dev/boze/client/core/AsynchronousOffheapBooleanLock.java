package dev.boze.client.core;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.SecureRandom;

public class AsynchronousOffheapBooleanLock {
    public static boolean BOOLEAN;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe usf = (Unsafe) f.get(null);
            long m = usf.allocateMemory(1024);
            long t = System.nanoTime();
            long j = usf.allocateMemory(1);
            usf.freeMemory(j);
            t ^= j;
            byte[] b = {(byte) (t << 56), (byte) (t << 48), (byte) (t << 40), (byte) (t << 32), (byte) (t << 24), (byte) (t << 16), (byte) (t << 8), (byte) t};
            SecureRandom r = new SecureRandom(b);

            for (int i = 0; i < 1024; i += 8) {
                usf.putLong(m + i, r.nextLong());
                m = Math.abs(m ^ r.nextLong());
                BOOLEAN ^= Long.signum(m) > 0;
            }
        } catch (Throwable _t) {
            _t.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello WOrld!");
    }
}