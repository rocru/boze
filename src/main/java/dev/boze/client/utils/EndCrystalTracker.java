package dev.boze.client.utils;

import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.Boze;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;

public class EndCrystalTracker implements IMinecraft {
   public static void method494(Vec3d center) {
      for (Entity var5 : mc.world.getEntities()) {
         if (var5 instanceof EndCrystalEntity && var5.getPos().distanceTo(center) <= 6.0 && var5 != null) {
            IEndCrystalEntity var6 = (IEndCrystalEntity)var5;
            if ((double)(System.currentTimeMillis() - var6.boze$getLastAttackTime()) > Boze.getModules().field905.field1519) {
               var6.boze$setHitsSinceLastAttack(1);
            } else {
               var6.boze$setHitsSinceLastAttack(var6.boze$getHitsSinceLastAttack() + 1);
            }

            var6.boze$setLastAttackTime(System.currentTimeMillis());
         }
      }
   }
}
