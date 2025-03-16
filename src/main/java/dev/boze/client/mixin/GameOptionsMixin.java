package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.FreeCam;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameOptions.class})
public class GameOptionsMixin {
   @Inject(
      method = {"setPerspective"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void setPerspective(Perspective var1, CallbackInfo var2) {
      if (FreeCam.INSTANCE.isEnabled()) {
         var2.cancel();
      }
   }
}
