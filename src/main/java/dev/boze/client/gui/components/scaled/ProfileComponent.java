package dev.boze.client.gui.components.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.text.ProfileTextComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class5907;
import mapped.Class5908;
import mapped.Class5911;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class ProfileComponent extends ScaledBaseComponent {
   private final double field1416 = 6.0 * BaseComponent.scaleFactor;
   private final ArrayList<InputBaseComponent> field1417 = new ArrayList();
   private final Class5911 field1418;

   public ProfileComponent(CurrentProfileSetting setting) {
      super(setting.name, 150.0 * BaseComponent.scaleFactor, 300.0 * BaseComponent.scaleFactor, true);
      this.field1417
         .add(
            new Class5908(
               "Profiles",
               this.field1388 + this.field1416,
               this.field1389 + this.field1416,
               this.field1390 - this.field1416 * 2.0,
               10.0 * BaseComponent.scaleFactor
            )
         );
      this.field1417
         .add(
            new ProfileTextComponent(
               this,
               "Close",
               this.field1388 + this.field1390 * 0.375,
               this.field1389 + this.field1391 - 15.0 * BaseComponent.scaleFactor - this.field1416,
               this.field1390 * 0.25,
               15.0 * BaseComponent.scaleFactor
            )
         );
      this.field1417
         .add(
            new Class5907(
               "New profile...",
               this.field1388 + this.field1416 * 2.0,
               this.field1389 + this.field1391 - 35.0 * BaseComponent.scaleFactor - this.field1416 * 2.0,
               this.field1390 - this.field1416 * 4.0,
               20.0 * BaseComponent.scaleFactor
            )
         );
      double var4 = this.field1391 - this.field1416 * 5.0 - 45.0 * BaseComponent.scaleFactor;
      this.field1418 = new Class5911(
         "Scroll",
         this.field1388 + this.field1416 * 2.0,
         this.field1389 + this.field1416 * 2.0 + 10.0 * BaseComponent.scaleFactor,
         this.field1390 - this.field1416 * 4.0,
         var4
      );
      this.field1417.add(this.field1418);
   }

   @Override
   public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
      super.render(matrices, mouseX, mouseY, delta);
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

      for (InputBaseComponent var9 : this.field1417) {
         var9.render(matrices, mouseX, mouseY, delta);
      }

      RenderUtil.field3963.method2235(matrices);
      IFontRender.method499().endBuilding();
   }
}
