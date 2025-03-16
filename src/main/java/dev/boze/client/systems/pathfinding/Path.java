package dev.boze.client.systems.pathfinding;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.Render3DEvent;
import java.util.ArrayList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Path {
   public final Vec3d field3875;
   public final ArrayList<Vec3d> field3876;
   public final ArrayList<Vec3d> field3877;
   public int field3878 = 0;

   public Path(Vec3d start) {
      this.field3875 = start;
      this.field3876 = new ArrayList();
      this.field3877 = new ArrayList();
   }

   private void method2092(Vec3d var1) {
      this.field3876.add(var1);
   }

   private void method2093(double var1, double var3) {
      for (Vec3d var9 : this.field3876) {
         double var10 = 0.0;
         if (!this.field3877.isEmpty()) {
            Vec3d var12 = (Vec3d)this.field3877.get(this.field3877.size() - 1);
            var10 -= var12.length();
         }

         Vec3d var21 = var9;
         Vec3d var13 = var9.normalize();
         boolean var14 = false;

         while (var21.length() > 0.0) {
            if (!var14 && var10 < var3) {
               var10 += var1;
            }

            var10 = MathHelper.clamp(var10, 0.0, var3);
            Vec3d var15 = var13.multiply(var10);
            if (var15.length() >= var21.length()) {
               var15 = var21;
            }

            if (var10 > var1) {
               double var16 = var10;

               double var18;
               for (var18 = 0.0; var16 > 0.0; var16 -= var1) {
                  var18 += var16;
               }

               var14 = var18 > var21.length();
               if (var14) {
                  double var20 = var10 - var1;
                  var10 = Math.max(var20, var1);
                  var15 = var13.multiply(var10);
                  if (var15.length() >= var21.length()) {
                     var15 = var21;
                  }
               }
            }

            this.field3877.add(var15);
            var21 = var21.subtract(var15);
         }
      }
   }

   public Vec3d method2094() {
      return this.field3878 >= this.field3877.size() ? null : (Vec3d)this.field3877.get(this.field3878++);
   }

   public Vec3d method2095() {
      return this.field3878 >= this.field3877.size() ? null : (Vec3d)this.field3877.get(this.field3878 + 1);
   }

   public boolean method2096() {
      return this.field3878 < this.field3877.size() - 1;
   }

   public void method2097(Render3DEvent event, BozeDrawColor color) {
      Vec3d var6 = this.field3875;

      for (Vec3d var8 : this.field3877) {
         Vec3d var9 = var6.add(var8);
         event.field1950.method1235(var6, var9, color);
         var6 = var9;
      }
   }
}
