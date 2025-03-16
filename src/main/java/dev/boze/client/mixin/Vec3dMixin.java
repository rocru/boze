package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IVec3d;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Vec3d.class})
public class Vec3dMixin implements IVec3d {
   @Shadow
   @Final
   @Mutable
   public double field2146;
   @Shadow
   @Final
   @Mutable
   public double field2147;
   @Shadow
   @Final
   @Mutable
   public double field2148;

   @Override
   public void set(double x, double y, double z) {
      this.field2146 = x;
      this.field2147 = y;
      this.field2148 = z;
   }

   @Override
   public void set(Vec3i vec) {
      this.set((double)vec.getX(), (double)vec.getY(), (double)vec.getZ());
   }

   @Override
   public void set(Vector3d vec) {
      this.set(vec.x, vec.y, vec.z);
   }

   @Override
   public void setY(double y) {
      this.field2147 = y;
   }
}
