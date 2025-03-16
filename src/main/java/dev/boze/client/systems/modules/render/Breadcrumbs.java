package dev.boze.client.systems.modules.render;

import dev.boze.client.enums.BreadcrumbsLimit;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Breadcrumbs extends Module {
   public static final Breadcrumbs INSTANCE = new Breadcrumbs();
   public ConcurrentHashMap<Vec3d, Long> field3422 = new ConcurrentHashMap();
   public CopyOnWriteArrayList<Vec3d> field3423 = new CopyOnWriteArrayList();
   public final BooleanSetting field3424 = new BooleanSetting("OnlyRender", false, "Keep rendering but stop adding breadcrumbs");
   private final IntSetting field3425 = new IntSetting("Delay", 0, 0, 20, 1, "Delay for adding breadcrumb vertices");
   private final EnumSetting<BreadcrumbsLimit> field3426 = new EnumSetting<BreadcrumbsLimit>("Limit", BreadcrumbsLimit.Length, "Limit for breadcrumbs");
   private final IntSetting field3427 = new IntSetting(
      "MaxVertices", 20000, 1000, 100000, 1000, "Maximum amount of breadcrumb vertices", this::lambda$new$0, this.field3426
   );
   private final FloatSetting field3428 = new FloatSetting(
      "FadeTime", 1.0F, 0.1F, 10.0F, 0.1F, "Fade time for breadcrumb vertices", this::lambda$new$1, this.field3426
   );
   private final RGBASetting field3429 = new RGBASetting("Color", new RGBAColor(125, 0, 255, 255), "Color for breadcrumbs line");
   private final BooleanSetting field3430 = new BooleanSetting("LineGradient", true, "Use gradient color for breadcrumbs line", this.field3429);
   private final FloatSetting field3431 = new FloatSetting(
      "Speed", 0.01F, 0.0F, 0.1F, 0.005F, "Gradient color change speed", this.field3430::method419, this.field3429
   );
   private int field3432 = 0;
   private final Timer field3433 = new Timer();

   public Breadcrumbs() {
      super("Breadcrumbs", "Draws a line where you've walked", Category.Render);
   }

   @EventHandler
   public void method1910(PostPlayerTickEvent event) {
      if (!this.field3424.method419()) {
         if (this.field3423.isEmpty() || !event.field1941.getPos().equals(this.field3423.get(this.field3423.size() - 1))) {
            if (this.field3432 > this.field3425.method434()) {
               Vec3d var5 = event.field1941.getPos();
               if (this.method1913()) {
                  var5 = var5.multiply(8.0, 1.0, 8.0);
               }

               if (!this.field3423.isEmpty()
                  && var5.distanceTo((Vec3d)this.field3423.get(this.field3423.size() - 1)) > 64.0
                  && !this.field3433.hasElapsed(1500.0)) {
                  return;
               }

               this.field3423.add(var5);
               this.field3433.reset();
               if (this.field3426.method461() == BreadcrumbsLimit.Length) {
                  if (this.field3423.size() >= this.field3427.method434()) {
                     this.field3423.remove(0);
                     this.field3423.remove(1);
                  }
               } else if (this.field3426.method461() == BreadcrumbsLimit.Fade) {
                  this.field3422.put(var5, System.currentTimeMillis());
               }
            }

            this.field3432++;
         }
      }
   }

   @EventHandler
   public void method1911(GameJoinEvent event) {
      this.field3423.clear();
   }

   @EventHandler
   public void method1912(Render3DEvent event) {
      float[] var5 = Color.RGBtoHSB(this.field3429.method1347().field408, this.field3429.method1347().field409, this.field3429.method1347().field410, null);

      for (int var6 = 0; var6 < this.field3423.size() - 2; var6++) {
         Vec3d var7 = (Vec3d)this.field3423.get(var6);
         Vec3d var8 = (Vec3d)this.field3423.get(var6 + 1);
         int var9 = this.field3429.method1347().field411;
         int var10 = this.field3429.method1347().field411;
         if (this.field3426.method461() == BreadcrumbsLimit.Fade) {
            if (!this.field3422.containsKey(var7) || !this.field3422.containsKey(var8)) {
               return;
            }

            float var11 = (float)(System.currentTimeMillis() - (Long)this.field3422.get(var7));
            float var12 = (float)(System.currentTimeMillis() - (Long)this.field3422.get(var8));
            if (var11 >= this.field3428.method423() * 1000.0F) {
               this.field3423.remove(var7);
               this.field3422.remove(var7);
               return;
            }

            var9 = (int)(MathHelper.clamp(1.0F - var11 / (this.field3428.method423() * 1100.0F), 0.0F, 1.0F) * 255.0F);
            var10 = (int)(MathHelper.clamp(1.0F - var12 / (this.field3428.method423() * 1100.0F), 0.0F, 1.0F) * 255.0F);
         }

         int var15 = Color.HSBtoRGB(var5[0], var5[1], var5[2]);
         RGBAColor var16 = new RGBAColor(var15 >> 16 & 0xFF, var15 >> 8 & 0xFF, var15 & 0xFF, var9);
         if (this.field3430.method419()) {
            var5[0] += this.field3431.method423() * 0.1F;
         }

         int var13 = Color.HSBtoRGB(var5[0], var5[1], var5[2]);
         RGBAColor var14 = new RGBAColor(var13 >> 16 & 0xFF, var13 >> 8 & 0xFF, var13 & 0xFF, var10);
         if (this.method1913()) {
            var7 = var7.multiply(0.125, 1.0, 0.125);
            var8 = var8.multiply(0.125, 1.0, 0.125);
         }

         event.field1950.method1233(var7.x, var7.y, var7.z, var8.x, var8.y, var8.z, var16, var14);
      }
   }

   private boolean method1913() {
      return mc.world.getRegistryKey().getValue().getPath().equals("the_nether");
   }

   private boolean lambda$new$1() {
      return this.field3426.method461() == BreadcrumbsLimit.Fade;
   }

   private boolean lambda$new$0() {
      return this.field3426.method461() == BreadcrumbsLimit.Length;
   }
}
