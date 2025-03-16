package dev.boze.client.systems.modules.render;

import dev.boze.client.events.GetFovEvent;
import dev.boze.client.events.MouseScrollEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class Zoom extends Module {
   public static final Zoom INSTANCE = new Zoom();
   private final MinMaxSetting field3870 = new MinMaxSetting("Zoom", 6.0, 1.0, 20.0, 0.1, "How much to zoom");
   private final BooleanSetting field3871 = new BooleanSetting("ScrollZoom", true, "Zoom by scrolling");
   private final MinMaxSetting field3872 = new MinMaxSetting("Sensitivity", 1.0, 0.1, 10.0, 0.1, "Scroll sensitivity", this.field3871);
   private double field3873 = this.field3870.getValue();
   private double field3874;

   public Zoom() {
      super("Zoom", "Zooms in", Category.Render);
   }

   @Override
   public void onEnable() {
      this.field3874 = (Double)mc.options.getMouseSensitivity().getValue();
      this.field3873 = this.field3870.getValue();
      mc.options.getMouseSensitivity().setValue(this.field3874 / Math.max(this.method2091() * 0.5, 1.0));
   }

   @Override
   public void onDisable() {
      mc.options.getMouseSensitivity().setValue(this.field3874);
   }

   @EventHandler
   public void method2089(MouseScrollEvent event) {
      if (this.field3871.method419() && mc.currentScreen == null) {
         this.field3873 = this.field3873 + event.vertical * 0.25 * this.field3872.getValue() * this.field3873;
         if (this.field3873 < 1.0) {
            this.field3873 = 1.0;
         }

         mc.options.getMouseSensitivity().setValue(this.field3874 / Math.max(this.method2091() * 0.5, 1.0));
         event.method1020();
      }
   }

   @EventHandler
   public void method2090(GetFovEvent event) {
      event.field1922 = event.field1922 / this.method2091();
   }

   public double method2091() {
      return this.field3873;
   }
}
