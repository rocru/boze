package dev.boze.client.utils.trackers;

import dev.boze.client.enums.CheckEntityMode;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class EntityTracker implements IMinecraft {
    public static ConcurrentHashMap<BlockPos, Long> field3914 = new ConcurrentHashMap();

    public static void method2142() {
        field3914.entrySet().removeIf(EntityTracker::lambda$update$0);
    }

    public static boolean method2143(HashSet<BlockPos> context, BlockPos pos, CheckEntityMode checkEntities) {
        if (!context.contains(pos) && !field3914.containsKey(pos)) {
            if (!mc.world.getBlockState(pos).isReplaceable() && mc.world.getBlockState(pos).getBlock() != Blocks.SNOW) {
                return false;
            } else if (checkEntities == CheckEntityMode.Off) {
                return true;
            } else {
                VoxelShape var6 = Blocks.DIRT.getDefaultState().getCollisionShape(mc.world, pos, ShapeContext.absent());
                Entity var7 = null;
                if (checkEntities == CheckEntityMode.NotCrystals) {
                    List var8 = mc.world.getOtherEntities(null, new Box(pos), EntityTracker::lambda$isEmpty$1);
                    if (!var8.isEmpty()) {
                        var7 = (Entity) var8.get(0);
                    }
                }

                return var6.isEmpty() || mc.world.doesNotIntersectEntities(var7, var6.offset(pos.getX(), pos.getY(), pos.getZ()));
            }
        } else {
            return false;
        }
    }

    private static boolean lambda$isEmpty$1(Entity var0) {
        return var0 instanceof EndCrystalEntity;
    }

    private static boolean lambda$update$0(Entry var0) {
        if ((double) (System.currentTimeMillis() - (Long) var0.getValue()) > (double) LatencyTracker.INSTANCE.field1308 * 5.0 + 50.0) {
            return true;
        } else {
            BlockState var4 = mc.world.getBlockState((BlockPos) var0.getKey());
            return !var4.isReplaceable();
        }
    }
}
