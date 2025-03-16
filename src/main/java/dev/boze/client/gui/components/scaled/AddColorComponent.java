package dev.boze.client.gui.components.scaled;

import dev.boze.client.enums.ColorTypes;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.WeirdColorSettingComponent;
import dev.boze.client.gui.components.text.eW;
import dev.boze.client.gui.components.text.eX;
import dev.boze.client.gui.components.text.eY;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.WeirdColorSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;

public class AddColorComponent extends ScaledBaseComponent {
   private final ArrayList<InputBaseComponent> field1480 = new ArrayList();
   private final String field1481;

   public AddColorComponent(ScaledBaseComponent previousPopup, String colorName) {
      super("Add Color", 0.0, 0.0);
      this.field1481 = colorName;
      double var6 = 20.0 * BaseComponent.scaleFactor;
      double var8 = 6.0 * BaseComponent.scaleFactor;
      boolean var10 = true;
      if (previousPopup instanceof WeirdColorSettingComponent) {
         WeirdColorSetting var11 = ((WeirdColorSettingComponent)previousPopup).method645();
         var10 = var11.method428() == ColorTypes.ALL;
      }

      int var17 = var10 ? 3 : 2;
      int var12 = var17 + 1;
      this.field1391 = var8 * (double)var12 + var6 + var6 * (double)var17;
      this.field1390 = 100.0 * BaseComponent.scaleFactor;
      this.field1388 = (double)mc.getWindow().getScaledWidth() * 0.5 - this.field1390 * 0.5;
      this.field1389 = (double)mc.getWindow().getScaledHeight() * 0.5 - this.field1391 * 0.5;
      double var13 = this.field1390 - var8 * 2.0;
      double var15 = this.field1389 + var8 + var6;
      this.field1480.add(new eW(this, "Static", this.field1388 + var8, var15, var13, var6, colorName, previousPopup));
      this.field1480.add(new eX(this, "Changing", this.field1388 + var8, var15 + var6 + var8, var13, var6, colorName, previousPopup));
      if (var10) {
         this.field1480.add(new eY(this, "Gradient", this.field1388 + var8, var15 + (var6 + var8) * 2.0, var13, var6, colorName, previousPopup));
      }
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
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

      for (InputBaseComponent var9 : this.field1480) {
         var9.render(context, mouseX, mouseY, delta);
      }

      RenderUtil.field3963.method2235(context);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         return false;
      } else {
         for (InputBaseComponent var10 : this.field1480) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
               break;
            }
         }

         return true;
      }
   }
}
