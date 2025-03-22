package dev.boze.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem.ShapeIndexBuffer;
import dev.boze.client.renderer.GL;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BuiltBuffer.DrawParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.ByteBuffer;

@Mixin(VertexBuffer.class)
public class VertexBufferMixin {
    @Shadow
    private int indexBufferId;

    @Inject(
            method = "uploadIndexBuffer(Lnet/minecraft/client/render/BuiltBuffer$DrawParameters;Ljava/nio/ByteBuffer;)Lcom/mojang/blaze3d/systems/RenderSystem$ShapeIndexBuffer;",
            at = @At("RETURN")
    )
    private void onUploadIndexBuffer(DrawParameters var1, ByteBuffer var2, CallbackInfoReturnable<ShapeIndexBuffer> var3) {
        if (var3.getReturnValue() == null) {
            GL.field2162 = this.indexBufferId;
        } else {
            GL.field2162 = ((ShapeIndexBufferAccessor) (Object) var3.getReturnValue()).getId();
        }
    }
}
