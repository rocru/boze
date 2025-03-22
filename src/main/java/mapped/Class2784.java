package mapped;

import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.Vec3dHelper;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Class2784 implements IMinecraft {
    public static final List<Class<? extends Item>> field98;

    static {
        field98 = Arrays.asList(ArmorStandItem.class, AxeItem.class, BlockItem.class, BoneMealItem.class, CompassItem.class, DebugStickItem.class, DecorationItem.class, EndCrystalItem.class, EnderEyeItem.class, FilledMapItem.class, FireChargeItem.class, FireworkRocketItem.class, FlintAndSteelItem.class, HoeItem.class, HoneycombItem.class, LeadItem.class, MinecartItem.class, PowderSnowBucketItem.class, ShearsItem.class, ShovelItem.class, SpawnEggItem.class, WritableBookItem.class, WrittenBookItem.class);
    }

    public Class2784() {
        super();
    }

    public static void method1801(final BlockPos pos) {
        final BlockHitResult blockHitResult = new BlockHitResult(Vec3d.ofCenter(pos).add(0.0, 0.5, 0.0), Direction.UP, pos, false);
        if (!Class2784.field98.contains(Class2784.mc.player.getMainHandStack().getItem().getClass())) {
            ((ClientPlayerInteractionManagerAccessor) Class2784.mc.interactionManager).callSyncSelectedSlot();
            Class5913.method17(Hand.MAIN_HAND, blockHitResult);
        } else if (!Class2784.field98.contains(Class2784.mc.player.getOffHandStack().getItem().getClass())) {
            ((ClientPlayerInteractionManagerAccessor) Class2784.mc.interactionManager).callSyncSelectedSlot();
            Class5913.method17(Hand.OFF_HAND, blockHitResult);
        } else {
            Class2784.mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockHitResult.getBlockPos(), blockHitResult.getSide()));
            Class2784.mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, blockHitResult.getBlockPos(), blockHitResult.getSide()));
        }
    }

    public static boolean method5443(final BlockHitResult result, final Direction side, final BlockPos blockPos) {
        return result.getSide() == side.getOpposite() && result.getBlockPos().equals(blockPos);
    }

    public static boolean method5444(final BlockHitResult result, final BlockHitResult other) {
        return result != null && other != null && result.getSide() == other.getSide() && result.getBlockPos().equals(other.getBlockPos());
    }

    public static boolean method2101(final BlockPos pos) {
        if (!Class2811.field103 && Class2812.field110.containsKey(pos)) {
            if (System.currentTimeMillis() - Class2812.field110.get(pos) <= 500L) {
                return false;
            }
            Class2812.field110.remove(pos);
        }
        if (!Class2811.field104 && !Class2784.mc.world.getBlockState(pos).isReplaceable() && Class2784.mc.world.getBlockState(pos).getBlock() != Blocks.SNOW && (!Class2811.field105 || !(Class2784.mc.world.getBlockState(pos).getBlock() instanceof BedBlock)) && (!Class2811.field109 || !(Class2784.mc.world.getBlockState(pos).getBlock() instanceof RespawnAnchorBlock))) {
            return false;
        }
        if (Class2811.field99) {
            return true;
        }
        final VoxelShape collisionShape = Blocks.DIRT.getDefaultState().getCollisionShape(Class2784.mc.world, pos, ShapeContext.absent());
        Entity entity = null;
        if (Class2811.field100) {
            final List otherEntities = Class2784.mc.world.getOtherEntities(null, new Box(pos), Class2784::lambda$canPlaceBlock$0);
            if (!otherEntities.isEmpty()) {
                entity = (Entity) otherEntities.get(0);
            }
        }
        return collisionShape.isEmpty() || Class2784.mc.world.doesNotIntersectEntities(entity, collisionShape.offset(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static BlockHitResult method5446(final BlockPos pos, final boolean strictDirection, final boolean rayTrace) {
        final ArrayList list = new ArrayList();
        final Direction[] values = Direction.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final Direction direction = values[i];
            final BlockPos offset = pos.offset(direction);
            if (Class2812.method2102(offset)) {
                final Vec3d method2144 = EntityUtil.method2144(Class2784.mc.player);
                Vec3d add = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).add(new Vec3d(direction.getUnitVector()).multiply(0.5));
                boolean b = false;
                if (rayTrace) {
                    if (!RaycastUtil.method116(add) && method2144.distanceTo(add) > Class2811.field108) {
                        final Vec3d[] method2145 = Vec3dHelper.method2170(direction.getAxis());
                        if (method2145 != null) {
                            final Vec3d[] array = method2145;
                            for (int length2 = array.length, j = 0; j < length2; ++j) {
                                final Vec3d add2 = add.add(array[j]);
                                if (method2144.distanceTo(add2) <= Class2811.field108 || RaycastUtil.method116(add2)) {
                                    add = add2;
                                    b = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        b = true;
                    }
                } else {
                    b = true;
                }
                if (b) {
                    if (strictDirection) {
                        final Vec3d vec3d = new Vec3d(offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
                        final BlockState blockState = Class2784.mc.world.getBlockState(offset);
                        final boolean b2 = blockState.getBlock() == Blocks.AIR || blockState.isFullCube(Class2784.mc.world, offset);
                        final ArrayList list2 = new ArrayList();
                        list2.addAll(Class2812.method5509(method2144.x - vec3d.x, Direction.WEST, Direction.EAST, !b2));
                        list2.addAll(Class2812.method5509(method2144.y - vec3d.y, Direction.DOWN, Direction.UP, true));
                        list2.addAll(Class2812.method5509(method2144.z - vec3d.z, Direction.NORTH, Direction.SOUTH, !b2));
                        if (!list2.contains(direction.getOpposite())) {
                            continue;
                        }
                    }
                    list.add(new BlockHitResult(add, direction.getOpposite(), offset, false));
                }
            }
        }
        return (BlockHitResult) list.stream().min(Comparator.comparing(Class2784::lambda$getPlacement$1)).orElse(null);
    }

    private static Double lambda$getPlacement$1(final BlockHitResult blockHitResult) {
        return EntityUtil.method2144(Class2784.mc.player).distanceTo(blockHitResult.getPos());
    }

    private static boolean lambda$canPlaceBlock$0(final Entity entity) {
        return entity instanceof EndCrystalEntity;
    }
}
