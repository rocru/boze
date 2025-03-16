package dev.boze.client.gui.components;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.Scissor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public abstract class BottomRowScaledComponent extends ScaledBaseComponent {
   private double field1419;
   private double field1420;
   private double field1421;
   private int field1422 = 0;
   private BottomRow field1423;
   private final int field1424;
   private Runnable field1425 = null;
   private final boolean field1426;
   protected String field1427 = "";

   public BottomRowScaledComponent(String name, BottomRow bottomRow, double widthRatio, double heightRatio) {
      this(name, bottomRow, widthRatio, heightRatio, 0);
   }

   public BottomRowScaledComponent(String name, BottomRow bottomRow, double widthRatio, double heightRatio, int extraComponents) {
      super(name, 0.0, 0.0);
      this.field1423 = bottomRow;
      this.field1426 = bottomRow == BottomRow.TextAddClose;
      this.field1424 = extraComponents;
      double var11 = heightRatio * 720.0 * BaseComponent.scaleFactor;
      double var13 = extraComponents > 0
         ? (double)extraComponents * ((double)Theme.method1376() * BaseComponent.scaleFactor + BaseComponent.scaleFactor * 6.0)
            + BaseComponent.scaleFactor * 6.0 * (double)extraComponents
         : 0.0;
      this.field1390 = Math.min(widthRatio * 1280.0 * BaseComponent.scaleFactor, (double)mc.getWindow().getScaledWidth());
      this.field1391 = Math.min(var11 + var13, (double)mc.getWindow().getScaledHeight());
      this.field1388 = (double)mc.getWindow().getScaledWidth() * 0.5 - this.field1390 * 0.5;
      this.field1389 = (double)mc.getWindow().getScaledHeight() * 0.5 - this.field1391 * 0.5;
   }

   protected void method1416() {
      this.field1422 = MathHelper.clamp(this.field1422, 0, MathHelper.clamp(this.method2010() - 12, 0, this.method2010()));
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
            this.field1387,
            this.field1388 + this.field1390 * 0.5 - IFontRender.method499().method501(this.field1387) * 0.5,
            this.field1389 + BaseComponent.scaleFactor * 6.0,
            Theme.method1350()
         );
      this.field1419 = this.field1389 + BaseComponent.scaleFactor * 12.0 + IFontRender.method499().method1390();
      double var8 = this.field1391 - IFontRender.method499().method1390() * 2.0 - BaseComponent.scaleFactor * 30.0;
      this.field1420 = var8
         - (
            this.field1424 > 0
               ? (double)this.field1424 * ((double)Theme.method1376() * BaseComponent.scaleFactor + BaseComponent.scaleFactor * 2.0)
                  + BaseComponent.scaleFactor * 6.0 * (double)this.field1424
               : 0.0
         );
      this.field1421 = (this.field1420 - BaseComponent.scaleFactor * 34.0) / 12.0;
      RenderUtil.field3963
         .method2257(
            this.field1388 + BaseComponent.scaleFactor * 12.0,
            this.field1419,
            this.field1390 - BaseComponent.scaleFactor * 24.0,
            this.field1420,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
            Theme.method1348()
         );
      this.method1416();
      int var10 = this.method2010();

      for (int var11 = 0; var11 < Math.min(var10, 12); var11++) {
         double var12 = this.field1419 + (double)var11 * (this.field1421 + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0;
         this.method639(
            context,
            var11 + this.field1422,
            this.field1388 + BaseComponent.scaleFactor * 18.0,
            var12,
            this.field1390 - BaseComponent.scaleFactor * 36.0,
            this.field1421
         );
      }

      if (this.field1424 > 0) {
         double var14 = this.field1419 + this.field1420 + BaseComponent.scaleFactor * 6.0;
         this.method641(context, var14, this.field1421);
      }

      this.method1964(mouseX, mouseY);
      RenderUtil.field3963.method2235(context);
      IFontRender.method499().endBuilding();
      IconManager.method1115();

      for (int var15 = 0; var15 < Math.min(var10, 12); var15++) {
         double var16 = this.field1419 + (double)var15 * (this.field1421 + BaseComponent.scaleFactor * 2.0) + BaseComponent.scaleFactor * 6.0;
         this.method640(
            context,
            var15 + this.field1422,
            this.field1388 + BaseComponent.scaleFactor * 18.0,
            var16,
            this.field1390 - BaseComponent.scaleFactor * 36.0,
            this.field1421
         );
      }

      this.method332(context);
      if (this.field1425 != null) {
         this.field1425.run();
         this.field1425 = null;
      }
   }

   protected void method635(Runnable callback) {
      this.field1425 = callback;
   }

   private void method1964(int param1, int param2) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: aload 0
      // 01: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1423 Ldev/boze/client/enums/BottomRow;
      // 04: invokevirtual dev/boze/client/enums/BottomRow.ordinal ()I
      // 07: lookupswitch 38 2 0 25 1 32
      // 20: aload 0
      // 21: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.method1198 ()V
      // 24: goto 2d
      // 27: aload 0
      // 28: iload 1
      // 29: iload 2
      // 2a: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.method1190 (II)V
      // 2d: return
   }

   private void method332(DrawContext param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: aload 0
      // 01: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1423 Ldev/boze/client/enums/BottomRow;
      // 04: invokevirtual dev/boze/client/enums/BottomRow.ordinal ()I
      // 07: lookupswitch 22 1 1 17
      // 18: aload 0
      // 19: aload 1
      // 1a: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.method295 (Lnet/minecraft/client/gui/DrawContext;)V
      // 1d: return
   }

   protected void method1198() {
      double var3 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
      double var5 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
      double var7 = this.field1389 + this.field1391 - var5 - BaseComponent.scaleFactor * 6.0;
      double var9 = this.field1388 + this.field1390 * 0.25 - var3 * 0.5;
      RenderUtil.field3963.method2257(var9, var7, var3, var5, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            "Add",
            var9 + var3 * 0.5 - IFontRender.method499().method501("Add") * 0.5,
            var7 + var5 * 0.5 - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      double var11 = this.field1388 + this.field1390 * 0.75 - var3 * 0.5;
      RenderUtil.field3963.method2257(var11, var7, var3, var5, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            "Close",
            var11 + var3 * 0.5 - IFontRender.method499().method501("Close") * 0.5,
            var7 + var5 * 0.5 - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
   }

   protected void handleAddAndCloseClick(double mouseX, double mouseY) {
      double var8 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
      double var10 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
      double var12 = this.field1389 + this.field1391 - var10 - BaseComponent.scaleFactor * 6.0;
      double var14 = this.field1388 + this.field1390 * 0.25 - var8 * 0.5;
      if (isMouseWithinBounds(mouseX, mouseY, var14, var12, var8, var10)) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         this.method1904();
      } else {
         double var16 = this.field1388 + this.field1390 * 0.75 - var8 * 0.5;
         if (isMouseWithinBounds(mouseX, mouseY, var16, var12, var8, var10)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(null);
         }
      }
   }

   private void method295(DrawContext var1) {
      RenderUtil.field3963.method2233();
      IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
      double var5 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
      double var7 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
      double var9 = this.field1389 + this.field1391 - var7 - BaseComponent.scaleFactor * 6.0;
      double var11 = this.field1388 + BaseComponent.scaleFactor * 12.0;
      double var13 = this.field1390 - var5 * 2.0 - BaseComponent.scaleFactor * 36.0;
      Scissor.enableScissor(var11, var9, var13, var7);
      RenderUtil.field3963.method2257(var11, var9, var13, var7, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1348());
      String var15 = this.field1427 + "|";
      double var16 = var11 + BaseComponent.scaleFactor * 4.0;
      double var18 = IFontRender.method499().method501(var15);
      if (var18 > var13 - BaseComponent.scaleFactor * 4.0) {
         double var20 = var18 - (var13 - BaseComponent.scaleFactor * 4.0);
         var16 -= var20;
      }

      if (System.currentTimeMillis() % 1000L >= 500L) {
         var15 = var15.substring(0, var15.length() - 1);
      }

      IFontRender.method499().drawShadowedText(var15, var16, var9 + var7 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
      RenderUtil.field3963.method2235(var1);
      IFontRender.method499().endBuilding();
      Scissor.disableScissor();
   }

   private void method1190(int var1, int var2) {
      double var5 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
      double var7 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
      double var9 = this.field1389 + this.field1391 - var7 - BaseComponent.scaleFactor * 6.0;
      double var11 = this.field1388 + BaseComponent.scaleFactor * 12.0;
      double var13 = this.field1390 - var5 * 2.0 - BaseComponent.scaleFactor * 36.0;
      double var15 = var11 + var13 + BaseComponent.scaleFactor * 6.0;
      RenderUtil.field3963.method2257(var15, var9, var5, var7, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            "Add",
            var15 + var5 * 0.5 - IFontRender.method499().method501("Add") * 0.5,
            var9 + var7 * 0.5 - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
      double var17 = var15 + var5 + BaseComponent.scaleFactor * 6.0;
      RenderUtil.field3963.method2257(var17, var9, var5, var7, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            "Close",
            var17 + var5 * 0.5 - IFontRender.method499().method501("Close") * 0.5,
            var9 + var7 * 0.5 - IFontRender.method499().method1390() * 0.5,
            Theme.method1350()
         );
   }

   private void method637(double var1, double var3) {
      double var8 = Math.max(IFontRender.method499().method501("Add"), IFontRender.method499().method501("Close")) + BaseComponent.scaleFactor * 6.0;
      double var10 = IFontRender.method499().method1390() + BaseComponent.scaleFactor * 6.0;
      double var12 = this.field1389 + this.field1391 - var10 - BaseComponent.scaleFactor * 6.0;
      double var14 = this.field1388 + BaseComponent.scaleFactor * 12.0;
      double var16 = this.field1390 - var8 * 2.0 - BaseComponent.scaleFactor * 36.0;
      double var18 = var14 + var16 + BaseComponent.scaleFactor * 6.0;
      if (isMouseWithinBounds(var1, var3, var18, var12, var8, var10)) {
         this.method1904();
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
      } else {
         double var20 = var18 + var8 + BaseComponent.scaleFactor * 6.0;
         if (isMouseWithinBounds(var1, var3, var20, var12, var8, var10)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            ClickGUI.field1335.method580(null);
         }
      }
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
         IFontRender var9 = IFontRender.method499();
         var9.startBuilding(BaseComponent.scaleFactor * 0.5, true);
         IconManager.applyScale(BaseComponent.scaleFactor * 0.4);
         this.method638(mouseX, mouseY, button);
         var9.endBuilding();
         IconManager.method1115();
         return true;
      } else {
         return super.isMouseOver(mouseX, mouseY, button);
      }
   }

   protected void method638(double mouseX, double mouseY, int button) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: aload 0
      // 01: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1423 Ldev/boze/client/enums/BottomRow;
      // 04: invokevirtual dev/boze/client/enums/BottomRow.ordinal ()I
      // 07: lookupswitch 40 2 0 25 1 34
      // 20: aload 0
      // 21: dload 1
      // 22: dload 3
      // 23: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.handleAddAndCloseClick (DD)V
      // 26: goto 2f
      // 29: aload 0
      // 2a: dload 1
      // 2b: dload 3
      // 2c: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.method637 (DD)V
      // 2f: dload 1
      // 30: dload 3
      // 31: aload 0
      // 32: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1388 D
      // 35: getstatic dev/boze/client/gui/components/BaseComponent.scaleFactor D
      // 38: ldc2_w 12.0
      // 3b: dmul
      // 3c: dadd
      // 3d: aload 0
      // 3e: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1419 D
      // 41: aload 0
      // 42: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1390 D
      // 45: getstatic dev/boze/client/gui/components/BaseComponent.scaleFactor D
      // 48: ldc2_w 24.0
      // 4b: dmul
      // 4c: dsub
      // 4d: aload 0
      // 4e: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1420 D
      // 51: invokestatic dev/boze/client/gui/components/BottomRowScaledComponent.isMouseWithinBounds (DDDDDD)Z
      // 54: ifeq e8
      // 57: bipush 0
      // 58: istore 9
      // 5a: iload 9
      // 5c: aload 0
      // 5d: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.method2010 ()I
      // 60: bipush 12
      // 62: invokestatic java/lang/Math.min (II)I
      // 65: if_icmpge e8
      // 68: aload 0
      // 69: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1419 D
      // 6c: iload 9
      // 6e: i2d
      // 6f: aload 0
      // 70: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1421 D
      // 73: getstatic dev/boze/client/gui/components/BaseComponent.scaleFactor D
      // 76: ldc2_w 2.0
      // 79: dmul
      // 7a: dadd
      // 7b: dmul
      // 7c: dadd
      // 7d: getstatic dev/boze/client/gui/components/BaseComponent.scaleFactor D
      // 80: ldc2_w 6.0
      // 83: dmul
      // 84: dadd
      // 85: dstore 10
      // 87: aload 0
      // 88: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1388 D
      // 8b: getstatic dev/boze/client/gui/components/BaseComponent.scaleFactor D
      // 8e: ldc2_w 18.0
      // 91: dmul
      // 92: dadd
      // 93: dstore 12
      // 95: aload 0
      // 96: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1390 D
      // 99: getstatic dev/boze/client/gui/components/BaseComponent.scaleFactor D
      // 9c: ldc2_w 36.0
      // 9f: dmul
      // a0: dsub
      // a1: dstore 14
      // a3: dload 1
      // a4: dload 3
      // a5: dload 12
      // a7: dload 10
      // a9: dload 14
      // ab: aload 0
      // ac: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1421 D
      // af: invokestatic dev/boze/client/gui/components/BottomRowScaledComponent.isMouseWithinBounds (DDDDDD)Z
      // b2: ifeq e2
      // b5: aload 0
      // b6: iload 9
      // b8: aload 0
      // b9: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1422 I
      // bc: iadd
      // bd: iload 5
      // bf: dload 12
      // c1: dload 10
      // c3: dload 14
      // c5: aload 0
      // c6: getfield dev/boze/client/gui/components/BottomRowScaledComponent.field1421 D
      // c9: dload 1
      // ca: dload 3
      // cb: invokevirtual dev/boze/client/gui/components/BottomRowScaledComponent.handleItemClick (IIDDDDDD)Z
      // ce: ifeq e1
      // d1: getstatic dev/boze/client/gui/components/BottomRowScaledComponent.mc Lnet/minecraft/client/MinecraftClient;
      // d4: invokevirtual net/minecraft/client/MinecraftClient.getSoundManager ()Lnet/minecraft/client/sound/SoundManager;
      // d7: getstatic net/minecraft/sound/SoundEvents.UI_BUTTON_CLICK Lnet/minecraft/registry/entry/RegistryEntry$Reference;
      // da: fconst_1
      // db: invokestatic net/minecraft/client/sound/PositionedSoundInstance.master (Lnet/minecraft/registry/entry/RegistryEntry;F)Lnet/minecraft/client/sound/PositionedSoundInstance;
      // de: invokevirtual net/minecraft/client/sound/SoundManager.play (Lnet/minecraft/client/sound/SoundInstance;)V
      // e1: return
      // e2: iinc 9 1
      // e5: goto 5a
      // e8: return
   }

   @Override
   public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
      if (isMouseWithinBounds(
         mouseX, mouseY, this.field1388 + BaseComponent.scaleFactor * 12.0, this.field1419, this.field1390 - BaseComponent.scaleFactor * 24.0, this.field1420
      )) {
         this.field1422 = (int)MathHelper.clamp((double)this.field1422 - amount, 0.0, (double)MathHelper.clamp(this.method2010() - 12, 0, this.method2010()));
         return true;
      } else {
         return super.onMouseScroll(mouseX, mouseY, amount);
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      return this.field1426 ? this.method2077(keyCode, scanCode, modifiers) : super.keyPressed(keyCode, scanCode, modifiers);
   }

   protected boolean method2077(int keyCode, int scanCode, int modifiers) {
      if (!this.field1426) {
         return false;
      } else if (keyCode == 257) {
         this.method1904();
         this.field1427 = "";
         return true;
      } else if (keyCode == 259 && this.field1427.length() > 0) {
         this.field1427 = this.field1427.substring(0, this.field1427.length() - 1);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void method583(char c) {
      if (this.field1426) {
         if (this.field1427.length() < 18 && c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_' || c == '-') {
            this.field1427 = this.field1427 + c;
         }
      }
   }

   protected abstract int method2010();

   protected abstract void method639(DrawContext var1, int var2, double var3, double var5, double var7, double var9);

   protected void method640(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
   }

   protected abstract boolean handleItemClick(int var1, int var2, double var3, double var5, double var7, double var9, double var11, double var13);

   protected abstract void method1904();

   protected void method641(DrawContext context, double startY, double height) {
   }
}
