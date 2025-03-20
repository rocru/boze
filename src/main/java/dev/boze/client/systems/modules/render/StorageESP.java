package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ESPMode;
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
import mapped.Class3083;
import meteordevelopment.orbit.EventHandler;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class StorageESP extends Module {
   public static final StorageESP INSTANCE = new StorageESP();
   public final EnumSetting<ESPMode> field3699 = new EnumSetting<ESPMode>("Mode", ESPMode.Shader, "Mode for drawing ESP");
   public final EnumSetting<ShaderMode> field3700 = new EnumSetting<ShaderMode>("Shader", ShaderMode.Colored, "Shader to use", this::lambda$new$0);
   public final BooleanSetting field3701 = new BooleanSetting("FastRender", false, "Make the shader render faster at the cost of quality", this.field3700);
   public final IntSetting field3702 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3700);
   public final FloatSetting field3703 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3700);
   public final FloatSetting field3704 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$1, this.field3700);
   private final IntSetting field3705 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3700);
   private final FloatSetting field3706 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3700);
   public final StringSetting field3707 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$2, this.field3700);
   public final ColorSetting field3708 = new ColorSetting("Fill", new BozeDrawColor(1681640397), "Color for shader fill", this::lambda$new$3, this.field3700);
   public final ColorSetting field3709 = new ColorSetting(
      "Outline", new BozeDrawColor(-12858419), "Color for shader outline", this::lambda$new$4, this.field3700
   );
   private final BooleanSetting field3710 = new BooleanSetting("Tracers", false, "Draw tracers");
   private final BooleanSetting field3711 = new BooleanSetting("Fade", false, "Fade box opacity");
   private final FloatSetting field3712 = new FloatSetting("MinDist", 8.0F, 0.0F, 20.0F, 0.5F, "Min distance for fading", this.field3711);
   private final FloatSetting field3713 = new FloatSetting("Factor", 0.05F, 0.01F, 0.5F, 0.01F, "Factor for fading", this.field3711);
   private BooleanSetting field3714 = new BooleanSetting("Chests", true, "Apply ESP to Chests");
   private RGBASetting field3715 = new RGBASetting("Color", new RGBAColor(1090511181), "Color for chest boxes", this::lambda$new$5, this.field3714);
   private RGBASetting field3716 = new RGBASetting("Outline", new RGBAColor(-7859), "Color for chest box outline", this.field3714);
   private BooleanSetting field3717 = new BooleanSetting("TrappedChests", true, "Apply ESP to Trapped Chests");
   private RGBASetting field3718 = new RGBASetting("Color", new RGBAColor(1090503757), "Color for trapped chest boxes", this::lambda$new$6, this.field3717);
   private RGBASetting field3719 = new RGBASetting("Outline", new RGBAColor(-15283), "Color for trapped chest box outline", this.field3717);
   private BooleanSetting field3720 = new BooleanSetting("Barrels", true, "Apply ESP to Barrels");
   private RGBASetting field3721 = new RGBASetting("Color", new RGBAColor(1090507341), "Color for barrels", this::lambda$new$7, this.field3720);
   private RGBASetting field3722 = new RGBASetting("Outline", new RGBAColor(-11699), "Color for barrel outline", this.field3720);
   private BooleanSetting field3723 = new BooleanSetting("EnderChests", true, "Apply ESP to Ender Chests");
   private RGBASetting field3724 = new RGBASetting("Color", new RGBAColor(1078853512), "Color for ender chest boxes", this::lambda$new$8, this.field3723);
   private RGBASetting field3725 = new RGBASetting("Outline", new RGBAColor(-11665528), "Color for ender chest box outline", this.field3723);
   private BooleanSetting field3726 = new BooleanSetting("Shulkers", true, "Apply ESP to Shulkers");
   private BooleanSetting field3727 = new BooleanSetting("Dynamic", true, "Use vanilla shulker colors", this.field3726);
   private RGBASetting aa = new RGBASetting("Color", new RGBAColor(1088507391), "Color for shulker boxes", this::lambda$new$9, this.field3726);
   private RGBASetting ab = new RGBASetting("Outline", new RGBAColor(-2011649), "Color for shulker outline", this::lambda$new$10, this.field3726);
   private BooleanSetting ac = new BooleanSetting("Other", false, "Apply ESP to other containers");
   private RGBASetting ad = new RGBASetting("Color", new RGBAColor(1087635277), "Color for container boxes", this::lambda$new$11, this.ac);
   private RGBASetting ae = new RGBASetting("Outline", new RGBAColor(-2883763), "Color for container outline", this.ac);
   private int af;
   private Renderer3D ag = null;
   public ByteTexture ah;
   public String ai = "";

   public StorageESP() {
      super("StorageESP", "Draws boxes around containers", Category.Render);
   }

   @EventHandler
   private void method2030(Render3DEvent var1) {
      this.af = 0;
      if (this.field3699.getValue() == ESPMode.Shader) {
         if (this.ag == null) {
            this.ag = new Renderer3D(false, true);
         }

         this.ag.method1217();
      }

      for (BlockEntity var6 : this.method2032()) {
         if (this.method2035(var6)) {
            double var7 = this.method2034(var6);
            RGBAColor var9 = this.method2036(var6).copy();
            RGBAColor var10 = this.method2037(var6).copy();
            var9.field411 = (int)((double)var9.field411 * var7);
            var10.field411 = (int)((double)var10.field411 * var7);
            if (this.field3710.getValue()) {
               var1.field1950
                  .method1236(
                     RotationHelper.field3956.x,
                     RotationHelper.field3956.y,
                     RotationHelper.field3956.z,
                     (double)var6.getPos().getX() + 0.5,
                     (double)var6.getPos().getY() + 0.5,
                     (double)var6.getPos().getZ() + 0.5,
                     var10
                  );
            }

            if (this.field3699.getValue() == ESPMode.Simple) {
               this.method2038(var1.field1950, var6, var9, var10);
            } else if (this.field3699.getValue() == ESPMode.Shader) {
               this.method2038(this.ag, var6, var9, var10);
            }

            this.af++;
         }
      }

      if (this.field3699.getValue() == ESPMode.Shader) {
         ChamsShaderRenderer.method1310(
            this::lambda$onRender$12,
            this.method2031(),
            this.field3701.getValue(),
            this.field3708,
            this.field3709,
            this.field3705.getValue(),
            this.field3706.getValue(),
            this.field3703.getValue(),
            this.field3704.getValue(),
            this.field3702.getValue(),
            this.ah
         );
      }
   }

   private ShaderMode method2031() {
      if (this.field3700.getValue() == ShaderMode.Image) {
         if (!this.field3707.getValue().isEmpty() && (!this.field3707.getValue().equals(this.ai) || this.ah == null)) {
            File var4 = new File(ConfigManager.images, this.field3707.getValue() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.ah = ByteTexturePacker.method493(var5);
               if (this.ah != null) {
                  this.ai = this.field3707.getValue();
               } else {
                  this.ai = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field3707.setValue("");
               this.ai = "";
            }
         }

         if (this.ah != null) {
            return ShaderMode.Image;
         }
      }

      return this.field3700.getValue() == ShaderMode.Rainbow ? ShaderMode.Rainbow : ShaderMode.Colored;
   }

   private List<BlockEntity> method2032() {
      ArrayList var4 = new ArrayList();

      for (WorldChunk var6 : this.method2033()) {
         var4.addAll(var6.getBlockEntities().values());
      }

      return var4;
   }

   private List<WorldChunk> method2033() {
      ArrayList var4 = new ArrayList();
      int var5 = (Integer)mc.options.getViewDistance().getValue();

      for (int var6 = -var5; var6 <= var5; var6++) {
         for (int var7 = -var5; var7 <= var5; var7++) {
            WorldChunk var8 = mc.world.getChunkManager().getWorldChunk((int)mc.player.getX() / 16 + var6, (int)mc.player.getZ() / 16 + var7);
            if (var8 != null) {
               var4.add(var8);
            }
         }
      }

      return var4;
   }

   private double method2034(BlockEntity var1) {
      if (!this.field3711.getValue()) {
         return 1.0;
      } else {
         double var5 = new Vec3d((double)var1.getPos().getX() + 0.5, (double)var1.getPos().getY() + 0.5, (double)var1.getPos().getZ() + 0.5)
            .distanceTo(mc.player.getEyePos());
         if (var5 <= (double)this.field3712.getValue().floatValue()) {
            return 1.0;
         } else {
            var5 -= (double)this.field3712.getValue().floatValue();
            return 1.0 - MathHelper.clamp(var5 / (1.0 / (double)this.field3713.getValue().floatValue()), 0.0, 1.0);
         }
      }
   }

   private boolean method2035(BlockEntity var1) {
      if (var1 instanceof TrappedChestBlockEntity) {
         return this.field3717.getValue();
      } else if (var1 instanceof EnderChestBlockEntity) {
         return this.field3723.getValue();
      } else if (var1 instanceof ChestBlockEntity) {
         return this.field3714.getValue();
      } else if (var1 instanceof BarrelBlockEntity) {
         return this.field3720.getValue();
      } else if (var1 instanceof ShulkerBoxBlockEntity) {
         return this.field3726.getValue();
      } else {
         return !(var1 instanceof AbstractFurnaceBlockEntity) && !(var1 instanceof DispenserBlockEntity) && !(var1 instanceof HopperBlockEntity)
            ? false
            : this.ac.getValue();
      }
   }

   private RGBAColor method2036(BlockEntity var1) {
      if (var1 instanceof TrappedChestBlockEntity) {
         return this.field3718.getValue();
      } else if (var1 instanceof EnderChestBlockEntity) {
         return this.field3724.getValue();
      } else if (var1 instanceof ChestBlockEntity) {
         return this.field3715.getValue();
      } else if (var1 instanceof BarrelBlockEntity) {
         return this.field3721.getValue();
      } else if (var1 instanceof ShulkerBoxBlockEntity var5) {
         return this.field3727.getValue() && var5.getColor() != null
            ? new RGBAColor(ColorARGB.withAlpha(var5.getColor().getEntityColor(), 64))
            : this.aa.getValue();
      } else {
         return this.ad.getValue();
      }
   }

   private RGBAColor method2037(BlockEntity var1) {
      if (var1 instanceof TrappedChestBlockEntity) {
         return this.field3719.getValue();
      } else if (var1 instanceof EnderChestBlockEntity) {
         return this.field3725.getValue();
      } else if (var1 instanceof ChestBlockEntity) {
         return this.field3716.getValue();
      } else if (var1 instanceof BarrelBlockEntity) {
         return this.field3722.getValue();
      } else if (var1 instanceof ShulkerBoxBlockEntity var5) {
         return this.field3727.getValue() && var5.getColor() != null
            ? new RGBAColor(ColorARGB.withAlpha(var5.getColor().getEntityColor(), 255))
            : this.ab.getValue();
      } else {
         return this.ae.getValue();
      }
   }

   private void method2038(Renderer3D var1, BlockEntity var2, RGBAColor var3, RGBAColor var4) {
      double var8 = (double)var2.getPos().getX();
      double var10 = (double)var2.getPos().getY();
      double var12 = (double)var2.getPos().getZ();
      double var14 = (double)(var2.getPos().getX() + 1);
      double var16 = (double)(var2.getPos().getY() + 1);
      double var18 = (double)(var2.getPos().getZ() + 1);
      byte var20 = 0;
      if (var2 instanceof ChestBlockEntity) {
         BlockState var21 = mc.world.getBlockState(var2.getPos());
         if ((var21.getBlock() == Blocks.CHEST || var21.getBlock() == Blocks.TRAPPED_CHEST) && var21.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE) {
            var20 = Class3083.method6049(ChestBlock.getFacing(var21));
         }
      }

      if (var2 instanceof ChestBlockEntity || var2 instanceof EnderChestBlockEntity) {
         double var23 = 0.0625;
         if (Class3083.method6051(var20, (byte)32)) {
            var8 += var23;
         }

         if (Class3083.method6051(var20, (byte)8)) {
            var12 += var23;
         }

         if (Class3083.method6051(var20, (byte)64)) {
            var14 -= var23;
         }

         var16 -= var23 * 2.0;
         if (Class3083.method6051(var20, (byte)16)) {
            var18 -= var23;
         }
      }

      var1.method1264(var8, var10, var12, var14, var16, var18, var3, var4, ShapeMode.Full, var20);
   }

   @Override
   public String method1322() {
      return Integer.toString(this.af);
   }

   private void lambda$onRender$12(Render3DEvent var1) {
      this.ag.method1219(var1.matrix);
   }

   private boolean lambda$new$11() {
      return this.field3699.getValue() != ESPMode.Shader;
   }

   private boolean lambda$new$10() {
      return !this.field3727.getValue();
   }

   private boolean lambda$new$9() {
      return this.field3699.getValue() != ESPMode.Shader && !this.field3727.getValue();
   }

   private boolean lambda$new$8() {
      return this.field3699.getValue() != ESPMode.Shader;
   }

   private boolean lambda$new$7() {
      return this.field3699.getValue() != ESPMode.Shader;
   }

   private boolean lambda$new$6() {
      return this.field3699.getValue() != ESPMode.Shader;
   }

   private boolean lambda$new$5() {
      return this.field3699.getValue() != ESPMode.Shader;
   }

   private boolean lambda$new$4() {
      return this.field3700.getValue() != ShaderMode.Colored;
   }

   private boolean lambda$new$3() {
      return this.field3700.getValue() == ShaderMode.Rainbow;
   }

   private boolean lambda$new$2() {
      return this.field3700.getValue() == ShaderMode.Image;
   }

   private boolean lambda$new$1() {
      return this.field3703.getValue() > 0.0F;
   }

   private boolean lambda$new$0() {
      return this.field3699.getValue() == ESPMode.Shader;
   }
}
