package dev.boze.client.gui.components.rotation;

import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;

public class fC extends RotationComponent {
   final SettingColorComponent field1221;

   public fC(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field1221 = var1;
   }

   @Override
   protected void method938(double value) {
      this.field1221.method1362().field1846[0] = value;
   }

   @Override
   protected double method2091() {
      return this.field1221.method1362().field1846[0];
   }

   @Override
   protected double method1614() {
      return 0.001;
   }

   @Override
   protected void method2142() {
      this.field1221.method1362().field1846[0] = 0.0;
   }
}
