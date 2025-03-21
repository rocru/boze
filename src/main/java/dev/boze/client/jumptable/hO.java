package dev.boze.client.jumptable;

import dev.boze.client.enums.ToggleStyle;

public class hO {
    public static final int[] field2099 = new int[ToggleStyle.values().length];

    static {
        try {
            field2099[ToggleStyle.Switch.ordinal()] = 1;
        } catch (NoSuchFieldError var5) {
        }

        try {
            field2099[ToggleStyle.Circle.ordinal()] = 2;
        } catch (NoSuchFieldError var4) {
        }

        try {
            field2099[ToggleStyle.Check.ordinal()] = 3;
        } catch (NoSuchFieldError var3) {
        }
    }
}
