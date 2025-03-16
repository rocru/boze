package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import net.minecraft.client.particle.TotemParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({TotemParticle.class})
public class TotemParticleMixin {
   @Redirect(
      method = {"<init>"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/particle/TotemParticle;setColor(FFF)V",
         ordinal = 0
      )
   )
   private void setColorYellow(TotemParticle var1, float var2, float var3, float var4) {
      if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3730.method419()) {
         var2 = Tint.INSTANCE.field3731.method423() * (1.0F - Tint.INSTANCE.field3732.method423())
            + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3732.method423() * Tint.INSTANCE.field3731.method423();
         var3 = Tint.INSTANCE.field3733.method423() * (1.0F - Tint.INSTANCE.field3734.method423())
            + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3734.method423() * Tint.INSTANCE.field3733.method423();
         var4 = Tint.INSTANCE.field3735.method423() * (1.0F - Tint.INSTANCE.field3736.method423())
            + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3736.method423() * Tint.INSTANCE.field3735.method423();
      }

      var1.setColor(var2, var3, var4);
   }

   @Redirect(
      method = {"<init>"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/particle/TotemParticle;setColor(FFF)V",
         ordinal = 1
      )
   )
   private void setColorGreen(TotemParticle var1, float var2, float var3, float var4) {
      if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3730.method419()) {
         var2 = Tint.INSTANCE.field3737.method423() * (1.0F - Tint.INSTANCE.field3738.method423())
            + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3738.method423() * Tint.INSTANCE.field3737.method423();
         var3 = Tint.INSTANCE.field3739.method423() * (1.0F - Tint.INSTANCE.field3740.method423())
            + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3740.method423() * Tint.INSTANCE.field3739.method423();
         var4 = Tint.INSTANCE.field3741.method423() * (1.0F - Tint.INSTANCE.field3742.method423())
            + Tint.INSTANCE.ai.nextFloat() * Tint.INSTANCE.field3742.method423() * Tint.INSTANCE.field3741.method423();
      }

      var1.setColor(var2, var3, var4);
   }
}
