package dev.boze.client.utils;

import dev.boze.client.jumptable.ir;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;

public class Vec3dHelper {
   public static final Vec3d[] field3930 = new Vec3d[]{
      new Vec3d(0.0, -0.45, -0.45), new Vec3d(0.0, -0.45, 0.45), new Vec3d(0.0, 0.45, -0.45), new Vec3d(0.0, 0.45, 0.45)
   };
   public static final Vec3d[] field3931 = new Vec3d[]{
      new Vec3d(-0.45, 0.0, -0.45), new Vec3d(-0.45, 0.0, 0.45), new Vec3d(0.45, 0.0, -0.45), new Vec3d(0.45, 0.0, 0.45)
   };
   public static final Vec3d[] field3932 = new Vec3d[]{
      new Vec3d(-0.45, -0.45, 0.0), new Vec3d(-0.45, 0.45, 0.0), new Vec3d(0.45, -0.45, 0.0), new Vec3d(0.45, 0.45, 0.0)
   };

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static Vec3d[] method2170(Axis axis) {
      return switch (ir.field2106[axis.ordinal()]) {
         case 1 -> field3930;
         case 2 -> field3931;
         case 3 -> field3932;
         default -> null;
      };
   }
}
