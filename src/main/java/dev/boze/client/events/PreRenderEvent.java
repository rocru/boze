package dev.boze.client.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class PreRenderEvent extends RenderEvent {
   private static final PreRenderEvent INSTANCE = new PreRenderEvent();

   public static PreRenderEvent method1091(BlockEntity blockEntity, MatrixStack matrix, VertexConsumerProvider vertex) {
      INSTANCE.blockEntity = blockEntity;
      INSTANCE.matrix = matrix;
      INSTANCE.vertexConsumer = vertex;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
