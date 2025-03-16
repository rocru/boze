package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import dev.boze.client.enums.ElytraFlyMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.Surround;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import mapped.Class3076;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3d;

public class ElytraFly extends Module {
   public static final ElytraFly INSTANCE = new ElytraFly();
   public final EnumSetting<ElytraFlyMode> field3217 = new EnumSetting<ElytraFlyMode>("Mode", ElytraFlyMode.Control, "Mode for Elytra flight");
   private final IntSetting field3218 = new IntSetting("MinHeight", 100, -100, 320, 1, "Minimum height for infinite mode", this::lambda$new$0);
   private final IntSetting field3219 = new IntSetting("MaxHeight", 140, -100, 320, 1, "Maximum height for infinite mode", this::lambda$new$1);
   private final FloatSetting field3220 = new FloatSetting("PitchStep", 0.8F, 0.1F, 1.0F, 0.1F, "Pitch step", this::lambda$new$2);
   private final MinMaxSetting field3221 = new MinMaxSetting("MinSpeed", 0.0, 0.0, 1.0, 0.1, "Minimum speed for infinite mode", this::lambda$new$3);
   private final MinMaxSetting field3222 = new MinMaxSetting("MaxSpeed", 3.5, 1.0, 10.0, 0.1, "Maximum speed for infinite mode", this::lambda$new$4);
   private final BooleanSetting field3223 = new BooleanSetting("AntiUnloaded", false, "Avoid unloaded chunks", this::lambda$new$5);
   private final BooleanSetting field3224 = new BooleanSetting("AvoidCollisions", false, "Stop when you're about to collide with a block", this::lambda$new$6);
   private final MinMaxSetting field3225 = new MinMaxSetting(
      "Ticks", 3.5, 0.1, 15.0, 0.1, "Ticks to predict for collision evasion", this::lambda$new$7, this.field3224
   );
   private final BooleanSetting field3226 = new BooleanSetting("Takeoff", true, "Automatically takeoff", this::lambda$new$8);
   private final BooleanSetting field3227 = new BooleanSetting("UseTimer", true, "Use timer while taking off", this::lambda$new$9);
   private final IntSetting field3228 = new IntSetting("Delay", 20, 0, 40, 1, "Tick delay for taking off", this::lambda$new$10);
   private final IntSetting field3229 = new IntSetting("Limit", 5, 0, 10, 1, "Take off limit", this::lambda$new$11);
   private final MinMaxSetting field3230 = new MinMaxSetting("Speed", 1.8, 0.1, 10.0, 0.1, "Speed", this::lambda$new$12);
   private final MinMaxSetting field3231 = new MinMaxSetting("UpSpeed", 1.0, 0.1, 10.0, 0.1, "Upwards Speed", this::lambda$new$13);
   private final MinMaxSetting field3232 = new MinMaxSetting("DownSpeed", 1.0, 0.1, 10.0, 0.1, "Downwards Speed", this::lambda$new$14);
   private final MinMaxSetting field3233 = new MinMaxSetting("AccelSpeed", 0.5, 0.05, 1.0, 0.05, "Acceleration speed", this::lambda$new$15);
   private final MinMaxSetting field3234 = new MinMaxSetting(
      "SetbackDelay", 0.25, 0.0, 20.0, 0.05, "Delay in seconds after rubber-banding before resuming", this::lambda$new$16
   );
   private final BooleanSetting field3235 = new BooleanSetting(
      "AutoPilot", false, "AutoPilot for OnGround mode\nUses baritone to walk around obstructions\n", this::lambda$new$17
   );
   private final MinMaxSetting field3236 = new MinMaxSetting("Acceleration", 0.0, 0.0, 0.1, 0.001, "Acceleration", this::lambda$new$18);
   private final BooleanSetting field3237 = new BooleanSetting(
      "SwingRocket", false, "Automatically swing your hand if you're holding a rocket", this::lambda$new$19
   );
   private final IntSetting field3238 = new IntSetting("SwingDelay", 10, 1, 50, 1, "Delay for swinging hand", this.field3237);
   private final Timer field3239 = new Timer();
   private final Timer field3240 = new Timer();
   private final Timer field3241 = new Timer();
   private int field3242 = 0;
   private double field3243 = 4.01389;
   private double field3244 = 0.0;
   private boolean field3245 = false;
   private float aa = 0.0F;
   private boolean ab = true;
   private float ac;

   public ElytraFly() {
      super("ElytraFly", "Elytra Fly", Category.Movement);
   }

   @Override
   public String method1322() {
      return this.field3217.method461().toString();
   }

   @EventHandler
   public void method1826(PlayerMoveEvent event) {
      Class3076.method6025(this);
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      }

