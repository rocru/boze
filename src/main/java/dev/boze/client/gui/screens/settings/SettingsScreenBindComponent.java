package dev.boze.client.gui.screens.settings;

import dev.boze.client.gui.components.BindComponent;
import dev.boze.client.gui.components.scaled.ModuleComponent;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;

public class SettingsScreenBindComponent extends BindComponent {
   final Module field2086;
   final ModuleComponent field2087;

   public SettingsScreenBindComponent(ModuleComponent var1, String var2, double var3, double var5, double var7, double var9, Module var11) {
      super(var2, var3, var5, var7, var9);
      this.field2087 = var1;
      this.field2086 = var11;
   }

   @Override
   protected void setBind(Bind value) {
      this.field2086.bind.set(value);
   }

   @Override
   protected Bind getBind() {
      return this.field2086.bind;
   }
}
