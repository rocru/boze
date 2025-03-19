package dev.boze.client.gui.components;

import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.api.setting.*;
import dev.boze.client.api.BozeBind;
import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.ModuleDisplayMode;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.scaled.ToggleableModuleSettingComponent;
import dev.boze.client.gui.components.setting.*;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.hM;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class2773;
import mapped.Class3077;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class AddonComponent extends BaseComponent implements IMinecraft {
   public final ToggleableModule field362;
   private final double field363;
   private final ArrayList<BaseComponent> field364 = new ArrayList();
   private final Class2773[] field365 = new Class2773[3];
   public boolean field366;
   private boolean field367 = false;
   private Timer field368 = new Timer();
   private double field369;
   private double field370;
   private double field371;
   private double field372;
   private double field373;
   private double field374;

   public AddonComponent(ToggleableModule addonModule, BaseComponent parent, double x, double y, double width) {
      super(addonModule.getTitle(), parent, x, y, width, (double)Theme.method1367() * scaleFactor);
      this.field362 = addonModule;
      this.field363 = (double)Theme.method1367() * scaleFactor;
      double var12 = this.field363;
      this.field364.add(new ToggleableModuleComponent(addonModule, this, x, var12, width, (double)Theme.method1376() * scaleFactor));
      var12 += (double)Theme.method1376() * scaleFactor;

      for (SettingBase var15 : addonModule.settings) {
         double var16 = scaleFactor * 3.0;
         double var18 = x + var16;
         double var20 = width - var16;
         if (var15 instanceof SettingToggle) {
            this.field364.add(new SettingToggleComponent((SettingToggle)var15, this, var18, var12, var20, (double)Theme.method1376() * scaleFactor));
            var12 += (double)Theme.method1376() * scaleFactor;
         } else if (var15 instanceof SettingSlider) {
            this.field364.add(new SettingSliderComponent((SettingSlider)var15, this, var18, var12, var20, ((double)Theme.method1376() + 4.0) * scaleFactor));
            var12 += ((double)Theme.method1376() + 4.0) * scaleFactor;
         } else if (var15 instanceof SettingMode) {
            this.field364.add(new SettingModeComponent((SettingMode)var15, this, var18, var12, var20, (double)Theme.method1376() * scaleFactor));
            var12 += (double)Theme.method1376() * scaleFactor;
         } else if (var15 instanceof SettingColor) {
            this.field364.add(new SettingColorComponent((SettingColor)var15, this, var18, var12, var20, (double)Theme.method1376() * scaleFactor));
            var12 += (double)Theme.method1376() * scaleFactor;
         }
      }

      this.field365[0] = new Class2773(Notifications.POWER, AddonComponent::lambda$new$0, Theme.INSTANCE.field2435);
      this.field365[1] = new Class2773(Notifications.TUNE, AddonComponent::lambda$new$1, Theme.INSTANCE.field2436);
      this.field365[2] = new Class2773(this.field366 ? Notifications.EXPAND_LESS : Notifications.EXPAND_MORE, this::lambda$new$2, Theme.INSTANCE.ab);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.field369 = (double)mouseX;
      this.field370 = (double)mouseY;
      if (!isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field363)) {
         this.field368.reset();
      } else if (Gui.INSTANCE.field2355.getValue() == 0.0 || this.field368.hasElapsed(Gui.INSTANCE.field2355.getValue() * 1000.0)) {
         ClickGUI.field1335.method581(this.field362.getDescription(), this.field318, this.field318 + this.field320);
      }

      String var8 = Theme.method1392(Theme.method1369(), this.field362.getTitle());

      double var9 = switch (hM.field2097[Theme.method1368().ordinal()]) {
         case 1 -> this.field318 + scaleFactor * 6.0;
         case 2 -> this.field318 + this.field320 / 2.0 - IFontRender.method499().method501(var8) / 2.0;
         case 3 -> this.field318 + this.field320 - scaleFactor * 6.0 - IFontRender.method499().method501(var8);
         default -> throw new IncompatibleClassChangeError();
      };
      if (Theme.method1373()) {
         RenderUtil.field3965
            .method2253(this.field318, this.field319, this.field320, this.field363, this.field362.getState() ? Theme.method1375() : Theme.method1374());
      } else {
         RenderUtil.field3963
            .method2252(this.field318, this.field319, this.field320, this.field363, this.field362.getState() ? Theme.method1353() : Theme.method1347());
      }

      IFontRender.method499()
         .drawShadowedText(
            var8,
            var9,
            this.field319 + this.field363 / 2.0 - IFontRender.method499().method1390() / 2.0,
            this.field362.getState() ? Theme.method1354() : Theme.method1350()
         );
      if (Theme.method1372() && !isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field363)) {
         if (Theme.method1370() == ModuleDisplayMode.Icons && Theme.method1371()) {
            String var26;
            if (this.field362.getBind().getBind() == -1) {
               var26 = " ";
            } else {
               var26 = this.field362.getBind().isButton()
                  ? KeyboardUtil.getButtonName(this.field362.getBind().getBind())
                  : KeyboardUtil.getKeyName(this.field362.getBind().getBind());
            }

            double var28 = this.field362.getBind().getBind() == -1
               ? IFontRender.method499().method1390() - scaleFactor * 2.0
               : IFontRender.method499().method501(var26);
            double var30 = IFontRender.method499().method1390();
            double var35 = Math.max(var28, var30);
            double var38 = Theme.method1368() == AlignMode.Right
               ? this.field318 + scaleFactor * 6.0
               : this.field318 + this.field320 - (var35 + scaleFactor * (double)(var35 > var28 ? 2 : 4)) * 0.5 - this.field363 * 0.1 - var28 / 2.0;
            IFontRender.method499()
               .drawShadowedText(
                  var26, var38, this.field319 + this.field363 * 0.5 - var30 * 0.5, this.field362.getState() ? Theme.method1355() : Theme.method1351()
               );
         }
      } else if (Theme.method1370() == ModuleDisplayMode.Icons) {
         if (isMouseWithinBounds((double)mouseX, (double)mouseY, this.field318, this.field319, this.field320, this.field363)) {
            double var11 = IconManager.method1116();
            double var13 = Math.min((this.field363 - var11) * 0.5, scaleFactor * 6.0);
            int var15 = 0;

            for (Class2773 var19 : this.field365) {
               if (var19.method5427()) {
                  var15++;
               }
            }

            double var33 = this.field318 + this.field320 - var11 * (double)var15 - var13 * (double)var15;
            if (Theme.method1368() == AlignMode.Right) {
               var33 = this.field318 + var13;
            }

            double var37 = this.field319 + this.field363 * 0.5 - var11 * 0.5;

            for (Class2773 var23 : this.field365) {
               if (var23.method5427()) {
                  var23.field77 = var33;
                  var23.field78 = var37;
                  var23.field79 = var11;
                  var33 += var11 + var13;
                  boolean var24 = isMouseWithinBounds((double)mouseX, (double)mouseY, var23.field77, var23.field78, var23.field79, var23.field79);
                  var23.field74
                     .render(
                        var23.field77,
                        var23.field78,
                        var24
                           ? (this.field362.getState() ? Theme.method1354() : Theme.method1350())
                           : (this.field362.getState() ? Theme.method1355() : Theme.method1351())
                     );
               }
            }
         }
      } else if (Theme.method1370() == ModuleDisplayMode.Bind) {
         String var25;
         if (this.field367) {
            var25 = "...";
         } else if (this.field362.getBind().getBind() == -1) {
            var25 = " ";
         } else {
            var25 = this.field362.getBind().isButton()
               ? KeyboardUtil.getButtonName(this.field362.getBind().getBind())
               : KeyboardUtil.getKeyName(this.field362.getBind().getBind());
         }

         double var12 = this.field362.getBind().getBind() == -1
            ? IFontRender.method499().method1390() - scaleFactor * 2.0
            : IFontRender.method499().method501(var25);
         double var14 = IFontRender.method499().method1390();
         double var34 = Math.max(var12, var14);
         RenderUtil.field3963
            .method2257(
               this.field371 = this.field318 + this.field320 - (var34 + scaleFactor * (double)(var34 > var12 ? 2 : 4)) - scaleFactor * 6.0,
               this.field372 = this.field319 + this.field363 * 0.5 - (var14 * 0.5 + scaleFactor),
               this.field373 = var34 + scaleFactor * (double)(var34 > var12 ? 2 : 4),
               this.field374 = var14 + scaleFactor * 2.0,
               15,
               12,
               scaleFactor * 4.0,
               Theme.method1347().method183(Theme.method1390())
            );
         IFontRender.method499()
            .drawShadowedText(
               var25,
               this.field318 + this.field320 - (var34 + scaleFactor * (double)(var34 > var12 ? 2 : 4)) * 0.5 - scaleFactor * 6.0 - var12 / 2.0,
               this.field319 + this.field363 * 0.5 - var14 * 0.5,
               Theme.method1350()
            );
         if (isMouseWithinBounds((double)mouseX, (double)mouseY, this.field371, this.field372, this.field373, this.field374)) {
            ClickGUI.field1335.field1337 = CursorType.IBeam;
         }
      } else if (Theme.method1370() == ModuleDisplayMode.State) {
         switch (hM.field2098[Gui.INSTANCE.field2371.method461().ordinal()]) {
            case 1:
               RenderUtil.field3963
                  .method2257(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field363 * 1.2,
                     this.field319 + this.field363 * 0.2,
                     this.field363 * 1.2,
                     this.field363 * 0.6,
                     15,
                     12,
                     this.field363 * 0.8,
                     this.field362.getState() ? Theme.method1353() : Theme.method1347().method183(Theme.method1390())
                  );
               RenderUtil.field3963
                  .method2261(
                     this.field318 + this.field320 - 6.0 * scaleFactor - (this.field362.getState() ? this.field363 * 0.5 : this.field363 * 1.1),
                     this.field319 + this.field363 * 0.3,
                     this.field363 * 0.4,
                     this.field362.getState() ? Theme.method1354() : Theme.method1350()
                  );
               break;
            case 2:
               RenderUtil.field3963
                  .method2261(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field363 * 0.6,
                     this.field319 + this.field363 * 0.2,
                     this.field363 * 0.6,
                     this.field362.getState() ? Theme.method1353() : Theme.method1347().method183(Theme.method1390())
                  );
               if (this.field362.getState()) {
                  RenderUtil.field3963
                     .method2261(
                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field363 * 0.45,
                        this.field319 + this.field363 * 0.35,
                        this.field363 * 0.3,
                        Theme.method1354()
                     );
               }
               break;
            case 3:
               RenderUtil.field3963
                  .method2242(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field363 * 0.3,
                     this.field319 + this.field363 * 0.5,
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field363 * 0.15,
                     this.field319 + this.field363 * 0.8,
                     !this.field362.getState() ? Theme.method1347().method183(Theme.method1390()) : Theme.method1352()
                  );
               RenderUtil.field3963
                  .method2242(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field363 * 0.15,
                     this.field319 + this.field363 * 0.8,
                     this.field318 + this.field320 - 6.0 * scaleFactor,
                     this.field319 + this.field363 * 0.2,
                     !this.field362.getState() ? Theme.method1347().method183(Theme.method1390()) : Theme.method1352()
                  );
         }
      }

      if (this.field366) {
         double var27 = this.field363;

         for (int var29 = 0; var29 < this.field364.size(); var29++) {
            BaseComponent var31 = (BaseComponent)this.field364.get(var29);
            if (var31 instanceof SettingBaseComponent
               && (
                  !(var31 instanceof ToggleableModuleComponent)
                     || Theme.method1370() != ModuleDisplayMode.Bind && Theme.method1370() != ModuleDisplayMode.Icons
               )) {
               SettingBaseComponent var32 = (SettingBaseComponent)var31;
               double var36 = scaleFactor * 3.0;
               var32.field318 = this.field318 + var36;
               var32.field319 = this.field319 + var27;
               var32.render(context, mouseX, mouseY, delta);
               if (var32.field321 > 0.0 && var32.field395 != null) {
                  RenderUtil.field3963.method2252(this.field318, this.field319 + var27, var36, var32.field321, var32.field395);
                  if (Theme.method1377()) {
                     RenderUtil.field3963.method2252(this.field318, this.field319 + var27, scaleFactor, var32.field321, Theme.method1351());
                  }
               }

               var27 += var32.field321;
            }
         }

         this.field321 = var27;
      } else {
         this.field321 = this.field363;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field366) {
         for (BaseComponent var10 : this.field364) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
               return true;
            }
         }
      }

      if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field363)) {
         if (Theme.method1370() == ModuleDisplayMode.Bind && isMouseWithinBounds(mouseX, mouseY, this.field371, this.field372, this.field373, this.field374)) {
            if (this.field367) {
               this.field362.setBind(new BozeBind(true, button));
               this.field367 = false;
               Class3077.field174 = false;
               return true;
            }

            if (button == 0 && !Class3077.field174) {
               this.field367 = true;
               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               return true;
            }
         } else {
            if (button == 0) {
               if (Theme.method1370() == ModuleDisplayMode.Icons) {
                  for (Class2773 var12 : this.field365) {
                     if (var12.method5427()
                        && isMouseWithinBounds(
                           mouseX, mouseY, var12.field77 - var12.field79 * 0.1, var12.field78 - var12.field79 * 0.1, var12.field79 * 1.2, var12.field79 * 1.2
                        )) {
                        var12.field75.run();
                        return true;
                     }
                  }
               }

               mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
               this.field362.setState(!this.field362.getState());
               return true;
            }

            if (button == 1) {
               this.field366 = !this.field366;
               this.field365[2].field74 = this.field366 ? Notifications.EXPAND_LESS : Notifications.EXPAND_MORE;
               return true;
            }
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int button) {
      for (BaseComponent var10 : this.field364) {
         var10.mouseClicked(mouseX, mouseY, button);
      }
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.field366) {
         for (BaseComponent var14 : this.field364) {
            if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
               return true;
            }
         }
      }

      return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
   }

   @Override
   public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
      if (this.field366) {
         for (BaseComponent var11 : this.field364) {
            if (var11.onMouseScroll(mouseX, mouseY, amount)) {
               return true;
            }
         }
      }

      return super.onMouseScroll(mouseX, mouseY, amount);
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.field367) {
         if (keyCode == 256) {
            this.field362.setBind(new BozeBind(false, -1));
         } else {
            this.field362.setBind(new BozeBind(false, keyCode));
         }

         this.field367 = false;
         Class3077.field174 = false;
         return true;
      } else {
         if (this.field366) {
            for (BaseComponent var8 : this.field364) {
               if (var8.keyPressed(keyCode, scanCode, modifiers)) {
                  return true;
               }
            }
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   @Override
   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      for (BaseComponent var8 : this.field364) {
         if (var8.keyReleased(keyCode, scanCode, modifiers)) {
            return true;
         }
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   private void lambda$new$2() {
      this.field366 = !this.field366;
      this.field365[2].field74 = this.field366 ? Notifications.EXPAND_LESS : Notifications.EXPAND_MORE;
   }

   private static void lambda$new$1(ToggleableModule var0) {
      ClickGUI.field1335.method580(new ToggleableModuleSettingComponent(var0));
      mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   private static void lambda$new$0(ToggleableModule var0) {
      var0.setState(!var0.getState());
      mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }
}
