package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
    @Inject(
            method = "getWaterColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetWaterColor(BlockRenderView var0, BlockPos var1, CallbackInfoReturnable<Integer> var2) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3755.getValue()) {
            var2.setReturnValue(Tint.INSTANCE.field3756.getValue().method2010());
        }
    }

    @Inject(
            method = "getGrassColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetGrassColor(BlockRenderView var0, BlockPos var1, CallbackInfoReturnable<Integer> var2) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.ac.getValue()) {
            var2.setReturnValue(Tint.INSTANCE.ad.getValue().method2010());
        }
    }

    @Inject(
            method = "getFoliageColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onGetFoliageColor(BlockRenderView var0, BlockPos var1, CallbackInfoReturnable<Integer> var2) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.ae.getValue()) {
            var2.setReturnValue(Tint.INSTANCE.af.getValue().method2010());
        }
    }
}
