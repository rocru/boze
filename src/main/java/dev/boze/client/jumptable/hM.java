package dev.boze.client.jumptable;

import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.ToggleStyle;

class hM {
    static final int[] field2097;
    static final int[] field2098 = new int[ToggleStyle.values().length];

    static {
        try {
            field2098[ToggleStyle.Switch.ordinal()] = 1;
        } catch (NoSuchFieldError var8) {
        }

        try {
            field2098[ToggleStyle.Circle.ordinal()] = 2;
        } catch (NoSuchFieldError var7) {
        }

        try {
            field2098[ToggleStyle.Check.ordinal()] = 3;
        } catch (NoSuchFieldError var6) {
        }

        field2097 = new int[AlignMode.values().length];

        try {
            field2097[AlignMode.Left.ordinal()] = 1;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2097[AlignMode.Center.ordinal()] = 2;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2097[AlignMode.Right.ordinal()] = 3;
        } catch (NoSuchFieldError var3) {
        }
    }
}
