package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.ac.Grim;
import dev.boze.client.ac.NCP;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AutoMineMode;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.IMinecraft;
import java.util.function.Function;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public final class BlockLocationInfo {
    private final BlockPos field2528;
    private final boolean field2529;

    public BlockLocationInfo(BlockPos pos, boolean burrow) {
        this.field2528 = pos;
        this.field2529 = burrow;
    }

    public BlockDirectionInfo method1467() {
        return new BlockDirectionInfo(this.field2528, this.method1470(), AutoMineMode.Auto);
    }

    BlockDirectionInfo method1468(Function<BlockPos, Boolean> var1) {
        return new BlockDirectionInfo(this.field2528, this.method1470(), AutoMineMode.Auto, var1);
    }

    BlockDirectionInfo method1469(AutoMineMode var1, Function<BlockPos, Boolean> var2) {
        return new BlockDirectionInfo(this.field2528, this.method1470(), var1, var2);
    }

    public Direction method1470() {
        if (AutoMine.INSTANCE.miner.field191.getValue()) {
            Direction var4 = null;
            double var5 = Double.MAX_VALUE;

            for (Direction var10 : Direction.values()) {
                if (AutoMine.INSTANCE.miner.field187.getValue() == AnticheatMode.Grim && Grim.field1831.method568(this.field2528, var10)
                        || AutoMine.INSTANCE.miner.field187.getValue() == AnticheatMode.NCP && NCP.field1836.method568(this.field2528, var10)) {
                    double var11 = this.field2528.toCenterPos().add(Vec3d.of(var10.getVector()).multiply(0.5)).squaredDistanceTo(IMinecraft.mc.player.getPos());
                    if (var11 < var5) {
                        var4 = var10;
                        var5 = var11;
                    }
                }
            }

            if (var4 != null) {
                return var4;
            }
        }

        return Direction.UP;
    }

    public BlockPos method1471() {
        return this.field2528;
    }

    public boolean method1472() {
        return this.field2529;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "BlockLocationInfo[]";
    }

}
