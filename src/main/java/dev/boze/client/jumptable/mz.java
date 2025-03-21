package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class mz {
    static final int[] field2115 = new int[Direction.values().length];

    static {
        try {
            field2115[Direction.NORTH.ordinal()] = 1;
        } catch (NoSuchFieldError var3) {
        }

        try {
            field2115[Direction.SOUTH.ordinal()] = 2;
        } catch (NoSuchFieldError var2) {
        }

        try {
            field2115[Direction.WEST.ordinal()] = 3;
        } catch (NoSuchFieldError var1) {
        }
    }
}
