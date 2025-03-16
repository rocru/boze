package dev.boze.client.ac;

import dev.boze.client.utils.world.PositionUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

class NearestHitCalculator {
    static BlockHitResult method947(ArrayList<BlockHitResult> var0) {
        Vec3d var1 = PositionUtil.getPlayerPosition();
        return var0.stream().min(Comparator.comparingDouble(result -> result.getPos().distanceTo(var1))).orElse(null);
    }
}