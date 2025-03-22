package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.world.chunk.light.ChunkSkyLightProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkSkyLightProvider.class)
public class ChunkSkylightProviderMixin {
    @Inject(
            at = @At("HEAD"),
            method = "method_51531",
            cancellable = true
    )
    private void recalculateLevel(long var1, long var3, int var5, CallbackInfo var6) {
        if (NoRender.method1972()) {
            var6.cancel();
        }
    }
}
