package dev.boze.client.utils;

import dev.boze.client.mixin.ClientPlayerEntityAccessor;

public class MovementUtils implements IMinecraft {
    public static boolean method2114() {
        if (!MinecraftUtils.isClientActive()) {
            return false;
        } else {
            double var3 = mc.player.getX() - ((ClientPlayerEntityAccessor) mc.player).getLastX();
            double var5 = mc.player.getY() - ((ClientPlayerEntityAccessor) mc.player).getLastY();
            double var7 = mc.player.getZ() - ((ClientPlayerEntityAccessor) mc.player).getLastZ();
            return !(var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4);
        }
    }
}
