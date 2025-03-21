package dev.boze.client.events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

public class CollisionEvent {
    private static final CollisionEvent field1909 = new CollisionEvent();
    public BlockState field1910;
    public BlockPos blockPos;
    public VoxelShape voxelShape;
    public CollisionType collisionType;

    public static CollisionEvent method1056(BlockState state, BlockPos pos, CollisionType type) {
        field1909.field1910 = state;
        field1909.blockPos = pos;
        field1909.voxelShape = null;
        field1909.collisionType = type;
        return field1909;
    }
}
