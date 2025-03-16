package dev.boze.client.gui.components.bind;

import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.client.api.BozeBind;
import dev.boze.client.gui.components.BindComponent;
import dev.boze.client.gui.components.scaled.ToggleableModuleSettingComponent;
import dev.boze.client.utils.Bind;

class eP extends BindComponent {
   final ToggleableModule field1990;
   final ToggleableModuleSettingComponent field1991;

   eP(ToggleableModuleSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ToggleableModule var11) {
      super(var2, var3, var5, var7, var9);
      this.field1991 = var1;
      this.field1990 = var11;
   }

   @Override
   protected void setBind(Bind value) {
      this.field1990.setBind(new BozeBind(!value.isKey(), value.getBind()));
   }

   @Override
   protected Bind getBind() {
      return new Bind(!this.field1990.getBind().isButton(), this.field1990.getBind().getBind());
   }
}
