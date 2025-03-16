package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class ap {
   static final int[] field2095 = new int[Direction.values().length];

   static {
      try {
         field2095[Direction.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         field2095[Direction.SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2095[Direction.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2095[Direction.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2095[Direction.UP.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2095[Direction.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
