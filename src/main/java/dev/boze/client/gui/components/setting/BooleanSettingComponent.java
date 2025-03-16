package dev.boze.client.gui.components.setting;

import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.ScaledSettingBaseComponent;
import dev.boze.client.jumptable.hU;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class BooleanSettingComponent extends ScaledSettingBaseComponent implements IMinecraft {
   private final BooleanSetting field281;
   private double field282 = 0.0;

   public BooleanSettingComponent(BooleanSetting setting, BaseComponent parent, double x, double y, double width, double height) {
      super(setting, parent, x, y, width, height);
      this.field281 = setting;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   @Override
   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      if (this.field281.method2116()) {
         this.field321 = (double)Theme.method1376() * scaleFactor;
         super.render(context, mouseX, mouseY, delta);
         RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field321, this.field272 = Theme.method1348());
         IFontRender.method499()
            .drawShadowedText(
               this.field316,
               this.field318 + 6.0 * scaleFactor,
               this.field319 + this.field321 / 2.0 - IFontRender.method499().method1390() / 2.0,
               Theme.method1350()
            );
         this.field282 = 0.0;
         if (this.field281.hasChildren()) {
            double var8 = this.field321 / 9.0;
            double var10 = this.field318 + this.field320 - scaleFactor * 6.0 - var8;
            if (this.field281.isExpanded()) {
               RenderUtil.field3963.method2257(var10, this.field319 + var8 * 2.0, var8, var8 * 5.0, 15, 12, var8 / 2.0, Theme.method1350());
            } else {
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 2.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 4.0, var8, Theme.method1350());
               RenderUtil.field3963.method2261(var10, this.field319 + var8 * 6.0, var8, Theme.method1350());
            }

            this.field282 = scaleFactor * 6.0 + var8 / 2.0;
         }

         switch (hU.field2105[Gui.INSTANCE.field2371.method461().ordinal()]) {
            case 1:
               RenderUtil.field3963
                  .method2257(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field282 - this.field321 * 1.2,
                     this.field319 + this.field321 * 0.2,
                     this.field321 * 1.2,
                     this.field321 * 0.6,
                     15,
                     12,
                     this.field321 * 0.8,
                     this.field281.method419() ? Theme.method1352() : Theme.method1352().method2025(Theme.method1391())
                  );
               RenderUtil.field3963
                  .method2261(
                     this.field318
                        + this.field320
                        - 6.0 * scaleFactor
                        - this.field282
                        - (this.field281.method419() ? this.field321 * 0.5 : this.field321 * 1.1),
                     this.field319 + this.field321 * 0.3,
                     this.field321 * 0.4,
                     Theme.method1350()
                  );
               break;
            case 2:
               RenderUtil.field3963
                  .method2261(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field282 - this.field321 * 0.6,
                     this.field319 + this.field321 * 0.2,
                     this.field321 * 0.6,
                     Theme.method1348().method183(Theme.method1390())
                  );
               if (this.field281.method419()) {
                  RenderUtil.field3963
                     .method2261(
                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field282 - this.field321 * 0.45,
                        this.field319 + this.field321 * 0.35,
                        this.field321 * 0.3,
                        Theme.method1350()
                     );
               }
               break;
            case 3:
               RenderUtil.field3963
                  .method2242(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field282 - this.field321 * 0.3,
                     this.field319 + this.field321 * 0.5,
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field282 - this.field321 * 0.15,
                     this.field319 + this.field321 * 0.8,
                     !this.field281.method419() ? Theme.method1348().method183(Theme.method1390()) : Theme.method1352()
                  );
               RenderUtil.field3963
                  .method2242(
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field282 - this.field321 * 0.15,
                     this.field319 + this.field321 * 0.8,
                     this.field318 + this.field320 - 6.0 * scaleFactor - this.field282,
                     this.field319 + this.field321 * 0.2,
                     !this.field281.method419() ? Theme.method1348().method183(Theme.method1390()) : Theme.method1352()
                  );
         }
      } else {
         this.field321 = 0.0;
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field281.method2116() && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
         if (button == 0) {
            this.field273.reset();
            this.field281.method421(!this.field281.method419());
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
         }

         if (button == 1 && this.field281.hasChildren()) {
            this.field273.reset();
            this.field281.setExpanded(!this.field281.isExpanded());
            return true;
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }
}
