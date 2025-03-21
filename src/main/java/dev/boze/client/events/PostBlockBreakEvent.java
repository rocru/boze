package dev.boze.client.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PostBlockBreakEvent extends BlockEvent {
    private static final PostBlockBreakEvent INSTANCE = new PostBlockBreakEvent();

    public static PostBlockBreakEvent method1032(BlockPos pos, Direction face, int blockHitDelay, float curBlockDamageMP) {
        INSTANCE.blockPos = pos;
        INSTANCE.direction = face;
        INSTANCE.blockBreakingCooldown = blockHitDelay;
        INSTANCE.currentBreakingProgress = curBlockDamageMP;
        INSTANCE.method1021(false);
        return INSTANCE;
    }
}
