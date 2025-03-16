package dev.boze.client.gui.components;

import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.Setting;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import net.minecraft.client.gui.DrawContext;

public class ScaledSettingBaseComponent extends BaseComponent {
   public final Setting field271;
   public RGBAColor field272;
   protected Timer field273 = new Timer();

   public ScaledSettingBaseComponent(Setting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting.name, parent, x, y, width, height);
      this.field271 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (!isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field321)) {
         this.field273.reset();
      } else if (Gui.INSTANCE.field2355.getValue() == 0.0 || this.field273.hasElapsed(Gui.INSTANCE.field2355.getValue() * 1000.0)) {
         ClickGUI.field1335
            .method581(
               this.field271.desc,
               this.field318 - (double)Theme.method1365() * scaleFactor - (this.field271.parent == null ? scaleFactor * 3.0 : scaleFactor * 3.0 * 2.0),
               this.field318 + this.field320 + (double)Theme.method1365() * scaleFactor
            );
         this.field271.descriptionSeen = true;
      }
   }
}
