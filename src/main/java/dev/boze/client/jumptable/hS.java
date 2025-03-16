package dev.boze.client.jumptable;

import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.ToggleStyle;

class hS {
   static final int[] field2103;
   static final int[] field2104 = new int[ToggleStyle.values().length];

   static {
      try {
         field2104[ToggleStyle.Switch.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         field2104[ToggleStyle.Circle.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         field2104[ToggleStyle.Check.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      field2103 = new int[AlignMode.values().length];

      try {
         field2103[AlignMode.Left.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2103[AlignMode.Center.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2103[AlignMode.Right.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }
   }
}
