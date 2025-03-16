package dev.boze.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.render.Tint;
import mapped.Class27;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RenderSystem.class})
public class RenderSystemMixin {
   @Inject(
      method = {"getShaderFogColor"},
      at = {@At("HEAD")},
      cancellable = true,
      remap = false
   )
   private static void onGetShaderFogColor(CallbackInfoReturnable<float[]> var0) {
      if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3743.method419()) {
         var0.setReturnValue(
            new float[]{
               (float)Tint.INSTANCE.field3744.method1347().field408 / 255.0F,
               (float)Tint.INSTANCE.field3744.method1347().field409 / 255.0F,
               (float)Tint.INSTANCE.field3744.method1347().field410 / 255.0F,
               (float)Tint.INSTANCE.field3744.method1347().field411 / 255.0F
            }
         );
      }
   }

   @Inject(
      method = {"flipFrame"},
      at = {@At("HEAD")}
   )
   private static void onFlipFrame(long var0, CallbackInfo var2) {
      Class27.EVENT_BUS.post(FlipFrameEvent.method1039());
      GhostRotations.INSTANCE.method1904();
   }
}
