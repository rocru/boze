package dev.boze.client.gui.components.rotation;

import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

public class fG extends RotationComponent {
   final ColorSetting field1212;
   final ColorSettingComponent field1213;

   fG(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field1213 = var1;
      this.field1212 = var11;
   }

   @Override
   protected void method938(double value) {
      this.field1212.method1374().field1846[0] = value;
   }

   @Override
   protected double method2091() {
      return this.field1212.method1374().field1846[0];
   }

   @Override
   protected double method1614() {
      return 0.001;
   }

   @Override
   protected void method2142() {
      this.field1212.method1374().field1846[0] = 0.0;
   }
}
