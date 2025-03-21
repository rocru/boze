package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.NoFallMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.utils.*;
import mapped.Class1202;
import mapped.Class2811;
import mapped.Class2812;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class NoFall extends Module {
    public static final NoFall INSTANCE = new NoFall();
    private final EnumSetting<NoFallMode> field527 = new EnumSetting<NoFallMode>("Mode", NoFallMode.Grim, "Mode for no fall", NoFall::lambda$new$0);
    private final MinMaxDoubleSetting field528 = new MinMaxDoubleSetting("AimSpeed", new double[]{1.0, 2.0}, 0.1, 5.0, 0.1, "Aim speed", this::method1971);
    public final MinMaxDoubleSetting field529 = new MinMaxDoubleSetting(
            "SwapDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to swap in ticks", this::method1971
    );
    public final MinMaxDoubleSetting field530 = new MinMaxDoubleSetting(
            "PlaceDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to place in ticks", this::method1971
    );
    private final BooleanSetting field531 = new BooleanSetting("MultiTask", false, "Place blocks while already using items", this::method1971);
    private final MinMaxSetting field532 = new MinMaxSetting("Distance", 3.0, 0.1, 20.0, 0.1, "Minimum fall distance to activate no fall");
    private final BooleanSetting field533 = new BooleanSetting("Proximity", false, "Only activate no fall when close to ground", this::lambda$new$1);
    private final MinMaxSetting field534 = new MinMaxSetting("Height", 4.0, 0.1, 10.0, 0.1, "Height above ground to activate no fall", this.field533);
    private final Timer field535 = new Timer();
    private boolean field536 = false;
    private boolean field537 = false;
    private final Timer field538 = new Timer();
    private final Timer field539 = new Timer();

    private NoFallMode method275() {
        return Options.INSTANCE.method1971() ? NoFallMode.Ghost : this.field527.getValue();
    }

    private boolean method1971() {
        return this.method275() == NoFallMode.Ghost;
    }

    public NoFall() {
        super("NoFall", "Prevent falling or fall damage", Category.Movement);
        this.field435 = true;
    }

    @Override
    public void onEnable() {
        this.field536 = false;
        this.field537 = false;
    }

    @EventHandler(
            priority = 300
    )
    public void method1695(MouseUpdateEvent event) {
        if (MinecraftUtils.isClientActive() && this.method1971() && !event.method1022()) {
            if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
                if (this.method1972()) {
                    int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
                    if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                        RotationHelper var6 = new RotationHelper(mc.player.getYaw(), 90.0F);
                        BlockHitResult var7 = RaycastUtil.method574(this.field533.getValue() ? this.field534.getValue() : 20.0, var6);
                        if (var7 != null && var7.getType() != Type.MISS) {
                            RotationHelper var8 = Class1202.method2391(mc.player.getEyePos(), var7.getPos());
                            var6 = var6.method603(var8, this.field528.getValue());
                            RotationHelper var9 = new RotationHelper(mc.player);
                            RotationHelper var10 = var6.method1600();
                            RotationHelper var11 = var10.method606(var9);
                            Pair[] var12 = RotationHelper.method614(var11);
                            Pair var13 = var12[0];

                            for (Pair var17 : var12) {
                                BlockHitResult var18 = RaycastUtil.method574(
                                        this.field533.getValue() ? this.field534.getValue() : 20.0, RotationHelper.method613(var9, var17)
                                );
                                if (var18.getType() != Type.MISS && var18.getBlockPos() == var7.getBlockPos() && var18.getSide() == var7.getSide()) {
                                    var13 = var17;
                                }
                            }

                            event.deltaY = event.deltaY + (Double) var13.getRight();
                            event.method1021(true);
                        }
                    } else {
                        this.field539.reset();
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1693(HandleInputEvent event) {
        if (this.method1971()) {
            if (this.method1972()) {
                int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
                if (this.field538.hasElapsed(this.field529.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
                    ((KeyBindingAccessor) mc.options.hotbarKeys[var5]).setTimesPressed(1);
                    this.field538.reset();
                    this.field529.method1296();
                }
            } else {
                this.field538.reset();
            }
        }
    }

    @EventHandler(
            priority = 76
    )
    public void method1883(RotationEvent event) {
        if (!Options.method477(this.field531.getValue()) && !event.method554(RotationMode.Vanilla)) {
            if (this.method1971() && this.method1972()) {
                int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
                if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                    RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                    BlockHitResult var7 = RaycastUtil.method574(Reach.method1614(), var6);
                    if (var7 != null && var7.getType() != Type.MISS) {
                        if (this.field539.hasElapsed(this.field530.method1295() * 50.0)) {
                            ((KeyBindingAccessor) mc.options.useKey).setTimesPressed(1);
                            this.field539.reset();
                            this.field530.method1296();
                        }
                    } else {
                        this.field539.reset();
                    }
                } else {
                    this.field539.reset();
                }
            }
        }
    }

    private boolean method1972() {
        if ((double) mc.player.fallDistance >= this.field532.getValue()
                && (
                !this.field533.getValue()
                        || !mc.world
                        .isSpaceEmpty(
                                new Box(
                                        mc.player.getBoundingBox().minX,
                                        mc.player.getBoundingBox().minY - this.field534.getValue(),
                                        mc.player.getBoundingBox().minZ,
                                        mc.player.getBoundingBox().maxX,
                                        mc.player.getBoundingBox().maxY,
                                        mc.player.getBoundingBox().maxZ
                                )
                        )
        )) {
            if (this.method1971()) {
                RotationHelper var4 = new RotationHelper(mc.player.getYaw(), 90.0F);
                BlockHitResult var5 = RaycastUtil.method574(Reach.method1614(), var4);
                return var5 == null || var5.getType() == Type.MISS || mc.world.getBlockState(var5.getBlockPos()).getBlock() != Blocks.WATER;
            }

            return true;
        } else {
            return false;
        }
    }

    @EventHandler
    private void method2041(final MovementEvent movementEvent) {
        if (this.method1971()) {
            return;
        }
        if (!this.field535.hasElapsed(500.0)) {
            return;
        }
        if (this.method275() == NoFallMode.Grim) {
            if (NoFall.mc.player.isOnGround()) {
                this.field536 = false;
            }
            else if (!this.field536 && NoFall.mc.player.fallDistance >= this.field532.getValue() && !NoFall.mc.world.isSpaceEmpty(new Box(NoFall.mc.player.getBoundingBox().minX, NoFall.mc.player.getBoundingBox().minY - 2.0, NoFall.mc.player.getBoundingBox().minZ, NoFall.mc.player.getBoundingBox().maxX, NoFall.mc.player.getBoundingBox().maxY, NoFall.mc.player.getBoundingBox().maxZ))) {
                this.field537 = true;
                this.field536 = true;
            }
            return;
        }
        if (this.method1972()) {
            switch (this.method275().ordinal()) {
                case 0:
                    movementEvent.isOnGround = true;
                    break;
                case 2:
                    movementEvent.field1931 += 0.001;
                    NoFall.mc.player.setVelocity(NoFall.mc.player.getVelocity().x, 0.0, NoFall.mc.player.getVelocity().z);
                    break;
                case 3:
                    movementEvent.field1931 += 3.0;
                    break;
                case 4:
                    for (int i = 0; i < 10; ++i) {
                        NoFall.mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(NoFall.mc.player.getX() + (10 - (int)(Math.random() * 20.0)), NoFall.mc.player.getY() + (10 - (int)(Math.random() * 20.0)), NoFall.mc.player.getZ() + (10 - (int)(Math.random() * 20.0)), NoFall.mc.player.getYaw() + (180 - (int)(Math.random() * 360.0)), NoFall.mc.player.getPitch(), true));
                        Class5913.method17(Hand.MAIN_HAND, new BlockHitResult(Vec3d.ZERO, Direction.UP, BlockPos.ofFloored(NoFall.mc.player.getPos()).add(10 - (int)(Math.random() * 20.0), 10 - (int)(Math.random() * 20.0), 10 - (int)(Math.random() * 20.0)), false));
                    }
                    break;
                case 5: {
                    final int method168 = InventoryHelper.method168(NoFall::lambda$onSendMovementPackets$2);
                    if (method168 == -1) {
                        return;
                    }
                    final boolean field101 = Class2811.field101;
                    Class2811.field101 = true;
                    final PlaceAction method169 = Class2812.method5502(BlockPos.ofFloored(NoFall.mc.player.getPos()).down(), true, true, false, false, Hand.MAIN_HAND, method168);
                    if (method169 != null) {
                        movementEvent.method1074(new ActionWrapper(method169));
                    }
                    Class2811.field101 = field101;
                    break;
                }
            }
        }
    }

    @EventHandler
    private void method2042(PacketBundleEvent var1) {
        if (var1.packet instanceof PlayerPositionLookS2CPacket) {
            this.field535.reset();
        }
    }

    @EventHandler
    private void method240(PostPacketSendEvent var1) {
        if (var1.packet instanceof PlayerMoveC2SPacket var5 && this.field537 && !this.method1971()) {
            this.field537 = false;
            mc.getNetworkHandler()
                    .sendPacket(
                            new Full(
                                    var5.getX(mc.player.getX()),
                                    var5.getY(mc.player.getY()) + 1.00001E-9,
                                    var5.getZ(mc.player.getZ()),
                                    var5.getYaw(mc.player.getYaw()) + 1337.0F,
                                    var5.getPitch(mc.player.getPitch()),
                                    var5.isOnGround()
                            )
                    );
        }
    }

    private static boolean lambda$onSendMovementPackets$2(ItemStack var0) {
        return var0.getItem() instanceof BlockItem;
    }

    private boolean lambda$new$1() {
        return this.method275() != NoFallMode.Grim;
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }
}
