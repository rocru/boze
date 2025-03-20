package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.enums.AutoCrystalAction;
import dev.boze.client.enums.AutoCrystalMaxDamage;
import dev.boze.client.enums.BasePlace;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.Timer;
import mapped.Class2811;
import mapped.Class3086;
import mapped.Class5912;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AutoCrystalBasePlace implements IMinecraft, Class5912, SettingsGroup {
   final EnumSetting<BasePlace> field122 = new EnumSetting<BasePlace>(
      "BasePlace",
      BasePlace.Bind,
      "Place obsidian\nOnly works when no valid place positions found\n - Off: Don't place obsidian\n - Bind: Place Obsidian on bind press\n - Auto: Automatically place obsidian - not recommended\n"
   );
   private final BindSetting field123 = new BindSetting("PlaceBind", Bind.create(), "Bind to place obsidian", this::lambda$new$0, this.field122);
   private final BooleanSetting field124 = new BooleanSetting("AirPlace", false, "Air place obsidian", this.field122);
   private final Setting<?>[] field125;
   private final AutoCrystal field126;
   private Class3086 field127 = null;
   private Timer field128 = new Timer();
   private final Timer field129 = new Timer();

   private static void method1800(String var0) {
      if (AutoCrystal.field1038 && mc.player != null) {
         System.out.println("[AutoCrystal.BasePlace @" + mc.player.age + "] " + var0);
      }
   }

   AutoCrystalBasePlace(AutoCrystal var1) {
      this.field126 = var1;
      this.field125 = new Setting[]{this.field122, this.field123, this.field124};
   }

   @Override
   public Setting<?>[] get() {
      return this.field125;
   }

   @Override
   public void method1812(MouseButtonEvent event) {
      if (this.field123.method476().matches(false, event.button)) {
         this.field129.reset();
      }
   }

   @Override
   public void method1944(KeyEvent event) {
      if (this.field123.method476().matches(true, event.key)) {
         this.field129.reset();
      }
   }

   @Override
   public HitResult[] method81() {
      if (!this.method2115() && this.method2114()) {
         if (this.field124.getValue()) {
            Class2811.field101 = true;
         }

         Class3086 var4 = null;
         if (this.field127 != null
            && !this.field128.hasElapsed(500.0)
            && this.method2101(this.field127.field208)
            && (var4 = this.method82(this.field127.field208)) != null) {
            Class2811.field101 = false;
            method1800("Algorithm found best base place position " + var4.field208.toShortString());
            this.field127 = var4;
            return new HitResult[]{var4.field209};
         } else {
            for (BlockPos var7 : this.method1144()) {
               Class3086 var8 = this.method82(var7);
               if (var8 != null && (var4 == null || var4.field210 < var8.field210)) {
                  var4 = var8;
               }
            }

            Class2811.field101 = false;
            if (var4 != null) {
               method1800("Algorithm found best base place position " + var4.field208.toShortString());
               this.field127 = var4;
               this.field128.reset();
               return new HitResult[]{var4.field209};
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   private Class3086 method82(BlockPos var1) {
      BlockPos var5 = null;
      HitResult var6 = null;
      double var7 = 0.0;
      HitResult var9 = this.field126.field1046.method150(null, var1);
      if (var9 == null) {
         return null;
      } else {
         Vec3d var10 = new Vec3d((double)var1.getX() + 0.5, (double)var1.getY() + 1.0, (double)var1.getZ() + 0.5);
         double var11 = this.field126.field1047.method5665(mc.player, AutoCrystalAction.Full, var10, var1, true);
         if (!this.field126.field1045.method727(var11)) {
            return null;
         } else {
            for (LivingEntity var14 : this.field126.field1042.method1144()) {
               if (var14 != null) {
                  double var15 = 0.0;
                  double var17 = this.field126.field1047.method5665(var14, AutoCrystalAction.Full, var10, var1, true);
                  double var19 = this.field126.field1042.method76(var14, var17);
                  if (var17 > var15 && var17 >= var19) {
                     var15 = var17;
                  }

                  if (!this.field126.field1042.method65(var11, var15)) {
                     return null;
                  }

                  if (var15 > var7
                     && (
                        !this.field126.field1042.field118.getValue()
                           || this.field126.field1042.field115.getValue() == AutoCrystalMaxDamage.Balance
                           || !(var11 > (double)this.field126.field1042.field116.getValue().floatValue())
                           || !(var15 < (double)(var14.getHealth() + var14.getAbsorptionAmount()))
                     )) {
                     var5 = var1;
                     var6 = var9;
                     var7 = var15;
                  }
               }
            }

            return var5 != null ? new Class3086(var5, var6, var7) : null;
         }
      }
   }

   private boolean method2114() {
      if (this.field126.field1044.method2114()) {
         return false;
      } else if (this.field122.getValue() == BasePlace.Auto) {
         return true;
      } else if (this.field122.getValue() == BasePlace.Bind && !this.field129.hasElapsed(250.0)) {
         this.field129.setLastTime(0L);
         return true;
      } else {
         return false;
      }
   }

   private List<BlockPos> method1144() {
      ArrayList var4 = new ArrayList();
      BlockPos var5 = mc.player.getBlockPos();
      int var6 = (int)Math.ceil((double)(this.field126.autoCrystalPlace.field138.getValue() + 1.0F));
      int var7 = (int)Math.ceil((double)(this.field126.autoCrystalPlace.field138.getValue() + 1.0F));

      for (int var8 = var5.getX() - var6; var8 < var5.getX() + var6; var8++) {
         for (int var9 = var5.getY() - var7; var9 < var5.getY() + var7 + 1; var9++) {
            for (int var10 = var5.getZ() - var6; var10 < var5.getZ() + var6; var10++) {
               Vec3d var11 = new Vec3d((double)var8 + 0.5, (double)var9 + 0.5, (double)var10 + 0.5);
               boolean var12 = false;

               for (LivingEntity var14 : this.field126.field1042.method1144()) {
                  if (var14 != null && var14.getPos().distanceTo(var11) <= (double)this.field126.crystalRange.getValue().floatValue() + 0.71) {
                     var12 = true;
                     break;
                  }
               }

               if (var12) {
                  BlockPos var15 = new BlockPos(var8, var9, var10);
                  if (this.method2101(var15)) {
                     var4.add(var15);
                  }
               }
            }
         }
      }

      return var4;
   }

   boolean method2101(BlockPos var1) {
      boolean var5 = var1.getY() == mc.world.getTopY() - 1;
      BlockPos var6 = var1.up();
      if (!var5 && !this.field126.field1045.method2102(var6)) {
         return false;
      } else {
         BlockPos var7 = var1.up(2);
         if (!var5 && this.field126.protocol.getValue() && !this.field126.field1045.method2102(var7)) {
            return false;
         } else {
            double var8 = this.field126.protocol.getValue() ? 2.0 : 1.0;
            Box var10 = new Box(var1).stretch(0.0, var8, 0.0);
            if (this.field126.field1045.method726(var10)) {
               return false;
            } else {
               Vec3d var11 = this.field126.ac.method510(mc.player, AutoCrystalAction.Full);
               Vec3d var12 = var11.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
               Vec3d var13 = this.field126.field1045.method725(var1);
               Vec3d var14 = new Vec3d((double)var1.getX() + 0.5, (double)var1.getY() + 1.0, (double)var1.getZ() + 0.5);
               float[] var15 = EntityUtil.method2147(var12, var13);
               return this.field126.field1041.method119(var14, var11, var15);
            }
         }
      }
   }

   private boolean method2115() {
      return this.field126.field1042.method1144().isEmpty();
   }

   private boolean lambda$new$0() {
      return this.field122.getValue() == BasePlace.Bind;
   }
}
