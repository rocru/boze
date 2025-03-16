package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public abstract class TextBaseComponent extends InputBaseComponent {
   public TextBaseComponent(String name, double x, double y, double width, double height) {
      super(name, x, y, width, height);
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      RenderUtil.field3963
         .method2257(this.field1133, this.field1134, this.field1135, this.field1136, 15, 24, Theme.method1387() ? field1131 * 6.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            this.field1132,
            this.field1133 + this.field1135 * 0.5 - IFontRender.method499().method501(this.field1132) * 0.5,
            this.field1134 + this.field1136 * 0.5 - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136)) {
         this.method1649(button);
         return true;
      } else {
         return false;
      }
   }

   protected abstract void method1649(int var1);
}
