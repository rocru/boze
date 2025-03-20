package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.utils.IMinecraft;
import mapped.Class2811;
import mapped.Class5912;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

import java.util.HashSet;

public class CrystalSelectionHandler implements Class5912, IMinecraft {
   private final AutoCrystal field130;

   CrystalSelectionHandler(AutoCrystal var1) {
      this.field130 = var1;
   }

   @Override
   public HitResult[] method81() {
      if (!this.method2114()) {
         return null;
      } else {
         BlockPos var4 = AutoMine.INSTANCE.autoSelect.field79.method1471();
         if (!mc.world.getBlockState(var4).isAir()) {
            return null;
         } else {
            HitResult[] var5 = null;
            HitResult var6 = this.field130.field1046.method150(null, var4);
            Class2811.field101 = false;
            if (var6 != null) {
               var5 = new HitResult[]{var6};
            } else {
               for (Direction var10 : Direction.values()) {
                  if (var10.getAxis() != Axis.Y) {
                     BlockPos var11 = var4.offset(var10);
                     if (mc.world.getBlockState(var11).isAir()) {
                        if (AutoMine.INSTANCE.autoSelect.field70.getValue()) {
                           Class2811.field101 = true;
                        }

                        HitResult var12 = this.field130.field1046.method150(null, var11);
                        Class2811.field101 = false;
                        if (var12 != null) {
                           HashSet var13 = new HashSet();
                           var13.add(var11);
                           HitResult var14 = this.field130.field1046.method150(var13, var4);
                           if (var14 != null) {
                              var5 = new HitResult[]{var12, var14};
                              break;
                           }
                        } else if (!AutoMine.INSTANCE.autoSelect.field70.getValue()) {
                           BlockPos var18 = var11.down();
                           if (mc.world.getBlockState(var18).isAir()) {
                              HitResult var19 = this.field130.field1046.method150(null, var18);
                              if (var19 != null) {
                                 HashSet var15 = new HashSet();
                                 var15.add(var18);
                                 HitResult var16 = this.field130.field1046.method150(var15, var11);
                                 if (var16 != null) {
                                    var15.add(var11);
                                    HitResult var17 = this.field130.field1046.method150(var15, var4);
                                    if (var17 != null) {
                                       var5 = new HitResult[]{var19, var16, var17};
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            return var5;
         }
      }
   }

   public boolean method2114() {
      return AutoCrystal.INSTANCE.isEnabled()
         && AutoMine.INSTANCE.isEnabled()
         && AutoMine.INSTANCE.autoSelect.field60.getValue()
         && AutoMine.INSTANCE.autoSelect.field68.getValue()
         && AutoMine.INSTANCE.autoSelect.field79 != null;
   }

   @Override
   public void method1812(MouseButtonEvent event) {
   }

   @Override
   public void method1944(KeyEvent event) {
   }
}
