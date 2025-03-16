package dev.boze.client.gui.components.rotation;

import dev.boze.client.gui.components.RotationComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.StaticColor;
import java.awt.Color;

class fI extends RotationComponent {
   final StaticColor field1208;
   final NewColorComponent field1209;

   fI(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, StaticColor var11) {
      super(var2, var3, var5, var7, var9);
      this.field1209 = var1;
      this.field1208 = var11;
   }

   @Override
   protected void method938(double value) {
      float[] var3 = Color.RGBtoHSB(this.field1208.field430, this.field1208.field431, this.field1208.field432, null);
      int[] var4 = RGBAColor.method190(value * 360.0, (double)var3[1], (double)var3[2]);
      this.field1208.field430 = var4[0];
      this.field1208.field431 = var4[1];
      this.field1208.field432 = var4[2];
   }

   @Override
   protected double method2091() {
      float[] var1 = Color.RGBtoHSB(this.field1208.field430, this.field1208.field431, this.field1208.field432, null);
      return (double)var1[0];
   }

   @Override
   protected double method1614() {
      return 0.001;
   }

   @Override
   protected void method2142() {
      this.field1208.field430 = 148;
      this.field1208.field431 = 123;
      this.field1208.field432 = 211;
   }
}
