package dev.boze.client.gui.components;

import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class2782;
import mapped.Class5928;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class EditableComponent extends BaseComponent implements IMinecraft {
   public String field387 = "";
   private boolean field388 = false;
   public boolean field389 = true;
   private double field390;
   private double field391;
   private double field392;
   private double field393;

   public EditableComponent(String name, BaseComponent parent, double x, double y, double width, double height) {
      super(name, parent, x, y, width, height);
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field388) {
         RenderUtil.field3963.method2233();
         IFontRender.method499().startBuilding(scaleFactor * 0.5);
         if (isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field321)) {
            IconManager.setScale(scaleFactor * 0.5);
            Notifications.LOCK
               .render(
                  this.field390 = this.field318 + this.field320 - Notifications.LOCK.method2091() - scaleFactor * 6.0,
                  this.field391 = this.field319 + this.field321 * 0.5 - IconManager.method1116() * 0.5,
                  Class2782.field94 ? Theme.method1350() : Theme.method1351()
               );
            this.field392 = Notifications.LOCK.method2091();
            this.field393 = IconManager.method1116();
         }

         if (Theme.method1387()) {
            RenderUtil.field3963.method2257(this.field318, this.field319, this.field320, this.field321, 15, 24, this.field321 * 0.5, Theme.method1347());
         } else {
            RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, Theme.method1347());
         }

         String var8 = this.field387 + (System.currentTimeMillis() % 1000L > 500L ? "_" : "");
         IFontRender.method499()
            .drawShadowedText(
               var8,
               this.field318 + this.field320 * 0.5 - IFontRender.method499().method501(this.field387) * 0.5,
               this.field319 + this.field321 * 0.5 - IFontRender.method499().method1390() * 0.5,
               Theme.method1350()
            );
         RenderUtil.field3963.method2235(context);
         IFontRender.method499().endBuilding(context);
         if (isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field321)) {
            IconManager.method1115();
         }
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field390, this.field391, this.field392, this.field393) && button == 0) {
         Class2782.field94 = !Class2782.field94;
         if (Class2782.field94) {
            this.field318 = (double)mc.getWindow().getScaledWidth() * 0.5 - this.field320 * 0.5;
            this.field319 = (double)mc.getWindow().getScaledHeight() - this.field321 * 0.5 - BaseComponent.scaleFactor * 24.0;
            Class2782.field95 = -1.0;
            Class2782.field96 = -1.0;
         }

         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button);
      }
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321) && !Class2782.field94 && button == 0) {
         this.field318 += deltaX;
         this.field319 += deltaY;
         Class2782.field95 = this.field318;
         Class2782.field96 = this.field319;
         return true;
      } else {
         return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode != 257 && (keyCode != 70 || (modifiers & 2) == 0)) {
         if (!this.field388 && Gui.INSTANCE.field2359.method419() && keyCode != 256 && keyCode != 261) {
            if (this.field389) {
               this.field389 = false;
               if (Gui.INSTANCE.bind.matches(true, keyCode)) {
                  return true;
               }
            }

            this.field388 = true;
            this.field387 = "";
         }

         if (!this.field388) {
            return super.keyPressed(keyCode, scanCode, modifiers);
         } else {
            if (keyCode == 259) {
               if (!this.field387.isEmpty()) {
                  this.field387 = this.field387.substring(0, this.field387.length() - 1);
               }
            } else if (keyCode == 256) {
               this.field388 = false;
               if (!Gui.INSTANCE.field2360.method419()) {
                  return false;
               }
            } else if (keyCode == 261) {
               this.field387 = "";
            } else {
               String var7 = GLFW.glfwGetKeyName(keyCode, scanCode);
               if (var7 != null && var7.length() == 1) {
                  if (Class5928.method159(340) || Class5928.method159(344)) {
                     var7 = var7.toUpperCase();
                  }

                  this.field387 = this.field387 + var7;
               }
            }

            return true;
         }
      } else {
         this.field388 = !this.field388;
         this.field387 = "";
         return true;
      }
   }
}
