package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EndCrystalEntity.class})
public class EndCrystalEntityMixin implements IEndCrystalEntity {
   @Unique
   private long lastAttackTime = 0L;
   @Unique
   private int hitsSinceLastAttack = 0;
   @Unique
   private long spawnTime;
   @Unique
   private boolean abandoned;

   @Override
   public long boze$getLastAttackTime() {
      return this.lastAttackTime;
   }

   @Override
   public void boze$setLastAttackTime(long lastAttackTime) {
      this.lastAttackTime = lastAttackTime;
   }

   @Override
   public int boze$getHitsSinceLastAttack() {
      return this.hitsSinceLastAttack;
   }

   @Override
   public void boze$setHitsSinceLastAttack(int hitsSinceLastAttack) {
      this.hitsSinceLastAttack = hitsSinceLastAttack;
   }

   @Override
   public long boze$getSpawnTime() {
      return this.spawnTime;
   }

   @Override
   public float boze$getTicksExisted() {
      return (float)(System.currentTimeMillis() - this.spawnTime) / 50.0F;
   }

   @Override
   public void boze$setAbandoned() {
      this.abandoned = true;
   }

   @Override
   public boolean boze$isAbandoned() {
      return this.abandoned;
   }

   @Inject(
      method = {"<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V"},
      at = {@At("TAIL")}
   )
   private void onInit(EntityType<? extends EndCrystalEntity> var1, World var2, CallbackInfo var3) {
      this.spawnTime = System.currentTimeMillis();
   }
}
