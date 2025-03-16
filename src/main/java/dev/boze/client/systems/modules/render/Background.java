package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.renderer.DrawMode;
import dev.boze.client.renderer.Mesh;
import dev.boze.client.renderer.ShaderMesh;
import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;

public class Background extends Module {
   public static final Background INSTANCE = new Background();
   private final MinMaxSetting field3384 = new MinMaxSetting("Distance", 350.0, 25.0, 350.0, 0.5, "Distance to background");
   private final ColorSetting field3385 = new ColorSetting("Color", new BozeDrawColor(-1778384896, true, 0.01, 0.0, 0.005), "Color for background");
   private Mesh field3386;

   public Background() {
      super("Background", "Renders custom background (sky)", Category.Render);
   }

   @EventHandler(
      priority = 1
   )
   public void method1896(Render3DEvent event) {
      if (this.field3386 == null) {
         this.field3386 = new ShaderMesh(
            ShaderRegistry.field2266, DrawMode.Triangles, Attrib.Vec3, Attrib.Color, Attrib.Float, Attrib.Vec2, Attrib.Float, Attrib.Vec2
         );
      }

      Vec3d var5 = mc.gameRenderer.getCamera().getPos();
      Vec3d var6 = this.method1898((double)mc.gameRenderer.getCamera().getPitch(), (double)mc.gameRenderer.getCamera().getYaw())
         .multiply(this.field3384.getValue());
      Vec3d var7 = var5.add(var6);
      Vec3d var8 = this.method1898(0.0, (double)mc.gameRenderer.getCamera().getYaw() - 90.0);
      Vec3d var9 = this.method1898(0.0, (double)mc.gameRenderer.getCamera().getYaw() + 90.0);
      Vec3d var10 = this.method1898((double)mc.gameRenderer.getCamera().getPitch() - 90.0, (double)mc.gameRenderer.getCamera().getYaw());
      Vec3d var11 = this.method1898((double)mc.gameRenderer.getCamera().getPitch() + 90.0, (double)mc.gameRenderer.getCamera().getYaw());
      Vec3d var12 = var7.add(var8.add(var11).multiply(42069.0));
      Vec3d var13 = var7.add(var8.add(var10).multiply(42069.0));
      Vec3d var14 = var7.add(var9.add(var10).multiply(42069.0));
      Vec3d var15 = var7.add(var9.add(var11).multiply(42069.0));
      this.field3386.method2142();
      ShaderRegistry.field2266.method2142();
      this.method1897(var15.x, var15.y, var15.z, var14.x, var14.y, var14.z, var13.x, var13.y, var13.z, var12.x, var12.y, var12.z, this.field3385.method1362());
      this.field3386.method721(event.matrix, true, true);
   }

   private void method1897(
      double var1,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      double var21,
      double var23,
      BozeDrawColor var25
   ) {
      this.field3386
         .method1214(
            this.field3386
               .method710(var1, var3, var5)
               .method715(var25)
               .method714(var25.method958())
               .method711(var25.method959()[0] * 0.005, var25.method959()[1] * 0.005)
               .method714(var25.method960())
               .method711(var25.getMinHue(), var25.getMaxHue())
               .method2010(),
            this.field3386
               .method710(var7, var9, var11)
               .method715(var25)
               .method714(var25.method958())
               .method711(var25.method959()[0] * 0.005, var25.method959()[1] * 0.005)
               .method714(var25.method960())
               .method711(var25.getMinHue(), var25.getMaxHue())
               .method2010(),
            this.field3386
               .method710(var13, var15, var17)
               .method715(var25)
               .method714(var25.method958())
               .method711(var25.method959()[0] * 0.005, var25.method959()[1] * 0.005)
               .method714(var25.method960())
               .method711(var25.getMinHue(), var25.getMaxHue())
               .method2010(),
            this.field3386
               .method710(var19, var21, var23)
               .method715(var25)
               .method714(var25.method958())
               .method711(var25.method959()[0] * 0.005, var25.method959()[1] * 0.005)
               .method714(var25.method960())
               .method711(var25.getMinHue(), var25.getMaxHue())
               .method2010()
         );
   }

   private Vec3d method1898(double var1, double var3) {
      double var5 = var1 * (Math.PI / 180.0);
      double var7 = -var3 * (Math.PI / 180.0);
      double var9 = Math.cos(var7);
      double var11 = Math.sin(var7);
      double var13 = Math.cos(var5);
      double var15 = Math.sin(var5);
      return new Vec3d(var11 * var13, -var15, var9 * var13);
   }
}
