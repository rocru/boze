package dev.boze.client.gui.components.slider.doubles;

import dev.boze.client.gui.components.DoubleSliderComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

class fo extends DoubleSliderComponent {
   final ColorSetting field2000;
   final ColorSettingComponent field2001;

   fo(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2001 = var1;
      this.field2000 = var11;
   }

   @Override
   protected void method938(double value) {
      if (Math.abs(0.0) < 1.0E-6) {
      }

      this.field2000.method1374().field1843 = 0.0;
   }

   @Override
   protected double method2091() {
      return this.field2000.method1374().field1843;
   }

   @Override
   protected double method1614() {
      return -1.0;
   }

   @Override
   protected double method1390() {
      return 1.0;
   }

   @Override
   protected double method1391() {
      return 0.01;
   }

   @Override
   protected void method2142() {
      this.field2000.method1374().field1843 = 0.0;
   }
}
