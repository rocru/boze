package dev.boze.client.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PreBlockBreakEvent extends BlockEvent {
   private static final PreBlockBreakEvent INSTANCE = new PreBlockBreakEvent();
   public boolean field1889 = false;
   public boolean field1890 = false;

   public static PreBlockBreakEvent method1033(BlockPos pos, Direction face, int blockHitDelay, float curBlockDamageMP, boolean initial) {
      INSTANCE.blockPos = pos;
      INSTANCE.direction = face;
      INSTANCE.blockBreakingCooldown = blockHitDelay;
      INSTANCE.currentBreakingProgress = curBlockDamageMP;
      INSTANCE.field1889 = false;
      INSTANCE.field1890 = initial;
      INSTANCE.method1021(false);
      return INSTANCE;
   }

   public static PreBlockBreakEvent method1034(BlockPos pos, Direction face) {
      INSTANCE.blockPos = pos;
      INSTANCE.direction = face;
      INSTANCE.blockBreakingCooldown = 0;
      INSTANCE.currentBreakingProgress = 0.0F;
      INSTANCE.field1889 = true;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
