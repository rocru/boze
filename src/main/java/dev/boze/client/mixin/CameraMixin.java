package dev.boze.client.mixin;

import dev.boze.client.systems.modules.movement.ElytraRecast;
import dev.boze.client.systems.modules.render.CameraClip;
import dev.boze.client.systems.modules.render.FreeCam;
import dev.boze.client.systems.modules.render.FreeLook;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Camera.class})
public abstract class CameraMixin {
    @Shadow
    private boolean thirdPerson;
    @Unique
    private float tickDelta;

    @Shadow
    protected abstract float clipToSpace(float var1);

    @ModifyArgs(
            method = {"update"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;moveBy(FFF)V",
                    ordinal = 0
            )
    )
    private void onUpdateMoveBy(Args var1) {
        var1.set(0, -this.clipToSpace(CameraClip.INSTANCE.isEnabled() ? CameraClip.INSTANCE.field3458.getValue() : 4.0F));
    }

    @Inject(
            method = {"clipToSpace"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onClipToSpace(float var1, CallbackInfoReturnable<Float> var2) {
        if (CameraClip.INSTANCE.field3457.getValue()) {
            var2.setReturnValue(var1);
        }
    }

    @Inject(
            method = {"update"},
            at = {@At("HEAD")}
    )
    private void onUpdateHead(BlockView var1, Entity var2, boolean var3, boolean var4, float var5, CallbackInfo var6) {
        this.tickDelta = var5;
    }

    @Inject(
            method = {"update"},
            at = {@At("TAIL")}
    )
    private void onUpdateTail(BlockView var1, Entity var2, boolean var3, boolean var4, float var5, CallbackInfo var6) {
        if (FreeCam.INSTANCE.isEnabled()) {
            this.thirdPerson = true;
        }
    }

    @ModifyArgs(
            method = {"update"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"
            )
    )
    private void onUpdateSetPosArgs(Args var1) {
        if (FreeCam.INSTANCE.isEnabled()) {
            var1.set(0, FreeCam.INSTANCE.method1947(this.tickDelta));
            var1.set(1, FreeCam.INSTANCE.method1948(this.tickDelta));
            var1.set(2, FreeCam.INSTANCE.method1949(this.tickDelta));
        }
    }

    @ModifyArgs(
            method = {"update"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
            )
    )
    private void onUpdateSetRotationArgs(Args var1) {
        if (FreeCam.INSTANCE.isEnabled()) {
            var1.set(0, FreeCam.INSTANCE.method1951(this.tickDelta));
            var1.set(1, FreeCam.INSTANCE.method1950(this.tickDelta));
        } else if (FreeLook.INSTANCE.isEnabled()) {
            var1.set(0, FreeLook.INSTANCE.method1957(this.tickDelta));
            var1.set(1, FreeLook.INSTANCE.method1956(this.tickDelta));
        } else if (ElytraRecast.method1974()) {
            var1.set(0, ElytraRecast.INSTANCE.method1957(this.tickDelta));
            var1.set(1, ElytraRecast.INSTANCE.method1956(this.tickDelta));
        }
    }
}
