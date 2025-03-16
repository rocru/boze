package mapped;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.systems.modules.client.Theme;
import net.minecraft.client.gui.DrawContext;

public class Class5908 extends InputBaseComponent {
   public Class5908(String name, double x, double y, double width, double height) {
      super(name, x, y, width, height);
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      IFontRender.method499()
         .drawShadowedText(
            this.field1132,
            this.field1133 + this.field1135 * 0.5 - IFontRender.method499().method501(this.field1132) * 0.5,
            this.field1134 + this.field1136 * 0.5 - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
   }
}
