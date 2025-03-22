package dev.boze.client.mixin;

import dev.boze.client.Boze;
import dev.boze.client.events.OcclusionEvent;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkOcclusionDataBuilder.class)
public class ChunkOcclusionDataBuilderMixin {
    @Inject(
            method = "markClosed",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMarkClosed(BlockPos var1, CallbackInfo var2) {
        OcclusionEvent var3 = Boze.EVENT_BUS.post(OcclusionEvent.method1076());
        if (var3.method1022()) {
            var2.cancel();
        }
    }
}
