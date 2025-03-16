package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;

class ToggleColorSettingComponent extends ToggleComponent {
   final ColorSetting field2049;
   final ColorSettingComponent field2050;

   ToggleColorSettingComponent(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2050 = var1;
      this.field2049 = var11;
   }

   @Override
   protected void setToggled(boolean value) {
      this.field2049.method1374().field1842 = value;
   }

   @Override
   protected boolean isToggled() {
      return this.field2049.method1374().field1842;
   }
}
