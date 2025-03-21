package dev.boze.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.systems.modules.render.NoRender;
import dev.boze.client.systems.modules.render.XRay;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BackgroundRenderer.class})
public class BackgroundRendererMixin {
   @Redirect(
      method = {"render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V", "applyFog"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"
      )
   )
   private static boolean hasStatusEffect(LivingEntity var0, RegistryEntry<StatusEffect> var1) {
      return NoRender.method1980(var1) ? false : var0.hasStatusEffect(var1);
   }

   @Inject(
      method = {"applyFog"},
      at = {@At("TAIL")}
   )
   private static void onApplyFog(Camera var0, FogType var1, float var2, boolean var3, float var4, CallbackInfo var5) {
      if (XRay.INSTANCE.isEnabled() && var1 == FogType.FOG_TERRAIN || NoRender.method1983(var1)) {
         RenderSystem.setShaderFogStart(var2 * 69.0F);
         RenderSystem.setShaderFogEnd(var2 * 69.42F);
      }
   }
}
