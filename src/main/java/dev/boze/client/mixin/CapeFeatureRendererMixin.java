package dev.boze.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import dev.boze.client.systems.modules.client.Capes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {
    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/SkinTextures;capeTexture()Lnet/minecraft/util/Identifier;"
            )
    )
    private Identifier onGetCapeTexture(
            Identifier var1,
            MatrixStack var2,
            VertexConsumerProvider var3,
            int var4,
            AbstractClientPlayerEntity var5,
            float var6,
            float var7,
            float var8,
            float var9,
            float var10,
            float var11
    ) {
        if (Capes.INSTANCE.isEnabled()) {
            GameProfile var14 = var5.getGameProfile();
            if (var14 == null) {
                return var1;
            }

            String var15 = Capes.field1290.getOrDefault(var14.getId().toString(), "none");
            if (var15.equals("default")) {
                return Capes.field1294;
            }

            if (var15.equals("beta")) {
                return Capes.field1295;
            }

            if (!Capes.field1296.isEmpty()) {
                Identifier var16 = Capes.field1296.getOrDefault(var14.getId().toString(), null);
                if (var16 != null) {
                    return var16;
                }
            }
        }

        return var1;
    }
}
