package dev.boze.client.gui.components.toggle;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.bottomrow.EditGradientColorComponent;
import dev.boze.client.utils.render.color.GradientColor;

class ToggleEditGradientComponent extends ToggleComponent {
   final GradientColor field2053;
   final EditGradientColorComponent field2054;

   ToggleEditGradientComponent(EditGradientColorComponent var1, String var2, double var3, double var5, double var7, double var9, GradientColor var11) {
      super(var2, var3, var5, var7, var9);
      this.field2054 = var1;
      this.field2053 = var11;
   }

   @Override
   protected void setToggled(boolean value) {
      this.field2053.field425 = value;
   }

   @Override
   protected boolean isToggled() {
      return this.field2053.field425;
   }
}
