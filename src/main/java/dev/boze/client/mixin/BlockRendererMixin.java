package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NewChunks;
import dev.boze.client.systems.modules.render.Search;
import dev.boze.client.systems.modules.render.Tint;
import dev.boze.client.systems.modules.render.XRay;
import dev.boze.client.utils.RGBAColor;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        value = {BlockRenderer.class},
        remap = false
)
public class BlockRendererMixin {
    @Inject(
            method = {"renderModel"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onRenderModel(BakedModel var1, BlockState var2, BlockPos var3, BlockPos var4, CallbackInfo var5) {
        Search var8 = Search.INSTANCE;
        if (var8.isEnabled() && var8.method2023(var2.getBlock())) {
            var8.field3692.add(new BlockPos(var3));
        }

        boolean var9 = XRay.INSTANCE.isEnabled() && !XRay.INSTANCE.method2088(var2.getBlock());
        if (var9) {
            var5.cancel();
        }

        if (NewChunks.INSTANCE.isEnabled() && NewChunks.INSTANCE.field3598.getValue()) {
            NewChunks.INSTANCE.method1967(var3, var2, true);
        }
    }

    @Redirect(
            method = {"bufferQuad"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/api/util/ColorARGB;toABGR(I)I"
            )
    )
    private int setColor(int var1) {
        boolean var4 = Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3753.getValue();
        if (var4) {
            int var5 = ColorARGB.unpackAlpha(var1);
            RGBAColor var6 = Tint.INSTANCE.field3754.getValue();
            return ColorABGR.pack(var6.field408, var6.field409, var6.field410, var5);
        } else {
            return ColorARGB.toABGR(var1);
        }
    }
}
