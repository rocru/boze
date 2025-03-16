package dev.boze.client.mixin;

import dev.boze.client.enums.HandTweaksHideShield;
import dev.boze.client.systems.modules.render.FullBright;
import dev.boze.client.systems.modules.render.HandTweaks;
import dev.boze.client.systems.modules.render.ViewModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({HeldItemRenderer.class})
public class HeldItemRendererMixin {
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   private ItemStack mainHand;
   @Shadow
   private ItemStack offHand;

   @Inject(
      method = {"renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onRenderItem(
      LivingEntity entity,
      ItemStack stack,
      ModelTransformationMode renderMode,
      boolean leftHanded,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (HandTweaks.INSTANCE.isEnabled()
         && HandTweaks.INSTANCE.field3572.method419()
         && (
            HandTweaks.INSTANCE.field3573.method461() == HandTweaksHideShield.Always && stack.getItem() instanceof ShieldItem
               || HandTweaks.INSTANCE.field3573.method461() == HandTweaksHideShield.On
                  && stack.getItem() instanceof ShieldItem
                  && HandTweaks.method1961(entity)
         )) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
         shift = Shift.BEFORE,
         ordinal = 1
      )}
   )
   public void onRenderItemBlockingTransform(
      AbstractClientPlayerEntity player,
      float tickDelta,
      float pitch,
      Hand hand,
      float swingProgress,
      ItemStack item,
      float equipProgress,
      MatrixStack matrices,
      VertexConsumerProvider vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      if (HandTweaks.method1960(player) && !(item.getItem() instanceof ShieldItem)) {
         boolean var14 = hand == Hand.MAIN_HAND;
         Arm var15 = var14 ? player.getMainArm() : player.getMainArm().getOpposite();
         int var16 = var15 == Arm.RIGHT ? 1 : -1;
         matrices.translate((float)var16 * -0.14142136F, 0.08F, 0.14142136F);
         matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
         matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var16 * 13.365F));
         matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var16 * 78.05F));
      }
   }

   @Inject(
      method = {"applySwingOffset"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onApplySwingOffset(MatrixStack var1, Arm var2, float var3, CallbackInfo var4) {
      if (HandTweaks.INSTANCE.isEnabled()) {
         var4.cancel();
         int var7 = var2 == Arm.RIGHT ? 1 : -1;
         float var8 = MathHelper.sin(var3 * var3 * (float) Math.PI);
         if (HandTweaks.INSTANCE.field3583.method419()) {
            var1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var7 * 45.0F));
         } else {
            var1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var7 * (45.0F + var8 * -20.0F)));
         }

         float var9 = MathHelper.sin(MathHelper.sqrt(var3) * (float) Math.PI);
         if (!HandTweaks.INSTANCE.field3584.method419()) {
            var1.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var7 * var9 * -20.0F));
         }

         if (!HandTweaks.INSTANCE.field3582.method419()) {
            var1.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var9 * -80.0F));
         }

         var1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var7 * -45.0F));
      }
   }

   @Redirect(
      method = {"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
         ordinal = 0
      )
   )
   private void onItemSwayY(MatrixStack var1, Quaternionf var2) {
      if (!HandTweaks.INSTANCE.isEnabled() || !HandTweaks.INSTANCE.field3581.method419()) {
         var1.multiply(var2);
      }
   }

   @Redirect(
      method = {"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
         ordinal = 1
      )
   )
   private void onItemSwayX(MatrixStack var1, Quaternionf var2) {
      if (!HandTweaks.INSTANCE.isEnabled() || !HandTweaks.INSTANCE.field3580.method419()) {
         var1.multiply(var2);
      }
   }

   @ModifyArgs(
      method = {"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
      )
   )
   private void modifyRenderItem(Args var1) {
      if (FullBright.INSTANCE.isEnabled() && FullBright.INSTANCE.field3568.method419()) {
         var1.set(9, LightmapTextureManager.pack(15, 15));
      }

      if (HandTweaks.INSTANCE.isEnabled() && HandTweaks.INSTANCE.field3579.method419()) {
         var1.set(6, 0.0F);
      }
   }

   @Inject(
      method = {"updateHeldItems"},
      at = {@At("TAIL")}
   )
   private void onUpdateHeldItems(CallbackInfo var1) {
      if (HandTweaks.INSTANCE.isEnabled() && HandTweaks.INSTANCE.field3579.method419()) {
         this.mainHand = this.client.player.getMainHandStack();
         this.offHand = this.client.player.getOffHandStack();
      }
   }

   @Redirect(
      method = {"renderFirstPersonItem"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getMainArm()Lnet/minecraft/util/Arm;"
      )
   )
   private Arm onGetMainArm(AbstractClientPlayerEntity var1) {
      return HandTweaks.INSTANCE.isEnabled() && HandTweaks.INSTANCE.field3570.method419() ? var1.getMainArm().getOpposite() : var1.getMainArm();
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")}
   )
   private void onRenderFirstPersonItemPre(
      AbstractClientPlayerEntity var1,
      float var2,
      float var3,
      Hand var4,
      float var5,
      ItemStack var6,
      float var7,
      MatrixStack var8,
      VertexConsumerProvider var9,
      int var10,
      CallbackInfo var11
   ) {
      if (ViewModel.INSTANCE.isEnabled()) {
         var8.push();
         if (ViewModel.INSTANCE.field3838.method419()) {
            var8.translate(ViewModel.INSTANCE.field3839.method423(), ViewModel.INSTANCE.field3840.method423(), ViewModel.INSTANCE.field3841.method423());
            var8.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ViewModel.INSTANCE.field3843.method423()));
            var8.multiply(RotationAxis.POSITIVE_X.rotationDegrees(ViewModel.INSTANCE.field3842.method423()));
            var8.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(ViewModel.INSTANCE.field3844.method423()));
            var8.scale(ViewModel.INSTANCE.field3845.method423(), ViewModel.INSTANCE.field3846.method423(), ViewModel.INSTANCE.field3847.method423());
         } else if (var4 == Hand.MAIN_HAND && ViewModel.INSTANCE.field3848.method419()) {
            var8.translate(ViewModel.INSTANCE.field3849.method423(), ViewModel.INSTANCE.field3850.method423(), ViewModel.INSTANCE.field3851.method423());
            var8.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ViewModel.INSTANCE.field3853.method423()));
            var8.multiply(RotationAxis.POSITIVE_X.rotationDegrees(ViewModel.INSTANCE.field3852.method423()));
            var8.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(ViewModel.INSTANCE.field3854.method423()));
            var8.scale(ViewModel.INSTANCE.field3855.method423(), ViewModel.INSTANCE.field3856.method423(), ViewModel.INSTANCE.field3857.method423());
         } else if (var4 == Hand.OFF_HAND && ViewModel.INSTANCE.field3858.method419()) {
            var8.translate(ViewModel.INSTANCE.field3859.method423(), ViewModel.INSTANCE.field3860.method423(), ViewModel.INSTANCE.field3861.method423());
            var8.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ViewModel.INSTANCE.field3863.method423()));
            var8.multiply(RotationAxis.POSITIVE_X.rotationDegrees(ViewModel.INSTANCE.field3862.method423()));
            var8.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(ViewModel.INSTANCE.field3864.method423()));
            var8.scale(ViewModel.INSTANCE.field3865.method423(), ViewModel.INSTANCE.field3866.method423(), ViewModel.INSTANCE.aa.method423());
         }
      }
   }

   @Inject(
      method = {"applyEatOrDrinkTransformation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onApplyEatOrDrinkTransformation(MatrixStack var1, float var2, Arm var3, ItemStack var4, PlayerEntity var5, CallbackInfo var6) {
      if (HandTweaks.INSTANCE.isEnabled() && HandTweaks.INSTANCE.field3575.method419()) {
         var6.cancel();
         float var10 = (float)this.client.player.getItemUseTimeLeft() - var2 + 1.0F;
         float var11 = var10 / (float)var4.getMaxUseTime(var5);
         if (var11 < 0.8F) {
            float var9 = MathHelper.abs(
               MathHelper.cos(var10 / (float)HandTweaks.INSTANCE.field3576.method434().intValue() * (float) Math.PI)
                  * HandTweaks.INSTANCE.field3577.method423()
            );
            if (HandTweaks.INSTANCE.field3578.method419()) {
               var9 *= -1.0F;
            }

            var1.translate(0.0F, var9, 0.0F);
         }

         float var13 = 1.0F - (float)Math.pow((double)var11, 27.0);
         int var12 = var3 == Arm.RIGHT ? 1 : -1;
         var1.translate(var13 * 0.6F * (float)var12, var13 * -0.5F, var13 * 0.0F);
         var1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)var12 * var13 * 90.0F));
         var1.multiply(RotationAxis.POSITIVE_X.rotationDegrees(var13 * 10.0F));
         var1.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)var12 * var13 * 30.0F));
      }
   }

   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("RETURN")}
   )
   private void onRenderFirstPersonItemPost(
      AbstractClientPlayerEntity var1,
      float var2,
      float var3,
      Hand var4,
      float var5,
      ItemStack var6,
      float var7,
      MatrixStack var8,
      VertexConsumerProvider var9,
      int var10,
      CallbackInfo var11
   ) {
      if (ViewModel.INSTANCE.isEnabled()) {
         var8.pop();
      }
   }

   @Redirect(
      method = {"updateHeldItems"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"
      )
   )
   public float onGetAttackCooldownProgress(ClientPlayerEntity clientPlayerEntity, float baseTime) {
      return HandTweaks.INSTANCE.isEnabled() && HandTweaks.INSTANCE.field3571.method419() ? 1.0F : clientPlayerEntity.getAttackCooldownProgress(baseTime);
   }
}
