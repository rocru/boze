package dev.boze.client.utils;

import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import mapped.Class27;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;

public class EndCrystalTracker implements IMinecraft {
   public static void method494(Vec3d center) {
      for (Entity var5 : mc.world.getEntities()) {
         if (var5 instanceof EndCrystalEntity && var5.getPos().distanceTo(center) <= 6.0 && var5 != null) {
            IEndCrystalEntity var6 = (IEndCrystalEntity)var5;
            if ((double)(System.currentTimeMillis() - var6.getLastAttackTime()) > Class27.getModules().field905.field1519) {
               var6.setHitsSinceLastAttack(1);
            } else {
               var6.setHitsSinceLastAttack(var6.getHitsSinceLastAttack() + 1);
            }

            var6.setLastAttackTime(System.currentTimeMillis());
         }
      }
   }
}
