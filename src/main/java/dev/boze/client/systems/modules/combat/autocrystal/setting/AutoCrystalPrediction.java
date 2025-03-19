package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.enums.AutoCrystalAction;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.misc.FakePlayer;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import mapped.Class5918;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Map.Entry;

public class AutoCrystalPrediction implements IMinecraft {
   private final AutoCrystal field1091;
   private static double field1092 = 0.0784;

   private static void method1800(String var0) {
      if (AutoCrystal.field1038 && mc.player != null) {
         System.out.println("[AutoCrystal.Prediction @" + mc.player.age + "] " + var0);
      }
   }

   AutoCrystalPrediction(AutoCrystal var1) {
      this.field1091 = var1;
   }

   void method2071(Render3DEvent var1) {
      if (AutoCrystal.field1038) {
         for (Entry var6 : AutoCrystalAction.Full.field1776.entrySet()) {
            var1.field1950.method1234(((LivingEntity)var6.getKey()).getPos(), (Vec3d)var6.getValue(), RGBAColor.field405);
         }
      }
   }

   public Vec3d method510(LivingEntity var1, AutoCrystalAction var2) {
      return var1 instanceof PlayerEntity && var2.method886(var1) != 0 ? var2.method887((PlayerEntity)var1) : var1.getPos();
   }

   public Box method511(LivingEntity var1, AutoCrystalAction var2) {
      return var1 instanceof PlayerEntity && var2.method886(var1) != 0
         ? var2.method888((PlayerEntity)var1)
         : var1.getBoundingBox(var1.getPose()).offset(var1.getPos());
   }

   void method2142() {
      for (AutoCrystalAction var7 : AutoCrystalAction.values()) {
         var7.field1776.clear();
         var7.field1777.clear();
      }
   }

   void method1416() {
      if (this.field1091.dontPredict.method419()) {
         AutoCrystalAction.Place.ticks = 0;
         AutoCrystalAction.Place.field1775 = 0;
         AutoCrystalAction.Full.ticks = 0;
         AutoCrystalAction.Full.field1775 = 0;
         AutoCrystalAction.Break.ticks = 0;
         AutoCrystalAction.Break.field1775 = 0;
      }

      float var4 = this.field1091.autoCrystalTracker.method1385();
      float var5 = this.field1091.autoCrystalTracker.method215();
      float var6 = var4 + var5;
      int var7 = Math.round(var4 / 50.0F);
      int var8 = Math.round(var5 / 50.0F);
      int var9 = Math.round(var6 / 50.0F);
      AutoCrystalAction.Place.ticks = var8;
      AutoCrystalAction.Place.field1775 = var8;
      AutoCrystalAction.Full.ticks = var9;
      AutoCrystalAction.Full.field1775 = var8;
      AutoCrystalAction.Break.ticks = var7;
      AutoCrystalAction.Break.field1775 = 0;
      if (AutoCrystal.field1038 && FakePlayer.INSTANCE.isEnabled() && FakePlayer.INSTANCE.fakePlayer != null) {
         for (AutoCrystalAction var13 : AutoCrystalAction.values()) {
            var13.method887(FakePlayer.INSTANCE.fakePlayer);
         }
      }
   }

   private static Vec3d method512(PlayerEntity var0, int var1) {
      if (var0.prevX == var0.getX() && var0.prevY == var0.getY() && var0.prevZ == var0.getZ()) {
         return new Vec3d(var0.getX(), var0.getY(), var0.getZ());
      } else {
         Pair var5 = Class5918.method38(var1, var0);
         if (var5 != null) {
            return ((ClientPlayerEntity)var5.getLeft()).getPos();
         } else {
            double var6 = var0.getX() - var0.prevX;
            double var8 = var0.getY() - var0.prevY;
            double var10 = var0.getZ() - var0.prevZ;
            double var12 = 0.0;
            double var14 = 0.0;
            double var16 = 0.0;

            for (int var18 = 0; var18 < var1; var18++) {
               double var19 = var12 + var6;
               double var21 = var14 + var8;
               double var23 = var16 + var10;
               if (!mc.world.isSpaceEmpty(var0, var0.getBoundingBox().offset(var19, var21, var23))) {
                  if (mc.world.isSpaceEmpty(var0, var0.getBoundingBox().offset(0.0, var21, 0.0))) {
                     var19 = var12;
                     var23 = var16;
                  } else if (mc.world.isSpaceEmpty(var0, var0.getBoundingBox().offset(var19, 0.0, var23))) {
                     var21 = var14;
                  } else {
                     var19 = var12;
                     var21 = var14;
                     var23 = var16;
                  }
               }

               var12 = var19;
               var14 = var21;
               var16 = var23;
               var8 -= field1092;
            }

            return new Vec3d(var0.getX() + var12, var0.getY() + var14, var0.getZ() + var16);
         }
      }
   }
}
