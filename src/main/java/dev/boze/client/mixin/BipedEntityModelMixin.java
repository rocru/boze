package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.HandTweaks;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BipedEntityModel.class})
public abstract class BipedEntityModelMixin<T extends LivingEntity> {
   @Shadow
   protected abstract void positionRightArm(T var1);

   @Shadow
   protected abstract void positionLeftArm(T var1);

   @Inject(
      method = {"setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V",
         shift = Shift.BEFORE
      )}
   )
   private void positionBlocking(T var1, float var2, float var3, float var4, float var5, float var6, CallbackInfo var7) {
      if (HandTweaks.method1960(var1)) {
         if (var1.getOffHandStack().getItem() instanceof ShieldItem) {
            this.positionRightArm((T)var1);
         } else {
            this.positionLeftArm((T)var1);
         }
      }
   }
}
