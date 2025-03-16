package dev.boze.client.gui.colorpanel.main;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.DoubleSliderComponent;
import dev.boze.client.manager.ColorManager;

class OffsetComponent extends DoubleSliderComponent {
   final ColorManager field1982;
   final ColorPanel field1983;

   OffsetComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1983 = var1;
      this.field1982 = var11;
   }

   @Override
   protected void method938(double value) {
      this.field1982.method1374().field1844 = value;
   }

   @Override
   protected double method2091() {
      return this.field1982.method1374().field1844;
   }

   @Override
   protected double method1614() {
      return 0.0;
   }

   @Override
   protected double method1390() {
      return 2.0;
   }

   @Override
   protected double method1391() {
      return 0.01;
   }

   @Override
   protected void method2142() {
      this.field1982.method1374().field1844 = 0.0;
   }
}
