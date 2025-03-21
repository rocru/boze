package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.FullBright;
import dev.boze.client.systems.modules.render.NameTags;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityRenderer.class})
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(
            method = {"renderLabelIfPresent"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void onRenderLabel(T var1, Text var2, MatrixStack var3, VertexConsumerProvider var4, int var5, float var6, CallbackInfo var7) {
        if (var1 instanceof PlayerEntity) {
            if (NameTags.INSTANCE.isEnabled()) {
                var7.cancel();
            }
        }
    }

    @Inject(
            method = {"getSkyLight"},
            at = {@At("RETURN")},
            cancellable = true
    )
    private void onGetSkyLight(CallbackInfoReturnable<Integer> var1) {
        var1.setReturnValue(Math.max(FullBright.field3569, var1.getReturnValueI()));
    }
}
