package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class nn {
    static final int[] field2121 = new int[Direction.values().length];

    static {
        try {
            field2121[Direction.DOWN.ordinal()] = 1;
        } catch (NoSuchFieldError var6) {
        }

        try {
            field2121[Direction.UP.ordinal()] = 2;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2121[Direction.NORTH.ordinal()] = 3;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2121[Direction.SOUTH.ordinal()] = 4;
        } catch (NoSuchFieldError var3) {
        }

        try {
            field2121[Direction.EAST.ordinal()] = 5;
        } catch (NoSuchFieldError var2) {
        }

        try {
            field2121[Direction.WEST.ordinal()] = 6;
        } catch (NoSuchFieldError var1) {
        }
    }
}
