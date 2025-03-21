package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.PistonPushStage;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.PlaceAction;
import mapped.Class2811;
import mapped.Class2812;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class PistonPush extends Module {
    public static final PistonPush INSTANCE = new PistonPush();
    private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "Multi Task");
    private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing");
    private final MinMaxSetting range = new MinMaxSetting("Range", 4.5, 1.0, 6.0, 0.1, "Range");
    private final BooleanSetting fillHole = new BooleanSetting("FillHole", false, "Fill hole after pushing player out");
    public static final BooleanSetting onlyWhileSneaking = new BooleanSetting("OnlyWhileSneaking", false, "Only push while sneaking");
    private PlayerEntity field2557;
    private BlockPos field2558;
    private BlockPos field2559;
    private BlockPos field2560;
    private BlockPos field2561;
    private Direction field2562;
    private PistonPushStage stage;

    public PistonPush() {
        super("PistonPush", "Pushes players out of holes with pistons", Category.Combat);
    }

    @Override
    public void onEnable() {
        if (!MinecraftUtils.isClientActive()) {
            this.setEnabled(false);
        } else {
            this.field2558 = null;
            this.field2559 = null;
            this.field2560 = null;
            this.field2562 = null;
            this.stage = PistonPushStage.Initial;
            this.field2557 = this.method1520();
            if (this.field2557 == null) {
                this.setEnabled(false);
                ChatInstance.method624("No target in range");
            }
        }
    }

    @EventHandler
    public void method1507(MovementEvent event) {
        if (onlyWhileSneaking.getValue() && !PistonPush.mc.player.input.sneaking) {
            return;
        }
        if (Options.method477(this.multiTask.getValue())) {
            return;
        }
        if (this.stage == null) {
            this.stage = PistonPushStage.Initial;
        }
        switch (this.stage.ordinal()) {
            case 0: {
                if (this.field2557 == null) {
                    this.stage = PistonPushStage.Done;
                    return;
                }
                this.field2561 = this.field2557.getBlockPos().up();
                this.field2558 = this.method1512(this.field2561);
                this.field2559 = this.method1511(this.field2558);
                this.field2560 = this.field2561.down();
                if (this.method1517(this.field2561, this.field2558, this.field2559, this.field2560)) {
                    this.stage = PistonPushStage.Done;
                    break;
                }
                if (this.method1518(this.field2561, this.field2558, this.field2559, this.field2560)) {
                    this.stage = PistonPushStage.Done;
                    break;
                }
                this.stage = PistonPushStage.Piston;
                break;
            }
            case 1: {
                if (!this.method1508(InventoryHelper.method163(Blocks.PISTON, Blocks.STICKY_PISTON), this.field2558, event)) break;
                this.stage = PistonPushStage.Redstone;
                break;
            }
            case 2: {
                if (!this.method1508(InventoryHelper.method163(Blocks.REDSTONE_BLOCK), this.field2559, event)) break;
                this.stage = PistonPushStage.Obsidian;
                break;
            }
            case 3: {
                if (this.fillHole.getValue()) {
                    Class2811.field99 = true;
                    this.method1508(InventoryHelper.method163(Blocks.OBSIDIAN), this.field2560, event);
                    Class2811.field99 = false;
                }
                this.stage = PistonPushStage.Done;
                break;
            }
            case 4: {
                this.setEnabled(false);
            }
        }
    }

    private boolean method1508(int var1, BlockPos var2, MovementEvent var3) {
        if (var2 != null && var1 != -1) {
            PlaceAction var7 = Class2812.method5501(var2, true, this.swing.getValue(), false, Hand.MAIN_HAND, var1);
            if (var7 == null) {
                return false;
            } else {
                float var8 = (float) this.method1509(this.field2562);
                var7.method2158(var8);
                var3.method1074(new ActionWrapper(var7));
                return true;
            }
        } else {
            return false;
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private int method1509(Direction var1) {
        if (var1 == null) {
            return (int) mc.player.getYaw();
        } else {
            return switch (var1) {
                case Direction.NORTH -> 180;
                case Direction.SOUTH -> 0;
                case Direction.WEST -> 90;
                default -> -90;
            };
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private Direction method1510(Direction var1) {
        return switch (var1) {
            case Direction.NORTH -> Direction.SOUTH;
            case Direction.SOUTH -> Direction.NORTH;
            case Direction.WEST -> Direction.EAST;
            default -> Direction.WEST;
        };
    }

    private BlockPos method1511(BlockPos var1) {
        ArrayList var5 = new ArrayList();
        if (var1 == null) {
            return null;
        } else {
            for (Direction var9 : Direction.values()) {
                if (!this.method1514(var1.offset(var9)) && this.method1513(var1.offset(var9))) {
                    var5.add(var1.offset(var9));
                }
            }

            if (var5.isEmpty()) {
                return null;
            } else {
                var5.sort(Comparator.comparingDouble(PistonPush::method1515));
                return (BlockPos) var5.get(0);
            }
        }
    }

    private BlockPos method1512(BlockPos var1) {
        ArrayList var5 = new ArrayList();

        for (Direction var9 : Direction.values()) {
            if (var9 != Direction.DOWN && var9 != Direction.UP && !this.method1514(var1.offset(var9))) {
                boolean var10 = this.method1519(var1.up())
                        && this.method1519(var1.offset(this.method1510(var9)))
                        && this.method1519(var1.offset(this.method1510(var9)).up());
                if (Class2812.method2101(var1.offset(var9)) && var10) {
                    var5.add(var1.offset(var9));
                }
            }
        }

        if (var5.isEmpty()) {
            return null;
        } else {
            var5.sort(Comparator.comparingDouble(PistonPush::method1515));
            this.field2562 = this.method1516(var1, (BlockPos) var5.get(0));
            return (BlockPos) var5.get(0);
        }
    }

    private boolean method1513(BlockPos var1) {
        if (Class2811.field104
                || mc.world.getBlockState(var1).isReplaceable()
                || Class2811.field105 && mc.world.getBlockState(var1).getBlock() instanceof BedBlock) {
            return Class2811.field99 || mc.world.canPlace(Blocks.DIRT.getDefaultState(), var1, ShapeContext.absent());
        } else {
            return false;
        }
    }

    private boolean method1514(BlockPos var1) {
        return !mc.world.canPlace(Blocks.DIRT.getDefaultState(), var1, ShapeContext.absent());
    }

    private static double method1515(BlockPos var0) {
        return new Vec3d((double) var0.getX() + 0.5, (double) var0.getY() + 0.5, (double) var0.getZ() + 0.5).distanceTo(mc.player.getEyePos());
    }

    private Direction method1516(BlockPos var1, BlockPos var2) {
        for (Direction var9 : Direction.values()) {
            if (var9 != Direction.DOWN && var9 != Direction.UP && var1.offset(var9).equals(var2)) {
                return var9;
            }
        }

        return null;
    }

    private boolean method1517(BlockPos... var1) {
        for (BlockPos var8 : var1) {
            if (var8 == null) {
                return true;
            }
        }

        return false;
    }

    private boolean method1518(BlockPos... var1) {
        for (BlockPos var8 : var1) {
            if (var8 != null
                    && new Vec3d((double) var8.getX() + 0.5, (double) var8.getY() + 0.5, (double) var8.getZ() + 0.5).distanceTo(mc.player.getEyePos())
                    > this.range.getValue() + 0.5) {
                return true;
            }
        }

        return false;
    }

    private boolean method1519(BlockPos var1) {
        return mc.world.getBlockState(var1).isAir();
    }

    private PlayerEntity method1520() {
        ArrayList var4 = new ArrayList();

        for (Entity var6 : mc.world.getEntities()) {
            if (var6 instanceof PlayerEntity
                    && var6 != mc.player
                    && !Friends.method2055(var6)
                    && !(var6.getEyePos().distanceTo(mc.player.getEyePos()) > this.range.getValue() + 1.0)
                    && !((PlayerEntity) var6).isDead()
                    && !(((PlayerEntity) var6).getHealth() + ((LivingEntity) var6).getAbsorptionAmount() <= 0.0F)) {
                var4.add(var6);
            }
        }

        return (PlayerEntity) var4.stream().min(Comparator.comparing(PistonPush::lambda$getTarget$0)).orElse(null);
    }

    private static Float lambda$getTarget$0(PlayerEntity var0) {
        return var0.distanceTo(mc.player);
    }
}
