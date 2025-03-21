package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.NoRender;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SignBlockEntityRenderer.class})
public class SignBlockEntityRendererMixin {
    @Inject(
            method = {"renderText"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void onRenderText(
            BlockPos pos,
            SignText signText,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int lineHeight,
            int lineWidth,
            boolean front,
            CallbackInfo ci
    ) {
        if (NoRender.method1986()) {
            ci.cancel();
        }
    }
}
