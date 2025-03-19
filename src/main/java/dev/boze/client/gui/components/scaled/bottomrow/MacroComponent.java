package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.Boze;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.MacroSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.Macro;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class MacroComponent extends BottomRowScaledComponent {
   private final MacroSetting field1452;
   private ArrayList<String> field1453 = new ArrayList();

   public MacroComponent(MacroSetting setting) {
      super(setting.name, BottomRow.AddClose, 0.1, 0.4);
      this.field1452 = setting;

      for (Macro var6 : Boze.getMacros().field2140) {
         this.field1453.add(var6.field1048);
      }
   }

   @Override
   protected int method2010() {
      return this.field1453.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var13 = (String)this.field1453.get(index);
      RenderUtil.field3963
         .method2257(
            itemX,
            itemY,
            itemWidth,
            itemHeight,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            var13.equals(this.field1452.method1322()) ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            var13, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
         );
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      this.field1452.method1341((String)this.field1453.get(index));
      return true;
   }

   @Override
   protected void method1904() {
   }

   @Override
   protected void method1198() {
      if (this.field1452.method1322().isEmpty()) {
         double var4 = IFontRender.method499().method501("Close") + BaseComponent.scaleFactor * 6.0;
         double var6 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
         double var8 = this.field1389 + this.field1391 - var6 - BaseComponent.scaleFactor * 6.0;
         double var10 = this.field1388 + this.field1390 * 0.5 - var4 * 0.5;
         RenderUtil.field3963.method2257(var10, var8, var4, var6, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
         IFontRender.method499()
            .drawShadowedText(
               "Close",
               var10 + var4 * 0.5 - IFontRender.method499().method501("Close") * 0.5,
               var8 + var6 * 0.5 - IFontRender.method499().method1390() * 0.5,
               Theme.method1350()
            );
      } else {
         double var14 = Math.max(IFontRender.method499().method501("Unselect"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
         double var15 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
         double var16 = this.field1389 + this.field1391 - var15 - BaseComponent.scaleFactor * 6.0;
         double var17 = this.field1388 + this.field1390 * 0.25 - var14 * 0.5;
         RenderUtil.field3963.method2257(var17, var16, var14, var15, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
         IFontRender.method499()
            .drawShadowedText(
               "Unselect",
               var17 + var14 * 0.5 - IFontRender.method499().method501("Unselect") * 0.5,
               var16 + var15 * 0.5 - IFontRender.method499().method1390() * 0.5,
               Theme.method1350()
            );
         double var12 = this.field1388 + this.field1390 * 0.75 - var14 * 0.5;
         RenderUtil.field3963.method2257(var12, var16, var14, var15, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
         IFontRender.method499()
            .drawShadowedText(
               "Close",
               var12 + var14 * 0.5 - IFontRender.method499().method501("Close") * 0.5,
               var16 + var15 * 0.5 - IFontRender.method499().method1390() * 0.5,
               Theme.method1350()
            );
      }
   }

   @Override
   protected void handleAddAndCloseClick(double mouseX, double mouseY) {
      double var8 = Math.max(IFontRender.method499().method501("Unselect"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
      double var10 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
      double var12 = this.field1389 + this.field1391 - var10 - BaseComponent.scaleFactor * 6.0;
      double var14 = this.field1388 + this.field1390 * 0.25 - var8 * 0.5;
      if (isMouseWithinBounds(mouseX, mouseY, var14, var12, var8, var10) && !this.field1452.method1322().isEmpty()) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         this.field1452.method1341("");
      } else {
         double var16 = this.field1452.method1322().isEmpty()
            ? this.field1388 + this.field1390 * 0.5 - var8 * 0.5
            : this.field1388 + this.field1390 * 0.75 - var8 * 0.5;
         if (isMouseWithinBounds(mouseX, mouseY, var16, var12, var8, var10)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(null);
         }
      }
   }
}
