package dev.boze.client.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent extends CancelableEvent {
    public BlockEntity blockEntity;
    public MatrixStack matrix;
    public VertexConsumerProvider vertexConsumer;
}
