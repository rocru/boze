package dev.boze.client.enums;

import dev.boze.client.utils.IMinecraft;

public enum ServerType implements IMinecraft {
    TwoBuilders,
    Generic;

    public static boolean method2114() {
        ServerType var3 = method729();
        return var3 == TwoBuilders;
    }

    public static ServerType method729() {
        if (mc.getCurrentServerEntry() == null) {
            return Generic;
        } else {
            return mc.getCurrentServerEntry().address.contains("2b2t") ? TwoBuilders : Generic;
        }
    }

    // $VF: synthetic method
    private static ServerType[] method730() {
        return new ServerType[]{TwoBuilders, Generic};
    }
}
