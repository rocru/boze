package dev.boze.client.mixin;

import dev.boze.client.Boze;
import dev.boze.client.events.CanWalkOnFluidEvent;
import dev.boze.client.events.PlayerTravelEvent;
import dev.boze.client.mixininterfaces.ILivingEntity;
import dev.boze.client.mixininterfaces.ILivingEntityClientAttack;
import dev.boze.client.systems.modules.misc.SoundFX;
import dev.boze.client.systems.modules.movement.*;
import dev.boze.client.systems.modules.render.HandTweaks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements ILivingEntityClientAttack, ILivingEntity {
    @Shadow
    public int jumpingCooldown;
    @Shadow
    protected double serverX;
    @Shadow
    protected double serverY;
    @Shadow
    protected double serverZ;
    @Unique
    private long damageSyncTime = 0L;
    @Unique
    private Vec3d lastServerPos;

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> var1);

    @Shadow
    @Nullable
    public abstract StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> var1);

    @Override
    public long boze$getDamageSyncTime() {
        return this.damageSyncTime;
    }

    @Override
    public void boze$setDamageSyncTime(long damageSyncTime) {
        this.damageSyncTime = damageSyncTime;
    }

    @Override
    public Vec3d boze$getLastServerPos() {
        return this.lastServerPos;
    }

    @Inject(
            method = "updateTrackedPositionAndAngles",
            at = @At("HEAD")
    )
    public void updateLastServerPos(double x, double y, double z, float yaw, float pitch, int interpolationSteps, CallbackInfo ci) {
        this.lastServerPos = new Vec3d(this.serverX, this.serverY, this.serverZ);
    }

    @Inject(
            method = "consumeItem",
            at = @At("HEAD")
    )
    private void onConsumeItem(CallbackInfo var1) {
        NoSlow.INSTANCE.method1852();
    }

    @Redirect(
            method = "damage",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;isClient:Z"
            )
    )
    private boolean isWorldClient(World var1) {
        return var1.isClient && !this.shouldClientAttack();
    }

    @Inject(
            method = "setHealth",
            at = @At("HEAD")
    )
    private void onSetHealth(float var1, CallbackInfo var2) {
        if (SoundFX.INSTANCE.isEnabled()) {
            SoundFX.INSTANCE.method1772(var1, (LivingEntity) (Object) this);
        }
    }

    @Inject(
            method = "travel",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onTravel(Vec3d var1, CallbackInfo var2) {
        if ((Object) this instanceof ClientPlayerEntity) {
            PlayerTravelEvent var3 = PlayerTravelEvent.method1047();
            Boze.EVENT_BUS.post(var3);
            if (var3.method1022()) {
                var2.cancel();
            }
        }
    }

    @Inject(
            method = "isFallFlying",
            at = @At("TAIL"),
            cancellable = true
    )
    public void recastIfLanded(CallbackInfoReturnable<Boolean> cir) {
        if (ElytraBoost.INSTANCE.field1011.field3208) {
            if (ElytraBoost.INSTANCE.field1011.field3210) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(true);
            }
        } else {
            if (ElytraRecast.INSTANCE.isEnabled() && ElytraRecast.INSTANCE.field582) {
                boolean var4 = cir.getReturnValue();
                if (ElytraRecast.INSTANCE.field581 && !var4) {
                    cir.setReturnValue(ElytraRecast.INSTANCE.method1971());
                }

                ElytraRecast.INSTANCE.field581 = var4;
                ElytraRecast.INSTANCE.field580.reset();
            }
        }
    }

    @Inject(
            method = "tickMovement",
            at = @At("HEAD"),
            cancellable = true
    )
    public void boze$tickMovement$head(CallbackInfo ci) {
        if (ElytraRecast.INSTANCE.isEnabled() && this.jumpingCooldown > 2 && this.equals(MinecraftClient.getInstance().player)) {
            this.jumpingCooldown = 2;
        }

        if (MinecraftClient.getInstance().player != null && this.equals(MinecraftClient.getInstance().player.getVehicle()) && BoatFly.INSTANCE.method1797()) {
            ci.cancel();
        }
    }

    @Redirect(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z"
            )
    )
    public boolean onPotionCheck(LivingEntity instance, RegistryEntry<StatusEffect> effect) {
        return (!AntiLevitation.INSTANCE.isEnabled() || instance != MinecraftClient.getInstance().player || effect != StatusEffects.LEVITATION) && instance.hasStatusEffect(effect);
    }

    @Inject(
            method = "canWalkOnFluid",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onCanWalkOnFluid(FluidState var1, CallbackInfoReturnable<Boolean> var2) {
        if ((Object) this == MinecraftClient.getInstance().player) {
            CanWalkOnFluidEvent var3 = Boze.EVENT_BUS.post(CanWalkOnFluidEvent.method1049(var1));
            var2.setReturnValue(var3.method1022());
        }
    }

    @Inject(
            method = "getHandSwingDuration",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetHandSwingDuration(CallbackInfoReturnable<Integer> var1) {
        if (HandTweaks.INSTANCE.isEnabled()) {
            if (this.hasStatusEffect(StatusEffects.HASTE)) {
                var1.setReturnValue(HandTweaks.INSTANCE.field3574.getValue() - (1 + this.getStatusEffect(StatusEffects.HASTE).getAmplifier()));
                return;
            }

            if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
                var1.setReturnValue(HandTweaks.INSTANCE.field3574.getValue() + (1 + this.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) * 2);
                return;
            }

            var1.setReturnValue(HandTweaks.INSTANCE.field3574.getValue());
        }
    }
}
