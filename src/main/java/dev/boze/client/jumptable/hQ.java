package dev.boze.client.jumptable;

import dev.boze.client.enums.AlignMode;

class hQ {
    static final int[] field2101 = new int[AlignMode.values().length];

    static {
        try {
            field2101[AlignMode.Left.ordinal()] = 1;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2101[AlignMode.Center.ordinal()] = 2;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2101[AlignMode.Right.ordinal()] = 3;
        } catch (NoSuchFieldError var3) {
        }
    }
}
