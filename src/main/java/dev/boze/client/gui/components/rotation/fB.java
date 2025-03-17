package dev.boze.client.gui.components.rotation;

import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;
import dev.boze.client.utils.RGBAColor;
import java.awt.Color;

public class fB extends RotationComponent {
   final SettingColorComponent field1205;

   public fB(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field1205 = var1;
   }

   @Override
   protected void method938(double value) {
      float[] var3 = Color.RGBtoHSB(this.field1205.method1362().field408, this.field1205.method1362().field409, this.field1205.method1362().field410, null);
      int[] var4 = RGBAColor.method190(value * 360.0, (double)var3[1], (double)var3[2]);
      this.field1205.method1362().method192(var4[0], var4[1], var4[2], this.field1205.method1362().field411);
   }

   @Override
   protected double method2091() {
      float[] var1 = Color.RGBtoHSB(this.field1205.method1362().field408, this.field1205.method1362().field409, this.field1205.method1362().field410, null);
      return (double)var1[0];
   }

   @Override
   protected double method1614() {
      return 0.001;
   }

   @Override
   protected void method2142() {
      float[] var1 = Color.RGBtoHSB(this.field1205.method1362().field408, this.field1205.method1362().field409, this.field1205.method1362().field410, null);
      float[] var2 = Color.RGBtoHSB(0, 0, 0, null);
      int[] var3 = RGBAColor.method190((double)var2[0], (double)var1[1], (double)var1[2]);
      this.field1205.method1362().method192(var3[0], var3[1], var3[2], this.field1205.method1362().field411);
   }
}
