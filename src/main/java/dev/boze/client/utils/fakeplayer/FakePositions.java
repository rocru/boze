package dev.boze.client.utils.fakeplayer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class FakePositions {
    private final Vec3d field3939;
    private final float field3940;
    private final float field3941;
    private final float field3942;

    public FakePositions(PlayerEntity player) {
        this.field3939 = player.getPos();
        this.field3940 = player.getYaw();
        this.field3941 = player.getPitch();
        this.field3942 = player.getHeadYaw();
    }

    public FakePositions(Vec3d pos, float yaw, float pitch, float head) {
        this.field3939 = pos;
        this.field3940 = yaw;
        this.field3941 = pitch;
        this.field3942 = head;
    }

    public Vec3d method2174() {
        return this.field3939;
    }

    public float method2175() {
        return this.field3940;
    }

    public float method2176() {
        return this.field3941;
    }

    public float method2177() {
        return this.field3942;
    }
}
