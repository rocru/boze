package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.XRay;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderLayers.class})
public class RenderLayersMixin {
   @Inject(
      method = {"getBlockLayer"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetBlockLayer(BlockState var0, CallbackInfoReturnable<RenderLayer> var1) {
      if (XRay.INSTANCE.isEnabled()) {
         var1.setReturnValue(RenderLayer.getTranslucent());
      }
   }
}
