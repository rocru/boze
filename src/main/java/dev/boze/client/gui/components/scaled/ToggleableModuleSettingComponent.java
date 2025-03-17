package dev.boze.client.gui.components.scaled;

import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.bind.eP;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;

public class ToggleableModuleSettingComponent extends ScaledBaseComponent {
   private static final double field1392 = 6.0;
   private final ArrayList<InputBaseComponent> field1393 = new ArrayList<>();

   public ToggleableModuleSettingComponent(ToggleableModule module) {
      super(module.getTitle(), 150.0 * BaseComponent.scaleFactor, 12.0 + 20.0 * BaseComponent.scaleFactor, true);
      this.field1393.add(new eP(this, "Bind", this.field1388 + 6.0, this.field1389 + 6.0, this.field1390 - 12.0, 20.0 * BaseComponent.scaleFactor, module));
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

      for (InputBaseComponent var9 : this.field1393) {
         var9.render(matrices, mouseX, mouseY, delta);
      }

      RenderUtil.field3963.method2235(matrices);
      IFontRender.method499().endBuilding();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         return false;
      } else {
         for (InputBaseComponent var10 : this.field1393) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
               break;
            }
         }

         return true;
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      for (InputBaseComponent var8 : this.field1393) {
         if (var8.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
         }
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }
}
