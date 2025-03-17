package dev.boze.client.gui.components.slider.doubles;

import dev.boze.client.gui.components.DoubleSliderComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;

public class fl extends DoubleSliderComponent {
   final SettingColorComponent field1999;

   public fl(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field1999 = var1;
   }

   @Override
   protected void method938(double value) {
      this.field1999.method1362().field1844 = value;
   }

   @Override
   protected double method2091() {
      return this.field1999.method1362().field1844;
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
      this.field1999.method1362().field1844 = 0.0;
   }
}
