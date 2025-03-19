package dev.boze.client.systems.modules.movement;

import dev.boze.client.Boze;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.VelocityCancel;
import dev.boze.client.events.*;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class Velocity extends Module {
   public static final Velocity INSTANCE = new Velocity();
   public final EnumSetting<AnticheatMode> field3366 = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Velocity mode");
   public final EnumSetting<VelocityCancel> field3367 = new EnumSetting<VelocityCancel>("Knockback", VelocityCancel.Always, "Cancel velocity from hits");
   private final EnumSetting<VelocityCancel> field3368 = new EnumSetting<VelocityCancel>(
      "Entities", VelocityCancel.Always, "Cancel velocity from entity collision"
   );
   public final EnumSetting<VelocityCancel> field3369 = new EnumSetting<VelocityCancel>("Explosions", VelocityCancel.Always, "Cancel velocity from explosions");
   public final BooleanSetting field3370 = new BooleanSetting("BlockStrict", false, "2b2t in-block Velocity bypass\n", this::lambda$new$0);
   private final BooleanSetting field3371 = new BooleanSetting(
      "ModulePriority", false, "Give other modules priority over velocity\nWill reduce velocity's consistency\n", this::lambda$new$1
   );
   private final BooleanSetting field3372 = new BooleanSetting("Blocks", true, "Cancel velocity from being pushed out of blocks");
   private final BooleanSetting field3373 = new BooleanSetting("Pistons", false, "No piston push");
   private final BooleanSetting field3374 = new BooleanSetting("InWater", true, "Cancel velocity in water");
   private final BooleanSetting field3375 = new BooleanSetting("InLava", true, "Cancel velocity in lava");
   private Timer field3376 = new Timer();
   private int field3377;
   public boolean field3378 = false;
   private int field3379;

   public Velocity() {
      super("Velocity", "Cancels unwanted velocity", Category.Movement);
   }

   @Override
   public void onEnable() {
      this.field3377 = Boze.lastTeleportId;
   }

   @Override
   public String method1322() {
      return this.field3366.method461().toString();
   }

   @EventHandler
   public void method1884(PreTickEvent event) {
      if (this.field3366.method461() == AnticheatMode.Grim && this.field3378 && MinecraftUtils.isClientActive()) {
         this.field3378 = false;
         if (this.field3379 <= 0) {
            Box var5 = mc.player.getBoundingBox();
            BlockPos var6 = BlockPos.ofFloored(var5.getCenter().getX(), var5.minY, var5.getCenter().getZ());
            mc.player
               .networkHandler
               .getConnection()
               .send(
                  new Full(
                     mc.player.getX(),
                     mc.player.getY(),
                     mc.player.getZ(),
                     ((ClientPlayerEntityAccessor)mc.player).getLastYaw(),
                     this.field3370.method419() ? 89.0F : ((ClientPlayerEntityAccessor)mc.player).getLastPitch(),
                     mc.player.isOnGround()
                  ),
                  null
               );
            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, var6, Direction.DOWN, 0));
         }
      }
   }

   @EventHandler(
      priority = 10000
   )
   public void method1885(ACRotationEvent event) {
      if (!this.field3371.method419()) {
         this.method1887(event);
      }
   }

   @EventHandler(
      priority = 21
   )
   public void method1886(ACRotationEvent event) {
      if (this.field3371.method419()) {
         this.method1887(event);
      }
   }

   private void method1887(ACRotationEvent var1) {
      if (var1.method1017() == AnticheatMode.Grim && this.field3366.method461() == AnticheatMode.Grim && this.field3370.method419() && this.method1895()) {
         var1.yaw = mc.player.getYaw();
         var1.pitch = 89.0F;
         var1.method1020();
      }
   }

   @EventHandler
   public void method1888(MovementEvent event) {
      if (!this.field3376.hasElapsed(1000.0)) {
         event.field1934 = true;
         mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround()));
         mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 69.0, mc.player.getZ(), mc.player.isOnGround()));
         this.field3377++;
         this.field3377 = Math.max(this.field3377, Boze.lastTeleportId);
         mc.player.networkHandler.sendPacket(new TeleportConfirmC2SPacket(this.field3377));
      }
   }

   @EventHandler
   public void method1889(GameJoinEvent event) {
      this.field3377 = 0;
   }

   @EventHandler
   public void method1890(PlayerVelocityEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (mc.player.age >= 5) {
            if (this.field3372.method419() && this.method1892()) {
               if (this.field3366.method461() == AnticheatMode.Grim && event.field1898) {
                  return;
               }

               event.method1021(true);
            }
         }
      }
   }

   @EventHandler
   public void method1891(PlayerPushEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (mc.player.age >= 5) {
            if (this.field3368.method461().method2114()) {
               event.method1020();
            }
         }
      }
   }

   public boolean method1892() {
      if (!MinecraftUtils.isClientActive()) {
         return false;
      } else if (mc.player.isTouchingWater() || mc.player.isSubmergedInWater()) {
         return this.field3374.method419();
      } else {
         return mc.player.isInLava() ? this.field3375.method419() : true;
      }
   }

   @EventHandler
   public void method1893(PlayerMoveEvent event) {
      if (event.movementType == MovementType.PISTON && this.field3373.method419() && this.method1892()) {
         if (mc.world.getBlockState(BlockPos.ofFloored(mc.player.getPos()).down()).getBlock() == Blocks.SLIME_BLOCK) {
            return;
         }

         this.field3376.reset();
         event.method1020();
      } else if (!this.field3376.hasElapsed(1000.0)) {
         event.method1020();
      }
   }

   @EventHandler
   public void method1894(PacketBundleEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (mc.player.age >= 5) {
            if (this.field3379 > 0) {
               this.field3379--;
            } else {
               if (event.packet instanceof EntityVelocityUpdateS2CPacket
                  && ((EntityVelocityUpdateS2CPacket)event.packet).getEntityId() == mc.player.getId()
                  && this.field3367.method461().method2114()) {
                  this.field3378 = true;
                  event.method1020();
               } else if (event.packet instanceof PlayerPositionLookS2CPacket && MinecraftUtils.isClientActive()) {
                  if (this.field3366.method461() == AnticheatMode.Grim) {
                     this.field3379 = 5;
                  }

                  if (!this.field3376.hasElapsed(1500.0)) {
                     PlayerPositionLookS2CPacket var5 = (PlayerPositionLookS2CPacket)event.packet;
                     ((PlayerPositionLookS2CPacketAccessor)var5).setYaw(mc.player.getYaw());
                     ((PlayerPositionLookS2CPacketAccessor)var5).setPitch(mc.player.getPitch());
                     var5.getFlags().remove(PositionFlag.X_ROT);
                     var5.getFlags().remove(PositionFlag.Y_ROT);
                  }
               }
            }
         }
      }
   }

   public boolean method1895() {
      if (this.field3366.method461() == AnticheatMode.Grim && this.field3370.method419()) {
         if (mc.world.isSpaceEmpty(mc.player.getBoundingBox())) {
            return false;
         } else {
            for (BlockPos var6 : Class5924.method348(mc.player.getPos())) {
               if (mc.world.getBlockState(var6).getBlock() == Blocks.AIR) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }

   private boolean lambda$new$1() {
      return this.field3366.method461() == AnticheatMode.Grim && this.field3370.method419();
   }

   private boolean lambda$new$0() {
      return this.field3366.method461() == AnticheatMode.Grim;
   }
}
