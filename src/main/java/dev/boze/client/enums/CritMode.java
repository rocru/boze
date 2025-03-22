package dev.boze.client.enums;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;

import java.util.function.BooleanSupplier;

public enum CritMode implements IMinecraft {
    Off(CritMode::lambda$static$0),
    Normal(CritMode::lambda$static$1),
    Force(CritMode::lambda$static$2);

    public static final String description = "Only attack when you can crit\n - Off: Don't wait for crits\n - Normal: Wait for crits when in air\n - Force: Always wait for crits\n";
    private static final CritMode[] field59 = method52();

    static {
        String[] var0 = new String[]{
                "Off",
                "Normal",
                "Force",
                "Only attack when you can crit\n - Off: Don't wait for crits\n - Normal: Wait for crits when in air\n - Force: Always wait for crits\n"
        };
    }

    private final BooleanSupplier field58;

    CritMode(BooleanSupplier var3) {
        this.field58 = var3;
    }

    private static boolean lambda$static$2() {
        return MinecraftUtils.isClientActive() && (mc.player.isOnGround() || (double) mc.player.fallDistance > 0.01);
    }

    private static boolean lambda$static$1() {
        if (!MinecraftUtils.isClientActive()) {
            return false;
        } else {
            return !mc.player.isOnGround() && (double) mc.player.fallDistance > 0.01;
        }
    }

    private static boolean lambda$static$0() {
        return false;
    }

    private static CritMode[] method52() {
        return new CritMode[]{Off, Normal, Force};
    }

    public boolean method2114() {
        return this.field58.getAsBoolean();
    }
}
