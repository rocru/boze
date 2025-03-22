package dev.boze.client.enums;

import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.systems.modules.movement.boatfly.nj;
import dev.boze.client.systems.modules.movement.boatfly.nk;
import dev.boze.client.systems.modules.movement.boatfly.nl;

public enum BoatFlyMode {
    Grim,
    NCP;

    private nj field1792;

    // $VF: synthetic method
    private static BoatFlyMode[] method904() {
        return new BoatFlyMode[]{Grim, NCP};
    }

    public nj method903(BoatFly var1) {
        if (this.field1792 == null) {
            this.field1792 = switch (this) {
                case Grim -> new nk(var1);
                case NCP -> new nl(var1);
            };
        }

        return this.field1792;
    }
}
