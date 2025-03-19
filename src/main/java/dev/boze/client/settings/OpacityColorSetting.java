package dev.boze.client.settings;

import dev.boze.client.utils.ColorWrapper;

import java.util.function.BooleanSupplier;

public class OpacityColorSetting extends WeirdColorSetting {
   public OpacityColorSetting(String name, ColorWrapper value, String description) {
      super(name, value, description);
      super.opacity = true;
   }

   public OpacityColorSetting(String name, ColorWrapper value, String description, BooleanSupplier visibility) {
      super(name, value, description, visibility);
      super.opacity = true;
   }

   public OpacityColorSetting(String name, ColorWrapper value, String description, Setting parent) {
      super(name, value, description, parent);
      super.opacity = true;
   }

   public OpacityColorSetting(String name, ColorWrapper value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, value, description, visibility, parent);
      super.opacity = true;
   }
}
