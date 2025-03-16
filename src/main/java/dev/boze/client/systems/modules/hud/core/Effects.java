package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import java.util.Collection;
import java.util.Comparator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;

public class Effects extends HUDModule {
   public static final Effects INSTANCE = new Effects();
   private final BooleanSetting field2614 = new BooleanSetting("Dynamic", true, "Dynamic text color");
   private final BooleanSetting field2615 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field2616 = new ColorSetting(
      "Text", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field2615
   );
   private final BooleanSetting field2617 = new BooleanSetting("Shadow", false, "Text shadow", this.field2615);
   private final Comparator<String> field2618 = this::lambda$new$0;
   private final Comparator<String> field2619 = this::lambda$new$1;

   public Effects() {
      super("Effects", "Shows your current potion effects", 40.0, 40.0);
   }

   @Override
   public void method295(DrawContext context) {
      boolean var5 = this.method2010() == 1 || this.method2010() == 2;
      boolean var6 = this.method2010() == 2 || this.method2010() == 4;
      java.util.ArrayList var7 = new java.util.ArrayList();
      java.util.ArrayList var8 = new java.util.ArrayList();
      Collection var9 = mc.player.getStatusEffects();
      if (var9 != null && !var9.isEmpty()) {
         for (StatusEffectInstance var11 : var9) {
            if (var11 != null) {
               StatusEffect var12 = (StatusEffect)var11.getEffectType().value();
               if (var12 != null) {
                  String var13 = I18n.translate(var12.getTranslationKey(), new Object[0]);
                  if (var11.getAmplifier() == 1) {
                     var13 = var13 + " 2";
                  } else if (var11.getAmplifier() == 2) {
                     var13 = var13 + " 3";
                  } else if (var11.getAmplifier() == 3) {
                     var13 = var13 + " 4";
                  }

                  var13 = var13 + " " + this.method1553(var11, 1.0F);
                  var7.add(var13);
                  var8.add(var12.getColor());
                  var7.sort(var5 ? this.field2618 : this.field2619);
               }
            }
         }
      }

      if (var7.size() == 0) {
         this.method314(20.0);
         this.method316(20.0);
         if (mc.currentScreen instanceof ClickGUI && ClickGUI.field1335.field1336 && HUD.INSTANCE.field2394.method419()) {
            HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
         }
      } else {
         double var17 = IFontRender.method499()
            .measureTextHeight((String)var7.get(var7.size() - 1), this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419());
         this.method314(var17);
         int[] var18 = new int[]{0};

         for (int var20 = 0; var20 < var7.size(); var20++) {
            String var14 = (String)var7.get(var20);
            double var15 = IFontRender.method499()
               .measureTextHeight(var14, this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419());
            if (HUD.INSTANCE.field2394.method419()) {
               HUD.INSTANCE
                  .field2397
                  .method2252(
                     this.method1391() + (var6 ? this.method313() - var15 - 4.0 : 0.0),
                     this.method305() + (double)var18[0],
                     var15 + 4.0,
                     IFontRender.method499().method502(this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419()) + 1.5,
                     RGBAColor.field402
                  );
            }

            IFontRender.method499()
               .drawShadowedText(
                  var14,
                  this.method1391() + (var6 ? this.method313() - var15 - 2.0 : 2.0),
                  this.method305() + (double)var18[0] + 0.5,
                  (RGBAColor)(this.field2614.method419()
                     ? new RGBAColor(0xFF000000 | (Integer)var8.get(var20))
                     : (this.field2615.method419() ? this.field2616.method1362() : HUD.INSTANCE.field2383.method1362())),
                  this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419()
               );
            var18[0] = (int)(
               (double)var18[0]
                  + IFontRender.method499().method502(this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419())
                  + 1.5
            );
         }

         this.method316((double)var18[0]);
      }
   }

   private String method1553(StatusEffectInstance var1, float var2) {
      if (var1.isAmbient()) {
         return "**:**";
      } else {
         int var6 = MathHelper.floor((float)var1.getDuration() * var2);
         return method1554(var6);
      }
   }

   public static String method1554(int ticks) {
      int var4 = ticks / 20;
      int var5 = var4 / 60;
      var4 %= 60;
      return var4 < 10 ? var5 + ":0" + var4 : var5 + ":" + var4;
   }

   private int lambda$new$1(String var1, String var2) {
      double var6 = IFontRender.method499()
            .measureTextHeight(var1, this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419())
         - IFontRender.method499().measureTextHeight(var2, this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419());
      return var6 != 0.0 ? (int)var6 : var1.compareTo(var2);
   }

   private int lambda$new$0(String var1, String var2) {
      double var6 = IFontRender.method499()
            .measureTextHeight(var2, this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419())
         - IFontRender.method499().measureTextHeight(var1, this.field2615.method419() ? this.field2617.method419() : HUD.INSTANCE.field2384.method419());
      return var6 != 0.0 ? (int)var6 : var2.compareTo(var1);
   }
}
