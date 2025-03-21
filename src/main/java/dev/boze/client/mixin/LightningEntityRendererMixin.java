package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LightningEntityRenderer.class})
public class LightningEntityRendererMixin {
    @Inject(
            method = {"drawBranch"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void onDrawBranch(
            Matrix4f var0,
            VertexConsumer var1,
            float var2,
            float var3,
            int var4,
            float var5,
            float var6,
            float var7,
            float var8,
            float var9,
            float var10,
            float var11,
            boolean var12,
            boolean var13,
            boolean var14,
            boolean var15,
            CallbackInfo var16
    ) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3751.getValue()) {
            RGBAColor var19 = Tint.INSTANCE.field3752.getValue();
            var1.vertex(var0, var2 + (var12 ? var11 : -var11), (float) (var4 * 16), var3 + (var13 ? var11 : -var11))
                    .color((float) var19.field408 / 255.0F, (float) var19.field409 / 255.0F, (float) var19.field410 / 255.0F, (float) var19.field411 / 255.0F);
            var1.vertex(var0, var5 + (var12 ? var10 : -var10), (float) ((var4 + 1) * 16), var6 + (var13 ? var10 : -var10))
                    .color((float) var19.field408 / 255.0F, (float) var19.field409 / 255.0F, (float) var19.field410 / 255.0F, (float) var19.field411 / 255.0F);
            var1.vertex(var0, var5 + (var14 ? var10 : -var10), (float) ((var4 + 1) * 16), var6 + (var15 ? var10 : -var10))
                    .color((float) var19.field408 / 255.0F, (float) var19.field409 / 255.0F, (float) var19.field410 / 255.0F, (float) var19.field411 / 255.0F);
            var1.vertex(var0, var2 + (var14 ? var11 : -var11), (float) (var4 * 16), var3 + (var15 ? var11 : -var11))
                    .color((float) var19.field408 / 255.0F, (float) var19.field409 / 255.0F, (float) var19.field410 / 255.0F, (float) var19.field411 / 255.0F);
            var16.cancel();
        }
    }
}
