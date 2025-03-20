package dev.boze.client.systems.modules;

import dev.boze.client.Boze;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.ToastManagerAccessor;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import mapped.Class3071;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public abstract class HUDModule extends Module {
   public final MinMaxSetting field595 = new MinMaxSetting("Scale", 1.0, 0.5, 1.5, 0.05, "Scale modifier for this HUD module");
   private double field596;
   private double field597;
   private double field598;
   private double field599;
   private double field600;
   private double field601;
   private boolean field602 = false;
   private int field603 = 1;
   private boolean field604;
   private boolean field605;

   public HUDModule(String name, String description, double width, double height) {
      this(name, description, Category.Hud, width, height);
   }

   public HUDModule(String name, String description, Category category, double width, double height) {
      this(
         name,
         description,
         category,
         (double)Theme.method1363() * 1.5 * BaseComponent.scaleFactor,
         (double)Theme.method1363() / 4.0 * BaseComponent.scaleFactor,
         1,
         width,
         height
      );
   }

   public HUDModule(String name, String description, Category category, double offsetX, double offsetY, int anchor, double width, double height) {
      super(name, description, category);
      this.field598 = width;
      this.field599 = height;
      this.field596 = MathHelper.clamp(offsetX, 0.0, (double)mc.getWindow().getScaledWidth() / 2.0);
      this.field597 = MathHelper.clamp(offsetY, 0.0, (double)mc.getWindow().getScaledHeight() / 2.0);
      this.field603 = anchor;
   }

   @Override
   public void isFriend(NbtCompound tag) {
      super.isFriend(tag);
      if (tag.contains("settings")) {
         NbtCompound var5 = tag.getCompound("settings");
         this.fromTag(var5);
      }
   }

   @Override
   public NbtCompound method226() {
      NbtCompound var4 = super.method226();
      NbtCompound var5;
      if (var4.contains("settings")) {
         var5 = var4.getCompound("settings");
      } else {
         var5 = new NbtCompound();
      }

      this.method294(var5);
      return var4;
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var3 = super.toTag();
      this.method294(var3);
      return var3;
   }

   private void method294(NbtCompound var1) {
      var1.putDouble("x", this.field596);
      var1.putDouble("y", this.field597);
      var1.putInt("a", this.field603);
      var1.putBoolean("sX", this.field604);
      var1.putBoolean("sY", this.field605);
   }

   @Override
   public Module fromTag(NbtCompound tag) {
      if (tag.contains("x")) {
         try {
            this.field596 = tag.getDouble("x");
         } catch (Exception var10) {
            ErrorLogger.log(var10);
            Boze.LOG.warn("Unable to load HUD module " + this.internalName + "'s x position from config");
         }
      }

      if (tag.contains("y")) {
         try {
            this.field597 = tag.getDouble("y");
         } catch (Exception var9) {
            ErrorLogger.log(var9);
            Boze.LOG.warn("Unable to load HUD module " + this.internalName + "'s y position from config");
         }
      }

      if (tag.contains("a")) {
         try {
            this.field603 = tag.getInt("a");
         } catch (Exception var8) {
            ErrorLogger.log(var8);
            Boze.LOG.warn("Unable to load HUD module " + this.internalName + "'s anchor position from config");
         }
      }

      if (tag.contains("sX")) {
         try {
            this.field604 = tag.getBoolean("sX");
         } catch (Exception var7) {
            ErrorLogger.log(var7);
            Boze.LOG.warn("Unable to load HUD module " + this.internalName + "'s x-axis snap from config");
         }
      }

      if (tag.contains("sY")) {
         try {
            this.field605 = tag.getBoolean("sY");
         } catch (Exception var6) {
            ErrorLogger.log(var6);
            Boze.LOG.warn("Unable to load HUD module " + this.internalName + "'s y-axis snap from config");
         }
      }

      return super.fromTag(tag);
   }

   public abstract void method295(DrawContext var1);

   protected void method296(String text, BozeDrawColor color, boolean shadow) {
      this.method314(IFontRender.method499().measureTextHeight(text, shadow) + (double)HUD.INSTANCE.field2377.getValue().intValue() * 2.0);
      this.method316(IFontRender.method499().method502(shadow) + (double)HUD.INSTANCE.field2378.getValue().intValue() * 2.0);
      if (HUD.INSTANCE.field2394.getValue()) {
         HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
      }

      IFontRender.method499()
         .drawShadowedText(
            text,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue(),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            color,
            shadow
         );
   }

   protected void method297(String a, String b, BozeDrawColor one, BozeDrawColor two, boolean shadow) {
      this.method314(IFontRender.method499().measureTextHeight(a + " " + b, shadow) + (double)HUD.INSTANCE.field2377.getValue().intValue() * 2.0);
      this.method316(IFontRender.method499().method502(shadow) + (double)HUD.INSTANCE.field2378.getValue().intValue() * 2.0);
      if (HUD.INSTANCE.field2394.getValue()) {
         HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
      }

      IFontRender.method499()
         .drawShadowedText(
            a,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue(),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            one,
            shadow
         );
      IFontRender.method499()
         .drawShadowedText(
            b,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue() + IFontRender.method499().method501(a + " "),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            two,
            shadow
         );
   }

   protected void method298(String a, String b, String c, BozeDrawColor one, BozeDrawColor two, BozeDrawColor three, boolean shadow) {
      this.method314(IFontRender.method499().measureTextHeight(a + " " + b + " " + c, shadow) + (double)HUD.INSTANCE.field2377.getValue().intValue() * 2.0);
      this.method316(IFontRender.method499().method502(shadow) + (double)HUD.INSTANCE.field2378.getValue().intValue() * 2.0);
      if (HUD.INSTANCE.field2394.getValue()) {
         HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
      }

      IFontRender.method499()
         .drawShadowedText(
            a,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue(),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            one,
            shadow
         );
      IFontRender.method499()
         .drawShadowedText(
            b,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue() + IFontRender.method499().method501(a + " "),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            two,
            shadow
         );
      IFontRender.method499()
         .drawShadowedText(
            c,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue() + IFontRender.method499().method501(a + " " + b + " "),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            three,
            shadow
         );
   }

   protected void method299(String a, String b, String c, BozeDrawColor one, BozeDrawColor two, BozeDrawColor three, boolean shadow) {
      this.method314(IFontRender.method499().measureTextHeight(a + b + c, shadow) + (double)HUD.INSTANCE.field2377.getValue().intValue() * 2.0);
      this.method316(IFontRender.method499().method502(shadow) + (double)HUD.INSTANCE.field2378.getValue().intValue() * 2.0);
      if (HUD.INSTANCE.field2394.getValue()) {
         HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
      }

      IFontRender.method499()
         .drawShadowedText(
            a,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue(),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            one,
            shadow
         );
      IFontRender.method499()
         .drawShadowedText(
            b,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue() + IFontRender.method499().method501(a),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            two,
            shadow
         );
      IFontRender.method499()
         .drawShadowedText(
            c,
            this.method1391() + (double)HUD.INSTANCE.field2377.getValue().intValue() + IFontRender.method499().method501(a + b),
            this.method305() + (double)HUD.INSTANCE.field2378.getValue().intValue(),
            three,
            shadow
         );
   }

   public void method300(double mouseX, double mouseY, double partialTicks) {
      if (this.method1973() && this.isEnabled()) {
         double var10 = mouseX + this.method317();
         double var12 = mouseY + this.method319();

         for (double var14 = 0.0; var14 <= 1.0; var14 += 0.1) {
            this.method65(Class3071.method6022(this.method1391(), var10, var14), this.method305());
         }

         for (double var16 = 0.0; var16 <= 1.0; var16 += 0.1) {
            this.method65(this.method1391(), Class3071.method6022(this.method305(), var12, var16));
         }
      }
   }

   public static double method2091() {
      return HUD.INSTANCE.field2376.getValue() * (double)Math.min(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
   }

   private boolean method65(double var1, double var3) {
      double var8 = method2091();
      if (var1 < 0.0 + var8) {
         var1 = var8;
      }

      if (var1 + this.method313() > (double)mc.getWindow().getScaledWidth() - var8) {
         var1 = (double)mc.getWindow().getScaledWidth() - this.method313() - var8;
      }

      if (var3 < 0.0 + var8) {
         var3 = var8;
      }

      if (var3 + this.method315() > (double)mc.getWindow().getScaledHeight() - var8) {
         var3 = (double)mc.getWindow().getScaledHeight() - this.method315() - var8;
      }

      if (!HUD.INSTANCE.field2381.getValue()) {
         for (Module var11 : Boze.getModules().modules) {
            if (var11 instanceof HUDModule && var11.isEnabled()) {
               HUDModule var12 = (HUDModule)var11;
               if (!this.method301(
                     var12.method1391() + 1.0,
                     var12.method305() + 1.0,
                     var12.method313() - 2.0,
                     var12.method315() - 2.0,
                     this.method1391() + 1.0,
                     this.method305() + 1.0,
                     this.method313() - 2.0,
                     this.method315() - 2.0
                  )
                  && this.method301(
                     var12.method1391() + 1.0,
                     var12.method305() + 1.0,
                     var12.method313() - 2.0,
                     var12.method315() - 2.0,
                     var1 + 1.0,
                     var3 + 1.0,
                     this.method313() - 2.0,
                     this.method315() - 2.0
                  )) {
                  return true;
               }
            }
         }
      }

      this.method938(var1);
      this.method306(var3);
      return false;
   }

   private boolean method301(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      if (!(var5 <= 0.0) && !(var7 <= 0.0) && !(var13 <= 0.0) && !(var15 <= 0.0)) {
         var5 += var1;
         var7 += var3;
         var13 += var9;
         var15 += var11;
         return (var5 < var1 || var5 > var9) && (var7 < var3 || var7 > var11) && (var13 < var9 || var13 > var1) && (var15 < var11 || var15 > var3);
      } else {
         return false;
      }
   }

   public double method1614() {
      double var3 = method2091();
       return switch (this.field603) {
           case 1, 3 -> var3;
           case 2, 4 -> (double) mc.getWindow().getScaledWidth() - var3;
           default -> 0.0;
       };
   }

   public double method1390() {
      double var3 = method2091();
       return switch (this.field603) {
           case 1, 2 -> var3;
           case 3, 4 -> (double) mc.getWindow().getScaledHeight() - var3;
           default -> 0.0;
       };
   }

   public double method1391() {
      if (this.field604) {
         return (double)mc.getWindow().getScaledWidth() * 0.5 - this.method313() * 0.5;
      } else {
          return switch (this.field603) {
              case 1, 3 -> this.method1614() + this.method307();
              case 2, 4 -> this.method1614() + this.method307() - this.field598;
              default -> 0.0;
          };
      }
   }

   public void method938(double posX) {
      double var6 = method2091();
      if (posX < 0.0 + var6) {
         posX = var6;
      }

      if (posX + this.method313() > (double)mc.getWindow().getScaledWidth() - var6) {
         posX = (double)mc.getWindow().getScaledWidth() - this.method313() - var6;
      }

      if (this.method305() + this.method315() / 2.0 > (double)((float)mc.getWindow().getScaledHeight() / 2.0F)) {
         double var8 = Math.abs(posX + this.method313() * 0.5 - (double)mc.getWindow().getScaledWidth() * 0.5);
         if (var8 < 0.03 * (double)mc.getWindow().getScaledWidth()) {
            this.method1649(3);
            this.method308(posX - this.method1614());
            this.field604 = true;
         } else if (posX + this.method313() / 2.0 > (double)((float)mc.getWindow().getScaledWidth() / 2.0F)) {
            this.method1649(4);
            this.method308(posX + this.method313() - this.method1614());
            this.field604 = false;
         } else {
            this.method1649(3);
            this.method308(posX - this.method1614());
            this.field604 = false;
         }
      } else {
         double var10 = Math.abs(posX + this.method313() * 0.5 - (double)mc.getWindow().getScaledWidth() * 0.5);
         if (var10 < 0.03 * (double)mc.getWindow().getScaledWidth()) {
            this.method1649(1);
            this.method308(posX - this.method1614());
            this.field604 = true;
         } else if (posX + this.method313() / 2.0 > (double)((float)mc.getWindow().getScaledWidth() / 2.0F)) {
            this.method1649(2);
            this.method308(posX + this.method313() - this.method1614());
            this.field604 = false;
         } else {
            this.method1649(1);
            this.method308(posX - this.method1614());
            this.field604 = false;
         }
      }
   }

   public double method305() {
      if (this.field605) {
         return (double)mc.getWindow().getScaledHeight() * 0.5 - this.method315() * 0.5;
      } else {
         double var4 = this.method322();
          return switch (this.field603) {
              case 1, 2 -> this.method1390() + this.method309() + var4;
              case 3, 4 -> this.method1390() + var4 + this.method309() - this.field599;
              default -> 0.0;
          };
      }
   }

   public void method306(double posY) {
      double var6 = method2091();
      if (posY < 0.0 + var6) {
         posY = var6;
      }

      if (posY + this.method313() > (double)mc.getWindow().getScaledWidth() - var6) {
         posY = (double)mc.getWindow().getScaledWidth() - this.method313() - var6;
      }

      if (this.method1391() + this.method313() / 2.0 > (double)((float)mc.getWindow().getScaledWidth() / 2.0F)) {
         double var8 = Math.abs(posY + this.method315() * 0.5 - (double)mc.getWindow().getScaledHeight() * 0.5);
         if (var8 < 0.03 * (double)mc.getWindow().getScaledHeight()) {
            this.method1649(2);
            this.method310(posY - this.method1390());
            this.field605 = true;
         } else if (posY + this.method315() / 2.0 > (double)((float)mc.getWindow().getScaledHeight() / 2.0F)) {
            this.method1649(4);
            this.method310(posY + this.method315() - this.method1390());
            this.field605 = false;
         } else {
            this.method1649(2);
            this.method310(posY - this.method1390());
            this.field605 = false;
         }
      } else {
         double var10 = Math.abs(posY + this.method315() * 0.5 - (double)mc.getWindow().getScaledHeight() * 0.5);
         if (var10 < 0.03 * (double)mc.getWindow().getScaledHeight()) {
            this.method1649(1);
            this.method310(posY - this.method1390());
            this.field605 = true;
         } else if (posY + this.method315() / 2.0 > (double)((float)mc.getWindow().getScaledHeight() / 2.0F)) {
            this.method1649(3);
            this.method310(posY + this.method315() - this.method1390());
            this.field605 = false;
         } else {
            this.method1649(1);
            this.method310(posY - this.method1390());
            this.field605 = false;
         }
      }
   }

   public double method307() {
      return this.field596;
   }

   public void method308(double offsetX) {
      this.field596 = offsetX;
   }

   public double method309() {
      return this.field597;
   }

   public void method310(double offsetY) {
      this.field597 = offsetY;
   }

   public boolean method1971() {
      return this.field604;
   }

   public void method1771(boolean snapX) {
      this.field604 = snapX;
   }

   public boolean method1972() {
      return this.field605;
   }

   public void method312(boolean snapY) {
      this.field605 = snapY;
   }

   public double method313() {
      return this.field598;
   }

   public void method314(double width) {
      this.field598 = width;
   }

   public double method315() {
      return this.field599;
   }

   public void method316(double height) {
      this.field599 = height;
   }

   public double method317() {
      return this.field600;
   }

   public void method318(double prevPosX) {
      this.field600 = prevPosX;
   }

   public double method319() {
      return this.field601;
   }

   public void method320(double prevPosY) {
      this.field601 = prevPosY;
   }

   public boolean method1973() {
      return mc.currentScreen instanceof ClickGUI ? this.field602 : false;
   }

   public void method321(boolean dragging) {
      this.field602 = dragging;
   }

   public int method2010() {
      return this.field603;
   }

   public void method1649(int anchor) {
      this.field603 = anchor;
   }

   protected double method322() {
      if (mc.currentScreen instanceof ChatScreen
         && HUD.INSTANCE.field2379.getValue()
         && (Math.abs(this.method307()) < 5.0 || this.method309() == 0.0)
         && this.method2010() > 2) {
         return -15.0;
      } else {
         if (HUD.INSTANCE.field2380.getValue() && this.method307() < 5.0 && this.method2010() == 2) {
            int var4 = 0;

            for (int var5 = 0; var5 < ((ToastManagerAccessor)mc.getToastManager()).getVisibleEntries().size(); var5++) {
               if (((ToastManagerAccessor)mc.getToastManager()).getVisibleEntries().get(var5) != null) {
                  var4 = var5 + 1;
               }
            }

            if (var4 > 0) {
               return (double)(var4 * 32 + 4);
            }
         }

         return 0.0;
      }
   }

   protected boolean method1974() {
      return mc.currentScreen instanceof ChatScreen && HUD.INSTANCE.field2380.getValue() && this.method307() == 0.0;
   }

   public static boolean method323(double mouseX, double mouseY, double x, double y, double width, double height) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
   }

   public boolean method324(double mouseX, double mouseY, int mouseButton) {
      if (!this.isEnabled()) {
         return false;
      } else {
         boolean var9 = method323(mouseX, mouseY, this.method1391(), this.method305(), this.method313(), this.method315());
         boolean var10 = false;
         switch (mouseButton) {
            case 0:
               if (var9) {
                  this.method321(true);
                  this.method318(this.method1391() - mouseX);
                  this.method320(this.method305() - mouseY);
                  var10 = true;
               }
               break;
            case 1:
               if (var9) {
                  this.setEnabled(!this.isEnabled());
                  var10 = true;
               }
         }

         return var10;
      }
   }

   public void method325(double mouseX, double mouseY, int mouseButton) {
      if (mouseButton == 0 && this.method1973()) {
         this.method321(false);
      }
   }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object fromTag(NbtCompound nbtCompound) {
   //   return this.fromTag(nbtCompound);
   //}
}
