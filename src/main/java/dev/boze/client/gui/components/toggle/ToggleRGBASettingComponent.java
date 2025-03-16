package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.settings.RGBASetting;

class ToggleRGBASettingComponent extends ToggleComponent {
   final RGBASetting field2055;
   final RGBASettingComponent field2056;

   ToggleRGBASettingComponent(RGBASettingComponent var1, String var2, double var3, double var5, double var7, double var9, RGBASetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field2056 = var1;
      this.field2055 = var11;
   }

   @Override
   protected void setToggled(boolean value) {
      this.field2055.method206(value);
   }

   @Override
   protected boolean isToggled() {
      return this.field2055.method2118();
   }
}
