package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import net.minecraft.world.biome.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FoliageColors.class})
public class FoliageColorsMixin {
   @Inject(
      method = {"getBirchColor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetBirchColor(CallbackInfoReturnable<Integer> var0) {
      if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.ae.getValue()) {
         var0.setReturnValue(Tint.INSTANCE.af.getValue().method2010());
         var0.cancel();
      }
   }

   @Inject(
      method = {"getSpruceColor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetSpruceColor(CallbackInfoReturnable<Integer> var0) {
      if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.ae.getValue()) {
         var0.setReturnValue(Tint.INSTANCE.af.getValue().method2010());
         var0.cancel();
      }
   }
}
