package dev.boze.client.gui.components.slider.floats.array;

import dev.boze.client.gui.components.FloatArraySliderComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.utils.RGBAColor;

import java.awt.*;

public class gd extends FloatArraySliderComponent {
   final ColorSetting field2007;
   final ColorSettingComponent field2008;

   gd(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2008 = var1;
      this.field2007 = var11;
   }

   @Override
   protected float[] method111() {
      return Color.RGBtoHSB(this.field2007.method1374().field408, this.field2007.method1374().field409, this.field2007.method1374().field410, null);
   }

   @Override
   protected void method521(float hue, float sat, float bri) {
      int[] var4 = RGBAColor.method190((double)hue * 360.0, (double)sat, (double)bri);
      this.field2007.method1374().method192(var4[0], var4[1], var4[2], this.field2007.method1374().field411);
   }
}
