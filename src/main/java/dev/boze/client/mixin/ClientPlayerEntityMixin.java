package dev.boze.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.NoSlowItems;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.events.PlayerVelocityEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.events.dN;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.WTap;
import dev.boze.client.systems.modules.misc.ExtraChat;
import dev.boze.client.systems.modules.misc.Scaffold;
import dev.boze.client.systems.modules.misc.Swing;
import dev.boze.client.systems.modules.movement.ElytraBoost;
import dev.boze.client.systems.modules.movement.ElytraRecast;
import dev.boze.client.systems.modules.movement.GrimDisabler;
import dev.boze.client.systems.modules.movement.NoSlow;
import dev.boze.client.systems.modules.movement.TickShift;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.trackers.EntityTracker;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.RotationHandler;
import dev.boze.client.Boze;
import mapped.Class2839;
import mapped.Class3091;
import mapped.Class5913;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
   value = {ClientPlayerEntity.class},
   priority = 999
)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements IClientPlayerEntity {
   @Shadow
   @Final
   public ClientPlayNetworkHandler networkHandler;
   @Shadow
   private boolean lastSneaking;
   @Shadow
   private boolean lastSprinting;
   @Shadow
   private int ticksSinceLastPositionPacketSent;
   @Shadow
   private float lastPitch;
   @Shadow
   private float lastYaw;
   @Shadow
   private double lastZ;
   @Shadow
   private double lastBaseY;
   @Shadow
   private double lastX;
   @Shadow
   private boolean lastOnGround;
   @Shadow
   public boolean autoJumpEnabled;
   @Shadow
   @Final
   protected MinecraftClient client;
   @Shadow
   public float renderYaw;
   @Shadow
   public float lastRenderYaw;
   @Shadow
   public float renderPitch;
   @Shadow
   public float lastRenderPitch;
   @Shadow
   public Input input;
   @Unique
   private float lastSpoofedYaw;
   @Unique
   private float lastSpoofedPitch;
   @Unique
   private long lastSpoofedTime;

   public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
      super(world, profile);
   }

   @Shadow
   public abstract float getPitch(float var1);

   @Shadow
   protected abstract boolean isCamera();

   @Shadow
   public abstract boolean isUsingItem();

   @Shadow
   public abstract boolean isSneaking();

   @Shadow
   protected abstract void autoJump(float var1, float var2);

   @Shadow
   public abstract float getYaw(float var1);

   @Unique
   public void sendMovementPackets(
      double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking, boolean hardRotate
   ) {
      if (sprinting != this.lastSprinting) {
         Mode var15 = sprinting ? Mode.START_SPRINTING : Mode.STOP_SPRINTING;
         this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, var15));
         this.lastSprinting = sprinting;
      }

      if (sneaking != this.lastSneaking) {
         Mode var29 = sneaking ? Mode.PRESS_SHIFT_KEY : Mode.RELEASE_SHIFT_KEY;
         this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, var29));
         this.lastSneaking = sneaking;
      }

      if (this.isCamera()) {
         if (!Options.INSTANCE.method1971() && !hardRotate) {
            if (AntiCheat.INSTANCE.field2316.method419()) {
               yaw += (float)(Math.sin((double)System.currentTimeMillis() / 1000.0) * 0.1);
               pitch += (float)(Math.sin((double)System.currentTimeMillis() / 2000.0) * 0.1);
            }

            if (AntiCheat.INSTANCE.field2315.method419()) {
               double var30 = Math.pow((Double)this.client.options.getMouseSensitivity().getValue() * 0.6 + 0.2, 3.0) * 1.2;
               yaw = (float)((double)yaw - (double)(yaw - ((ClientPlayerEntityAccessor)this.client.player).getLastYaw()) % var30);
               pitch = (float)((double)pitch - (double)(pitch - ((ClientPlayerEntityAccessor)this.client.player).getLastPitch()) % var30);
            }
         }

         double var31 = x - this.lastX;
         double var17 = y - this.lastBaseY;
         double var19 = z - this.lastZ;
         double var21 = (double)(yaw - this.lastYaw);
         double var23 = (double)(pitch - this.lastPitch);
         this.ticksSinceLastPositionPacketSent++;
         boolean var25 = var31 * var31 + var17 * var17 + var19 * var19 > 9.0E-4 || this.ticksSinceLastPositionPacketSent >= 20;
         boolean var26 = var21 != 0.0 || var23 != 0.0;
         int var27 = -1;
         if (var25 && !this.hasVehicle()) {
            if (ElytraBoost.INSTANCE.field1011.field3208 && (var27 = ElytraBoost.INSTANCE.field1011.method1822()) != -1) {
               if (this.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
                  InvUtils.method2201().method2207(var27).method2218(2);
                  this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, Mode.START_FALL_FLYING));
               } else if (ElytraBoost.INSTANCE.field1011.field3210) {
                  this.networkHandler.sendPacket(new ClientCommandC2SPacket(this, Mode.START_FALL_FLYING));
               }
            } else if (ElytraBoost.INSTANCE.field1011.field3211) {
               var27 = InventoryHelper.method169(ClientPlayerEntityMixin::lambda$sendMovementPackets$0);
               ElytraBoost.INSTANCE.field1011.field3211 = false;
               if (var27 != -1) {
                  InvUtils.method2201().method2211(2).method2213(var27);
               }
            }
         }

         if (this.hasVehicle()) {
            Vec3d var28 = this.getVelocity();
            this.networkHandler.sendPacket(new Full(var28.x, -999.0, var28.z, yaw, pitch, onGround));
            var25 = false;
         } else if (!var25 || !var26 && !hardRotate) {
            if (var25) {
               this.networkHandler.sendPacket(new PositionAndOnGround(x, y, z, onGround));
            } else if (var26) {
               this.networkHandler.sendPacket(new LookAndOnGround(yaw, pitch, onGround));
            } else if (this.lastOnGround != onGround) {
               this.networkHandler.sendPacket(new OnGroundOnly(onGround));
            }
         } else {
            this.networkHandler.sendPacket(new Full(x, y, z, yaw, pitch, onGround));
         }

         if (var25) {
            this.lastX = x;
            this.lastBaseY = y;
            this.lastZ = z;
            this.ticksSinceLastPositionPacketSent = 0;
            if (var27 != -1) {
               InvUtils.method2201().method2207(var27).method2218(2);
            }
         }

         if (var26) {
            this.lastYaw = yaw;
            this.lastPitch = pitch;
         }

         this.lastOnGround = onGround;
         this.autoJumpEnabled = (Boolean)this.client.options.getAutoJump().getValue();
      }
   }

   @Override
   public void boze$sendMovementPackets(double x, double y, double z, float yaw, float pitch, boolean onGround) {
      this.sendMovementPackets(x, y, z, yaw, pitch, onGround, this.isSprinting(), this.isSneaking(), false);
   }

   @Override
   public void boze$sendMovementPackets(double x, double y, double z, float yaw, float pitch) {
      this.boze$sendMovementPackets(x, y, z, yaw, pitch, this.isOnGround());
   }

   @Override
   public void boze$sendMovementPackets(double x, double y, double z) {
      this.boze$sendMovementPackets(x, y, z, this.getYaw(), this.getPitch(), this.isOnGround());
   }

   @Override
   public void boze$sendMovementPackets(float yaw, float pitch, boolean onGround) {
      this.boze$sendMovementPackets(this.getX(), this.getY(), this.getZ(), yaw, pitch, onGround);
   }

   @Override
   public void boze$sendMovementPackets(float yaw, float pitch) {
      this.boze$sendMovementPackets(this.getX(), this.getY(), this.getZ(), yaw, pitch, this.isOnGround());
   }

   @Override
   public void boze$sendMovementPackets(double x, double y, double z, boolean onGround) {
      this.boze$sendMovementPackets(x, y, z, this.getYaw(), this.getPitch(), onGround);
   }

   @Override
   public void boze$sendMovementPackets(boolean onGround) {
      this.boze$sendMovementPackets(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch(), onGround);
   }

   @Override
   public void boze_tick() {
      super.tick();
   }

   @Inject(
      method = {"sendMovementPackets"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendMovementPackets(CallbackInfo var1) {
      if (GrimDisabler.INSTANCE.isEnabled()) {
         double var4 = this.getX() - this.lastX;
         double var6 = this.getY() - this.lastBaseY;
         double var8 = this.getZ() - this.lastZ;
         boolean var10 = var4 * var4 + var6 * var6 + var8 * var8 > 9.0E-4 || this.ticksSinceLastPositionPacketSent >= 20;
         if (var10 && var4 != 0.0 && var8 != 0.0) {
            this.sendMovementPackets(
               this.getX(),
               this.getY(),
               this.getZ(),
               this.getYaw(),
               420.0F + (float)Math.random(),
               this.isOnGround(),
               this.isSprinting(),
               this.isSneaking(),
               true
            );
            var1.cancel();
            return;
         }
      }

      EntityTracker.method2142();
      if (NoSlow.INSTANCE.isEnabled()
         && NoSlow.INSTANCE.field3304.method461() == NoSlowItems.GrimV2Old
         && this.isUsingItem()
         && this.getActiveItem().getItem() != Items.BOW) {
         this.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.getInventory().selectedSlot % 8 + 1));
         this.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(this.getInventory().selectedSlot));
      }

      Class2839.field111 = this.getInventory().selectedSlot;
      boolean var11 = NoSlow.INSTANCE.isEnabled()
         && NoSlow.INSTANCE.field3304.method461() == NoSlowItems.GrimV2Old
         && this.isUsingItem()
         && this.getActiveItem().getItem() != Items.BOW;
      if (!RotationHandler.field1546.method2114() && !var11 && !ElytraBoost.INSTANCE.field1011.field3208 && !ElytraBoost.INSTANCE.field1011.field3211) {
         Boze.prevLastYaw = this.lastYaw;
         Boze.prevLastPitch = this.lastPitch;
         MovementEvent var12 = (MovementEvent) Boze.EVENT_BUS
            .post(
               MovementEvent.method1075(
                  this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch(), this.isOnGround(), this.isSprinting(), this.isSneaking()
               )
            );
         if (var12.field1934) {
            var1.cancel();
         } else {
            ACRotationEvent var14 = ACRotationEvent.method1016(
               AnticheatMode.NCP, RotationHandler.field1546.method1384(), RotationHandler.field1546.method1385()
            );
            Boze.EVENT_BUS.post(var14);
            TickShift.field898 = true;
            if (var14.method1022()) {
               var1.cancel();
               this.sendMovementPackets(
                  var12.field1930, var12.field1931, var12.field1932, var14.yaw, var14.pitch, var12.isOnGround, var12.isSprinting, var12.isSneaking, false
               );
               this.lastSpoofedYaw = var14.yaw;
               this.lastSpoofedPitch = var14.pitch;
               this.lastSpoofedTime = System.currentTimeMillis();
               Options.field994.reset();
            }

            RotationEvent var15 = RotationEvent.method553(RotationMode.Sequential, var12.yaw, var12.pitch, this.getRotationVector(), false);
            Boze.EVENT_BUS.post(var15);

            while (!var12.field1933.isEmpty()) {
               ActionWrapper var17 = (ActionWrapper)var12.field1933.poll();
               if (var17.field3900) {
                  var1.cancel();
                  this.sendMovementPackets(
                     var12.field1930,
                     var12.field1931,
                     var12.field1932,
                     var17.field3902,
                     var17.field3903,
                     var12.isOnGround,
                     var12.isSprinting,
                     var12.isSneaking,
                     var17.field3901
                  );
                  this.lastSpoofedYaw = var17.field3902;
                  this.lastSpoofedPitch = var17.field3903;
                  this.lastSpoofedTime = System.currentTimeMillis();
                  Options.field994.reset();
               }

               if (var17.field3904 != null) {
                  var17.field3904.run();
               }
            }

            if (!var1.isCancelled()
               && !Options.INSTANCE.method1971()
               && (
                  System.currentTimeMillis() - this.lastSpoofedTime < (long)(AntiCheat.INSTANCE.field2317.method434() * 50)
                     || var12.field1931 != this.getY()
                     || var12.field1930 != this.getX()
                     || var12.field1932 != this.getZ()
                     || var12.isOnGround != this.isOnGround()
                     || var12.isSprinting != this.isSprinting()
                     || var12.isSneaking != this.isSneaking()
               )) {
               var1.cancel();
               TickShift.field898 = System.currentTimeMillis() - this.lastSpoofedTime < 370L;
               this.sendMovementPackets(
                  var12.field1930,
                  var12.field1931,
                  var12.field1932,
                  System.currentTimeMillis() - this.lastSpoofedTime < 370L ? this.lastSpoofedYaw : var12.yaw,
                  System.currentTimeMillis() - this.lastSpoofedTime < 370L ? this.lastSpoofedPitch : var12.pitch,
                  var12.isOnGround,
                  var12.isSprinting,
                  var12.isSneaking,
                  false
               );
               Options.field994.reset();
            }

            TickShift.field898 = false;
            if (this.getInventory().selectedSlot != Class2839.field111) {
               this.getInventory().selectedSlot = Class2839.field111;
            }

            if (NoSlow.INSTANCE.isEnabled()
               && NoSlow.INSTANCE.field3304.method461() == NoSlowItems.GrimV2Old
               && this.isUsingItem()
               && this.getActiveItem().getItem() != Items.BOW) {
               BlockHitResult var18 = new BlockHitResult(
                  new Vec3d(this.getX() + 0.5, this.getY(), this.getZ() + 0.5), Direction.UP, BlockPos.ofFloored(this.getPos()).down(), false
               );
               if (this.client.crosshairTarget instanceof BlockHitResult var19) {
                  var18 = var19;
               }

               Class5913.method17(Hand.MAIN_HAND, var18);
            }
         }
      } else {
         var1.cancel();
         BlockHitResult var5 = null;
         if (var11 && !(this.client.crosshairTarget instanceof BlockHitResult)) {
            var5 = new BlockHitResult(
               new Vec3d(this.getX() + 0.5, this.getY(), this.getZ() + 0.5), Direction.UP, BlockPos.ofFloored(this.getPos()).down(), false
            );
         }

         MovementEvent var13 = MovementEvent.method1075(
            this.getX(),
            this.getY(),
            this.getZ(),
            RotationHandler.field1546.method1384(),
            RotationHandler.field1546.method1385(),
            this.isOnGround(),
            this.isSprinting(),
            this.isSneaking()
         );
         var13.method1021(RotationHandler.field1546.method2114());
         Boze.EVENT_BUS.post(var13);
         if (!var13.field1934) {
            ACRotationEvent var7 = ACRotationEvent.method1016(AnticheatMode.NCP, var13.yaw, var13.pitch);
            var7.method1021(RotationHandler.field1546.method2114());
            Boze.EVENT_BUS.post(var7);
            this.sendMovementPackets(
               this.getX(),
               this.getY(),
               this.getZ(),
               RotationHandler.field1546.method2114() ? RotationHandler.field1546.method1384() : var7.yaw,
               var11 && var5 == null ? 90.0F : (RotationHandler.field1546.method2114() ? RotationHandler.field1546.method1385() : var7.pitch),
               this.isOnGround(),
               this.isSprinting(),
               this.isSneaking(),
               false
            );
            RotationEvent var16 = RotationEvent.method553(RotationMode.Sequential, var13.yaw, var13.pitch, this.getRotationVector(), true);
            Boze.EVENT_BUS.post(var16);
            Boze.prevLastYaw = this.lastYaw;
            Boze.prevLastPitch = this.lastPitch;
            Options.field994.reset();
            if (this.getInventory().selectedSlot != Class2839.field111) {
               this.getInventory().selectedSlot = Class2839.field111;
            }

            if (var11) {
               if (this.client.crosshairTarget instanceof BlockHitResult var9) {
                  var5 = var9;
               } else if (var5 == null) {
                  var5 = new BlockHitResult(
                     new Vec3d(this.getX() + 0.5, this.getY(), this.getZ() + 0.5), Direction.UP, BlockPos.ofFloored(this.getPos()).down(), false
                  );
               }

               Class5913.method17(Hand.MAIN_HAND, var5);
            }
         }
      }
   }

   @Inject(
      method = {"isSneaking"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onIsSneaking(CallbackInfoReturnable<Boolean> cir) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         if (Scaffold.INSTANCE.isEnabled() && Scaffold.INSTANCE.field3112 && Scaffold.INSTANCE.field3093.method461() == AnticheatMode.Grim) {
            cir.setReturnValue(true);
         }
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At("HEAD")}
   )
   private void onPlayerTickPre(CallbackInfo var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         Boze.EVENT_BUS.post(PrePlayerTickEvent.method1090(MinecraftClient.getInstance().player));
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At("TAIL")}
   )
   private void onPlayerTickPost(CallbackInfo var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         Boze.EVENT_BUS.post(PostPlayerTickEvent.method1085(MinecraftClient.getInstance().player));
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("TAIL")}
   )
   private void onTickPost(CallbackInfo var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         WTap.INSTANCE.method1626();
      }
   }

   @Inject(
      method = {"move"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"
      )},
      cancellable = true
   )
   public void onMove(MovementType type, Vec3d movement, CallbackInfo ci) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         if (!Class3091.field217) {
            ci.cancel();
            PlayerMoveEvent var4 = (PlayerMoveEvent) Boze.EVENT_BUS.post(PlayerMoveEvent.method1036(type, movement));
            if (!var4.method1022()) {
               double var5 = this.getX();
               double var7 = this.getZ();
               super.move(var4.movementType, var4.vec3);
               this.autoJump((float)(this.getX() - var5), (float)(this.getZ() - var7));
            }
         }
      }
   }

   @Redirect(
      method = {"swingHand"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"
      )
   )
   private void onSwingHand(AbstractClientPlayerEntity var1, Hand var2) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         if (!Swing.INSTANCE.isEnabled()) {
            var1.swingHand(var2, false);
         }
      }
   }

   @Redirect(
      method = {"tickNausea"},
      at = @At(
         value = "FIELD",
         target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"
      )
   )
   private Screen getScreenUpdateNausea(MinecraftClient var1) {
      if (!((ClientPlayerEntity)this).equals(var1.player)) {
         return var1.currentScreen;
      } else {
         return ExtraChat.INSTANCE.isEnabled() && ExtraChat.INSTANCE.method1699() && ExtraChat.INSTANCE.field2940.method419() ? null : var1.currentScreen;
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At("HEAD")}
   )
   private void onTickMovementPre(CallbackInfo var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         ElytraRecast.INSTANCE.method1854();
         ElytraBoost.INSTANCE.method1854();
         dN var4 = (dN) Boze.EVENT_BUS.post(dN.method1035());
         if (var4.method1022()) {
            Class2839.field113 = true;
         }
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/MinecraftClient;getTutorialManager()Lnet/minecraft/client/tutorial/TutorialManager;"
      )}
   )
   private void onTickMovementMid(CallbackInfo var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         ElytraRecast.INSTANCE.method1904();
         ElytraBoost.INSTANCE.method1904();
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At("TAIL")}
   )
   private void onTickMovementPost(CallbackInfo var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         Class2839.field113 = false;
      }
   }

   @Inject(
      method = {"isUsingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onIsUsingItem(CallbackInfoReturnable<Boolean> var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         if (Class2839.field113) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(
      method = {"shouldSlowDown"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onShouldSlowDown(CallbackInfoReturnable<Boolean> var1) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         if (NoSlow.INSTANCE.isEnabled()) {
            if (NoSlow.INSTANCE.field3316.method419() && this.isSneaking()) {
               var1.setReturnValue(false);
            }

            if (NoSlow.INSTANCE.field3317.method419() && this.isCrawling()) {
               var1.setReturnValue(false);
            }
         }
      }
   }

   @Inject(
      method = {"pushOutOfBlocks"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onPushOutOfBlocks(double var1, double var3, CallbackInfo var5) {
      if (((ClientPlayerEntity)this).equals(this.client.player)) {
         PlayerVelocityEvent var6 = (PlayerVelocityEvent) Boze.EVENT_BUS.post(PlayerVelocityEvent.method1048(false));
         if (var6.method1022()) {
            var5.cancel();
         }
      }
   }

   @Unique
   private static boolean lambda$sendMovementPackets$0(ItemStack var0) {
      return var0.isEmpty();
   }
}
