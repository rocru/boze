package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({PlayerEntityModel.class})
public class PlayerEntityModelMixin {
   @Redirect(
      method = {"setAngles*"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;isInSneakingPose()Z"
      )
   )
   private boolean onIsInSneakingPose(LivingEntity var1) {
      return NoRender.method1992() ? true : var1.isSneaking();
   }
}
