package dev.boze.client.enums;

import dev.boze.client.systems.modules.movement.Velocity;
import dev.boze.client.utils.IMinecraft;

public enum VelocityCancel {
    Off,
    Always,
    InBlocks;

    private static final VelocityCancel[] field1791 = method902();

    public boolean method2114() {
        return Velocity.INSTANCE.method1892()
                && (this == Always || this == InBlocks && !IMinecraft.mc.world.isSpaceEmpty(IMinecraft.mc.player.getBoundingBox()))
                && Velocity.INSTANCE.method1895();
    }

    private static VelocityCancel[] method902() {
        return new VelocityCancel[]{Off, Always, InBlocks};
    }
}
