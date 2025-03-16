package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class y {
   static final int[] field2131 = new int[Direction.values().length];

   static {
      try {
         field2131[Direction.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError var6) {
      }

      try {
         field2131[Direction.SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2131[Direction.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2131[Direction.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2131[Direction.UP.ordinal()] = 5;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2131[Direction.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError var1) {
      }
   }
}
