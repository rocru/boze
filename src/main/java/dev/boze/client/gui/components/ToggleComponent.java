package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public abstract class ToggleComponent extends InputBaseComponent {
   public ToggleComponent(String name, double x, double y, double width, double height) {
      super(name, x, y, width, height);
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      IFontRender.method499()
         .drawShadowedText(
            this.field1132,
            this.field1133 + 6.0 * field1131,
            this.field1134 + this.field1136 / 2.0 - IFontRender.method499().method1390() / 2.0,
            Theme.method1350()
         );
      RenderUtil.field3963
         .method2257(
            this.field1133 + this.field1135 - 6.0 * field1131 - this.field1136 * 1.2,
            this.field1134 + this.field1136 * 0.2,
            this.field1136 * 1.2,
            this.field1136 * 0.6,
            15,
            12,
            this.field1136 * 0.8,
            Theme.method1348().method183(Theme.method1390())
         );
      RenderUtil.field3963
         .method2261(
            this.field1133 + this.field1135 - 6.0 * field1131 - (this.isToggled() ? this.field1136 * 0.5 : this.field1136 * 1.1),
            this.field1134 + this.field1136 * 0.3,
            this.field1136 * 0.4,
            Theme.method1350()
         );
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1133, this.field1134, this.field1135, this.field1136) && button == 0) {
         this.setToggled(!this.isToggled());
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   protected abstract void setToggled(boolean var1);

   protected abstract boolean isToggled();
}
