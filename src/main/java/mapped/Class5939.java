package mapped;

import meteordevelopment.discordipc.connection.State;

public class Class5939 {
    public static final int[] field250 = new int[State.values().length];

    static {
        try {
            field250[State.Opcode.ordinal()] = 1;
        } catch (NoSuchFieldError var3) {
        }

        try {
            field250[State.Length.ordinal()] = 2;
        } catch (NoSuchFieldError var2) {
        }

        try {
            field250[State.Data.ordinal()] = 3;
        } catch (NoSuchFieldError var1) {
        }
    }
}
