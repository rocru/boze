package dev.boze.client.mixin;

import dev.boze.client.systems.modules.movement.TridentPlus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({TridentItem.class})
public class TridentItemMixin {
    @ModifyArgs(
            method = {"onStoppedUsing"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;addVelocity(DDD)V"
            )
    )
    private void modifyVelocity(Args var1) {
        double var4 = TridentPlus.INSTANCE.isEnabled() ? TridentPlus.INSTANCE.field3360.getValue() : 1.0;
        boolean var6 = TridentPlus.INSTANCE.isEnabled() && TridentPlus.INSTANCE.field3364.getValue().isPressed();
        if (var6) {
            var1.set(0, -MinecraftClient.getInstance().player.getVelocity().x);
            var1.set(1, -MinecraftClient.getInstance().player.getVelocity().y);
            var1.set(2, -MinecraftClient.getInstance().player.getVelocity().z);
        } else {
            var1.set(0, (Double) var1.get(0) * var4);
            var1.set(1, (Double) var1.get(1) * var4);
            var1.set(2, (Double) var1.get(2) * var4);
        }
    }

    @ModifyConstant(
            method = {"onStoppedUsing"},
            constant = {@Constant(
                    intValue = 10
            )}
    )
    public int modifyDelay(int original) {
        return TridentPlus.INSTANCE.isEnabled() ? 0 : original;
    }

    @Redirect(
            method = {"use"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"
            )
    )
    private boolean isInWaterUse(PlayerEntity var1) {
        return TridentPlus.INSTANCE.isEnabled() && TridentPlus.INSTANCE.field3361.getValue() || var1.isTouchingWaterOrRain();
    }

    @Redirect(
            method = {"onStoppedUsing"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"
            )
    )
    private boolean isInWaterPostUse(PlayerEntity var1) {
        return TridentPlus.INSTANCE.isEnabled() && TridentPlus.INSTANCE.field3361.getValue() || var1.isTouchingWaterOrRain();
    }
}
