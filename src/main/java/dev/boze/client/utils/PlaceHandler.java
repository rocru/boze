package dev.boze.client.utils;

import dev.boze.client.enums.InteractionAwaitMode;
import dev.boze.client.enums.InteractionBlockMode;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.settings.*;
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class5913;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class PlaceHandler extends InteractionHandler {
    private final EnumSetting<InteractionAwaitMode> field244 = new EnumSetting<InteractionAwaitMode>(
            "Await",
            InteractionAwaitMode.Off,
            "Await placement confirmation before further placements\n - Off: Don't await\n - Semi: Only await when placing many blocks\n - Strict: Always await\n",
            this::method2114
    );
    public final BooleanSetting field245 = new BooleanSetting("Extension", false, "Extend around players in the way");
    public final BooleanSetting field246 = new BooleanSetting("Shield", false, "Shield blocks being mined by also surrounding them");
    public final BooleanSetting field247 = new BooleanSetting("PingFix", false, "Ping compensation");
    public final BooleanSetting field248 = new BooleanSetting("Predicted", false, "Consider predicted mining for Shield and PingFix");
    public final EnumSetting<InteractionBlockMode> field249 = new EnumSetting<InteractionBlockMode>(
            "Blocks",
            InteractionBlockMode.BlastProof,
            "The type of blocks to place\n - Obsidian: Only Obsidian\n - BlastProof: Most blast-proof block found\n - Custom: Custom blocks\nTo add or remove custom blocks:\n Use '.set <module> blocklist add <block>' to add\n Use '.set <module> blocklist del <block>' to remove\n Use '.set <module> blocklist list' to list all blocks\n"
    );
    public final StringModeSetting field250 = new StringModeSetting("BlockList", "Blocks for custom mode");
    private final SettingBlock field251 = new SettingBlock(
            "Placement",
            "Block placement settings\nIn ghost mode, you can fine-tune the rotations in the GhostRotations module\n",
            this.field226,
            this.field227,
            this.field228,
            this.field229,
            this.field230,
            this.field231,
            this.field234,
            this.field235,
            this.field236,
            this.field237,
            this.field239,
            this.field238,
            this.field240,
            this.field244,
            this.field241,
            this.field242,
            this.field245,
            this.field246,
            this.field247,
            this.field248,
            this.field249,
            this.field250
    );

    @Override
    public Setting<?>[] get() {
        return this.field251.method472();
    }

    public void method152(BlockUpdateS2CPacket packet, SwapHandler swapHelper, int slot, BlockPos[] surroundingBlocks) {
        for (BlockPos var11 : surroundingBlocks) {
            if (var11.equals(packet.getPos())) {
                if (this.method150(null, var11) instanceof BlockHitResult var13) {
                    if (this.method2116() && mc.player.isOnGround()) {
                        float[] var14 = EntityUtil.method2146(var13.getPos());
                        ((IClientPlayerEntity) mc.player).boze$sendMovementPackets(var14[0], var14[1]);
                    }

                    if (slot >= 0) {
                        swapHelper.method723(this.method149(), slot);
                    }

                    Class5913.method17(slot == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND, var13);
                    mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(slot == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND));
                    if (slot >= 0) {
                        swapHelper.method1416();
                    }
                }

                return;
            }
        }
    }

    public boolean method153(BlockPos pos, BlockPos placement, Entity target) {
        if (target == null) {
            return false;
        } else if (this.field244.getValue() == InteractionAwaitMode.Off) {
            return false;
        } else {
            if (EntityTracker.field3914.containsKey(pos)) {
                if (this.field244.getValue() == InteractionAwaitMode.Strict) {
                    return true;
                }

                if (this.field244.getValue() == InteractionAwaitMode.Semi) {
                    Box var7 = target.getBoundingBox();
                    return var7.contains((double) placement.getX() + 0.5, placement.getY() - 1, (double) placement.getZ() + 0.5);
                }
            }

            return false;
        }
    }

    public Box method1953() {
        return this.method155(mc.player);
    }

    public Box method155(Entity entity) {
        Box var4 = entity.getBoundingBox().shrink(1.0E-4, 0.0, 1.0E-4);
        BlockPos var5 = BlockPos.ofFloored(entity.getX(), var4.minY, entity.getZ());
        if (mc.world.getBlockState(var5).getBlock() == Blocks.ENDER_CHEST) {
            var4 = var4.offset(0.0, 0.13, 0.0);
        }

        return var4;
    }
}
