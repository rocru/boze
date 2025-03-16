package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.PlaceRenderMode;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.render.Placement;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.StringSetting;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.OldColors;
import dev.boze.client.utils.render.ByteTexture;
import java.io.File;
import java.io.FileInputStream;
import mapped.Class3064;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

public class PlaceRender extends Module {
   public static final PlaceRender INSTANCE = new PlaceRender();
   public final EnumSetting<PlaceRenderMode> field3619 = new EnumSetting<PlaceRenderMode>("Mode", PlaceRenderMode.Simple, "Mode for drawing placements");
   public final EnumSetting<ShaderMode> field3620 = new EnumSetting<ShaderMode>("Shader", ShaderMode.Colored, "Shader to use", this::lambda$new$0);
   public final BooleanSetting field3621 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3620);
   public final IntSetting field3622 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3620);
   public final FloatSetting field3623 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3620);
   public final FloatSetting field3624 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$1, this.field3620);
   private final IntSetting field3625 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3620);
   private final FloatSetting field3626 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3620);
   public final StringSetting field3627 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$2, this.field3620);
   public final ColorSetting field3628 = new ColorSetting("Fill", new BozeDrawColor(1681640397), "Color for shader fill", this::lambda$new$3, this.field3620);
   public final ColorSetting field3629 = new ColorSetting(
      "Outline", new BozeDrawColor(-12858419), "Color for shader outline", this::lambda$new$4, this.field3620
   );
   private final BooleanSetting field3630 = new BooleanSetting("ColorSync", false, "Sync colors to global color");
   private final BooleanSetting field3631 = new BooleanSetting("Fade", true, "Fade placement renders", this::lambda$new$5);
   private final IntSetting field3632 = new IntSetting("Ticks", 10, 1, 20, 1, "Amount of ticks to render placements for");
   private final BooleanSetting field3633 = new BooleanSetting("Grow", true, "Grow placements");
   private final MinMaxSetting field3634 = new MinMaxSetting("GrowDuration", 0.1, 0.1, 0.5, 0.05, "Grow duration as a fraction of ticks");
   private final BooleanSetting field3635 = new BooleanSetting("Shrink", true, "Shrink placements");
   private final MinMaxSetting field3636 = new MinMaxSetting("ShrinkDuration", 0.1, 0.1, 0.5, 0.05, "shrink duration as a fraction of ticks");
   private Renderer3D field3637;
   private ByteTexture field3638;
   private String field3639 = "";
   private final Class3064<Placement> field3640 = new Class3064<Placement>(Placement::new);

   public PlaceRender() {
      super("PlaceRender", "Renders placements for modules", Category.Render);
      this.setEnabled(true);
   }

   public static int method2010() {
      return INSTANCE.field3632.method434() * 50;
   }

   @EventHandler(
      priority = 4000
   )
   public void method2011(Render3DEvent event) {
      if (this.field3619.method461() == PlaceRenderMode.Shader) {
         if (this.field3637 == null) {
            this.field3637 = new Renderer3D(false, true);
         }

         this.field3637.method1217();
      }
   }

   @EventHandler(
      priority = -4000
   )
   public void method2012(Render3DEvent event) {
      if (this.field3619.method461() == PlaceRenderMode.Shader) {
         ChamsShaderRenderer.method1311(
            this::lambda$onRender3DPost$6,
            this.method2016(),
            this.field3621.method419(),
            this.field3630.method419()
               ? (BozeDrawColor)OldColors.INSTANCE.clientGradient.method1362().method964().method196(this.field3628.method1362().field411)
               : this.field3628.method1362(),
            this.field3630.method419()
               ? (BozeDrawColor)OldColors.INSTANCE.clientGradient.method1362().method964().method196(this.field3629.method1362().field411)
               : this.field3629.method1362(),
            this.field3625.method434(),
            this.field3626.method423(),
            this.field3623.method423(),
            this.field3624.method423(),
            this.field3622.method434(),
            this.field3638
         );
      }
   }

   public void method2013(Render3DEvent event, BlockPos pos, long time, BozeDrawColor color, BozeDrawColor outline) {
      this.method2014(event, new Box(pos), time, color, outline);
   }

   public void method2014(Render3DEvent event, Box bb, long time, BozeDrawColor color, BozeDrawColor outline) {
      if (this.isEnabled()) {
         if (this.field3619.method461() == PlaceRenderMode.Simple
            && this.field3631.method419()
            && System.currentTimeMillis() - time > (long)((int)((float)method2010() / 2.0F))) {
            float var10 = MathHelper.clamp(
               1.0F - ((float)(System.currentTimeMillis() - time) - (float)method2010() / 2.0F) / ((float)method2010() / 2.0F), 0.0F, 1.0F
            );
            color = (BozeDrawColor)color.method964().method197((float)color.field411 * var10);
            outline = (BozeDrawColor)outline.method964().method197((float)outline.field411 * var10);
         }

         if (this.field3630.method419()) {
            color = (BozeDrawColor)OldColors.INSTANCE.clientGradient.method1362().method964().method196(color.field411);
            outline = (BozeDrawColor)OldColors.INSTANCE.clientGradient.method1362().method964().method196(outline.field411);
         }

         if (this.field3633.method419() && (double)(System.currentTimeMillis() - time) < (double)method2010() * this.field3634.getValue()) {
            double var12 = MathHelper.clamp((double)(System.currentTimeMillis() - time) / ((double)method2010() * this.field3634.getValue()), 0.0, 1.0);
            bb = bb.expand(var12 * -0.5);
         }

         if (this.field3635.method419() && (double)(System.currentTimeMillis() - time) > (double)method2010() * (1.0 - this.field3636.getValue())) {
            double var13 = MathHelper.clamp(
               ((double)(System.currentTimeMillis() - time) - (double)method2010() * (1.0 - this.field3636.getValue()))
                  / ((double)method2010() * this.field3636.getValue()),
               0.0,
               1.0
            );
            bb = bb.expand(var13 * -0.5);
         }

         Renderer3D var14 = this.field3619.method461() == PlaceRenderMode.Shader ? this.field3637 : event.field1950;
         var14.method1273(bb, color, this.field3619.method461() == PlaceRenderMode.Shader ? color : outline, ShapeMode.Full, 0);
      }
   }

   public void method2015(Render3DEvent event, Placement renderPos, BozeDrawColor color, BozeDrawColor outline) {
      if (this.isEnabled()) {
         if (System.currentTimeMillis() - renderPos.method1159() <= (long)method2010()) {
            if (this.field3619.method461() == PlaceRenderMode.Simple
               && this.field3631.method419()
               && System.currentTimeMillis() - renderPos.method1159() > (long)((int)((float)method2010() / 2.0F))) {
               float var8 = MathHelper.clamp(
                  1.0F - ((float)(System.currentTimeMillis() - renderPos.method1159()) - (float)method2010() / 2.0F) / ((float)method2010() / 2.0F), 0.0F, 1.0F
               );
               color = (BozeDrawColor)color.method964().method196((int)((float)color.field411 * var8));
               outline = (BozeDrawColor)outline.method964().method196((int)((float)outline.field411 * var8));
            }

            if (this.field3630.method419()) {
               color = (BozeDrawColor)OldColors.INSTANCE.clientGradient.method1362().method964().method196(color.field411);
               outline = (BozeDrawColor)OldColors.INSTANCE.clientGradient.method1362().method964().method196(outline.field411);
            }

            Box var11 = new Box(
               (double)renderPos.field2151[0],
               (double)renderPos.field2151[1],
               (double)renderPos.field2151[2],
               (double)(renderPos.field2151[0] + 1),
               (double)(renderPos.field2151[1] + 1),
               (double)(renderPos.field2151[2] + 1)
            );
            if (this.field3633.method419() && (double)(System.currentTimeMillis() - renderPos.field2152) < (double)method2010() * this.field3634.getValue()) {
               double var9 = 1.0
                  - MathHelper.clamp((double)(System.currentTimeMillis() - renderPos.field2152) / ((double)method2010() * this.field3634.getValue()), 0.0, 1.0);
               var11 = var11.expand(var9 * -0.5);
            }

            if (this.field3635.method419()
               && (double)(System.currentTimeMillis() - renderPos.field2152) > (double)method2010() * (1.0 - this.field3636.getValue())) {
               double var12 = MathHelper.clamp(
                  ((double)(System.currentTimeMillis() - renderPos.field2152) - (double)method2010() * (1.0 - this.field3636.getValue()))
                     / ((double)method2010() * this.field3636.getValue()),
                  0.0,
                  1.0
               );
               var11 = var11.expand(var12 * -0.5);
            }

            Renderer3D var13 = this.field3619.method461() == PlaceRenderMode.Shader ? this.field3637 : event.field1950;
            var13.method1273(var11, color, this.field3619.method461() == PlaceRenderMode.Shader ? color : outline, ShapeMode.Full, 0);
         }
      }
   }

   private ShaderMode method2016() {
      if (this.field3620.method461() == ShaderMode.Image) {
         if (!this.field3627.method1322().isEmpty() && (!this.field3627.method1322().equals(this.field3639) || this.field3638 == null)) {
            File var4 = new File(ConfigManager.images, this.field3627.method1322() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.field3638 = ByteTexturePacker.method493(var5);
               if (this.field3638 != null) {
                  this.field3639 = this.field3627.method1322();
               } else {
                  this.field3639 = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field3627.method1341("");
               this.field3639 = "";
            }
         }

         if (this.field3638 != null) {
            return ShaderMode.Image;
         }
      }

      return this.field3620.method461() == ShaderMode.Rainbow ? ShaderMode.Rainbow : ShaderMode.Colored;
   }

   public Placement method2017(BlockPos pos) {
      Placement var4 = this.field3640.method5993();
      var4.method1157(pos, System.currentTimeMillis());
      return var4;
   }

   private void lambda$onRender3DPost$6(Render3DEvent var1) {
      this.field3637.method1219(var1.matrix);
   }

   private boolean lambda$new$5() {
      return this.field3619.method461() == PlaceRenderMode.Simple;
   }

   private boolean lambda$new$4() {
      return this.field3620.method461() != ShaderMode.Colored;
   }

   private boolean lambda$new$3() {
      return this.field3620.method461() == ShaderMode.Rainbow;
   }

   private boolean lambda$new$2() {
      return this.field3620.method461() == ShaderMode.Image;
   }

   private boolean lambda$new$1() {
      return this.field3623.method423() > 0.0F;
   }

   private boolean lambda$new$0() {
      return this.field3619.method461() == PlaceRenderMode.Shader;
   }
}
