package mapped;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.settings.generic.ScalingSetting.ScalingMode;
import dev.boze.client.systems.modules.render.Zoom;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3d;
import org.joml.Vector4f;

public class Class5922 implements IMinecraft {
   private static final Vector4f field22 = new Vector4f();
   private static final Vector4f field23 = new Vector4f();
   private static final Vector4f field24 = new Vector4f();
   private static final Vector3d field25 = new Vector3d();
   private static final Vector3d field26 = new Vector3d();
   private static final Matrix4f field27 = new Matrix4f();
   private static final Matrix4f field28 = new Matrix4f();
   private static int field29;
   public static double field30;

   public static void method58(Matrix4f modelView) {
      field27.set(var74);
      field28.set(RenderSystem.getProjectionMatrix());
      Class3062.method5989(field25, mc.gameRenderer.getCamera().getPos());
      field26.set(field25);
      field26.negate();
      field29 = mc.getWindow().calculateScaleFactor((Integer)mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
   }

   public static boolean method59(Vector3d pos, ScalingSetting mode) {
      double var5 = field25.getDistance(var75);
      double var10;
      if (var76.method1304() == ScalingMode.Const) {
         var10 = 1.0 - var5 * 0.01;
         var10 = MathHelper.clamp(var10, 0.5, 2.147483647E9);
         var10 *= var76.method1303();
      } else {
         var10 = var76.method1303();
         var10 = MathHelper.clamp(1.0 / var5, var76.getMinValue(), var76.getMaxValue()) * var10 * 3.0;
      }

      if (Zoom.INSTANCE.isEnabled()) {
         var10 *= Zoom.INSTANCE.method2091();
      }

      return method60(var75, var10);
   }

   public static boolean method60(Vector3d pos, double s) {
      field30 = var78;
      field22.set(field26.x + var77.x, field26.y + var77.y, field26.z + var77.z, 1.0);
      field22.mul(field27, field23);
      field23.mul(field28, field24);
      if (field24.w <= 0.0F) {
         return false;
      } else {
         method66(field24);
         double var6 = (double)(field24.x * (float)mc.getWindow().getFramebufferWidth());
         double var8 = (double)(field24.y * (float)mc.getWindow().getFramebufferHeight());
         if (!Double.isInfinite(var6) && !Double.isInfinite(var8)) {
            var77.set(var6 / (double)field29, ((double)mc.getWindow().getFramebufferHeight() - var8) / (double)field29, (double)field24.z);
            return true;
         } else {
            return false;
         }
      }
   }

   public static void method61(Vector3d pos) {
      Matrix4fStack var1 = RenderSystem.getModelViewStack();
      method63(var1, var79);
   }

   public static void method62(Vector3d pos, DrawContext drawContext) {
      method61(var80);
      MatrixStack var2 = var81.getMatrices();
      var2.push();
      var2.translate((float)var80.x, (float)var80.y, 0.0F);
      var2.scale((float)field30, (float)field30, 1.0F);
   }

   private static void method63(Matrix4fStack var0, Vector3d var1) {
      var0.pushMatrix();
      var0.translate((float)var1.x, (float)var1.y, 0.0F);
      var0.scale((float)field30, (float)field30, 1.0F);
   }

   public static void method2142() {
      RenderSystem.getModelViewStack().popMatrix();
   }

   public static void method332(DrawContext drawContext) {
      method2142();
      var5948.getMatrices().pop();
   }

   private static void method66(Vector4f var0) {
      float var1 = 1.0F / var0.w * 0.5F;
      var0.x = var0.x * var1 + 0.5F;
      var0.y = var0.y * var1 + 0.5F;
      var0.z = var0.z * var1 + 0.5F;
      var0.w = var1;
   }
}
