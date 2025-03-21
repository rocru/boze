package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.XRay;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.translucent_sorting.TranslucentGeometryCollector;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = {FluidRendererImpl.class},
        remap = false
)
public class FluidRendererImplMixin {
    @Inject(
            method = {"render"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onRender(
            LevelSlice var1,
            BlockState var2,
            FluidState var3,
            BlockPos var4,
            BlockPos var5,
            TranslucentGeometryCollector var6,
            ChunkBuildBuffers var7,
            CallbackInfo var8
    ) {
        boolean var11 = XRay.INSTANCE.isEnabled() && !XRay.INSTANCE.method2088(var3.getBlockState().getBlock());
        if (var11) {
            var8.cancel();
        }
    }
}
