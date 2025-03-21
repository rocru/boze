package dev.boze.client.mixin;

import dev.boze.client.enums.HandTweaksHideShield;
import dev.boze.client.systems.modules.render.HandTweaks;
import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerEntityRenderer.class})
public class PlayerEntityRendererMixin {
    @Redirect(
            method = {"getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isInSneakingPose()Z"
            )
    )
    private boolean onIsInSneakingPose(AbstractClientPlayerEntity var1) {
        return NoRender.method1992() || var1.isSneaking();
    }

    @Redirect(
            method = {"setModelPose"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isInSneakingPose()Z"
            )
    )
    private boolean onIsInSneakingPoseModel(AbstractClientPlayerEntity var1) {
        return NoRender.method1992() || var1.isSneaking();
    }

    @Inject(
            method = {"getArmPose"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private static void onGetArmPose(AbstractClientPlayerEntity var0, Hand var1, CallbackInfoReturnable<ArmPose> var2) {
        if (HandTweaks.INSTANCE.isEnabled() && HandTweaks.INSTANCE.field3572.getValue()) {
            ItemStack var5 = var0.getStackInHand(var1);
            ItemStack var6 = var0.getStackInHand(var1.equals(Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND);
            if (HandTweaks.INSTANCE.field3573.getValue() == HandTweaksHideShield.Always || !(var5.getItem() instanceof ShieldItem) || HandTweaks.method1961(var0)
            ) {
                if (var6.getItem() instanceof ShieldItem && HandTweaks.method1960(var0)) {
                    var2.setReturnValue(ArmPose.BLOCK);
                } else if (var5.getItem() instanceof ShieldItem
                        && HandTweaks.INSTANCE.field3573.getValue() != HandTweaksHideShield.Off
                        && (var2.getReturnValue() == ArmPose.ITEM || var2.getReturnValue() == ArmPose.BLOCK)) {
                    var2.setReturnValue(ArmPose.EMPTY);
                }
            }
        }
    }
}
