package dev.boze.client.utils;

import dev.boze.client.utils.misc.ICopyable;
import dev.boze.client.utils.misc.ISerializable;
import java.awt.Color;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

public class RGBAColor implements ICopyable<RGBAColor>, ISerializable<RGBAColor> {
   public static final RGBAColor field402 = new RGBAColor();
   public static final RGBAColor field403 = new RGBAColor(255, 0, 0, 255);
   public static final RGBAColor field404 = new RGBAColor(255, 200, 0, 255);
   public static final RGBAColor field405 = new RGBAColor(0, 255, 0, 255);
   public static final RGBAColor field406 = new RGBAColor(0, 0, 255, 255);
   public static final RGBAColor field407 = new RGBAColor(35, 35, 35, 255);
   public int field408;
   public int field409;
   public int field410;
   public int field411;

   public RGBAColor() {
      this(255, 255, 255, 255);
   }

   public RGBAColor(int var1, int var2, int var3) {
      this.field408 = var1;
      this.field409 = var2;
      this.field410 = var3;
      this.field411 = 255;
      this.method2142();
   }

   public RGBAColor(int var1, int var2, int var3, int var4) {
      this.field408 = var1;
      this.field409 = var2;
      this.field410 = var3;
      this.field411 = var4;
      this.method2142();
   }

   public RGBAColor(float var1, float var2, float var3, float var4) {
      this.field408 = (int)(var1 * 255.0F);
      this.field409 = (int)(var2 * 255.0F);
      this.field410 = (int)(var3 * 255.0F);
      this.field411 = (int)(var4 * 255.0F);
      this.method2142();
   }

   public RGBAColor(int var1) {
      this.field408 = method1541(var1);
      this.field409 = method186(var1);
      this.field410 = method187(var1);
      this.field411 = method188(var1);
   }

   public RGBAColor(RGBAColor var1) {
      this.field408 = var1.field408;
      this.field409 = var1.field409;
      this.field410 = var1.field410;
      this.field411 = var1.field411;
   }

   public RGBAColor(Color var1) {
      this.field408 = var1.getRed();
      this.field409 = var1.getGreen();
      this.field410 = var1.getBlue();
      this.field411 = var1.getAlpha();
   }

   public RGBAColor method2025(double var1) {
      return new RGBAColor(
         Math.max((int)((double)this.field408 * var1), 0),
         Math.max((int)((double)this.field409 * var1), 0),
         Math.max((int)((double)this.field410 * var1), 0),
         this.field411
      );
   }

   public RGBAColor method183(double var1) {
      int var6 = (int)(1.0 / (1.0 - var1));
      if (this.field408 == 0 && this.field409 == 0 && this.field410 == 0) {
         return new RGBAColor(var6, var6, var6, this.field411);
      } else {
         if (this.field408 > 0 && this.field408 < var6) {
            this.field408 = var6;
         }

         if (this.field409 > 0 && this.field409 < var6) {
            this.field409 = var6;
         }

         if (this.field410 > 0 && this.field410 < var6) {
            this.field410 = var6;
         }

         return new RGBAColor(
            Math.min((int)((double)this.field408 / var1), 255),
            Math.min((int)((double)this.field409 / var1), 255),
            Math.min((int)((double)this.field410 / var1), 255),
            this.field411
         );
      }
   }

   public static int method184(int var0, int var1, int var2, int var3) {
      return (var0 << 16) + (var1 << 8) + var2 + (var3 << 24);
   }

   public static int method1541(int var0) {
      return var0 >> 16 & 0xFF;
   }

   public static int method186(int var0) {
      return var0 >> 8 & 0xFF;
   }

   public static int method187(int var0) {
      return var0 & 0xFF;
   }

   public static int method188(int var0) {
      return var0 >> 24 & 0xFF;
   }

