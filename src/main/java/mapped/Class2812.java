package mapped;

import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.PlaceAction;
import dev.boze.client.utils.RaycastUtil;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class Class2812 implements IMinecraft {
    public static ConcurrentHashMap<BlockPos, Long> field110;

    static {
        Class2812.field110 = new ConcurrentHashMap<BlockPos, Long>();
    }

    public Class2812() {
        super();
    }

    public static PlaceAction method5497(final Block block, final BlockPos pos, final boolean rotate) {
        return method5499(block, pos, rotate, true, false);
    }

    public static PlaceAction method5498(final Block block, final BlockPos pos, final boolean rotate, final boolean swing) {
        return method5499(block, pos, rotate, swing, false);
    }

    public static PlaceAction method5499(final Block block, final BlockPos pos, final boolean rotate, final boolean swing, final boolean strictDirection) {
        Hand hand = null;
        int selectedSlot = -1;
        final ItemStack mainHandStack = Class2812.mc.player.getMainHandStack();
        if (mainHandStack != ItemStack.EMPTY && mainHandStack.getItem() instanceof BlockItem && ((BlockItem) mainHandStack.getItem()).getBlock() == block) {
            hand = Hand.MAIN_HAND;
            selectedSlot = Class2812.mc.player.getInventory().selectedSlot;
        }
        final ItemStack offHandStack = Class2812.mc.player.getOffHandStack();
        if (offHandStack != ItemStack.EMPTY && offHandStack.getItem() instanceof BlockItem && ((BlockItem) offHandStack.getItem()).getBlock() == block) {
            hand = Hand.OFF_HAND;
        }
        if (hand == null) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Class2812.mc.player.getInventory().getStack(i);
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == block) {
                    hand = Hand.MAIN_HAND;
                    selectedSlot = i;
                    break;
                }
            }
        }
        if (hand == null) {
            return null;
        }
        return method5501(pos, rotate, swing, strictDirection, hand, selectedSlot);
    }

    public static PlaceAction method5500(final Item item, final BlockPos pos, final boolean rotate, final boolean swing, final boolean strictDirection) {
        Hand hand = null;
        int selectedSlot = -1;
        final ItemStack mainHandStack = Class2812.mc.player.getMainHandStack();
        if (mainHandStack != ItemStack.EMPTY && mainHandStack.getItem() instanceof BlockItem && mainHandStack.getItem() == item) {
            hand = Hand.MAIN_HAND;
            selectedSlot = Class2812.mc.player.getInventory().selectedSlot;
        }
        final ItemStack offHandStack = Class2812.mc.player.getOffHandStack();
        if (offHandStack != ItemStack.EMPTY && offHandStack.getItem() instanceof BlockItem && offHandStack.getItem() == item) {
            hand = Hand.OFF_HAND;
        }
        if (hand == null) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Class2812.mc.player.getInventory().getStack(i);
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof BlockItem && stack.getItem() == item) {
                    hand = Hand.MAIN_HAND;
                    selectedSlot = i;
                    break;
                }
            }
        }
        if (hand == null) {
            return null;
        }
        return method5501(pos, rotate, swing, strictDirection, hand, selectedSlot);
    }

    public static PlaceAction method5501(final BlockPos pos, final boolean rotate, final boolean swing, final boolean strictDirection, final Hand hand, final int slot) {
        return method5502(pos, rotate, swing, strictDirection, false, hand, slot);
    }

    public static PlaceAction method5502(final BlockPos pos, final boolean rotate, final boolean swing, final boolean strictDirection, final boolean rayTrace, final Hand hand, final int slot) {
        if (!method2101(pos)) {
            return null;
        }
        final Direction method5508 = method5508(pos, strictDirection, rayTrace);
        if (method5508 == null) {
            return null;
        }
        final BlockPos offset = pos.offset(method5508);
        final Direction opposite = method5508.getOpposite();
        final float[] method5509 = EntityUtil.method2146(new Vec3d(offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5).add(new Vec3d(opposite.getUnitVector()).multiply(0.5)));
        return new PlaceAction(offset, opposite, method5509[0], method5509[1], hand, swing, rotate, slot);
    }

    public static boolean method2101(final BlockPos pos) {
        return method5505(pos, false, false);
    }

    public static boolean method5504(final BlockPos pos, final boolean strictDirection) {
        return method5505(pos, strictDirection, false);
    }

    public static boolean method5505(final BlockPos pos, final boolean strictDirection, final boolean rayTrace) {
        if (!Class2811.field103 && Class2812.field110.containsKey(pos)) {
            if (System.currentTimeMillis() - Class2812.field110.get(pos) <= 500L) {
                return false;
            }
            Class2812.field110.remove(pos);
        }
        return (Class2811.field104 || Class2812.mc.world.getBlockState(pos).isReplaceable() || (Class2811.field105 && Class2812.mc.world.getBlockState(pos).getBlock() instanceof BedBlock) || (Class2811.field109 && Class2812.mc.world.getBlockState(pos).getBlock() instanceof RespawnAnchorBlock)) && method5508(pos, strictDirection, rayTrace) != null && (Class2811.field99 || Class2812.mc.world.canPlace(Blocks.DIRT.getDefaultState(), pos, ShapeContext.absent()));
    }

    public static boolean method5506(final BlockPos pos, final boolean strictDirection, final boolean rayTrace) {
        if (!Class2811.field103 && Class2812.field110.containsKey(pos)) {
            if (System.currentTimeMillis() - Class2812.field110.get(pos) <= 500L) {
                return false;
            }
            Class2812.field110.remove(pos);
        }
        return (Class2811.field104 || Class2812.mc.world.getBlockState(pos).isReplaceable() || (Class2811.field105 && Class2812.mc.world.getBlockState(pos).getBlock() instanceof BedBlock) || (Class2811.field109 && Class2812.mc.world.getBlockState(pos).getBlock() instanceof RespawnAnchorBlock)) && (Class2811.field99 || Class2812.mc.world.canPlace(Blocks.DIRT.getDefaultState(), pos, ShapeContext.absent()));
    }

    public static boolean method2102(final BlockPos pos) {
        if (Class2811.field106 && !Options.INSTANCE.method1971() && AntiCheat.INSTANCE.field2318.getValue() && Class2812.field110.containsKey(pos) && System.currentTimeMillis() - Class2812.field110.get(pos) > 500L) {
            Class2812.field110.remove(pos);
        }
        final BlockState blockState = Class2812.mc.world.getBlockState(pos);
        return (Class2811.field107 || !(blockState.getBlock() instanceof BedBlock)) && ((Class2811.field101 && blockState.isAir()) || (Class2811.field102 && !blockState.getFluidState().isEmpty()) || (!blockState.isAir() && blockState.getFluidState().isEmpty()));
    }

    public static Direction method5508(BlockPos var2927, boolean var2928, boolean var2929) {
        ArrayList<Direction> arrayList = new ArrayList<Direction>();
        for (Direction direction : Direction.values()) {
            Vec3d vec3d;
            BlockPos blockPos = var2927.offset(direction);
            if (!Class2812.method2102(blockPos)) continue;
            Vec3d vec3d2 = EntityUtil.method2144(Class2812.mc.player);
            if (var2929 && !RaycastUtil.method116(vec3d = new Vec3d((double) var2927.getX() + 0.5, (double) var2927.getY() + 0.5, (double) var2927.getZ() + 0.5).add(new Vec3d(direction.getUnitVector()).multiply(0.5))) && vec3d2.distanceTo(vec3d) > (double) Class2811.field108)
                continue;
            if (var2928) {
                vec3d = new Vec3d((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5);
                BlockState blockState = Class2812.mc.world.getBlockState(blockPos);
                boolean bl = blockState.getBlock() == Blocks.AIR || blockState.isFullCube(Class2812.mc.world, blockPos);
                ArrayList<Direction> arrayList2 = new ArrayList<Direction>();
                arrayList2.addAll(Class2812.method5509(vec3d2.x - vec3d.x, Direction.WEST, Direction.EAST, !bl));
                arrayList2.addAll(Class2812.method5509(vec3d2.y - vec3d.y, Direction.DOWN, Direction.UP, true));
                arrayList2.addAll(Class2812.method5509(vec3d2.z - vec3d.z, Direction.NORTH, Direction.SOUTH, !bl));
                if (!arrayList2.contains(direction.getOpposite())) continue;
            }
            arrayList.add(direction);
        }
        return arrayList.stream().min(Comparator.comparing(arg_0 -> Class2812.lambda$getPlaceDirection$0(var2927, arg_0))).orElse(null);
    }

    public static ArrayList<Direction> method5509(final double diff, final Direction negativeSide, final Direction positiveSide, final boolean bothIfInRange) {
        final ArrayList list = new ArrayList();
        if (diff < -0.5) {
            list.add(negativeSide);
        }
        if (diff > 0.5) {
            list.add(positiveSide);
        }
        if (bothIfInRange) {
            if (!list.contains(negativeSide)) {
                list.add(negativeSide);
            }
            if (!list.contains(positiveSide)) {
                list.add(positiveSide);
            }
        }
        return list;
    }

    private static Double lambda$getPlaceDirection$0(final BlockPos blockPos, final Direction direction) {
        return EntityUtil.method2144(Class2812.mc.player).distanceTo(new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5).add(new Vec3d(direction.getUnitVector()).multiply(0.5)));
    }
}
