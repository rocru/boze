package dev.boze.client.gui.screens;

import mapped.Class2768;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class BozeScreen extends Screen {
   private Class2768 field2083 = null;

   protected BozeScreen() {
      super(Text.literal("Boze"));
   }

   public void method1126(Class2768 window) {
      this.field2083 = window;
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field2083 != null) {
         this.field2083.method5401(context, mouseX, mouseY);
      }

      super.render(context, mouseX, mouseY, delta);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return this.field2083 != null ? this.field2083.method5406(keyCode, scanCode, modifiers) : super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean shouldCloseOnEsc() {
      return this.field2083 != null ? this.field2083.method5409() : super.shouldCloseOnEsc();
   }

   public void close() {
      super.close();
   }

   protected void init() {
      if (this.field2083 != null) {
         this.field2083.method5400();
      }

      super.init();
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return this.field2083 != null ? this.field2083.method5402(mouseX, mouseY, button) : super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return this.field2083 != null ? this.field2083.method5403(mouseX, mouseY, button) : super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      return this.field2083 != null
         ? this.field2083.method5404(mouseX, mouseY, button, deltaX, deltaY)
         : super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double amount) {
      return this.field2083 != null ? this.field2083.method5405(mouseX, mouseY, amount) : super.mouseScrolled(mouseX, mouseY, horizontalAmount, amount);
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      return this.field2083 != null ? this.field2083.method5407(keyCode, scanCode, modifiers) : super.keyReleased(keyCode, scanCode, modifiers);
   }

   public boolean charTyped(char chr, int modifiers) {
      return this.field2083 != null ? this.field2083.method5408(chr, modifiers) : super.charTyped(chr, modifiers);
   }
}
