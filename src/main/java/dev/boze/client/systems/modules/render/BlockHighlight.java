package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.BlockHighlightMode;
import dev.boze.client.enums.BlockHighlightShader;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.jumptable.nn;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.misc.AirPlace;
import dev.boze.client.utils.render.ByteTexture;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlockHighlight extends Module {
   public static final BlockHighlight INSTANCE = new BlockHighlight();
   private final EnumSetting<BlockHighlightMode> field3387 = new EnumSetting<BlockHighlightMode>(
      "Mode", BlockHighlightMode.Normal, "Mode for drawing highlight"
   );
   public final ColorSetting field3388 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill");
   public final ColorSetting field3389 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline");
   private final BooleanSetting field3390 = new BooleanSetting("Shader", false, "Use a shader");
   public final EnumSetting<BlockHighlightShader> field3391 = new EnumSetting<BlockHighlightShader>(
      "Shader", BlockHighlightShader.Normal, "Shader to use", this.field3390
   );
   public final BooleanSetting field3392 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3390);
   public final IntSetting field3393 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3390);
   public final FloatSetting field3394 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3390);
   public final FloatSetting field3395 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3390);
   private final IntSetting field3396 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3390);
   private final FloatSetting field3397 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3390);
   public final StringSetting field3398 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3390);
   private final BooleanSetting field3399 = new BooleanSetting("Fade", true, "Fade block highlights", this::lambda$new$2);
   private final BooleanSetting field3400 = new BooleanSetting("OnlyLast", false, "Only fade last block", this::lambda$new$3, this.field3399);
   private final IntSetting field3401 = new IntSetting(
      "Ticks", 2, 1, 20, 1, "Amount of ticks to render blocks for after looking away", this::lambda$new$4, this.field3399
   );
   private ConcurrentHashMap<Box, Long> field3402 = new ConcurrentHashMap();
   private CopyOnWriteArrayList<Box> field3403 = new CopyOnWriteArrayList();
   private Renderer3D field3404 = null;
   private ByteTexture field3405;
   private String field3406 = "";

   public BlockHighlight() {
      super("BlockHighlight", "Highlights the block you're currently looking at", Category.Render);
   }

   @EventHandler
   public void method1899(Render3DEvent event) {
      if (mc.crosshairTarget != null && mc.crosshairTarget instanceof BlockHitResult var5) {
         BlockPos var13 = var5.getBlockPos();
         Direction var7 = var5.getSide();
         BlockState var8 = mc.world.getBlockState(var13);
         VoxelShape var9 = var8.getOutlineShape(mc.world, var13);
         Box var10 = null;
         if (var9.isEmpty()) {
            if (!(var8.getBlock() instanceof AirBlock) || !AirPlace.INSTANCE.isEnabled()) {
               if (this.field3399.getValue() && !this.field3390.getValue()) {
                  this.field3402.forEach(this::lambda$onRender3D$6);
               }

               return;
            }

            var10 = new Box(var13);
         }

         if (this.field3390.getValue()) {
            if (this.field3404 == null) {
               this.field3404 = new Renderer3D(false, true);
            }

            this.field3404.method1217();
         }

         this.field3403.clear();
         if (var9.isEmpty() && var10 != null) {
            this.method1903(
               var10, this.field3387.getValue() == BlockHighlightMode.Flat ? var7 : null, this.field3390.getValue() ? this.field3404 : event.field1950
            );
         } else if (this.field3387.getValue() == BlockHighlightMode.Complex) {
            for (Box var12 : var9.getBoundingBoxes()) {
               this.method1903(var12.offset(var13), null, this.field3390.getValue() ? this.field3404 : event.field1950);
            }
         } else {
            Box var14 = var9.getBoundingBox();
            this.method1903(
               var14.offset(var13),
               this.field3387.getValue() == BlockHighlightMode.Flat ? var7 : null,
               this.field3390.getValue() ? this.field3404 : event.field1950
            );
         }

         if (this.field3399.getValue() && !this.field3390.getValue() && !this.field3400.getValue()) {
            this.field3402.forEach(this::lambda$onRender3D$7);
         }

         if (this.field3390.getValue()) {
            ChamsShaderRenderer.method1310(
               this::lambda$onRender3D$8,
               this.method1900(),
               this.field3392.getValue(),
               this.field3388,
               this.field3389,
               this.field3396.method434(),
               this.field3397.getValue(),
               this.field3394.getValue(),
               this.field3395.getValue(),
               this.field3393.method434(),
               this.field3405
            );
         }
      } else {
         if (this.field3399.getValue() && !this.field3390.getValue()) {
            this.field3402.forEach(this::lambda$onRender3D$5);
         }
      }
   }

   private ShaderMode method1900() {
      if (this.field3391.getValue() == BlockHighlightShader.Image) {
         if (!this.field3398.getValue().isEmpty() && (!this.field3398.getValue().equals(this.field3406) || this.field3405 == null)) {
            File var4 = new File(ConfigManager.images, this.field3398.getValue() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.field3405 = ByteTexturePacker.method493(var5);
               if (this.field3405 != null) {
                  this.field3406 = this.field3398.getValue();
               } else {
                  this.field3406 = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field3398.setValue("");
               this.field3406 = "";
            }
         }

         if (this.field3405 != null) {
            return ShaderMode.Image;
         }
      }

      return ShaderMode.Rainbow;
   }

   private void method1901(Box var1, long var2, Renderer3D var4) {
      float var7 = MathHelper.clamp(1.0F - (float)(System.currentTimeMillis() - var2) / (float)this.method1902(), 0.0F, 1.0F);
      BozeDrawColor var8 = (BozeDrawColor)this.field3388.getValue().copy().method197(var7);
      BozeDrawColor var9 = (BozeDrawColor)this.field3389.getValue().copy().method197(var7);
      var4.method1273(var1, var8, var9, ShapeMode.Full, 0);
   }

   private int method1902() {
      return this.field3401.method434() * 50;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method1903(Box var1, Direction var2, Renderer3D var3) {
      if (var2 != null) {
         switch (nn.field2121[var2.ordinal()]) {
            case 1:
               var1 = new Box(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.minY, var1.maxZ);
               break;
            case 2:
               var1 = new Box(var1.minX, var1.maxY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
               break;
            case 3:
               var1 = new Box(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.minZ);
               break;
            case 4:
               var1 = new Box(var1.minX, var1.minY, var1.maxZ, var1.maxX, var1.maxY, var1.maxZ);
               break;
            case 5:
               var1 = new Box(var1.maxX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
               break;
            case 6:
               var1 = new Box(var1.minX, var1.minY, var1.minZ, var1.minX, var1.maxY, var1.maxZ);
         }
      }

      if (this.field3399.getValue()) {
         this.field3402.put(var1, System.currentTimeMillis());
         this.field3403.add(var1);
      }

      if (this.field3390.getValue()) {
         var3.method1268(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ, this.field3389.getValue(), 0);
      } else {
         var3.method1273(var1, this.field3388.getValue(), this.field3389.getValue(), ShapeMode.Full, 0);
      }
   }

   private void lambda$onRender3D$8(Render3DEvent var1) {
      this.field3404.method1219(var1.matrix);
   }

   private void lambda$onRender3D$7(Render3DEvent var1, Box var2, Long var3) {
      if (System.currentTimeMillis() - var3 > (long)this.method1902()) {
         this.field3402.remove(var2);
      } else if (!this.field3403.contains(var2)) {
         this.method1901(var2, var3, var1.field1950);
      }
   }

   private void lambda$onRender3D$6(Render3DEvent var1, Box var2, Long var3) {
      if (System.currentTimeMillis() - var3 > (long)this.method1902()) {
         this.field3402.remove(var2);
      } else if (!this.field3400.getValue() || this.field3403.contains(var2)) {
         this.method1901(var2, var3, var1.field1950);
      }
   }

   private void lambda$onRender3D$5(Render3DEvent var1, Box var2, Long var3) {
      if (System.currentTimeMillis() - var3 > (long)this.method1902()) {
         this.field3402.remove(var2);
      } else if (!this.field3400.getValue() || this.field3403.contains(var2)) {
         this.method1901(var2, var3, var1.field1950);
      }
   }

   private boolean lambda$new$4() {
      return !this.field3390.getValue();
   }

   private boolean lambda$new$3() {
      return !this.field3390.getValue();
   }

   private boolean lambda$new$2() {
      return !this.field3390.getValue();
   }

   private boolean lambda$new$1() {
      return this.field3391.getValue() == BlockHighlightShader.Image;
   }

   private boolean lambda$new$0() {
      return this.field3394.getValue() > 0.0F;
   }
}
