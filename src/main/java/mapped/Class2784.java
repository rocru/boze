package mapped;

import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.Vec3dHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.CompassItem;
import net.minecraft.item.DebugStickItem;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.HoneycombItem;
import net.minecraft.item.Item;
import net.minecraft.item.LeadItem;
import net.minecraft.item.MinecartItem;
import net.minecraft.item.PowderSnowBucketItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class Class2784 implements IMinecraft {
   public static final List<Class<? extends Item>> field98 = Arrays.asList(
      ArmorStandItem.class,
      AxeItem.class,
      BlockItem.class,
      BoneMealItem.class,
      CompassItem.class,
      DebugStickItem.class,
      DecorationItem.class,
      EndCrystalItem.class,
      EnderEyeItem.class,
      FilledMapItem.class,
      FireChargeItem.class,
      FireworkRocketItem.class,
      FlintAndSteelItem.class,
      HoeItem.class,
      HoneycombItem.class,
      LeadItem.class,
      MinecartItem.class,
      PowderSnowBucketItem.class,
      ShearsItem.class,
      ShovelItem.class,
      SpawnEggItem.class,
      WritableBookItem.class,
      WrittenBookItem.class
   );

   public static void method1801(BlockPos pos) {
      BlockHitResult var4 = new BlockHitResult(Vec3d.ofCenter(var2852).add(0.0, 0.5, 0.0), Direction.UP, var2852, false);
      if (!field98.contains(mc.player.getMainHandStack().getItem().getClass())) {
         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
         Class5913.method17(Hand.MAIN_HAND, var4);
      } else if (!field98.contains(mc.player.getOffHandStack().getItem().getClass())) {
         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
         Class5913.method17(Hand.OFF_HAND, var4);
      } else {
         mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, var4.getBlockPos(), var4.getSide()));
         mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.ABORT_DESTROY_BLOCK, var4.getBlockPos(), var4.getSide()));
      }
   }

   public static boolean method5443(BlockHitResult result, Direction side, BlockPos blockPos) {
      return var2853.getSide() == var2854.getOpposite() && var2853.getBlockPos().equals(var2855);
   }

   public static boolean method5444(BlockHitResult result, BlockHitResult other) {
      return var2856 != null && var2857 != null ? var2856.getSide() == var2857.getSide() && var2856.getBlockPos().equals(var2857.getBlockPos()) : false;
   }

   public static boolean method2101(BlockPos pos) {
      if (!Class2811.field103 && Class2812.field110.containsKey(var2858)) {
         if (System.currentTimeMillis() - (Long)Class2812.field110.get(var2858) <= 500L) {
            return false;
         }

         Class2812.field110.remove(var2858);
      }

      if (Class2811.field104
         || mc.world.getBlockState(var2858).isReplaceable()
         || mc.world.getBlockState(var2858).getBlock() == Blocks.SNOW
         || Class2811.field105 && mc.world.getBlockState(var2858).getBlock() instanceof BedBlock
         || Class2811.field109 && mc.world.getBlockState(var2858).getBlock() instanceof RespawnAnchorBlock) {
         if (Class2811.field99) {
            return true;
         } else {
            VoxelShape var4 = Blocks.DIRT.getDefaultState().getCollisionShape(mc.world, var2858, ShapeContext.absent());
            Entity var5 = null;
            if (Class2811.field100) {
               List var6 = mc.world.getOtherEntities(null, new Box(var2858), Class2784::lambda$canPlaceBlock$0);
               if (!var6.isEmpty()) {
                  var5 = (Entity)var6.get(0);
               }
            }

            return var4.isEmpty()
               || mc.world.doesNotIntersectEntities(var5, var4.offset((double)var2858.getX(), (double)var2858.getY(), (double)var2858.getZ()));
         }
      } else {
         return false;
      }
   }

   public static BlockHitResult method5446(BlockPos pos, boolean strictDirection, boolean rayTrace) {
      ArrayList var6 = new ArrayList();

      for (Direction var10 : Direction.values()) {
         BlockPos var11 = var2859.offset(var10);
         if (Class2812.method2102(var11)) {
            Vec3d var12 = EntityUtil.method2144(mc.player);
            Vec3d var13 = new Vec3d((double)var2859.getX() + 0.5, (double)var2859.getY() + 0.5, (double)var2859.getZ() + 0.5)
               .add(new Vec3d(var10.getUnitVector()).multiply(0.5));
            boolean var14 = false;
            if (var2861) {
               if (!RaycastUtil.method116(var13) && var12.distanceTo(var13) > (double)Class2811.field108) {
                  Vec3d[] var15 = Vec3dHelper.method2170(var10.getAxis());
                  if (var15 != null) {
                     for (Vec3d var19 : var15) {
                        Vec3d var20 = var13.add(var19);
                        if (var12.distanceTo(var20) <= (double)Class2811.field108 || RaycastUtil.method116(var20)) {
                           var13 = var20;
                           var14 = true;
                           break;
                        }
                     }
                  }
               } else {
                  var14 = true;
               }
            } else {
               var14 = true;
            }

            if (var14) {
               if (var2860) {
                  Vec3d var21 = new Vec3d((double)var11.getX() + 0.5, (double)var11.getY() + 0.5, (double)var11.getZ() + 0.5);
                  BlockState var22 = mc.world.getBlockState(var11);
                  boolean var23 = var22.getBlock() == Blocks.AIR || var22.isFullCube(mc.world, var11);
                  ArrayList var24 = new ArrayList();
                  var24.addAll(Class2812.method5509(var12.x - var21.x, Direction.WEST, Direction.EAST, !var23));
                  var24.addAll(Class2812.method5509(var12.y - var21.y, Direction.DOWN, Direction.UP, true));
                  var24.addAll(Class2812.method5509(var12.z - var21.z, Direction.NORTH, Direction.SOUTH, !var23));
                  if (!var24.contains(var10.getOpposite())) {
                     continue;
                  }
               }

               var6.add(new BlockHitResult(var13, var10.getOpposite(), var11, false));
            }
         }
      }

      return (BlockHitResult)var6.stream().min(Comparator.comparing(Class2784::lambda$getPlacement$1)).orElse(null);
   }

   private static Double lambda$getPlacement$1(BlockHitResult var0) {
      return EntityUtil.method2144(mc.player).distanceTo(var0.getPos());
   }

   private static boolean lambda$canPlaceBlock$0(Entity var0) {
      return var0 instanceof EndCrystalEntity;
   }
}
