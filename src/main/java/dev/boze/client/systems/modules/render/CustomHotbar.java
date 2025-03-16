package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class CustomHotbar extends Module {
   public static final CustomHotbar INSTANCE = new CustomHotbar();
   public final ColorSetting field3494 = new ColorSetting("Fill", new BozeDrawColor(-1610612736), "Fill Color");
   public final BooleanSetting field3495 = new BooleanSetting("Offhand", false, "Custom Offhand");
   public final ColorSetting field3496 = new ColorSetting("Color", new BozeDrawColor(-1610612736), "Offhand Color", this.field3495);
   public final BooleanSetting field3497 = new BooleanSetting("Selection", false, "Custom Selection indicator");
   public final ColorSetting field3498 = new ColorSetting("Color", new BozeDrawColor(-1), "Selection Color", this.field3495);

   private CustomHotbar() {
      super("CustomHotbar", "Custom Hotbar", Category.Render);
   }
}
