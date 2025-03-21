package dev.boze.client.mixin;

import dev.boze.client.enums.ChamsMode;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.render.Chams;
import dev.boze.client.systems.modules.render.ESP;
import dev.boze.client.utils.RGBAColor;
import mapped.Class2839;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({EndCrystalEntityRenderer.class})
public class EndCrystalEntityRendererMixin {
    @Shadow
    @Final
    public ModelPart core;
    @Shadow
    @Final
    public ModelPart frame;

    @Inject(
            method = {"render*"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onRender(EndCrystalEntity var1, float var2, float var3, MatrixStack var4, VertexConsumerProvider var5, int var6, CallbackInfo var7) {
        if (ESP.INSTANCE.isEnabled() && !ChamsShaderRenderer.field2247 && ESP.INSTANCE.method1933(var1)) {
            var7.cancel();
        }

        if (Chams.INSTANCE.isEnabled()
                && (Chams.INSTANCE.field3463.getValue() == ChamsMode.Normal || Chams.INSTANCE.field3463.getValue() == ChamsMode.Both)
                && Chams.INSTANCE.method1924(var1)
                && !ChamsShaderRenderer.field2248
                && !ChamsShaderRenderer.field2247) {
            var7.cancel();
        } else {
            Class2839.field114 = Chams.INSTANCE.method1924(var1);
        }
    }

    @ModifyArgs(
            method = {"render*"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
                    ordinal = 0
            )
    )
    private void modifyScale(Args var1) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue() && Class2839.field114) {
            var1.set(0, 2.0F * Chams.INSTANCE.ae.getValue());
            var1.set(1, 2.0F * Chams.INSTANCE.af.getValue());
            var1.set(2, 2.0F * Chams.INSTANCE.ae.getValue());
        }
    }

    @ModifyArgs(
            method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"
            )
    )
    private void modifySpin(Args var1) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue() && Class2839.field114) {
            var1.set(0, (Float) var1.get(0) * Chams.INSTANCE.ag.getValue());
        }
    }

    @Inject(
            method = {"getYOffset"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void onGetYOffset(EndCrystalEntity var0, float var1, CallbackInfoReturnable<Float> var2) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue() && Class2839.field114) {
            var2.cancel();
            float var5 = (float) var0.endCrystalAge + var1;
            float var6 = MathHelper.sin(var5 * 0.2F) / 2.0F + 0.5F;
            var6 = (var6 * var6 + var6) * 0.4F * Chams.INSTANCE.ah.getValue();
            var2.setReturnValue(var6 - 1.4F);
        }
    }

    @Redirect(
            method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    ordinal = 3
            )
    )
    public void redirectCoreRender(ModelPart modelPart, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue() && Class2839.field114) {
            RGBAColor var8 = Chams.INSTANCE.ai.getValue();
            this.core.render(matrices, vertices, light, overlay, var8.method2010());
        } else {
            this.core.render(matrices, vertices, light, overlay);
        }
    }

    @Redirect(
            method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    ordinal = 1
            )
    )
    private void redirectFrame1(ModelPart var1, MatrixStack var2, VertexConsumer var3, int var4, int var5) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue() && Class2839.field114) {
            RGBAColor var8 = Chams.INSTANCE.aj.getValue();
            this.frame.render(var2, var3, var4, var5, var8.method2010());
        } else {
            this.frame.render(var2, var3, var4, var5);
        }
    }

    @Redirect(
            method = {"render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                    ordinal = 2
            )
    )
    private void redirectFrame2(ModelPart var1, MatrixStack var2, VertexConsumer var3, int var4, int var5) {
        if (Chams.INSTANCE.isEnabled() && Chams.INSTANCE.ad.getValue() && Class2839.field114) {
            RGBAColor var8 = Chams.INSTANCE.aj.getValue();
            this.frame.render(var2, var3, var4, var5, var8.method2010());
        } else {
            this.frame.render(var2, var3, var4, var5);
        }
    }
}
