package dev.boze.client.systems.modules.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.*;
import dev.boze.client.mixin.PlayerEntityAccessor;
import dev.boze.client.mixin.PlayerInteractEntityC2SPacketAccessor;
import dev.boze.client.mixininterfaces.IPlayerInteractEntityC2SPacket;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.BackTrack;
import dev.boze.client.systems.modules.combat.Criticals;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.fakeplayer.FakeClientPlayerEntity;
import dev.boze.client.utils.fakeplayer.FakePlayerEntityWithTotem;
import dev.boze.client.utils.fakeplayer.FakePositions;
import mapped.Class5914;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

public class FakePlayer extends Module {
   public static final FakePlayer INSTANCE = new FakePlayer();
   public final BooleanSetting move = new BooleanSetting("Move", false, "Move the player along path");
   public final BooleanSetting loop = new BooleanSetting("Loop", true, "Loop the path");
   private final BindSetting recordPositions = new BindSetting("Record", Bind.create(), "Bind to record path", this.move);
   private final BooleanSetting damage = new BooleanSetting("Damage", true, "Apply damage to player");
   private final BooleanSetting gApple = new BooleanSetting("GApple", true, "Make player eat gapples");
   private final FloatSetting gAppleDelay = new FloatSetting("Delay", 1.5F, 1.5F, 5.0F, 0.1F, "Delay for eating gapples", this.gApple);
   public final List<FakePositions> positions = new ArrayList();
   public final dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();
   public FakeClientPlayerEntity fakePlayer = null;
   public int field2946;

