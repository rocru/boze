package dev.boze.client.mixin;

import dev.boze.client.utils.MinecraftUtils;
import mapped.Class3076;
import net.minecraft.client.render.RenderTickCounter.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Dynamic.class})
public class RenderTickCounterMixin {
    @Shadow
    private float lastFrameDuration;

    @Inject(
            method = {"beginRenderTick(J)I"},
            at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/RenderTickCounter$Dynamic;prevTimeMillis:J",
                    opcode = 181
            )}
    )
    private void onBeingRenderTick(long var1, CallbackInfoReturnable<Integer> var3) {
        if (MinecraftUtils.isClientActive()) {
            this.lastFrameDuration = this.lastFrameDuration * Class3076.method6027();
        } else {
            Class3076.method6026();
        }
    }
}
