package dev.boze.client.jumptable;

import net.minecraft.world.Difficulty;

class pP {
   static final int[] field2128 = new int[Difficulty.values().length];

   static {
      try {
         field2128[Difficulty.PEACEFUL.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2128[Difficulty.EASY.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2128[Difficulty.HARD.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }
   }
}
