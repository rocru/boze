package dev.boze.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import dev.boze.client.systems.modules.client.Capes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ElytraFeatureRenderer.class})
public abstract class ElytraFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public ElytraFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @ModifyExpressionValue(
            method = {"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/SkinTextures;capeTexture()Lnet/minecraft/util/Identifier;"
            )}
    )
    private Identifier modifyCapeTexture(
            Identifier var1,
            MatrixStack var2,
            VertexConsumerProvider var3,
            int var4,
            T var5,
            float var6,
            float var7,
            float var8,
            float var9,
            float var10,
            float var11
    ) {
        if (var5 instanceof AbstractClientPlayerEntity var14) {
            if (Capes.INSTANCE.isEnabled()) {
                GameProfile var15 = var14.getGameProfile();
                if (var15 == null) {
                    return var1;
                }

                String var16 = Capes.field1290.getOrDefault(var15.getId().toString(), "none");
                if (var16.equals("default")) {
                    return Capes.field1294;
                }

                if (var16.equals("beta")) {
                    return Capes.field1295;
                }

                if (!Capes.field1296.isEmpty()) {
                    Identifier var17 = Capes.field1296.getOrDefault(var15.getId().toString(), null);
                    if (var17 != null) {
                        return var17;
                    }
                }
            }

            return var1;
        } else {
            return var1;
        }
    }
}
