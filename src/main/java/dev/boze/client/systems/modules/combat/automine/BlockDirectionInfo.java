package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.enums.AutoMineMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;
import java.util.function.Function;

public class BlockDirectionInfo {
    public final BlockPos field2523;
    final Direction field2524;
    final Vec3d field2525;
    public final AutoMineMode field2526;
    final Function<BlockPos, Boolean> field2527;

    public BlockDirectionInfo(BlockPos var1, Direction var2, AutoMineMode var3) {
        this(var1, var2, var3, BlockDirectionInfo::lambda$new$0);
    }

    BlockDirectionInfo(BlockPos var1, Direction var2, AutoMineMode var3, Function<BlockPos, Boolean> var4) {
        this.field2523 = var1;
        this.field2524 = var2;
        this.field2525 = new Vec3d(
                (double) var1.getX() + 0.5 + (double) var2.getUnitVector().x * 0.5,
                (double) var1.getY() + 0.5 + (double) var2.getUnitVector().y * 0.5,
                (double) var1.getZ() + 0.5 + (double) var2.getUnitVector().z * 0.5
        );
        this.field2526 = var3;
        this.field2527 = var4;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            BlockDirectionInfo var5 = (BlockDirectionInfo) o;
            return Objects.equals(this.field2523, var5.field2523);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.field2523);
    }

    private static Boolean lambda$new$0(BlockPos var0) {
        return true;
    }
}
