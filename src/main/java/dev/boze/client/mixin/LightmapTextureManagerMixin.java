package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.FullBright;
import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @ModifyArgs(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"
            )
    )
    private void updateSetColor(Args var1) {
        if (FullBright.INSTANCE.isEnabled() && FullBright.INSTANCE.field3565.getValue()) {
            var1.set(2, -1);
        }
    }

    @Inject(
            method = "getDarknessFactor(F)F",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getDarknessFactor(float var1, CallbackInfoReturnable<Float> var2) {
        if (NoRender.method1989()) {
            var2.setReturnValue(0.0F);
        }
    }
}
