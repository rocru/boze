package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.legit.Reach;
import mapped.Class5914;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class WallInteract extends Module {
    public static final WallInteract INSTANCE = new WallInteract();
    private final ArrayList<BlockPos> field3136 = new ArrayList();

    public WallInteract() {
        super("WallInteract", "Interact with block entities through walls", Category.Misc);
    }

    @EventHandler
    private void method1780(PrePlayerTickEvent var1) {
        if (!mc.player.isSneaking() && mc.options.useKey.isPressed()) {
            Iterable<BlockEntity> var5 = Class5914.method19();

            for (BlockEntity var7 : var5) {
                if (BlockPos.ofFloored(mc.player.raycast(Reach.method1614(), mc.getRenderTickCounter().getTickDelta(true), false).getPos()).equals(var7.getPos())) {
                    return;
                }
            }

            Vec3d var16 = new Vec3d(0.0, 0.0, 0.1)
                    .rotateX(-((float) Math.toRadians(mc.player.getPitch())))
                    .rotateY(-((float) Math.toRadians(mc.player.getYaw())));
            this.field3136.clear();

            for (int var17 = 1; (double) var17 < Reach.method1614() * 10.0; var17++) {
                BlockPos var8 = BlockPos.ofFloored(mc.player.getCameraPosVec(mc.getRenderTickCounter().getTickDelta(true)).add(var16.multiply(var17)));
                if (!this.field3136.contains(var8)) {
                    this.field3136.add(var8);
                }
            }

            BlockEntity var18 = null;
            Vec3d var19 = null;
            double var9 = 999.0;

            for (BlockEntity var12 : var5) {
                if (this.field3136.contains(var12.getPos())) {
                    Vec3d var13 = new Vec3d((double) var12.getPos().getX() + 0.5, (double) var12.getPos().getY() + 0.5, (double) var12.getPos().getZ() + 0.5);
                    double var14 = var13.distanceTo(mc.player.getEyePos());
                    if (var18 == null || var14 < var9) {
                        var18 = var12;
                        var19 = var13;
                        var9 = var14;
                    }
                }
            }

            if (var18 != null) {
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(var19, Direction.UP, var18.getPos(), true));
            }
        }
    }
}
