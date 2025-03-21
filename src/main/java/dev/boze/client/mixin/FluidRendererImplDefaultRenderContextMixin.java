package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadView;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(
        targets = {"net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl$DefaultRenderContext"},
        remap = false
)
public abstract class FluidRendererImplDefaultRenderContextMixin {
    @Inject(
            method = {"getColorProvider"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onGetColorProvider(Fluid var1, CallbackInfoReturnable<ColorProvider<FluidState>> var2) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.aa.getValue() && var1.getDefaultState().isIn(FluidTags.LAVA)) {
            var2.setReturnValue(this::lavaTintProvider);
        }
    }

    @Unique
    private void lavaTintProvider(LevelSlice var1, BlockPos var2, Mutable var3, FluidState var4, ModelQuadView var5, int[] var6) {
        Arrays.fill(var6, ColorABGR.withAlpha(Tint.INSTANCE.ab.getValue().method2010(), 255));
    }
}
