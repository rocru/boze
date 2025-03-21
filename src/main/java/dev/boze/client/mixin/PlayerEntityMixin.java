package dev.boze.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.boze.client.Boze;
import dev.boze.client.events.ClipAtLedgeEvent;
import dev.boze.client.events.PlayerGrimV3BypassEvent;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.systems.modules.movement.Flight;
import dev.boze.client.utils.player.RotationHandler;
import mapped.Class2839;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({PlayerEntity.class})
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Shadow
    public boolean damage(DamageSource source, float amount) {
        throw new IllegalStateException("Mixins application failed");
    }

    @Shadow
    public abstract void remove(RemovalReason var1);

    @Inject(
            method = {"tick"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z",
                    ordinal = 1,
                    shift = Shift.BEFORE
            )}
    )
    private void noClipHook(CallbackInfo var1) {
        if (!this.noClip && Flight.INSTANCE.isEnabled() && Flight.INSTANCE.field3279.getValue()) {
            this.noClip = true;
        }
    }

    @Inject(
            method = {"clipAtLedge"},
            at = {@At("HEAD")},
            cancellable = true
    )
    protected void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        if (this.equals(MinecraftClient.getInstance().player)) {
            ClipAtLedgeEvent var2 = Boze.EVENT_BUS.post(ClipAtLedgeEvent.method1055());
            if (var2.method1022()) {
                info.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = {"travel"},
            at = {@At("HEAD")}
    )
    private void onTravelPre(Vec3d var1, CallbackInfo var2) {
        if (MinecraftClient.getInstance().player != null) {
            if (this.equals(MinecraftClient.getInstance().player)) {
                if (RotationHandler.field1546.method2114()) {
                    Class2839.field112 = MinecraftClient.getInstance().player.getYaw();
                    MinecraftClient.getInstance().player.setYaw(RotationHandler.field1546.method1384());
                } else {
                    PlayerGrimV3BypassEvent var3 = PlayerGrimV3BypassEvent.method1079();
                    Boze.EVENT_BUS.post(var3);
                    Class2839.field112 = MinecraftClient.getInstance().player.getYaw();
                    if (var3.method1022()) {
                        MinecraftClient.getInstance().player.setYaw(var3.yaw);
                    }
                }
            }
        }
    }

    @Inject(
            method = {"travel"},
            at = {@At("RETURN")}
    )
    private void onTravelPost(Vec3d var1, CallbackInfo var2) {
        if (MinecraftClient.getInstance().player != null) {
            if (this.equals(MinecraftClient.getInstance().player)) {
                MinecraftClient.getInstance().player.setYaw(Class2839.field112);
            }
        }
    }

    @Inject(
            method = {"jump"},
            at = {@At("HEAD")}
    )
    private void onJumpPre(CallbackInfo var1) {
        if (MinecraftClient.getInstance().player != null) {
            if (this.equals(MinecraftClient.getInstance().player)) {
                if (RotationHandler.field1546.method2114()) {
                    Class2839.field112 = MinecraftClient.getInstance().player.getYaw();
                    MinecraftClient.getInstance().player.setYaw(RotationHandler.field1546.method1384());
                } else {
                    PlayerGrimV3BypassEvent var2 = PlayerGrimV3BypassEvent.method1079();
                    Boze.EVENT_BUS.post(var2);
                    Class2839.field112 = MinecraftClient.getInstance().player.getYaw();
                    if (var2.method1022()) {
                        MinecraftClient.getInstance().player.setYaw(var2.yaw);
                    }
                }
            }
        }
    }

    @Inject(
            method = {"jump"},
            at = {@At("RETURN")}
    )
    private void onJumpPost(CallbackInfo var1) {
        if (MinecraftClient.getInstance().player != null) {
            if (this.equals(MinecraftClient.getInstance().player)) {
                MinecraftClient.getInstance().player.setYaw(Class2839.field112);
            }
        }
    }

    @ModifyReturnValue(
            method = {"getBlockInteractionRange"},
            at = {@At("RETURN")}
    )
    private double modifyReturnBlockInteractionRange(double var1) {
        return Reach.method1614();
    }

    @ModifyReturnValue(
            method = {"getEntityInteractionRange"},
            at = {@At("RETURN")}
    )
    private double modifyReturnEntityInteractionRange(double var1) {
        return Reach.method1613();
    }
}
