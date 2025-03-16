package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class ny {
   static final int[] field2126 = new int[Direction.values().length];

   static {
      try {
         field2126[Direction.DOWN.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         field2126[Direction.UP.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2126[Direction.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2126[Direction.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2126[Direction.EAST.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2126[Direction.WEST.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
