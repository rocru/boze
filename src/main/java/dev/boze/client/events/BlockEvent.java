package dev.boze.client.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockEvent extends CancelableEvent {
    protected BlockPos blockPos;
    protected Direction direction;
    protected int blockBreakingCooldown;
    protected float currentBreakingProgress;

    public BlockPos method1024() {
        return this.blockPos;
    }

    public void method1025(BlockPos pos) {
        this.blockPos = pos;
    }

    public Direction method1026() {
        return this.direction;
    }

    public void method1027(Direction face) {
        this.direction = face;
    }

    public int method1028() {
        return this.blockBreakingCooldown;
    }

    public void method1029(int blockHitDelay) {
        this.blockBreakingCooldown = blockHitDelay;
    }

    public float method1030() {
        return this.currentBreakingProgress;
    }

    public void method1031(float curBlockDamageMP) {
        this.currentBreakingProgress = curBlockDamageMP;
    }
}
