package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.TargetSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ReachCircles extends Module {
   public static final ReachCircles INSTANCE = new ReachCircles();
   private final MinMaxSetting field3662 = new MinMaxSetting("Reach", 3.0, 1.0, 10.0, 0.1, "Reach distance");
   private final MinMaxSetting field3663 = new MinMaxSetting("Range", 10.0, 1.0, 100.0, 0.1, "Range to render reach circles");
   private final ColorSetting field3664 = new ColorSetting("Color", new BozeDrawColor(-65536), "Color of the reach circles");
   private final TargetSetting field3665 = new TargetSetting();

   private ReachCircles() {
      super("ReachCircles", "Draws reach circles around entities", Category.Render);
   }

   @EventHandler
   public void method2022(Render3DEvent event) {
      for (Entity var6 : mc.world.getEntities()) {
         if (this.field3665.method2055(var6)) {
            double var7 = mc.player.getPos().distanceTo(var6.getPos());
            if (!(var7 > this.field3663.getValue())) {
               double var9 = MathHelper.lerp((double)event.field1951, var6.lastRenderX, var6.getX());
               double var11 = MathHelper.lerp((double)event.field1951, var6.lastRenderY, var6.getY());
               double var13 = MathHelper.lerp((double)event.field1951, var6.lastRenderZ, var6.getZ());

               for (double var15 = 0.0; var15 < 360.0; var15++) {
                  double var17 = var9 + Math.cos(Math.toRadians(var15)) * this.field3662.getValue();
                  double var19 = var13 + Math.sin(Math.toRadians(var15)) * this.field3662.getValue();
                  double var21 = var9 + Math.cos(Math.toRadians(var15 + 1.0)) * this.field3662.getValue();
                  double var23 = var13 + Math.sin(Math.toRadians(var15 + 1.0)) * this.field3662.getValue();
                  event.field1950.method1241(var17, var11, var19, var21, var11, var23, this.field3664.method1362());
               }
            }
         }
      }
   }
}
