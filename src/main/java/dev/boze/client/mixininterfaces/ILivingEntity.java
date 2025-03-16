package dev.boze.client.mixininterfaces;

import net.minecraft.util.math.Vec3d;

public interface ILivingEntity {
   long boze$getDamageSyncTime();

   void boze$setDamageSyncTime(long var1);

   Vec3d boze$getLastServerPos();
}
