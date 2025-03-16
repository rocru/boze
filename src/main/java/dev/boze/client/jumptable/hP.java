package dev.boze.client.jumptable;

import dev.boze.client.enums.AlignMode;

class hP {
   static final int[] field2100 = new int[AlignMode.values().length];

   static {
      try {
         field2100[AlignMode.Left.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2100[AlignMode.Center.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2100[AlignMode.Right.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }
   }
}
