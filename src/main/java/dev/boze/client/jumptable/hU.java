package dev.boze.client.jumptable;

import dev.boze.client.enums.ToggleStyle;

class hU {
   static final int[] field2105 = new int[ToggleStyle.values().length];

   static {
      try {
         field2105[ToggleStyle.Switch.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2105[ToggleStyle.Circle.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2105[ToggleStyle.Check.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }
   }
}
