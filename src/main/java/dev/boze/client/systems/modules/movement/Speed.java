package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.SpeedMode;
import dev.boze.client.events.*;
import dev.boze.client.mixin.LivingEntityAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.fakeplayer.FakeClientPlayerEntity;
import dev.boze.client.utils.player.RotationHandler;
import mapped.Class3076;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Box;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Speed extends Module {
   public static final Speed INSTANCE = new Speed();
   private final EnumSetting<SpeedMode> field715 = new EnumSetting<SpeedMode>(
      "Mode",
      SpeedMode.Strafe,
      "Mode for speed\n - Vanilla: Vanilla speed\n - Strafe: Generic strafe, optimized for NCP\n - Grim: Strafe from Grim\n - BHop: Generic BHop, customizable speed\n - OnGround: OnGround (packet) speed\n"
   );
   private final MinMaxSetting field716 = new MinMaxSetting("EntityBoost", 0.0, 0.0, 0.5, 0.05, "Boost when passing by entities", this::lambda$new$0);
   private final FloatSetting field717 = new FloatSetting("Speed", 1.0F, 0.1F, 50.0F, 0.02F, "Speed multiplier", this::lambda$new$1);
   private final MinMaxSetting field718 = new MinMaxSetting("AccelSpeed", 1.0, 0.05, 1.0, 0.05, "Acceleration speed", this::lambda$new$2);
   private final BooleanSetting field719 = new BooleanSetting("Strict", true, "Makes speed work better on stricter servers", this::lambda$new$3);
   private final FloatSetting field720 = new FloatSetting("HopReduction", 0.0F, 0.0F, 1.0F, 0.05F, "Hop reduction", this::lambda$new$4);
   private final IntSetting field721 = new IntSetting("HopPulse", 0, 0, 5, 1, "Hop pulse", this::lambda$new$5);
   private final BooleanSetting field722 = new BooleanSetting("UseTimer", true, "Use timer to go slightly faster", this::lambda$new$6);
   private final BooleanSetting field723 = new BooleanSetting("CrystalBoost", false, "Use crystal explosions to go faster momentarily", this::lambda$new$7);
   private final MinMaxSetting field724 = new MinMaxSetting("Factor", 1.0, 0.1, 2.0, 0.05, "Boost factor", this.field723);
   private final BooleanSetting field725 = new BooleanSetting("NoJump", false, "Don't jump", this::lambda$new$8);
   private final BooleanSetting field726 = new BooleanSetting("WhileSneaking", false, "Speed while sneaking", this::lambda$new$9);
   private final BooleanSetting field727 = new BooleanSetting("InLiquid", true, "Speed in liquids");
   private final BooleanSetting field728 = new BooleanSetting("InWebs", true, "Speed in webs");
   private final BooleanSetting field729 = new BooleanSetting("AvoidUnloaded", true, "Avoid flying into unloaded chunks", this::lambda$new$10);
   private final IntSetting field730 = new IntSetting(
      "Ticks", 5, 1, 20, 1, "Amount of ticks to simulate\nHigher value = less chance of rubber-banding", this::lambda$new$11, this.field729
   );
   private double field731 = 0.0;
   private double field732 = 0.0;
   private boolean field733 = false;
   private int field734 = 4;
   private int field735;
   private int field736 = 0;
   private int field737 = 0;
   private final Timer field738 = new Timer();
   private double field739 = 0.0;
   private Timer field740 = new Timer();
   private final Timer field741 = new Timer();
   public static boolean field742 = false;
   private float field743;
   private float aa;
   private float ab;
   private boolean ac = false;
   private double ad;

   public Speed() {
      super("Speed", "Makes you move faster", Category.Movement);
   }

   private boolean method1971() {
      Box var4 = mc.player.getBoundingBox().withMinY(this.ad + 2.0).withMaxY(this.ad + 2.21);
      Box var5 = mc.player.getBoundingBox().withMinY(this.ad).withMaxY(this.ad + 1.8);
      return !mc.world.isSpaceEmpty(var4) && mc.world.isSpaceEmpty(var5);
   }

   @EventHandler(
      priority = 25
   )
   public void method1871(PlayerGrimV3BypassEvent event) {
      if (this.field715.method461() == SpeedMode.Grim
         || (this.field715.method461() == SpeedMode.Strafe || this.field715.method461() == SpeedMode.BHop)
            && this.method1971()
            && (this.field743 != 0.0F || this.aa != 0.0F)) {
         if (!mc.player.isSneaking()) {
            if (event.method1022()) {
               this.ab = -420.0F;
            } else {
               this.ab = mc.player.getYaw();
               if (this.field743 == 0.0F && this.aa == 0.0F) {
                  this.ab = -420.0F;
               } else {
                  if (this.field743 != 0.0F) {
                     if (this.aa >= 1.0F) {
                        this.ab = this.ab + (float)(this.field743 > 0.0F ? -45 : 45);
                     } else if (this.aa <= -1.0F) {
                        this.ab = this.ab + (float)(this.field743 > 0.0F ? 45 : -45);
                     }

                     if (this.field743 < 0.0F) {
                        this.ab += 180.0F;
                     }
                  } else if (this.aa != 0.0F) {
                     if (this.aa >= 1.0F) {
                        this.ab += -90.0F;
                     } else if (this.aa <= 1.0F) {
                        this.ab += 90.0F;
                     }
                  }

                  this.ab %= 360.0F;

                  try {
                     mc.player.setSprinting(true);
                  } catch (Exception var6) {
                  }

                  event.yaw = this.ab;
                  event.method1021(true);
               }
            }
         }
      }
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      if (this.field715.method461() == SpeedMode.Grim && this.field716.getValue() > 0.0) {
         for (Entity var6 : mc.world.getEntities()) {
            if (var6 instanceof LivingEntity
               && var6 != mc.player
               && !(var6 instanceof ArmorStandEntity)
               && !(var6 instanceof FakePlayerEntity)
               && !(var6 instanceof FakeClientPlayerEntity)
               && (double)var6.distanceTo(mc.player) <= 1.5) {
               mc.player
                  .setVelocity(
                     mc.player.getVelocity().x * (1.0 + this.field716.getValue()),
                     mc.player.getVelocity().y,
                     mc.player.getVelocity().z * (1.0 + this.field716.getValue())
                  );
               return;
            }
         }
      }
   }

   @EventHandler
   private void method1810(PostTickEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if ((this.field715.method461() == SpeedMode.Strafe || this.field715.method461() == SpeedMode.BHop) && this.method1971()) {
            ((LivingEntityAccessor)mc.player).setJumpCooldown(0);
            this.field741.reset();
            this.ac = true;
         }
      }
   }

   @EventHandler(
      priority = 25
   )
   public void method1872(TickInputPostEvent event) {
      if (!RotationHandler.field1546.method2114()) {
         if (!mc.player.isSneaking()) {
            if (this.field715.method461() == SpeedMode.Grim
               || (this.field715.method461() == SpeedMode.Strafe || this.field715.method461() == SpeedMode.BHop)
                  && this.method1971()
                  && (event.field1954 != 0.0F || event.field1953 != 0.0F)) {
               this.field743 = event.field1954;
               this.aa = event.field1953;
               if (event.field1954 != 0.0F || event.field1953 != 0.0F) {
                  event.field1954 = 1.0F;
                  if (!this.field725.method419() || this.field715.method461() != SpeedMode.Grim) {
                     event.field1955 = true;
                  }
               }

               event.field1953 = 0.0F;
            }
         }
      }
   }

   @EventHandler(
      priority = 40
   )
   public void method1893(PlayerMoveEvent event) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 1
      // 001: getfield dev/boze/client/events/PlayerMoveEvent.field1892 Z
      // 004: ifeq 008
      // 007: return
      // 008: aload 0
      // 009: getfield dev/boze/client/systems/modules/movement/Speed.field726 Ldev/boze/client/settings/BooleanSetting;
      // 00c: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 00f: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 012: ifne 022
      // 015: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 018: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 01b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isSneaking ()Z
      // 01e: ifeq 022
      // 021: return
      // 022: aload 0
      // 023: getfield dev/boze/client/systems/modules/movement/Speed.field727 Ldev/boze/client/settings/BooleanSetting;
      // 026: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 029: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 02c: ifne 039
      // 02f: ldc_w net/minecraft/block/FluidBlock
      // 032: invokestatic mapped/Class5924.method91 (Ljava/lang/Class;)Z
      // 035: ifeq 039
      // 038: return
      // 039: aload 0
      // 03a: getfield dev/boze/client/systems/modules/movement/Speed.field728 Ldev/boze/client/settings/BooleanSetting;
      // 03d: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 040: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 043: ifne 050
      // 046: ldc_w net/minecraft/block/CobwebBlock
      // 049: invokestatic mapped/Class5924.method91 (Ljava/lang/Class;)Z
      // 04c: ifeq 050
      // 04f: return
      // 050: aload 0
      // 051: getfield dev/boze/client/systems/modules/movement/Speed.field738 Ldev/boze/client/utils/Timer;
      // 054: ldc2_w 350.0
      // 057: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // 05a: ifne 05e
      // 05d: return
      // 05e: aload 0
      // 05f: getfield dev/boze/client/systems/modules/movement/Speed.field715 Ldev/boze/client/settings/EnumSetting;
      // 062: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 065: getstatic dev/boze/client/enums/SpeedMode.Grim Ldev/boze/client/enums/SpeedMode;
      // 068: if_acmpne 06c
      // 06b: return
      // 06c: aload 0
      // 06d: getfield dev/boze/client/systems/modules/movement/Speed.ac Z
      // 070: ifeq 088
      // 073: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 076: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 079: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 07c: ifeq 087
      // 07f: aload 0
      // 080: bipush 0
      // 081: putfield dev/boze/client/systems/modules/movement/Speed.ac Z
      // 084: goto 088
      // 087: return
      // 088: aload 1
      // 089: getfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // 08c: getfield net/minecraft/util/math/Vec3d.x D
      // 08f: dstore 5
      // 091: aload 1
      // 092: getfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // 095: getfield net/minecraft/util/math/Vec3d.y D
      // 098: dstore 7
      // 09a: aload 1
      // 09b: getfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // 09e: getfield net/minecraft/util/math/Vec3d.z D
      // 0a1: dstore 9
      // 0a3: getstatic dev/boze/client/systems/modules/movement/AutoWalk.INSTANCE Ldev/boze/client/systems/modules/movement/AutoWalk;
      // 0a6: invokevirtual dev/boze/client/systems/modules/Module.isEnabled ()Z
      // 0a9: ifeq 0d4
      // 0ac: getstatic dev/boze/client/systems/modules/movement/AutoWalk.INSTANCE Ldev/boze/client/systems/modules/movement/AutoWalk;
      // 0af: getfield dev/boze/client/systems/modules/movement/AutoWalk.field3148 Ldev/boze/client/settings/EnumSetting;
      // 0b2: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0b5: getstatic dev/boze/client/enums/AutoWalkMode.Baritone Ldev/boze/client/enums/AutoWalkMode;
      // 0b8: if_acmpeq 0d4
      // 0bb: getstatic dev/boze/client/systems/modules/movement/AutoWalk.INSTANCE Ldev/boze/client/systems/modules/movement/AutoWalk;
      // 0be: getfield dev/boze/client/systems/modules/movement/AutoWalk.field3147 Ldev/boze/client/settings/BooleanSetting;
      // 0c1: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 0c4: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 0c7: ifeq 0d0
      // 0ca: ldc_w -1.0
      // 0cd: goto 0e0
      // 0d0: fconst_1
      // 0d1: goto 0e0
      // 0d4: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 0d7: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0da: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 0dd: getfield net/minecraft/client/input/Input.movementForward F
      // 0e0: fstore 11
      // 0e2: aload 0
      // 0e3: getfield dev/boze/client/systems/modules/movement/Speed.field715 Ldev/boze/client/settings/EnumSetting;
      // 0e6: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0e9: checkcast dev/boze/client/enums/SpeedMode
      // 0ec: invokevirtual dev/boze/client/enums/SpeedMode.ordinal ()I
      // 0ef: tableswitch 2900 0 4 33 380 2900 1902 2474
      // 110: aload 0
      // 111: getfield dev/boze/client/systems/modules/movement/Speed.field717 Ldev/boze/client/settings/FloatSetting;
      // 114: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 117: invokevirtual java/lang/Float.floatValue ()F
      // 11a: ldc_w 10.0
      // 11d: fmul
      // 11e: f2d
      // 11f: invokestatic mapped/Class5924.method95 (D)Lnet/minecraft/util/math/Vec3d;
      // 122: astore 12
      // 124: aload 12
      // 126: invokevirtual net/minecraft/util/math/Vec3d.getX ()D
      // 129: dstore 5
      // 12b: aload 12
      // 12d: invokevirtual net/minecraft/util/math/Vec3d.getZ ()D
      // 130: dstore 9
      // 132: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 135: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 138: getstatic net/minecraft/entity/effect/StatusEffects.SPEED Lnet/minecraft/registry/entry/RegistryEntry;
      // 13b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.hasStatusEffect (Lnet/minecraft/registry/entry/RegistryEntry;)Z
      // 13e: ifeq 16d
      // 141: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 144: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 147: getstatic net/minecraft/entity/effect/StatusEffects.SPEED Lnet/minecraft/registry/entry/RegistryEntry;
      // 14a: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getStatusEffect (Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;
      // 14d: invokevirtual net/minecraft/entity/effect/StatusEffectInstance.getAmplifier ()I
      // 150: bipush 1
      // 151: iadd
      // 152: i2d
      // 153: ldc2_w 0.205
      // 156: dmul
      // 157: dstore 13
      // 159: dload 5
      // 15b: dload 5
      // 15d: dload 13
      // 15f: dmul
      // 160: dadd
      // 161: dstore 5
      // 163: dload 9
      // 165: dload 9
      // 167: dload 13
      // 169: dmul
      // 16a: dadd
      // 16b: dstore 9
      // 16d: aload 0
      // 16e: getfield dev/boze/client/systems/modules/movement/Speed.field729 Ldev/boze/client/settings/BooleanSetting;
      // 171: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 174: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 177: ifeq 1d1
      // 17a: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 17d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 180: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 183: dload 5
      // 185: aload 0
      // 186: getfield dev/boze/client/systems/modules/movement/Speed.field730 Ldev/boze/client/settings/IntSetting;
      // 189: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 18c: invokevirtual java/lang/Integer.intValue ()I
      // 18f: i2d
      // 190: dmul
      // 191: dadd
      // 192: invokestatic net/minecraft/util/math/ChunkSectionPos.getSectionCoord (D)I
      // 195: istore 13
      // 197: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 19a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 19d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 1a0: dload 9
      // 1a2: aload 0
      // 1a3: getfield dev/boze/client/systems/modules/movement/Speed.field730 Ldev/boze/client/settings/IntSetting;
      // 1a6: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 1a9: invokevirtual java/lang/Integer.intValue ()I
      // 1ac: i2d
      // 1ad: dmul
      // 1ae: dadd
      // 1af: invokestatic net/minecraft/util/math/ChunkSectionPos.getSectionCoord (D)I
      // 1b2: istore 14
      // 1b4: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 1b7: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 1ba: invokevirtual net/minecraft/client/world/ClientWorld.getChunkManager ()Lnet/minecraft/client/world/ClientChunkManager;
      // 1bd: iload 13
      // 1bf: iload 14
      // 1c1: invokevirtual net/minecraft/client/world/ClientChunkManager.isChunkLoaded (II)Z
      // 1c4: istore 15
      // 1c6: iload 15
      // 1c8: ifne 1d1
      // 1cb: dconst_0
      // 1cc: dstore 5
      // 1ce: dconst_0
      // 1cf: dstore 9
      // 1d1: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 1d4: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1d7: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 1da: astore 13
      // 1dc: aload 0
      // 1dd: getfield dev/boze/client/systems/modules/movement/Speed.field718 Ldev/boze/client/settings/MinMaxSetting;
      // 1e0: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 1e3: invokevirtual java/lang/Double.doubleValue ()D
      // 1e6: dconst_1
      // 1e7: dcmpg
      // 1e8: ifge 236
      // 1eb: aload 13
      // 1ed: dload 5
      // 1ef: dload 7
      // 1f1: dload 9
      // 1f3: invokevirtual net/minecraft/util/math/Vec3d.subtract (DDD)Lnet/minecraft/util/math/Vec3d;
      // 1f6: invokevirtual net/minecraft/util/math/Vec3d.length ()D
      // 1f9: ldc2_w 0.5
      // 1fc: dcmpl
      // 1fd: ifle 236
      // 200: aload 13
      // 202: getfield net/minecraft/util/math/Vec3d.x D
      // 205: dload 5
      // 207: aload 13
      // 209: getfield net/minecraft/util/math/Vec3d.x D
      // 20c: dsub
      // 20d: aload 0
      // 20e: getfield dev/boze/client/systems/modules/movement/Speed.field718 Ldev/boze/client/settings/MinMaxSetting;
      // 211: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 214: invokevirtual java/lang/Double.doubleValue ()D
      // 217: dmul
      // 218: dadd
      // 219: dstore 5
      // 21b: aload 13
      // 21d: getfield net/minecraft/util/math/Vec3d.z D
      // 220: dload 9
      // 222: aload 13
      // 224: getfield net/minecraft/util/math/Vec3d.z D
      // 227: dsub
      // 228: aload 0
      // 229: getfield dev/boze/client/systems/modules/movement/Speed.field718 Ldev/boze/client/settings/MinMaxSetting;
      // 22c: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 22f: invokevirtual java/lang/Double.doubleValue ()D
      // 232: dmul
      // 233: dadd
      // 234: dstore 9
      // 236: aload 1
      // 237: new net/minecraft/util/math/Vec3d
      // 23a: dup
      // 23b: dload 5
      // 23d: dload 7
      // 23f: dload 9
      // 241: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 244: putfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // 247: aload 1
      // 248: bipush 1
      // 249: putfield dev/boze/client/events/PlayerMoveEvent.field1892 Z
      // 24c: aload 0
      // 24d: getfield dev/boze/client/systems/modules/movement/Speed.field718 Ldev/boze/client/settings/MinMaxSetting;
      // 250: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 253: invokevirtual java/lang/Double.doubleValue ()D
      // 256: dconst_1
      // 257: dcmpg
      // 258: ifge 268
      // 25b: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 25e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 261: aload 1
      // 262: getfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // 265: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (Lnet/minecraft/util/math/Vec3d;)V
      // 268: goto c43
      // 26b: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 26e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 271: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 274: ifeq 284
      // 277: aload 0
      // 278: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 27b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 27e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 281: putfield dev/boze/client/systems/modules/movement/Speed.ad D
      // 284: aload 0
      // 285: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1971 ()Z
      // 288: ifeq 29d
      // 28b: aload 0
      // 28c: bipush 4
      // 28d: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 290: aload 0
      // 291: invokestatic mapped/Class5924.method2091 ()D
      // 294: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 297: aload 0
      // 298: dconst_0
      // 299: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 29c: return
      // 29d: aload 0
      // 29e: getfield dev/boze/client/systems/modules/movement/Speed.field719 Ldev/boze/client/settings/BooleanSetting;
      // 2a1: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 2a4: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 2a7: ifeq 636
      // 2aa: aload 0
      // 2ab: aload 0
      // 2ac: getfield dev/boze/client/systems/modules/movement/Speed.field735 I
      // 2af: bipush 1
      // 2b0: iadd
      // 2b1: putfield dev/boze/client/systems/modules/movement/Speed.field735 I
      // 2b4: aload 0
      // 2b5: aload 0
      // 2b6: getfield dev/boze/client/systems/modules/movement/Speed.field735 I
      // 2b9: bipush 5
      // 2ba: irem
      // 2bb: putfield dev/boze/client/systems/modules/movement/Speed.field735 I
      // 2be: aload 0
      // 2bf: getfield dev/boze/client/systems/modules/movement/Speed.field735 I
      // 2c2: ifeq 2cc
      // 2c5: aload 0
      // 2c6: invokestatic mapped/Class3076.method6025 (Ldev/boze/client/systems/modules/Module;)V
      // 2c9: goto 31d
      // 2cc: invokestatic mapped/Class5924.method2116 ()Z
      // 2cf: ifeq 31d
      // 2d2: aload 0
      // 2d3: getfield dev/boze/client/systems/modules/movement/Speed.field722 Ldev/boze/client/settings/BooleanSetting;
      // 2d6: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 2d9: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 2dc: ifeq 2e8
      // 2df: aload 0
      // 2e0: bipush 10
      // 2e2: ldc_w 1.44
      // 2e5: invokestatic mapped/Class3076.method6024 (Ldev/boze/client/systems/modules/Module;IF)V
      // 2e8: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 2eb: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2ee: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 2f1: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2f4: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 2f7: getfield net/minecraft/util/math/Vec3d.x D
      // 2fa: ldc2_w 1.0199999809265137
      // 2fd: dmul
      // 2fe: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 301: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 304: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 307: getfield net/minecraft/util/math/Vec3d.y D
      // 30a: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 30d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 310: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 313: getfield net/minecraft/util/math/Vec3d.z D
      // 316: ldc2_w 1.0199999809265137
      // 319: dmul
      // 31a: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 31d: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 320: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 323: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 326: ifeq 334
      // 329: invokestatic mapped/Class5924.method2116 ()Z
      // 32c: ifeq 334
      // 32f: aload 0
      // 330: bipush 2
      // 331: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 334: aload 0
      // 335: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 338: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 33b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 33e: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 341: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 344: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 347: d2i
      // 348: i2d
      // 349: dsub
      // 34a: invokevirtual dev/boze/client/systems/modules/movement/Speed.method355 (D)D
      // 34d: aload 0
      // 34e: ldc2_w 0.138
      // 351: invokevirtual dev/boze/client/systems/modules/movement/Speed.method355 (D)D
      // 354: dcmpl
      // 355: ifne 3b9
      // 358: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 35b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 35e: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 361: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 364: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 367: getfield net/minecraft/util/math/Vec3d.x D
      // 36a: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 36d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 370: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 373: getfield net/minecraft/util/math/Vec3d.y D
      // 376: ldc2_w 0.08
      // 379: dsub
      // 37a: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 37d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 380: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 383: getfield net/minecraft/util/math/Vec3d.z D
      // 386: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 389: dload 7
      // 38b: ldc2_w 0.09316090325960147
      // 38e: dsub
      // 38f: dstore 7
      // 391: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 394: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 397: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 39a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 39d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 3a0: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 3a3: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 3a6: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 3a9: ldc2_w 0.09316090325960147
      // 3ac: dsub
      // 3ad: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 3b0: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 3b3: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 3b6: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setPos (DDD)V
      // 3b9: aload 0
      // 3ba: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 3bd: bipush 1
      // 3be: if_icmpne 3f0
      // 3c1: fload 11
      // 3c3: fconst_0
      // 3c4: fcmpl
      // 3c5: ifne 3d9
      // 3c8: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 3cb: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 3ce: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 3d1: getfield net/minecraft/client/input/Input.movementSideways F
      // 3d4: fconst_0
      // 3d5: fcmpl
      // 3d6: ifeq 3f0
      // 3d9: aload 0
      // 3da: bipush 2
      // 3db: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 3de: aload 0
      // 3df: ldc2_w 1.38
      // 3e2: invokestatic mapped/Class5924.method2091 ()D
      // 3e5: dmul
      // 3e6: ldc2_w 0.01
      // 3e9: dsub
      // 3ea: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 3ed: goto 4b5
      // 3f0: aload 0
      // 3f1: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 3f4: bipush 2
      // 3f5: if_icmpne 444
      // 3f8: aload 0
      // 3f9: bipush 3
      // 3fa: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 3fd: aload 0
      // 3fe: ldc2_w 0.399399995803833
      // 401: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1389 (D)D
      // 404: dstore 7
      // 406: aload 0
      // 407: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1972 ()Z
      // 40a: ifne 412
      // 40d: ldc2_w 0.2
      // 410: dstore 7
      // 412: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 415: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 418: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 41b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 41e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 421: getfield net/minecraft/util/math/Vec3d.x D
      // 424: dload 7
      // 426: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 429: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 42c: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 42f: getfield net/minecraft/util/math/Vec3d.z D
      // 432: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 435: aload 0
      // 436: aload 0
      // 437: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 43a: ldc2_w 2.149
      // 43d: dmul
      // 43e: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 441: goto 4b5
      // 444: aload 0
      // 445: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 448: bipush 3
      // 449: if_icmpne 46d
      // 44c: aload 0
      // 44d: bipush 4
      // 44e: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 451: ldc2_w 0.66
      // 454: aload 0
      // 455: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 458: invokestatic mapped/Class5924.method2091 ()D
      // 45b: dsub
      // 45c: dmul
      // 45d: dstore 12
      // 45f: aload 0
      // 460: aload 0
      // 461: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 464: dload 12
      // 466: dsub
      // 467: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 46a: goto 4b5
      // 46d: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 470: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 473: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 476: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 479: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 47c: dconst_0
      // 47d: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 480: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 483: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 486: getfield net/minecraft/util/math/Vec3d.y D
      // 489: dconst_0
      // 48a: invokevirtual net/minecraft/util/math/Box.offset (DDD)Lnet/minecraft/util/math/Box;
      // 48d: invokevirtual net/minecraft/client/world/ClientWorld.isSpaceEmpty (Lnet/minecraft/util/math/Box;)Z
      // 490: ifeq 49f
      // 493: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 496: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 499: getfield net/minecraft/client/network/ClientPlayerEntity.verticalCollision Z
      // 49c: ifeq 4a4
      // 49f: aload 0
      // 4a0: bipush 1
      // 4a1: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 4a4: aload 0
      // 4a5: aload 0
      // 4a6: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4a9: aload 0
      // 4aa: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4ad: ldc2_w 159.0
      // 4b0: ddiv
      // 4b1: dsub
      // 4b2: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4b5: aload 0
      // 4b6: aload 0
      // 4b7: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4ba: invokestatic mapped/Class5924.method2091 ()D
      // 4bd: invokestatic java/lang/Math.max (DD)D
      // 4c0: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4c3: aload 0
      // 4c4: getfield dev/boze/client/systems/modules/movement/Speed.field739 D
      // 4c7: dconst_0
      // 4c8: dcmpl
      // 4c9: ifle 4f8
      // 4cc: aload 0
      // 4cd: getfield dev/boze/client/systems/modules/movement/Speed.field723 Ldev/boze/client/settings/BooleanSetting;
      // 4d0: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 4d3: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 4d6: ifeq 4f8
      // 4d9: aload 0
      // 4da: getfield dev/boze/client/systems/modules/movement/Speed.field740 Ldev/boze/client/utils/Timer;
      // 4dd: ldc2_w 75.0
      // 4e0: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // 4e3: ifne 4f8
      // 4e6: aload 0
      // 4e7: aload 0
      // 4e8: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4eb: aload 0
      // 4ec: getfield dev/boze/client/systems/modules/movement/Speed.field739 D
      // 4ef: invokestatic java/lang/Math.max (DD)D
      // 4f2: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 4f5: goto 522
      // 4f8: aload 0
      // 4f9: getfield dev/boze/client/systems/modules/movement/Speed.field719 Ldev/boze/client/settings/BooleanSetting;
      // 4fc: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 4ff: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 502: ifeq 522
      // 505: aload 0
      // 506: aload 0
      // 507: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 50a: aload 0
      // 50b: getfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // 50e: bipush 25
      // 510: if_icmple 519
      // 513: ldc2_w 0.449
      // 516: goto 51c
      // 519: ldc2_w 0.433
      // 51c: invokestatic java/lang/Math.min (DD)D
      // 51f: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 522: fload 11
      // 524: fstore 12
      // 526: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 529: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 52c: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 52f: getfield net/minecraft/client/input/Input.movementSideways F
      // 532: fstore 13
      // 534: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 537: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 53a: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // 53d: fstore 14
      // 53f: aload 0
      // 540: aload 0
      // 541: getfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // 544: bipush 1
      // 545: iadd
      // 546: putfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // 549: aload 0
      // 54a: getfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // 54d: bipush 50
      // 54f: if_icmple 557
      // 552: aload 0
      // 553: bipush 0
      // 554: putfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // 557: fload 12
      // 559: fconst_0
      // 55a: fcmpl
      // 55b: ifne 56e
      // 55e: fload 13
      // 560: fconst_0
      // 561: fcmpl
      // 562: ifne 56e
      // 565: dconst_0
      // 566: dstore 5
      // 568: dconst_0
      // 569: dstore 9
      // 56b: goto 5cf
      // 56e: fload 12
      // 570: fconst_0
      // 571: fcmpl
      // 572: ifeq 5cf
      // 575: fload 13
      // 577: fconst_1
      // 578: fcmpl
      // 579: iflt 596
      // 57c: fload 14
      // 57e: fload 12
      // 580: fconst_0
      // 581: fcmpl
      // 582: ifle 58a
      // 585: bipush -45
      // 587: goto 58c
      // 58a: bipush 45
      // 58c: i2f
      // 58d: fadd
      // 58e: fstore 14
      // 590: fconst_0
      // 591: fstore 13
      // 593: goto 5b6
      // 596: fload 13
      // 598: ldc_w -1.0
      // 59b: fcmpg
      // 59c: ifgt 5b6
      // 59f: fload 14
      // 5a1: fload 12
      // 5a3: fconst_0
      // 5a4: fcmpl
      // 5a5: ifle 5ad
      // 5a8: bipush 45
      // 5aa: goto 5af
      // 5ad: bipush -45
      // 5af: i2f
      // 5b0: fadd
      // 5b1: fstore 14
      // 5b3: fconst_0
      // 5b4: fstore 13
      // 5b6: fload 12
      // 5b8: fconst_0
      // 5b9: fcmpl
      // 5ba: ifle 5c3
      // 5bd: fconst_1
      // 5be: fstore 12
      // 5c0: goto 5cf
      // 5c3: fload 12
      // 5c5: fconst_0
      // 5c6: fcmpg
      // 5c7: ifge 5cf
      // 5ca: ldc_w -1.0
      // 5cd: fstore 12
      // 5cf: fload 14
      // 5d1: ldc_w 90.0
      // 5d4: fadd
      // 5d5: f2d
      // 5d6: invokestatic java/lang/Math.toRadians (D)D
      // 5d9: invokestatic java/lang/Math.cos (D)D
      // 5dc: dstore 15
      // 5de: fload 14
      // 5e0: ldc_w 90.0
      // 5e3: fadd
      // 5e4: f2d
      // 5e5: invokestatic java/lang/Math.toRadians (D)D
      // 5e8: invokestatic java/lang/Math.sin (D)D
      // 5eb: dstore 17
      // 5ed: fload 12
      // 5ef: f2d
      // 5f0: aload 0
      // 5f1: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 5f4: dmul
      // 5f5: dload 15
      // 5f7: dmul
      // 5f8: fload 13
      // 5fa: f2d
      // 5fb: aload 0
      // 5fc: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 5ff: dmul
      // 600: dload 17
      // 602: dmul
      // 603: dadd
      // 604: dstore 5
      // 606: fload 12
      // 608: f2d
      // 609: aload 0
      // 60a: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 60d: dmul
      // 60e: dload 17
      // 610: dmul
      // 611: fload 13
      // 613: f2d
      // 614: aload 0
      // 615: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 618: dmul
      // 619: dload 15
      // 61b: dmul
      // 61c: dsub
      // 61d: dstore 9
      // 61f: fload 12
      // 621: fconst_0
      // 622: fcmpl
      // 623: ifne 633
      // 626: fload 13
      // 628: fconst_0
      // 629: fcmpl
      // 62a: ifne 633
      // 62d: dconst_0
      // 62e: dstore 5
      // 630: dconst_0
      // 631: dstore 9
      // 633: goto 844
      // 636: aload 0
      // 637: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 63a: bipush 1
      // 63b: if_icmpne 656
      // 63e: fload 11
      // 640: fconst_0
      // 641: fcmpl
      // 642: ifeq 656
      // 645: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 648: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 64b: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 64e: getfield net/minecraft/client/input/Input.movementSideways F
      // 651: fconst_0
      // 652: fcmpl
      // 653: ifne 78f
      // 656: aload 0
      // 657: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 65a: bipush 2
      // 65b: if_icmpne 6ed
      // 65e: fload 11
      // 660: fconst_0
      // 661: fcmpl
      // 662: ifne 676
      // 665: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 668: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 66b: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 66e: getfield net/minecraft/client/input/Input.movementSideways F
      // 671: fconst_0
      // 672: fcmpl
      // 673: ifeq 6ed
      // 676: aload 0
      // 677: ldc2_w 0.3999
      // 67a: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1389 (D)D
      // 67d: dstore 12
      // 67f: aload 0
      // 680: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1972 ()Z
      // 683: ifne 68b
      // 686: ldc2_w 0.2
      // 689: dstore 12
      // 68b: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 68e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 691: new net/minecraft/util/math/Vec3d
      // 694: dup
      // 695: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 698: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 69b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 69e: getfield net/minecraft/util/math/Vec3d.x D
      // 6a1: dload 12
      // 6a3: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 6a6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 6a9: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 6ac: getfield net/minecraft/util/math/Vec3d.z D
      // 6af: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 6b2: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (Lnet/minecraft/util/math/Vec3d;)V
      // 6b5: dload 12
      // 6b7: dstore 7
      // 6b9: aload 0
      // 6ba: aload 0
      // 6bb: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 6be: aload 0
      // 6bf: getfield dev/boze/client/systems/modules/movement/Speed.field733 Z
      // 6c2: ifeq 6d7
      // 6c5: ldc2_w 1.6835
      // 6c8: aload 0
      // 6c9: getfield dev/boze/client/systems/modules/movement/Speed.field717 Ldev/boze/client/settings/FloatSetting;
      // 6cc: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 6cf: invokevirtual java/lang/Float.floatValue ()F
      // 6d2: f2d
      // 6d3: dmul
      // 6d4: goto 6e6
      // 6d7: ldc2_w 1.395
      // 6da: aload 0
      // 6db: getfield dev/boze/client/systems/modules/movement/Speed.field717 Ldev/boze/client/settings/FloatSetting;
      // 6de: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 6e1: invokevirtual java/lang/Float.floatValue ()F
      // 6e4: f2d
      // 6e5: dmul
      // 6e6: dmul
      // 6e7: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 6ea: goto 79e
      // 6ed: aload 0
      // 6ee: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 6f1: bipush 3
      // 6f2: if_icmpne 721
      // 6f5: ldc2_w 0.66
      // 6f8: aload 0
      // 6f9: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 6fc: invokestatic mapped/Class5924.method2091 ()D
      // 6ff: dsub
      // 700: dmul
      // 701: dstore 12
      // 703: aload 0
      // 704: aload 0
      // 705: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 708: dload 12
      // 70a: dsub
      // 70b: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 70e: aload 0
      // 70f: aload 0
      // 710: getfield dev/boze/client/systems/modules/movement/Speed.field733 Z
      // 713: ifne 71a
      // 716: bipush 1
      // 717: goto 71b
      // 71a: bipush 0
      // 71b: putfield dev/boze/client/systems/modules/movement/Speed.field733 Z
      // 71e: goto 79e
      // 721: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 724: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 727: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 72a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 72d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 730: dconst_0
      // 731: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 734: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 737: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 73a: getfield net/minecraft/util/math/Vec3d.y D
      // 73d: dconst_0
      // 73e: invokevirtual net/minecraft/util/math/Box.offset (DDD)Lnet/minecraft/util/math/Box;
      // 741: invokevirtual net/minecraft/client/world/ClientWorld.isSpaceEmpty (Lnet/minecraft/util/math/Box;)Z
      // 744: ifeq 753
      // 747: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 74a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 74d: getfield net/minecraft/client/network/ClientPlayerEntity.verticalCollision Z
      // 750: ifeq 77b
      // 753: aload 0
      // 754: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 757: ifle 77b
      // 75a: aload 0
      // 75b: fload 11
      // 75d: fconst_0
      // 75e: fcmpl
      // 75f: ifne 777
      // 762: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 765: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 768: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 76b: getfield net/minecraft/client/input/Input.movementSideways F
      // 76e: fconst_0
      // 76f: fcmpl
      // 770: ifne 777
      // 773: bipush 0
      // 774: goto 778
      // 777: bipush 1
      // 778: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 77b: aload 0
      // 77c: aload 0
      // 77d: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 780: aload 0
      // 781: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 784: ldc2_w 159.0
      // 787: ddiv
      // 788: dsub
      // 789: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 78c: goto 79e
      // 78f: aload 0
      // 790: ldc2_w 1.35
      // 793: invokestatic mapped/Class5924.method2091 ()D
      // 796: dmul
      // 797: ldc2_w 0.01
      // 79a: dsub
      // 79b: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 79e: aload 0
      // 79f: aload 0
      // 7a0: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 7a3: invokestatic mapped/Class5924.method2091 ()D
      // 7a6: invokestatic java/lang/Math.max (DD)D
      // 7a9: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 7ac: aload 0
      // 7ad: getfield dev/boze/client/systems/modules/movement/Speed.field739 D
      // 7b0: dconst_0
      // 7b1: dcmpl
      // 7b2: ifle 7e1
      // 7b5: aload 0
      // 7b6: getfield dev/boze/client/systems/modules/movement/Speed.field723 Ldev/boze/client/settings/BooleanSetting;
      // 7b9: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 7bc: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 7bf: ifeq 7e1
      // 7c2: aload 0
      // 7c3: getfield dev/boze/client/systems/modules/movement/Speed.field740 Ldev/boze/client/utils/Timer;
      // 7c6: ldc2_w 75.0
      // 7c9: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // 7cc: ifne 7e1
      // 7cf: aload 0
      // 7d0: aload 0
      // 7d1: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 7d4: aload 0
      // 7d5: getfield dev/boze/client/systems/modules/movement/Speed.field739 D
      // 7d8: invokestatic java/lang/Math.max (DD)D
      // 7db: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 7de: goto 80b
      // 7e1: aload 0
      // 7e2: getfield dev/boze/client/systems/modules/movement/Speed.field719 Ldev/boze/client/settings/BooleanSetting;
      // 7e5: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 7e8: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 7eb: ifeq 80b
      // 7ee: aload 0
      // 7ef: aload 0
      // 7f0: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 7f3: aload 0
      // 7f4: getfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // 7f7: bipush 25
      // 7f9: if_icmple 802
      // 7fc: ldc2_w 0.449
      // 7ff: goto 805
      // 802: ldc2_w 0.433
      // 805: invokestatic java/lang/Math.min (DD)D
      // 808: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 80b: aload 0
      // 80c: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 80f: invokestatic mapped/Class5924.method93 (D)Lnet/minecraft/util/math/Vec3d;
      // 812: astore 12
      // 814: aload 12
      // 816: getfield net/minecraft/util/math/Vec3d.x D
      // 819: dstore 5
      // 81b: aload 12
      // 81d: getfield net/minecraft/util/math/Vec3d.z D
      // 820: dstore 9
      // 822: fload 11
      // 824: fconst_0
      // 825: fcmpl
      // 826: ifne 83a
      // 829: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 82c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 82f: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 832: getfield net/minecraft/client/input/Input.movementSideways F
      // 835: fconst_0
      // 836: fcmpl
      // 837: ifeq 844
      // 83a: aload 0
      // 83b: aload 0
      // 83c: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 83f: bipush 1
      // 840: iadd
      // 841: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 844: aload 1
      // 845: new net/minecraft/util/math/Vec3d
      // 848: dup
      // 849: dload 5
      // 84b: dload 7
      // 84d: dload 9
      // 84f: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 852: putfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // 855: aload 1
      // 856: bipush 1
      // 857: putfield dev/boze/client/events/PlayerMoveEvent.field1892 Z
      // 85a: goto c43
      // 85d: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 860: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 863: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 866: ifeq 876
      // 869: aload 0
      // 86a: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 86d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 870: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 873: putfield dev/boze/client/systems/modules/movement/Speed.ad D
      // 876: aload 0
      // 877: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1971 ()Z
      // 87a: ifeq 88f
      // 87d: aload 0
      // 87e: bipush 4
      // 87f: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 882: aload 0
      // 883: invokestatic mapped/Class5924.method2091 ()D
      // 886: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 889: aload 0
      // 88a: dconst_0
      // 88b: putfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 88e: return
      // 88f: aload 0
      // 890: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 893: tableswitch 361 0 4 33 64 248 282 282
      // 8b4: invokestatic mapped/Class5924.method2116 ()Z
      // 8b7: ifeq 8d3
      // 8ba: aload 0
      // 8bb: aload 0
      // 8bc: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 8bf: bipush 1
      // 8c0: iadd
      // 8c1: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 8c4: aload 0
      // 8c5: ldc2_w 1.18
      // 8c8: invokestatic mapped/Class5924.method2091 ()D
      // 8cb: dmul
      // 8cc: ldc2_w 0.01
      // 8cf: dsub
      // 8d0: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 8d3: invokestatic mapped/Class5924.method2116 ()Z
      // 8d6: ifeq 9fc
      // 8d9: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 8dc: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 8df: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 8e2: ifne 8e8
      // 8e5: goto 9fc
      // 8e8: aload 0
      // 8e9: ldc2_w 0.40123128
      // 8ec: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1389 (D)D
      // 8ef: dstore 7
      // 8f1: aload 0
      // 8f2: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1972 ()Z
      // 8f5: ifne 8fd
      // 8f8: ldc2_w 0.2
      // 8fb: dstore 7
      // 8fd: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 900: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 903: new net/minecraft/util/math/Vec3d
      // 906: dup
      // 907: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 90a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 90d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 910: getfield net/minecraft/util/math/Vec3d.x D
      // 913: dload 7
      // 915: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 918: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 91b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 91e: getfield net/minecraft/util/math/Vec3d.z D
      // 921: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 924: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (Lnet/minecraft/util/math/Vec3d;)V
      // 927: aload 0
      // 928: aload 0
      // 929: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 92c: aload 0
      // 92d: getfield dev/boze/client/systems/modules/movement/Speed.field717 Ldev/boze/client/settings/FloatSetting;
      // 930: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 933: invokevirtual java/lang/Float.floatValue ()F
      // 936: f2d
      // 937: ldc2_w 1.6
      // 93a: dmul
      // 93b: dmul
      // 93c: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 93f: aload 0
      // 940: getfield dev/boze/client/systems/modules/movement/Speed.field721 Ldev/boze/client/settings/IntSetting;
      // 943: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 946: invokevirtual java/lang/Integer.intValue ()I
      // 949: ifle 97e
      // 94c: aload 0
      // 94d: getfield dev/boze/client/systems/modules/movement/Speed.field737 I
      // 950: aload 0
      // 951: getfield dev/boze/client/systems/modules/movement/Speed.field721 Ldev/boze/client/settings/IntSetting;
      // 954: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 957: invokevirtual java/lang/Integer.intValue ()I
      // 95a: irem
      // 95b: ifeq 974
      // 95e: aload 0
      // 95f: aload 0
      // 960: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 963: dconst_1
      // 964: aload 0
      // 965: getfield dev/boze/client/systems/modules/movement/Speed.field720 Ldev/boze/client/settings/FloatSetting;
      // 968: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 96b: invokevirtual java/lang/Float.floatValue ()F
      // 96e: f2d
      // 96f: dsub
      // 970: dmul
      // 971: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 974: aload 0
      // 975: aload 0
      // 976: getfield dev/boze/client/systems/modules/movement/Speed.field737 I
      // 979: bipush 1
      // 97a: iadd
      // 97b: putfield dev/boze/client/systems/modules/movement/Speed.field737 I
      // 97e: aload 0
      // 97f: aload 0
      // 980: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 983: bipush 1
      // 984: iadd
      // 985: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 988: goto 9fc
      // 98b: aload 0
      // 98c: aload 0
      // 98d: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 990: ldc2_w 0.76
      // 993: aload 0
      // 994: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 997: invokestatic mapped/Class5924.method2091 ()D
      // 99a: dsub
      // 99b: dmul
      // 99c: dsub
      // 99d: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 9a0: aload 0
      // 9a1: aload 0
      // 9a2: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 9a5: bipush 1
      // 9a6: iadd
      // 9a7: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 9aa: goto 9fc
      // 9ad: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 9b0: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 9b3: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 9b6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 9b9: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 9bc: dconst_0
      // 9bd: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 9c0: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 9c3: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 9c6: getfield net/minecraft/util/math/Vec3d.y D
      // 9c9: dconst_0
      // 9ca: invokevirtual net/minecraft/util/math/Box.offset (DDD)Lnet/minecraft/util/math/Box;
      // 9cd: invokevirtual net/minecraft/client/world/ClientWorld.isSpaceEmpty (Lnet/minecraft/util/math/Box;)Z
      // 9d0: ifeq 9e6
      // 9d3: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // 9d6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 9d9: getfield net/minecraft/client/network/ClientPlayerEntity.verticalCollision Z
      // 9dc: ifeq 9eb
      // 9df: aload 0
      // 9e0: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 9e3: ifle 9eb
      // 9e6: aload 0
      // 9e7: bipush 0
      // 9e8: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // 9eb: aload 0
      // 9ec: aload 0
      // 9ed: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 9f0: aload 0
      // 9f1: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // 9f4: ldc2_w 159.0
      // 9f7: ddiv
      // 9f8: dsub
      // 9f9: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // 9fc: aload 0
      // 9fd: aload 0
      // 9fe: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a01: invokestatic mapped/Class5924.method2091 ()D
      // a04: invokestatic java/lang/Math.max (DD)D
      // a07: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a0a: aload 0
      // a0b: getfield dev/boze/client/systems/modules/movement/Speed.field739 D
      // a0e: dconst_0
      // a0f: dcmpl
      // a10: ifle a3f
      // a13: aload 0
      // a14: getfield dev/boze/client/systems/modules/movement/Speed.field723 Ldev/boze/client/settings/BooleanSetting;
      // a17: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // a1a: invokevirtual java/lang/Boolean.booleanValue ()Z
      // a1d: ifeq a3f
      // a20: aload 0
      // a21: getfield dev/boze/client/systems/modules/movement/Speed.field740 Ldev/boze/client/utils/Timer;
      // a24: ldc2_w 75.0
      // a27: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // a2a: ifne a3f
      // a2d: aload 0
      // a2e: aload 0
      // a2f: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a32: aload 0
      // a33: getfield dev/boze/client/systems/modules/movement/Speed.field739 D
      // a36: invokestatic java/lang/Math.max (DD)D
      // a39: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a3c: goto a69
      // a3f: aload 0
      // a40: getfield dev/boze/client/systems/modules/movement/Speed.field719 Ldev/boze/client/settings/BooleanSetting;
      // a43: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // a46: invokevirtual java/lang/Boolean.booleanValue ()Z
      // a49: ifeq a69
      // a4c: aload 0
      // a4d: aload 0
      // a4e: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a51: aload 0
      // a52: getfield dev/boze/client/systems/modules/movement/Speed.field736 I
      // a55: bipush 25
      // a57: if_icmple a60
      // a5a: ldc2_w 0.449
      // a5d: goto a63
      // a60: ldc2_w 0.433
      // a63: invokestatic java/lang/Math.min (DD)D
      // a66: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a69: aload 0
      // a6a: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // a6d: invokestatic mapped/Class5924.method93 (D)Lnet/minecraft/util/math/Vec3d;
      // a70: astore 12
      // a72: aload 12
      // a74: getfield net/minecraft/util/math/Vec3d.x D
      // a77: dstore 5
      // a79: aload 12
      // a7b: getfield net/minecraft/util/math/Vec3d.z D
      // a7e: dstore 9
      // a80: aload 1
      // a81: new net/minecraft/util/math/Vec3d
      // a84: dup
      // a85: dload 5
      // a87: dload 7
      // a89: dload 9
      // a8b: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // a8e: putfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // a91: aload 1
      // a92: bipush 1
      // a93: putfield dev/boze/client/events/PlayerMoveEvent.field1892 Z
      // a96: goto c43
      // a99: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // a9c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // a9f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // aa2: ifne aae
      // aa5: aload 0
      // aa6: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // aa9: bipush 3
      // aaa: if_icmpeq aae
      // aad: return
      // aae: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // ab1: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // ab4: getfield net/minecraft/client/network/ClientPlayerEntity.horizontalCollision Z
      // ab7: ifne ac8
      // aba: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // abd: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // ac0: getfield net/minecraft/client/network/ClientPlayerEntity.forwardSpeed F
      // ac3: fconst_0
      // ac4: fcmpl
      // ac5: ifne ad6
      // ac8: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // acb: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // ace: getfield net/minecraft/client/network/ClientPlayerEntity.forwardSpeed F
      // ad1: fconst_0
      // ad2: fcmpl
      // ad3: ifeq b69
      // ad6: aload 0
      // ad7: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // ada: bipush 2
      // adb: if_icmpne b28
      // ade: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // ae1: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // ae4: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // ae7: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // aea: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // aed: getfield net/minecraft/util/math/Vec3d.x D
      // af0: ldc2_w -0.5
      // af3: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // af6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // af9: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // afc: getfield net/minecraft/util/math/Vec3d.z D
      // aff: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // b02: aload 0
      // b03: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1972 ()Z
      // b06: ifeq b0f
      // b09: ldc2_w 0.2
      // b0c: goto b12
      // b0f: ldc2_w 0.4
      // b12: dstore 7
      // b14: aload 0
      // b15: aload 0
      // b16: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // b19: ldc2_w 2.149
      // b1c: dmul
      // b1d: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // b20: aload 0
      // b21: bipush 3
      // b22: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // b25: goto b69
      // b28: aload 0
      // b29: getfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // b2c: bipush 3
      // b2d: if_icmpne b51
      // b30: ldc2_w 0.66
      // b33: aload 0
      // b34: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // b37: invokestatic mapped/Class5924.method2091 ()D
      // b3a: dsub
      // b3b: dmul
      // b3c: dstore 12
      // b3e: aload 0
      // b3f: aload 0
      // b40: getfield dev/boze/client/systems/modules/movement/Speed.field732 D
      // b43: dload 12
      // b45: dsub
      // b46: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // b49: aload 0
      // b4a: bipush 2
      // b4b: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // b4e: goto b69
      // b51: aload 0
      // b52: invokevirtual dev/boze/client/systems/modules/movement/Speed.method1972 ()Z
      // b55: ifne b64
      // b58: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // b5b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // b5e: getfield net/minecraft/client/network/ClientPlayerEntity.verticalCollision Z
      // b61: ifeq b69
      // b64: aload 0
      // b65: bipush 1
      // b66: putfield dev/boze/client/systems/modules/movement/Speed.field734 I
      // b69: aload 0
      // b6a: aload 0
      // b6b: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // b6e: invokestatic mapped/Class5924.method2091 ()D
      // b71: invokestatic java/lang/Math.max (DD)D
      // b74: aload 0
      // b75: getfield dev/boze/client/systems/modules/movement/Speed.field717 Ldev/boze/client/settings/FloatSetting;
      // b78: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // b7b: invokevirtual java/lang/Float.floatValue ()F
      // b7e: f2d
      // b7f: invokestatic java/lang/Math.min (DD)D
      // b82: putfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // b85: fload 11
      // b87: fstore 12
      // b89: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // b8c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // b8f: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // b92: getfield net/minecraft/client/input/Input.movementSideways F
      // b95: fstore 13
      // b97: fload 12
      // b99: f2d
      // b9a: dconst_0
      // b9b: dcmpl
      // b9c: ifeq bc1
      // b9f: fload 13
      // ba1: f2d
      // ba2: dconst_0
      // ba3: dcmpl
      // ba4: ifeq bc1
      // ba7: fload 12
      // ba9: f2d
      // baa: ldc2_w 0.7853981633974483
      // bad: invokestatic java/lang/Math.sin (D)D
      // bb0: dmul
      // bb1: d2f
      // bb2: fstore 12
      // bb4: fload 13
      // bb6: f2d
      // bb7: ldc2_w 0.7853981633974483
      // bba: invokestatic java/lang/Math.cos (D)D
      // bbd: dmul
      // bbe: d2f
      // bbf: fstore 13
      // bc1: fload 12
      // bc3: f2d
      // bc4: aload 0
      // bc5: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // bc8: dmul
      // bc9: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // bcc: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // bcf: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // bd2: f2d
      // bd3: invokestatic java/lang/Math.toRadians (D)D
      // bd6: invokestatic java/lang/Math.sin (D)D
      // bd9: dneg
      // bda: dmul
      // bdb: fload 13
      // bdd: f2d
      // bde: aload 0
      // bdf: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // be2: dmul
      // be3: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // be6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // be9: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // bec: f2d
      // bed: invokestatic java/lang/Math.toRadians (D)D
      // bf0: invokestatic java/lang/Math.cos (D)D
      // bf3: dmul
      // bf4: dadd
      // bf5: dstore 5
      // bf7: fload 12
      // bf9: f2d
      // bfa: aload 0
      // bfb: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // bfe: dmul
      // bff: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // c02: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // c05: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // c08: f2d
      // c09: invokestatic java/lang/Math.toRadians (D)D
      // c0c: invokestatic java/lang/Math.cos (D)D
      // c0f: dmul
      // c10: fload 13
      // c12: f2d
      // c13: aload 0
      // c14: getfield dev/boze/client/systems/modules/movement/Speed.field731 D
      // c17: dmul
      // c18: getstatic dev/boze/client/systems/modules/movement/Speed.mc Lnet/minecraft/client/MinecraftClient;
      // c1b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // c1e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // c21: f2d
      // c22: invokestatic java/lang/Math.toRadians (D)D
      // c25: invokestatic java/lang/Math.sin (D)D
      // c28: dneg
      // c29: dmul
      // c2a: dsub
      // c2b: dstore 9
      // c2d: aload 1
      // c2e: new net/minecraft/util/math/Vec3d
      // c31: dup
      // c32: dload 5
      // c34: dload 7
      // c36: dload 9
      // c38: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // c3b: putfield dev/boze/client/events/PlayerMoveEvent.vec3 Lnet/minecraft/util/math/Vec3d;
      // c3e: aload 1
      // c3f: bipush 1
      // c40: putfield dev/boze/client/events/PlayerMoveEvent.field1892 Z
      // c43: return
   }

   public void method1710(ExplosionS2CPacket velocity) {
      if (this.field719.method419()) {
         double var5 = mc.player.getX() - mc.player.prevX;
         double var7 = mc.player.getZ() - mc.player.prevZ;
         if (velocity.getPlayerVelocityX() > 0.0F && var5 > 0.0 && velocity.getPlayerVelocityZ() > 0.0F && var7 > 0.0
            || (double)velocity.getPlayerVelocityX() < 0.0 && var5 < 0.0 && (double)velocity.getPlayerVelocityZ() < 0.0 && var7 < 0.0
            || (double)velocity.getPlayerVelocityX() > 0.0 && var5 > 0.0 && (double)velocity.getPlayerVelocityZ() < 0.0 && var7 < 0.0
            || (double)velocity.getPlayerVelocityX() < 0.0 && var5 < 0.0 && (double)velocity.getPlayerVelocityZ() > 0.0 && var7 > 0.0) {
            this.field739 = Math.sqrt(
                  (double)(velocity.getPlayerVelocityX() * velocity.getPlayerVelocityX() + velocity.getPlayerVelocityZ() * velocity.getPlayerVelocityZ())
               )
               * this.field724.getValue();
            this.field740.reset();
         }
      } else {
         this.field739 = Math.sqrt(
               (double)(velocity.getPlayerVelocityX() * velocity.getPlayerVelocityX() + velocity.getPlayerVelocityZ() * velocity.getPlayerVelocityZ())
            )
            * this.field724.getValue();
         this.field740.reset();
      }

      field742 = true;
   }

   @Override
   public String method1322() {
      return this.field715.method461().name();
   }

   private double method1389(double var1) {
      StatusEffectInstance var6 = mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST) ? mc.player.getStatusEffect(StatusEffects.JUMP_BOOST) : null;
      if (var6 != null) {
         var1 += (double)((float)(var6.getAmplifier() + 1) * 0.1F);
      }

      return var1;
   }

   private boolean method1972() {
      return !mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, 0.21, 0.0)).iterator().hasNext();
   }

   private double method355(double var1) {
      BigDecimal var3 = new BigDecimal(var1);
      var3 = var3.setScale(3, RoundingMode.HALF_UP);
      return var3.doubleValue();
   }

   @EventHandler(
      priority = 25
   )
   public void method1633(MovementEvent event) {
      if (!Class5924.method2116()) {
         this.field731 = 0.0;
         if (this.field715.method461() != SpeedMode.Grim) {
            mc.player.setVelocity(0.0, mc.player.getVelocity().y, 0.0);
         }

         Class3076.method6025(this);
      } else if (this.field722.method419()
         && (this.field715.method461() != SpeedMode.Strafe || !this.field719.method419())
         && this.field715.method461() != SpeedMode.Grim) {
         Class3076.method6024(this, 69, 1.0875F);
      } else {
         Class3076.method6025(this);
      }

      double var5 = mc.player.getX() - mc.player.prevX;
      double var7 = mc.player.getZ() - mc.player.prevZ;
      field742 = false;
      this.field732 = Math.sqrt(var5 * var5 + var7 * var7);
      if (this.field715.method461() == SpeedMode.Grim && this.ab != -420.0F && !mc.player.isSneaking()) {
         mc.player.setSprinting(true);
         event.method1074(new ActionWrapper(this.ab, mc.player.getPitch()));
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && MinecraftUtils.isClientActive() && !(mc.currentScreen instanceof DownloadingTerrainScreen)) {
         this.field738.reset();
         this.field734 = 4;
         this.field731 = Class5924.method2091();
         this.field732 = 0.0;
      } else if (event.packet instanceof ExplosionS2CPacket var5 && !field742) {
         this.method1710(var5);
      }
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field734 = 4;
         this.field731 = Class5924.method2091();
         this.field732 = 0.0;
      }
   }

   @Override
   public void onDisable() {
      Class3076.method6025(this);
   }

   private boolean lambda$new$11() {
      return this.field715.method461() == SpeedMode.Vanilla;
   }

   private boolean lambda$new$10() {
      return this.field715.method461() == SpeedMode.Vanilla;
   }

   private boolean lambda$new$9() {
      return this.field715.method461() != SpeedMode.Grim;
   }

   private boolean lambda$new$8() {
      return this.field715.method461() == SpeedMode.Grim;
   }

   private boolean lambda$new$7() {
      return this.field715.method461() != SpeedMode.Grim;
   }

   private boolean lambda$new$6() {
      return this.field715.method461() != SpeedMode.Grim;
   }

   private boolean lambda$new$5() {
      return this.field715.method461() == SpeedMode.BHop;
   }

   private boolean lambda$new$4() {
      return this.field715.method461() == SpeedMode.BHop;
   }

   private boolean lambda$new$3() {
      return this.field715.method461() == SpeedMode.Strafe;
   }

   private boolean lambda$new$2() {
      return this.field715.method461() == SpeedMode.Vanilla;
   }

   private boolean lambda$new$1() {
      return this.field715.method461() != SpeedMode.Grim;
   }

   private boolean lambda$new$0() {
      return this.field715.method461() == SpeedMode.Grim;
   }
}
