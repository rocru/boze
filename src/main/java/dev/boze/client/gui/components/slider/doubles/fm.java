package dev.boze.client.gui.components.slider.doubles;

import dev.boze.client.gui.components.DoubleSliderComponent;
import dev.boze.client.gui.components.scaled.ShaderSettingComponent;

class fm extends DoubleSliderComponent {
   final ShaderSettingComponent field1201;

   fm(ShaderSettingComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field1201 = var1;
   }

   @Override
   protected void method938(double value) {
      this.field1201.field1479.method458().method207((float)value);
   }

   @Override
   protected double method2091() {
      return (double)this.field1201.field1479.method458().method1384();
   }

   @Override
   protected double method1614() {
      return 0.0;
   }

   @Override
   protected double method1390() {
      return 5.0;
   }

   @Override
   protected double method1391() {
      return 0.05;
   }

   @Override
   protected void method2142() {
      this.field1201.field1479.method458().method207(this.field1201.field1479.method458().field55);
   }
}
