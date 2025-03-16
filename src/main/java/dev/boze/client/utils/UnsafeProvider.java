package dev.boze.client.utils;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class UnsafeProvider {
   private static final Unsafe unsafe;

   public static Unsafe get() {
      return unsafe;
   }

   static {
      try {
         Field var11 = Unsafe.class.getDeclaredField("theUnsafe");
         var11.setAccessible(true);
         unsafe = (Unsafe)var11.get(null);
      } catch (IllegalAccessException | NoSuchFieldException var12) {
         throw new RuntimeException("Error initializing UnsafeProvider", var12);
      }
   }
}
