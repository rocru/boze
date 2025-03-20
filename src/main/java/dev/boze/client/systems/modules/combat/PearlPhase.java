package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.enums.*;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.*;
import mapped.Class3002;
import mapped.Class5913;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PearlPhase extends Module {
    public static final PearlPhase INSTANCE = new PearlPhase();
    private final EnumSetting<PearlMode> pearlMode = new EnumSetting<PearlMode>(
            "Mode",
            PearlMode.Forward,
            "Mode for pearling\n - Forward: Only pearl forward\n - Omni: Pearl in any direction\n - Collision: Auto-pearl into colliding blocks\n"
    );
    private final EnumSetting<AwaitMode> awaitMode = new EnumSetting<AwaitMode>(
            "Await",
            AwaitMode.Off,
            "Await horizontal collision/input to phase\n - Off: Don't wait, phase or disable instantly\n - Once: Wait once, disable after phasing\n - Repeat: Wait, stay enabled after phasing\n"
    );
    private final FloatSetting pitch = new FloatSetting("Pitch", 86.0F, 45.0F, 90.0F, 1.0F, "Pitch to throw the pearl at\n84-86 is highly recommended\n");
    private final BooleanSetting zeroTick = new BooleanSetting("ZeroTick", true, "Zero tick rotate\nRecommended\n");
    private final BooleanSetting swap = new BooleanSetting(
            "Bypass", false, "Bypass for PearlPhase where it's patched\nOnly bypasses on some servers, try and see\n"
    );
    private final EnumSetting<SwapMode> swapMode = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Auto swap mode");
    private boolean field563;
    private final Timer field564 = new Timer();
    private static final Vec3d[] field565 = new Vec3d[]{
            new Vec3d(1.0, 0.0, 0.0),
            new Vec3d(-1.0, 0.0, 0.0),
            new Vec3d(0.0, 0.0, 1.0),
            new Vec3d(0.0, 0.0, -1.0),
            new Vec3d(0.75, 0.0, 0.75),
            new Vec3d(0.75, 0.0, -0.75),
            new Vec3d(-0.75, 0.0, 0.75),
            new Vec3d(-0.75, 0.0, -0.75)
    };

    public PearlPhase() {
        super("PearlPhase", "Automatically pearl phases", Category.Combat);
    }

    @Override
    public void onEnable() {
        if (MinecraftUtils.isClientActive()) {
            this.field563 = false;
            Boze.EVENT_BUS.unsubscribe(Class3002.class);
        }
    }

    @EventHandler(
            priority = 400
    )
    public void method1885(ACRotationEvent event) {
        if (this.field564.hasElapsed(350.0)) {
            if (this.method1971()) {
                if (this.awaitMode.getValue() == AwaitMode.Off) {
                    this.setEnabled(false);
                }

                this.field563 = false;
            } else if (!this.zeroTick.getValue()) {
                if (this.swap.getValue() && !this.field563) {
                    event.yaw = mc.player.getYaw();
                    event.pitch = 90.0F;
                    event.method1021(true);
                } else {
                    event.yaw = (float) this.method2010();
                    event.pitch = this.pitch.getValue();
                    event.method1021(true);
                }
            }
        }
    }

    @EventHandler(
            priority = 400
    )
    public void method1883(RotationEvent event) {
        if (!event.method554(RotationMode.Sequential)) {
            if (this.field564.hasElapsed(350.0)) {
                if (!this.method1971()) {
                    if (this.swap.getValue() && !this.field563) {
                        int var8 = InventoryHelper.method166(PearlPhase::lambda$onInteract$0, this.swapMode.getValue());
                        if (var8 == -1) {
                            ChatInstance.method625("No blocks found for strict bypass! Attempting to pearl without bypassing");
                            this.field563 = true;
                        } else if (InventoryUtil.method534(this, 400, this.swapMode.getValue(), var8)) {
                            BlockPos var6 = BlockPos.ofFloored(mc.player.getPos()).down();
                            BlockHitResult var7 = new BlockHitResult(
                                    new Vec3d((double) var6.getX() + 0.5, (double) var6.getY() + 1.0, (double) var6.getZ() + 0.5), Direction.UP, var6, false
                            );
                            if (this.zeroTick.getValue()) {
                                ((IClientPlayerEntity) mc.player).boze$sendMovementPackets((float) this.method2010(), 90.0F);
                            } else if (((ClientPlayerEntityAccessor) mc.player).getLastPitch() < 89.0F
                                    || Math.abs(((ClientPlayerEntityAccessor) mc.player).getLastYaw() - (float) this.method2010()) > 5.0F) {
                                return;
                            }

                            event.method556(this, PlaceMode.Packet, null, 400, -1, var7, Hand.MAIN_HAND);
                            this.field563 = true;
                            InventoryUtil.method396(this);
                        }
                    } else {
                        int var5 = InventoryHelper.method166(PearlPhase::lambda$onInteract$1, this.swapMode.getValue());
                        if (var5 == -1) {
                            ChatInstance.method626("No pearls found");
                            this.setEnabled(false);
                        } else if (InventoryUtil.method534(this, 400, this.swapMode.getValue(), var5)) {
                            Boze.EVENT_BUS.subscribe(Class3002.class);
                            if (this.zeroTick.getValue()) {
                                ((IClientPlayerEntity) mc.player).boze$sendMovementPackets((float) this.method2010(), this.pitch.getValue());
                            } else if (((ClientPlayerEntityAccessor) mc.player).getLastPitch() < this.pitch.getValue() - 2.0F
                                    || Math.abs(((ClientPlayerEntityAccessor) mc.player).getLastYaw() - (float) this.method2010()) > 5.0F) {
                                return;
                            }

                            Class5913.method16(Hand.MAIN_HAND);
                            mc.player.swingHand(Hand.MAIN_HAND);
                            InventoryUtil.method396(this);
                            if (this.awaitMode.getValue() != AwaitMode.Repeat) {
                                this.setEnabled(false);
                            } else {
                                this.field563 = false;
                                this.field564.reset();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean method1971() {
        if (!MinecraftUtils.isClientActive()) {
            return true;
        } else {
            Box var4 = mc.player.getBoundingBox(mc.player.getPose()).offset(mc.player.getPos());
            if (!mc.world.isSpaceEmpty(var4)) {
                return true;
            } else if (this.pearlMode.getValue() != PearlMode.Collision && !mc.player.horizontalCollision) {
                return true;
            } else if (this.pearlMode.getValue() == PearlMode.Forward) {
                return !mc.options.forwardKey.isPressed();
            } else {
                return this.pearlMode.getValue() == PearlMode.Omni ? !Class5924.method2116() : this.method1954() == null;
            }
        }
    }

    private int method2010() {
        return Math.round(EntityUtil.method2146(method285())[0]) + 180;
    }

    private Vec3d method1954() {
        Vec3d var4 = null;
        Box var5 = mc.player.getBoundingBox(mc.player.getPose()).offset(mc.player.getPos()).expand(0.001, 0.0, 0.001);
        if (mc.world.isSpaceEmpty(var5)) {
            return null;
        } else {
            for (int var6 = 0; var6 < field565.length; var6++) {
                Vec3d var7 = field565[var6];
                Vec3d var8 = method285();
                Vec3d var9 = var8.add(var7);
                BlockPos var10 = BlockPos.ofFloored(var9);
                BlockPos var11 = var10.down();
                BlockState var12 = mc.world.getBlockState(var10);
                BlockState var13 = mc.world.getBlockState(var11);
                if (!this.method284(var12)
                        && !this.method284(var13)
                        && mc.world.getEntitiesByClass(EndCrystalEntity.class, new Box(var10), PearlPhase::lambda$getVec$2).isEmpty()
                        && (var4 == null || mc.player.getPos().distanceTo(var9) < mc.player.getPos().distanceTo(var4))) {
                    var4 = var9;
                }
            }

            return var4;
        }
    }

    private boolean method284(BlockState var1) {
        return var1.isAir() || var1.getBlock() == Blocks.LAVA || var1.getBlock() == Blocks.WATER;
    }

    private static Vec3d method285() {
        return new Vec3d(Math.floor(mc.player.getX()) + 0.5, mc.player.getY(), Math.floor(mc.player.getZ()) + 0.5);
    }

    private static boolean lambda$getVec$2(EndCrystalEntity var0) {
        return true;
    }

    private static boolean lambda$onInteract$1(ItemStack var0) {
        return var0.getItem() == Items.ENDER_PEARL;
    }

    private static boolean lambda$onInteract$0(ItemStack var0) {
        return var0.getItem() instanceof BlockItem;
    }
}
