package dev.boze.client.systems.modules.combat.autocrystal;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface IPlace {
    BlockHitResult method113(BlockPos var1);

    BlockHitResult method114(BlockPos var1);

    Vec3d method115(Vec3d var1);

    boolean method116(Vec3d var1);

    boolean method117(Vec3d var1, Vec3d var2);

    boolean method118(Vec3d var1, float[] var2);

    boolean method119(Vec3d var1, Vec3d var2, float[] var3);
}
