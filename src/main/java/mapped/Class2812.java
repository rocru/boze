package mapped;

import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.PlaceAction;
import dev.boze.client.utils.RaycastUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Class2812 implements IMinecraft {
   public static ConcurrentHashMap<BlockPos, Long> field110 = new ConcurrentHashMap();

   public static PlaceAction method5497(Block block, BlockPos pos, boolean rotate) {
      return method5499(var2887, var2888, var2889, true, false);
   }

   public static PlaceAction method5498(Block block, BlockPos pos, boolean rotate, boolean swing) {
      return method5499(var2890, var2891, var2892, var2893, false);
   }

   public static PlaceAction method5499(Block block, BlockPos pos, boolean rotate, boolean swing, boolean strictDirection) {
      Hand var8 = null;
      int var9 = -1;
      ItemStack var10 = mc.player.getMainHandStack();
      if (var10 != ItemStack.EMPTY && var10.getItem() instanceof BlockItem) {
         Block var11 = ((BlockItem)var10.getItem()).getBlock();
         if (var11 == var2894) {
            var8 = Hand.MAIN_HAND;
            var9 = mc.player.getInventory().selectedSlot;
         }
      }

      ItemStack var15 = mc.player.getOffHandStack();
      if (var15 != ItemStack.EMPTY && var15.getItem() instanceof BlockItem) {
         Block var12 = ((BlockItem)var15.getItem()).getBlock();
         if (var12 == var2894) {
            var8 = Hand.OFF_HAND;
         }
      }

      if (var8 == null) {
         for (int var16 = 0; var16 < 9; var16++) {
            ItemStack var13 = mc.player.getInventory().getStack(var16);
            if (var13 != ItemStack.EMPTY && var13.getItem() instanceof BlockItem) {
               Block var14 = ((BlockItem)var13.getItem()).getBlock();
               if (var14 == var2894) {
                  var8 = Hand.MAIN_HAND;
                  var9 = var16;
                  break;
               }
            }
         }
      }

      return var8 == null ? null : method5501(var2895, var2896, var2897, var2898, var8, var9);
   }

   public static PlaceAction method5500(Item item, BlockPos pos, boolean rotate, boolean swing, boolean strictDirection) {
      Hand var8 = null;
      int var9 = -1;
      ItemStack var10 = mc.player.getMainHandStack();
      if (var10 != ItemStack.EMPTY && var10.getItem() instanceof BlockItem && var10.getItem() == var2899) {
         var8 = Hand.MAIN_HAND;
         var9 = mc.player.getInventory().selectedSlot;
      }

      ItemStack var11 = mc.player.getOffHandStack();
      if (var11 != ItemStack.EMPTY && var11.getItem() instanceof BlockItem && var11.getItem() == var2899) {
         var8 = Hand.OFF_HAND;
      }

      if (var8 == null) {
         for (int var12 = 0; var12 < 9; var12++) {
            ItemStack var13 = mc.player.getInventory().getStack(var12);
            if (var13 != ItemStack.EMPTY && var13.getItem() instanceof BlockItem && var13.getItem() == var2899) {
               var8 = Hand.MAIN_HAND;
               var9 = var12;
               break;
            }
         }
      }

      return var8 == null ? null : method5501(var2900, var2901, var2902, var2903, var8, var9);
   }

   public static PlaceAction method5501(BlockPos pos, boolean rotate, boolean swing, boolean strictDirection, Hand hand, int slot) {
      return method5502(var2904, var2905, var2906, var2907, false, var2908, var2909);
   }

   public static PlaceAction method5502(BlockPos pos, boolean rotate, boolean swing, boolean strictDirection, boolean rayTrace, Hand hand, int slot) {
      if (!method2101(var2910)) {
         return null;
      } else {
         Direction var10 = method5508(var2910, var2913, var2914);
         if (var10 == null) {
            return null;
         } else {
            BlockPos var11 = var2910.offset(var10);
            Direction var12 = var10.getOpposite();
            Vec3d var13 = new Vec3d((double)var11.getX() + 0.5, (double)var11.getY() + 0.5, (double)var11.getZ() + 0.5)
               .add(new Vec3d(var12.getUnitVector()).multiply(0.5));
            float[] var14 = EntityUtil.method2146(var13);
            return new PlaceAction(var11, var12, var14[0], var14[1], var2915, var2912, var2911, var2916);
         }
      }
   }

   public static boolean method2101(BlockPos pos) {
      return method5505(var2917, false, false);
   }

   public static boolean method5504(BlockPos pos, boolean strictDirection) {
      return method5505(var2918, var2919, false);
   }

   public static boolean method5505(BlockPos pos, boolean strictDirection, boolean rayTrace) {
      if (!Class2811.field103 && field110.containsKey(var2920)) {
         if (System.currentTimeMillis() - (Long)field110.get(var2920) <= 500L) {
            return false;
         }

         field110.remove(var2920);
      }

      if (Class2811.field104
         || mc.world.getBlockState(var2920).isReplaceable()
         || Class2811.field105 && mc.world.getBlockState(var2920).getBlock() instanceof BedBlock
         || Class2811.field109 && mc.world.getBlockState(var2920).getBlock() instanceof RespawnAnchorBlock) {
         if (method5508(var2920, var2921, var2922) == null) {
            return false;
         } else {
            return Class2811.field99 ? true : mc.world.canPlace(Blocks.DIRT.getDefaultState(), var2920, ShapeContext.absent());
         }
      } else {
         return false;
      }
   }

   public static boolean method5506(BlockPos pos, boolean strictDirection, boolean rayTrace) {
      if (!Class2811.field103 && field110.containsKey(var2923)) {
         if (System.currentTimeMillis() - (Long)field110.get(var2923) <= 500L) {
            return false;
         }

         field110.remove(var2923);
      }

      if (Class2811.field104
         || mc.world.getBlockState(var2923).isReplaceable()
         || Class2811.field105 && mc.world.getBlockState(var2923).getBlock() instanceof BedBlock
         || Class2811.field109 && mc.world.getBlockState(var2923).getBlock() instanceof RespawnAnchorBlock) {
         return Class2811.field99 ? true : mc.world.canPlace(Blocks.DIRT.getDefaultState(), var2923, ShapeContext.absent());
      } else {
         return false;
      }
   }

   public static boolean method2102(BlockPos pos) {
      if (Class2811.field106
         && !Options.INSTANCE.method1971()
         && AntiCheat.INSTANCE.field2318.method419()
         && field110.containsKey(var2926)
         && System.currentTimeMillis() - (Long)field110.get(var2926) > 500L) {
         field110.remove(var2926);
      }

      BlockState var4 = mc.world.getBlockState(var2926);
      return !Class2811.field107 && var4.getBlock() instanceof BedBlock
         ? false
         : Class2811.field101 && var4.isAir() || Class2811.field102 && !var4.getFluidState().isEmpty() || !var4.isAir() && var4.getFluidState().isEmpty();
   }

   public static Direction method5508(BlockPos pos, boolean strictDirection, boolean rayTrace) {
      ArrayList var6 = new ArrayList();

      for (Direction var10 : Direction.values()) {
         BlockPos var11 = var2927.offset(var10);
         if (method2102(var11)) {
            Vec3d var12 = EntityUtil.method2144(mc.player);
            if (var2929) {
               Vec3d var13 = new Vec3d((double)var2927.getX() + 0.5, (double)var2927.getY() + 0.5, (double)var2927.getZ() + 0.5)
                  .add(new Vec3d(var10.getUnitVector()).multiply(0.5));
               if (!RaycastUtil.method116(var13) && var12.distanceTo(var13) > (double)Class2811.field108) {
                  continue;
               }
            }

            if (var2928) {
               Vec3d var17 = new Vec3d((double)var11.getX() + 0.5, (double)var11.getY() + 0.5, (double)var11.getZ() + 0.5);
               BlockState var14 = mc.world.getBlockState(var11);
               boolean var15 = var14.getBlock() == Blocks.AIR || var14.isFullCube(mc.world, var11);
               ArrayList var16 = new ArrayList();
               var16.addAll(method5509(var12.x - var17.x, Direction.WEST, Direction.EAST, !var15));
               var16.addAll(method5509(var12.y - var17.y, Direction.DOWN, Direction.UP, true));
               var16.addAll(method5509(var12.z - var17.z, Direction.NORTH, Direction.SOUTH, !var15));
               if (!var16.contains(var10.getOpposite())) {
                  continue;
               }
            }

            var6.add(var10);
         }
      }

      return (Direction)var6.stream().min(Comparator.comparing(Class2812::lambda$getPlaceDirection$0)).orElse(null);
   }

   public static ArrayList<Direction> method5509(double diff, Direction negativeSide, Direction positiveSide, boolean bothIfInRange) {
      ArrayList var8 = new ArrayList();
      if (var2930 < -0.5) {
         var8.add(var2931);
      }

      if (var2930 > 0.5) {
         var8.add(var2932);
      }

      if (var2933) {
         if (!var8.contains(var2931)) {
            var8.add(var2931);
         }

         if (!var8.contains(var2932)) {
            var8.add(var2932);
         }
      }

      return var8;
   }

   private static Double lambda$getPlaceDirection$0(BlockPos var0, Direction var1) {
      return EntityUtil.method2144(mc.player)
         .distanceTo(
            new Vec3d((double)var0.getX() + 0.5, (double)var0.getY() + 0.5, (double)var0.getZ() + 0.5).add(new Vec3d(var1.getUnitVector()).multiply(0.5))
         );
   }
}
