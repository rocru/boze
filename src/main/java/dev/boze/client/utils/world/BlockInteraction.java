package dev.boze.client.utils.world;

import mapped.Class2784;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class BlockInteraction {
    private final BlockPos field3977;
    private boolean field3978 = false;
    private boolean field3979 = false;
    private double field3980 = 0.0;
    private BlockHitResult field3981 = null;
    private boolean field3982 = false;

    private BlockInteraction(BlockPos var1) {
        this.field3977 = var1;
    }

    public static BlockInteraction method2272(BlockPos pos) {
        return new BlockInteraction(pos);
    }

    public BlockInteraction method2273() {
        this.field3978 = true;
        return this;
    }

    public BlockInteraction method2274() {
        this.field3979 = true;
        return this;
    }

    public BlockInteraction method2275(boolean rayCast) {
        this.field3978 = rayCast;
        return this;
    }

    public BlockInteraction method2276(boolean strictDirection) {
        this.field3979 = strictDirection;
        return this;
    }

    public BlockInteraction method2277(double wallsRange) {
        this.field3980 = wallsRange;
        return this;
    }

    public BlockPos method2278() {
        return this.field3977;
    }

    public BlockHitResult method2279() {
        if (this.field3982) {
            return this.field3981;
        } else {
            this.field3981 = Class2784.method5446(this.field3977, this.field3979, this.field3978);
            if (this.field3981 == null) {
                this.field3982 = true;
                return null;
            } else {
                return this.field3981;
            }
        }
    }

    public double method2280() {
        return 0.0;
    }
}
