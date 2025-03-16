package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.XRay;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   value = {BlockOcclusionCache.class},
   remap = false
)
public class BlockOcclusionCacheMixin {
   @Inject(
      method = {"shouldDrawSide"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void shouldDrawSide(BlockState var1, BlockView var2, BlockPos var3, Direction var4, CallbackInfoReturnable<Boolean> var5) {
      if (XRay.INSTANCE.isEnabled()) {
         var5.setReturnValue(XRay.INSTANCE.method2086(var1, var2, var3, var4, var5.getReturnValueZ()));
      }
   }
}
