package dev.boze.client.gui.colorpanel.gradient;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.manager.ColorManager;

public class MinHueComponent extends RotationComponent {
   final ColorManager field1203;
   final ColorPanel field1204;

   public MinHueComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1204 = var1;
      this.field1203 = var11;
   }

   @Override
   protected void method938(double value) {
      this.field1203.method1374().field1846[0] = value;
   }

   @Override
   protected double method2091() {
      return this.field1203.method1374().field1846[0];
   }

   @Override
   protected double method1614() {
      return 0.001;
   }

   @Override
   protected void method2142() {
      this.field1203.method1374().field1846[0] = 0.0;
   }
}
