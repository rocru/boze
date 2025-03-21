package dev.boze.client.jumptable;

import net.minecraft.util.math.Direction.Axis;

class ir {
    static final int[] field2106 = new int[Axis.values().length];

    static {
        try {
            field2106[Axis.X.ordinal()] = 1;
        } catch (NoSuchFieldError var3) {
        }

        try {
            field2106[Axis.Y.ordinal()] = 2;
        } catch (NoSuchFieldError var2) {
        }

        try {
            field2106[Axis.Z.ordinal()] = 3;
        } catch (NoSuchFieldError var1) {
        }
    }
}
