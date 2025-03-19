package dev.boze.client.gui.screens.account;

import dev.boze.client.Boze;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.AccountManagerComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.AccountSetting;
import dev.boze.client.systems.accounts.types.TheAlteningAccount;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class5928;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

public class AlteningAccountScreen extends ScaledBaseComponent {
   private final AccountSetting field1470;
   private String field1471;

   public AlteningAccountScreen(AccountSetting setting) {
      super(setting.name, 0.2, 0.2);
      this.field1470 = setting;
      this.field1471 = "";
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.834);
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
            "Add altening account",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Add altening account") * 0.5,
            this.field1389 + this.field1391 * 0.15 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      double var9 = this.field1388 + this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5;
      String var11 = this.field1471 + (System.currentTimeMillis() % 1500L < 750L ? "|" : "");
      RenderUtil.field3963
         .method2257(
            var9 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 3.0,
            this.field1390 - (this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5 - BaseComponent.scaleFactor * 3.0) * 2.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1348()
         );
      IFontRender.method499().drawShadowedText(var11, var9, this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390(), Theme.method1350());
      if (this.field1471.isEmpty()) {
         IFontRender.method499()
            .drawShadowedText("Token", var9, this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390(), new RGBAColor(1157627903));
      }

      if (isMouseWithinBounds(
         (double)mouseX,
         (double)mouseY,
         var9 - BaseComponent.scaleFactor * 3.0,
         this.field1389 + this.field1391 * 0.5 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 3.0,
         this.field1390 - (this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5 - BaseComponent.scaleFactor * 3.0) * 2.0,
         IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
      )) {
         ClickGUI.field1335.field1337 = CursorType.IBeam;
      }

      RenderUtil.field3963
         .method2257(
            var9 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
            IFontRender.method499().method501("Cancel") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + this.field1390 * 0.75 - IFontRender.method499().method501("Cancel") * 0.5 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
            IFontRender.method499().method501("Cancel") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Cancel",
            this.field1388 + this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Add",
            this.field1388 + this.field1390 * 0.75 - IFontRender.method499().method501("Add") * 0.5,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      RenderUtil.field3963.method2235(context);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.834, true);
         double var9 = this.field1388 + this.field1390 * 0.25 - IFontRender.method499().method501("Cancel") * 0.5;
         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            var9 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
            IFontRender.method499().method501("Cancel") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new AccountManagerComponent(this.field1470));
         } else if (isMouseWithinBounds(
               mouseX,
               mouseY,
               this.field1388 + this.field1390 * 0.75 - IFontRender.method499().method501("Cancel") * 0.5 - BaseComponent.scaleFactor * 3.0,
               this.field1389 + this.field1391 * 0.75 - IFontRender.method499().method1390() / 2.0 - BaseComponent.scaleFactor * 3.0,
               IFontRender.method499().method501("Cancel") + BaseComponent.scaleFactor * 6.0,
               IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
            )
            && !this.field1471.isEmpty()) {
            TheAlteningAccount var11 = new TheAlteningAccount(this.field1471);
            if (!Boze.getAccounts().method1131(var11)) {
               AccountManagerComponent.method642(var11, this.field1470);
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               ClickGUI.field1335.method580(new AccountManagerComponent(this.field1470));
            }
         }

         IFontRender.method499().endBuilding();
         return true;
      } else {
         return super.isMouseOver(mouseX, mouseY, button);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      try {
         if (keyCode == 259) {
            this.field1471 = this.field1471.substring(0, this.field1471.length() - 1);
         } else if (keyCode == 257) {
            if (!this.field1471.isEmpty()) {
               TheAlteningAccount var7 = new TheAlteningAccount(this.field1471);
               if (!Boze.getAccounts().method1131(var7)) {
                  AccountManagerComponent.method642(var7, this.field1470);
                  mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                  ClickGUI.field1335.method580(new AccountManagerComponent(this.field1470));
               }
            }
         } else if (keyCode == 86 && Class5928.method159(341)) {
            String var9 = GLFW.glfwGetClipboardString(mc.getWindow().getHandle());
            if (var9 != null && !var9.isEmpty()) {
               var9.chars().forEach(this::lambda$keyPressed$0);
            }
         }
      } catch (Exception var8) {
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public void method583(char c) {
      if (this.field1471.length() < 26 && c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_' || c == '-' || c == '.' || c == '@') {
         this.field1471 = this.field1471 + c;
      }
   }

   private void lambda$keyPressed$0(int var1) {
      this.method583((char)var1);
   }
}
