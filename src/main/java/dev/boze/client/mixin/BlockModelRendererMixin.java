package dev.boze.client.mixin;

import dev.boze.client.systems.modules.render.Tint;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.BufferAllocator.CloseableBuffer;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin({BlockModelRenderer.class})
public class BlockModelRendererMixin {
    @Inject(
            method = {"renderQuad(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFFIIIII)V"},
            at = {@At("TAIL")}
    )
    private void onRenderQuad(
            BlockRenderView var1,
            BlockState var2,
            BlockPos var3,
            VertexConsumer var4,
            Entry var5,
            BakedQuad var6,
            float var7,
            float var8,
            float var9,
            float var10,
            int var11,
            int var12,
            int var13,
            int var14,
            int var15,
            CallbackInfo var16
    ) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3753.getValue()) {
            RGBAColor var19 = Tint.INSTANCE.field3754.getValue();
            this.modifyBuffer(var4, (float) var19.field408 / 255.0F, (float) var19.field409 / 255.0F, (float) var19.field410 / 255.0F, 255);
        }
    }

    @Unique
    private void modifyBuffer(VertexConsumer var1, float var2, float var3, float var4, int var5) {
        if (var1 instanceof BufferBuilderAccessor var6) {
            int var7 = var6.getVertexFormat().getVertexSizeByte();
            CloseableBuffer var8 = var6.getAllocator().getAllocated();

            try {
                if (var8 != null) {
                    ByteBuffer var9 = var8.getBuffer();

                    for (int var10 = 1; var10 <= 4; var10++) {
                        float var11 = (float) (var9.get(var9.capacity() - var7 * var10 + 12) & 255) / 255.0F;
                        float var12 = (float) (var9.get(var9.capacity() - var7 * var10 + 13) & 255) / 255.0F;
                        float var13 = (float) (var9.get(var9.capacity() - var7 * var10 + 14) & 255) / 255.0F;
                        float var14 = var11 * var2;
                        float var15 = var12 * var3;
                        float var16 = var13 * var4;
                        var9.put(var9.capacity() - var7 * var10 + 12, (byte) ((int) (var14 * 255.0F)));
                        var9.put(var9.capacity() - var7 * var10 + 13, (byte) ((int) (var15 * 255.0F)));
                        var9.put(var9.capacity() - var7 * var10 + 14, (byte) ((int) (var16 * 255.0F)));
                        var9.put(var9.capacity() - var7 * var10 + 15, (byte) var5);
                    }
                }
            } catch (Throwable var18) {
                if (var8 != null) {
                    try {
                        var8.close();
                    } catch (Throwable var17) {
                        var18.addSuppressed(var17);
                    }
                }

                throw var18;
            }

            if (var8 != null) {
                var8.close();
            }
        }
    }
}
