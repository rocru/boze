package dev.boze.client.render;

import net.minecraft.util.math.BlockPos;

public class Placement {
   private final int[] field2151 = new int[3];
   private long field2152;

   public void method1157(BlockPos pos, long time) {
      this.field2151[0] = pos.getX();
      this.field2151[1] = pos.getY();
      this.field2151[2] = pos.getZ();
      this.field2152 = time;
   }

   public int[] method1158() {
      return this.field2151;
   }

   public long method1159() {
      return this.field2152;
   }
}
