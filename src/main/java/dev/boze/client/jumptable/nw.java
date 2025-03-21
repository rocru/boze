package dev.boze.client.jumptable;

import net.minecraft.entity.SpawnGroup;

class nw {
    static final int[] field2125 = new int[SpawnGroup.values().length];

    static {
        try {
            field2125[SpawnGroup.CREATURE.ordinal()] = 1;
        } catch (NoSuchFieldError var7) {
        }

        try {
            field2125[SpawnGroup.AMBIENT.ordinal()] = 2;
        } catch (NoSuchFieldError var6) {
        }

        try {
            field2125[SpawnGroup.WATER_AMBIENT.ordinal()] = 3;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2125[SpawnGroup.WATER_CREATURE.ordinal()] = 4;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2125[SpawnGroup.UNDERGROUND_WATER_CREATURE.ordinal()] = 5;
        } catch (NoSuchFieldError var3) {
        }

        try {
            field2125[SpawnGroup.AXOLOTLS.ordinal()] = 6;
        } catch (NoSuchFieldError var2) {
        }

        try {
            field2125[SpawnGroup.MONSTER.ordinal()] = 7;
        } catch (NoSuchFieldError var1) {
        }
    }
}