      if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
         if (Surround.INSTANCE.autoDisable.field927) {
            if (this.field3217.method461() == ElytraFlyMode.Packet) {
               if (mc.player.horizontalCollision) {
                  this.field3244 = 0.0;
               }

               if (!this.field3241.hasElapsed(this.field3234.getValue() * 1000.0)) {
                  return;
               }

               if (this.field3245) {
                  if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
                     this.field3245 = false;
                     this.field3244 = 0.0;
                     ChatInstance.method740(this.getName(), "Baritone finished walking");
                     this.field3242 = 0;
                     mc.player.setYaw(this.aa);
                  }

                  return;
               }

               if (this.field3235.method419() && (mc.player.horizontalCollision || ElytraRecast.method286(mc.player.getPos(), mc.player.getYaw(), 15.0))) {
                  this.field3244 = 0.0;
                  ChatInstance.method740(this.getName(), "Using baritone to walk around obstructions");
                  this.field3245 = true;
                  this.aa = mc.player.getYaw();
                  mc.player.stopFallFlying();
                  this.field3242 = 0;
                  double var20 = 25.0;

                  while (true) {
                     float var23 = (float)Math.toRadians((double)MathHelper.wrapDegrees(mc.player.getYaw()));
                     int var8 = (int)Math.floor(mc.player.getPos().x - (double)MathHelper.sin(var23) * var20);
                     int var28 = (int)Math.floor(mc.player.getPos().z + (double)MathHelper.cos(var23) * var20);
                     if (!ElytraRecast.method286(new Vec3d((double)var8, mc.player.getPos().y, (double)var28), mc.player.getYaw(), 15.0) || var20 > 50.0) {
                        if ((Boolean)BaritoneAPI.getSettings().censorCoordinates.value) {
                           ChatInstance.method740(this.getName(), "Walking to censored which is " + var20 + " blocks further");
                        } else {
                           ChatInstance.method740(this.getName(), "Walking to " + var8 + ", " + var28 + " which is " + var20 + " blocks further");
                        }

                        var23 = (float)Math.toRadians((double)MathHelper.wrapDegrees(mc.player.getYaw()));
                        var8 = (int)Math.floor(mc.player.getPos().x - (double)MathHelper.sin(var23) * var20);
                        var28 = (int)Math.floor(mc.player.getPos().z + (double)MathHelper.cos(var23) * var20);
                        BaritoneAPI.getProvider()
                           .getPrimaryBaritone()
                           .getCustomGoalProcess()
                           .setGoalAndPath(new GoalBlock(var8, (int)mc.player.getPos().y, var28));
                        return;
                     }

                     var20 += 5.0;
                  }
               }

               double var19 = this.field3230.getValue();
               double var22 = this.field3233.getValue() * 0.1;
               Vec3d var27 = new Vec3d(mc.player.getVelocity().x, 0.0, mc.player.getVelocity().z);
               if (var27.length() < 0.25) {
                  var19 = 0.3;
                  var22 = var27.length() < 0.05 ? 0.01 : 0.025;
               }

               this.field3244 = MathHelper.clamp(this.field3244 + var22, 0.0, var19);
               Vec3d var30 = Class5924.method94(this.field3244, this.field3235.method419());
               mc.player.setVelocity(new Vec3d(var30.x, 0.0, var30.z));
               event.field1892 = true;
               event.vec3 = mc.player.getVelocity();
            } else if (mc.player.isFallFlying()) {
               this.field3242 = 0;
               if (this.field3217.method461() == ElytraFlyMode.ControlStrict) {
                  this.field3243 = this.field3243 + this.field3236.getValue();
                  double var5 = Math.sin(-Math.toRadians((double)mc.player.getYaw())) * this.field3243;
                  double var7 = Math.cos(Math.toRadians((double)mc.player.getYaw())) * this.field3243;
                  if (this.field3223.method419()
                     && !mc.world.getChunkManager().isChunkLoaded((int)((mc.player.getX() + var5) / 16.0), (int)((mc.player.getZ() + var7) / 16.0))) {
                     event.vec3 = Vec3d.ZERO;
                     return;
                  }

                  if (this.field3224.method419()) {
                     for (VoxelShape var10 : mc.world
                        .getBlockCollisions(
                           mc.player, mc.player.getBoundingBox().offset(var5 * this.field3225.getValue(), 0.0, var7 * this.field3225.getValue())
                        )) {
                        if (!var10.isEmpty()) {
                           event.vec3 = Vec3d.ZERO;
                           return;
                        }
                     }
                  }

                  event.vec3 = new Vec3d(var5, 0.0, var7);
               } else if (this.field3217.method461() != ElytraFlyMode.Infinite) {
                  Vector3d var18 = new Vector3d(mc.player.getVelocity().x, mc.player.getVelocity().y, mc.player.getVelocity().z);
                  Vec3d var6 = mc.player.getRotationVector();
                  double var21 = Math.sqrt(var6.x * var6.x + var6.z * var6.z);
                  double var26 = mc.player.getVelocity().horizontalLength();
                  Vec3d var11 = Class5924.method93(this.field3230.getValue());
                  if (mc.options.sneakKey.isPressed()) {
                     var18.set(var11.x, -this.field3232.getValue(), var11.z);
                  } else if (this.field3217.method461() == ElytraFlyMode.Control && mc.options.jumpKey.isPressed() && var26 > 1.0) {
                     double var12 = var26 * 0.01325 * this.field3231.getValue();
                     var18.y += var12 * 3.2;
                     var18.x = var18.x - var6.x * var12 / var21;
                     var18.z = var18.z - var6.z * var12 / var21;
                  } else if (this.field3217.method461() == ElytraFlyMode.Creative && mc.options.jumpKey.isPressed()) {
                     var18.set(var11.x, this.field3231.getValue(), var11.z);
                  } else {
                     var18.set(var11.x, -3.0E-14, var11.z);
                  }

                  if (this.field3224.method419()) {
                     for (VoxelShape var13 : mc.world
                        .getBlockCollisions(
                           mc.player,
                           mc.player
                              .getBoundingBox()
                              .offset(var18.x * this.field3225.getValue(), var18.y * this.field3225.getValue(), var18.z * this.field3225.getValue())
                        )) {
                        if (!var13.isEmpty()) {
                           event.field1892 = true;
                           event.vec3 = Vec3d.ZERO;
                           return;
                        }
                     }
                  }

                  if (this.field3217.method461() == ElytraFlyMode.Control) {
                     Vec3d var32 = mc.player.getVelocity();
                     if (var32.subtract(var18.x, var18.y, var18.z).length() > 0.5) {
                        var18.set(
                           var32.x + (var18.x - var32.x) * this.field3233.getValue(),
                           var32.y + (var18.y - var32.y) * this.field3233.getValue(),
                           var32.z + (var18.z - var32.z) * this.field3233.getValue()
                        );
                     }
                  }

                  if (this.field3217.method461() == ElytraFlyMode.Packet) {
                     mc.player.setVelocity(new Vec3d(var18.x * this.field3244, 0.0, var18.z * this.field3244));
                  } else {
                     mc.player.setVelocity(new Vec3d(var18.x, var18.y, var18.z));
                  }

                  event.field1892 = true;
                  event.vec3 = mc.player.getVelocity();
               }
            } else if (!mc.player.isOnGround() && mc.player.getY() <= mc.player.prevY) {
               this.field3243 = 4.01389;
               if (this.field3217.method461() == ElytraFlyMode.ControlStrict || this.field3226.method419()) {
                  event.vec3 = new Vec3d(0.0, 0.0, 0.0);
                  if (this.field3227.method419()) {
                     Class3076.method6024(this, 12, 0.125F);
                  }

                  if (this.field3239.hasElapsed((double)(this.field3228.method434() * 50))) {
                     if (this.field3242 >= this.field3229.method434()) {
                        NotificationManager.method1151(
                           new Notification(this.getName(), " Unable to take off", Notifications.WARNING, NotificationPriority.Yellow)
                        );
                        this.setEnabled(false);
                        return;
                     }

                     mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_FALL_FLYING));
                     this.field3242++;
                     this.field3239.reset();
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method1827(PreTickEvent event) {
      if (MinecraftUtils.isClientActive() && this.field3217.method461() == ElytraFlyMode.Infinite && mc.player.isFallFlying()) {
         double var5 = mc.player.getX() - mc.player.prevX;
         double var7 = mc.player.getZ() - mc.player.prevZ;
         double var9 = Math.sqrt(var5 * var5 + var7 * var7);
         if (!this.ab || !(mc.player.getY() <= (double)this.field3218.method434().intValue()) && !(var9 > this.field3222.getValue())) {
            if (!this.ab && (mc.player.getY() >= (double)this.field3219.method434().intValue() || var9 < this.field3221.getValue())) {
               this.ab = true;
            }
         } else {
            this.ab = false;
         }

         if (!this.ab && mc.player.getPitch() > -40.0F) {
            this.ac = this.ac - this.field3220.method423() * 5.0F;
            if (this.ac < -40.0F) {
               this.ac = -40.0F;
            }
         } else if (this.ab && mc.player.getPitch() < 40.0F) {
            this.ac = this.ac + this.field3220.method423() * 5.0F;
            if (this.ac > 40.0F) {
               this.ac = 40.0F;
            }
         }

         mc.player.setPitch(this.ac);
      }
   }

   @EventHandler(
      priority = 1000
   )
   public void method1828(MovementEvent event) {
      if (!this.field3245) {
         if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
            if (this.field3217.method461() == ElytraFlyMode.Packet) {
               if (this.field3241.hasElapsed(this.field3234.getValue() * 1000.0)) {
                  mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.START_FALL_FLYING));
               }
            } else {
               if (this.field3217.method461() == ElytraFlyMode.ControlStrict) {
                  event.method1074(new ActionWrapper(event.yaw, -2.0F, true));
               }

               if (this.field3237.method419() && this.field3240.hasElapsed((double)(this.field3238.method434() * 50))) {
                  if (mc.player.getMainHandStack().getItem() == Items.FIREWORK_ROCKET) {
                     mc.player.swingHand(Hand.MAIN_HAND);
                     this.field3238.method435();
                  } else if (mc.player.getOffHandStack().getItem() == Items.FIREWORK_ROCKET) {
                     mc.player.swingHand(Hand.OFF_HAND);
                     this.field3238.method435();
                  }
               }
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.field3242 = 0;
      this.field3243 = 4.01389;
      this.field3244 = 0.0;
      this.field3245 = false;
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         if (mc.player.getY() < (double)this.field3219.method434().intValue() && this.field3217.method461() == ElytraFlyMode.Infinite) {
            ChatInstance.method742("ElytraFly", "Player must be above or at max height");
            this.setEnabled(false);
         }

         this.ac = 40.0F;
      }
   }

