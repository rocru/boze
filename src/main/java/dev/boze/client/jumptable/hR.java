package dev.boze.client.jumptable;

import dev.boze.client.enums.ToggleStyle;

public class hR {
    public static final int[] field2102 = new int[ToggleStyle.values().length];

    static {
        try {
            field2102[ToggleStyle.Switch.ordinal()] = 1;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2102[ToggleStyle.Circle.ordinal()] = 2;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2102[ToggleStyle.Check.ordinal()] = 3;
        } catch (NoSuchFieldError var3) {
        }
    }
}
