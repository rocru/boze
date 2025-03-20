package dev.boze.client.mixin;

import dev.boze.client.Boze;
import dev.boze.client.events.BoatEntityMoveEvent;
import dev.boze.client.events.LivingEntityMoveEvent;
import dev.boze.client.events.PlayerPositionEvent;
import dev.boze.client.events.PlayerPushEvent;
import dev.boze.client.mixininterfaces.IEntity;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.Hitboxes;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.fakeplayer.FakeClientPlayerEntity;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin({Entity.class})
public abstract class EntityMixin implements IEntity {
   @Shadow
   public boolean noClip;
   @Shadow
   private Box boundingBox;
   @Shadow
   private World world;
   @Shadow
   protected boolean firstUpdate;
   @Shadow
   protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;
   @Shadow
   public int age;

   @Shadow
   public abstract Box getBoundingBox();

   @Shadow
   public static Vec3d adjustMovementForCollisions(@Nullable Entity entity, Vec3d movement, Box entityBoundingBox, World world, List<VoxelShape> collisions) {
      return null;
   }

   @Shadow
   public abstract Vec3d getRotationVector(float var1, float var2);

   @Override
   public boolean boze$isInWater() {
      return !this.firstUpdate && this.fluidHeight.getDouble(FluidTags.WATER) > 0.0;
   }

   @Inject(
      method = {"getRotationVec"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void injectRotationFix(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
      if (this == MinecraftClient.getInstance().player) {
         RotationHelper var5 = GhostRotations.INSTANCE.field760;
         if (var5 != null) {
            cir.setReturnValue(this.getRotationVector(var5.method1385(), var5.method1384()));
         } else if (!Options.INSTANCE.method1971() && AntiCheat.INSTANCE.field2314.getValue() && !Options.field994.hasElapsed(100.0)) {
            cir.setReturnValue(this.getRotationVector(((ClientPlayerEntityAccessor)this).getLastPitch(), ((ClientPlayerEntityAccessor)this).getLastYaw()));
         }
      }
   }

   @Inject(
      method = {"pushAwayFrom"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushAwayFrom(Entity var1, CallbackInfo var2) {
      if (this == MinecraftClient.getInstance().player) {
         PlayerPushEvent var3 = (PlayerPushEvent) Boze.EVENT_BUS.post(PlayerPushEvent.method1083());
         if (var3.method1022()) {
            var2.cancel();
         }
      } else if (var1 instanceof FakePlayerEntity || var1 instanceof FakeClientPlayerEntity) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"move"},
      at = {@At("HEAD")}
   )
   private void onMove(MovementType var1, Vec3d var2, CallbackInfo var3) {
      if (this instanceof LivingEntity) {
         Boze.EVENT_BUS.post(LivingEntityMoveEvent.method1071((LivingEntity)this, var2));
      } else if (this instanceof BoatEntity) {
         Boze.EVENT_BUS.post(BoatEntityMoveEvent.method1051((BoatEntity)this, var2));
      }
   }

   @Inject(
      method = {"getTargetingMargin"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetTargetingMargin(CallbackInfoReturnable<Float> var1) {
      float var4 = (float)Hitboxes.INSTANCE.method1603((Entity)this);
      if (var4 != 0.0F) {
         var1.setReturnValue(var4);
      }
   }

   @Inject(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;setPosition(DDD)V",
         shift = Shift.BEFORE,
         ordinal = 1
      )},
      locals = LocalCapture.CAPTURE_FAILSOFT
   )
   private void inject$stepEvent(MovementType var1, Vec3d var2, CallbackInfo var3, Vec3d var4) {
      if (this == MinecraftClient.getInstance().player) {
         Boze.EVENT_BUS.post(PlayerPositionEvent.method1081(var4.y));
      }
   }
}
