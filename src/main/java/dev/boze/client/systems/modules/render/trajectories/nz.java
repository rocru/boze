package dev.boze.client.systems.modules.render.trajectories;

import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.jumptable.ny;
import dev.boze.client.systems.modules.render.Trajectories;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.player.RotationHelper;
import java.util.ArrayList;
import java.util.List;
import mapped.Class3062;
import mapped.Class3071;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3d;

public class nz {
   private final List<Vector3d> field3807;
   private final List<Long> field3808;
   private final Vector3d field3809;
   private final Timer field3810;
   private BlockHitResult field3811;
   private Entity field3812;
   private int field3813;
   final Trajectories field3814;

   public nz(final Trajectories this$0) {
      this.field3814 = this$0;
      this.field3807 = new ArrayList();
      this.field3808 = new ArrayList();
      this.field3809 = new Vector3d();
      this.field3810 = new Timer();
      this.field3813 = -1;
   }

   public void method2061() {
      for (Vector3d var5 : this.field3807) {
         this.field3814.field3806.method5994(var5);
      }

      this.field3807.clear();
      this.field3808.clear();
      this.field3811 = null;
      this.field3812 = null;
      this.field3813 = -1;
   }

   public void method2062(boolean shouldOffset) {
      this.method2063();
      Vector3d var5 = this.field3814.field3806.method5993().set(this.field3814.field3805.field13);
      if (shouldOffset) {
         Class3062.method5989(this.field3809, RotationHelper.field3956.subtract(var5.x, var5.y, var5.z));
      } else {
         this.field3809.set(0.0, 0.0, 0.0);
      }

      this.method2064(var5);
      long var6 = System.nanoTime();

      while (System.nanoTime() - var6 < 1000000L) {
         HitResult var8 = this.field3814.field3805.method49();
         if (var8 != null) {
            this.method2065(var8);
            break;
         }

         this.method2063();
      }
   }

   private void method2063() {
      this.field3807.add(this.field3814.field3806.method5993().set(this.field3814.field3805.field13));
   }

   private void method2064(Vector3d var1) {
      this.field3807.add(var1);
      if (this.field3814.field3792.method419()) {
         this.field3808.add(System.currentTimeMillis());
      }
   }

