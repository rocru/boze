package dev.boze.client.systems.modules.render.Trails;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.systems.modules.render.Trails;
import dev.boze.client.utils.RGBAColor;
import java.util.ArrayList;
import java.util.List;
import mapped.Class3071;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3d;

class nx {
   private final List<Vector3d> field3774;
   private final List<Long> field3775;
   private int field3776;
   final Trails field3777;

   private nx(Trails var1) {
      this.field3777 = var1;
      this.field3774 = new ArrayList();
      this.field3775 = new ArrayList();
      this.field3776 = -1;
   }

   public void method2050() {
      for (Vector3d var5 : this.field3774) {
         this.field3777.field3771.method5994(var5);
      }

      this.field3774.clear();
      this.field3775.clear();
      this.field3776 = -1;
   }

   private void method2051(Vector3d var1) {
      this.field3774.add(var1);
      this.field3775.add(System.currentTimeMillis());
   }

   public void method2052(Render3DEvent event, RGBAColor origin, RGBAColor hit) {
      if (this.field3774.size() != 0) {
         for (int var7 = 1; var7 < this.field3774.size(); var7++) {
            float var8 = 0.0F;
            float var9 = 0.0F;
            if (this.field3776 != -1) {
               if (this.field3775.size() > var7) {
                  if ((float)(System.currentTimeMillis() - (Long)this.field3775.get(var7 - 1)) / (this.field3777.field3767.method423() * 1000.0F) > 1.0F) {
                     this.field3774.remove(var7 - 1);
                     this.field3775.remove(var7 - 1);
                     continue;
                  }

                  var8 = MathHelper.clamp(
                     (float)(System.currentTimeMillis() - (Long)this.field3775.get(var7 - 1)) / (this.field3777.field3767.method423() * 1000.0F), 0.0F, 1.0F
                  );
                  var9 = MathHelper.clamp(
                     (float)(System.currentTimeMillis() - (Long)this.field3775.get(var7)) / (this.field3777.field3767.method423() * 1000.0F), 0.0F, 1.0F
                  );
               }
            } else {
               var8 = (float)(var7 - 1) / (float)this.field3774.size();
               var9 = (float)var7 / (float)this.field3774.size();
            }

            RGBAColor var10 = Class3071.method6016(origin, hit, (double)var8);
            RGBAColor var11 = Class3071.method6016(origin, hit, (double)var9);
            this.field3777
               .field3773
               .method1233(
                  ((Vector3d)this.field3774.get(var7 - 1)).x,
                  ((Vector3d)this.field3774.get(var7 - 1)).y,
                  ((Vector3d)this.field3774.get(var7 - 1)).z,
                  ((Vector3d)this.field3774.get(var7)).x,
                  ((Vector3d)this.field3774.get(var7)).y,
                  ((Vector3d)this.field3774.get(var7)).z,
                  var10,
                  var11
               );
         }
      }
   }
}
