package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class nt {
   static final int[] field2124 = new int[Direction.values().length];

   static {
      try {
         field2124[Direction.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2124[Direction.SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2124[Direction.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2124[Direction.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
      }
   }
}
