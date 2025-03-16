package dev.boze.client.gui.components.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.rotation.fI;
import dev.boze.client.gui.components.slider.floats.array.ge;
import dev.boze.client.gui.components.slider.ints.f7;
import dev.boze.client.gui.components.slider.ints.f8;
import dev.boze.client.gui.components.slider.ints.f9;
import dev.boze.client.gui.components.text.fd;
import dev.boze.client.gui.components.text.fe;
import dev.boze.client.gui.components.text.ff;
import dev.boze.client.gui.components.text.fg;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Colors;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.color.StaticColor;
import java.util.ArrayList;
import mapped.Class2775;
import net.minecraft.client.gui.DrawContext;

public class NewColorComponent extends ScaledBaseComponent {
   public static StaticColor field1488 = null;
   private static final double field1489 = 6.0;
   private final ArrayList<InputBaseComponent> field1490 = new ArrayList();

   public NewColorComponent(String colorName, StaticColor color, ScaledBaseComponent previousPopup, Class2775 saveCallback) {
      super("Edit " + colorName, 218.0 * BaseComponent.scaleFactor, 104.0 * BaseComponent.scaleFactor, true);
      this.field1490
         .add(
            new ge(
               this,
               "SB",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 6.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               46.0 * BaseComponent.scaleFactor,
               color
            )
         );
      this.field1490
         .add(
            new fI(
               this,
               "Hue",
               this.field1388 + 6.0 * BaseComponent.scaleFactor,
               this.field1389 + 54.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               color
            )
         );
      this.field1490
         .add(
            new f7(
               this,
               "Red",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 6.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               color
            )
         );
      this.field1490
         .add(
            new f8(
               this,
               "Green",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 30.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               color
            )
         );
      this.field1490
         .add(
            new f9(
               this,
               "Blue",
               this.field1388 + 112.0 * BaseComponent.scaleFactor,
               this.field1389 + 54.0 * BaseComponent.scaleFactor,
               100.0 * BaseComponent.scaleFactor,
               18.0 * BaseComponent.scaleFactor,
               color
            )
         );
      double var8 = field1488 != null ? 35.0 * BaseComponent.scaleFactor : 47.0 * BaseComponent.scaleFactor;
      double var10 = 20.0 * BaseComponent.scaleFactor;
      double var12 = var8 * (double)(field1488 != null ? 4 : 3) + (field1488 != null ? 18.0 : 12.0) * BaseComponent.scaleFactor;
      double var14 = this.field1388 + (this.field1390 - var12) / 2.0;
      double var16 = this.field1389 + this.field1391 - 26.0 * BaseComponent.scaleFactor;
      this.field1490.add(new fd(this, "Copy", var14, var16, var8, var10, color));
      if (field1488 != null) {
         this.field1490.add(new fe(this, "Paste", var14 + var8 + 6.0 * BaseComponent.scaleFactor, var16, var8, var10, color));
      }

      this.field1490
         .add(
            new ff(
               this,
               "Cancel",
               var14 + var8 * (double)(field1488 != null ? 2 : 1) + 6.0 * (double)(field1488 != null ? 2 : 1) * BaseComponent.scaleFactor,
               var16,
               var8,
               var10,
               previousPopup
            )
         );
      this.field1490
         .add(
            new fg(
               this,
               "Save",
               var14 + var8 * (double)(field1488 != null ? 3 : 2) + 6.0 * (double)(field1488 != null ? 3 : 2) * BaseComponent.scaleFactor,
               var16,
               var8,
               var10,
               saveCallback,
               color,
               previousPopup
            )
         );
   }

   public NewColorComponent(String colorName, StaticColor color, ScaledBaseComponent previousPopup) {
      this(colorName, color, previousPopup, NewColorComponent::lambda$new$1);
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

      for (InputBaseComponent var9 : this.field1490) {
         var9.render(context, mouseX, mouseY, delta);
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
         for (InputBaseComponent var10 : this.field1490) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
               break;
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
         for (InputBaseComponent var14 : this.field1490) {
            if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
               break;
            }
         }

         return true;
      }
   }

   private static void lambda$new$1(ScaledBaseComponent var0, String var1, StaticColor var2) {
      if (var0 instanceof BottomRowScaledComponent var6) {
         ClickGUI.field1335.method580(var6);
         var6.method635(NewColorComponent::lambda$new$0);
      }
   }

   private static void lambda$new$0(String var0, StaticColor var1) {
      Colors.INSTANCE.field2343.put(var0, var1);
   }
}
