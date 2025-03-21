package dev.boze.client.mixin;

import dev.boze.client.systems.modules.movement.Jesus;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PowderSnowBlock.class})
public class PowderSnowBlockMixin {
    @Inject(
            method = {"canWalkOnPowderSnow"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private static void onCanWalkOnPowderSnow(Entity var0, CallbackInfoReturnable<Boolean> var1) {
        if (Jesus.INSTANCE.isEnabled() && Jesus.INSTANCE.field3291.getValue()) {
            var1.setReturnValue(true);
        }
    }
}
