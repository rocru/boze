package dev.boze.client.mixininterfaces;

import net.minecraft.util.math.Vec3d;

public interface ILivingEntity {
   long getDamageSyncTime();

   void setDamageSyncTime(long var1);

   Vec3d getLastServerPos();
}
