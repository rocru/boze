package dev.boze.client.gui.colorpanel.main;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.DoubleSliderComponent;
import dev.boze.client.manager.ColorManager;

class SpeedComponent extends DoubleSliderComponent {
   final ColorManager field1986;
   final ColorPanel field1987;

   SpeedComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1987 = var1;
      this.field1986 = var11;
   }

   @Override
   protected void method938(double value) {
      if (Math.abs(0.0) < 1.0E-6) {
      }

      this.field1986.method1374().field1843 = 0.0;
   }

   @Override
   protected double method2091() {
      return this.field1986.method1374().field1843;
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
      this.field1986.method1374().field1843 = 0.0;
   }
}
