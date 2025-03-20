package dev.boze.client.gui.components.setting.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.PNGComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.StringSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class StringSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
   private final StringSetting field314;

   public StringSettingComponent(StringSetting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field314 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field314.method2116()) {
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
         double var8 = 0.0;
         if (this.field314.hasChildren()) {
            double var10 = this.field321 / 9.0;
            double var12 = this.field318 + this.field320 - scaleFactor * 6.0 - var10;
            if (this.field314.isExpanded()) {
               RenderUtil.field3963.method2257(var12, this.field319 + var10 * 2.0, var10, var10 * 5.0, 15, 12, var10 / 2.0, Theme.method1350());
            } else {
               RenderUtil.field3963.method2261(var12, this.field319 + var10 * 2.0, var10, Theme.method1350());
               RenderUtil.field3963.method2261(var12, this.field319 + var10 * 4.0, var10, Theme.method1350());
               RenderUtil.field3963.method2261(var12, this.field319 + var10 * 6.0, var10, Theme.method1350());
            }

            var8 = scaleFactor * 6.0 + var10 / 2.0;
         }

         Notifications.VIEW_LIST
            .render(
               this.field318 + this.field320 - scaleFactor * 6.0 - var8 - Notifications.VIEW_LIST.method2091(),
               this.field319 + this.field321 * 0.5 - Notifications.VIEW_LIST.method1614() * 0.5,
               Theme.method1350()
            );
         if (!this.field314.getValue().isEmpty()) {
            IFontRender.method499()
               .drawShadowedText(
                  this.field314.getValue(),
                  this.field318 + this.field320 - scaleFactor * 21.0 - var8 - IFontRender.method499().method501(this.field314.getValue()),
                  this.field319 + this.field321 * 0.5 - IFontRender.method499().method1390() * 0.5,
                  Theme.method1350()
               );
         }
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field314.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
         if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new PNGComponent(this.field314));
            return true;
         }

         if (button == 1 && this.field314.hasChildren()) {
            this.field314.setExpanded(!this.field314.isExpanded());
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }
}
