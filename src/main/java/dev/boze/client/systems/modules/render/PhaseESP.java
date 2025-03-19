package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.jumptable.nt;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.*;
import net.minecraft.util.math.Direction.Axis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PhaseESP extends Module {
   public static final PhaseESP INSTANCE = new PhaseESP();
   private final BooleanSetting field461 = new BooleanSetting("OnlyInHoles", false, "Only in holes");
   private final BooleanSetting field462 = new BooleanSetting("Corners", true, "Show corners");
   private final FloatSetting field463 = new FloatSetting("Width", 1.0F, 1.0F, 5.0F, 0.1F, "Width of the lines", PhaseESP::lambda$new$0);
   private final ColorSetting field464 = new ColorSetting("Safe", new BozeDrawColor(0, 255, 0, 255), "Color for safe blocks");
   private final ColorSetting field465 = new ColorSetting("Mineable", new BozeDrawColor(255, 255, 0, 255), "Color for mineable blocks");
   private final ColorSetting field466 = new ColorSetting("Unsafe", new BozeDrawColor(255, 0, 0, 255), "Color for unsafe blocks");
   private final FloatSetting field467 = new FloatSetting("Rectangle", 0.0F, 0.0F, 1.0F, 0.01F, "Height of the rectangles");
   private final FloatSetting field468 = new FloatSetting("Opacity", 0.25F, 0.05F, 1.0F, 0.05F, "Opacity of the rectangles", this::lambda$new$1);
   private Renderer3D field469 = null;

   public PhaseESP() {
      super("PhaseESP", "Pearl phase esp", Category.Render);
   }

   @EventHandler
   public void method2071(Render3DEvent event) {
      if (!this.field461.method419() || Class5924.method76(true)) {
         event.field1950.field2166.field1594 = this.field463.method423();
         Box var5 = mc.player.getBoundingBox().shrink(1.0E-4, 0.0, 1.0E-4);
         Set var6 = this.method250(var5);
         if (this.field469 == null) {
            this.field469 = new Renderer3D(false, false);
         }

         this.field469.method1217();

         for (BlockPos var8 : var6) {
            for (Direction var12 : Direction.values()) {
               if (var12.getAxis() != Axis.Y) {
                  BlockPos var13 = var8.offset(var12);
                  BlockState var14 = mc.world.getBlockState(var13);
                  if (var14.getBlock() == Blocks.BEDROCK || var14.getBlock() == Blocks.OBSIDIAN) {
                     BlockPos var15 = var13.down();
                     BlockState var16 = mc.world.getBlockState(var15);
                     if (var16.isAir() || var16.getBlock() == Blocks.LAVA || var16.getBlock() == Blocks.WATER) {
                        this.method248(this.field469, var13, var12, this.field466.method1362());
                     } else if (var16.getBlock() == Blocks.BEDROCK) {
                        if (var14.getBlock() == Blocks.BEDROCK) {
                           this.method248(this.field469, var13, var12, this.field464.method1362());
                        } else {
                           this.method248(this.field469, var13, var12, this.field465.method1362());
                        }
                     } else {
                        this.method248(this.field469, var13, var12, this.field465.method1362());
                     }
                  }
               }
            }
         }

         this.field469.method1219(event.matrix);
         if (this.field462.method419()) {
            Map var17 = Map.of(
               BlockPos.ofFloored(var5.minX - 1.0, var5.minY, var5.minZ - 1.0),
               new Vec3i(1, 0, 1),
               BlockPos.ofFloored(var5.minX - 1.0, var5.minY, var5.maxZ + 1.0),
               new Vec3i(1, 0, 0),
               BlockPos.ofFloored(var5.maxX + 1.0, var5.minY, var5.minZ - 1.0),
               new Vec3i(0, 0, 1),
               BlockPos.ofFloored(var5.maxX + 1.0, var5.minY, var5.maxZ + 1.0),
               new Vec3i(0, 0, 0)
            );
            var17.forEach(this::lambda$onRender3D$2);
         }

         event.field1950.field2166.field1594 = 1.0F;
      }
   }

   private void method247(Render3DEvent var1, BlockPos var2, Vec3i var3, BozeDrawColor var4) {
      Vec3d var8 = new Vec3d((double)(var2.getX() + var3.getX()), (double)var2.getY(), (double)(var2.getZ() + var3.getZ()));
      Vec3d var9 = new Vec3d((double)(var2.getX() + var3.getX()), (double)(var2.getY() + 1), (double)(var2.getZ() + var3.getZ()));
      if (this.field467.method423() > 0.0F) {
         Box var10 = new Box(var8, var9);
         var10 = var10.expand((double)this.field467.method423().floatValue() * 0.5, 0.0, (double)this.field467.method423().floatValue() * 0.5);
         BozeDrawColor var11 = var4.copy();
         var11.field411 = (int)(this.field468.method423() * 255.0F);
         var1.field1950.method1273(var10, var11, var4, ShapeMode.Full, 0);
      } else {
         var1.field1950.method1235(var8, var9, var4);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method248(Renderer3D var1, BlockPos var2, Direction var3, BozeDrawColor var4) {
      switch (nt.field2124[var3.getOpposite().ordinal()]) {
         case 1:
            Vec3d var12 = new Vec3d((double)var2.getX(), (double)var2.getY(), (double)var2.getZ());
            Vec3d var15 = new Vec3d((double)(var2.getX() + 1), (double)var2.getY(), (double)var2.getZ());
            this.method249(var1, var12, var15, var4);
            break;
         case 2:
            Vec3d var11 = new Vec3d((double)var2.getX(), (double)var2.getY(), (double)(var2.getZ() + 1));
            Vec3d var14 = new Vec3d((double)(var2.getX() + 1), (double)var2.getY(), (double)(var2.getZ() + 1));
            this.method249(var1, var11, var14, var4);
            break;
         case 3:
            Vec3d var10 = new Vec3d((double)(var2.getX() + 1), (double)var2.getY(), (double)var2.getZ());
            Vec3d var13 = new Vec3d((double)(var2.getX() + 1), (double)var2.getY(), (double)(var2.getZ() + 1));
            this.method249(var1, var10, var13, var4);
            break;
         case 4:
            Vec3d var8 = new Vec3d((double)var2.getX(), (double)var2.getY(), (double)var2.getZ());
            Vec3d var9 = new Vec3d((double)var2.getX(), (double)var2.getY(), (double)(var2.getZ() + 1));
            this.method249(var1, var8, var9, var4);
      }
   }

   private void method249(Renderer3D var1, Vec3d var2, Vec3d var3, BozeDrawColor var4) {
      if (this.field467.method423() == 0.0F) {
         var1.method1235(var2, var3, var4);
      } else {
         BozeDrawColor var8 = var4.copy();
         var8.field411 = (int)(this.field468.method423() * 255.0F);
         var1.method1258(
            var2.x,
            var2.y,
            var2.z,
            var3.x,
            var3.y,
            var3.z,
            var3.x,
            var3.y + (double)this.field467.method423().floatValue(),
            var3.z,
            var2.x,
            var2.y + (double)this.field467.method423().floatValue(),
            var2.z,
            var8,
            var4,
            ShapeMode.Full
         );
      }
   }

   private Set<BlockPos> method250(Box var1) {
      return new HashSet(
         Arrays.asList(
            BlockPos.ofFloored(var1.minX, var1.minY, var1.minZ),
            BlockPos.ofFloored(var1.minX, var1.minY, var1.maxZ),
            BlockPos.ofFloored(var1.maxX, var1.minY, var1.minZ),
            BlockPos.ofFloored(var1.maxX, var1.minY, var1.maxZ)
         )
      );
   }

   private void lambda$onRender3D$2(Render3DEvent var1, BlockPos var2, Vec3i var3) {
      BlockState var7 = mc.world.getBlockState(var2);
      if (var7.getBlock() == Blocks.BEDROCK || var7.getBlock() == Blocks.OBSIDIAN) {
         BlockPos var8 = var2.down();
         BlockState var9 = mc.world.getBlockState(var8);
         if (var9.isAir() || var9.getBlock() == Blocks.LAVA || var9.getBlock() == Blocks.WATER) {
            this.method247(var1, var2, var3, this.field466.method1362());
         } else if (var9.getBlock() == Blocks.BEDROCK) {
            if (var7.getBlock() == Blocks.BEDROCK) {
               this.method247(var1, var2, var3, this.field464.method1362());
            } else {
               this.method247(var1, var2, var3, this.field465.method1362());
            }
         } else {
            this.method247(var1, var2, var3, this.field465.method1362());
         }
      }
   }

   private boolean lambda$new$1() {
      return this.field467.method423() > 0.0F;
   }

   private static boolean lambda$new$0() {
      return !MinecraftClient.IS_SYSTEM_MAC;
   }
}
