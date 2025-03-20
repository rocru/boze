package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.ac.Anticheat;
import dev.boze.client.ac.Grim;
import dev.boze.client.ac.NCP;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AttackMode;
import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.misc.AirPlace;
import dev.boze.client.utils.*;
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class2811;
import mapped.Class2923;
import mapped.Class5912;
import net.minecraft.block.BlockState;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashSet;

class CrystalHitter implements IMinecraft {
   private final AutoCrystal field1093;
   private final SwapHandler field1094;
   private final Class5912[] field1095;
   Vec3d field1096;
   HitResult[] field1097 = null;

   CrystalHitter(AutoCrystal var1) {
      this.field1093 = var1;
      this.field1094 = new SwapHandler(var1, 25);
      this.field1095 = new Class5912[]{var1.field1044, var1.autoCrystalBasePlace};
   }

   Vec3d method1954() {
      return this.field1096;
   }

   void method2142() {
      if (Class2923.method2114()
         || mc.player.getInventory().getMainHandStack().getItem() instanceof EndCrystalItem
         || this.field1093.field1041.field213.method461() != AutoMineSwapMode.Off
            && InventoryHelper.method165(Class2923.field126, this.field1093.field1041.field213.method461()) != -1) {
         HitResult[] var4 = null;

         for (Class5912 var8 : this.field1095) {
            if ((var4 = var8.method81()) != null) {
               break;
            }
         }

         this.field1097 = var4;
         if (this.field1097 != null && this.field1097.length > 0) {
            this.field1096 = this.field1097[0].getPos();
         } else {
            this.field1096 = null;
         }
      }
   }

   boolean method109(RotationEvent var1) {
      if (this.field1097 == null) {
         return false;
      } else {
         for (int var5 = 0; var5 < this.field1097.length; var5++) {
            HitResult var6 = this.field1097[var5];
            int var7 = InventoryHelper.method174(BlastResistanceCalculator.field3907, this.method149());
            if (var7 == -1) {
               this.method1416();
               break;
            }

            BlockHitResult var8 = (BlockHitResult)var6;
            if (!this.field1094.method723(this.method149(), var7)) {
               this.method1416();
               break;
            }

            if (var5 > 0 && this.field1093.field1041.method2115()) {
               if (this.method2114()) {
                  this.field1094.method1416();
                  this.method1416();
                  return true;
               }

               float[] var9 = EntityUtil.method2146(var6.getPos());
               ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var9[0], var9[1]);
            }

            if (this.field1093.field1041.field205.method461() == AnticheatMode.Grim && mc.world.getBlockState(var8.getBlockPos()).isAir()) {
               AirPlace.method393(var8, var7 == -2);
            } else {
               var1.method557(AutoCrystal.INSTANCE, AttackMode.Packet, var8, var7 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
            }
         }

         this.field1094.method1416();
         this.method1416();
         return true;
      }
   }

   private void method1416() {
      this.field1097 = null;
      this.field1096 = null;
   }

   void method1812(MouseButtonEvent var1) {
      for (Class5912 var8 : this.field1095) {
         var8.method1812(var1);
      }
   }

   void method1944(KeyEvent var1) {
      for (Class5912 var8 : this.field1095) {
         var8.method1944(var1);
      }
   }

   HitResult method150(HashSet<BlockPos> var1, BlockPos var2) {
      NCP var6 = NCP.field1836;
      ArrayList<Direction> var7 = new ArrayList<>(6);
      this.method513(var1, var2, var7, false, var6);
      if (var7.isEmpty() && Class2811.field101) {
         this.method513(var1, var2, var7, true, var6);
      }

      ArrayList<BlockHitResult> var8 = new ArrayList<>(var7.size());
      if (this.field1093.field1041.field205.method461() == AnticheatMode.NCP) {
         var6.method941(
                 this.field1093.autoCrystalPlace.field138.method423(),
                 this.field1093.autoCrystalPlace.field139.method423()
         );
      } else {
         var6.method941(
                 this.field1093.autoCrystalPlace.field138.method423(),
                 this.field1093.autoCrystalPlace.field138.method423()
         );
      }

      for (Direction var10 : var7) {
         BlockPos var11 = var2.offset(var10);
         boolean var12 = mc.world.getBlockState(var11).isAir();
         BlockHitResult var13 = var6.method566(var12 ? var2 : var11, var10.getOpposite());
         if (var13 != null) {
            var8.add(var13);
         }
      }

      var6.method942();
      return var6.method565(var8);
   }

   private void method513(HashSet<BlockPos> var1, BlockPos var2, ArrayList<Direction> var3, boolean var4, NCP var5) {
      for (Direction var12 : Direction.values()) {
         BlockPos var13 = var2.offset(var12);
         if (this.method514(var1, var13, var4)
            && var5.method567(var13, var12.getOpposite(), this.field1093.field1041.field210.method419(), this.field1093.field1041.field210.method419())) {
            var3.add(var12);
         }
      }
   }

   public boolean method514(HashSet<BlockPos> context, BlockPos pos, boolean airPlace) {
      BlockState var7 = mc.world.getBlockState(pos);
      if (context != null && context.contains(pos)) {
         return true;
      } else {
         return EntityTracker.field3914.containsKey(pos) || airPlace && var7.isAir() || Class2811.field102 && !var7.getFluidState().isEmpty() || !var7.isAir() && var7.getFluidState().isEmpty();
      }
   }

   private Anticheat method515() {
      return this.field1093.field1041.field205.method461() == AnticheatMode.NCP ? NCP.field1836 : Grim.field1831;
   }

   private SwapMode method149() {
      return this.field1093.field1041.field213.method461() == AutoMineSwapMode.Off ? SwapMode.Silent : this.field1093.field1041.field213.method461().swapMode;
   }

   private boolean method2114() {
      if (!this.field1093.field1041.method2115()) {
         return false;
      } else if (mc.options.jumpKey.isPressed()) {
         return true;
      } else if (mc.options.forwardKey.isPressed()) {
         return true;
      } else if (mc.options.backKey.isPressed()) {
         return true;
      } else {
         return mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed();
      }
   }
}
