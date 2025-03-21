package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.FullBright;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({DimensionType.class})
public class DimensionTypeMixin {
    @Shadow
    @Final
    private float ambientLight;

    @Inject(
            method = {"ambientLight"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void ambientLight(CallbackInfoReturnable<Float> var1) {
        if (FullBright.INSTANCE.isEnabled()) {
            var1.setReturnValue(Math.max(this.ambientLight, FullBright.INSTANCE.field3567.getValue()));
        }
    }
}
