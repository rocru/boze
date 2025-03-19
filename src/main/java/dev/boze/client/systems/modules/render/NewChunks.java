package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.NewChunksScan;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewChunks extends Module {
   public static final NewChunks INSTANCE = new NewChunks();
   private final EnumSetting<NewChunksScan> field3597 = new EnumSetting<NewChunksScan>("Scan", NewChunksScan.New, "Mode for scanning chunks");
   public final BooleanSetting field3598 = new BooleanSetting("OreMethod", false, "Use ore method to detect new 1.17+ chunks - assumes all are new");
   public final ColorSetting field3599 = new ColorSetting("NewFill", new BozeDrawColor(1694448443), "Color for new chunk fill");
   public final ColorSetting field3600 = new ColorSetting("NewOutline", new BozeDrawColor(-50373), "Color for new chunk outline");
   public final ColorSetting field3601 = new ColorSetting("OldFill", new BozeDrawColor(1681603583), "Color for old chunk fill");
   public final ColorSetting field3602 = new ColorSetting("OldOutline", new BozeDrawColor(-12895233), "Color for old chunk outline");
   private final BooleanSetting field3603 = new BooleanSetting("DisregardNegative", false, "Disregard negative (<y0) blocks");
   private ExecutorService field3604;
   private final Set<ChunkPos> field3605 = Collections.synchronizedSet(new HashSet());
   private final Set<ChunkPos> field3606 = Collections.synchronizedSet(new HashSet());

   public NewChunks() {
      super("NewChunks", "Guesses where new/old chunks are", Category.Render);
   }

   @Override
   public void onEnable() {
      this.field3604 = Executors.newFixedThreadPool(1);
   }

   @Override
   public void onDisable() {
      if (this.field3604 != null) {
         this.field3604.shutdownNow();
      }

      this.field3604 = null;
      this.field3605.clear();
      this.field3606.clear();
   }

   @EventHandler
   private void method1965(Render3DEvent var1) {
      if (this.field3597.method461() != NewChunksScan.Old) {
         ArrayList var5 = new ArrayList();
         synchronized (this.field3605) {
            for (ChunkPos var8 : this.field3605) {
               if (mc.getCameraEntity().getBlockPos().isWithinDistance(var8.getStartPos(), 1024.0)) {
                  Box var9 = new Box(
                     (double)var8.getStartX(),
                     Math.max((double)mc.world.getBottomY(), mc.player.getY() - 100.0),
                     (double)var8.getStartZ(),
                     (double)(var8.getStartX() + 16),
                     Math.max((double)mc.world.getBottomY(), mc.player.getY() - 100.0),
                     (double)(var8.getStartZ() + 16)
                  );
                  var5.add(var9);
               }
            }
         }

         for (Box var18 : var5) {
            var1.field1950.method1273(var18, this.field3599.method1362(), this.field3600.method1362(), ShapeMode.Full, 0);
         }
      }

      if (this.field3597.method461() != NewChunksScan.New) {
         ArrayList var14 = new ArrayList();
         synchronized (this.field3606) {
            for (ChunkPos var21 : this.field3606) {
               if (mc.getCameraEntity().getBlockPos().isWithinDistance(var21.getStartPos(), 1024.0)) {
                  Box var22 = new Box(
                     (double)var21.getStartX(),
                     Math.max((double)mc.world.getBottomY(), mc.player.getY() - 100.0),
                     (double)var21.getStartZ(),
                     (double)(var21.getStartX() + 16),
                     Math.max((double)mc.world.getBottomY(), mc.player.getY() - 100.0),
                     (double)(var21.getStartZ() + 16)
                  );
                  var14.add(var22);
               }
            }
         }

         for (Box var20 : var14) {
            var1.field1950.method1273(var20, this.field3601.method1362(), this.field3602.method1362(), ShapeMode.Full, 0);
         }
      }
   }

   @EventHandler
   private void method1966(GameJoinEvent var1) {
      this.field3605.clear();
      this.field3606.clear();
   }

   public void method1967(BlockPos pos, BlockState state, boolean onlyOre) {
      if (!this.field3603.method419() || pos.getY() >= 0) {
         if (this.field3598.method419() && state.getBlock() == Blocks.COPPER_ORE
            || state.getBlock() == Blocks.DEEPSLATE_COPPER_ORE
            || state.getBlock() == Blocks.NETHER_GOLD_ORE
            || state.getBlock() == Blocks.ANCIENT_DEBRIS) {
            ChunkPos var7 = new ChunkPos(pos);
            if (!this.field3605.contains(var7)) {
               this.field3605.add(var7);
               this.field3606.remove(var7);
               return;
            }
         }

         if (!onlyOre) {
            if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill()) {
               ChunkPos var12 = new ChunkPos(pos);

               for (Direction var11 : Direction.values()) {
                  if (var11 != Direction.DOWN && mc.world.getBlockState(pos.offset(var11)).getFluidState().isStill() && !this.field3606.contains(var12)) {
                     this.field3605.add(var12);
                     return;
                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void method1968(PacketBundleEvent var1) {
      if (var1.packet instanceof ChunkDataS2CPacket && mc.world != null) {
         ChunkDataS2CPacket var10 = (ChunkDataS2CPacket)var1.packet;
         ChunkPos var11 = new ChunkPos(var10.getChunkX(), var10.getChunkZ());
         WorldChunk var12 = new WorldChunk(mc.world, var11);
         var12.loadFromPacket(
            var10.getChunkData().getSectionsDataBuf(), new NbtCompound(), var10.getChunkData().getBlockEntities(var10.getChunkX(), var10.getChunkZ())
         );
         if (this.field3604 == null) {
            this.field3604 = Executors.newFixedThreadPool(1);
         }

         this.field3604.execute(this::lambda$onPacketReceive$0);
      } else if (var1.packet instanceof BlockUpdateS2CPacket var5) {
         if (this.field3604 == null) {
            this.field3604 = Executors.newFixedThreadPool(1);
         }

         this.field3604.execute(this::lambda$onPacketReceive$1);
      } else if (var1.packet instanceof ExplosionS2CPacket var8) {
         if (this.field3604 == null) {
            this.field3604 = Executors.newFixedThreadPool(1);
         }

         for (BlockPos var7 : var8.getAffectedBlocks()) {
            this.field3604.execute(this::lambda$onPacketReceive$2);
         }
      } else if (var1.packet instanceof ChunkDeltaUpdateS2CPacket var9) {
         if (this.field3604 == null) {
            this.field3604 = Executors.newFixedThreadPool(1);
         }

         var9.visitUpdates(this::lambda$onPacketReceive$4);
      }
   }

   private void lambda$onPacketReceive$4(BlockPos var1, BlockState var2) {
      BlockPos var3 = var1.toImmutable();
      this.field3604.execute(this::lambda$onPacketReceive$3);
   }

   private void lambda$onPacketReceive$3(BlockPos var1, BlockState var2) {
      this.method1967(var1, var2, false);
   }

   private void lambda$onPacketReceive$2(BlockPos var1) {
      this.method1967(var1, Blocks.AIR.getDefaultState(), false);
   }

   private void lambda$onPacketReceive$1(BlockUpdateS2CPacket var1) {
      this.method1967(var1.getPos(), var1.getState(), false);
   }

   private void lambda$onPacketReceive$0(ChunkPos var1, WorldChunk var2) {
      if (!this.field3605.contains(var1) && mc.world.getChunkManager().getChunk(var1.x, var1.z) == null) {
         for (int var6 = 0; var6 < 16; var6++) {
            for (int var7 = this.field3603.method419() ? 0 : mc.world.getBottomY(); var7 < mc.world.getTopY(); var7++) {
               for (int var8 = 0; var8 < 16; var8++) {
                  FluidState var9 = var2.getFluidState(var6, var7, var8);
                  if (!var9.isEmpty() && !var9.isStill()) {
                     this.field3606.add(var1);
                     return;
                  }
               }
            }
         }
      }
   }
}