   public static RGBAColor method189(double var0, double var2, double var4) {
      if (var2 <= 0.0) {
         return new RGBAColor((int)(var4 * 255.0), (int)(var4 * 255.0), (int)(var4 * 255.0), 255);
      } else {
         double var8 = var0;
         if (var0 >= 360.0) {
            var8 = 0.0;
         }

         var8 /= 60.0;
         int var18 = (int)var8;
         double var16 = var8 - (double)var18;
         double var10 = var4 * (1.0 - var2);
         double var12 = var4 * (1.0 - var2 * var16);
         double var14 = var4 * (1.0 - var2 * (1.0 - var16));
         double var19;
         double var21;
         double var23;
         switch (var18) {
            case 0:
               var19 = var4;
               var21 = var14;
               var23 = var10;
               break;
            case 1:
               var19 = var12;
               var21 = var4;
               var23 = var10;
               break;
            case 2:
               var19 = var10;
               var21 = var4;
               var23 = var14;
               break;
            case 3:
               var19 = var10;
               var21 = var12;
               var23 = var4;
               break;
            case 4:
               var19 = var14;
               var21 = var10;
               var23 = var4;
               break;
            case 5:
            default:
               var19 = var4;
               var21 = var10;
               var23 = var12;
         }

         return new RGBAColor((int)(var19 * 255.0), (int)(var21 * 255.0), (int)(var23 * 255.0), 255);
      }
   }

   public static int[] method190(double var0, double var2, double var4) {
      if (var2 <= 0.0) {
         return new int[]{(int)(var4 * 255.0), (int)(var4 * 255.0), (int)(var4 * 255.0)};
      } else {
         double var9 = var0;
         if (var0 >= 360.0) {
            var9 = 0.0;
         }

         if (var9 < 0.0) {
            var9 = 359.0;
         }

         var9 /= 60.0;
         int var19 = (int)var9;
         double var17 = var9 - (double)var19;
         double var11 = var4 * (1.0 - var2);
         double var13 = var4 * (1.0 - var2 * var17);
         double var15 = var4 * (1.0 - var2 * (1.0 - var17));
         double var20;
         double var22;
         double var24;
         switch (var19) {
            case 0:
               var20 = var4;
               var22 = var15;
               var24 = var11;
               break;
            case 1:
               var20 = var13;
               var22 = var4;
               var24 = var11;
               break;
            case 2:
               var20 = var11;
               var22 = var4;
               var24 = var15;
               break;
            case 3:
               var20 = var11;
               var22 = var13;
               var24 = var4;
               break;
            case 4:
               var20 = var15;
               var22 = var11;
               var24 = var4;
               break;
            case 5:
            default:
               var20 = var4;
               var22 = var11;
               var24 = var13;
         }

         return new int[]{(int)(var20 * 255.0), (int)(var22 * 255.0), (int)(var24 * 255.0)};
      }
   }

   public static float[] method191(int var0, int var1, int var2) {
      float[] var12 = new float[3];
      int var13 = var0 > var1 ? var0 : var1;
      if (var2 > var13) {
         var13 = var2;
      }

      int var14 = var0 < var1 ? var0 : var1;
      if (var2 < var14) {
         var14 = var2;
      }

      double var10 = (double)var13 / 255.0;
      double var8;
      if (var13 != 0) {
         var8 = (double)(var13 - var14) / (double)var13;
      } else {
         var8 = 0.0;
      }

      double var6;
      if (var8 == 0.0) {
         var6 = 0.0;
      } else {
         double var15 = (double)(var13 - var0) / (double)(var13 - var14);
         double var17 = (double)(var13 - var1) / (double)(var13 - var14);
         double var19 = (double)(var13 - var2) / (double)(var13 - var14);
         if (var0 == var13) {
            var6 = var19 - var17;
         } else if (var1 == var13) {
            var6 = 2.0 + var15 - var19;
         } else {
            var6 = 4.0 + var17 - var15;
         }

         var6 /= 6.0;
         if (var6 < 0.0) {
            var6++;
         }
      }

      var12[0] = (float)var6;
      var12[1] = (float)var8;
      var12[2] = (float)var10;
      return var12;
   }

