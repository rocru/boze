package dev.boze.client.mixininterfaces;

import net.minecraft.util.math.Vec3i;
import org.joml.Vector3d;

public interface IVec3d {
   void set(double var1, double var3, double var5);

   void set(Vec3i var1);

   void set(Vector3d var1);

   void setY(double var1);
}