   @Override
   public void onDisable() {
      Class3076.method6025(this);
      if (this.field3217.method461() == ElytraFlyMode.Packet && MinecraftUtils.isClientActive() && !((ClientPlayerEntityAccessor)mc.player).getLastSneaking()) {
         mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
         ((ClientPlayerEntityAccessor)mc.player).setLastSneaking(true);
      }
   }

   @EventHandler
   public void method1829(PacketBundleEvent event) {
      if (!this.field3245) {
         if (event.packet instanceof PlayerPositionLookS2CPacket) {
            this.field3243 = 4.01389;
            this.field3244 = 0.0;
            this.field3241.reset();
         } else if (event.packet instanceof EntityTrackerUpdateS2CPacket var5
            && this.field3217.method461() == ElytraFlyMode.Packet
            && mc.player != null
            && var5.id() == mc.player.getId()) {
            event.method1020();
         }
      }
   }

   private boolean lambda$new$19() {
      return this.field3217.method461() != ElytraFlyMode.Packet && this.field3217.method461() != ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$18() {
      return this.field3217.method461() == ElytraFlyMode.ControlStrict;
   }

   private boolean lambda$new$17() {
      return this.field3217.method461() == ElytraFlyMode.Packet;
   }

   private boolean lambda$new$16() {
      return this.field3217.method461() == ElytraFlyMode.Packet;
   }

   private boolean lambda$new$15() {
      return this.field3217.method461() == ElytraFlyMode.Control || this.field3217.method461() == ElytraFlyMode.Packet;
   }

   private boolean lambda$new$14() {
      return this.field3217.method461() != ElytraFlyMode.Packet
         && this.field3217.method461() != ElytraFlyMode.ControlStrict
         && this.field3217.method461() != ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$13() {
      return this.field3217.method461() != ElytraFlyMode.Packet
         && this.field3217.method461() != ElytraFlyMode.ControlStrict
         && this.field3217.method461() != ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$12() {
      return this.field3217.method461() != ElytraFlyMode.ControlStrict && this.field3217.method461() != ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$11() {
      return (this.field3226.method419() || this.field3217.method461() == ElytraFlyMode.ControlStrict) && this.field3217.method461() != ElytraFlyMode.Packet;
   }

   private boolean lambda$new$10() {
      return (this.field3226.method419() || this.field3217.method461() == ElytraFlyMode.ControlStrict) && this.field3217.method461() != ElytraFlyMode.Packet;
   }

   private boolean lambda$new$9() {
      return (this.field3226.method419() || this.field3217.method461() == ElytraFlyMode.ControlStrict) && this.field3217.method461() != ElytraFlyMode.Packet;
   }

   private boolean lambda$new$8() {
      return this.field3217.method461() != ElytraFlyMode.Packet;
   }

   private boolean lambda$new$7() {
      return this.field3217.method461() != ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$6() {
      return this.field3217.method461() != ElytraFlyMode.Infinite && this.field3217.method461() != ElytraFlyMode.Packet;
   }

   private boolean lambda$new$5() {
      return this.field3217.method461() == ElytraFlyMode.ControlStrict;
   }

   private boolean lambda$new$4() {
      return this.field3217.method461() == ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$3() {
      return this.field3217.method461() == ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$2() {
      return this.field3217.method461() == ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$1() {
      return this.field3217.method461() == ElytraFlyMode.Infinite;
   }

   private boolean lambda$new$0() {
      return this.field3217.method461() == ElytraFlyMode.Infinite;
   }
}
