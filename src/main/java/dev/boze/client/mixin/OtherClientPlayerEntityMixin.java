package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IOtherClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherClientPlayerEntityMixin extends PlayerEntityMixin implements IOtherClientPlayerEntity {
    @Unique
    private boolean hasMoved = false;

    @Override
    public boolean boze$doDamage(DamageSource source, float amount) {
        return super.damage(source, amount);
    }

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onDamage(DamageSource var1, float var2, CallbackInfoReturnable<Boolean> var3) {
        if (this.shouldDamage()) {
            var3.setReturnValue(this.returnDamage(var1, var2));
        }
    }

    @Inject(
            method = "setVelocityClient",
            at = @At("HEAD")
    )
    private void onSetVelocityClient(double var1, double var3, double var5, CallbackInfo var7) {
        if (this.age >= 20 && (var1 != 0.0 || var5 != 0.0)) {
            this.hasMoved = true;
        }
    }

    @Override
    public boolean boze$hasMoved() {
        return this.hasMoved;
    }
}