   public RGBAColor method192(int var1, int var2, int var3, int var4) {
      this.field408 = var1;
      this.field409 = var2;
      this.field410 = var3;
      this.field411 = var4;
      this.method2142();
      return this;
   }

   public RGBAColor method193(int var1) {
      this.field408 = var1;
      this.method2142();
      return this;
   }

   public RGBAColor method194(int var1) {
      this.field409 = var1;
      this.method2142();
      return this;
   }

   public RGBAColor method195(int var1) {
      this.field410 = var1;
      this.method2142();
      return this;
   }

   public RGBAColor method196(int var1) {
      this.field411 = var1;
      this.method2142();
      return this;
   }

   public RGBAColor method197(float var1) {
      this.field411 = (int)((float)this.field411 * var1);
      this.method2142();
      return this;
   }

   @Override
   public RGBAColor set(RGBAColor var1) {
      this.field408 = var1.field408;
      this.field409 = var1.field409;
      this.field410 = var1.field410;
      this.field411 = var1.field411;
      this.method2142();
      return this;
   }

   public boolean method1701(String var1) {
      String[] var5 = var1.split(",");
      if (var5.length != 3 && var5.length != 4) {
         return false;
      } else {
         try {
            int var6 = Integer.parseInt(var5[0]);
            int var7 = Integer.parseInt(var5[1]);
            int var8 = Integer.parseInt(var5[2]);
            int var9 = var5.length == 4 ? Integer.parseInt(var5[3]) : this.field411;
            this.field408 = var6;
            this.field409 = var7;
            this.field410 = var8;
            this.field411 = var9;
            return true;
         } catch (NumberFormatException var10) {
            return false;
         }
      }
   }

   @Override
   public RGBAColor copy() {
      return new RGBAColor(this.field408, this.field409, this.field410, this.field411);
   }

   public void method2142() {
      if (this.field408 < 0) {
         this.field408 = 0;
      } else if (this.field408 > 255) {
         this.field408 = 255;
      }

      if (this.field409 < 0) {
         this.field409 = 0;
      } else if (this.field409 > 255) {
         this.field409 = 255;
      }

      if (this.field410 < 0) {
         this.field410 = 0;
      } else if (this.field410 > 255) {
         this.field410 = 255;
      }

      if (this.field411 < 0) {
         this.field411 = 0;
      } else if (this.field411 > 255) {
         this.field411 = 255;
      }
   }

   public Vec3d method1954() {
      return new Vec3d((double)this.field408 / 255.0, (double)this.field409 / 255.0, (double)this.field410 / 255.0);
   }

   public int method2010() {
      return method184(this.field408, this.field409, this.field410, this.field411);
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var1 = new NbtCompound();
      var1.putInt("r", this.field408);
      var1.putInt("g", this.field409);
      var1.putInt("b", this.field410);
      var1.putInt("a", this.field411);
      return var1;
   }

   @Override
   public RGBAColor fromTag(NbtCompound var1) {
      this.field408 = var1.getInt("r");
      this.field409 = var1.getInt("g");
      this.field410 = var1.getInt("b");
      this.field411 = var1.getInt("a");
      this.method2142();
      return this;
   }

   public String toString() {
      return this.field408 + " " + this.field409 + " " + this.field410 + " " + this.field411;
   }

   public boolean method202(RGBAColor var1) {
      return var1 != null && this.field408 == var1.field408 && this.field409 == var1.field409 && this.field410 == var1.field410 && this.field411 == var1.field411;
   }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public ICopyable copy() {
   //   return this.copy();
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public ICopyable set(ICopyable var1) {
   //   return this.method198((RGBAColor)var1);
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object fromTag(NbtCompound var1) {
   //   return this.fromTag(var1);
   //}
}
