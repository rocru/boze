package dev.boze.client.ac;

import dev.boze.client.utils.world.PositionUtil;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

class NearestHitCalculator {
   static BlockHitResult method947(ArrayList<BlockHitResult> var0) {
      Vec3d var1 = PositionUtil.getPlayerPosition();
      return (BlockHitResult)var0.stream().min(Comparator.comparingDouble(NearestHitCalculator::lambda$nearestHit$0)).orElse(null);
   }

   private static double lambda$nearestHit$0(Vec3d var0, BlockHitResult var1) {
      return var1.getPos().distanceTo(var0);
   }
}
