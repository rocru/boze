package dev.boze.client.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class PostRenderEvent extends RenderEvent {
   private static final PostRenderEvent INSTANCE = new PostRenderEvent();

   public static PostRenderEvent method1087(BlockEntity blockEntity, MatrixStack matrix, VertexConsumerProvider vertex) {
      INSTANCE.blockEntity = blockEntity;
      INSTANCE.matrix = matrix;
      INSTANCE.vertexConsumer = vertex;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
