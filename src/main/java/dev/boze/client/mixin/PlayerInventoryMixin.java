package dev.boze.client.mixin;

import dev.boze.client.systems.modules.misc.MiningTweaks;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerInventory.class})
public class PlayerInventoryMixin {
    @Inject(
            method = {"getBlockBreakingSpeed"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void onGetBlockBreakingSpeed(CallbackInfoReturnable<Float> var1) {
        if (MiningTweaks.INSTANCE.isEnabled()) {
            var1.setReturnValue(var1.getReturnValue() * MiningTweaks.INSTANCE.speed.getValue());
        }
    }
}
