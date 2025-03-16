package mapped;

import net.minecraft.util.math.Vec3d;

class Class3090 extends Vec3d {
   private final long field216;

   public Class3090(double xIn, double yIn, double zIn, long time) {
      super(xIn, yIn, zIn);
      this.field216 = time;
   }

   public long method6065() {
      return this.field216;
   }
}
