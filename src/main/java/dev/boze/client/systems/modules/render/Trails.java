package dev.boze.client.systems.modules.render;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.trails.nx;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RGBAColor;
import mapped.Class3062;
import mapped.Class3064;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class Trails extends Module {
   public static final Trails INSTANCE = new Trails();
   private final IntArraySetting field3765 = new IntArraySetting("Range", new int[]{0, 150}, 0, 250, 1, "Range to render trails within");
   private final BooleanSetting field3766 = new BooleanSetting("Self", false, "Render trails for yourself");
   private final FloatSetting field3767 = new FloatSetting("FadeTime", 1.0F, 0.1F, 10.0F, 0.1F, "Fade time in seconds");
   private final RGBASetting field3768 = new RGBASetting("StartColor", new RGBAColor(125, 0, 255, 255), "Start color for trails");
   private final RGBASetting field3769 = new RGBASetting("EndColor", new RGBAColor(0, 125, 255, 0), "End color for trails");
   private final FloatSetting field3770 = new FloatSetting("Width", 1.5F, 0.5F, 5.0F, 0.1F, "Line width", Trails::lambda$new$0);
   private final Class3064<Vector3d> field3771 = new Class3064<Vector3d>(Vector3d::new);
   private final List<nx> field3772 = new ArrayList();
   private Renderer3D field3773;

   private Trails() {
      super("Trails", "Shows fading trails behind players", Category.Render);
   }

   @EventHandler
   private void method2046(Render3DEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if (this.field3773 == null) {
            this.field3773 = new Renderer3D(false, false);
         }

         this.field3773.field2166.field1594 = this.field3770.method423();
         this.field3773.method1217();

         for (nx var6 : this.field3772) {
            if (mc.world.getEntityById(var6.field3776) == null) {
               var6.method2050();
            }
         }

         for (Entity var9 : mc.world.getEntities()) {
            if (var9.age > 1 && this.method2048(var9)) {
               this.method2047(var9, (double)var1.field1951);
            }
         }

         for (nx var10 : this.field3772) {
            var10.method2052(var1, this.field3768.method1347(), this.field3769.method1347());
         }

         this.field3773.method1219(var1.matrix);
      }
   }

   private void method2047(Entity var1, double var2) {
      nx var7 = null;

      for (nx var9 : this.field3772) {
         if (var9.field3776 == var1.getId()) {
            var7 = var9;
            break;
         }
      }

      if (var7 == null) {
         var7 = this.method2049();
         var7.field3776 = var1.getId();
      }

      Vector3d var10 = Class3062.method5990(this.field3771.method5993(), var1, var2);
      var7.method2051(var10);
   }

   private boolean method2048(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return this.field3766.method419();
         } else {
            double var5 = (double)var1.distanceTo(mc.player);
            return !(var5 < (double)this.field3765.method2010()) && !(var5 > (double)this.field3765.method1547());
         }
      } else {
         return false;
      }
   }

   private nx method2049() {
      for (nx var5 : this.field3772) {
         if (var5.field3774.isEmpty()) {
            return var5;
         }
      }

      nx var6 = new nx(this);
      this.field3772.add(var6);
      return var6;
   }

   private static boolean lambda$new$0() {
      return !MinecraftClient.IS_SYSTEM_MAC;
   }
}
