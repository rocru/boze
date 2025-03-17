package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

public class f2 extends IntSliderComponent {
   final ColorSetting field2015;
   final ColorSettingComponent field2016;

   f2(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2016 = var1;
      this.field2015 = var11;
   }

   @Override
   protected void method1649(int value) {
      this.field2015.method1374().field411 = value;
   }

   @Override
   protected int method2010() {
      return this.field2015.method1374().field411;
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
      this.field2015.method1374().field411 = this.field2015.field980.field411;
   }
}
