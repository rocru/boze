package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.utils.RGBAColor;

public class f3 extends IntSliderComponent {
   final ColorSetting field2017;
   final ColorSettingComponent field2018;

   f3(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2018 = var1;
      this.field2017 = var11;
   }

   @Override
   protected void method1649(int value) {
      this.field2017.method1374().field408 = value;
   }

   @Override
   protected int method2010() {
      return this.field2017.method1374().field408;
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
      this.field2017.method1374().field408 = this.field2017.field980.field408;
   }

   @Override
   protected RGBAColor method1347() {
      return RGBAColor.field403;
   }
}
