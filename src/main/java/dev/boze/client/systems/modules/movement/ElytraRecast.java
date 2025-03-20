package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import dev.boze.client.enums.ElytraRecastDirection;
import dev.boze.client.events.*;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.AutoMend;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import mapped.Class3076;
import mapped.Class5928;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

import java.util.LinkedList;

public class ElytraRecast extends Module {
   public static final ElytraRecast INSTANCE = new ElytraRecast();
   public final IntSetting field566 = new IntSetting("Pitch", 75, 0, 90, 1, "Pitch");
   private final EnumSetting<ElytraRecastDirection> field567 = new EnumSetting<ElytraRecastDirection>(
      "YawSnap", ElytraRecastDirection.Off, "Snap yaw to specified direction"
   );
   private final MinMaxSetting field568 = new MinMaxSetting("Boost", 0.0, 0.0, 1.5, 0.05, "Boost factor\nSet to 0 to disable\n");
   private final MinMaxSetting field569 = new MinMaxSetting(
      "MinSpeed", 0.1, 0.0, 10.0, 0.1, "Min speed for boost\nWon't boost when below this speed\n", this.field568
   );
   private final MinMaxSetting field570 = new MinMaxSetting("MaxSpeed", 1.8, 0.1, 10.0, 0.1, "Max speed for boost", this.field568);
   private final BooleanSetting field571 = new BooleanSetting("Baritone", false, "Use baritone to walk around obstructions");
   private final BooleanSetting field572 = new BooleanSetting(
      "KeepY",
      false,
      "Keep Y-level the same as when you enable module\nPrevents Baritone from going off-track if you fall through a hole\n",
      this.field571::getValue
   );
   private final BooleanSetting field573 = new BooleanSetting("LockOn", false, "Lock onto initial direction when enabled", this.field571);
   private final IntSetting field574 = new IntSetting(
      "MaxBlocks", 5, 1, 20, 1, "Max blocks to steer off course for lock on", this.field573::getValue, this.field571
   );
   private final BooleanSetting field575 = new BooleanSetting("AutoTakeoff", true, "Automatically takeoff");
   private final BooleanSetting field576 = new BooleanSetting("UseTimer", false, "Use timer while taking off", this.field575);
   private final IntSetting field577 = new IntSetting("Delay", 20, 0, 40, 1, "Tick delay for taking off", this.field575);
   private final BooleanSetting field578 = new BooleanSetting(
      "AutoMend", true, "Automatically mend elytra\nThis uses the AutoMend module, make sure it's configured properly"
   );
   private final BooleanSetting field579 = new BooleanSetting("FreeLook", false, "Look around without rotating");
   public final Timer field580 = new Timer();
   public boolean field581;
   public boolean field582 = false;
   private Timer field583 = new Timer();
   private int field584 = 0;
   private double field585 = 0.0;
   private boolean field586 = true;
   private LinkedList<Packet<?>> field587 = new LinkedList();
   private LinkedList<Packet<?>> field588 = new LinkedList();
   private final Timer field589 = new Timer();
   private boolean field590 = false;
   private double field591 = 0.0;
   private Vec3d field592 = null;
   private float field593 = 0.0F;
   private Vec3d field594 = null;
   private Vec3d aa = null;
   private boolean ab = false;
   private float ac;
   private float ad;
   private float ae;
   private float af;
   private Perspective ag = null;
   private long ah = 0L;

   public ElytraRecast() {
      super("ElytraRecast", "Fast elytra travel on highways", Category.Movement);
      this.field579.method401(this::lambda$new$0);
   }

