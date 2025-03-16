package dev.boze.client.mixininterfaces;

import net.minecraft.entity.damage.DamageSource;

public interface IOtherClientPlayerEntity {
   default boolean doDamage(DamageSource source, float amount) {
      return true;
   }

   default boolean returnDamage(DamageSource source, float amount) {
      return this.doDamage(source, amount);
   }

   default boolean shouldDamage() {
      return false;
   }

   boolean hasMoved();
}