   public FakePlayer() {
      super("FakePlayer", "Spawns a fake player", Category.Misc);
      this.field435 = true;
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientReadyForSinglePlayer()) {
         this.toggle();
      } else {
         GameProfile var4 = new GameProfile(new UUID(1L, 1L), "FakePlayer");
         this.field2946 = 0;
         this.fakePlayer = (FakeClientPlayerEntity)this.method1712(var4, FakePlayerEntityWithTotem::new);
         this.fakePlayer.method561(this.damage::getValue);
      }
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive()) {
         mc.world.removeEntity(this.fakePlayer.getId(), RemovalReason.DISCARDED);
         this.fakePlayer = null;
      }
   }

   @EventHandler
   private void method1705(KeyEvent var1) {
      if (this.recordPositions.method476().matches(true, var1.key) && var1.action == KeyAction.Press && mc.currentScreen == null) {
         this.positions.clear();
      }
   }

   @EventHandler
   private void method1706(MouseButtonEvent var1) {
      if (this.recordPositions.method476().matches(false, var1.button) && var1.action == KeyAction.Press && mc.currentScreen == null) {
         this.positions.clear();
      }
   }

   @EventHandler
   public void method1707(PreTickEvent event) {
      if (MinecraftUtils.isClientActive() && this.fakePlayer != null) {
         if ((double)mc.player.distanceTo(this.fakePlayer) > (double)Class5914.method2010() * 16.0) {
            this.setEnabled(false);
         }
      } else {
         this.setEnabled(false);
      }
   }

   @EventHandler
   private void method1708(MovementEvent var1) {
      if (this.gApple.getValue() && this.timer.hasElapsed((double)(this.gAppleDelay.getValue() * 1000.0F))) {
         this.fakePlayer.setAbsorptionAmount(16.0F);
         this.fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
         this.fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 6000, 0));
         this.fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 6000, 0));
         this.fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, 3));
         this.timer.reset();
      }

      if (this.recordPositions.method476().isPressed() && mc.currentScreen == null) {
         this.positions.add(new FakePositions(mc.player));
      } else if (this.move.getValue() && !this.positions.isEmpty() && (!BackTrack.INSTANCE.isEnabled() || BackTrack.INSTANCE.field1028 < 0)) {
         if (this.field2946 >= this.positions.size()) {
            this.field2946 = 0;
            if (!this.loop.getValue()) {
               this.move.setValue(false);
               return;
            }
         }

         FakePositions var5 = (FakePositions)this.positions.get(this.field2946);
         this.fakePlayer.field1305 = var5;
         this.field2946++;
      }
   }

   @EventHandler
   private void method1709(PacketBundleEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if (var1.packet instanceof ExplosionS2CPacket var5) {
            mc.send(this::lambda$onPacketReceive$0);
         }
      }
   }

   private void method1710(ExplosionS2CPacket var1) {
      if (mc.world != null && this.fakePlayer != null && this.isEnabled()) {
         double var5 = var1.getX();
         double var7 = var1.getY();
         double var9 = var1.getZ();
         Vec3d var11 = new Vec3d(var5, var7, var9);
         double var12 = this.fakePlayer.getPos().distanceTo(var11) / 12.0;
         if (!(var12 > 1.0)) {
            float var14 = var1.getRadius();
            double var15 = (double)Explosion.getExposure(var11, this.fakePlayer);
            double var21;
            double var17 = var21 = (1.0 - var12) * var15;
            float var19 = (float)((var17 * var17 + var21) / 2.0 * 7.0 * (double)var14 * 2.0 + 1.0);
            DamageSource var20 = mc.world
               .getDamageSources()
               .explosion(new Explosion(mc.world, mc.player, var5, var7, var9, var14, false, DestructionType.DESTROY));
            this.fakePlayer.damage(var20, var19);
         }
      }
   }

   @EventHandler
   private void method1711(PrePacketSendEvent var1) {
      if (var1.packet instanceof IPlayerInteractEntityC2SPacket var5
         && var5.boze$getType() == InteractType.ATTACK
         && this.fakePlayer.getId() == ((PlayerInteractEntityC2SPacketAccessor)var1.packet).getEntityId()) {
         var1.method1020();
         if (!Criticals.INSTANCE.isEnabled()
            && (
               mc.player.isSprinting()
                  || !(mc.player.fallDistance > 0.0F)
                  || mc.player.isOnGround()
                  || mc.player.isHoldingOntoLadder()
                  || mc.player.isSubmergedInWater()
                  || mc.player.isRiding()
            )) {
            if ((double)mc.player.getAttackCooldownProgress(0.5F) > 0.9) {
               mc.world
                  .playSound(
                     mc.player,
                     mc.player.getX(),
                     mc.player.getY(),
                     mc.player.getZ(),
                     SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                     mc.player.getSoundCategory(),
                     1.0F,
                     1.0F
                  );
            } else {
               mc.world
                  .playSound(
                     mc.player,
                     mc.player.getX(),
                     mc.player.getY(),
                     mc.player.getZ(),
                     SoundEvents.ENTITY_PLAYER_ATTACK_WEAK,
                     mc.player.getSoundCategory(),
                     1.0F,
                     1.0F
                  );
            }
         } else {
            mc.world
               .playSound(
                  mc.player,
                  mc.player.getX(),
                  mc.player.getY(),
                  mc.player.getZ(),
                  SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                  mc.player.getSoundCategory(),
                  1.0F,
                  1.0F
               );
         }
      }
   }

   private OtherClientPlayerEntity method1712(GameProfile var1, BiFunction<ClientWorld, GameProfile, OtherClientPlayerEntity> var2) {
      if (!MinecraftUtils.isClientActive()) {
         return null;
      } else {
         OtherClientPlayerEntity var6 = method1713(var1, var2);
         int var7 = -420;

         while (mc.world.getEntityById(var7) != null) {
            var7 = -ThreadLocalRandom.current().nextInt(420, 69420);
         }

         var6.setId(var7);
         mc.world.addEntity(var6);
         return var6;
      }
   }

   private static OtherClientPlayerEntity method1713(GameProfile var0, BiFunction<ClientWorld, GameProfile, OtherClientPlayerEntity> var1) {
      OtherClientPlayerEntity var5 = (OtherClientPlayerEntity)var1.apply(mc.world, var0);
      ((PlayerEntityAccessor)var5).setInventory(mc.player.getInventory());
      ((PlayerEntityAccessor)var5).setPlayerScreenHandler(mc.player.playerScreenHandler);
      var5.updatePositionAndAngles(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch());
      var5.setHeadYaw(mc.player.getHeadYaw());
      var5.prevHeadYaw = mc.player.getHeadYaw();
      var5.setYaw(mc.player.getYaw());
      var5.prevYaw = mc.player.getYaw();
      var5.setBodyYaw(mc.player.bodyYaw);
      var5.prevBodyYaw = mc.player.bodyYaw;
      var5.setPitch(mc.player.getPitch());
      var5.prevPitch = mc.player.getPitch();
      var5.setOnGround(mc.player.isOnGround());
      var5.setSneaking(mc.player.isSneaking());
      var5.setHealth(mc.player.getHealth());
      var5.setAbsorptionAmount(mc.player.getAbsorptionAmount());

      for (StatusEffectInstance var7 : mc.player.getActiveStatusEffects().values()) {
         var5.addStatusEffect(var7);
      }

      return var5;
   }

   public JsonObject recordPositions() {
      JsonObject var4 = new JsonObject();
      JsonArray var5 = new JsonArray();

      for (FakePositions var7 : this.positions) {
         JsonObject var8 = new JsonObject();
         var8.addProperty("x", var7.method2174().x);
         var8.addProperty("y", var7.method2174().y);
         var8.addProperty("z", var7.method2174().z);
         var8.addProperty("yaw", var7.method2175());
         var8.addProperty("pitch", var7.method2176());
         var8.addProperty("head", var7.method2177());
         var5.add(var8);
      }

      var4.add("positions", var5);
      return var4;
   }

   public void method1714(JsonObject json) {
      this.positions.clear();
      JsonArray var5 = json.getAsJsonArray("positions");

      for (int var6 = 0; var6 < var5.size(); var6++) {
         JsonObject var7 = var5.get(var6).getAsJsonObject();
         Vec3d var8 = new Vec3d(var7.get("x").getAsDouble(), var7.get("y").getAsDouble(), var7.get("z").getAsDouble());
         float var9 = var7.get("yaw").getAsFloat();
         float var10 = var7.get("pitch").getAsFloat();
         float var11 = var7.get("head").getAsFloat();
         FakePositions var12 = new FakePositions(var8, var9, var10, var11);
         this.positions.add(var12);
      }
   }

   private void lambda$onPacketReceive$0(ExplosionS2CPacket var1) {
      this.method1710(var1);
   }
}
