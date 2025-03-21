package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction;

class nf {
    static final int[] field2119 = new int[Direction.values().length];

    static {
        try {
            field2119[Direction.DOWN.ordinal()] = 1;
        } catch (NoSuchFieldError var6) {
        }

        try {
            field2119[Direction.UP.ordinal()] = 2;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2119[Direction.NORTH.ordinal()] = 3;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2119[Direction.SOUTH.ordinal()] = 4;
        } catch (NoSuchFieldError var3) {
        }

        try {
            field2119[Direction.EAST.ordinal()] = 5;
        } catch (NoSuchFieldError var2) {
        }

        try {
            field2119[Direction.WEST.ordinal()] = 6;
        } catch (NoSuchFieldError var1) {
        }
    }
}
