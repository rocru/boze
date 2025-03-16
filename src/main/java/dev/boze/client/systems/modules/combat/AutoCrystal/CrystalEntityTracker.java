package dev.boze.client.systems.modules.combat.AutoCrystal;

import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.utils.IMinecraft;
import java.util.LinkedList;
import mapped.Class27;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;

class CrystalEntityTracker implements IMinecraft {
   private LinkedList<Runnable> field1238 = new LinkedList();

   void method2142() {
      while (!this.field1238.isEmpty()) {
         ((Runnable)this.field1238.poll()).run();
      }
   }

   void markAsDeadOnTick(Vec3d var1) {
      this.field1238.add(this::lambda$markAsDeadOnTick$0);
   }

   void method530(Vec3d var1) {
      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof EndCrystalEntity && var6.getPos().distanceTo(var1) <= 6.0 && var6 != null) {
            IEndCrystalEntity var7 = (IEndCrystalEntity)var6;
            if ((double)(System.currentTimeMillis() - var7.getLastAttackTime()) > Class27.getModules().field905.field1519) {
               var7.setHitsSinceLastAttack(1);
            } else {
               var7.setHitsSinceLastAttack(var7.getHitsSinceLastAttack() + 1);
            }

            var7.setLastAttackTime(System.currentTimeMillis());
         }
      }
   }

   private void lambda$markAsDeadOnTick$0(Vec3d var1) {
      this.method530(var1);
   }
}
