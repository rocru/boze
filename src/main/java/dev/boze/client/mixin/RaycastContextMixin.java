package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IRaycastContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({RaycastContext.class})
public class RaycastContextMixin implements IRaycastContext {
    @Shadow
    @Final
    @Mutable
    private Vec3d start;
    @Shadow
    @Final
    @Mutable
    private Vec3d end;
    @Shadow
    @Final
    @Mutable
    private ShapeType shapeType;
    @Shadow
    @Final
    @Mutable
    private FluidHandling fluid;
    @Mutable
    @Shadow
    @Final
    private ShapeContext shapeContext;

    @Override
    public void boze$set(Vec3d start, Vec3d end, ShapeType shapeType, FluidHandling fluidHandling, Entity entity) {
        this.start = start;
        this.end = end;
        this.shapeType = shapeType;
        this.fluid = fluidHandling;
        this.shapeContext = ShapeContext.of(entity);
    }
}
