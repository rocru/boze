package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.SearchShader;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.player.RotationHelper;
import dev.boze.client.utils.render.ByteTexture;
import mapped.Class3071;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.CopyOnWriteArrayList;

public class Search extends Module {
   public static final Search INSTANCE = new Search();
   public final StringModeSetting field3666 = new StringModeSetting("Blocks", "Blocks to highlight");
   public final ColorSetting field3667 = new ColorSetting("Color", new BozeDrawColor(1685836692), "Color for fill");
   public final ColorSetting field3668 = new ColorSetting("Outline", new BozeDrawColor(-8662124), "Color for outline");
   public final BooleanSetting field3669 = new BooleanSetting("Shader", false, "Use a shader");
   public final EnumSetting<SearchShader> field3670 = new EnumSetting<SearchShader>("Shader", SearchShader.Normal, "Shader to use", this.field3669);
   public final BooleanSetting field3671 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3669);
   public final IntSetting field3672 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3669);
   public final FloatSetting field3673 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3669);
   public final FloatSetting field3674 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3669);
   public final IntSetting field3675 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3669);
   public final FloatSetting field3676 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3669);
   public final StringSetting field3677 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3669);
   private final BooleanSetting field3678 = new BooleanSetting("Tracers", false, "Draw tracers to blocks");
   private final FloatSetting field3679 = new FloatSetting("Width", 1.0F, 1.0F, 5.0F, 0.1F, "Line width", Search::lambda$new$2, this.field3678);
   private final IntSetting field3680 = new IntSetting("MaxDist", 250, 10, 500, 1, "Max distance", this.field3678);
   private final IntSetting field3681 = new IntSetting("MinDist", 0, 0, 50, 1, "Min distance", this.field3678);
   private final BooleanSetting field3682 = new BooleanSetting("DistanceColor", false, "Color code tracers by their distance", this.field3678);
   private final RGBASetting field3683 = new RGBASetting("Color", new RGBAColor(-65536), "Color for tracers", this::lambda$new$3, this.field3678);
   private final RGBASetting field3684 = new RGBASetting(
      "Minimum", new RGBAColor(-65536), "Color for minimum distance", this.field3682::method419, this.field3678
   );
   private final RGBASetting field3685 = new RGBASetting(
      "Maximum", new RGBAColor(-16711936), "Color for maximum distance", this.field3682::method419, this.field3678
   );
   private final BooleanSetting field3686 = new BooleanSetting("TracerFade", false, "Fade tracer opacity", this.field3678);
   private final FloatSetting field3687 = new FloatSetting(
      "MinDist", 8.0F, 0.0F, 50.0F, 0.5F, "Min distance for fading", this.field3686::method419, this.field3678
   );
   private final FloatSetting field3688 = new FloatSetting(
      "Factor", 0.005F, 0.002F, 0.1F, 0.002F, "Factor for fading", this.field3686::method419, this.field3678
   );
   private Renderer3D field3689;
   private ByteTexture field3690;
   private String field3691 = "";
   public CopyOnWriteArrayList<BlockPos> field3692 = new CopyOnWriteArrayList();
   private Renderer3D field3693 = null;

   public Search() {
      super(
         "Search",
         "Add block: .search add <block name>\nRemove block: .search del <block name>\nList blocks: .search list\nClear blocks: .search clear\n",
         Category.Render
      );
   }

   public boolean method2023(Block block) {
      return this.field3666.method2032().contains(block);
   }

   @Override
   public void onEnable() {
      if (mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }
   }

   @EventHandler
   private void method2024(Render3DEvent var1) {
      if (this.field3669.method419()) {
         if (this.field3689 == null) {
            this.field3689 = new Renderer3D(false, true);
         }

         this.field3689.method1217();
      }

      this.field3692.removeIf(this::lambda$onRender3D$4);

      for (BlockPos var6 : this.field3692) {
         BlockState var7 = mc.world.getBlockState(var6);
         Object var8 = null;
         VoxelShape var9 = var7.getOutlineShape(mc.world, var6);
         if (var9.isEmpty()) {
            var8 = new Box(var6);
         } else {
            var8 = var9.getBoundingBox().offset(var6);
         }

         if (var8 != null) {
            if (this.field3669.method419()) {
               this.field3689
                  .method1268(
                     ((Box)var8).minX, ((Box)var8).minY, ((Box)var8).minZ, ((Box)var8).maxX, ((Box)var8).maxY, ((Box)var8).maxZ, this.field3668.method1362(), 0
                  );
            } else {
               var1.field1950.method1273((Box)var8, this.field3667.method1362(), this.field3668.method1362(), ShapeMode.Full, 0);
            }
         }
      }

      if (this.field3669.method419()) {
         ChamsShaderRenderer.method1310(
            this::lambda$onRender3D$5,
            this.method2027(),
            this.field3671.method419(),
            this.field3667,
            this.field3668,
            this.field3675.method434(),
            this.field3676.method423(),
            this.field3673.method423(),
            this.field3674.method423(),
            this.field3672.method434(),
            this.field3690
         );
      }

      if (this.field3678.method419()) {
         if (this.field3693 == null) {
            this.field3693 = new Renderer3D();
         }

         this.field3693.field2166.field1594 = this.field3679.method423();
         this.field3693.field2170.field1594 = this.field3679.method423();
         this.field3693.method1217();

         for (BlockPos var16 : this.field3692) {
            Vec3d var17 = null;
            BlockState var20 = mc.world.getBlockState(var16);
            VoxelShape var21 = var20.getOutlineShape(mc.world, var16);
            if (var21 != null && !var21.isEmpty()) {
               var17 = var21.getBoundingBox().getCenter().add((double)var16.getX(), (double)var16.getY(), (double)var16.getZ());
            } else {
               var17 = new Vec3d((double)var16.getX() + 0.5, (double)var16.getY() + 0.5, (double)var16.getZ() + 0.5);
            }

            double var10 = var17.distanceTo(RotationHelper.field3956);
            if (!(var10 > (double)this.field3680.method434().intValue()) && !(var10 < (double)this.field3681.method434().intValue())) {
               RGBAColor var12 = this.method2025(var10);
               if (this.field3686.method419()) {
                  double var13 = this.method2026(var10);
                  var12 = var12.copy().method196((int)((double)var12.field411 * var13));
               }

               if (var12 instanceof BozeDrawColor) {
                  this.field3693
                     .method1241(
                        RotationHelper.field3956.x, RotationHelper.field3956.y, RotationHelper.field3956.z, var17.x, var17.y, var17.z, (BozeDrawColor)var12
                     );
               } else {
                  this.field3693
                     .method1236(RotationHelper.field3956.x, RotationHelper.field3956.y, RotationHelper.field3956.z, var17.x, var17.y, var17.z, var12);
               }
            }
         }

         this.field3693.method1219(var1.matrix);
      }
   }

   private RGBAColor method2025(double var1) {
      return this.field3682.method419()
         ? Class3071.method6016(
            this.field3684.method1347(),
            this.field3685.method1347(),
            (var1 - (double)this.field3681.method434().intValue()) / (double)this.field3680.method434().intValue()
         )
         : this.field3683.method1347();
   }

   private double method2026(double var1) {
      if (var1 <= (double)this.field3687.method423().floatValue()) {
         return 1.0;
      } else {
         var1 -= (double)this.field3687.method423().floatValue();
         return 1.0 - MathHelper.clamp(var1 / (1.0 / (double)this.field3688.method423().floatValue()), 0.0, 1.0);
      }
   }

   private ShaderMode method2027() {
      if (this.field3670.method461() == SearchShader.Image) {
         if (!this.field3677.method1322().isEmpty() && (!this.field3677.method1322().equals(this.field3691) || this.field3690 == null)) {
            File var4 = new File(ConfigManager.images, this.field3677.method1322() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.field3690 = ByteTexturePacker.method493(var5);
               if (this.field3690 != null) {
                  this.field3691 = this.field3677.method1322();
               } else {
                  this.field3691 = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field3677.method1341("");
               this.field3691 = "";
            }
         }

         if (this.field3690 != null) {
            return ShaderMode.Image;
         }
      }

      return ShaderMode.Rainbow;
   }

   private void lambda$onRender3D$5(Render3DEvent var1) {
      this.field3689.method1219(var1.matrix);
   }

   private boolean lambda$onRender3D$4(BlockPos var1) {
      return !this.method2023(mc.world.getBlockState(var1).getBlock());
   }

   private boolean lambda$new$3() {
      return !this.field3682.method419();
   }

   private static boolean lambda$new$2() {
      return !MinecraftClient.IS_SYSTEM_MAC;
   }

   private boolean lambda$new$1() {
      return this.field3670.method461() == SearchShader.Image;
   }

   private boolean lambda$new$0() {
      return this.field3673.method423() > 0.0F;
   }
}
