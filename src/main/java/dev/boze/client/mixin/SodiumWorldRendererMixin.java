package dev.boze.client.mixin;

import dev.boze.client.shaders.ChamsShaderRenderer;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = SodiumWorldRenderer.class,
        remap = false
)
public class SodiumWorldRendererMixin {
    @Inject(
            method = "isEntityVisible",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onIsEntityVisible(Entity var1, CallbackInfoReturnable<Boolean> var2) {
        if (ChamsShaderRenderer.field2248) {
            var2.setReturnValue(true);
        }
    }
}
