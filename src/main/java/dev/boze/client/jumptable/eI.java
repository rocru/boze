package dev.boze.client.jumptable;

import net.minecraft.entity.SpawnGroup;

public class eI {
   public static final int[] field2096 = new int[SpawnGroup.values().length];

   static {
      try {
         field2096[SpawnGroup.CREATURE.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2096[SpawnGroup.WATER_AMBIENT.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2096[SpawnGroup.WATER_CREATURE.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2096[SpawnGroup.AMBIENT.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2096[SpawnGroup.MONSTER.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