   @Override
   public void onEnable() {
      this.field584 = 0;
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field581 = true;
         this.ae = this.ac = mc.player.getYaw();
         this.af = this.ad = mc.player.getPitch();
         this.ag = mc.options.getPerspective();
         this.field582 = false;
         this.field586 = true;
         this.field585 = 0.0;
         this.field590 = false;
         this.field591 = Math.floor(mc.player.getY());
         if (this.field573.getValue() && this.field571.getValue()) {
            this.field594 = mc.player.getPos();
            Vec3d var4 = mc.player.getRotationVec(1.0F);
            this.aa = new Vec3d(var4.x, 0.0, var4.z).normalize();
         } else {
            this.field594 = null;
            this.aa = null;
         }

         this.field592 = mc.player.getPos();
         this.field593 = mc.player.getYaw();
      }
   }

   @EventHandler
   public void method1942(PostPlayerTickEvent event) {
      if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
         if (this.field582 && mc.player.isFallFlying()) {
            double var5 = Math.pow(mc.player.getVelocity().getX(), 2.0) + Math.pow(mc.player.getVelocity().getZ(), 2.0);
            if (var5 > Math.pow(this.field570.getValue(), 2.0) || var5 < Math.pow(this.field569.getValue(), 2.0)) {
               return;
            }

            float var7 = (float)Math.toRadians((double)mc.player.getYaw());
            mc.player
               .addVelocity(
                  (double)MathHelper.sin(var7) * -(this.field568.getValue() * 0.02), 0.0, (double)MathHelper.cos(var7) * this.field568.getValue() * 0.02
               );
         }
      }
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      Class3076.method6025(this);
      if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
         if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
            this.method288(mc.options.jumpKey, true);
         }

         if (this.field590) {
            if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().getGoal() == null) {
               this.field590 = false;
               this.field582 = false;
               ChatInstance.method740(this.getName(), "Baritone finished walking");
               this.method288(mc.options.jumpKey, true);
               this.field584 = 0;
            }
         } else if (this.field582 && mc.player.isFallFlying()) {
            if (this.field571.getValue() && (mc.player.horizontalCollision || method286(this.field592, this.field593, 15.0))) {
               this.field582 = false;
               this.field589.reset();
               ChatInstance.method740(this.getName(), "Using baritone to walk around obstructions");
               mc.player.stopFallFlying();
               this.method288(mc.options.forwardKey, false);
               this.method288(mc.options.jumpKey, false);
               this.field590 = true;
               this.field584 = 0;
               double var14 = 25.0;
               int var7;
               int var8;
               if (this.field573.getValue() && this.field594 != null && this.aa != null) {
                  Vec3d var15 = mc.player.getPos().add(this.aa.multiply(var14));
                  var7 = (int)Math.floor(var15.x);
                  var8 = (int)Math.floor(var15.z);
               } else {
                  float var9 = (float)Math.toRadians((double)MathHelper.wrapDegrees(this.field593));
                  var7 = (int)Math.floor(this.field592.x - (double)MathHelper.sin(var9) * var14);
                  var8 = (int)Math.floor(this.field592.z + (double)MathHelper.cos(var9) * var14);
               }

               while ((!method286(new Vec3d((double)var7, this.field592.y, (double)var8), this.field593, 15.0) || var14 > 50.0) && !(var14 > 50.0)) {
                  var14 += 5.0;
                  if (this.field573.getValue() && this.field594 != null && this.aa != null) {
                     Vec3d var17 = mc.player.getPos().add(this.aa.multiply(var14));
                     var7 = (int)Math.floor(var17.x);
                     var8 = (int)Math.floor(var17.z);
                  } else {
                     float var16 = (float)Math.toRadians((double)MathHelper.wrapDegrees(this.field593));
                     var7 = (int)Math.floor(this.field592.x - (double)MathHelper.sin(var16) * var14);
                     var8 = (int)Math.floor(this.field592.z + (double)MathHelper.cos(var16) * var14);
                  }
               }

               if ((Boolean)BaritoneAPI.getSettings().censorCoordinates.value) {
                  ChatInstance.method740(this.getName(), "Walking to censored which is " + var14 + " blocks further");
               } else {
                  ChatInstance.method740(this.getName(), "Walking to " + var7 + ", " + var8 + " which is " + var14 + " blocks further");
               }

               this.field592 = new Vec3d((double)var7, this.field592.y, (double)var8);
               BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(var7, (int)this.field592.y, var8));
            } else if (mc.player.horizontalCollision) {
               this.field582 = false;
               this.field589.reset();
               ChatInstance.method740(this.getName(), "Stuck, stopping e-fly");
               mc.player.stopFallFlying();
               this.method288(mc.options.forwardKey, false);
            } else if (this.field578.getValue()) {
               ItemStack var5 = mc.player.getEquippedStack(EquipmentSlot.CHEST);
               if (var5.getDamage() > 10 && AutoMend.INSTANCE.method2010() != -1) {
                  AutoMend.INSTANCE.setEnabled(true);
               }
            }
         }
      }
   }

   public void method1904() {
      if (this.ab) {
         this.ab = false;
         mc.player.input.jumping = true;
      }
   }

   public void method1854() {
      if (INSTANCE.isEnabled()
         && this.field575.getValue()
         && !this.field590
         && !this.field582
         && mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA
         && !mc.player.isOnGround()
         && mc.player.getY() - this.field591 > 1.1
         && mc.player.getY() - this.field591 < 1.2) {
         if (!this.field589.hasElapsed(2500.0)) {
            return;
         }

         if (this.field576.getValue()) {
            Class3076.method6024(this, 12, 0.125F);
         }

         if (this.field583.hasElapsed((double)(this.field577.getValue() * 50))) {
            if (this.field584 >= 5) {
               ChatInstance.method740(this.getName(), "Failed to takeoff, waiting...");
               this.field584 = 0;
               this.field589.reset();
               return;
            }

            mc.player.input.jumping = false;
            this.ab = true;
            this.field584++;
            this.field583.reset();
         }
      }
   }

   @EventHandler
   public void method2072(PreTickEvent event) {
      if (MinecraftUtils.isClientActive() && !this.field590 && this.field589.hasElapsed(2500.0)) {
         if (mc.player.isFallFlying()) {
            this.field582 = true;
         }

         if (this.field582) {
            if (method1972()) {
               this.method288(mc.options.forwardKey, true);
               this.method288(mc.options.jumpKey, true);
               mc.player.setPitch((float)this.field566.getValue().intValue());
               if (this.field567.getValue() != ElytraRecastDirection.Off) {
                  mc.player.setYaw(this.field567.getValue().field1766);
               }

               mc.player.setSprinting(true);
            }
         }
      }
   }

   @EventHandler
   public void method1853(PrePacketSendEvent event) {
      if (event.packet instanceof CommonPongC2SPacket var5 && MinecraftUtils.isClientActive()) {
         if (this.field582 && mc.player.isFallFlying()) {
            if (!mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, -1.0, 0.0))
               && (
                  this.field586 && this.method1389(((ClientPlayerEntityAccessor)mc.player).getLastY()) < 0.8
                     || !this.field586 && this.method1389(((ClientPlayerEntityAccessor)mc.player).getLastY()) < 0.4
               )) {
               this.field587.add(var5);
               event.method1020();
            }

            if (!this.field586 && this.method1389(((ClientPlayerEntityAccessor)mc.player).getLastY()) >= 0.4 && !this.field587.isEmpty()) {
               while (!this.field587.isEmpty()) {
                  mc.player.networkHandler.getConnection().send((Packet)this.field587.poll(), null);
               }
            }
         } else if (!this.field587.isEmpty()) {
            while (!this.field587.isEmpty()) {
               mc.player.networkHandler.getConnection().send((Packet)this.field587.poll(), null);
            }
         }

         return;
      }

      if (event.packet instanceof PlayerMoveC2SPacket var6) {
         if (this.field582 && mc.player.isFallFlying()) {
            if (var6.getY(this.field585) < this.field585) {
               this.field586 = true;
            } else if (var6.getY(this.field585) > this.field585) {
               this.field586 = false;
            }

            this.field585 = var6.getY(this.field585);
            if (!mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, -1.0, 0.0))
               && (
                  this.field586 && this.method1389(var6.getY(((ClientPlayerEntityAccessor)mc.player).getLastY())) < 0.8
                     || !this.field586 && this.method1389(((ClientPlayerEntityAccessor)mc.player).getLastY()) < 0.4
               )) {
               this.field588.add(var6);
               event.method1020();
               this.field591 = Math.floor(mc.player.getY());
               if (var6.getY(mc.player.getY()) - this.field591 <= 0.0) {
                  if (this.field572.getValue() && Math.floor(mc.player.getY()) < Math.floor(this.field592.getY())) {
                     this.field592 = new Vec3d(mc.player.getX(), this.field592.getY(), mc.player.getZ());
                  } else {
                     this.field592 = mc.player.getPos();
                  }

                  this.field593 = this.field567.getValue() != ElytraRecastDirection.Off ? this.field567.getValue().field1766 : mc.player.getYaw();
               }
            }

            if (!this.field586 && this.method1389(((ClientPlayerEntityAccessor)mc.player).getLastY()) >= 0.4 && !this.field588.isEmpty()) {
               while (!this.field588.isEmpty()) {
                  mc.player.networkHandler.getConnection().send((Packet)this.field588.poll(), null);
               }
            }
         } else if (!this.field588.isEmpty()) {
            while (!this.field588.isEmpty()) {
               mc.player.networkHandler.getConnection().send((Packet)this.field588.poll(), null);
            }
         }
      }
   }

   public static boolean method286(Vec3d bottom, float yaw, double range) {
      Vec3d var7 = method289(0.0F, yaw).multiply(range);
      Vec3d var8 = method289(0.0F, yaw + 90.0F).multiply((double)mc.player.getWidth() * 0.75);
      Vec3d var9 = method289(0.0F, yaw - 90.0F).multiply((double)mc.player.getWidth() * 0.75);
      Vec3d var10 = bottom.add(0.0, mc.player.getBoundingBox().maxY - mc.player.getBoundingBox().minY, 0.0);
      Vec3d var11 = bottom.add(var8);
      Vec3d var12 = var10.add(var8);
      Vec3d var13 = bottom.add(var9);
      Vec3d var14 = var10.add(var9);
      return method117(bottom, bottom.add(var7))
         || method117(var10, var10.add(var7))
         || method117(var11, var11.add(var7))
         || method117(var12, var12.add(var7))
         || method117(var13, var13.add(var7))
         || method117(var14, var14.add(var7));
   }

   private static boolean method117(Vec3d var0, Vec3d var1) {
      return mc.world.raycast(new RaycastContext(var0, var1, ShapeType.COLLIDER, FluidHandling.NONE, mc.player)).getType() != Type.MISS;
   }

   private double method1389(double var1) {
      return var1 - Math.floor(var1);
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && MinecraftUtils.isClientActive()) {
         mc.player.stopFallFlying();
         this.method288(mc.options.forwardKey, false);
         this.method288(mc.options.jumpKey, true);
         this.field582 = false;
         this.field584 = 0;
         this.field589.reset();
      }
   }

   private void method288(KeyBinding var1, boolean var2) {
      var1.setPressed(var2);
      Class5928.method288(var1, var2);
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive()) {
         Class3076.method6025(this);
         this.method288(mc.options.forwardKey, false);
         this.method288(mc.options.jumpKey, false);
         this.field588.clear();
         this.field587.clear();
      }
   }

   public boolean method1971() {
      return method1972() && this.method1973();
   }

   public static boolean method1972() {
      ItemStack var3 = mc.player.getEquippedStack(EquipmentSlot.CHEST);
      return !mc.player.getAbilities().flying && !mc.player.hasVehicle() && !mc.player.isClimbing() && var3.isOf(Items.ELYTRA) && ElytraItem.isUsable(var3);
   }

   private boolean method1973() {
      if (!mc.player.isTouchingWater() && !mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
         ItemStack var4 = mc.player.getEquippedStack(EquipmentSlot.CHEST);
         if (var4.isOf(Items.ELYTRA) && ElytraItem.isUsable(var4)) {
            mc.player.startFallFlying();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static Vec3d method289(float var0, float var1) {
      float var2 = var0 * (float) (Math.PI / 180.0);
      float var3 = -var1 * (float) (Math.PI / 180.0);
      float var4 = MathHelper.cos(var3);
      float var5 = MathHelper.sin(var3);
      float var6 = MathHelper.cos(var2);
      float var7 = MathHelper.sin(var2);
      return new Vec3d((double)(var5 * var6), (double)(-var7), (double)(var4 * var6));
   }

   public void method1955(double dX, double dY) {
      this.ac = this.ae;
      this.ad = this.af;
      this.ae = (float)((double)this.ae + dX);
      this.af = (float)((double)this.af + dY);
      this.af = MathHelper.clamp(this.af, -90.0F, 90.0F);
   }

   public float method1956(float tickDelta) {
      return tickDelta == 1.0F ? this.af : MathHelper.lerp(tickDelta, this.ad, this.af);
   }

   public float method1957(float tickDelta) {
      return tickDelta == 1.0F ? this.ae : MathHelper.lerp(tickDelta, this.ac, this.ae);
   }

   @EventHandler
   public void method1958(PostRender event) {
      if (MinecraftUtils.isClientActive()) {
         float var5 = (float)(System.currentTimeMillis() - this.ah) / 5.0F;
         this.ah = System.currentTimeMillis();
         float var6 = 0.0F;
         float var7 = 0.0F;
         if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 263)) {
            var6 -= var5;
         }

         if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 262)) {
            var6 += var5;
         }

         if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 265)) {
            var7 -= var5;
         }

         if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 264)) {
            var7 += var5;
         }

         mc.player.setYaw(mc.player.getYaw() + var6);
         mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + var7, -90.0F, 90.0F));
      }
   }

   public static boolean method1974() {
      return INSTANCE.isEnabled() && INSTANCE.field579.getValue() && mc.player != null && mc.player.isFallFlying();
   }

   private void lambda$new$0(Boolean var1) {
      if (var1 && this.isEnabled() && MinecraftUtils.isClientActive()) {
         this.ae = this.ac = mc.player.getYaw();
         this.af = this.ad = mc.player.getPitch();
         this.ag = mc.options.getPerspective();
      }
   }
}
