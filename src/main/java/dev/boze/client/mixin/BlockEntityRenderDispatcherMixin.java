package dev.boze.client.mixin;

import dev.boze.client.events.PostRenderEvent;
import dev.boze.client.events.PreRenderEvent;
import mapped.Class27;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BlockEntityRenderDispatcher.class})
public class BlockEntityRenderDispatcherMixin {
   @Shadow
   private static <T extends BlockEntity> void render(BlockEntityRenderer<T> var0, T var1, float var2, MatrixStack var3, VertexConsumerProvider var4) {
   }

   @Redirect(
      method = {"*"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"
      )
   )
   private static <T extends BlockEntity> void render_render(BlockEntityRenderer<T> var0, T var1, float var2, MatrixStack var3, VertexConsumerProvider var4) {
      PreRenderEvent var5 = (PreRenderEvent)Class27.EVENT_BUS.post(PreRenderEvent.method1091(var1, var3, var4));
      if (!var5.method1022()) {
         render(var0, var5.blockEntity, var2, var5.matrix, var5.vertexConsumer);
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"},
      at = {@At("RETURN")}
   )
   private static <T extends BlockEntity> void render(
      BlockEntityRenderer<T> var0, T var1, float var2, MatrixStack var3, VertexConsumerProvider var4, CallbackInfo var5
   ) {
      Class27.EVENT_BUS.post(PostRenderEvent.method1087(var1, var3, var4));
   }
}
