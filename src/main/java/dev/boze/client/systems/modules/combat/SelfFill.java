package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.enums.*;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.world.BlockInteraction;
import mapped.Class2784;
import mapped.Class2811;
import mapped.Class3069;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SelfFill extends Module {
    public static final SelfFill INSTANCE = new SelfFill();
    private final EnumSetting<SelfFillMode> selfFillMode = new EnumSetting<SelfFillMode>("Type", SelfFillMode.Burrow, "Type of SelfFill");
    private final EnumSetting<PlaceMode> placeMode = new EnumSetting<PlaceMode>("Place", PlaceMode.Vanilla, "Place mode");
    private final EnumSetting<AnticheatMode> interactionMode = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Mode to use for placing", this::lambda$new$0);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true, "Rotate");
    private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing", this::lambda$new$1);
    private final BooleanSetting onlyEChests = new BooleanSetting("OnlyEChests", false, "Only use E-Chests", this::lambda$new$2);
    private final BooleanSetting attackCrystals = new BooleanSetting("AttackCrystals", true, "Attack crystals in the way", this::lambda$new$3);
    private final EnumSetting<SwapMode> swap = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Auto Swap mode");
    private final BooleanSetting await = new BooleanSetting("Await", false, "When in block, wait till mined instead of disabling instantly");
    private final BooleanSetting customOffset = new BooleanSetting("CustomOffset", false, "Custom SelfFill offset", this::lambda$new$4);
    private final MinMaxSetting offset = new MinMaxSetting("Offset", -9.0, -30.0, 30.0, 0.1, "SelfFill offset", this::lambda$new$5);
    private BlockPos field2565 = null;
    private boolean field2566 = false;
    private EndCrystalEntity field2567 = null;
    private BlockInteraction field2568 = null;
    private boolean field2569 = false;

    public SelfFill() {
        super("SelfFill", "Places a block in your feet", Category.Combat);
    }

    private static boolean lambda$onRotate$6(Entity var0) {
        return var0 instanceof EndCrystalEntity;
    }

    @Override
    public void onEnable() {
        this.field2565 = null;
        this.field2566 = false;
        this.field2568 = null;
        this.field2567 = null;
        this.field2569 = false;
    }

    @EventHandler(
            priority = 150
    )
    public void method1525(ACRotationEvent event) {
        if (this.selfFillMode.getValue() == SelfFillMode.Burrow) {
            if (this.attackCrystals.getValue()) {
                BlockPos var10 = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                List var11 = mc.world.getOtherEntities(null, new Box(var10), SelfFill::lambda$onRotate$6);
                if (!var11.isEmpty() && var11.get(0) instanceof EndCrystalEntity var12) {
                    this.field2567 = var12;
                }

                if (this.field2567 != null) {
                    IEndCrystalEntity var13 = (IEndCrystalEntity) this.field2567;
                    if ((double) (System.currentTimeMillis() - var13.boze$getLastAttackTime()) < Boze.getModules().field905.field1519) {
                        this.field2567 = null;
                    } else {
                        double var15 = Class3069.method6004(mc.player, this.field2567.getPos());
                        if (var15 >= (double) (mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
                            this.field2569 = true;
                            this.field2567 = null;
                        }
                    }
                }

                if (this.rotate.getValue() && this.field2567 != null) {
                    float[] var14 = EntityUtil.method2146(this.field2567.getPos());
                    event.method1021(true);
                    event.yaw = var14[0];
                    event.pitch = var14[1];
                }
            }
        } else if (!event.method1018(this.interactionMode.getValue(), this.rotate.getValue())) {
            BlockPos var5 = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
            Class2811.field99 = true;
            if (Class2784.method2101(var5) && mc.player.isOnGround()) {
                Class2811.field99 = false;
                BlockInteraction var6 = BlockInteraction.method2272(var5);
                if (var6.method2279() == null) {
                    if (!this.await.getValue()) {
                        this.setEnabled(false);
                    }
                } else {
                    this.field2568 = var6;
                    if (this.rotate.getValue()) {
                        float[] var7 = EntityUtil.method2146(var6.method2279().getPos());
                        event.method1021(true);
                        event.yaw = var7[0];
                        event.pitch = var7[1];
                        if (((ClientPlayerEntityAccessor) mc.player).getLastPitch() < 0.0F && event.pitch > 0.0F) {
                            event.pitch = 0.0F;
                            this.field2568 = null;
                        }
                    }
                }
            } else {
                Class2811.field99 = false;
                if (!this.await.getValue()) {
                    this.setEnabled(false);
                }
            }
        }
    }

    @EventHandler(
            priority = 150
    )
    public void method1526(RotationEvent event) {
        if (this.selfFillMode.getValue() != SelfFillMode.Burrow) {
            if (!event.method554(RotationMode.Sequential)) {
                int var5 = this.method1528();
                if (var5 == -1) {
                    ChatInstance.method742(this.getName(), "No webs in hotbar");
                    this.setEnabled(false);
                } else {
                    if (this.field2568 != null && this.field2568.method2279() != null) {
                        event.method556(
                                this,
                                this.field2569 ? PlaceMode.Packet : this.placeMode.getValue(),
                                this.swap.getValue(),
                                200,
                                var5,
                                this.field2568.method2279(),
                                var5 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND
                        );
                        if (this.placeMode.getValue() == PlaceMode.Vanilla && !this.field2569) {
                            Class2784.method1801(this.field2568.method2278());
                        }

                        ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).callSyncSelectedSlot();
                        this.field2568 = null;
                        this.setEnabled(false);
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = 200
    )
    public void method1527(MovementEvent event) {
        if (this.selfFillMode.getValue() != SelfFillMode.Web) {
            int var5 = this.method1528();
            if (var5 == -1) {
                this.setEnabled(false);
            } else {
                BlockPos var6 = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                if (this.field2565 == null) {
                    this.field2565 = var6;
                } else if (!this.field2565.equals(var6)) {
                    if (this.await.getValue() && !mc.player.isOnGround() && !this.field2566) {
                        this.field2565 = var6;
                        return;
                    }

                    this.setEnabled(false);
                    return;
                }

                if (mc.world.getBlockState(var6).getBlock() == Blocks.AIR && mc.player.isOnGround()) {
                    if (this.field2567 != null) {
                        mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(this.field2567, mc.player.isSneaking()));
                        mc.player.swingHand(Hand.MAIN_HAND);
                        IEndCrystalEntity var16 = (IEndCrystalEntity) this.field2567;
                        var16.boze$setLastAttackTime(System.currentTimeMillis());
                        this.field2567 = null;
                    } else {
                        BlockPos var7 = var6.down();
                        Direction var8 = Direction.UP;
                        if (this.rotate.getValue()) {
                            if (((ClientPlayerEntityAccessor) mc.player).getLastPitch() < 0.0F) {
                                mc.player.networkHandler.sendPacket(new LookAndOnGround(mc.player.getYaw(), 0.0F, true));
                            }

                            mc.player.networkHandler.sendPacket(new Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), 90.0F, true));
                            ((ClientPlayerEntityAccessor) mc.player).setLastY(mc.player.getY() + 1.16);
                            ((ClientPlayerEntityAccessor) mc.player).setLastPitch(90.0F);
                        }

                        mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.42, mc.player.getZ(), false));
                        mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.75, mc.player.getZ(), false));
                        mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.01, mc.player.getZ(), false));
                        mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.16, mc.player.getZ(), false));
                        float var9 = (float) (0.5 + (double) var6.getX());
                        float var10 = (float) ((double) var6.getY());
                        float var11 = (float) (0.5 + (double) var6.getZ());
                        boolean var12 = mc.player.getInventory().selectedSlot != var5;
                        int var13 = mc.player.getInventory().selectedSlot;
                        if (var12) {
                            mc.player.getInventory().selectedSlot = var5;
                            ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).callSyncSelectedSlot();
                        }

                        Class5913.method17(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(var9, var10, var11), var8, var7, false));
                        if (this.swing.getValue()) {
                            mc.player.swingHand(Hand.MAIN_HAND);
                        }

                        this.field2566 = true;
                        if (var12) {
                            mc.player.getInventory().selectedSlot = var13;
                        }

                        if (this.customOffset.getValue()) {
                            mc.player
                                    .networkHandler
                                    .sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + this.offset.getValue(), mc.player.getZ(), false));
                            this.setEnabled(false);
                        } else if (mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, 2.34, 0.0))) {
                            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 2.34, mc.player.getZ(), false));
                            this.setEnabled(false);
                        } else {
                            for (double var14 = 9.0; var14 < 16.0; var14++) {
                                if (mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, -var14, 0.0))) {
                                    mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() - var14, mc.player.getZ(), false));
                                    this.setEnabled(false);
                                    return;
                                }
                            }

                            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 2.34, mc.player.getZ(), false));
                            this.setEnabled(false);
                        }
                    }
                } else if (!this.await.getValue()) {
                    this.setEnabled(false);
                }
            }
        }
    }

    private int method1528() {
        int var4 = -1;
        ItemStack var5 = mc.player.getMainHandStack();
        if (var5 != ItemStack.EMPTY && var5.getItem() instanceof BlockItem) {
            Block var6 = ((BlockItem) var5.getItem()).getBlock();
            if (this.method1529(var6)) {
                var4 = mc.player.getInventory().selectedSlot;
            }
        }

        ItemStack var10 = mc.player.getOffHandStack();
        if (var10 != ItemStack.EMPTY && var10.getItem() instanceof BlockItem) {
            Block var7 = ((BlockItem) var10.getItem()).getBlock();
            if (this.method1529(var7)) {
                var4 = -2;
            }
        }

        if (var4 == -1) {
            for (int var11 = 0; var11 < 9; var11++) {
                ItemStack var8 = mc.player.getInventory().getStack(var11);
                if (var8 != ItemStack.EMPTY && var8.getItem() instanceof BlockItem) {
                    Block var9 = ((BlockItem) var8.getItem()).getBlock();
                    if (this.method1529(var9)) {
                        var4 = var11;
                        break;
                    }
                }
            }
        }

        return var4;
    }

    private boolean method1529(Block var1) {
        if (this.selfFillMode.getValue() == SelfFillMode.Web) {
            return var1 == Blocks.COBWEB;
        } else {
            return this.onlyEChests.getValue()
                    ? var1 == Blocks.ENDER_CHEST
                    : var1 == Blocks.OBSIDIAN || var1 == Blocks.CRYING_OBSIDIAN || var1 == Blocks.NETHERITE_BLOCK || var1 == Blocks.ENDER_CHEST;
        }
    }

    private boolean lambda$new$5() {
        return this.selfFillMode.getValue() == SelfFillMode.Burrow && this.customOffset.getValue();
    }

    private boolean lambda$new$4() {
        return this.selfFillMode.getValue() == SelfFillMode.Burrow;
    }

    private boolean lambda$new$3() {
        return this.selfFillMode.getValue() == SelfFillMode.Burrow;
    }

    private boolean lambda$new$2() {
        return this.selfFillMode.getValue() == SelfFillMode.Burrow;
    }

    private boolean lambda$new$1() {
        return this.selfFillMode.getValue() == SelfFillMode.Burrow;
    }

    private boolean lambda$new$0() {
        return this.selfFillMode.getValue() == SelfFillMode.Web;
    }
}
