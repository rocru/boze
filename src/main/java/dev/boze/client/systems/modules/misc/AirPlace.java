package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AirPlace extends Module {
    public static final AirPlace INSTANCE = new AirPlace();
    private final BooleanSetting twobeetwotee = new BooleanSetting("2b2t", true, "2b2t bypass");
    private int field880 = 0;

    public AirPlace() {
        super("AirPlace", "Place blocks in the air", Category.Misc);
    }

    public static void method393(BlockHitResult blockHitResult, boolean offHand) {
        Class5913.method16(offHand ? Hand.OFF_HAND : Hand.MAIN_HAND);
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        Class5913.method17(offHand ? Hand.MAIN_HAND : Hand.OFF_HAND, blockHitResult);
        mc.player.swingHand(offHand ? Hand.MAIN_HAND : Hand.OFF_HAND);
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        mc.player.swingHand(offHand ? Hand.OFF_HAND : Hand.MAIN_HAND);
    }

    @EventHandler
    public void method1883(RotationEvent event) {
        if (event.field1284 != RotationMode.Vanilla) {
            if (this.field880 > 0) {
                if (mc.options.useKey.isPressed()) {
                    this.field880--;
                    return;
                }

                this.field880 = 0;
            }

            boolean var5 = mc.player.getMainHandStack().getItem() instanceof BlockItem;
            boolean var6 = mc.player.getOffHandStack().getItem() instanceof BlockItem;
            if (var5 || var6) {
                if (!this.twobeetwotee.getValue()) {
                    if (mc.crosshairTarget instanceof BlockHitResult var9 && this.method392(var9) && mc.options.useKey.isPressed()) {
                        BlockHitResult var11 = new BlockHitResult(var9.getPos(), var9.getSide(), var9.getBlockPos(), var9.isInsideBlock());
                        Class5913.method17(var5 ? Hand.MAIN_HAND : Hand.OFF_HAND, var11);
                        mc.player.swingHand(var5 ? Hand.MAIN_HAND : Hand.OFF_HAND);
                        this.field880 = 4;
                    }
                } else {
                    if (mc.crosshairTarget instanceof BlockHitResult var7 && this.method392(var7) && mc.options.useKey.isPressed()) {
                        method393(var7, !var5);
                        this.field880 = 4;
                    }
                }
            }
        }
    }

    private boolean method392(BlockHitResult var1) {
        return mc.world.getBlockState(var1.getBlockPos()).isAir();
    }
}
