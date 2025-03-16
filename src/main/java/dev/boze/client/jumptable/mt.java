package dev.boze.client.jumptable;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.Direction;

class mt {
   static final int[] field2110;
   static final int[] field2111 = new int[Direction.values().length];

   static {
      try {
         field2111[Direction.EAST.ordinal()] = 1;
      } catch (NoSuchFieldError var8) {
      }

      try {
         field2111[Direction.SOUTH.ordinal()] = 2;
      } catch (NoSuchFieldError var7) {
      }

      try {
         field2111[Direction.WEST.ordinal()] = 3;
      } catch (NoSuchFieldError var6) {
      }

      field2110 = new int[SpawnGroup.values().length];

      try {
         field2110[SpawnGroup.CREATURE.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2110[SpawnGroup.WATER_AMBIENT.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2110[SpawnGroup.WATER_CREATURE.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2110[SpawnGroup.AMBIENT.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2110[SpawnGroup.MONSTER.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
