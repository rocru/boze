package dev.boze.client.gui.components.slider.floats.array;

import dev.boze.client.gui.components.FloatArraySliderComponent;
import dev.boze.client.gui.components.scaled.SettingColorComponent;
import dev.boze.client.utils.RGBAColor;

import java.awt.*;

public class gb extends FloatArraySliderComponent {
   final SettingColorComponent field2004;

   public gb(SettingColorComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field2004 = var1;
   }

   @Override
   protected float[] method111() {
      return Color.RGBtoHSB(this.field2004.method1362().field408, this.field2004.method1362().field409, this.field2004.method1362().field410, null);
   }

   @Override
   protected void method521(float hue, float sat, float bri) {
      int[] var4 = RGBAColor.method190((double)hue * 360.0, (double)sat, (double)bri);
      this.field2004.method1362().method192(var4[0], var4[1], var4[2], this.field2004.method1362().field411);
   }
}
