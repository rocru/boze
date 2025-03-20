package dev.boze.client.systems.modules.render;

import dev.boze.client.events.MouseScrollEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.MathHelper;

public class CameraClip extends Module {
   public static final CameraClip INSTANCE = new CameraClip();
   public final BooleanSetting field3457 = new BooleanSetting("Clip", true, "Clip the camera through walls");
   public final FloatSetting field3458 = new FloatSetting("Distance", 4.0F, 1.0F, 20.0F, 0.2F, "Camera distance");
   private final BooleanSetting field3459 = new BooleanSetting("ScrollZoom", true, "Zoom by scrolling");
   private final FloatSetting field3460 = new FloatSetting("Sensitivity", 1.0F, 0.1F, 10.0F, 0.1F, "Scroll sensitivity", this.field3459);

   public CameraClip() {
      super("CameraClip", "Clip the camera through walls when in third person", Category.Render);
   }

   @EventHandler
   private void method1920(MouseScrollEvent var1) {
      if (this.field3459.getValue() && mc.currentScreen == null && !mc.options.getPerspective().isFirstPerson()) {
         this.field3458.setValue(MathHelper.clamp(this.field3458.getValue() - this.field3460.getValue() * (float)var1.vertical, 1.0F, 20.0F));
         var1.method1020();
      }
   }
}
