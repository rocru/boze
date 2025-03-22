package dev.boze.client.enums;

import dev.boze.client.utils.GenericServer;
import dev.boze.client.utils.HypixelServer;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MCServer;

public enum Server implements IMinecraft {
    Hypixel(new HypixelServer()),
    Generic(new GenericServer());

    private final MCServer field1256;

    Server(MCServer var3) {
        this.field1256 = var3;
    }

    public static Server method539() {
        if (mc.getCurrentServerEntry() == null) {
            return Generic;
        } else {
            return mc.getCurrentServerEntry().address.contains("hypixel") ? Hypixel : Generic;
        }
    }

    // $VF: synthetic method
    private static Server[] method540() {
        return new Server[]{Hypixel, Generic};
    }

    public MCServer method538() {
        return this.field1256;
    }
}
