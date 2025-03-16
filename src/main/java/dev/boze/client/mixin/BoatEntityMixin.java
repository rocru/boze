package dev.boze.client.mixin;

import dev.boze.client.systems.modules.movement.AutoWalk;
import dev.boze.client.systems.modules.movement.BoatFly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BoatEntity.class})
public abstract class BoatEntityMixin extends Entity {
   @Unique
   private boolean ignore = false;

   public BoatEntityMixin(EntityType<?> type, World world) {
      super(type, world);
   }

   @Shadow
   public abstract void setInputs(boolean var1, boolean var2, boolean var3, boolean var4);

   @Inject(
      method = {"setInputs"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSetInput(boolean var1, boolean var2, boolean var3, boolean var4, CallbackInfo var5) {
      if (!this.ignore) {
         if (AutoWalk.INSTANCE.isEnabled()) {
            this.ignore = true;
            this.setInputs(var1, var2, var3 || !AutoWalk.INSTANCE.field3147.method419(), var4 || AutoWalk.INSTANCE.field3147.method419());
            var5.cancel();
            this.ignore = false;
         }
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/vehicle/BoatEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
      )},
      cancellable = true
   )
   private void onTickInvokeMove(CallbackInfo var1) {
      if (BoatFly.INSTANCE.method1797()) {
         var1.cancel();
      }
   }

   @Inject(
      method = {"updatePaddles"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onUpdatePaddles(CallbackInfo var1) {
      if (BoatFly.INSTANCE.method1792()) {
         var1.cancel();
      }
   }
}
