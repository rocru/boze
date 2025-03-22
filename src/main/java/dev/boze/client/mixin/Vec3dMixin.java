package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IVec3d;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Vec3d.class)
public class Vec3dMixin implements IVec3d {
    @Shadow
    @Final
    @Mutable
    public double x;
    @Shadow
    @Final
    @Mutable
    public double y;
    @Shadow
    @Final
    @Mutable
    public double z;

    @Override
    public void boze$set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void boze$set(Vec3i vec) {
        this.boze$set(vec.getX(), vec.getY(), vec.getZ());
    }

    @Override
    public void boze$set(Vector3d vec) {
        this.boze$set(vec.x, vec.y, vec.z);
    }

    @Override
    public void boze$setY(double y) {
        this.y = y;
    }
}
