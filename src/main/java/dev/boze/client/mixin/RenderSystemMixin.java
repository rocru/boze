package dev.boze.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.Boze;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.render.Tint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderSystem.class})
public class RenderSystemMixin {
    @Inject(
            method = {"getShaderFogColor"},
            at = {@At("HEAD")},
            cancellable = true,
            remap = false
    )
    private static void onGetShaderFogColor(CallbackInfoReturnable<float[]> var0) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3743.getValue()) {
            var0.setReturnValue(
                    new float[]{
                            (float) Tint.INSTANCE.field3744.getValue().field408 / 255.0F,
                            (float) Tint.INSTANCE.field3744.getValue().field409 / 255.0F,
                            (float) Tint.INSTANCE.field3744.getValue().field410 / 255.0F,
                            (float) Tint.INSTANCE.field3744.getValue().field411 / 255.0F
                    }
            );
        }
    }

    @Inject(
            method = {"flipFrame"},
            at = {@At("HEAD")}
    )
    private static void onFlipFrame(long var0, CallbackInfo var2) {
        Boze.EVENT_BUS.post(FlipFrameEvent.method1039());
        GhostRotations.INSTANCE.method1904();
    }
}
