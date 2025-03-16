package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.utils.RGBAColor;

class fY extends IntSliderComponent {
   final RGBASetting field2040;
   final RGBASettingComponent field2041;

   fY(RGBASettingComponent var1, String var2, double var3, double var5, double var7, double var9, RGBASetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2041 = var1;
      this.field2040 = var11;
   }

   @Override
   protected void method1649(int value) {
      this.field2040.method1348().field410 = value;
   }

   @Override
   protected int method2010() {
      return this.field2040.method1348().field410;
   }

   @Override
   protected int method1547() {
      return 0;
   }

   @Override
   protected int method1365() {
      return 255;
   }

   @Override
   protected void method2142() {
      this.field2040.method1348().field410 = this.field2040.field958.field410;
   }

   @Override
   protected RGBAColor method1347() {
      return RGBAColor.field406;
   }
}
