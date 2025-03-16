package dev.boze.client.mixininterfaces;

import net.minecraft.util.math.Vec3i;
import org.joml.Vector3d;

public interface IVec3d {
   void boze$set(double var1, double var3, double var5);

   void boze$set(Vec3i var1);

   void boze$set(Vector3d var1);

   void boze$setY(double var1);
}
