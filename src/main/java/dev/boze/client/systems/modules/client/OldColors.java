package dev.boze.client.systems.modules.client;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ModuleColors;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;

public class OldColors extends Module {
   public static final OldColors INSTANCE = new OldColors();
   public final RGBASetting field2404 = new RGBASetting("Client", new RGBAColor(-7046189), "Main client color");
   public final ColorSetting clientGradient = new ColorSetting("ClientGradient", new BozeDrawColor(-7046189), "Main client 3D color");
   private final EnumSetting<ModuleColors> field2405 = new EnumSetting<ModuleColors>("ModuleColors", ModuleColors.Consistent, "Module colors");

   public OldColors() {
      super(
         "OldColors",
         "Customize global colors\nYou can sync other colors to these by pressing the circular button on the left of the preview square\n",
         Category.Client,
         ConfigCategory.Visuals
      );
      this.field2404.method456(false);
      this.clientGradient.setSync(false);
   }

   @Override
   public boolean setEnabled(boolean newState) {
      return false;
   }

   public static RGBAColor method1342() {
      return INSTANCE.field2404.method1347();
   }

   public static BozeDrawColor method1343() {
      return INSTANCE.clientGradient.method1362();
   }

   public static ModuleColors method1344() {
      return INSTANCE.field2405.method461();
   }
}
