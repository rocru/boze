package dev.boze.client.systems.modules.render.spawnesp;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.systems.modules.render.SpawnESP;
import net.minecraft.util.math.BlockPos;

class BlockRenderer {
   private int field3694;
   private int field3695;
   private int field3696;
   private boolean field3697;
   final SpawnESP field3698;

   private BlockRenderer(SpawnESP var1) {
      this.field3698 = var1;
   }

   public BlockRenderer method2028(BlockPos blockPos, boolean potential) {
      this.field3694 = blockPos.getX();
      this.field3695 = blockPos.getY();
      this.field3696 = blockPos.getZ();
      this.field3697 = potential;
      return this;
   }

   public void method2029(Render3DEvent event) {
      BozeDrawColor var5 = this.field3697 ? this.field3698.field769.method1362() : this.field3698.field768.method1362();
      int var6 = event.field1950
         .field2170
         .method710((double)this.field3694, (double)this.field3695 + 0.05, (double)this.field3696)
         .method715(var5)
         .method714(var5.method958())
         .method712(var5.method959())
         .method714(var5.method960())
         .method711(var5.getMinHue(), var5.getMaxHue())
         .method2010();
      int var7 = event.field1950
         .field2170
         .method710((double)this.field3694, (double)this.field3695 + 0.05, (double)(this.field3696 + 1))
         .method715(var5)
         .method714(var5.method958())
         .method712(var5.method959())
         .method714(var5.method960())
         .method711(var5.getMinHue(), var5.getMaxHue())
         .method2010();
      int var8 = event.field1950
         .field2170
         .method710((double)(this.field3694 + 1), (double)this.field3695 + 0.05, (double)this.field3696)
         .method715(var5)
         .method714(var5.method958())
         .method712(var5.method959())
         .method714(var5.method960())
         .method711(var5.getMinHue(), var5.getMaxHue())
         .method2010();
      int var9 = event.field1950
         .field2170
         .method710((double)(this.field3694 + 1), (double)this.field3695 + 0.05, (double)(this.field3696 + 1))
         .method715(var5)
         .method714(var5.method958())
         .method712(var5.method959())
         .method714(var5.method960())
         .method711(var5.getMinHue(), var5.getMaxHue())
         .method2010();
      event.field1950.field2170.method1964(var6, var7);
      event.field1950.field2170.method1964(var8, var9);
      event.field1950.field2170.method1964(var6, var8);
      event.field1950.field2170.method1964(var7, var9);
   }
}
