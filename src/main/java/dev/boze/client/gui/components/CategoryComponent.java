package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.client.Theme;
import net.minecraft.client.gui.DrawContext;

class CategoryComponent extends BaseComponent {
   private final Category field361;

   CategoryComponent(BaseComponent var1, double var2, double var4, double var6, double var8, Category var10) {
      super(var10 == null ? "Addons" : var10.name(), var1, var2, var4, var6, var8);
      this.field361 = var10;
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      super.render(context, mouseX, mouseY, delta);
      IFontRender.method499()
         .drawShadowedText(
            this.field316,
            this.field318 + this.field320 * 0.5 - IFontRender.method499().method501(this.field316) * 0.5,
            this.field319 + this.field321 * 0.5 - IFontRender.method499().method1390() * 0.5,
            this.field361 == ((CategoriesComponent)this.field317).field348 ? Theme.method1350() : Theme.method1350().method2025(Theme.method1391())
         );
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
         ((CategoriesComponent)this.field317).field348 = this.field361;
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }
}
