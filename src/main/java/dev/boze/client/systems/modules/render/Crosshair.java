package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.render.RenderUtil;
import meteordevelopment.orbit.EventHandler;

public class Crosshair extends Module {
   public static final Crosshair INSTANCE = new Crosshair();
   private final BooleanSetting field3490 = new BooleanSetting("Rounded", true, "Rounded crosshair");
   private final ColorSetting field3491 = new ColorSetting("Color", new BozeDrawColor(255, 255, 255, 255), "The color of the crosshair");
   private final MinMaxSetting field3492 = new MinMaxSetting("Length", 5.0, 1.0, 50.0, 0.1, "The length of the crosshair");
   private final MinMaxSetting field3493 = new MinMaxSetting("Width", 1.0, 1.0, 15.0, 0.1, "The width of the crosshair");

   private Crosshair() {
      super("Crosshair", "Draws a custom crosshair on your screen", Category.Render);
   }

   @EventHandler
   private void method1927(Render2DEvent var1) {
      if (!mc.options.hudHidden) {
         double var5 = (double)mc.getWindow().getScaledWidth() / 2.0;
         double var7 = (double)mc.getWindow().getScaledHeight() / 2.0;
         RenderUtil.field3965.method2233();
         RenderUtil.field3965
            .method2253(
               var5 - this.field3493.getValue() / 2.0,
               var7 - this.field3493.getValue() / 2.0,
               this.field3493.getValue(),
               this.field3493.getValue(),
               this.field3491.method1362()
            );
         if (this.field3490.method419()) {
            RenderUtil.field3965
               .method2258(
                  var5 - this.field3493.getValue() / 2.0 - this.field3492.getValue(),
                  var7 - this.field3493.getValue() / 2.0,
                  this.field3492.getValue(),
                  this.field3493.getValue(),
                  9,
                  24,
                  this.field3493.getValue() / 2.0,
                  this.field3491.method1362()
               );
            RenderUtil.field3965
               .method2258(
                  var5 + this.field3493.getValue() / 2.0,
                  var7 - this.field3493.getValue() / 2.0,
                  this.field3492.getValue(),
                  this.field3493.getValue(),
                  6,
                  24,
                  this.field3493.getValue() / 2.0,
                  this.field3491.method1362()
               );
            RenderUtil.field3965
               .method2258(
                  var5 - this.field3493.getValue() / 2.0,
                  var7 - this.field3493.getValue() / 2.0 - this.field3492.getValue(),
                  this.field3493.getValue(),
                  this.field3492.getValue(),
                  3,
                  24,
                  this.field3493.getValue() / 2.0,
                  this.field3491.method1362()
               );
            RenderUtil.field3965
               .method2258(
                  var5 - this.field3493.getValue() / 2.0,
                  var7 + this.field3493.getValue() / 2.0,
                  this.field3493.getValue(),
                  this.field3492.getValue(),
                  12,
                  24,
                  this.field3493.getValue() / 2.0,
                  this.field3491.method1362()
               );
         } else {
            RenderUtil.field3965
               .method2253(
                  var5 - this.field3493.getValue() / 2.0 - this.field3492.getValue(),
                  var7 - this.field3493.getValue() / 2.0,
                  this.field3492.getValue(),
                  this.field3493.getValue(),
                  this.field3491.method1362()
               );
            RenderUtil.field3965
               .method2253(
                  var5 + this.field3493.getValue() / 2.0,
                  var7 - this.field3493.getValue() / 2.0,
                  this.field3492.getValue(),
                  this.field3493.getValue(),
                  this.field3491.method1362()
               );
            RenderUtil.field3965
               .method2253(
                  var5 - this.field3493.getValue() / 2.0,
                  var7 - this.field3493.getValue() / 2.0 - this.field3492.getValue(),
                  this.field3493.getValue(),
                  this.field3492.getValue(),
                  this.field3491.method1362()
               );
            RenderUtil.field3965
               .method2253(
                  var5 - this.field3493.getValue() / 2.0,
                  var7 + this.field3493.getValue() / 2.0,
                  this.field3493.getValue(),
                  this.field3492.getValue(),
                  this.field3491.method1362()
               );
         }

         RenderUtil.field3965.method2235(null);
      }
   }
}
