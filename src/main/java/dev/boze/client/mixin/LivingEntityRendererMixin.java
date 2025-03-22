package dev.boze.client.mixin;

import dev.boze.client.Boze;
import dev.boze.client.enums.ChamsMode;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.ESP;
import dev.boze.client.systems.modules.render.FreeCam;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Unique
    private float lastPartialTick;

    @Shadow
    @Nullable
    protected abstract RenderLayer getRenderLayer(T var1, boolean var2, boolean var3, boolean var4);

    @Inject(
            method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHasLabel(T var1, CallbackInfoReturnable<Boolean> var2) {
        if (FreeCam.INSTANCE.isEnabled() && MinecraftClient.isHudEnabled()) {
            var2.setReturnValue(true);
        }
    }

    @ModifyVariable(
            method = "render*",
            ordinal = 2,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private float calculateRenderBodyYaw(float var1, LivingEntity var2) {
        return !Boze.isInventory && !Options.field993 && var2 == MinecraftClient.getInstance().player && MinecraftClient.getInstance().player != null
                ? MathHelper.lerpAngleDegrees(
                this.lastPartialTick, Boze.prevLastYaw, ((ClientPlayerEntityAccessor) MinecraftClient.getInstance().player).getLastYaw()
        )
                : var1;
    }

    @ModifyVariable(
            method = "render*",
            ordinal = 3,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private float calculateRenderHeadYaw(float var1, LivingEntity var2) {
        return !Boze.isInventory && !Options.field993 && var2 == MinecraftClient.getInstance().player && MinecraftClient.getInstance().player != null
                ? MathHelper.lerpAngleDegrees(
                this.lastPartialTick, Boze.prevLastYaw, ((ClientPlayerEntityAccessor) MinecraftClient.getInstance().player).getLastYaw()
        )
                : var1;
    }

    @ModifyVariable(
            method = "render*",
            ordinal = 5,
            at = @At(
                    value = "STORE",
                    ordinal = 3
            )
    )
    private float calculateRenderPitch(float var1, LivingEntity var2) {
        return !Boze.isInventory && !Options.field993 && var2 == MinecraftClient.getInstance().player && MinecraftClient.getInstance().player != null
                ? MathHelper.lerpAngleDegrees(
                this.lastPartialTick, Boze.prevLastPitch, ((ClientPlayerEntityAccessor) MinecraftClient.getInstance().player).getLastPitch()
        )
                : var1;
    }

    @Inject(
            method = "render*",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderHead(T var1, float var2, float var3, MatrixStack var4, VertexConsumerProvider var5, int var6, CallbackInfo var7) {
        this.lastPartialTick = var3;
        if (ESP.INSTANCE.isEnabled() && !ChamsShaderRenderer.field2247 && ESP.INSTANCE.method1933(var1)) {
            var7.cancel();
        } else {
            if (Chams.INSTANCE.isEnabled()
                    && Chams.INSTANCE.field3463.getValue() == ChamsMode.Both
                    && Chams.INSTANCE.method1924(var1)
                    && !ChamsShaderRenderer.field2248
                    && !ChamsShaderRenderer.field2247) {
                var7.cancel();
            }
        }
    }

    @ModifyArgs(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
            )
    )
    private void modifyColor(Args var1, T var2, float var3, float var4, MatrixStack var5, VertexConsumerProvider var6, int var7) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.field3462.getValue() && Chams.INSTANCE.method1924(var2)) {
            RGBAColor var10 = Chams.INSTANCE.method1925(var2);
            var1.set(4, var10.method2010());
        }
    }
}
