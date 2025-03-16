package dev.boze.client.gui.components.slider.ints;

import dev.boze.client.gui.components.IntSliderComponent;
import dev.boze.client.gui.components.scaled.ShaderSettingComponent;

class fV extends IntSliderComponent {
   final ShaderSettingComponent field1228;

   fV(ShaderSettingComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field1228 = var1;
   }

   @Override
   protected void method1649(int value) {
      this.field1228.field1479.method457().method1649(value);
   }

   @Override
   protected int method2010() {
      return this.field1228.field1479.method457().method2010();
   }

   @Override
   protected int method1547() {
      return 0;
   }

   @Override
   protected int method1365() {
      return 5;
   }

   @Override
   protected void method2142() {
      this.field1228.field1479.method457().method1649(this.field1228.field1479.method457().field48);
   }
}
