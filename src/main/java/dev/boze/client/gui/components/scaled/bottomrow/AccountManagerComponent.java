package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.gui.screens.account.AddAccountScreen;
import dev.boze.client.manager.AccountManager;
import dev.boze.client.settings.AccountSetting;
import dev.boze.client.systems.accounts.Account;
import dev.boze.client.systems.accounts.AccountCache;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.network.BozeExecutor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class27;
import net.minecraft.client.gui.DrawContext;

public class AccountManagerComponent extends BottomRowScaledComponent {
   private final AccountSetting field1447;
   private final AccountManager field1448;

   public AccountManagerComponent(AccountSetting setting) {
      super(setting.name, BottomRow.AddClose, 0.15, 0.4);
      this.field1447 = setting;
      this.field1448 = Class27.getAccounts();
   }

   @Override
   protected int method2010() {
      return this.field1448.method1133();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      Account var13 = this.field1448.method1129(index);
      String var14 = "[" + var13.method673().toString().substring(0, 1) + "] " + var13.method210();
      boolean var15 = var13.method210().equals(this.field1447.method1322());
      RenderUtil.field3963
         .method2257(
            itemX,
            itemY,
            itemWidth,
            itemHeight,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            var15 ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            var14, itemX + BaseComponent.scaleFactor * 18.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
         );
      Notifications.DELETE
         .render(
            itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091(),
            itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5,
            Theme.method1350()
         );
   }

   @Override
   protected void method640(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      Account var14 = this.field1448.method1129(index);
      RenderUtil.field3967.method2233();
      AccountCache var15 = var14.method674();
      RenderUtil.field3967
         .method2270(
            itemX + BaseComponent.scaleFactor * 6.0,
            itemY + itemHeight * 0.5 - BaseComponent.scaleFactor * 4.0,
            BaseComponent.scaleFactor * 8.0,
            BaseComponent.scaleFactor * 8.0,
            var15.method551().method2115() ? 90.0 : 0.0,
            0.0,
            0.0,
            1.0,
            1.0,
            RGBAColor.field402
         );
      var15.method551().method2142();
      RenderUtil.field3967.method2235(context);
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      Account var18 = this.field1448.method1129(index);
      double var19 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
      if (isMouseWithinBounds(
         mouseX,
         mouseY,
         var19,
         itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5,
         Notifications.DELETE.method2091(),
         Notifications.DELETE.method1614()
      )) {
         if (var18.method210().equals(this.field1447.method1322())) {
            this.field1447.method1341("");
         }

         this.field1448.method1132(var18);
         return true;
      } else {
         BozeExecutor.method2200(this::lambda$handleItemClick$0);
         return true;
      }
   }

   @Override
   protected void method1904() {
      ClickGUI.field1335.method580(new AddAccountScreen(this.field1447));
   }

   static void method642(Account<?> var0, AccountSetting var1) {
      BozeExecutor.method2200(AccountManagerComponent::lambda$addAccount$1);
   }

   private static void lambda$addAccount$1(Account var0, AccountSetting var1) {
      if (var0.method2114()) {
         Class27.getAccounts().method1130(var0);
         if (var0.method2115()) {
            var1.method1341(var0.method210());
            Class27.getPlayerManager().method2142();
         }
      }
   }

   private void lambda$handleItemClick$0(Account var1) {
      if (var1.method2115()) {
         this.field1447.method1341(var1.method210());
         Class27.getPlayerManager().method2142();
      }
   }
}
