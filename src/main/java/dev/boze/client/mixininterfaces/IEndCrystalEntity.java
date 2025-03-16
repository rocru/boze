package dev.boze.client.mixininterfaces;

public interface IEndCrystalEntity {
   long getLastAttackTime();

   void setLastAttackTime(long var1);

   int getHitsSinceLastAttack();

   void setHitsSinceLastAttack(int var1);

   long getSpawnTime();

   float getTicksExisted();

   void setAbandoned();

   boolean isAbandoned();
}
