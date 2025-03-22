package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IBox;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Box.class)
public class BoxMixin implements IBox {
    @Shadow
    @Final
    @Mutable
    public double minX;
    @Shadow
    @Final
    @Mutable
    public double minY;
    @Shadow
    @Final
    @Mutable
    public double minZ;
    @Shadow
    @Final
    @Mutable
    public double maxX;
    @Shadow
    @Final
    @Mutable
    public double maxY;
    @Shadow
    @Final
    @Mutable
    public double maxZ;

    @Override
    public void boze$expand(double expand) {
        this.minX -= expand;
        this.minY -= expand;
        this.minZ -= expand;
        this.maxX += expand;
        this.maxY += expand;
        this.maxZ += expand;
    }
}
