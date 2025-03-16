package mapped;

import dev.boze.client.renderer.Mesh;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import java.util.Collection;
import java.util.HashMap;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawContext;

public class Class3032 implements IMinecraft {
   private static final HashMap<String, Float> field133 = new HashMap();
   private static Framebuffer field134;
   private static long field135 = System.nanoTime();

   public static float method5931(String var0) {
      return (Float)field133.getOrDefault(var0, 0.0F);
   }

   private static void method2142() {
      if (field134 == null) {
         field134 = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
         field135 = System.nanoTime();
      }
   }

   public static int method2010() {
      method2142();
      return field134.getColorAttachment();
   }

   public static void method332(DrawContext var0) {
      method2142();
      Collection var4 = GradientColor.field420.values();
      int var5 = var4.size();
      if (var5 != 0) {
         boolean var6 = var4.stream().anyMatch(Class3032::lambda$render$0);
         boolean var7 = var4.stream().anyMatch(Class3032::lambda$render$1);
         field134.beginWrite(false);
         if (var6) {
            RenderUtil.field3963.method2233();
         }

         if (var7) {
            RenderUtil.field3966.method2233();
         }

         double var8 = (double)mc.getWindow().getScaledHeight() / (double)var5;
         float var10 = 1.0F / (float)var5;
         float var11 = var10 / 2.0F;
         double var12 = 0.0;
         float var14 = var11;
         float var15 = (float)(System.nanoTime() - field135) / 1.0E9F;
         field135 = System.nanoTime();

         for (GradientColor var17 : var4) {
            method5935(var17.field424 ? RenderUtil.field3966 : RenderUtil.field3963, var17, 0.0, var12, (double)mc.getWindow().getScaledWidth(), var8);
            field133.put(var17.field423, var14);
            var12 += var8;
            var14 += var10;
            var17.field426 = var17.field426 + var17.field427 * var15;
            var17.field426 %= 1.0F;
            if (var17.field426 < 0.0F) {
               var17.field426++;
            }
         }

         if (var6) {
            RenderUtil.field3963.method2235(var0);
         }

         if (var7) {
            RenderUtil.field3966.method2235(var0);
         }

         mc.getFramebuffer().beginWrite(false);
      }
   }

   public static void method5935(RenderUtil var0, GradientColor var1, double var2, double var4, double var6, double var8) {
      long var13 = System.currentTimeMillis();
      float var15 = (float)(var13 - Renderer3D.field2172) / 1000.0F;
      float var16 = var15 * var1.field429 % 1.0F;
      if (var16 < 0.0F) {
         var16++;
      }

      int var17 = var1.field422.size();
      StaticColor[] var18 = new StaticColor[var17 + (var1.field425 ? var17 - 1 : 1)];

      int var19;
      for (var19 = 0; var19 < var17; var19++) {
         var18[var19] = (StaticColor)var1.field422.get(var19);
      }

      float var20 = var1.field428;
      if (var1.field425) {
         for (int var21 = var17 - 2; var19 < var18.length; var19++) {
            var18[var19] = (StaticColor)var1.field422.get(var21--);
         }
      } else {
         var18[var17] = (StaticColor)var1.field422.get(0);
         var20 *= 1.0F / ((float)var18.length - 2.0F) / (1.0F / ((float)var18.length - 1.0F));
      }

      float[] var34 = new float[var18.length];

      for (int var31 = 0; var31 < var18.length; var31++) {
         float var22 = (float)var31 / (float)(var18.length - 1);
         var34[var31] = (var22 + var16) % 1.0F;
         if (var34[var31] < 0.0F) {
            var34[var31]++;
         }

         var34[var31] *= var20;
      }

      double var35 = var6 / (double)(var18.length - 1) * (double)var20;
      int var24 = 0;
      float var25 = var34[0];

      for (int var32 = 1; var32 < var34.length; var32++) {
         if (var34[var32] < var25) {
            var24 = var32;
            var25 = var34[var32];
         }
      }

      if (var34[var24] != 0.0F) {
         int var26 = var24 - 1;
         if (var26 < 0) {
            var26 += var34.length - 1;
         }

         StaticColor var28 = var18[var26];
         StaticColor var29 = var18[var24];
         if (var1.field424) {
            method5937(var0.field3971, var2 + var6 * (double)var34[var24] - var35, var4, var35, var8, var28, var29);
         } else {
            method5936(var0.field3971, var2 + var6 * (double)var34[var24] - var35, var4, var35, var8, var28, var29);
         }
      }

      for (float var36 = 0.0F; (double)var36 < var6; var36 += (float)(var35 * (double)(var18.length - 1))) {
         for (int var33 = 0; var33 < var18.length - 1; var33++) {
            double var27 = (double)var34[var33] * var6 + (double)var36;
            StaticColor var37 = var18[var33];
            StaticColor var30 = var18[var33 + 1];
            if (var1.field424) {
               method5937(var0.field3971, var2 + var27, var4, var35, var8, var37, var30);
            } else {
               method5936(var0.field3971, var2 + var27, var4, var35, var8, var37, var30);
            }
         }
      }
   }

   private static void method5936(Mesh var0, double var1, double var3, double var5, double var7, StaticColor var9, StaticColor var10) {
      var0.method1214(
         var0.method711(var1, var3).method708(var9, 1.0F).method2010(),
         var0.method711(var1, var3 + var7).method708(var9, 1.0F).method2010(),
         var0.method711(var1 + var5, var3 + var7).method708(var10, 1.0F).method2010(),
         var0.method711(var1 + var5, var3).method708(var10, 1.0F).method2010()
      );
   }

   private static void method5937(Mesh var0, double var1, double var3, double var5, double var7, StaticColor var9, StaticColor var10) {
      float[] var11 = RGBAColor.method191(var9.field430, var9.field431, var9.field432);
      float[] var12 = RGBAColor.method191(var10.field430, var10.field431, var10.field432);
      var0.method1214(
         var0.method711(var1, var3).method706(var11).method2010(),
         var0.method711(var1, var3 + var7).method706(var11).method2010(),
         var0.method711(var1 + var5, var3 + var7).method706(var12).method2010(),
         var0.method711(var1 + var5, var3).method706(var12).method2010()
      );
   }

   public static void method1964(int var0, int var1) {
      if (field134 != null) {
         field134.resize(var0, var1, false);
      }
   }

   private static boolean lambda$render$1(GradientColor var0) {
      return var0.field424;
   }

   private static boolean lambda$render$0(GradientColor var0) {
      return !var0.field424;
   }
}
