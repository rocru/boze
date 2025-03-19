package dev.boze.client.systems.modules.misc;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.*;
import dev.boze.client.events.MovementEvent;
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
import dev.boze.client.systems.modules.misc.autotool.qG;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.render.ByteTexture;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.shape.VoxelShape;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class Nuker extends Module {
   public static final Nuker INSTANCE = new Nuker();
   private final BooleanSetting field2976 = new BooleanSetting("Render", true, "Render block being mined");
   private final EnumSetting<NukerHighlight> field2977 = new EnumSetting<NukerHighlight>(
      "Mode", NukerHighlight.Normal, "Mode for drawing highlight", this.field2976
   );
   public final ColorSetting field2978 = new ColorSetting("Color", new BozeDrawColor(105015015), "Color for fill", this.field2976);
   public final ColorSetting field2979 = new ColorSetting("Outline", new BozeDrawColor(-891161), "Color for outline", this.field2976);
   private final BooleanSetting field2980 = new BooleanSetting("Shader", false, "Use a shader", this.field2976::method419);
   public final EnumSetting<NukerShader> field2981 = new EnumSetting<NukerShader>("Shader", NukerShader.Normal, "Shader to use", this.field2980);
   public final BooleanSetting field2982 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field2980);
   public final IntSetting field2983 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field2980);
   public final FloatSetting field2984 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field2980);
   public final FloatSetting field2985 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field2980);
   private final IntSetting field2986 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field2980);
   private final FloatSetting field2987 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field2980);
   public final StringSetting field2988 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field2980);
   private final BooleanSetting field2989 = new BooleanSetting("Fade", true, "Fade placement renders", this::lambda$new$2);
   private final IntSetting field2990 = new IntSetting("Ticks", 2, 1, 20, 1, "Amount of ticks to render placements for", this::lambda$new$3, this.field2989);
   private final BooleanSetting field2991 = new BooleanSetting("Rotate", true, "Rotate");
   private final BooleanSetting field2992 = new BooleanSetting("Swing", true, "Swing");
   private final IntSetting field2993 = new IntSetting("Range", 5, 1, 7, 1, "Mine range");
   private final MinMaxSetting field2994 = new MinMaxSetting("StrictRange", 4.2, 0.0, 6.0, 0.1, "Strict range");
   private final IntSetting field2995 = new IntSetting("MaxActions", 3, 1, 8, 1, "Max mines per tick (for instant mine)");
   private final IntSetting field2996 = new IntSetting("Interval", 0, 0, 5, 1, "Mining interval");
   private final BooleanSetting field2997 = new BooleanSetting("PacketMine", false, "Packet mine blocks");
   private final BooleanSetting field2998 = new BooleanSetting("Flatten", true, "Flatten");
   private final EnumSetting<NukerPriority> field2999 = new EnumSetting<NukerPriority>("Priority", NukerPriority.Closest, "Block priority");
   public final StringModeSetting field3000 = new StringModeSetting("Blocks", "Blocks to filter");
   private final EnumSetting<NukerFilter> field3001 = new EnumSetting<NukerFilter>("Filter", NukerFilter.Off, "Block filter");
   private boolean field3002;
   private int field3003;
   private int field3004;
   private final Mutable aa = new Mutable();
   private ConcurrentHashMap<BlockPos, Long> ab = new ConcurrentHashMap();
   private ConcurrentHashMap<Box, Long> ac = new ConcurrentHashMap();
   private Renderer3D ad = null;
   private ByteTexture ae;
   private String af = "";

   public Nuker() {
      super("Nuker", "Destroys blocks around you", Category.Misc);
      this.field3000.field951 = false;
      this.addSettings(this.field2976, this.field2980);
   }

   @EventHandler
   public void method1731(Render3DEvent event) {
      if (this.field2976.method419()) {
         this.ab.forEach(this::lambda$onRender3D$4);
         if (this.ab.isEmpty()) {
            if (this.field2989.method419() && !this.field2980.method419()) {
               this.ac.forEach(this::lambda$onRender3D$5);
            }

            return;
         }

         this.ab.forEach(this::lambda$onRender3D$6);
         if (this.field2989.method419() && !this.field2980.method419()) {
            this.ac.forEach(this::lambda$onRender3D$7);
         }

         if (this.field2980.method419()) {
            ChamsShaderRenderer.method1310(
               this::lambda$onRender3D$8,
               this.method1732(),
               this.field2982.method419(),
               this.field2978,
               this.field2979,
               this.field2986.method434(),
               this.field2987.method423(),
               this.field2984.method423(),
               this.field2985.method423(),
               this.field2983.method434(),
               this.ae
            );
         }
      }
   }

   private ShaderMode method1732() {
      if (this.field2981.method461() == NukerShader.Image) {
         if (!this.field2988.method1322().isEmpty() && (!this.field2988.method1322().equals(this.af) || this.ae == null)) {
            File var4 = new File(ConfigManager.images, this.field2988.method1322() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.ae = ByteTexturePacker.method493(var5);
               if (this.ae != null) {
                  this.af = this.field2988.method1322();
               } else {
                  this.af = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field2988.method1341("");
               this.af = "";
            }
         }

         if (this.ae != null) {
            return ShaderMode.Image;
         }
      }

      return ShaderMode.Rainbow;
   }

   private void method1733(Box var1, long var2, Renderer3D var4) {
      float var7 = MathHelper.clamp(1.0F - (float)(System.currentTimeMillis() - var2) / (float)this.method1734(), 0.0F, 1.0F);
      BozeDrawColor var8 = (BozeDrawColor)this.field2978.method1362().copy().method197(var7);
      BozeDrawColor var9 = (BozeDrawColor)this.field2979.method1362().copy().method197(var7);
      var4.method1273(var1, var8, var9, ShapeMode.Full, 0);
   }

   private int method1734() {
      return this.field2990.method434() * 50;
   }

   private void method1735(Box var1, Renderer3D var2) {
      if (this.field2989.method419()) {
         this.ac.put(var1, System.currentTimeMillis());
      }

      if (this.field2980.method419()) {
         var2.method1268(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ, this.field2979.method1362(), 0);
      } else {
         var2.method1273(var1, this.field2978.method1362(), this.field2979.method1362(), ShapeMode.Full, 0);
      }
   }

   @Override
   public void onEnable() {
      this.field3002 = true;
      this.field3003 = 0;
      this.field3004 = 0;
   }

   @EventHandler(
      priority = 17
   )
   private void method1736(MovementEvent var1) {
      if (!var1.method1022()) {
         if (this.field3003 > 0) {
            this.field3003--;
         } else {
            int var5 = mc.player.getBlockX();
            int var6 = mc.player.getBlockY();
            int var7 = mc.player.getBlockZ();
            Vec3d var8 = EntityUtil.method2144(mc.player);
            double var9 = this.field2994.getValue() * this.field2994.getValue();
            ArrayList var11 = new ArrayList();

            for (int var12 = var5 - this.field2993.method434(); var12 <= var5 + this.field2993.method434(); var12++) {
               for (int var13 = var7 - this.field2993.method434(); var13 <= var7 + this.field2993.method434(); var13++) {
                  for (int var14 = Math.max(mc.world.getBottomY(), var6 - this.field2993.method434()); var14 <= var6 + this.field2993.method434(); var14++) {
                     if ((!(this.field2994.getValue() > 0.0) || !(var8.squaredDistanceTo((double)var12 + 0.5, (double)var14 + 0.5, (double)var13 + 0.5) > var9))
                        && (!this.field2998.method419() || !((double)var14 < Math.floor(mc.player.getY())))) {
                        BlockPos var15 = new BlockPos(var12, var14, var13);
                        if (qG.method2101(var15) && this.method1737(mc.world.getBlockState(var15).getBlock())) {
                           var11.add(var15);
                        }
                     }
                  }
               }
            }

            if (this.field2999.method461() == NukerPriority.Highest) {
               var11.sort(Comparator.comparingDouble(Nuker::lambda$onSendMovementPackets$9));
            } else if (this.field2999.method461() != NukerPriority.Random) {
               var11.sort(Comparator.comparingDouble(this::lambda$onSendMovementPackets$10));
            }

            if (var11.isEmpty()) {
               if (this.field3004++ >= this.field2996.method434()) {
                  this.field3002 = true;
               }
            } else {
               this.field3004 = 0;
               if (!this.field3002 && !this.aa.equals(var11.get(0))) {
                  this.field3003 = this.field2996.method434();
                  this.field3002 = false;
                  this.aa.set((Vec3i)var11.get(0));
                  if (this.field3003 > 0) {
                     return;
                  }
               }

               int var21 = 0;

               for (BlockPos var23 : var11) {
                  if (var21 >= this.field2995.method434()) {
                     break;
                  }

                  boolean var24 = qG.method2102(var23);
                  Direction var16 = qG.method737(var23, true, (double)this.field2993.method434().intValue());
                  if (var16 != null) {
                     Vec3d var17 = new Vec3d(
                        (double)var23.getX() + 0.5 + (double)var16.getVector().getX() * 0.5,
                        (double)var23.getY() + 0.5 + (double)var16.getVector().getY() * 0.5,
                        (double)var23.getZ() + 0.5 + (double)var16.getVector().getZ() * 0.5
                     );
                     float[] var18 = EntityUtil.method2146(var17);
                     Object var19 = null;
                     if (this.field2997.method419()) {
                        var19 = new ActionWrapper(this::lambda$onSendMovementPackets$11, var18[0], var18[1], this.field2991.method419());
                     } else {
                        var19 = new ActionWrapper(this::lambda$onSendMovementPackets$12, var18[0], var18[1], this.field2991.method419());
                     }

                     if (this.field2976.method419()) {
                        this.ab.put(var23, System.currentTimeMillis());
                     }

                     var1.method1074((ActionWrapper)var19);
                     this.aa.set(var23);
                     var21++;
                     if (!var24 && !this.field2997.method419()) {
                        break;
                     }
                  }
               }

               this.field3002 = false;
            }
         }
      }
   }

   private boolean method1737(Block var1) {
      if (this.field3001.method461() == NukerFilter.Blacklist) {
         if (this.field3000.method2032().contains(var1)) {
            return false;
         }
      } else if (this.field3001.method461() == NukerFilter.Whitelist && !this.field3000.method2032().contains(var1)) {
         return false;
      }

      return true;
   }

   private void lambda$onSendMovementPackets$12(BlockPos var1) {
      qG.method735(var1, (double)this.field2993.method434().intValue(), true);
   }

   private void lambda$onSendMovementPackets$11(BlockPos var1, Direction var2) {
      mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, var1, var2));
      mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var1, var2));
      if (this.field2992.method419()) {
         mc.player.swingHand(Hand.MAIN_HAND);
      }
   }

   private double lambda$onSendMovementPackets$10(Vec3d var1, BlockPos var2) {
      return var1.squaredDistanceTo((double)var2.getX() + 0.5, (double)var2.getY() + 0.5, (double)var2.getZ() + 0.5)
         * (double)(this.field2999.method461() == NukerPriority.Closest ? 1 : -1);
   }

   private static double lambda$onSendMovementPackets$9(BlockPos var0) {
      return (double)(-1 * var0.getY());
   }

   private void lambda$onRender3D$8(Render3DEvent var1) {
      this.ad.method1219(var1.matrix);
   }

   private void lambda$onRender3D$7(Render3DEvent var1, Box var2, Long var3) {
      if (System.currentTimeMillis() - var3 > (long)this.method1734()) {
         this.ac.remove(var2);
      } else {
         this.method1733(var2, var3, var1.field1950);
      }
   }

   private void lambda$onRender3D$6(Render3DEvent var1, BlockPos var2, Long var3) {
      BlockState var7 = mc.world.getBlockState(var2);
      VoxelShape var8 = var7.getOutlineShape(mc.world, var2);
      if (!var8.isEmpty()) {
         if (this.field2980.method419()) {
            if (this.ad == null) {
               this.ad = new Renderer3D(false, true);
            }

            this.ad.method1217();
         }

         if (this.field2977.method461() == NukerHighlight.Complex) {
            for (Box var10 : var8.getBoundingBoxes()) {
               this.method1735(var10.offset(var2), this.field2980.method419() ? this.ad : var1.field1950);
            }
         } else {
            Box var11 = var8.getBoundingBox();
            this.method1735(var11.offset(var2), this.field2980.method419() ? this.ad : var1.field1950);
         }
      }
   }

   private void lambda$onRender3D$5(Render3DEvent var1, Box var2, Long var3) {
      if (System.currentTimeMillis() - var3 > (long)this.method1734()) {
         this.ac.remove(var2);
      } else {
         this.method1733(var2, var3, var1.field1950);
      }
   }

   private void lambda$onRender3D$4(BlockPos var1, Long var2) {
      if (mc.world.getBlockState(var1).isAir() || System.currentTimeMillis() - var2 > 1000L) {
         this.ab.remove(var1);
      }
   }

   private boolean lambda$new$3() {
      return !this.field2980.method419();
   }

   private boolean lambda$new$2() {
      return !this.field2980.method419() && this.field2976.method419();
   }

   private boolean lambda$new$1() {
      return this.field2981.method461() == NukerShader.Image;
   }

   private boolean lambda$new$0() {
      return this.field2984.method423() > 0.0F;
   }
}
