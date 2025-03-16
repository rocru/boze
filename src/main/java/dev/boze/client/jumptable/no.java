package dev.boze.client.jumptable;

import net.minecraft.entity.SpawnGroup;

class no {
   static final int[] field2122 = new int[SpawnGroup.values().length];

   static {
      try {
         field2122[SpawnGroup.CREATURE.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
      }

      try {
         field2122[SpawnGroup.AMBIENT.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
      }

      try {
         field2122[SpawnGroup.WATER_AMBIENT.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2122[SpawnGroup.WATER_CREATURE.ordinal()] = 4;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2122[SpawnGroup.UNDERGROUND_WATER_CREATURE.ordinal()] = 5;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2122[SpawnGroup.AXOLOTLS.ordinal()] = 6;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2122[SpawnGroup.MONSTER.ordinal()] = 7;
      } catch (NoSuchFieldError var1) {
      }
   }
}
