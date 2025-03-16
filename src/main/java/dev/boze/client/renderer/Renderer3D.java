package dev.boze.client.renderer;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.render.Placement;
import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import mapped.Class3083;
import mapped.Class5903;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Renderer3D {
   public final Mesh field2166 = new ShaderMesh(ShaderRegistry.field2249, DrawMode.Lines, Attrib.Vec3, Attrib.Color);
   public final Mesh field2167 = new ShaderMesh(ShaderRegistry.field2249, DrawMode.Triangles, Attrib.Vec3, Attrib.Color);
   public final Mesh field2168 = new TextureShaderMesh(ShaderRegistry.field2252, DrawMode.Lines, Attrib.Vec3, Attrib.Color);
   public final Mesh field2169 = new TextureShaderMesh(ShaderRegistry.field2252, DrawMode.Triangles, Attrib.Vec3, Attrib.Color);
   public final Mesh field2170 = new ShaderMesh(
      ShaderRegistry.field2257, DrawMode.Lines, Attrib.Vec3, Attrib.Color, Attrib.Float, Attrib.Vec2, Attrib.Float, Attrib.Vec2
   );
   public final Mesh field2171 = new ShaderMesh(
      ShaderRegistry.field2257, DrawMode.Triangles, Attrib.Vec3, Attrib.Color, Attrib.Float, Attrib.Vec2, Attrib.Float, Attrib.Vec2
   );
   public static final long field2172 = System.currentTimeMillis();
   public final boolean field2173;
   public final boolean field2174;

   public Renderer3D() {
      this.field2173 = false;
      this.field2174 = false;
   }

   public Renderer3D(boolean useDepth, boolean cull) {
      this.field2173 = useDepth;
      this.field2174 = cull;
   }

   public void method1217() {
      this.field2166.method2142();
      this.field2167.method2142();
      this.field2168.method2142();
      this.field2169.method2142();
      this.field2170.method2142();
      this.field2171.method2142();
   }

   public void method1218() {
      this.field2166.method1198();
      this.field2167.method1198();
      this.field2168.method1198();
      this.field2169.method1198();
      this.field2170.method1198();
      this.field2171.method1198();
   }

   public void method1219(MatrixStack matrices) {
      this.field2166.method721(matrices, this.field2173, this.field2174);
      this.field2167.method721(matrices, this.field2173, this.field2174);
      this.field2168.method721(matrices, this.field2173, this.field2174);
      this.field2169.method721(matrices, this.field2173, this.field2174);
      this.field2170.method721(matrices, this.field2173, this.field2174);
      this.field2171.method721(matrices, this.field2173, this.field2174);
   }

   public void method1220(Box box, ColorWrapper choice) {
      this.method1222(box, choice, ShapeMode.Full, 0);
   }

   public void method1221(Box box, ColorWrapper choice, ShapeMode shapeMode) {
      this.method1222(box, choice, shapeMode, 0);
   }

   public void method1222(Box box, ColorWrapper choice, ShapeMode shapeMode, int excludeDir) {
      double var8 = box.minX;
      double var10 = box.minY;
      double var12 = box.minZ;
      double var14 = box.maxX;
      double var16 = box.maxY;
      double var18 = box.maxZ;
      if (shapeMode.method2115()) {
         this.method1223(var8, var10, var12, var14, var16, var18, choice, excludeDir);
      }

      if (shapeMode.method2114()) {
         this.method1225(var8, var10, var12, var14, var16, var18, choice, excludeDir);
      }
   }

   public void method1223(double x1, double y1, double z1, double x2, double y2, double z2, ColorWrapper choice, int excludeDir) {
      this.method1224(x1, y1, z1, x2, y2, z2, choice.field3910, choice.field3911, excludeDir);
   }

   public void method1224(double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float opacity, int excludeDir) {
      if (color instanceof GradientColor var19) {
         this.method1227(this.field2169, x1, y1, z1, x2, y2, z2, var19, opacity, excludeDir);
      } else if (color instanceof ChangingColor var20) {
         this.method1227(this.field2167, x1, y1, z1, x2, y2, z2, var20.method208(), opacity, excludeDir);
      } else if (color instanceof StaticColor var21) {
         this.method1227(this.field2167, x1, y1, z1, x2, y2, z2, var21, opacity, excludeDir);
      }
   }

   public void method1225(double x1, double y1, double z1, double x2, double y2, double z2, ColorWrapper choice, int excludeDir) {
      this.method1226(x1, y1, z1, x2, y2, z2, choice.field3910, choice.field3911, excludeDir);
   }

   public void method1226(double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float opacity, int excludeDir) {
      if (color instanceof GradientColor var19) {
         this.method1229(this.field2168, x1, y1, z1, x2, y2, z2, var19, opacity, excludeDir);
      } else if (color instanceof ChangingColor var20) {
         this.method1229(this.field2166, x1, y1, z1, x2, y2, z2, var20.method208(), opacity, excludeDir);
      } else if (color instanceof StaticColor var21) {
         this.method1229(this.field2166, x1, y1, z1, x2, y2, z2, var21, opacity, excludeDir);
      }
   }

   public void method1227(Mesh mesh, double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float opacity, int excludeDir) {
      this.method1228(mesh, x1, y1, z1, x2, y2, z2, color, opacity, opacity, excludeDir);
   }

   public void method1228(
      Mesh mesh, double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float topOpacity, float bottomOpacity, int excludeDir
   ) {
      StaticColor var21 = color.method208();
      int var22 = mesh.method710(x1, y1, z1).method708(var21, bottomOpacity).method2010();
      int var23 = mesh.method710(x1, y1, z2).method708(var21, bottomOpacity).method2010();
      int var24 = mesh.method710(x2, y1, z1).method708(var21, bottomOpacity).method2010();
      int var25 = mesh.method710(x2, y1, z2).method708(var21, bottomOpacity).method2010();
      int var26 = mesh.method710(x1, y2, z1).method708(var21, topOpacity).method2010();
      int var27 = mesh.method710(x1, y2, z2).method708(var21, topOpacity).method2010();
      int var28 = mesh.method710(x2, y2, z1).method708(var21, topOpacity).method2010();
      int var29 = mesh.method710(x2, y2, z2).method708(var21, topOpacity).method2010();
      if (excludeDir == 0) {
         mesh.method1214(var22, var23, var27, var26);
         mesh.method1214(var24, var28, var29, var25);
         mesh.method1214(var22, var26, var28, var24);
         mesh.method1214(var23, var25, var29, var27);
         mesh.method1214(var22, var24, var25, var23);
         mesh.method1214(var26, var27, var29, var28);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32)) {
            mesh.method1214(var22, var23, var27, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)64)) {
            mesh.method1214(var24, var28, var29, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)8)) {
            mesh.method1214(var22, var26, var28, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)16)) {
            mesh.method1214(var23, var25, var29, var27);
         }

         if (Class3083.method6051(excludeDir, (byte)4)) {
            mesh.method1214(var22, var24, var25, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)2)) {
            mesh.method1214(var26, var27, var29, var28);
         }
      }

      mesh.method1416();
   }

   public void method1229(Mesh mesh, double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float opacity, int excludeDir) {
      this.method1230(mesh, x1, y1, z1, x2, y2, z2, color, opacity, opacity, excludeDir);
   }

   public void method1230(
      Mesh mesh, double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float topOpacity, float bottomOpacity, int excludeDir
   ) {
      int var21 = mesh.method710(x1, y1, z1).method708(color.method208(), bottomOpacity).method2010();
      int var22 = mesh.method710(x1, y1, z2).method708(color.method208(), bottomOpacity).method2010();
      int var23 = mesh.method710(x2, y1, z1).method708(color.method208(), bottomOpacity).method2010();
      int var24 = mesh.method710(x2, y1, z2).method708(color.method208(), bottomOpacity).method2010();
      int var25 = mesh.method710(x1, y2, z1).method708(color.method208(), topOpacity).method2010();
      int var26 = mesh.method710(x1, y2, z2).method708(color.method208(), topOpacity).method2010();
      int var27 = mesh.method710(x2, y2, z1).method708(color.method208(), topOpacity).method2010();
      int var28 = mesh.method710(x2, y2, z2).method708(color.method208(), topOpacity).method2010();
      if (excludeDir == 0) {
         mesh.method1964(var21, var25);
         mesh.method1964(var22, var26);
         mesh.method1964(var23, var27);
         mesh.method1964(var24, var28);
         mesh.method1964(var21, var22);
         mesh.method1964(var23, var24);
         mesh.method1964(var21, var23);
         mesh.method1964(var22, var24);
         mesh.method1964(var25, var26);
         mesh.method1964(var27, var28);
         mesh.method1964(var25, var27);
         mesh.method1964(var26, var28);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)8)) {
            mesh.method1964(var21, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)16)) {
            mesh.method1964(var22, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)8)) {
            mesh.method1964(var23, var27);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)16)) {
            mesh.method1964(var24, var28);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)4)) {
            mesh.method1964(var21, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)4)) {
            mesh.method1964(var23, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)4)) {
            mesh.method1964(var21, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)4)) {
            mesh.method1964(var22, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)2)) {
            mesh.method1964(var25, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)2)) {
            mesh.method1964(var27, var28);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)2)) {
            mesh.method1964(var25, var27);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)2)) {
            mesh.method1964(var26, var28);
         }
      }

      this.field2166.method1416();
   }

   public void method1231(Mesh mesh, double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float opacity) {
      this.method1232(mesh, x1, y1, z1, x2, y2, z2, color, opacity, opacity);
   }

   public void method1232(Mesh mesh, double x1, double y1, double z1, double x2, double y2, double z2, Class5903<?> color, float startOpacity, float endOpacity) {
      mesh.method1964(
         mesh.method710(x1, y1, z1).method708(color.method208(), startOpacity).method2010(),
         mesh.method710(x2, y2, z2).method708(color.method208(), endOpacity).method2010()
      );
   }

   public void method1233(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor color1, RGBAColor color2) {
      this.field2166
         .method1964(this.field2166.method710(x1, y1, z1).method715(color1).method2010(), this.field2166.method710(x2, y2, z2).method715(color2).method2010());
   }

   public void method1234(Vec3d one, Vec3d two, RGBAColor color) {
      this.method1233(one.x, one.y, one.z, two.x, two.y, two.z, color, color);
   }

   public void method1235(Vec3d one, Vec3d two, BozeDrawColor color) {
      this.method1240(one.x, one.y, one.z, two.x, two.y, two.z, color, color);
   }

   public void method1236(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor color) {
      this.method1233(x1, y1, z1, x2, y2, z2, color, color);
   }

   public void method1237(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor color, int excludeDir) {
      int var18 = this.field2166.method710(x1, y1, z1).method715(color).method2010();
      int var19 = this.field2166.method710(x1, y1, z2).method715(color).method2010();
      int var20 = this.field2166.method710(x2, y1, z1).method715(color).method2010();
      int var21 = this.field2166.method710(x2, y1, z2).method715(color).method2010();
      int var22 = this.field2166.method710(x1, y2, z1).method715(color).method2010();
      int var23 = this.field2166.method710(x1, y2, z2).method715(color).method2010();
      int var24 = this.field2166.method710(x2, y2, z1).method715(color).method2010();
      int var25 = this.field2166.method710(x2, y2, z2).method715(color).method2010();
      if (excludeDir == 0) {
         this.field2166.method1964(var18, var22);
         this.field2166.method1964(var19, var23);
         this.field2166.method1964(var20, var24);
         this.field2166.method1964(var21, var25);
         this.field2166.method1964(var18, var19);
         this.field2166.method1964(var20, var21);
         this.field2166.method1964(var18, var20);
         this.field2166.method1964(var19, var21);
         this.field2166.method1964(var22, var23);
         this.field2166.method1964(var24, var25);
         this.field2166.method1964(var22, var24);
         this.field2166.method1964(var23, var25);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2166.method1964(var18, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2166.method1964(var19, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2166.method1964(var20, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2166.method1964(var21, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var18, var19);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var20, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var18, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var19, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var22, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var24, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var22, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var23, var25);
         }
      }

      this.field2166.method1416();
   }

   public void method1238(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor topColor, RGBAColor bottomColor, int excludeDir) {
      int var19 = this.field2166.method710(x1, y1, z1).method715(bottomColor).method2010();
      int var20 = this.field2166.method710(x1, y1, z2).method715(bottomColor).method2010();
      int var21 = this.field2166.method710(x2, y1, z1).method715(bottomColor).method2010();
      int var22 = this.field2166.method710(x2, y1, z2).method715(bottomColor).method2010();
      int var23 = this.field2166.method710(x1, y2, z1).method715(topColor).method2010();
      int var24 = this.field2166.method710(x1, y2, z2).method715(topColor).method2010();
      int var25 = this.field2166.method710(x2, y2, z1).method715(topColor).method2010();
      int var26 = this.field2166.method710(x2, y2, z2).method715(topColor).method2010();
      if (excludeDir == 0) {
         this.field2166.method1964(var19, var23);
         this.field2166.method1964(var20, var24);
         this.field2166.method1964(var21, var25);
         this.field2166.method1964(var22, var26);
         this.field2166.method1964(var19, var20);
         this.field2166.method1964(var21, var22);
         this.field2166.method1964(var19, var21);
         this.field2166.method1964(var20, var22);
         this.field2166.method1964(var23, var24);
         this.field2166.method1964(var25, var26);
         this.field2166.method1964(var23, var25);
         this.field2166.method1964(var24, var26);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2166.method1964(var19, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2166.method1964(var20, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2166.method1964(var21, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2166.method1964(var22, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var19, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var21, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var19, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2166.method1964(var20, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var23, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var25, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var23, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2166.method1964(var24, var26);
         }
      }

      this.field2166.method1416();
   }

   public void method1239(int x, int y, int z, RGBAColor color, int excludeDir) {
      this.method1237((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1), color, excludeDir);
   }

   public void method1240(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor color1, BozeDrawColor color2) {
      this.field2170
         .method1964(
            this.field2170
               .method710(x1, y1, z1)
               .method715(color1)
               .method714(color1.method958())
               .method712(color1.method959())
               .method714(color1.method960())
               .method711(color1.getMinHue(), color1.getMaxHue())
               .method2010(),
            this.field2170
               .method710(x2, y2, z2)
               .method715(color2)
               .method714(color2.method958())
               .method712(color2.method959())
               .method714(color2.method960())
               .method711(color2.getMinHue(), color2.getMaxHue())
               .method2010()
         );
   }

   public void method1241(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor color) {
      this.method1240(x1, y1, z1, x2, y2, z2, color, color);
   }

   public void method1242(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor color, int excludeDir) {
      int var18 = this.field2170
         .method710(x1, y1, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var19 = this.field2170
         .method710(x1, y1, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var20 = this.field2170
         .method710(x2, y1, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var21 = this.field2170
         .method710(x2, y1, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var22 = this.field2170
         .method710(x1, y2, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var23 = this.field2170
         .method710(x1, y2, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var24 = this.field2170
         .method710(x2, y2, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var25 = this.field2170
         .method710(x2, y2, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      if (excludeDir == 0) {
         this.field2170.method1964(var18, var22);
         this.field2170.method1964(var19, var23);
         this.field2170.method1964(var20, var24);
         this.field2170.method1964(var21, var25);
         this.field2170.method1964(var18, var19);
         this.field2170.method1964(var20, var21);
         this.field2170.method1964(var18, var20);
         this.field2170.method1964(var19, var21);
         this.field2170.method1964(var22, var23);
         this.field2170.method1964(var24, var25);
         this.field2170.method1964(var22, var24);
         this.field2170.method1964(var23, var25);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2170.method1964(var18, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2170.method1964(var19, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2170.method1964(var20, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2170.method1964(var21, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var18, var19);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var20, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var18, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var19, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var22, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var24, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var22, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var23, var25);
         }
      }

      this.field2170.method1416();
   }

   public void method1243(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor topColor, BozeDrawColor bottomColor, int excludeDir) {
      int var19 = this.field2170
         .method710(x1, y1, z1)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var20 = this.field2170
         .method710(x1, y1, z2)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var21 = this.field2170
         .method710(x2, y1, z1)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var22 = this.field2170
         .method710(x2, y1, z2)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var23 = this.field2170
         .method710(x1, y2, z1)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      int var24 = this.field2170
         .method710(x1, y2, z2)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      int var25 = this.field2170
         .method710(x2, y2, z1)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      int var26 = this.field2170
         .method710(x2, y2, z2)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      if (excludeDir == 0) {
         this.field2170.method1964(var19, var23);
         this.field2170.method1964(var20, var24);
         this.field2170.method1964(var21, var25);
         this.field2170.method1964(var22, var26);
         this.field2170.method1964(var19, var20);
         this.field2170.method1964(var21, var22);
         this.field2170.method1964(var19, var21);
         this.field2170.method1964(var20, var22);
         this.field2170.method1964(var23, var24);
         this.field2170.method1964(var25, var26);
         this.field2170.method1964(var23, var25);
         this.field2170.method1964(var24, var26);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2170.method1964(var19, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2170.method1964(var20, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)8)) {
            this.field2170.method1964(var21, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)16)) {
            this.field2170.method1964(var22, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var19, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var21, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var19, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)4)) {
            this.field2170.method1964(var20, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)32) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var23, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)64) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var25, var26);
         }

         if (Class3083.method6051(excludeDir, (byte)8) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var23, var25);
         }

         if (Class3083.method6051(excludeDir, (byte)16) && Class3083.method6051(excludeDir, (byte)2)) {
            this.field2170.method1964(var24, var26);
         }
      }

      this.field2170.method1416();
   }

   public void method1244(int x, int y, int z, BozeDrawColor color, int excludeDir) {
      this.method1242((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1), color, excludeDir);
   }

   public void method1245(
      double x1,
      double y1,
      double z1,
      double x2,
      double y2,
      double z2,
      double x3,
      double y3,
      double z3,
      double x4,
      double y4,
      double z4,
      RGBAColor topLeft,
      RGBAColor topRight,
      RGBAColor bottomRight,
      RGBAColor bottomLeft
   ) {
      this.field2167
         .method1214(
            this.field2167.method710(x1, y1, z1).method715(bottomLeft).method2010(),
            this.field2167.method710(x2, y2, z2).method715(topLeft).method2010(),
            this.field2167.method710(x3, y3, z3).method715(topRight).method2010(),
            this.field2167.method710(x4, y4, z4).method715(bottomRight).method2010()
         );
   }

   public void method1246(
      double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, RGBAColor color
   ) {
      this.method1245(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, color, color, color, color);
   }

   public void method1247(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor color) {
      this.method1246(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, color);
   }

   public void method1248(double x1, double y, double z1, double x2, double z2, RGBAColor color) {
      this.method1246(x1, y, z1, x1, y, z2, x2, y, z2, x2, y, z1, color);
   }

   public void method1249(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor topColor, RGBAColor bottomColor) {
      this.method1245(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, topColor, topColor, bottomColor, bottomColor);
   }

   public void method1250(
      double x1,
      double y1,
      double z1,
      double x2,
      double y2,
      double z2,
      double x3,
      double y3,
      double z3,
      double x4,
      double y4,
      double z4,
      BozeDrawColor topLeft,
      BozeDrawColor topRight,
      BozeDrawColor bottomRight,
      BozeDrawColor bottomLeft
   ) {
      this.field2171
         .method1214(
            this.field2171
               .method710(x1, y1, z1)
               .method715(bottomLeft)
               .method714(bottomLeft.method958())
               .method712(bottomLeft.method959())
               .method714(bottomLeft.method960())
               .method711(bottomLeft.getMinHue(), bottomLeft.getMaxHue())
               .method2010(),
            this.field2171
               .method710(x2, y2, z2)
               .method715(topLeft)
               .method714(topLeft.method958())
               .method712(topLeft.method959())
               .method714(topLeft.method960())
               .method711(topLeft.getMinHue(), topLeft.getMaxHue())
               .method2010(),
            this.field2171
               .method710(x3, y3, z3)
               .method715(topRight)
               .method714(topRight.method958())
               .method712(topRight.method959())
               .method714(topRight.method960())
               .method711(topRight.getMinHue(), topRight.getMaxHue())
               .method2010(),
            this.field2171
               .method710(x4, y4, z4)
               .method715(bottomRight)
               .method714(bottomRight.method958())
               .method712(bottomRight.method959())
               .method714(bottomRight.method960())
               .method711(bottomRight.getMinHue(), bottomRight.getMaxHue())
               .method2010()
         );
   }

   public void method1251(
      double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, BozeDrawColor color
   ) {
      this.method1250(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, color, color, color, color);
   }

   public void method1252(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor color) {
      this.method1251(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, color);
   }

   public void method1253(double x1, double y, double z1, double x2, double z2, BozeDrawColor color) {
      this.method1251(x1, y, z1, x1, y, z2, x2, y, z2, x2, y, z1, color);
   }

   public void method1254(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor topRainbowColor, BozeDrawColor bottomRainbowColor) {
      this.method1250(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, topRainbowColor, topRainbowColor, bottomRainbowColor, bottomRainbowColor);
   }

   public void method1255(
      double x1,
      double y1,
      double z1,
      double x2,
      double y2,
      double z2,
      double x3,
      double y3,
      double z3,
      double x4,
      double y4,
      double z4,
      RGBAColor sideColor,
      RGBAColor lineColor,
      ShapeMode mode
   ) {
      if (mode.method2114()) {
         int var31 = this.field2166.method710(x1, y1, z1).method715(lineColor).method2010();
         int var32 = this.field2166.method710(x2, y2, z2).method715(lineColor).method2010();
         int var33 = this.field2166.method710(x3, y3, z3).method715(lineColor).method2010();
         int var34 = this.field2166.method710(x4, y4, z4).method715(lineColor).method2010();
         this.field2166.method1964(var31, var32);
         this.field2166.method1964(var32, var33);
         this.field2166.method1964(var33, var34);
         this.field2166.method1964(var34, var31);
      }

      if (mode.method2115()) {
         this.method1246(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, sideColor);
      }
   }

   public void method1256(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor sideColor, RGBAColor lineColor, ShapeMode mode) {
      this.method1255(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, sideColor, lineColor, mode);
   }

   public void method1257(double x1, double y, double z1, double x2, double z2, RGBAColor sideColor, RGBAColor lineColor, ShapeMode mode) {
      this.method1255(x1, y, z1, x1, y, z2, x2, y, z2, x2, y, z1, sideColor, lineColor, mode);
   }

   public void method1258(
      double x1,
      double y1,
      double z1,
      double x2,
      double y2,
      double z2,
      double x3,
      double y3,
      double z3,
      double x4,
      double y4,
      double z4,
      BozeDrawColor sideRainbowColor,
      BozeDrawColor lineRainbowColor,
      ShapeMode mode
   ) {
      if (mode.method2114()) {
         int var31 = this.field2170
            .method710(x1, y1, z1)
            .method715(lineRainbowColor)
            .method714(lineRainbowColor.method958())
            .method712(lineRainbowColor.method959())
            .method714(lineRainbowColor.method960())
            .method711(lineRainbowColor.getMinHue(), lineRainbowColor.getMaxHue())
            .method2010();
         int var32 = this.field2170
            .method710(x2, y2, z2)
            .method715(lineRainbowColor)
            .method714(lineRainbowColor.method958())
            .method712(lineRainbowColor.method959())
            .method714(lineRainbowColor.method960())
            .method711(lineRainbowColor.getMinHue(), lineRainbowColor.getMaxHue())
            .method2010();
         int var33 = this.field2170
            .method710(x3, y3, z3)
            .method715(lineRainbowColor)
            .method714(lineRainbowColor.method958())
            .method712(lineRainbowColor.method959())
            .method714(lineRainbowColor.method960())
            .method711(lineRainbowColor.getMinHue(), lineRainbowColor.getMaxHue())
            .method2010();
         int var34 = this.field2170
            .method710(x4, y4, z4)
            .method715(lineRainbowColor)
            .method714(lineRainbowColor.method958())
            .method712(lineRainbowColor.method959())
            .method714(lineRainbowColor.method960())
            .method711(lineRainbowColor.getMinHue(), lineRainbowColor.getMaxHue())
            .method2010();
         this.field2170.method1964(var31, var32);
         this.field2170.method1964(var32, var33);
         this.field2170.method1964(var33, var34);
         this.field2170.method1964(var34, var31);
      }

      if (mode.method2115()) {
         this.method1251(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, sideRainbowColor);
      }
   }

   public void method1259(
      double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor sideRainbowColor, BozeDrawColor lineRainbowColor, ShapeMode mode
   ) {
      this.method1258(x1, y1, z1, x1, y2, z1, x2, y2, z2, x2, y1, z2, sideRainbowColor, lineRainbowColor, mode);
   }

   public void method1260(double x1, double y, double z1, double x2, double z2, BozeDrawColor sideRainbowColor, BozeDrawColor lineRainbowColor, ShapeMode mode) {
      this.method1258(x1, y, z1, x1, y, z2, x2, y, z2, x2, y, z1, sideRainbowColor, lineRainbowColor, mode);
   }

   public void method1261(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor color, int excludeDir) {
      int var18 = this.field2167.method710(x1, y1, z1).method715(color).method2010();
      int var19 = this.field2167.method710(x1, y1, z2).method715(color).method2010();
      int var20 = this.field2167.method710(x2, y1, z1).method715(color).method2010();
      int var21 = this.field2167.method710(x2, y1, z2).method715(color).method2010();
      int var22 = this.field2167.method710(x1, y2, z1).method715(color).method2010();
      int var23 = this.field2167.method710(x1, y2, z2).method715(color).method2010();
      int var24 = this.field2167.method710(x2, y2, z1).method715(color).method2010();
      int var25 = this.field2167.method710(x2, y2, z2).method715(color).method2010();
      if (excludeDir == 0) {
         this.field2167.method1214(var18, var19, var23, var22);
         this.field2167.method1214(var20, var24, var25, var21);
         this.field2167.method1214(var18, var22, var24, var20);
         this.field2167.method1214(var19, var21, var25, var23);
         this.field2167.method1214(var18, var20, var21, var19);
         this.field2167.method1214(var22, var23, var25, var24);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32)) {
            this.field2167.method1214(var18, var19, var23, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)64)) {
            this.field2167.method1214(var20, var24, var25, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)8)) {
            this.field2167.method1214(var18, var22, var24, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)16)) {
            this.field2167.method1214(var19, var21, var25, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)4)) {
            this.field2167.method1214(var18, var20, var21, var19);
         }

         if (Class3083.method6051(excludeDir, (byte)2)) {
            this.field2167.method1214(var22, var23, var25, var24);
         }
      }

      this.field2167.method1416();
   }

   public void method1262(double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor topColor, RGBAColor bottomColor, int excludeDir) {
      int var19 = this.field2167.method710(x1, y1, z1).method715(bottomColor).method2010();
      int var20 = this.field2167.method710(x1, y1, z2).method715(bottomColor).method2010();
      int var21 = this.field2167.method710(x2, y1, z1).method715(bottomColor).method2010();
      int var22 = this.field2167.method710(x2, y1, z2).method715(bottomColor).method2010();
      int var23 = this.field2167.method710(x1, y2, z1).method715(topColor).method2010();
      int var24 = this.field2167.method710(x1, y2, z2).method715(topColor).method2010();
      int var25 = this.field2167.method710(x2, y2, z1).method715(topColor).method2010();
      int var26 = this.field2167.method710(x2, y2, z2).method715(topColor).method2010();
      if (excludeDir == 0) {
         this.field2167.method1214(var19, var20, var24, var23);
         this.field2167.method1214(var21, var25, var26, var22);
         this.field2167.method1214(var19, var23, var25, var21);
         this.field2167.method1214(var20, var22, var26, var24);
         this.field2167.method1214(var19, var21, var22, var20);
         this.field2167.method1214(var23, var24, var26, var25);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32)) {
            this.field2167.method1214(var19, var20, var24, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)64)) {
            this.field2167.method1214(var21, var25, var26, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)8)) {
            this.field2167.method1214(var19, var23, var25, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)16)) {
            this.field2167.method1214(var20, var22, var26, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)4)) {
            this.field2167.method1214(var19, var21, var22, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)2)) {
            this.field2167.method1214(var23, var24, var26, var25);
         }
      }

      this.field2167.method1416();
   }

   public void method1263(int x, int y, int z, RGBAColor color, int excludeDir) {
      this.method1261((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1), color, excludeDir);
   }

   public void method1264(
      double x1, double y1, double z1, double x2, double y2, double z2, RGBAColor sideColor, RGBAColor lineColor, ShapeMode mode, int excludeDir
   ) {
      if (mode.method2114()) {
         this.method1237(x1, y1, z1, x2, y2, z2, lineColor, excludeDir);
      }

      if (mode.method2115()) {
         this.method1261(x1, y1, z1, x2, y2, z2, sideColor, excludeDir);
      }
   }

   public void method1265(BlockPos pos, RGBAColor sideColor, RGBAColor lineColor, ShapeMode mode, int excludeDir) {
      if (mode.method2114()) {
         this.method1237(
            (double)pos.getX(),
            (double)pos.getY(),
            (double)pos.getZ(),
            (double)(pos.getX() + 1),
            (double)(pos.getY() + 1),
            (double)(pos.getZ() + 1),
            lineColor,
            excludeDir
         );
      }

      if (mode.method2115()) {
         this.method1261(
            (double)pos.getX(),
            (double)pos.getY(),
            (double)pos.getZ(),
            (double)(pos.getX() + 1),
            (double)(pos.getY() + 1),
            (double)(pos.getZ() + 1),
            sideColor,
            excludeDir
         );
      }
   }

   public void method1266(Box box, RGBAColor sideColor, RGBAColor lineColor, ShapeMode mode, int excludeDir) {
      if (mode.method2114()) {
         this.method1237(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineColor, excludeDir);
      }

      if (mode.method2115()) {
         this.method1261(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideColor, excludeDir);
      }
   }

   public void method1267(
      Box box, RGBAColor topSideColor, RGBAColor topLineColor, RGBAColor bottomSideColor, RGBAColor bottomLineColor, ShapeMode mode, int excludeDir
   ) {
      if (mode.method2114()) {
         this.method1238(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, topLineColor, bottomLineColor, excludeDir);
      }

      if (mode.method2115()) {
         this.method1262(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, topSideColor, bottomSideColor, excludeDir);
      }
   }

   public void method1268(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor color, int excludeDir) {
      int var18 = this.field2171
         .method710(x1, y1, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var19 = this.field2171
         .method710(x1, y1, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var20 = this.field2171
         .method710(x2, y1, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var21 = this.field2171
         .method710(x2, y1, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var22 = this.field2171
         .method710(x1, y2, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var23 = this.field2171
         .method710(x1, y2, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var24 = this.field2171
         .method710(x2, y2, z1)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      int var25 = this.field2171
         .method710(x2, y2, z2)
         .method715(color)
         .method714(color.method958())
         .method712(color.method959())
         .method714(color.method960())
         .method711(color.getMinHue(), color.getMaxHue())
         .method2010();
      if (excludeDir == 0) {
         this.field2171.method1214(var18, var19, var23, var22);
         this.field2171.method1214(var20, var24, var25, var21);
         this.field2171.method1214(var18, var22, var24, var20);
         this.field2171.method1214(var19, var21, var25, var23);
         this.field2171.method1214(var18, var20, var21, var19);
         this.field2171.method1214(var22, var23, var25, var24);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32)) {
            this.field2171.method1214(var18, var19, var23, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)64)) {
            this.field2171.method1214(var20, var24, var25, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)8)) {
            this.field2171.method1214(var18, var22, var24, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)16)) {
            this.field2171.method1214(var19, var21, var25, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)4)) {
            this.field2171.method1214(var18, var20, var21, var19);
         }

         if (Class3083.method6051(excludeDir, (byte)2)) {
            this.field2171.method1214(var22, var23, var25, var24);
         }
      }

      this.field2171.method1416();
   }

   public void method1269(double x1, double y1, double z1, double x2, double y2, double z2, BozeDrawColor topColor, BozeDrawColor bottomColor, int excludeDir) {
      int var19 = this.field2171
         .method710(x1, y1, z1)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var20 = this.field2171
         .method710(x1, y1, z2)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var21 = this.field2171
         .method710(x2, y1, z1)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var22 = this.field2171
         .method710(x2, y1, z2)
         .method715(bottomColor)
         .method714(bottomColor.method958())
         .method712(bottomColor.method959())
         .method714(bottomColor.method960())
         .method711(bottomColor.getMinHue(), bottomColor.getMaxHue())
         .method2010();
      int var23 = this.field2171
         .method710(x1, y2, z1)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      int var24 = this.field2171
         .method710(x1, y2, z2)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      int var25 = this.field2171
         .method710(x2, y2, z1)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      int var26 = this.field2171
         .method710(x2, y2, z2)
         .method715(topColor)
         .method714(topColor.method958())
         .method712(topColor.method959())
         .method714(topColor.method960())
         .method711(topColor.getMinHue(), topColor.getMaxHue())
         .method2010();
      if (excludeDir == 0) {
         this.field2171.method1214(var19, var20, var24, var23);
         this.field2171.method1214(var21, var25, var26, var22);
         this.field2171.method1214(var19, var23, var25, var21);
         this.field2171.method1214(var20, var22, var26, var24);
         this.field2171.method1214(var19, var21, var22, var20);
         this.field2171.method1214(var23, var24, var26, var25);
      } else {
         if (Class3083.method6051(excludeDir, (byte)32)) {
            this.field2171.method1214(var19, var20, var24, var23);
         }

         if (Class3083.method6051(excludeDir, (byte)64)) {
            this.field2171.method1214(var21, var25, var26, var22);
         }

         if (Class3083.method6051(excludeDir, (byte)8)) {
            this.field2171.method1214(var19, var23, var25, var21);
         }

         if (Class3083.method6051(excludeDir, (byte)16)) {
            this.field2171.method1214(var20, var22, var26, var24);
         }

         if (Class3083.method6051(excludeDir, (byte)4)) {
            this.field2171.method1214(var19, var21, var22, var20);
         }

         if (Class3083.method6051(excludeDir, (byte)2)) {
            this.field2171.method1214(var23, var24, var26, var25);
         }
      }

      this.field2171.method1416();
   }

   public void method1270(int x, int y, int z, BozeDrawColor color, int excludeDir) {
      this.method1268((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1), color, excludeDir);
   }

   public void method1271(
      double x1,
      double y1,
      double z1,
      double x2,
      double y2,
      double z2,
      BozeDrawColor sideRainbowColor,
      BozeDrawColor lineRainbowColor,
      ShapeMode mode,
      int excludeDir
   ) {
      if (mode.method2114()) {
         this.method1242(x1, y1, z1, x2, y2, z2, lineRainbowColor, excludeDir);
      }

      if (mode.method2115()) {
         this.method1268(x1, y1, z1, x2, y2, z2, sideRainbowColor, excludeDir);
      }
   }

   public void method1272(BlockPos pos, BozeDrawColor sideRainbowColor, BozeDrawColor lineRainbowColor, ShapeMode mode, int excludeDir) {
      if (mode.method2114()) {
         this.method1242(
            (double)pos.getX(),
            (double)pos.getY(),
            (double)pos.getZ(),
            (double)(pos.getX() + 1),
            (double)(pos.getY() + 1),
            (double)(pos.getZ() + 1),
            lineRainbowColor,
            excludeDir
         );
      }

      if (mode.method2115()) {
         this.method1268(
            (double)pos.getX(),
            (double)pos.getY(),
            (double)pos.getZ(),
            (double)(pos.getX() + 1),
            (double)(pos.getY() + 1),
            (double)(pos.getZ() + 1),
            sideRainbowColor,
            excludeDir
         );
      }
   }

   public void method1273(Box box, BozeDrawColor sideRainbowColor, BozeDrawColor lineRainbowColor, ShapeMode mode, int excludeDir) {
      if (mode.method2114()) {
         this.method1242(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineRainbowColor, excludeDir);
      }

      if (mode.method2115()) {
         this.method1268(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sideRainbowColor, excludeDir);
      }
   }

   public void method1274(Placement renderPos, BozeDrawColor sideRainbowColor, BozeDrawColor lineRainbowColor, ShapeMode mode, int excludeDir) {
      int[] var9 = renderPos.method1158();
      if (mode.method2114()) {
         this.method1242(
            (double)var9[0],
            (double)var9[1],
            (double)var9[2],
            (double)(var9[0] + 1),
            (double)(var9[1] + 1),
            (double)(var9[2] + 1),
            lineRainbowColor,
            excludeDir
         );
      }

      if (mode.method2115()) {
         this.method1268(
            (double)var9[0],
            (double)var9[1],
            (double)var9[2],
            (double)(var9[0] + 1),
            (double)(var9[1] + 1),
            (double)(var9[2] + 1),
            sideRainbowColor,
            excludeDir
         );
      }
   }

   public void method1275(
      Box box,
      BozeDrawColor topSideRainbowColor,
      BozeDrawColor topLineRainbowColor,
      BozeDrawColor bottomSideRainbowColor,
      BozeDrawColor bottomLineRainbowColor,
      ShapeMode mode,
      int excludeDir
   ) {
      if (mode.method2114()) {
         this.method1243(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, topLineRainbowColor, bottomLineRainbowColor, excludeDir);
      }

      if (mode.method2115()) {
         this.method1269(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, topSideRainbowColor, bottomSideRainbowColor, excludeDir);
      }
   }
}
