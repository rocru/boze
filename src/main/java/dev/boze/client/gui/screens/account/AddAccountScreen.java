package dev.boze.client.gui.screens.account;

import dev.boze.client.Boze;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.AccountManagerComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.AccountSetting;
import dev.boze.client.systems.accounts.MicrosoftLogin;
import dev.boze.client.systems.accounts.types.MicrosoftAccount;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class AddAccountScreen extends ScaledBaseComponent {
   private final AccountSetting field1466;
   private double field1467;

   public AddAccountScreen(AccountSetting setting) {
      super("Add Account", 0.1, 0.2);
      this.field1466 = setting;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
      this.field1467 = (this.field1391 - BaseComponent.scaleFactor * 36.0 - IFontRender.method499().method1390()) / 4.0;
      RenderUtil.field3963
         .method2257(
            this.field1388,
            this.field1389,
            this.field1390,
            this.field1391,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1349()
         );
      if (Theme.method1382()) {
         ClickGUI.field1335
            .field1333
            .method2257(
               this.field1388,
               this.field1389,
               this.field1390,
               this.field1391,
               15,
               24,
               Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
               RGBAColor.field402
            );
      }

      IFontRender.method499()
         .drawShadowedText(
            this.field1387,
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501(this.field1387) * 0.5,
            this.field1389 + BaseComponent.scaleFactor * 6.0,
            Theme.method1350()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + BaseComponent.scaleFactor * 12.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Microsoft",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Microsoft") * 0.5,
            this.field1389
               + BaseComponent.scaleFactor * 12.0
               + IFontRender.method499().method1390()
               + this.field1467 * 0.5
               - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + this.field1467 + BaseComponent.scaleFactor * 18.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Altening",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Altening") * 0.5,
            this.field1389
               + this.field1467
               + BaseComponent.scaleFactor * 18.0
               + IFontRender.method499().method1390()
               + this.field1467 * 0.5
               - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + this.field1467 * 2.0 + BaseComponent.scaleFactor * 24.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Cracked",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Cracked") * 0.5,
            this.field1389
               + this.field1467 * 2.0
               + BaseComponent.scaleFactor * 24.0
               + IFontRender.method499().method1390()
               + this.field1467 * 0.5
               - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + this.field1467 * 3.0 + BaseComponent.scaleFactor * 30.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Cancel",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Cancel") * 0.5,
            this.field1389
               + this.field1467 * 3.0
               + BaseComponent.scaleFactor * 30.0
               + IFontRender.method499().method1390()
               + this.field1467 * 0.5
               - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      RenderUtil.field3963.method2235(context);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5, true);
         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + this.field1467 * 3.0 + BaseComponent.scaleFactor * 30.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new AccountManagerComponent(this.field1466));
         } else if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + this.field1467 * 2.0 + BaseComponent.scaleFactor * 24.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new CrackedAccountScreeen(this.field1466));
         } else if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + this.field1467 + BaseComponent.scaleFactor * 18.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new AlteningAccountScreen(this.field1466));
         } else if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + BaseComponent.scaleFactor * 6.0,
            this.field1389 + BaseComponent.scaleFactor * 12.0 + IFontRender.method499().method1390(),
            this.field1390 - BaseComponent.scaleFactor * 12.0,
            this.field1467
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
            MicrosoftLogin.method1316(this::lambda$mouseClicked$0);
         }

         IFontRender.method499().endBuilding();
         return true;
      } else {
         return super.isMouseOver(mouseX, mouseY, button);
      }
   }

   private void lambda$mouseClicked$0(String var1) {
      if (var1 != null) {
         MicrosoftAccount var5 = new MicrosoftAccount(var1);
         if (!Boze.getAccounts().method1131(var5)) {
            AccountManagerComponent.method642(var5, this.field1466);
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new AccountManagerComponent(this.field1466));
         }
      }
   }
}
