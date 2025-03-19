package dev.boze.client.jumptable;

import net.minecraft.world.Difficulty;

public class mI {
   public static final int[] field2107 = new int[Difficulty.values().length];

   static {
      try {
         field2107[Difficulty.PEACEFUL.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2107[Difficulty.EASY.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2107[Difficulty.HARD.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
