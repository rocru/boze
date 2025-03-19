package dev.boze.client.gui.colorpanel;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.colorpanel.action.CopyComponent;
import dev.boze.client.gui.colorpanel.action.PasteComponent;
import dev.boze.client.gui.colorpanel.action.ResetComponent;
import dev.boze.client.gui.colorpanel.gradient.MaxHueComponent;
import dev.boze.client.gui.colorpanel.gradient.MinHueComponent;
import dev.boze.client.gui.colorpanel.main.*;
import dev.boze.client.gui.colorpanel.secondary.HuiComponent;
import dev.boze.client.gui.colorpanel.secondary.OpacityComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.manager.ColorManager;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class ColorPanel extends InputBaseComponent {
   private static final double field1138 = 6.0;
   private final ColorManager field1139;
   private final ArrayList<InputBaseComponent> field1140 = new ArrayList();
   private final ArrayList<InputBaseComponent> field1141 = new ArrayList();
   private final ArrayList<InputBaseComponent> field1142 = new ArrayList();

   public ColorPanel(String name, ColorManager parameters, double x, double y, double width, double height) {
      super(name, x, y, width, height);
      this.field1139 = parameters;
      this.field1140.add(new GradientComponent(this, "Gradient", x + 6.0 * field1131, y + 6.0 * field1131, 100.0 * field1131, 14.0 * field1131, parameters));
      this.field1140.add(new SpeedComponent(this, "Speed", x + 6.0 * field1131, y + 26.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1140.add(new OffsetComponent(this, "Offset", x + 6.0 * field1131, y + 50.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1140
         .add(
            new StrengthComponent(
               this, "Strength", x + 112.0 * field1131 + field1131, y + 6.0 * field1131 + field1131, 66.0 * field1131, 66.0 * field1131, parameters
            )
         );
      this.field1140.add(new SBComponent(this, "SB", x + 186.0 * field1131, y + 6.0 * field1131, 132.0 * field1131, 68.0 * field1131, parameters));
      this.field1141.add(new HuiComponent(this, "Hue", x + 6.0 * field1131, y + 80.0 * field1131, 153.0 * field1131, 18.0 * field1131, parameters));
      this.field1141.add(new OpacityComponent(this, "Opacity", x + 165.0 * field1131, y + 80.0 * field1131, 153.0 * field1131, 18.0 * field1131, parameters));
      this.field1142.add(new MinHueComponent(this, "Min Hue", x + 6.0 * field1131, y + 80.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1142.add(new MaxHueComponent(this, "Max Hue", x + 112.0 * field1131, y + 80.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1142
         .add(
            new dev.boze.client.gui.colorpanel.gradient.OpacityComponent(
               this, "Opacity", x + 218.0 * field1131, y + 80.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters
            )
         );
      this.field1140.add(new RedComponent(this, "Red", x + 6.0 * field1131, y + 104.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1140.add(new GreenComponent(this, "Green", x + 112.0 * field1131, y + 104.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1140.add(new BlueComponent(this, "Blue", x + 218.0 * field1131, y + 104.0 * field1131, 100.0 * field1131, 18.0 * field1131, parameters));
      this.field1140.add(new CopyComponent(this, "Copy", x + 6.0 * field1131, y + 128.0 * field1131, 100.0 * field1131, 20.0 * field1131, parameters));
      this.field1140.add(new PasteComponent(this, "Paste", x + 112.0 * field1131, y + 128.0 * field1131, 100.0 * field1131, 20.0 * field1131, parameters));
      this.field1140.add(new ResetComponent(this, "Reset", x + 218.0 * field1131, y + 128.0 * field1131, 100.0 * field1131, 20.0 * field1131, parameters));
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      RenderUtil.field3963.method2233();
      RenderUtil.field3966.method2233();
      IFontRender.method499().startBuilding(field1131 * 0.5);

      for (InputBaseComponent var9 : this.field1140) {
         var9.render(context, mouseX, mouseY, delta);
      }

      if (!this.field1139.method1374().field1842 && this.field1139.method1374().field1843 == 0.0) {
         for (InputBaseComponent var13 : this.field1141) {
            var13.render(context, mouseX, mouseY, delta);
         }
      } else {
         for (InputBaseComponent var12 : this.field1142) {
            var12.render(context, mouseX, mouseY, delta);
         }
      }

      RenderUtil.field3963.method2235(context);
      RenderUtil.field3966.method2235(context);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
         return false;
      } else {
         for (InputBaseComponent var10 : this.field1140) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
               break;
            }
         }

         if (!this.field1139.method1374().field1842 && this.field1139.method1374().field1843 == 0.0) {
            for (InputBaseComponent var14 : this.field1141) {
               var14.mouseClicked(mouseX, mouseY, button);
            }
         } else {
            for (InputBaseComponent var13 : this.field1142) {
               var13.mouseClicked(mouseX, mouseY, button);
            }
         }

         return true;
      }
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
         return false;
      } else {
         for (InputBaseComponent var14 : this.field1140) {
            if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
               break;
            }
         }

         if (!this.field1139.method1374().field1842 && this.field1139.method1374().field1843 == 0.0) {
            for (InputBaseComponent var18 : this.field1141) {
               var18.onDrag(mouseX, mouseY, button, deltaX, deltaY);
            }
         } else {
            for (InputBaseComponent var17 : this.field1142) {
               var17.onDrag(mouseX, mouseY, button, deltaX, deltaY);
            }
         }

         return true;
      }
   }
}
