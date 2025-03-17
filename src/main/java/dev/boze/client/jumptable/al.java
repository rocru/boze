package dev.boze.client.jumptable;

import net.minecraft.entity.SpawnGroup;

public class al {
   public static final int[] field2094 = new int[SpawnGroup.values().length];

   static {
      try {
         field2094[SpawnGroup.CREATURE.ordinal()] = 1;
      } catch (NoSuchFieldError var5) {
      }

      try {
         field2094[SpawnGroup.WATER_AMBIENT.ordinal()] = 2;
      } catch (NoSuchFieldError var4) {
      }

      try {
         field2094[SpawnGroup.WATER_CREATURE.ordinal()] = 3;
      } catch (NoSuchFieldError var3) {
      }

      try {
         field2094[SpawnGroup.AMBIENT.ordinal()] = 4;
      } catch (NoSuchFieldError var2) {
      }

      try {
         field2094[SpawnGroup.MONSTER.ordinal()] = 5;
      } catch (NoSuchFieldError var1) {
      }
   }
}
