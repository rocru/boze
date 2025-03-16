package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.HoleESPIgnoreOwn;
import dev.boze.client.enums.HoleESPMode;
import dev.boze.client.enums.HoleESPOptimization;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.settings.StringSetting;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.holeesp.HoleESPRunnable;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.ByteTexture;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import mapped.Class3071;
import mapped.Class3085;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HoleESP extends Module {
   public static final HoleESP INSTANCE = new HoleESP();
   private final EnumSetting<HoleESPOptimization> field851 = new EnumSetting<HoleESPOptimization>(
      "Optimization", HoleESPOptimization.Normal, "Mode for hole scanning"
   );
   private final IntSetting field852 = new IntSetting("ScanInterval", 5, 1, 10, 1, "Interval in ticks for scanning for holes", this::lambda$new$0);
   private final BooleanSetting field853 = new BooleanSetting("FrustumCheck", false, "Check if holes are in your FOV when scanning");
   private final BooleanSetting field854 = new BooleanSetting("Double", true, "Scan double holes");
   private final EnumSetting<HoleESPIgnoreOwn> field855 = new EnumSetting<HoleESPIgnoreOwn>("IgnoreOwn", HoleESPIgnoreOwn.Off, "Don't render your own hole");
   private final IntSetting field856 = new IntSetting("Range", 16, 4, 56, 1, "Horizontal range for hole scanning");
   private final IntSetting field857 = new IntSetting("DownRange", 5, 0, 50, 1, "Downwards range for hole scanning");
   private final IntSetting field858 = new IntSetting("UpRange", 2, 0, 50, 1, "Upwards range for hole scanning");
   public final EnumSetting<HoleESPMode> field859 = new EnumSetting<HoleESPMode>("Mode", HoleESPMode.Simple, "Mode for drawing holes");
   private final BooleanSetting field860 = new BooleanSetting("SidesOnly", false, "Only render sides of holes");
   public final EnumSetting<ShaderMode> field861 = new EnumSetting<ShaderMode>("Shader", ShaderMode.Colored, "Shader to use", this::lambda$new$1);
   public final BooleanSetting field862 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field861);
   public final IntSetting field863 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field861);
   public final FloatSetting field864 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field861);
   public final FloatSetting field865 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$2, this.field861);
   private final IntSetting field866 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field861);
   private final FloatSetting field867 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field861);
   public final StringSetting field868 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$3, this.field861);
   public final ColorSetting field869 = new ColorSetting("Fill", new BozeDrawColor(1681640397), "Color for shader fill", this::lambda$new$4, this.field861);
   public final ColorSetting field870 = new ColorSetting("Outline", new BozeDrawColor(-12858419), "Color for shader outline", this::lambda$new$5, this.field861);
   private final BooleanSetting field871 = new BooleanSetting("Shrink", false, "Shrink hole height");
   private final FloatSetting field872 = new FloatSetting("MinDist", 10.0F, 0.0F, 20.0F, 0.5F, "Min distance for shrinking", this.field871);
   private final FloatSetting field873 = new FloatSetting("Factor", 0.15F, 0.01F, 0.5F, 0.01F, "Factor for shrinking", this.field871);
   private final BooleanSetting field874 = new BooleanSetting("Fade", true, "Fade hole opacity", this::lambda$new$6);
   private final FloatSetting field875 = new FloatSetting("MinDist", 10.0F, 0.0F, 20.0F, 0.5F, "Min distance for fading", this.field874);
   private final FloatSetting field876 = new FloatSetting("Factor", 0.15F, 0.01F, 0.5F, 0.01F, "Factor for fading", this.field874);
   private final BooleanSetting field877 = new BooleanSetting("Safe", true, "Show safe holes");
   private final FloatSetting field878 = new FloatSetting("Height", 1.0F, -2.0F, 2.0F, 0.1F, "Height for safe holes");
   private final RGBASetting field879 = new RGBASetting("Top", new RGBAColor(0, 255, 0, 75), "Top color for safe holes", this::lambda$new$7, this.field877);
   private final RGBASetting aa = new RGBASetting("TopLine", new RGBAColor(0, 255, 0, 255), "Top line color for safe holes", this.field877);
   private final RGBASetting ab = new RGBASetting("Bottom", new RGBAColor(0, 255, 0, 75), "Bottom color for safe holes", this::lambda$new$8, this.field877);
   private final RGBASetting ac = new RGBASetting("BottomLine", new RGBAColor(0, 255, 0, 255), "Bottom line color for safe holes", this.field877);
   private final BooleanSetting ad = new BooleanSetting("Unsafe", true, "Show unsafe holes");
   private final FloatSetting ae = new FloatSetting("Height", 1.0F, -2.0F, 2.0F, 0.1F, "Height for unsafe holes");
   private final RGBASetting af = new RGBASetting("Top", new RGBAColor(255, 0, 0, 75), "Top color for unsafe holes", this::lambda$new$9, this.ad);
   private final RGBASetting ag = new RGBASetting("TopLine", new RGBAColor(255, 0, 0, 255), "Top line color for unsafe holes", this.ad);
   private final RGBASetting ah = new RGBASetting("Bottom", new RGBAColor(255, 0, 0, 75), "Bottom color for unsafe holes", this::lambda$new$10, this.ad);
   private final RGBASetting ai = new RGBASetting("BottomLine", new RGBAColor(255, 0, 0, 255), "Bottom line color for unsafe holes", this.ad);
   private final BooleanSetting aj = new BooleanSetting("Void", false, "Show void holes");
   private final FloatSetting ak = new FloatSetting("Height", 1.0F, -2.0F, 2.0F, 0.1F, "Height for rendering void holes", this.aj);
   private final RGBASetting al = new RGBASetting("Top", new RGBAColor(255, 0, 255, 75), "Top color for void holes", this::lambda$new$11, this.aj);
   private final RGBASetting am = new RGBASetting("TopLine", new RGBAColor(255, 0, 255, 255), "Top line color for void holes", this.aj);
   private final RGBASetting an = new RGBASetting("Bottom", new RGBAColor(255, 0, 255, 75), "Bottom color for void holes", this::lambda$new$12, this.aj);
   private final RGBASetting ao = new RGBASetting("BottomLine", new RGBAColor(255, 0, 255, 255), "Bottom line color for void holes", this.aj);
   private Renderer3D ap;
   private ByteTexture aq;
   private String ar = "";
   private List<BlockPos> as = new ArrayList();
   private List<BlockPos> at = new ArrayList();
   private List<Class3085> au = new ArrayList();
   private List<Class3085> av = new ArrayList();
   private List<BlockPos> aw = new ArrayList();
   private int ax;
   private boolean ay;
   private double az;
   private double aA;
   private int aB;

   public HoleESP() {
      super("HoleESP", "Shows safe holes for Crystal PVP", Category.Render);
   }

   @Override
   public String method1322() {
      return Integer.toString(this.aB);
   }

   @EventHandler
   private void method1831(PrePlayerTickEvent var1) {
      if (this.field851.method461() == HoleESPOptimization.Thread) {
         new Thread(new HoleESPRunnable(this)).start();
      } else {
         this.method1904();
      }
   }

   @EventHandler
   private void method2071(Render3DEvent var1) {
      this.ay = true;
      this.aB = 0;
      if (this.ap == null) {
         this.ap = new Renderer3D(false, !this.field860.method419());
      } else if (this.ap.field2174 == this.field860.method419()) {
         this.ap = new Renderer3D(false, !this.field860.method419());
      }

      this.ap.method1217();
      this.az = mc.player.getCameraPosVec(var1.field1951).y;
      this.aA = Class3071.method6019(mc.player).y;

      for (BlockPos var6 : this.as) {
         this.method386(
            var1,
            new Box(
               (double)var6.getX(),
               (double)var6.getY(),
               (double)var6.getZ(),
               (double)(var6.getX() + 1),
               (double)var6.getY() + (double)this.field878.method423().floatValue() * this.method388(var6),
               (double)(var6.getZ() + 1)
            ),
            this.field879.method1347(),
            this.aa.method1347(),
            this.ab.method1347(),
            this.ac.method1347()
         );
      }

      for (BlockPos var11 : this.at) {
         this.method386(
            var1,
            new Box(
               (double)var11.getX(),
               (double)var11.getY(),
               (double)var11.getZ(),
               (double)(var11.getX() + 1),
               (double)var11.getY() + (double)this.ae.method423().floatValue() * this.method388(var11),
               (double)(var11.getZ() + 1)
            ),
            this.af.method1347(),
            this.ag.method1347(),
            this.ah.method1347(),
            this.ai.method1347()
         );
      }

      for (Class3085 var12 : this.au) {
         this.method386(
            var1,
            new Box(
               (double)var12.field206.getX(),
               (double)var12.field206.getY(),
               (double)var12.field206.getZ(),
               (double)(var12.field207.getX() + 1),
               (double)var12.field206.getY() + (double)this.field878.method423().floatValue() * this.method389(var12),
               (double)(var12.field207.getZ() + 1)
            ),
            this.field879.method1347(),
            this.aa.method1347(),
            this.ab.method1347(),
            this.ac.method1347()
         );
      }

      for (Class3085 var13 : this.av) {
         this.method386(
            var1,
            new Box(
               (double)var13.field206.getX(),
               (double)var13.field206.getY(),
               (double)var13.field206.getZ(),
               (double)(var13.field207.getX() + 1),
               (double)var13.field206.getY() + (double)this.ae.method423().floatValue() * this.method389(var13),
               (double)(var13.field207.getZ() + 1)
            ),
            this.af.method1347(),
            this.ag.method1347(),
            this.ah.method1347(),
            this.ai.method1347()
         );
      }

      for (BlockPos var14 : this.aw) {
         this.method386(
            var1,
            new Box(
               (double)var14.getX(),
               (double)var14.getY(),
               (double)var14.getZ(),
               (double)(var14.getX() + 1),
               (double)((float)var14.getY() + this.ak.method423()),
               (double)(var14.getZ() + 1)
            ),
            this.al.method1347(),
            this.am.method1347(),
            this.an.method1347(),
            this.ao.method1347()
         );
      }

      if (this.field859.method461() == HoleESPMode.Shader) {
         ChamsShaderRenderer.method1310(
            this::lambda$onRender3D$13,
            this.method1921(),
            this.field862.method419(),
            this.field869,
            this.field870,
            this.field866.method434(),
            this.field867.method423(),
            this.field864.method423(),
            this.field865.method423(),
            this.field863.method434(),
            this.aq
         );
      } else {
         this.ap.method1219(var1.matrix);
      }

      this.ay = false;
   }

   private ShaderMode method1921() {
      if (this.field861.method461() == ShaderMode.Image) {
         if (!this.field868.method1322().isEmpty() && (!this.field868.method1322().equals(this.ar) || this.aq == null)) {
            File var4 = new File(ConfigManager.images, this.field868.method1322() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.aq = ByteTexturePacker.method493(var5);
               if (this.aq != null) {
                  this.ar = this.field868.method1322();
               } else {
                  this.ar = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field868.method1341("");
               this.ar = "";
            }
         }

         if (this.aq != null) {
            return ShaderMode.Image;
         }
      }

      return this.field861.method461() == ShaderMode.Rainbow ? ShaderMode.Rainbow : ShaderMode.Colored;
   }

   private void method386(Render3DEvent var1, Box var2, RGBAColor var3, RGBAColor var4, RGBAColor var5, RGBAColor var6) {
      if (this.field855.method461() != HoleESPIgnoreOwn.Off && var2.intersects(mc.player.getBoundingBox())) {
         if (this.field855.method461() == HoleESPIgnoreOwn.Eye) {
            if (var2.maxY > this.az - 0.25) {
               if (!(var2.minY < this.az - 0.25)) {
                  return;
               }

               var2 = var2.withMaxY(this.az - 0.25);
            }
         } else {
            if (this.field855.method461() != HoleESPIgnoreOwn.Feet) {
               return;
            }

            if (var2.maxY > this.aA) {
               if (!(var2.minY < this.aA)) {
                  return;
               }

               var2 = var2.withMaxY(this.aA);
            }
         }
      }

      this.aB++;
      boolean var10 = this.field859.method461() == HoleESPMode.Shader;
      if (!var10 && this.field874.method419()) {
         double var11 = this.method387(var2);
         var3 = var3.copy().method196((int)((double)var3.field411 * var11));
         var4 = var3.copy().method196((int)((double)var4.field411 * var11));
         var5 = var5.copy().method196((int)((double)var5.field411 * var11));
         var6 = var5.copy().method196((int)((double)var6.field411 * var11));
      }

      this.ap
         .method1267(
            var2, var10 ? var4 : var3, var4, var10 ? var6 : var5, var6, var10 ? ShapeMode.Triangles : ShapeMode.Full, this.field860.method419() ? 6 : 0
         );
   }

   private void method1904() {
      if (this.ax < this.field852.method434()) {
         this.ax++;
         if (this.field851.method461() != HoleESPOptimization.Off) {
            return;
         }
      }

      if (!this.ay) {
         this.ax = 0;
         Frustum var4 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
         ArrayList var5 = new ArrayList(this.as.size());
         ArrayList var6 = new ArrayList(this.at.size());
         ArrayList var7 = new ArrayList(this.au.size());
         ArrayList var8 = new ArrayList(this.av.size());
         ArrayList var9 = new ArrayList(this.aw.size());
         int var10 = (int)mc.player.getX();
         int var11 = (int)mc.player.getY();
         int var12 = (int)mc.player.getZ();

         for (int var13 = var10 - this.field856.method434(); var13 <= var10 + this.field856.method434(); var13++) {
            for (int var14 = var12 - this.field856.method434(); var14 <= var12 + this.field856.method434(); var14++) {
               for (int var15 = Math.max(mc.world.getBottomY(), var11 - this.field857.method434());
                  var15 <= var11 + this.field858.method434() && var15 <= mc.world.getTopY();
                  var15++
               ) {
                  BlockPos var16 = new BlockPos(var13, var15, var14);
                  if ((
                        !mc.world.getBlockState(var16).blocksMovement()
                           || !mc.world.getBlockState(var16.add(0, 1, 0)).blocksMovement()
                           || !mc.world.getBlockState(var16.add(0, 2, 0)).blocksMovement()
                     )
                     && (!this.field853.method419() || var4 == null || var4.isVisible(new Box(var16).expand(1.0)))) {
                     if (this.aj.method419() && var16.getY() == mc.world.getBottomY() && mc.world.getBlockState(var16).getBlock() != Blocks.BEDROCK) {
                        var9.add(var16);
                     } else if (method2101(var16)) {
                        var5.add(var16);
                     } else if (method2102(var16)) {
                        var6.add(var16);
                     } else if (this.field854.method419()) {
                        BlockPos var17;
                        if ((var17 = this.method1511(var16)) != null) {
                           var7.add(new Class3085(var16, var17));
                        } else if ((var17 = this.method1512(var16)) != null) {
                           var8.add(new Class3085(var16, var17));
                        }
                     }
                  }
               }
            }
         }

         this.as = var5;
         this.at = var6;
         this.au = var7;
         this.av = var8;
         this.aw = var9;
      }
   }

   private double method387(Box var1) {
      if (!this.field874.method419()) {
         return 1.0;
      } else {
         double var5 = var1.getCenter().distanceTo(mc.player.getEyePos());
         if (var5 <= (double)this.field875.method423().floatValue()) {
            return 1.0;
         } else {
            var5 -= (double)this.field875.method423().floatValue();
            return 1.0 - MathHelper.clamp(var5 / (1.0 / (double)this.field876.method423().floatValue()), 0.0, 1.0);
         }
      }
   }

   private double method388(BlockPos var1) {
      if (!this.field871.method419()) {
         return 1.0;
      } else {
         double var5 = new Vec3d((double)var1.getX() + 0.5, (double)var1.getY(), (double)var1.getZ() + 0.5).distanceTo(mc.player.getEyePos());
         if (var5 <= (double)this.field872.method423().floatValue()) {
            return 1.0;
         } else {
            var5 -= (double)this.field872.method423().floatValue();
            return 1.0 - MathHelper.clamp(var5 / (1.0 / (double)this.field873.method423().floatValue()), 0.0, 1.0);
         }
      }
   }

   private double method389(Class3085 var1) {
      if (!this.field871.method419()) {
         return 1.0;
      } else {
         double var5 = new Box(var1.field206).union(new Box(var1.field207)).getCenter().distanceTo(mc.player.getEyePos());
         if (var5 <= (double)this.field872.method423().floatValue()) {
            return 1.0;
         } else {
            var5 -= (double)this.field872.method423().floatValue();
            return 1.0 - MathHelper.clamp(var5 / (1.0 / (double)this.field873.method423().floatValue()), 0.0, 1.0);
         }
      }
   }

   public static boolean method2101(BlockPos pos) {
      if (mc.world.getBlockState(pos).isAir() && mc.world.getBlockState(pos.up()).isAir() && mc.world.getBlockState(pos.up(2)).isAir()) {
         for (Direction var7 : Direction.values()) {
            if (var7 != Direction.UP && mc.world.getBlockState(pos.offset(var7)).getBlock() != Blocks.BEDROCK) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean method2102(BlockPos pos) {
      if (mc.world.getBlockState(pos).isAir() && mc.world.getBlockState(pos.up()).isAir() && mc.world.getBlockState(pos.up(2)).isAir()) {
         for (Direction var7 : Direction.values()) {
            if (var7 != Direction.UP) {
               Block var8 = mc.world.getBlockState(pos.offset(var7)).getBlock();
               if (var8 != Blocks.BEDROCK && var8 != Blocks.OBSIDIAN && var8 != Blocks.CRYING_OBSIDIAN) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public BlockPos method1511(BlockPos pos) {
      if (mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos).isAir()
         && mc.world.getBlockState(pos.up()).isAir()
         && mc.world.getBlockState(pos.up(2)).isAir()
         && mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK
         && mc.world.getBlockState(pos.east()).isAir()
         && mc.world.getBlockState(pos.east().up()).isAir()
         && mc.world.getBlockState(pos.east().up(2)).isAir()) {
         return pos.add(1, 0, 0);
      } else {
         return mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos).isAir()
               && mc.world.getBlockState(pos.up()).isAir()
               && mc.world.getBlockState(pos.up(2)).isAir()
               && mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.BEDROCK
               && mc.world.getBlockState(pos.south()).isAir()
               && mc.world.getBlockState(pos.south().up()).isAir()
               && mc.world.getBlockState(pos.south().up(2)).isAir()
            ? pos.add(0, 0, 1)
            : null;
      }
   }

   public BlockPos method1512(BlockPos pos) {
      if ((
            mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.down()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK
         )
         && (
            mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.west()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK
         )
         && (
            mc.world.getBlockState(pos.south()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.south()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK
         )
         && (
            mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.north()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK
         )
         && mc.world.getBlockState(pos).isAir()
         && mc.world.getBlockState(pos.up()).isAir()
         && mc.world.getBlockState(pos.up(2)).isAir()
         && (
            mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.east().down()).getBlock() == Blocks.BEDROCK
         )
         && (
            mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.east(2)).getBlock() == Blocks.BEDROCK
         )
         && (
            mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.east().south()).getBlock() == Blocks.BEDROCK
         )
         && (
            mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.OBSIDIAN
               || mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.CRYING_OBSIDIAN
               || mc.world.getBlockState(pos.east().north()).getBlock() == Blocks.BEDROCK
         )
         && mc.world.getBlockState(pos.east()).isAir()
         && mc.world.getBlockState(pos.east().up()).isAir()
         && mc.world.getBlockState(pos.east().up(2)).isAir()) {
         return pos.add(1, 0, 0);
      } else {
         return (
                  mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.down()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK
               )
               && (
                  mc.world.getBlockState(pos.west()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.west()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK
               )
               && (
                  mc.world.getBlockState(pos.east()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.east()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK
               )
               && (
                  mc.world.getBlockState(pos.north()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.north()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK
               )
               && mc.world.getBlockState(pos).isAir()
               && mc.world.getBlockState(pos.up()).isAir()
               && mc.world.getBlockState(pos.up(2)).isAir()
               && (
                  mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.south().down()).getBlock() == Blocks.BEDROCK
               )
               && (
                  mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.south(2)).getBlock() == Blocks.BEDROCK
               )
               && (
                  mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.south().east()).getBlock() == Blocks.BEDROCK
               )
               && (
                  mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.OBSIDIAN
                     || mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.CRYING_OBSIDIAN
                     || mc.world.getBlockState(pos.south().west()).getBlock() == Blocks.BEDROCK
               )
               && mc.world.getBlockState(pos.south()).isAir()
               && mc.world.getBlockState(pos.south().up()).isAir()
               && mc.world.getBlockState(pos.south().up(2)).isAir()
            ? pos.add(0, 0, 1)
            : null;
      }
   }

   private void lambda$onRender3D$13(Render3DEvent var1) {
      this.ap.method1219(var1.matrix);
   }

   private boolean lambda$new$12() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$11() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$10() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$9() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$8() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$7() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$6() {
      return this.field859.method461() != HoleESPMode.Shader;
   }

   private boolean lambda$new$5() {
      return this.field861.method461() != ShaderMode.Colored;
   }

   private boolean lambda$new$4() {
      return this.field861.method461() == ShaderMode.Rainbow;
   }

   private boolean lambda$new$3() {
      return this.field861.method461() == ShaderMode.Image;
   }

   private boolean lambda$new$2() {
      return this.field864.method423() > 0.0F;
   }

   private boolean lambda$new$1() {
      return this.field859.method461() == HoleESPMode.Shader;
   }

   private boolean lambda$new$0() {
      return this.field851.method461() != HoleESPOptimization.Off;
   }
}
