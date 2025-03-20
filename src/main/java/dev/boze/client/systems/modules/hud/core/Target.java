package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.trackers.TargetTracker;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class5929;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class Target extends HUDModule implements Class5929 {
   public static final Target INSTANCE = new Target();
   private final IntSetting field615 = new IntSetting("Range", 30, 3, 250, 1, "Max range to show target");
   private final BooleanSetting field616 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field617 = new ColorSetting(
      "Name", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Name color", this.field616
   );
   private final BooleanSetting field618 = new BooleanSetting("Shadow", false, "Text shadow", this.field616);
   private final RGBASetting field619 = new RGBASetting("Health", new RGBAColor(0, 255, 0, 255), "Health color");
   private final RGBASetting field620 = new RGBASetting("HealthBar", new RGBAColor(0, 255, 0, 125), "Health bar background color");
   private final RGBASetting field621 = new RGBASetting("Absorption", new RGBAColor(255, 255, 0, 255), "Absorption color");
   private final RGBASetting field622 = new RGBASetting("AbsorptionBar", new RGBAColor(255, 255, 0, 125), "Absorption bar background color");
   private double field623;
   private double field624;

   public Target() {
      super("Target", "Shows the target", 150.0, 50.0);
      this.field595.setValue(0.5);
   }

   @Override
   public void method295(DrawContext context) {
      PlayerEntity var5 = TargetTracker.method1520();
      if (var5 != null
         && mc.player.networkHandler.getPlayerListEntry(var5.getUuid()) != null
         && !(mc.player.distanceTo(var5) > (float)this.field615.getValue().intValue())) {
         this.field623 = Math.max(IFontRender.method499().method501(var5.getName().getString()), IFontRender.method499().method501("Example42"));
         this.field624 = IFontRender.method499().method1390();
         this.method1904();
         if (HUD.INSTANCE.field2394.getValue()) {
            HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
         }

         double var6 = this.method333();
         IFontRender.method499()
            .drawShadowedText(
               var5.getName().getString(),
               this.method1391() + var6 + this.method334() * 2.0,
               this.method305() + this.method334(),
               this.field616.getValue() ? this.field617.getValue() : HUD.INSTANCE.field2383.getValue(),
               this.field616.getValue() ? this.field618.getValue() : HUD.INSTANCE.field2384.getValue()
            );
         float var8 = var5.getHealth();
         float var9 = var5.getAbsorptionAmount();
         double var10;
         double var12;
         double var14;
         double var16;
         if (var9 == 0.0F) {
            var10 = this.field623;
            var12 = var10 * (double)var8 / (double)var5.getMaxHealth();
            var14 = 0.0;
            var16 = 0.0;
         } else {
            float var18 = var5.getMaxHealth();
            float var20 = var18 + 16.0F;
            var10 = this.field623 * (double)var18 / (double)var20;
            var12 = var10 * (double)var8 / (double)var5.getMaxHealth();
            var14 = this.field623 * 16.0 / (double)var20;
            var16 = var14 * (double)var9 / 16.0;
         }

         RenderUtil.field3963
            .method2252(
               this.method1391() + var6 + this.method334() * 2.0,
               this.method305() + this.field624 + this.method334() * 2.0,
               var10,
               this.field624,
               this.field620.getValue()
            );
         RenderUtil.field3963
            .method2252(
               this.method1391() + var6 + this.method334() * 2.0,
               this.method305() + this.field624 + this.method334() * 2.0,
               var12,
               this.field624,
               this.field619.getValue()
            );
         if (var14 > 0.0) {
            RenderUtil.field3963
               .method2252(
                  this.method1391() + var6 + this.method334() * 2.0 + var10,
                  this.method305() + this.field624 + this.method334() * 2.0,
                  var14,
                  this.field624,
                  this.field622.getValue()
               );
            RenderUtil.field3963
               .method2252(
                  this.method1391() + var6 + this.method334() * 2.0 + var10,
                  this.method305() + this.field624 + this.method334() * 2.0,
                  var16,
                  this.field624,
                  this.field621.getValue()
               );
         }
      } else {
         if (mc.currentScreen instanceof ClickGUI && ClickGUI.field1335.field1336) {
            this.field623 = IFontRender.method499().method501("Example42");
            this.field624 = IFontRender.method499().method1390();
            this.method1904();
            if (HUD.INSTANCE.field2394.getValue()) {
               HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
            }
         }
      }
   }

   @Override
   public void method332(DrawContext context) {
      PlayerEntity var5 = TargetTracker.method1520();
      if (var5 != null && !(mc.player.distanceTo(var5) > (float)this.field615.getValue().intValue())) {
         PlayerListEntry var6 = mc.player.networkHandler.getPlayerListEntry(var5.getUuid());
         if (var6 != null) {
            Identifier var7 = var6.getSkinTextures().texture();
            int var8 = (int)(this.method333() + 0.5);
            context.drawTexture(
               var7, (int)(this.method1391() + this.method334()), (int)(this.method305() + this.method334() + 0.5), var8, var8, 8.0F, 8.0F, 8, 8, 64, 64
            );
            context.drawTexture(
               var7, (int)(this.method1391() + this.method334()), (int)(this.method305() + this.method334() + 0.5), var8, var8, 40.0F, 8.0F, 8, 8, 64, 64
            );
         }
      }
   }

   private void method1904() {
      this.method314(this.method333() + this.field623 + this.method334() * 3.0);
      this.method316(this.method333() + this.method334() * 2.0);
   }

   private double method333() {
      return this.field624 * 2.0 + this.method334();
   }

   private double method334() {
      return this.field624 * 0.2;
   }
}
