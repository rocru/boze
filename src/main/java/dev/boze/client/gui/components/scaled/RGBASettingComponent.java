package dev.boze.client.gui.components.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.rotation.fE;
import dev.boze.client.gui.components.slider.floats.array.gc;
import dev.boze.client.gui.components.slider.ints.f0;
import dev.boze.client.gui.components.slider.ints.f1;
import dev.boze.client.gui.components.slider.ints.fW;
import dev.boze.client.gui.components.slider.ints.fX;
import dev.boze.client.gui.components.slider.ints.fY;
import dev.boze.client.gui.components.slider.ints.fZ;
import dev.boze.client.gui.components.text.e5;
import dev.boze.client.gui.components.text.e6;
import dev.boze.client.gui.components.text.e7;
import dev.boze.client.gui.components.text.e8;
import dev.boze.client.gui.components.toggle.ToggleRGBASettingComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;

public class RGBASettingComponent extends ScaledBaseComponent {
   public static RGBAColor field1482 = null;
   private static final double field1483 = 6.0;
   private final RGBASetting field1484;
   private final ArrayList<InputBaseComponent> field1485 = new ArrayList<>();
   private final ArrayList<InputBaseComponent> field1486 = new ArrayList<>();
   private final ArrayList<InputBaseComponent> field1487 = new ArrayList<>();

   public RGBASettingComponent(RGBASetting setting) {
      super(setting.name, 218.0 * BaseComponent.scaleFactor, 128.0 * BaseComponent.scaleFactor, true);
      this.field1484 = setting;
      this.field1485
         .add(
            new gc(
               this,
               "SB",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 6.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               46.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new fW(
               this,
               "Red",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 6.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new fX(
               this,
               "Green",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 30.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new fY(
               this,
               "Blue",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 54.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new fZ(
               this,
               "Blue",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 54.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new f0(
               this,
               "Opacity",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 78.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new ToggleRGBASettingComponent(
               this,
               "Rainbow",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 82.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               14.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1486
         .add(
            new fE(
               this,
               "Hue",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 56.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1487
         .add(
            new f1(
               this,
               "Speed",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 56.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new e5(
               this,
               "Copy",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 102.0 * BaseComponent.scaleFactor,
               47.0 * BaseComponent.scaleFactor,
               20.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new e6(
               this,
               "Paste",
               this.field1388 + 59.0 * BaseComponent.scaleFactor,
               this.field1389 + 102.0 * BaseComponent.scaleFactor,
               47.0 * BaseComponent.scaleFactor,
               20.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new e7(
               this,
               "Reset",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 102.0 * BaseComponent.scaleFactor,
               47.0 * BaseComponent.scaleFactor,
               20.0 * BaseComponent.scaleFactor,
               setting
            )
         );
      this.field1485
         .add(
            new e8(
               this,
               "Apply",
               this.field1388 + 165.0 * BaseComponent.scaleFactor,
               this.field1389 + 102.0 * BaseComponent.scaleFactor,
               47.0 * BaseComponent.scaleFactor,
               20.0 * BaseComponent.scaleFactor
            )
         );
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      RenderUtil.field3963.method2233();
      RenderUtil.field3966.method2233();
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

      for (InputBaseComponent var9 : this.field1485) {
         var9.render(context, mouseX, mouseY, delta);
      }

      if (this.field1484.method2118()) {
         for (InputBaseComponent var12 : this.field1487) {
            var12.render(context, mouseX, mouseY, delta);
         }
      } else {
         for (InputBaseComponent var13 : this.field1486) {
            var13.render(context, mouseX, mouseY, delta);
         }
      }

      RenderUtil.field3963.method2235(context);
      RenderUtil.field3966.method2235(context);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         return false;
      } else {
         for (InputBaseComponent var10 : this.field1485) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
               break;
            }
         }

         if (this.field1484.method2118()) {
            for (InputBaseComponent var13 : this.field1487) {
               var13.mouseClicked(mouseX, mouseY, button);
            }
         } else {
            for (InputBaseComponent var14 : this.field1486) {
               var14.mouseClicked(mouseX, mouseY, button);
            }
         }

         return true;
      }
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         return false;
      } else {
         for (InputBaseComponent var14 : this.field1485) {
            if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
               break;
            }
         }

         if (this.field1484.method2118()) {
            for (InputBaseComponent var17 : this.field1487) {
               var17.onDrag(mouseX, mouseY, button, deltaX, deltaY);
            }
         } else {
            for (InputBaseComponent var18 : this.field1486) {
               var18.onDrag(mouseX, mouseY, button, deltaX, deltaY);
            }
         }

         return true;
      }
   }
}
