package mapped;

import dev.boze.client.mixin.ClientPlayNetworkHandlerAccessor;
import dev.boze.client.systems.iterators.BlockEntityIterator;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Class5914 implements IMinecraft {
    public Class5914() {
        super();
    }

    public static Iterable<BlockEntity> method19() {
        return BlockEntityIterator::new;
    }

    public static int method2010() {
        return Math.max(Class5914.mc.options.getViewDistance().getValue(), ((ClientPlayNetworkHandlerAccessor) Class5914.mc.getNetworkHandler()).getChunkLoadDistance());
    }

    public static boolean method5504(final BlockPos blockPos, final boolean doubles) {
        if (!MinecraftUtils.isClientActive()) {
            return false;
        }
        final BlockPos up = blockPos.up();
        if (Class5914.mc.world.getBlockState(up).isSolidBlock(Class5914.mc.world, up)) {
            return false;
        }
        int n = 0;
        final Direction[] values = Direction.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final Direction direction = values[i];
            if (direction != Direction.UP) {
                if (Class5914.mc.world.getBlockState(blockPos.offset(direction)).getBlock().getBlastResistance() < 600.0f) {
                    if (!doubles || direction == Direction.DOWN) {
                        return false;
                    }
                    ++n;
                    final Direction[] values2 = Direction.values();
                    for (int length2 = values2.length, j = 0; j < length2; ++j) {
                        final Direction direction2 = values2[j];
                        if (direction2 != direction.getOpposite()) {
                            if (direction2 != Direction.UP) {
                                if (Class5914.mc.world.getBlockState(blockPos.offset(direction).offset(direction2)).getBlock().getBlastResistance() < 600.0f) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return n < 2;
    }
}
