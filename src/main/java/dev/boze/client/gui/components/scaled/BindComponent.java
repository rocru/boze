package dev.boze.client.gui.components.scaled;

import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.MacroManagerComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.MacroManagerSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.Macro;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3077;
import mapped.Class5928;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class BindComponent extends ScaledBaseComponent {
   private final Macro field1491;
   private final MacroManagerSetting field1492;
   private String field1493;
   private double field1494;
   private double field1495;
   private double field1496;
   private int field1497 = 0;
   private boolean field1498 = false;
   private double field1499;
   private double field1500;
   private double field1501;
   private double field1502;

   public BindComponent(Macro macro, MacroManagerSetting setting) {
      super(macro.field1048, 0.8, 0.4);
      this.field1491 = macro;
      this.field1492 = setting;
      this.field1493 = "";
   }

   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
      IconManager.setScale(BaseComponent.scaleFactor * 0.4);
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

      IFontRender.method499()
         .drawShadowedText(
            this.field1491.field1048,
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501(this.field1491.field1048) * 0.5,
            this.field1389 + BaseComponent.scaleFactor * 6.0,
            Theme.method1350()
         );
      this.field1494 = this.field1389 + BaseComponent.scaleFactor * 12.0 + IFontRender.method499().method1390();
      this.field1495 = this.field1391 - BaseComponent.scaleFactor * 48.0 - IFontRender.method499().method1390() * 3.0;
      this.field1496 = (this.field1495 - BaseComponent.scaleFactor * 34.0) / 12.0;
      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 12.0,
            this.field1494,
            this.field1390 - BaseComponent.scaleFactor * 24.0,
            this.field1495,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1348()
         );

      for (int var8 = 0; var8 < Math.min(this.field1491.field1051.size(), 12); var8++) {
         RenderUtil.field3963
            .method2257(
               this.field1388 + BaseComponent.scaleFactor * 18.0,
               this.field1494 + (double)var8 * (this.field1496 + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0,
               this.field1390 - BaseComponent.scaleFactor * 36.0,
               this.field1496,
               15,
               24,
               Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
               Theme.method1347()
            );
         IFontRender.method499()
            .drawShadowedText(
               (String)this.field1491.field1051.get(var8 + this.field1497),
               this.field1388 + BaseComponent.scaleFactor * 24.0,
               this.field1494
                  + (double)var8 * (this.field1496 + BaseComponent.scaleFactor * 2.0)
                  + BaseComponent.scaleFactor * 6.0
                  + this.field1496 * 0.5
                  - IFontRender.method499().method1390() * 0.5,
               Theme.method1350()
            );
         Notifications.DELETE
            .render(
               this.field1388 + BaseComponent.scaleFactor * 18.0 + this.field1390 - BaseComponent.scaleFactor * 42.0 - Notifications.DELETE.method2091(),
               this.field1494
                  + (double)var8 * (this.field1496 + BaseComponent.scaleFactor * 2.0)
                  + BaseComponent.scaleFactor * 6.0
                  + this.field1496 * 0.5
                  - Notifications.DELETE.method1614() * 0.5,
               Theme.method1350()
            );
      }

      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 12.0,
            this.field1494 + this.field1495 + BaseComponent.scaleFactor * 6.0,
            this.field1390 - BaseComponent.scaleFactor * 24.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 12.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1348()
         );
      Notifications.ADD
         .render(
            this.field1388 + BaseComponent.scaleFactor * 12.0 + this.field1390 - BaseComponent.scaleFactor * 30.0 - Notifications.ADD.method2091(),
            this.field1494 + this.field1495 + BaseComponent.scaleFactor * 12.0 + IFontRender.method499().method1390() * 0.5 - IconManager.method1116() * 0.5,
            Theme.method1350()
         );
      String var16 = this.field1493 + (System.currentTimeMillis() % 1500L < 750L ? "|" : "");
      IFontRender.method499()
         .drawShadowedText(
            var16,
            this.field1388 + BaseComponent.scaleFactor * 18.0,
            this.field1494
               + this.field1495
               + BaseComponent.scaleFactor * 6.0
               + (IFontRender.method499().method1390() + BaseComponent.scaleFactor * 12.0) * 0.5
               - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      if (isMouseWithinBounds(
            (double)mouseX,
            (double)mouseY,
            this.field1388 + BaseComponent.scaleFactor * 12.0,
            this.field1494 + this.field1495 + BaseComponent.scaleFactor * 6.0,
            this.field1390 - BaseComponent.scaleFactor * 24.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 12.0
         )
         && !isMouseWithinBounds(
            (double)mouseX,
            (double)mouseY,
            this.field1388 + BaseComponent.scaleFactor * 12.0 + (this.field1390 - BaseComponent.scaleFactor * 42.0),
            this.field1494 + this.field1495 + BaseComponent.scaleFactor * 4.0 + IFontRender.method499().method1390() * 0.5,
            BaseComponent.scaleFactor * 16.0,
            BaseComponent.scaleFactor * 16.0
         )) {
         ClickGUI.field1335.field1337 = CursorType.IBeam;
      }

      String var9;
      if (this.field1498) {
         var9 = "...";
      } else if (this.field1491.field1049.getBind() == -1) {
         var9 = " ";
      } else {
         var9 = this.field1491.field1049.isKey()
            ? KeyboardUtil.getKeyName(this.field1491.field1049.getBind())
            : KeyboardUtil.getButtonName(this.field1491.field1049.getBind());
      }

      double var10 = this.field1491.field1049.getBind() == -1
         ? IFontRender.method499().method1390() - BaseComponent.scaleFactor * 2.0
         : IFontRender.method499().method501(var9);
      double var12 = IFontRender.method499().method1390();
      double var14 = Math.max(var10, var12);
      RenderUtil.field3963
         .method2257(
            this.field1499 = this.field1388
               + this.field1390
               - (var14 + BaseComponent.scaleFactor * (double)(var14 > var10 ? 4 : 6))
               - BaseComponent.scaleFactor * 12.0,
            this.field1500 = this.field1389 + this.field1391 - var12 - BaseComponent.scaleFactor * 12.0,
            this.field1501 = var14 + BaseComponent.scaleFactor * (double)(var14 > var10 ? 4 : 6),
            this.field1502 = var12 + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            BaseComponent.scaleFactor * 4.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            var9,
            this.field1388
               + this.field1390
               - (var14 + BaseComponent.scaleFactor * (double)(var14 > var10 ? 4 : 6)) * 0.5
               - BaseComponent.scaleFactor * 12.0
               - var10 / 2.0,
            this.field1389 + this.field1391 - var12 - BaseComponent.scaleFactor * 9.0,
            Theme.method1350()
         );
      if (isMouseWithinBounds((double)mouseX, (double)mouseY, this.field1499, this.field1500, this.field1501, this.field1502)) {
         ClickGUI.field1335.field1337 = CursorType.IBeam;
      }

      IFontRender.method499()
         .drawShadowedText(
            "Bind: ",
            this.field1499 - BaseComponent.scaleFactor * 6.0 - IFontRender.method499().method501("Bind: "),
            this.field1389 + this.field1391 - var12 - BaseComponent.scaleFactor * 9.0,
            Theme.method1350()
         );
      RenderUtil.field3963
         .method2257(
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Close") * 0.5 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 12.0,
            IFontRender.method499().method501("Close") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            "Close",
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Close") * 0.5,
            this.field1389 + this.field1391 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 9.0,
            Theme.method1350()
         );
      RenderUtil.field3963.method2235(context);
      IFontRender.method499().endBuilding();
      IconManager.method1115();
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         return super.isMouseOver(mouseX, mouseY, button);
      } else {
         if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501("Close") * 0.5 - BaseComponent.scaleFactor * 3.0,
            this.field1389 + this.field1391 - IFontRender.method499().method1390() - BaseComponent.scaleFactor * 12.0,
            IFontRender.method499().method501("Close") + BaseComponent.scaleFactor * 6.0,
            IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0
         )) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(new MacroManagerComponent(this.field1492));
         } else if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + BaseComponent.scaleFactor * 12.0,
            this.field1494,
            this.field1390 - BaseComponent.scaleFactor * 24.0,
            this.field1495
         )) {
            for (int var9 = 0; var9 < Math.min(this.field1491.field1051.size(), 12); var9++) {
               if (isMouseWithinBounds(
                  mouseX,
                  mouseY,
                  this.field1388 + BaseComponent.scaleFactor * 14.0 + (this.field1390 - BaseComponent.scaleFactor * 50.0),
                  this.field1494
                     + (double)var9 * (this.field1496 + BaseComponent.scaleFactor * 2.0)
                     + BaseComponent.scaleFactor * 6.0
                     + this.field1496 * 0.5
                     - BaseComponent.scaleFactor * 8.0,
                  BaseComponent.scaleFactor * 16.0,
                  BaseComponent.scaleFactor * 16.0
               )) {
                  this.field1491.field1051.remove(var9 + this.field1497);
                  mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                  return true;
               }
            }
         } else if (isMouseWithinBounds(
            mouseX,
            mouseY,
            this.field1388 + BaseComponent.scaleFactor * 12.0 + (this.field1390 - BaseComponent.scaleFactor * 42.0),
            this.field1494 + this.field1495 + BaseComponent.scaleFactor * 4.0 + IFontRender.method499().method1390() * 0.5,
            BaseComponent.scaleFactor * 16.0,
            BaseComponent.scaleFactor * 16.0
         )) {
            if (this.field1493.length() > 0) {
               this.field1491.field1051.add(this.field1493);
               this.field1493 = "";
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
         } else if (isMouseWithinBounds(mouseX, mouseY, this.field1499, this.field1500, this.field1501, this.field1502)) {
            if (this.field1498) {
               this.field1491.field1049.set(false, button);
               this.field1498 = false;
               Class3077.field174 = false;
               return true;
            }

            if (button == 0 && !Class3077.field174) {
               this.field1498 = true;
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               return true;
            }
         }

         return true;
      }
   }

   @Override
   public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
      if (isMouseWithinBounds(
         mouseX, mouseY, this.field1388 + BaseComponent.scaleFactor * 12.0, this.field1494, this.field1390 - BaseComponent.scaleFactor * 24.0, this.field1495
      )) {
         this.field1497 = (int)MathHelper.clamp(
            (double)this.field1497 - amount, 0.0, (double)MathHelper.clamp(this.field1491.field1051.size() - 12, 0, this.field1491.field1051.size())
         );
         return true;
      } else {
         return super.onMouseScroll(mouseX, mouseY, amount);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.field1498) {
         if (keyCode == 256) {
            this.field1491.field1049.set(true, -1);
         } else {
            this.field1491.field1049.set(true, keyCode);
         }

         this.field1498 = false;
         Class3077.field174 = false;
         return true;
      } else {
         try {
            if (keyCode == 259) {
               this.field1493 = this.field1493.substring(0, this.field1493.length() - 1);
            } else if (keyCode == 257) {
               if (this.field1493.length() > 0) {
                  this.field1491.field1051.add(this.field1493);
                  this.field1493 = "";
                  mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               }
            } else if (keyCode == 86 && Class5928.method159(341)) {
               String var7 = GLFW.glfwGetClipboardString(mc.getWindow().getHandle());
               if (var7 != null && !var7.isEmpty()) {
                  var7.chars().forEach(this::lambda$keyPressed$0);
               }
            }
         } catch (Exception var8) {
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   @Override
   public void method583(char c) {
      this.field1493 = this.field1493 + c;
   }

   private void lambda$keyPressed$0(int var1) {
      char var5 = (char)var1;
      if (var5 == '\n') {
         if (this.field1493.length() > 0) {
            this.field1491.field1051.add(this.field1493);
            this.field1493 = "";
         }
      } else {
         this.method583(var5);
      }
   }
}
