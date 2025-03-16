package mapped;

import dev.boze.client.mixin.ClientPlayNetworkHandlerAccessor;
import dev.boze.client.systems.iterators.BlockEntityIterator;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Class5914 implements IMinecraft {
   public static Iterable<BlockEntity> method19() {
      return BlockEntityIterator::new;
   }

   public static int method2010() {
      return Math.max((Integer)mc.options.getViewDistance().getValue(), ((ClientPlayNetworkHandlerAccessor)mc.getNetworkHandler()).getChunkLoadDistance());
   }

   public static boolean method5504(BlockPos blockPos, boolean doubles) {
      if (!MinecraftUtils.isClientActive()) {
         return false;
      } else {
         BlockPos var5 = var6.up();
         if (mc.world.getBlockState(var5).isSolidBlock(mc.world, var5)) {
            return false;
         } else {
            int var61 = 0;

            for (Direction var10 : Direction.values()) {
               if (var10 != Direction.UP) {
                  BlockState var11 = mc.world.getBlockState(var6.offset(var10));
                  if (var11.getBlock().getBlastResistance() < 600.0F) {
                     if (!var7 || var10 == Direction.DOWN) {
                        return false;
                     }

                     var61++;

                     for (Direction var15 : Direction.values()) {
                        if (var15 != var10.getOpposite() && var15 != Direction.UP) {
                           BlockState var16 = mc.world.getBlockState(var6.offset(var10).offset(var15));
                           if (var16.getBlock().getBlastResistance() < 600.0F) {
                              return false;
                           }
                        }
                     }
                  }
               }
            }

            return var61 < 2;
         }
      }
   }
}
