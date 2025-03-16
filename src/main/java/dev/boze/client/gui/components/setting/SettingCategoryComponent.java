package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class SettingCategoryComponent extends ScaledSettingBaseComponent {
   private final SettingCategory field315;

   public SettingCategoryComponent(SettingCategory setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field315 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field315.method2116()) {
         this.field321 = (double)Theme.method1376() * scaleFactor;
         super.render(context, mouseX, mouseY, delta);
         RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
         IFontRender.method499()
            .drawShadowedText(
               this.field316,
               this.field318 + 6.0 * scaleFactor,
               this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         double var8 = this.field321 / 9.0;
         double var10 = this.field318 + this.field320 - scaleFactor * 6.0 - var8;
         if (this.field315.isExpanded()) {
            RenderUtil.field3963.method2257(var10, this.field319 + var8 * 2.0, var8, var8 * 5.0, 15, 12, var8 / 2.0, Theme.method1350());
         } else {
            RenderUtil.field3963.method2261(var10, this.field319 + var8 * 2.0, var8, Theme.method1350());
            RenderUtil.field3963.method2261(var10, this.field319 + var8 * 4.0, var8, Theme.method1350());
            RenderUtil.field3963.method2261(var10, this.field319 + var8 * 6.0, var8, Theme.method1350());
         }
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field315.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
         this.field315.setExpanded(!this.field315.isExpanded());
         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }
}
