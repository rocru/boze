package dev.boze.client.mixin;

import net.caffeinemc.mods.sodium.client.render.util.RenderAsserts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = {RenderAsserts.class},
        remap = false
)
public class RenderAssertsMixin {
    @Inject(
            method = {"validateCurrentThread"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void onValidateCurrentThread(CallbackInfoReturnable<Boolean> var0) {
        var0.setReturnValue(true);
    }
}
