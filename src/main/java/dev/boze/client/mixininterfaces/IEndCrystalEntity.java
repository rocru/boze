package dev.boze.client.mixininterfaces;

public interface IEndCrystalEntity {
   long boze$getLastAttackTime();

   void boze$setLastAttackTime(long var1);

   int boze$getHitsSinceLastAttack();

   void boze$setHitsSinceLastAttack(int var1);

   long boze$getSpawnTime();

   float boze$getTicksExisted();

   void boze$setAbandoned();

   boolean boze$isAbandoned();
}
