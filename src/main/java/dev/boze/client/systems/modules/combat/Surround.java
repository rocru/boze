package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.ac.Ghost;
import dev.boze.client.core.BozeLogger;
import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.combat.surround.AutoEnable;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.systems.modules.movement.Step;
import dev.boze.client.systems.render.PlacementRenderer;
import dev.boze.client.utils.*;
import dev.boze.client.utils.trackers.BlockBreakingTracker;
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class1202;
import mapped.Class2784;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class Surround extends Module {
    public static Surround INSTANCE = new Surround();
    private final PlacementRenderer field2570 = new PlacementRenderer();
    final PlaceHandler field2571 = new PlaceHandler();
    private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "Surround while already using items");
    private final EnumSetting<SurroundReactMode> react = new EnumSetting<SurroundReactMode>(
            "React",
            SurroundReactMode.Tick,
            "When to react to changes in the world\n - Tick: React to everything during game ticks, most consistent\n - Packet: Reacts to everything instantly, as soon as it's received from the server\n"
    );
    private final BooleanSetting autoCenter = new BooleanSetting("AutoCenter", true, "Automatically center on block", this::lambda$new$0);
    public final SettingCategory autoDisable = new SettingCategory("AutoDisable", "Auto disable settings");
    private final BooleanSetting onJump = new BooleanSetting("OnJump", false, "Disable when you jump", this.autoDisable);
    private final BooleanSetting onStep = new BooleanSetting("OnStep", false, "Disable when you step", this.autoDisable);
    private final BooleanSetting onTP = new BooleanSetting("OnTP", false, "Disable when you teleport/chorus", this.autoDisable);
    private final BooleanSetting whenDone = new BooleanSetting("WhenDone", false, "Disable when done surrounding", this.autoDisable);
    final BooleanSetting autoEnable = new BooleanSetting(
            "AutoEnable", false, "Auto enable surround when in semi/full hole\nTo make it only auto-enable in full holes, set all sub-settings to max (4/6/8)"
    );
    final IntSetting min1x1 = new IntSetting("Min1x1", 3, 2, 4, 1, "Minimum existing blocks to auto-enable in 1x1 holes", this.autoEnable);
    final IntSetting min2x1 = new IntSetting("Min2x1", 4, 3, 6, 1, "Minimum existing blocks to auto-enable in 2x1 holes", this.autoEnable);
    final IntSetting min2x2 = new IntSetting("Min2x2", 6, 4, 8, 1, "Minimum existing blocks to auto-enable in 2x2 holes", this.autoEnable);
    private final SwapHandler field2572 = new SwapHandler(this, 150);
    private final AutoEnable field2573 = new AutoEnable();
    private HitResult[] field2574 = null;
    private BlockHitResult field2575 = null;
    private RotationHelper field2576;
    private final Timer field2577 = new Timer();
    private final Timer field2578 = new Timer();

    private static void method1530(String var0) {
        if (mc.player != null) {
            BozeLogger.method529(INSTANCE, var0);
        }
    }

    private Surround() {
        super(
                "Surround",
                "Surrounds your feet with obsidian, which protects you from explosions\nIf no Obsidian, will use Ender Chests or other blast proof blocks\n",
                Category.Combat
        );
        Boze.EVENT_BUS.subscribe(this.field2570);
        Boze.EVENT_BUS.subscribe(this.field2573);
        this.field435 = true;
        this.addSettings(this.field2570.field224);
    }

    @Override
    public void onEnable() {
        if (!MinecraftUtils.isClientActive()) {
            this.setEnabled(false);
        } else {
            this.field2574 = null;
            this.field2571.field230.method1296();
            if (this.autoCenter.getValue() && this.field2571.method147() == Anticheat.NCP) {
                Class5924.method2142();
            }
        }
    }

    @EventHandler
    public void method1531(PlayerPositionEvent event) {
        if (this.onStep.getValue() && Step.INSTANCE.isEnabled()) {
            this.setEnabled(false);
        }
    }

    @EventHandler
    public void method1532(PacketBundleEvent event) {
        if (!(event.packet instanceof PlayerPositionLookS2CPacket) || !this.onTP.getValue()) {
            if (event.packet instanceof BlockUpdateS2CPacket var5 && this.react.getValue() == SurroundReactMode.Packet && this.field2571.method2114()) {
                try {
                    if (var5.getState().isAir()) {
                        int var10 = InventoryHelper.method174(
                                BlastResistanceCalculator.method2130(this.field2571.field249.getValue(), this.field2571.field250), this.field2571.method149()
                        );
                        if (var10 == -1) {
                            return;
                        }

                        method1530("Reacting to block update at " + var5.getPos());
                        Box var7 = this.field2571.method1953();
                        BlockPos[] var8 = TrapUtil.method586(var7, TrapMode.Flat);
                        this.field2571.method152(var5, this.field2572, var10, var8);
                    }
                } catch (Exception var9) {
                }
            }
        } else if (MinecraftUtils.isClientActive()
                && mc.player
                .getPos()
                .distanceTo(
                        new Vec3d(
                                ((PlayerPositionLookS2CPacket) event.packet).getX(),
                                ((PlayerPositionLookS2CPacket) event.packet).getY(),
                                ((PlayerPositionLookS2CPacket) event.packet).getZ()
                        )
                )
                > 1.0) {
            method1530("Disabled due to teleport");
            this.setEnabled(false);
        }
    }

    @EventHandler
    public void method1533(HandleInputEvent event) {
        if (!this.field2571.method2114()) {
            if (this.field2576 != null) {
                int var5 = InventoryHelper.method176(BlastResistanceCalculator.method2130(this.field2571.field249.getValue(), this.field2571.field250));
                if (this.field2577.hasElapsed(this.field2571.field232.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
                    ((KeyBindingAccessor) mc.options.hotbarKeys[var5]).setTimesPressed(1);
                    this.field2577.reset();
                    this.field2571.field232.method1296();
                }
            } else {
                this.field2577.reset();
            }
        }
    }

    private boolean method1534() {
        if (this.onJump.getValue() && mc.player.getVelocity().y > 0.0 && !mc.player.isOnGround()) {
            this.setEnabled(false);
            return false;
        } else if (Options.method477(this.multiTask.getValue())) {
            return false;
        } else {
            int var4 = InventoryHelper.method174(
                    BlastResistanceCalculator.method2130(this.field2571.field249.getValue(), this.field2571.field250), this.field2571.method149()
            );
            if (var4 == -1) {
                return false;
            } else {
                Box var5 = this.field2571.method1953();
                this.field2574 = TrapUtil.method584(this.field2571, var5, TrapMode.Flat);
                if (this.field2574.length == 0) {
                    if (this.whenDone.getValue()) {
                        this.setEnabled(false);
                    }

                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    private boolean method1535() {
        Ghost.field1313.method569(this.field2571.field228, this.field2571.field230, this.field2571.field231);
        if (!this.method1534()) {
            this.field2576 = null;
            return false;
        } else if (this.field2574 != null && this.field2574.length != 0 && this.field2574[0] instanceof BlockHitResult) {
            this.field2575 = (BlockHitResult) this.field2574[0];
            RotationHelper var4 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
            RotationHelper var5 = Class1202.method2391(mc.player.getEyePos(), this.field2575.getPos());
            this.field2576 = var4.method603(var5, this.field2571.field229.getValue());
            return true;
        } else {
            this.field2576 = null;
            return false;
        }
    }

    @EventHandler(
            priority = 150
    )
    public void method1536(eJ event) {
        if (!this.field2571.method2114() && this.field2571.field227.getValue() != BlockPlaceMode.Mouse && !event.method1101()) {
            if (this.method1535()) {
                event.method1099(this.field2576.method600(this::lambda$onGhostRotate$1));
            }
        }
    }

    @EventHandler(
            priority = 170
    )
    public void method1537(MouseUpdateEvent event) {
        if (MinecraftUtils.isClientActive() && !event.method1022()) {
            if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
                if (!this.field2571.method2114() && this.field2571.field227.getValue() != BlockPlaceMode.Normal) {
                    if (this.method1535()) {
                        RotationHelper var5 = new RotationHelper(mc.player);
                        RotationHelper var6 = this.field2576.method1600();
                        RotationHelper var7 = var6.method606(var5);
                        Pair[] var8 = RotationHelper.method614(var7);
                        Pair var9 = var8[0];

                        for (Pair var13 : var8) {
                            BlockHitResult var14 = RaycastUtil.method575(Reach.method1614(), RotationHelper.method613(var5, var13), true);
                            if (var14.getType() != Type.MISS && var14.getBlockPos() == this.field2575.getBlockPos() && var14.getSide() == this.field2575.getSide()) {
                                var9 = var13;
                            }
                        }

                        event.deltaX = event.deltaX + (Double) var9.getLeft();
                        event.deltaY = event.deltaY + (Double) var9.getRight();
                        event.method1021(true);
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = 150
    )
    public void method1538(ACRotationEvent event) {
        if (!this.field2571.method2115()) {
            if (!event.method1018(this.field2571.method147().interactMode, this.field2571.method2116())) {
                if (this.method1534()) {
                    if (this.field2571.method2116()) {
                        HitResult var5 = this.field2574[0];
                        Vec3d var6 = var5.getPos();
                        event.method1019(var6);
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = 150
    )
    public void method1539(RotationEvent event) {
        if (!Options.method477(this.multiTask.getValue()) && !event.method555(this.field2571.method147().type, this.field2571.method2116())) {
            if (this.field2574 != null) {
                if (!this.field2571.method2115()) {
                    byte var15 = 0;
                    HashMap var16 = null;
                    if (this.field2571.field247.getValue()) {
                        var16 = BlockBreakingTracker.field1511.method666(this.field2571.field248.getValue());
                    }

                    for (int var17 = 0; var17 < this.field2574.length; var17++) {
                        HitResult var8 = this.field2574[var17];
                        if (var8.getType() == Type.BLOCK) {
                            int var9 = InventoryHelper.method174(
                                    BlastResistanceCalculator.method2130(this.field2571.field249.getValue(), this.field2571.field250), this.field2571.method149()
                            );
                            if (var9 == -1) {
                                method1530("No valid blocks found in inventory");
                                this.field2574 = null;
                                break;
                            }

                            BlockHitResult var10 = (BlockHitResult) var8;
                            BlockPos var11 = var10.getBlockPos().offset(var10.getSide());
                            if (this.field2571.method153(var10.getBlockPos(), var11, mc.player)) {
                                method1530("Awaiting block update at " + var11);
                            } else {
                                if (!this.field2572.method723(this.field2571.method149(), var9)) {
                                    method1530("Failed to swap to slot " + var9);
                                    this.field2574 = null;
                                    break;
                                }

                                AttackMode var12 = var15 > 0 ? AttackMode.Packet : this.field2571.field234.getValue();
                                boolean var13 = false;
                                if (this.field2571.field247.getValue() && var16.containsKey(var11)) {
                                    var12 = AttackMode.Packet;
                                    var13 = true;
                                }

                                if (var17 > 0 && this.field2571.method2116()) {
                                    if (this.field2571.method2117()) {
                                        this.field2572.method1416();
                                        this.field2574 = null;
                                        return;
                                    }

                                    float[] var14 = EntityUtil.method2146(var8.getPos());
                                    ((IClientPlayerEntity) mc.player).boze$sendMovementPackets(var14[0], var14[1]);
                                }

                                if (var12 == AttackMode.Vanilla) {
                                    this.field2572.method2142();
                                }

                                method1530("Placing block at " + var11 + " using slot " + var9);
                                event.method557(this, var12, var10, var9 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                                this.field2570.method146(var10);
                                if (var12 == AttackMode.Vanilla) {
                                    Class2784.method1801(var10.getBlockPos().offset(var10.getSide()));
                                }

                                if (var13 || var15 == 2) {
                                    EntityTracker.field3914.remove(var10.getBlockPos().offset(var10.getSide()));
                                    if (var15 == 2) {
                                        var15 = 1;
                                    }
                                }
                            }
                        } else if (var8.getType() == Type.ENTITY) {
                            Entity var18 = ((EntityHitResult) var8).getEntity();
                            if (var17 > 0 && this.field2571.method2116()) {
                                if (!this.field2571.method2117()) {
                                    this.field2572.method1416();
                                    this.field2574 = null;
                                    return;
                                }

                                float[] var19 = EntityUtil.method2146(var8.getPos());
                                ((IClientPlayerEntity) mc.player).boze$sendMovementPackets(var19[0], var19[1]);
                            }

                            method1530(String.format("Attacking crystal at (%d, %d, %d)", (int) var18.getX(), (int) var18.getY(), (int) var18.getZ()));
                            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var18, mc.player.isSneaking()));
                            mc.player.swingHand(Hand.MAIN_HAND);
                            IEndCrystalEntity var20 = (IEndCrystalEntity) var18;
                            var20.boze$setLastAttackTime(System.currentTimeMillis());
                            var15 = 2;
                        }
                    }

                    this.field2572.method1416();
                    this.field2574 = null;
                } else if (this.field2575 != null && this.field2576 != null) {
                    int var5 = InventoryHelper.method176(BlastResistanceCalculator.method2130(this.field2571.field249.getValue(), this.field2571.field250));
                    if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                        RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                        BlockHitResult var7 = RaycastUtil.method575(Reach.method1614(), var6, true);
                        if (Class2784.method5444(this.field2575, var7)) {
                            if (this.field2578.hasElapsed(this.field2571.field233.method1295() * 50.0)) {
                                ((KeyBindingAccessor) mc.options.useKey).setTimesPressed(1);
                                this.field2578.reset();
                                this.field2571.field233.method1296();
                                this.field2570.method146(this.field2575);
                            }
                        } else {
                            this.field2578.reset();
                        }

                        this.field2574 = null;
                        this.field2575 = null;
                    } else {
                        this.field2574 = null;
                        this.field2575 = null;
                        this.field2578.reset();
                    }
                }
            }
        }
    }

    private Boolean lambda$onGhostRotate$1(RotationHelper var1) {
        BlockHitResult var5 = RaycastUtil.method575(Reach.method1614(), var1, true);
        if (var5.getType() == Type.MISS) {
            return false;
        } else {
            return var5.getBlockPos() == this.field2575.getBlockPos() && var5.getSide() == this.field2575.getSide();
        }
    }

    private boolean lambda$new$0() {
        return this.field2571.method147() == Anticheat.NCP;
    }
}
