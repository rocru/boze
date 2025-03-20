package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.CrystalAssistAimPoint;
import dev.boze.client.enums.CrystalAssistPriority;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import mapped.Class3069;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CrystalAssist extends Module {
   public static final CrystalAssist INSTANCE = new CrystalAssist();
   private final BooleanSetting field2771 = new BooleanSetting("AntiSuicide", true, "Try to avoid aiming at crystals that may kill you");
   private final BooleanSetting field2772 = new BooleanSetting("OnlyOwn", false, "Only aim at crystals that you placed");
   private final EnumSetting<CrystalAssistAimPoint> field2773 = new EnumSetting<CrystalAssistAimPoint>(
      "AimPoint", CrystalAssistAimPoint.Closest, "Aim point to use"
   );
   private final BooleanSetting field2774 = new BooleanSetting("StopOverCrystal", false, "Stop assisting when looking at crystal");
   private final BooleanSetting field2775 = new BooleanSetting("OnlyWhenClicking", true, "Only assist when clicking attack key");
   private final IntSetting field2776 = new IntSetting("Ticks", 5, 1, 40, 1, "Ticks to assist for after clicking", this.field2775::getValue);
   private final MinMaxSetting field2777 = new MinMaxSetting("Range", 3.0, 1.0, 7.0, 0.1, "Range to target crystals within");
   private final BooleanSetting field2778 = new BooleanSetting("ThroughWalls", false, "Target through walls");
   private final MinMaxSetting field2779 = new MinMaxSetting("BoxScale", 0.8, 0.1, 1.0, 0.1, "Scale of the box to target within");
   private final MinMaxDoubleSetting field2780 = new MinMaxDoubleSetting("Speed", new double[]{1.0, 2.0}, 0.1, 10.0, 0.1, "Assist speed");
   private final MinMaxSetting field2781 = new MinMaxSetting("MaxDelta", 5.0, 0.01, 10.0, 0.02, "Max mouse delta change per poll");
   private final BooleanSetting field2782 = new BooleanSetting("DontResist", false, "Don't resist mouse movement in opposite directions");
   private final BooleanSetting field2783 = new BooleanSetting("Vertical", true, "Assist vertical rotation");
   private final IntSetting field2784 = new IntSetting("FOV", 180, 1, 180, 1, "Maximum FOV to target within");
   private final EnumSetting<CrystalAssistPriority> field2785 = new EnumSetting<CrystalAssistPriority>(
      "Priority", CrystalAssistPriority.Distance, "The priority to target crystals with"
   );
   private final Comparator<Entity> field2786 = Comparator.comparing(this::lambda$new$0);
   private final Timer field2787 = new Timer();
   private final ConcurrentHashMap<BlockPos, Long> field2788 = new ConcurrentHashMap();

   public CrystalAssist() {
      super("CrystalAssist", "Assists in targeting crystals", Category.Legit);
   }

   @EventHandler
   public void method1596(PrePacketSendEvent event) {
      if (this.field2772.getValue()
         && event.packet instanceof PlayerInteractBlockC2SPacket var5
         && mc.player.getStackInHand(var5.getHand()).getItem() == Items.END_CRYSTAL) {
         this.field2788.put(var5.getBlockHitResult().getBlockPos(), System.currentTimeMillis());
      }
   }

   @EventHandler
   public void method1597(MovementEvent event) {
      if (this.field2772.getValue()) {
         this.field2788.entrySet().removeIf(CrystalAssist::lambda$onSendMovementPackets$1);
      }
   }

   @EventHandler
   public void method1598(MouseUpdateEvent event) {
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
      // 000: invokestatic dev/boze/client/utils/MinecraftUtils.isClientActive ()Z
      // 003: ifeq 00d
      // 006: aload 1
      // 007: invokevirtual dev/boze/client/events/MouseUpdateEvent.method1022 ()Z
      // 00a: ifeq 00e
      // 00d: return
      // 00e: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 011: getfield net/minecraft/client/MinecraftClient.options Lnet/minecraft/client/option/GameOptions;
      // 014: getfield net/minecraft/client/option/GameOptions.attackKey Lnet/minecraft/client/option/KeyBinding;
      // 017: invokevirtual net/minecraft/client/option/KeyBinding.isPressed ()Z
      // 01a: ifeq 024
      // 01d: aload 0
      // 01e: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2787 Ldev/boze/client/utils/Timer;
      // 021: invokevirtual dev/boze/client/utils/Timer.reset ()V
      // 024: aload 0
      // 025: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2775 Ldev/boze/client/settings/BooleanSetting;
      // 028: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 02b: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 02e: ifeq 04a
      // 031: aload 0
      // 032: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2787 Ldev/boze/client/utils/Timer;
      // 035: aload 0
      // 036: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2776 Ldev/boze/client/settings/IntSetting;
      // 039: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 03c: invokevirtual java/lang/Integer.intValue ()I
      // 03f: bipush 50
      // 041: imul
      // 042: i2d
      // 043: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // 046: ifeq 04a
      // 049: return
      // 04a: new dev/boze/client/utils/RotationHelper
      // 04d: dup
      // 04e: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 051: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 054: invokespecial dev/boze/client/utils/RotationHelper.<init> (Lnet/minecraft/entity/Entity;)V
      // 057: astore 5
      // 059: aload 5
      // 05b: ifnonnull 05f
      // 05e: return
      // 05f: aload 0
      // 060: invokevirtual dev/boze/client/systems/modules/legit/CrystalAssist.method1599 ()Lnet/minecraft/entity/Entity;
      // 063: astore 6
      // 065: aload 6
      // 067: ifnonnull 06b
      // 06a: return
      // 06b: aload 0
      // 06c: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2774 Ldev/boze/client/settings/BooleanSetting;
      // 06f: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 072: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 075: ifeq 09a
      // 078: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 07b: getfield net/minecraft/client/MinecraftClient.crosshairTarget Lnet/minecraft/util/hit/HitResult;
      // 07e: astore 8
      // 080: aload 8
      // 082: instanceof net/minecraft/util/hit/EntityHitResult
      // 085: ifeq 09a
      // 088: aload 8
      // 08a: checkcast net/minecraft/util/hit/EntityHitResult
      // 08d: astore 7
      // 08f: aload 7
      // 091: invokevirtual net/minecraft/util/hit/EntityHitResult.getEntity ()Lnet/minecraft/entity/Entity;
      // 094: aload 6
      // 096: if_acmpne 09a
      // 099: return
      // 09a: aload 6
      // 09c: invokevirtual net/minecraft/entity/Entity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 09f: aload 6
      // 0a1: invokevirtual net/minecraft/entity/Entity.getTargetingMargin ()F
      // 0a4: f2d
      // 0a5: invokevirtual net/minecraft/util/math/Box.expand (D)Lnet/minecraft/util/math/Box;
      // 0a8: dconst_1
      // 0a9: aload 0
      // 0aa: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2779 Ldev/boze/client/settings/MinMaxSetting;
      // 0ad: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 0b0: invokevirtual java/lang/Double.doubleValue ()D
      // 0b3: dsub
      // 0b4: invokevirtual net/minecraft/util/math/Box.contract (D)Lnet/minecraft/util/math/Box;
      // 0b7: astore 7
      // 0b9: aload 7
      // 0bb: invokevirtual net/minecraft/util/math/Box.getCenter ()Lnet/minecraft/util/math/Vec3d;
      // 0be: astore 8
      // 0c0: aload 0
      // 0c1: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2773 Ldev/boze/client/settings/EnumSetting;
      // 0c4: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0c7: checkcast dev/boze/client/enums/CrystalAssistAimPoint
      // 0ca: invokevirtual dev/boze/client/enums/CrystalAssistAimPoint.ordinal ()I
      // 0cd: tableswitch 170 0 4 35 59 78 170 88
      // 0f0: aload 7
      // 0f2: aload 5
      // 0f4: invokevirtual dev/boze/client/utils/RotationHelper.method1954 ()Lnet/minecraft/util/math/Vec3d;
      // 0f7: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 0fa: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0fd: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 100: invokestatic mapped/Class5917.method136 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 103: astore 8
      // 105: goto 177
      // 108: aload 7
      // 10a: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 10d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 110: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 113: invokestatic mapped/Class5917.method123 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 116: astore 8
      // 118: goto 177
      // 11b: aload 7
      // 11d: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 120: astore 8
      // 122: goto 177
      // 125: aload 6
      // 127: invokevirtual net/minecraft/entity/Entity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 12a: astore 9
      // 12c: new net/minecraft/util/math/Box
      // 12f: dup
      // 130: aload 9
      // 132: getfield net/minecraft/util/math/Vec3d.x D
      // 135: ldc2_w 0.25
      // 138: dsub
      // 139: aload 9
      // 13b: getfield net/minecraft/util/math/Vec3d.y D
      // 13e: ldc2_w 0.25
      // 141: dsub
      // 142: aload 9
      // 144: getfield net/minecraft/util/math/Vec3d.z D
      // 147: ldc2_w 0.25
      // 14a: dsub
      // 14b: aload 9
      // 14d: getfield net/minecraft/util/math/Vec3d.x D
      // 150: ldc2_w 0.25
      // 153: dadd
      // 154: aload 9
      // 156: getfield net/minecraft/util/math/Vec3d.y D
      // 159: aload 9
      // 15b: getfield net/minecraft/util/math/Vec3d.z D
      // 15e: ldc2_w 0.25
      // 161: dadd
      // 162: invokespecial net/minecraft/util/math/Box.<init> (DDDDDD)V
      // 165: astore 10
      // 167: aload 10
      // 169: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 16c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 16f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 172: invokestatic mapped/Class5917.method123 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 175: astore 8
      // 177: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 17a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 17d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 180: aload 8
      // 182: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 185: astore 9
      // 187: aload 9
      // 189: aload 5
      // 18b: invokevirtual dev/boze/client/utils/RotationHelper.method605 (Ldev/boze/client/utils/RotationHelper;)F
      // 18e: f2d
      // 18f: aload 0
      // 190: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2784 Ldev/boze/client/settings/IntSetting;
      // 193: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 196: invokevirtual java/lang/Integer.intValue ()I
      // 199: i2d
      // 19a: ldc2_w 1.417
      // 19d: dmul
      // 19e: dcmpl
      // 19f: ifle 1a3
      // 1a2: return
      // 1a3: aload 5
      // 1a5: aload 9
      // 1a7: aload 0
      // 1a8: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2780 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 1ab: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1287 ()[D
      // 1ae: invokevirtual dev/boze/client/utils/RotationHelper.method603 (Ldev/boze/client/utils/RotationHelper;[D)Ldev/boze/client/utils/RotationHelper;
      // 1b1: invokevirtual dev/boze/client/utils/RotationHelper.method1600 ()Ldev/boze/client/utils/RotationHelper;
      // 1b4: astore 10
      // 1b6: aload 10
      // 1b8: aload 5
      // 1ba: invokevirtual dev/boze/client/utils/RotationHelper.method606 (Ldev/boze/client/utils/RotationHelper;)Ldev/boze/client/utils/RotationHelper;
      // 1bd: astore 11
      // 1bf: aload 11
      // 1c1: invokestatic dev/boze/client/utils/RotationHelper.method614 (Ldev/boze/client/utils/RotationHelper;)[Lnet/minecraft/util/Pair;
      // 1c4: astore 12
      // 1c6: aload 12
      // 1c8: bipush 0
      // 1c9: aaload
      // 1ca: invokevirtual net/minecraft/util/Pair.getLeft ()Ljava/lang/Object;
      // 1cd: checkcast java/lang/Double
      // 1d0: invokevirtual java/lang/Double.doubleValue ()D
      // 1d3: aload 0
      // 1d4: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2781 Ldev/boze/client/settings/MinMaxSetting;
      // 1d7: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 1da: invokevirtual java/lang/Double.doubleValue ()D
      // 1dd: dneg
      // 1de: ldc2_w 10.0
      // 1e1: dmul
      // 1e2: aload 0
      // 1e3: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2781 Ldev/boze/client/settings/MinMaxSetting;
      // 1e6: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 1e9: invokevirtual java/lang/Double.doubleValue ()D
      // 1ec: ldc2_w 10.0
      // 1ef: dmul
      // 1f0: invokestatic net/minecraft/util/math/MathHelper.clamp (DDD)D
      // 1f3: dstore 13
      // 1f5: aload 0
      // 1f6: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2782 Ldev/boze/client/settings/BooleanSetting;
      // 1f9: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 1fc: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 1ff: ifeq 20e
      // 202: aload 1
      // 203: getfield dev/boze/client/events/MouseUpdateEvent.deltaX D
      // 206: dload 13
      // 208: dmul
      // 209: dconst_0
      // 20a: dcmpl
      // 20b: iflt 21e
      // 20e: aload 1
      // 20f: aload 1
      // 210: getfield dev/boze/client/events/MouseUpdateEvent.deltaX D
      // 213: dload 13
      // 215: dadd
      // 216: putfield dev/boze/client/events/MouseUpdateEvent.deltaX D
      // 219: aload 1
      // 21a: bipush 1
      // 21b: invokevirtual dev/boze/client/events/MouseUpdateEvent.method1021 (Z)V
      // 21e: aload 0
      // 21f: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2783 Ldev/boze/client/settings/BooleanSetting;
      // 222: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 225: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 228: ifeq 283
      // 22b: aload 12
      // 22d: bipush 0
      // 22e: aaload
      // 22f: invokevirtual net/minecraft/util/Pair.getRight ()Ljava/lang/Object;
      // 232: checkcast java/lang/Double
      // 235: invokevirtual java/lang/Double.doubleValue ()D
      // 238: aload 0
      // 239: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2781 Ldev/boze/client/settings/MinMaxSetting;
      // 23c: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 23f: invokevirtual java/lang/Double.doubleValue ()D
      // 242: dneg
      // 243: ldc2_w 10.0
      // 246: dmul
      // 247: aload 0
      // 248: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2781 Ldev/boze/client/settings/MinMaxSetting;
      // 24b: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 24e: invokevirtual java/lang/Double.doubleValue ()D
      // 251: ldc2_w 10.0
      // 254: dmul
      // 255: invokestatic net/minecraft/util/math/MathHelper.clamp (DDD)D
      // 258: dstore 15
      // 25a: aload 0
      // 25b: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2782 Ldev/boze/client/settings/BooleanSetting;
      // 25e: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 261: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 264: ifeq 273
      // 267: aload 1
      // 268: getfield dev/boze/client/events/MouseUpdateEvent.deltaY D
      // 26b: dload 15
      // 26d: dmul
      // 26e: dconst_0
      // 26f: dcmpl
      // 270: iflt 283
      // 273: aload 1
      // 274: aload 1
      // 275: getfield dev/boze/client/events/MouseUpdateEvent.deltaY D
      // 278: dload 15
      // 27a: dadd
      // 27b: putfield dev/boze/client/events/MouseUpdateEvent.deltaY D
      // 27e: aload 1
      // 27f: bipush 1
      // 280: invokevirtual dev/boze/client/events/MouseUpdateEvent.method1021 (Z)V
      // 283: return
   }

   private Entity method1599() {
      ArrayList var4 = new ArrayList();

      for (Entity var6 : mc.world.getEntities()) {
         if (this.method1601(var6)
            && !((double)var6.distanceTo(mc.player) > this.field2777.getValue() + 1.0)
            && (this.field2778.getValue() || mc.player.canSee(var6))) {
            var4.add(var6);
         }
      }

      return (Entity)var4.stream().min(this.field2786).orElse(null);
   }

   private RotationHelper method1600() {
      return new RotationHelper(mc.player);
   }

   private boolean method1601(Entity var1) {
      if (var1 instanceof EndCrystalEntity var5) {
         if (this.field2771.getValue()) {
            double var6 = Class3069.method6003(mc.player, var5.getPos(), 0, null, false);
            if (var6 >= (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
               return false;
            }
         }

         return this.field2772.getValue() ? this.field2788.containsKey(var5.getBlockPos().down()) : true;
      } else {
         return false;
      }
   }

   private static boolean lambda$onSendMovementPackets$1(Entry var0) {
      return System.currentTimeMillis() - (Long)var0.getValue() > 10000L;
   }

   private Double lambda$new$0(Entity param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: aload 0
      // 01: getfield dev/boze/client/systems/modules/legit/CrystalAssist.field2785 Ldev/boze/client/settings/EnumSetting;
      // 04: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 07: checkcast dev/boze/client/enums/CrystalAssistPriority
      // 0a: invokevirtual dev/boze/client/enums/CrystalAssistPriority.ordinal ()I
      // 0d: lookupswitch 89 2 0 27 1 58
      // 28: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 2b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 31: aload 1
      // 32: invokevirtual net/minecraft/entity/Entity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 35: aload 1
      // 36: invokevirtual net/minecraft/entity/Entity.getTargetingMargin ()F
      // 39: f2d
      // 3a: invokevirtual net/minecraft/util/math/Box.expand (D)Lnet/minecraft/util/math/Box;
      // 3d: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 40: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 43: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 46: areturn
      // 47: getstatic dev/boze/client/systems/modules/legit/CrystalAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 4a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 4d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 50: aload 1
      // 51: invokevirtual net/minecraft/entity/Entity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 54: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 57: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 5a: aload 0
      // 5b: invokevirtual dev/boze/client/systems/modules/legit/CrystalAssist.method1600 ()Ldev/boze/client/utils/RotationHelper;
      // 5e: invokevirtual dev/boze/client/utils/RotationHelper.method605 (Ldev/boze/client/utils/RotationHelper;)F
      // 61: f2d
      // 62: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 65: areturn
      // 66: dconst_0
      // 67: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 6a: areturn
   }
}
