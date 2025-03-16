package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({World.class})
public class WorldMixin {
   @Inject(
      method = {"getRainGradient"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getRainGradient(CallbackInfoReturnable<Float> var1) {
      if (NoRender.method1984()) {
         var1.setReturnValue(0.0F);
      }
   }

   @Inject(
      method = {"getThunderGradient"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getThunderGradient(CallbackInfoReturnable<Float> var1) {
      if (NoRender.method1984()) {
         var1.setReturnValue(0.0F);
      }
   }
}
