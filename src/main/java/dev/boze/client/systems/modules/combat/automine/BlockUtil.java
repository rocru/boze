package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.enums.FilterMode;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import mapped.Class3062;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class BlockUtil implements IMinecraft {
    public static boolean method2101(BlockPos var0) {
        try {
            AutoMine var4 = AutoMine.INSTANCE;
            BlockState var5 = mc.world.getBlockState(var0);
            if (var5.isAir()) {
                return false;
            } else if (!method519(var4, var5.getBlock())) {
                return false;
            } else {
                return var5.getHardness(mc.world, var0) != -1.0F && method2102(var0);
            }
        } catch (NullPointerException var6) {
            return false;
        }
    }

    static boolean method2102(BlockPos var0) {
        try {
            Vec3d var4 = Class3062.method5991(EntityUtil.method2144(mc.player), new Box(var0));
            return var4.squaredDistanceTo(EntityUtil.method2144(mc.player)) <= Math.pow(AutoMine.INSTANCE.miner.field193.getValue(), 2.0);
        } catch (NullPointerException var5) {
            return false;
        }
    }

    static boolean method519(AutoMine var0, Block var1) {
        if (var0.filter.getValue() == FilterMode.Blacklist) {
            if (var0.blocks.method2032().contains(var1)) {
                return false;
            }
        } else if (var0.filter.getValue() == FilterMode.Whitelist && !var0.blocks.method2032().contains(var1)) {
            return false;
        }

        return !(var1 instanceof FluidBlock);
    }
}
