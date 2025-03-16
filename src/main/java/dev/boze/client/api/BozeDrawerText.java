package dev.boze.client.api;

import dev.boze.api.render.DrawColor;
import dev.boze.api.render.DrawerText;
import dev.boze.client.font.IFontRender;
import net.minecraft.client.gui.DrawContext;

public class BozeDrawerText implements DrawerText {
   private static BozeDrawerText field1851 = null;

   public static BozeDrawerText method969() {
      if (field1851 == null) {
         field1851 = new BozeDrawerText();
      }

      return field1851;
   }

   public void startDrawing(double var1) {
      IFontRender.method499().startBuilding(var1);
   }

   public void stopDrawing(DrawContext var1) {
      IFontRender.method499().endBuilding(var1);
   }

   public double draw(String var1, double var2, double var4, DrawColor var6, boolean var7) {
      return IFontRender.method499().drawShadowedText(var1, var2, var4, (BozeDrawColor)var6, var7);
   }

   public void startSizing(double var1) {
      IFontRender.method499().startBuilding(var1, true);
   }

   public void stopSizing() {
      IFontRender.method499().endBuilding();
   }

   public double getWidth(String var1, boolean var2) {
      return IFontRender.method499().measureTextHeight(var1, var2);
   }

   public double getHeight(boolean var1) {
      return IFontRender.method499().method502(var1);
   }
}
