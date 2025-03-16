package dev.boze.client.ac;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public interface Anticheat extends IMinecraft {
    BlockHitResult method565(ArrayList<BlockHitResult> var1);

    BlockHitResult method566(BlockPos var1, Direction var2);

    boolean method567(BlockPos var1, Direction var2, boolean var3, boolean var4);

    default boolean method568(BlockPos pos, Direction direction) {
        return this.method567(pos, direction, true, true);
    }
}
