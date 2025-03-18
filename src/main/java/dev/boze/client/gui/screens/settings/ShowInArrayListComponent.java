package dev.boze.client.gui.screens.settings;

import dev.boze.client.gui.components.ToggleComponent;
import dev.boze.client.gui.components.scaled.ModuleComponent;
import dev.boze.client.systems.modules.Module;

public class ShowInArrayListComponent extends ToggleComponent {
   final Module field2088;
   final ModuleComponent field2089;

   public ShowInArrayListComponent(ModuleComponent var1, String var2, double var3, double var5, double var7, double var9, Module var11) {
      super(var2, var3, var5, var7, var9);
      this.field2089 = var1;
      this.field2088 = var11;
   }

   @Override
   protected void setToggled(boolean value) {
      this.field2088.setVisibility(value);
   }

   @Override
   protected boolean isToggled() {
      return this.field2088.getVisibility();
   }
}
