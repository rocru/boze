package dev.boze.client.utils;

import dev.boze.client.enums.CheckEntityMode;
import dev.boze.client.enums.CrystalAttackMode;
import dev.boze.client.enums.TrapMode;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.utils.trackers.BlockBreakingTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import mapped.Class27;
import mapped.Class3069;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class TrapUtil implements IMinecraft {
   private static final HashSet<BlockPos> field1351 = new HashSet();
   private static final LinkedList<HitResult> field1352 = new LinkedList();

   public static HitResult[] method584(PlaceHandler helper, Box box, TrapMode boxType) {
      return method585(helper, box, boxType, TrapUtil::lambda$getNextNActions$0);
   }

   public static HitResult[] method585(PlaceHandler helper, Box box, TrapMode boxType, Predicate<BlockPos> validity) {
      BlockPos[] var7 = method586(box, boxType);
      BlockPos[] var8;
      if (helper.field245.method419()) {
         for (BlockPos var12 : var7) {
            for (PlayerEntity var14 : mc.world.getEntitiesByClass(PlayerEntity.class, new Box(var12), TrapUtil::lambda$getNextNActions$1)) {
               if (Math.floor(var14.getY() + 0.13) < Math.floor(mc.player.getY() + 0.13)) {
                  box = box.union(
                     var14.getBoundingBox().offset(0.0, Math.floor(mc.player.getY() + 0.13) - Math.floor(var14.getY() + 0.13), 0.0).shrink(1.0E-4, 0.0, 1.0E-4)
                  );
               } else {
                  box = box.union(var14.getBoundingBox().shrink(1.0E-4, 0.0, 1.0E-4));
               }
            }
         }

         var8 = method586(box, boxType);
      } else {
         var8 = var7;
      }

      HashMap var24 = BlockBreakingTracker.field1511.method666(helper.field248.method419());
      if (helper.field246.method419()) {
         for (BlockPos var34 : (BlockPos[])var8.clone()) {
            if (var24.containsKey(var34)) {
               BlockPos[] var38 = new BlockPos[var8.length + 6];
               System.arraycopy(var8, 0, var38, 0, var8.length);

               for (Direction var18 : Direction.values()) {
                  var38[var8.length + var18.ordinal()] = var34.offset(var18);
               }

               var8 = var38;
            }
         }
      }

      int var26 = var8.length;

      for (int var28 = 0; var28 < var8.length; var28++) {
         BlockPos var31 = var8[var28];
         if (!validity.test(var31)) {
            var8[var28] = null;
            var26--;
         } else if (EntityTracker.method2143(field1351, var31, CheckEntityMode.NotCrystals)
            || helper.field247.method419() && !mc.world.getBlockState(var31).isAir() && (Float)var24.getOrDefault(var31, 0.0F) > 0.8F) {
            HitResult var35 = helper.method150(field1351, var31);
            if (var35 != null) {
               field1352.add(var35);
               field1351.add(var31);
               var8[var28] = null;
               var26--;
               if (field1352.size() >= helper.method2010()) {
                  break;
               }
            }
         } else {
            var8[var28] = null;
            var26--;
         }
      }

      HashSet var29 = new HashSet();
      if (var26 > 0 && field1352.size() < helper.method2010()) {
         for (int var32 = 0; var32 < var8.length; var32++) {
            BlockPos var36 = var8[var32];
            if (var36 != null && !var29.contains(var36)) {
               if ((double)var36.getY() > box.maxY) {
                  for (Direction var50 : Direction.values()) {
                     BlockPos var53 = var36.offset(var50);
                     if (EntityTracker.field3914.containsKey(var53)) {
                        break;
                     }

                     if (EntityTracker.method2143(field1351, var53, CheckEntityMode.NotCrystals)) {
                        HitResult var19 = helper.method150(field1351, var53);
                        if (var19 != null
                           && helper.method147().ac.method567(var53, var50.getOpposite(), helper.field238.method419(), helper.field239.method419())) {
                           var29.add(var36);
                           field1352.add(var19);
                           field1351.add(var53);
                           break;
                        }
                     }
                  }
               } else {
                  for (int var40 = 1; var40 <= 3; var40++) {
                     BlockPos var44 = var36.offset(Direction.DOWN, var40);
                     if (EntityTracker.method2143(field1351, var44, CheckEntityMode.NotCrystals)) {
                        HitResult var47 = helper.method150(field1351, var44);
                        if (var47 != null) {
                           field1352.add(var47);
                           field1351.add(var44);
                           if (field1352.size() >= helper.method2010()) {
                              break;
                           }

                           for (int var51 = var40 - 1; var51 >= 1; var51--) {
                              BlockPos var54 = var36.offset(Direction.DOWN, var51);
                              if (EntityTracker.method2143(field1351, var54, CheckEntityMode.NotCrystals)) {
                                 var47 = helper.method150(field1351, var54);
                                 if (var47 != null) {
                                    field1352.add(var47);
                                    field1351.add(var54);
                                    if (field1352.size() >= helper.method2010()) {
                                       break;
                                    }
                                 }
                              }
                           }

                           if (field1352.size() >= helper.method2010()) {
                              break;
                           }
                        }
                     }
                  }
               }

               HitResult var41 = helper.method150(field1351, var36);
               if (var41 != null) {
                  field1352.add(var41);
                  field1351.add(var36);
                  var8[var32] = null;
                  var26--;
                  if (field1352.size() >= helper.method2010()) {
                     break;
                  }
               }
            }
         }
      }

      ArrayList var33 = new ArrayList(field1352.size() + 1);

      while (!field1352.isEmpty()) {
         HitResult var37 = (HitResult)field1352.poll();
         if (helper.method148() != CrystalAttackMode.Ignore && var37 instanceof BlockHitResult var42) {
            BlockPos var45 = var42.getBlockPos().offset(var42.getSide());
            List var49 = mc.world.getOtherEntities(null, new Box(var45), TrapUtil::lambda$getNextNActions$2);
            if (!var49.isEmpty() && var49.get(0) instanceof EndCrystalEntity var52) {
               long var56 = ((IEndCrystalEntity)var52).getLastAttackTime();
               if ((double)(System.currentTimeMillis() - var56) > Class27.getModules().field905.field1519) {
                  double var20 = Class3069.method6004(mc.player, var52.getPos());
                  if (var20 >= (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) || helper.method148() == CrystalAttackMode.Off) {
                     continue;
                  }

                  Vec3d var22 = helper.method148() == CrystalAttackMode.Sequential ? var42.getPos() : var52.getPos();
                  EntityHitResult var23 = new EntityHitResult(var52, var22);
                  var33.add(var23);
                  EndCrystalTracker.method494(var52.getPos());
                  if (helper.method148() == CrystalAttackMode.Strict) {
                     break;
                  }
               }
            }
         }

         var33.add(var37);
      }

      field1351.clear();
      return (HitResult[])var33.toArray(new HitResult[0]);
   }

   public static BlockPos[] method586(Box box, TrapMode type) {
      if (type == TrapMode.Top) {
         return new BlockPos[]{
            BlockPos.ofFloored(box.minX - 1.0, box.minY, box.minZ),
            BlockPos.ofFloored(box.minX, box.minY, box.minZ - 1.0),
            BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.minZ),
            BlockPos.ofFloored(box.maxX, box.minY, box.minZ - 1.0),
            BlockPos.ofFloored(box.minX - 1.0, box.minY, box.maxZ),
            BlockPos.ofFloored(box.minX, box.minY, box.maxZ + 1.0),
            BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.maxZ),
            BlockPos.ofFloored(box.maxX, box.minY, box.maxZ + 1.0),
            BlockPos.ofFloored(box.minX - 1.0, box.minY + 1.0, box.minZ),
            BlockPos.ofFloored(box.minX, box.minY + 1.0, box.minZ - 1.0),
            BlockPos.ofFloored(box.maxX + 1.0, box.minY + 1.0, box.minZ),
            BlockPos.ofFloored(box.maxX, box.minY + 1.0, box.minZ - 1.0),
            BlockPos.ofFloored(box.minX - 1.0, box.minY + 1.0, box.maxZ),
            BlockPos.ofFloored(box.minX, box.minY + 1.0, box.maxZ + 1.0),
            BlockPos.ofFloored(box.maxX + 1.0, box.minY + 1.0, box.maxZ),
            BlockPos.ofFloored(box.maxX, box.minY + 1.0, box.maxZ + 1.0),
            BlockPos.ofFloored(box.minX, box.minY + 2.0, box.minZ),
            BlockPos.ofFloored(box.minX, box.minY + 2.0, box.maxZ),
            BlockPos.ofFloored(box.maxX, box.minY + 2.0, box.minZ),
            BlockPos.ofFloored(box.maxX, box.minY + 2.0, box.maxZ),
            BlockPos.ofFloored(box.minX, box.minY - 1.0, box.minZ),
            BlockPos.ofFloored(box.minX, box.minY - 1.0, box.maxZ),
            BlockPos.ofFloored(box.maxX, box.minY - 1.0, box.minZ),
            BlockPos.ofFloored(box.maxX, box.minY - 1.0, box.maxZ)
         };
      } else {
         return type == TrapMode.Flat
            ? new BlockPos[]{
               BlockPos.ofFloored(box.minX - 1.0, box.minY, box.minZ),
               BlockPos.ofFloored(box.minX, box.minY, box.minZ - 1.0),
               BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.minZ),
               BlockPos.ofFloored(box.maxX, box.minY, box.minZ - 1.0),
               BlockPos.ofFloored(box.minX - 1.0, box.minY, box.maxZ),
               BlockPos.ofFloored(box.minX, box.minY, box.maxZ + 1.0),
               BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.maxZ),
               BlockPos.ofFloored(box.maxX, box.minY, box.maxZ + 1.0),
               BlockPos.ofFloored(box.minX, box.minY - 1.0, box.minZ),
               BlockPos.ofFloored(box.minX, box.minY - 1.0, box.maxZ),
               BlockPos.ofFloored(box.maxX, box.minY - 1.0, box.minZ),
               BlockPos.ofFloored(box.maxX, box.minY - 1.0, box.maxZ)
            }
            : new BlockPos[]{
               BlockPos.ofFloored(box.minX - 1.0, box.minY, box.minZ),
               BlockPos.ofFloored(box.minX, box.minY, box.minZ - 1.0),
               BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.minZ),
               BlockPos.ofFloored(box.maxX, box.minY, box.minZ - 1.0),
               BlockPos.ofFloored(box.minX - 1.0, box.minY, box.maxZ),
               BlockPos.ofFloored(box.minX, box.minY, box.maxZ + 1.0),
               BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.maxZ),
               BlockPos.ofFloored(box.maxX, box.minY, box.maxZ + 1.0),
               BlockPos.ofFloored(box.minX - 1.0, box.minY + 1.0, box.minZ),
               BlockPos.ofFloored(box.minX, box.minY + 1.0, box.minZ - 1.0),
               BlockPos.ofFloored(box.maxX + 1.0, box.minY + 1.0, box.minZ),
               BlockPos.ofFloored(box.maxX, box.minY + 1.0, box.minZ - 1.0),
               BlockPos.ofFloored(box.minX - 1.0, box.minY + 1.0, box.maxZ),
               BlockPos.ofFloored(box.minX, box.minY + 1.0, box.maxZ + 1.0),
               BlockPos.ofFloored(box.maxX + 1.0, box.minY + 1.0, box.maxZ),
               BlockPos.ofFloored(box.maxX, box.minY + 1.0, box.maxZ + 1.0),
               BlockPos.ofFloored(box.minX, box.minY - 1.0, box.minZ),
               BlockPos.ofFloored(box.minX, box.minY - 1.0, box.maxZ),
               BlockPos.ofFloored(box.maxX, box.minY - 1.0, box.minZ),
               BlockPos.ofFloored(box.maxX, box.minY - 1.0, box.maxZ)
            };
      }
   }

   public static BlockPos[] method587(Box box) {
      return new BlockPos[]{
         BlockPos.ofFloored(box.minX - 1.0, box.minY, box.minZ),
         BlockPos.ofFloored(box.minX, box.minY, box.minZ - 1.0),
         BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.minZ),
         BlockPos.ofFloored(box.maxX, box.minY, box.minZ - 1.0),
         BlockPos.ofFloored(box.minX - 1.0, box.minY, box.maxZ),
         BlockPos.ofFloored(box.minX, box.minY, box.maxZ + 1.0),
         BlockPos.ofFloored(box.maxX + 1.0, box.minY, box.maxZ),
         BlockPos.ofFloored(box.maxX, box.minY, box.maxZ + 1.0)
      };
   }

   private static boolean lambda$getNextNActions$2(Entity var0) {
      return var0 instanceof EndCrystalEntity;
   }

   private static boolean lambda$getNextNActions$1(PlayerEntity var0) {
      return !var0.equals(mc.player);
   }

   private static boolean lambda$getNextNActions$0(BlockPos var0) {
      return true;
   }
}
