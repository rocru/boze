package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.enums.AutoCrystalAction;
import dev.boze.client.enums.CrystalAuraUpdateMode;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

class CrystalProcessor implements IMinecraft {
   private final AutoCrystal field1365;

   CrystalProcessor(AutoCrystal var1) {
      this.field1365 = var1;
   }

   void method489(BlockPos var1, Vec3d var2, int var3) {
      if (this.method2101(var1)) {
         if (!this.method2102(var1)) {
            if (!this.method2114()) {
               if (this.method615(var2, var1)) {
                  if (this.field1365.autoCrystalBreak.method94(var2, var3, null)) {
                     this.field1365.autoCrystalBreak.timer.reset();
                     this.field1365.autoCrystalTracker.field1531 = true;
                     this.field1365.aa.markAsDeadOnTick(var2);
                     if (this.field1365.field1041.field212.method461() == CrystalAuraUpdateMode.Packet) {
                        try {
                           if (this.field1365.autoCrystalPlace.method2114()) {
                              this.field1365.autoCrystalPlace.method1198();
                           }
                        } catch (Exception var8) {
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean method2101(BlockPos var1) {
      return this.field1365.field1041.field212.method461() != CrystalAuraUpdateMode.Tick
         && this.field1365.autoCrystalTracker.field1534 != null
         && this.field1365.autoCrystalTracker.field1534.method6061().equals(var1)
         && this.field1365.autoCrystalBreak.timer.hasElapsed((double)(this.field1365.autoCrystalBreak.field180.method423() * 50.0F));
   }

   private boolean method2102(BlockPos var1) {
      return this.field1365.field1042.field114.method419()
         && this.field1365.autoCrystalTracker.field1534 != null
         && this.field1365.autoCrystalTracker.field1534.method6061().equals(var1)
         && this.field1365.autoCrystalTracker.field1536 < this.field1365.autoCrystalTracker.field1537;
   }

   private boolean method2114() {
      return this.field1365.ticksExisted.method423() > 0.0F
         || this.field1365.autoCrystalBreak.method2116()
         || mc.player.hasStatusEffect(StatusEffects.WEAKNESS);
   }

   private boolean method615(Vec3d var1, BlockPos var2) {
      try {
         return this.field1365.field1041.method116(var1)
            && this.field1365.field1047.method5665(mc.player, AutoCrystalAction.Break, var1, var2, true) + 2.0
               < (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount());
      } catch (Exception var7) {
         return false;
      }
   }
}
