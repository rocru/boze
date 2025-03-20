package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ModuleComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class SettingBlockComponent extends ScaledSettingBaseComponent implements IMinecraft {
   final SettingBlock field400;

   public SettingBlockComponent(SettingBlock setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field400 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field400.method2116()) {
         this.field321 = (double)Theme.method1376() * scaleFactor;
         if (!isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field321)) {
            this.field273.reset();
         } else if (Gui.INSTANCE.field2355.getValue() == 0.0 || this.field273.hasElapsed(Gui.INSTANCE.field2355.getValue() * 1000.0)) {
            ClickGUI.field1335
               .method581(
                  ((ModuleComponent)this.field317).field343 == this.field400 ? "Go back to all settings" : this.field271.desc,
                  this.field318 - (double)Theme.method1365() * scaleFactor - (this.field271.parent == null ? scaleFactor * 3.0 : scaleFactor * 3.0 * 2.0),
                  this.field318 + this.field320 + (double)Theme.method1365() * scaleFactor
               );
            this.field271.descriptionSeen = true;
         }

         RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
         if (((ModuleComponent)this.field317).field343 == this.field400) {
            IFontRender.method499()
               .drawShadowedText(
                  "Back",
                  this.field318 + 6.0 * scaleFactor,
                  this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
                  Theme.method1350()
               );
            Notifications.EXPAND_LESS
               .render(
                  this.field318 + this.field320 - scaleFactor * 6.0 - Notifications.EXPAND_LESS.method2091(),
                  this.field319 + this.field321 * 0.5 - IconManager.method1116() * 0.5,
                  Theme.method1350()
               );
         } else {
            IFontRender.method499()
               .drawShadowedText(
                  this.field316,
                  this.field318 + 6.0 * scaleFactor,
                  this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
                  Theme.method1350()
               );
            Notifications.VIEW_LIST
               .render(
                  this.field318 + this.field320 - scaleFactor * 6.0 - Notifications.VIEW_LIST.method2091(),
                  this.field319 + this.field321 * 0.5 - IconManager.method1116() * 0.5,
                  Theme.method1350()
               );
         }
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field400.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
         if (button == 0) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if (((ModuleComponent)this.field317).field343 == this.field400) {
               ((ModuleComponent)this.field317).field343.setValue(false);
               ((ModuleComponent)this.field317).field343 = null;
            } else {
               ((ModuleComponent)this.field317).field343 = this.field400;
               ((ModuleComponent)this.field317).field343.setValue(true);
            }

            return true;
         }

         if (button == 1 && this.field400.hasChildren()) {
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }
}