   private void method2065(HitResult var1) {
      if (var1.getType() == Type.BLOCK) {
         this.field3811 = (BlockHitResult)var1;
         this.field3807.add(Class3062.method5989(this.field3814.field3806.method5993(), var1.getPos()));
      } else if (var1.getType() == Type.ENTITY) {
         this.field3812 = ((EntityHitResult)var1).getEntity();
         this.field3807
            .add(Class3062.method5989(this.field3814.field3806.method5993(), var1.getPos()).add(0.0, (double)(this.field3812.getHeight() / 2.0F), 0.0));
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public void method2066(Render3DEvent event, RGBAColor origin, RGBAColor hit) {
      if (this.field3807.size() != 0) {
         for (int var7 = 1; var7 < this.field3807.size(); var7++) {
            float var8 = 0.0F;
            float var9 = 0.0F;
            if (this.field3813 != -1) {
               if (this.field3814.field3792.method419() && this.field3808.size() > var7) {
                  if ((float)(System.currentTimeMillis() - (Long)this.field3808.get(var7 - 1)) / (this.field3814.field3791.method423() * 1000.0F) > 1.0F) {
                     this.field3807.remove(var7 - 1);
                     this.field3808.remove(var7 - 1);
                     continue;
                  }

                  var8 = MathHelper.clamp(
                     (float)(System.currentTimeMillis() - (Long)this.field3808.get(var7 - 1)) / (this.field3814.field3791.method423() * 1000.0F), 0.0F, 1.0F
                  );
                  var9 = MathHelper.clamp(
                     (float)(System.currentTimeMillis() - (Long)this.field3808.get(var7)) / (this.field3814.field3791.method423() * 1000.0F), 0.0F, 1.0F
                  );
               }
            } else {
               var8 = (float)(var7 - 1) / (float)this.field3807.size();
               var9 = (float)var7 / (float)this.field3807.size();
            }

            RGBAColor var10 = Class3071.method6016(origin, hit, (double)var8);
            RGBAColor var11 = Class3071.method6016(origin, hit, (double)var9);
            if (this.field3809.length() > 0.0) {
               double var12 = Class3071.method6022(
                  ((Vector3d)this.field3807.get(var7 - 1)).x + this.field3809.x, ((Vector3d)this.field3807.get(var7 - 1)).x, (double)var8
               );
               double var14 = Class3071.method6022(
                  ((Vector3d)this.field3807.get(var7)).x + this.field3809.x, ((Vector3d)this.field3807.get(var7)).x, (double)var9
               );
               double var16 = Class3071.method6022(
                  ((Vector3d)this.field3807.get(var7 - 1)).y + this.field3809.y, ((Vector3d)this.field3807.get(var7 - 1)).y, (double)var8
               );
               double var18 = Class3071.method6022(
                  ((Vector3d)this.field3807.get(var7)).y + this.field3809.y, ((Vector3d)this.field3807.get(var7)).y, (double)var9
               );
               double var20 = Class3071.method6022(
                  ((Vector3d)this.field3807.get(var7 - 1)).z + this.field3809.z, ((Vector3d)this.field3807.get(var7 - 1)).z, (double)var8
               );
               double var22 = Class3071.method6022(
                  ((Vector3d)this.field3807.get(var7)).z + this.field3809.z, ((Vector3d)this.field3807.get(var7)).z, (double)var9
               );
               this.field3814.ac.method1233(var12, var16, var20, var14, var18, var22, var10, var11);
            } else {
               this.field3814
                  .ac
                  .method1233(
                     ((Vector3d)this.field3807.get(var7 - 1)).x,
                     ((Vector3d)this.field3807.get(var7 - 1)).y,
                     ((Vector3d)this.field3807.get(var7 - 1)).z,
                     ((Vector3d)this.field3807.get(var7)).x,
                     ((Vector3d)this.field3807.get(var7)).y,
                     ((Vector3d)this.field3807.get(var7)).z,
                     var10,
                     var11
                  );
            }
         }

         if (this.field3811 != null) {
            Box var24 = new Box(this.field3811.getBlockPos());
            switch (ny.field2126[this.field3811.getSide().ordinal()]) {
               case 1:
                  var24 = new Box(var24.minX, var24.minY, var24.minZ, var24.maxX, var24.minY, var24.maxZ);
                  break;
               case 2:
                  var24 = new Box(var24.minX, var24.maxY, var24.minZ, var24.maxX, var24.maxY, var24.maxZ);
                  break;
               case 3:
                  var24 = new Box(var24.minX, var24.minY, var24.minZ, var24.maxX, var24.maxY, var24.minZ);
                  break;
               case 4:
                  var24 = new Box(var24.minX, var24.minY, var24.maxZ, var24.maxX, var24.maxY, var24.maxZ);
                  break;
               case 5:
                  var24 = new Box(var24.maxX, var24.minY, var24.minZ, var24.maxX, var24.maxY, var24.maxZ);
                  break;
               case 6:
                  var24 = new Box(var24.minX, var24.minY, var24.minZ, var24.minX, var24.maxY, var24.maxZ);
            }

            this.field3814.ac.method1273(var24, this.field3814.field3783.method1362(), this.field3814.field3784.method1362(), ShapeMode.Full, 0);
         }

         if (this.field3812 != null) {
            double var25 = (this.field3812.getX() - this.field3812.prevX) * (double)event.field1951;
            double var26 = (this.field3812.getY() - this.field3812.prevY) * (double)event.field1951;
            double var27 = (this.field3812.getZ() - this.field3812.prevZ) * (double)event.field1951;
            Box var13 = this.field3812.getBoundingBox();
            this.field3814
               .ac
               .method1271(
                  var25 + var13.minX,
                  var26 + var13.minY,
                  var27 + var13.minZ,
                  var25 + var13.maxX,
                  var26 + var13.maxY,
                  var27 + var13.maxZ,
                  this.field3814.field3783.method1362(),
                  this.field3814.field3784.method1362(),
                  ShapeMode.Full,
                  0
               );
         }
      }
   }
}
