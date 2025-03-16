package dev.boze.client.mixin;

import dev.boze.client.enums.PlayerOverlay;
import dev.boze.client.events.PlayerOverlayEvent;
import dev.boze.client.Boze;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameOverlayRenderer.class})
public class InGameOverlayRendererMixin {
   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onRenderFireOverlay(MinecraftClient var0, MatrixStack var1, CallbackInfo var2) {
      PlayerOverlayEvent var3 = (PlayerOverlayEvent) Boze.EVENT_BUS.post(PlayerOverlayEvent.method1080(PlayerOverlay.Fire));
      if (var3.method1022()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"renderUnderwaterOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onRenderUnderwaterOverlay(MinecraftClient var0, MatrixStack var1, CallbackInfo var2) {
      PlayerOverlayEvent var3 = (PlayerOverlayEvent) Boze.EVENT_BUS.post(PlayerOverlayEvent.method1080(PlayerOverlay.Liquid));
      if (var3.method1022()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"renderInWallOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void render(Sprite var0, MatrixStack var1, CallbackInfo var2) {
      PlayerOverlayEvent var3 = (PlayerOverlayEvent) Boze.EVENT_BUS.post(PlayerOverlayEvent.method1080(PlayerOverlay.Wall));
      if (var3.method1022()) {
         var2.cancel();
      }
   }
}
