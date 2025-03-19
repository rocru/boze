package dev.boze.client.systems.modules.combat.aura;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.AimPoint;
import dev.boze.client.enums.AttackPriority;
import dev.boze.client.enums.AuraAntiBlock;
import dev.boze.client.enums.AuraReference;
import dev.boze.client.enums.ClickMethod;
import dev.boze.client.enums.CritMode;
import dev.boze.client.enums.InteractionMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.enums.Targeting;
import dev.boze.client.enums.TrackMode;
import dev.boze.client.events.OpenScreenEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.events.eJ;
import dev.boze.client.jumptable.eI;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.GhostModule;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.combat.Aura;
import dev.boze.client.systems.modules.legit.AntiBots;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.click.ClickManager;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import mapped.Class1202;
import mapped.Class3089;
import mapped.Class5917;
import mapped.Class5924;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class AuraGhost extends GhostModule {
   private final Aura field2460;
   private final ClickManager field2461;
   public final BooleanSetting field2462 = new BooleanSetting("Render", true, "Render target");
   private final ColorSetting field2463 = new ColorSetting("Color", new BozeDrawColor(1691624484), "Color for fill", this.field2462);
   private final ColorSetting field2464 = new ColorSetting("Outline", new BozeDrawColor(-2874332), "Color for outline", this.field2462);
   private final BooleanSetting field2465 = new BooleanSetting("InInventory", false, "Track and attack players while in inventory");
   private final BooleanSetting field2466 = new BooleanSetting("WeaponOnly", false, "Only attack with weapons");
   private final BooleanSetting field2467 = new BooleanSetting("OnlyWhenHolding", false, "Only attack when holding left click");
   private final EnumSetting<CritMode> field2468 = new EnumSetting<CritMode>(
      "AwaitCrits",
      CritMode.Off,
      "Only attack when you can crit\n - Off: Don't wait for crits\n - Normal: Wait for crits when in air\n - Force: Always wait for crits\n"
   );
   private final MinMaxDoubleSetting field2469 = new MinMaxDoubleSetting("Reach", new double[]{2.95, 3.0}, 1.0, 6.0, 0.05, "Reach");
   private final MinMaxDoubleSetting field2470 = new MinMaxDoubleSetting("AimSpeed", new double[]{0.2, 0.4}, 0.1, 10.0, 0.1, "Aim speed");
   private final EnumSetting<AuraReference> field2471 = new EnumSetting<AuraReference>(
      "Reference", AuraReference.Client, "The frame of reference for rotation-related calculations"
   );
   private final IntSetting field2472 = new IntSetting("FOV", 180, 1, 180, 1, "The maximum FOV to target players within");
   private final EnumSetting<AimPoint> field2473 = new EnumSetting<AimPoint>("AimPoint", AimPoint.Clamped, "Aim point to use");
   private final BooleanSetting field2474 = new BooleanSetting("RayCast", true, "Check if current target can be seen");
   private final EnumSetting<TrackMode> field2475 = new EnumSetting<TrackMode>("Walls", TrackMode.Off, "Whether to track/attack through walls or not");
   private final BooleanSetting field2476 = new BooleanSetting("Avoid", false, "Avoid walls when rotating", this.field2475);
   private final BooleanSetting field2477 = new BooleanSetting("AlwaysHit", false, "(Blatant) Always hit the target");
   private final EnumSetting<ClickMethod> field2478 = new EnumSetting<ClickMethod>("Clicking", ClickMethod.Normal, "The clicking method to use");
   private final IntArraySetting field2479 = new IntArraySetting("CPS", new int[]{8, 12}, 1, 20, 1, "The CPS to click at", this.field2478);
   private final FloatSetting field2480 = new FloatSetting(
      "CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$0, this.field2478
   );
   private final BooleanSetting field2481 = new BooleanSetting("ModelDelay", false, "Model attack delays based on human response times", this.field2478);
   private final BooleanSetting field2482 = new BooleanSetting("MissSwing", false, "Swing at the air if target missed", this.field2478);
   private final EnumSetting<Targeting> field2483 = new EnumSetting<Targeting>("Targeting", Targeting.Single, "The targeting method to use");
   private final EnumSetting<AttackPriority> field2484 = new EnumSetting<AttackPriority>(
      "Priority", AttackPriority.Distance, "The priority to target enemies with", this.field2483
   );
   private final BooleanSetting field2485 = new BooleanSetting("Sticky", false, "Stick to the same target when possible", this.field2483);
   private final EnumSetting<AuraAntiBlock> field2486 = new EnumSetting<AuraAntiBlock>("AntiBlock", AuraAntiBlock.Off, "Anti block");
   private final BooleanSetting field2487 = new BooleanSetting("AttackBlocking", true, "Attack players that are blocking", this.field2486);
   private final BooleanSetting field2488 = new BooleanSetting("DisableOnDeath", true, "Disable when you die");
   private final BooleanSetting field2489 = new BooleanSetting("DisableOnTP", true, "Disable when you get teleported");
   private final SettingCategory field2490 = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting field2491 = new BooleanSetting("Players", true, "Target players", this.field2490);
   private final BooleanSetting field2492 = new BooleanSetting("Friends", false, "Target friends", this.field2490);
   private final BooleanSetting field2493 = new BooleanSetting("Animals", false, "Target animals", this.field2490);
   private final BooleanSetting field2494 = new BooleanSetting("Monsters", false, "Target monsters", this.field2490);
   private final BooleanSetting field2495 = new BooleanSetting("Fireballs", false, "Target fireballs", this.field2490);
   private CopyOnWriteArrayList<Pair<Entity, Vec3d>> field2496 = new CopyOnWriteArrayList();
   private boolean field2497 = false;
   private Entity field2498 = null;
   private final Timer field2499 = new Timer();
   private final Comparator<Pair<Entity, Vec3d>> field2500 = Comparator.comparing(this::lambda$new$1);

   public AuraGhost(Aura aura) {
      this.field2460 = aura;
      this.field2461 = new ClickManager(this.field2478, this.field2479, this.field2480);
   }

   public void method1415() {
      this.field2461.method2172();
   }

   public void method1416() {
      this.field2496 = new CopyOnWriteArrayList();
      this.field2498 = null;
   }

   @Override
   public InteractionMode getInteractionMode() {
      return Options.INSTANCE.method1971() ? InteractionMode.Ghost : this.field2460.mode.method461();
   }

   public void method1417(eJ event) {
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
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 1
      // 001: invokevirtual dev/boze/client/events/eJ.method1101 ()Z
      // 004: ifeq 008
      // 007: return
      // 008: invokestatic dev/boze/client/systems/modules/misc/AutoEat.method1663 ()Z
      // 00b: ifeq 00f
      // 00e: return
      // 00f: aload 0
      // 010: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2466 Ldev/boze/client/settings/BooleanSetting;
      // 013: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 016: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 019: ifeq 045
      // 01c: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 01f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 022: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getMainHandStack ()Lnet/minecraft/item/ItemStack;
      // 025: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 028: instanceof net/minecraft/item/SwordItem
      // 02b: ifne 045
      // 02e: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 031: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 034: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getMainHandStack ()Lnet/minecraft/item/ItemStack;
      // 037: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 03a: instanceof net/minecraft/item/AxeItem
      // 03d: ifne 045
      // 040: aload 0
      // 041: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1416 ()V
      // 044: return
      // 045: aload 0
      // 046: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2467 Ldev/boze/client/settings/BooleanSetting;
      // 049: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 04c: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 04f: ifeq 066
      // 052: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 055: getfield net/minecraft/client/MinecraftClient.options Lnet/minecraft/client/option/GameOptions;
      // 058: getfield net/minecraft/client/option/GameOptions.attackKey Lnet/minecraft/client/option/KeyBinding;
      // 05b: invokevirtual net/minecraft/client/option/KeyBinding.isPressed ()Z
      // 05e: ifne 066
      // 061: aload 0
      // 062: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1416 ()V
      // 065: return
      // 066: new java/util/ArrayList
      // 069: dup
      // 06a: aload 0
      // 06b: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2496 Ljava/util/concurrent/CopyOnWriteArrayList;
      // 06e: invokespecial java/util/ArrayList.<init> (Ljava/util/Collection;)V
      // 071: astore 5
      // 073: aload 0
      // 074: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2496 Ljava/util/concurrent/CopyOnWriteArrayList;
      // 077: invokevirtual java/util/concurrent/CopyOnWriteArrayList.clear ()V
      // 07a: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 07d: getfield dev/boze/client/systems/modules/client/GhostRotations.field760 Ldev/boze/client/utils/RotationHelper;
      // 080: ifnonnull 093
      // 083: new dev/boze/client/utils/RotationHelper
      // 086: dup
      // 087: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 08a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 08d: invokespecial dev/boze/client/utils/RotationHelper.<init> (Lnet/minecraft/entity/Entity;)V
      // 090: goto 099
      // 093: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 096: getfield dev/boze/client/systems/modules/client/GhostRotations.field760 Ldev/boze/client/utils/RotationHelper;
      // 099: astore 6
      // 09b: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 09e: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 0a1: invokevirtual net/minecraft/client/world/ClientWorld.getEntities ()Ljava/lang/Iterable;
      // 0a4: invokeinterface java/lang/Iterable.iterator ()Ljava/util/Iterator; 1
      // 0a9: astore 7
      // 0ab: aload 7
      // 0ad: invokeinterface java/util/Iterator.hasNext ()Z 1
      // 0b2: ifeq 24c
      // 0b5: aload 7
      // 0b7: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
      // 0bc: checkcast net/minecraft/entity/Entity
      // 0bf: astore 8
      // 0c1: aload 0
      // 0c2: aload 8
      // 0c4: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1426 (Lnet/minecraft/entity/Entity;)Z
      // 0c7: ifne 0cd
      // 0ca: goto 0ab
      // 0cd: aload 8
      // 0cf: invokevirtual net/minecraft/entity/Entity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 0d2: aload 8
      // 0d4: invokevirtual net/minecraft/entity/Entity.getTargetingMargin ()F
      // 0d7: f2d
      // 0d8: invokevirtual net/minecraft/util/math/Box.expand (D)Lnet/minecraft/util/math/Box;
      // 0db: astore 9
      // 0dd: aload 9
      // 0df: invokevirtual net/minecraft/util/math/Box.getCenter ()Lnet/minecraft/util/math/Vec3d;
      // 0e2: astore 10
      // 0e4: aload 0
      // 0e5: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2473 Ldev/boze/client/settings/EnumSetting;
      // 0e8: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0eb: checkcast dev/boze/client/enums/AimPoint
      // 0ee: invokevirtual dev/boze/client/enums/AimPoint.ordinal ()I
      // 0f1: tableswitch 77 0 2 27 51 70
      // 10c: aload 9
      // 10e: aload 6
      // 110: invokevirtual dev/boze/client/utils/RotationHelper.method1954 ()Lnet/minecraft/util/math/Vec3d;
      // 113: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 116: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 119: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 11c: invokestatic mapped/Class5917.method136 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 11f: astore 10
      // 121: goto 13e
      // 124: aload 9
      // 126: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 129: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 12c: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 12f: invokestatic mapped/Class5917.method123 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 132: astore 10
      // 134: goto 13e
      // 137: aload 9
      // 139: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 13c: astore 10
      // 13e: aload 10
      // 140: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 143: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 146: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 149: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 14c: aload 0
      // 14d: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 150: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 153: aload 0
      // 154: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 157: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 15a: dmul
      // 15b: dcmpl
      // 15c: ifle 162
      // 15f: goto 0ab
      // 162: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 165: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 168: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 16b: aload 10
      // 16d: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 170: aload 0
      // 171: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1427 ()Ldev/boze/client/utils/RotationHelper;
      // 174: invokevirtual dev/boze/client/utils/RotationHelper.method605 (Ldev/boze/client/utils/RotationHelper;)F
      // 177: f2d
      // 178: aload 0
      // 179: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2472 Ldev/boze/client/settings/IntSetting;
      // 17c: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 17f: invokevirtual java/lang/Integer.intValue ()I
      // 182: i2d
      // 183: ldc2_w 1.417
      // 186: dmul
      // 187: dcmpl
      // 188: ifle 18e
      // 18b: goto 0ab
      // 18e: aload 9
      // 190: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 193: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 196: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 199: invokevirtual net/minecraft/util/math/Box.contains (Lnet/minecraft/util/math/Vec3d;)Z
      // 19c: ifeq 1c1
      // 19f: invokestatic mapped/Class5924.method2116 ()Z
      // 1a2: ifeq 1c1
      // 1a5: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 1a8: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1ab: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 1ae: aload 6
      // 1b0: invokevirtual dev/boze/client/utils/RotationHelper.method1954 ()Lnet/minecraft/util/math/Vec3d;
      // 1b3: ldc2_w 0.01
      // 1b6: invokevirtual net/minecraft/util/math/Vec3d.multiply (D)Lnet/minecraft/util/math/Vec3d;
      // 1b9: invokevirtual net/minecraft/util/math/Vec3d.add (Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 1bc: astore 11
      // 1be: goto 1cd
      // 1c1: aload 0
      // 1c2: aload 9
      // 1c4: aload 8
      // 1c6: aload 6
      // 1c8: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1424 (Lnet/minecraft/util/math/Box;Lnet/minecraft/entity/Entity;Ldev/boze/client/utils/RotationHelper;)Lnet/minecraft/util/math/Vec3d;
      // 1cb: astore 11
      // 1cd: aload 11
      // 1cf: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 1d2: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1d5: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 1d8: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 1db: aload 0
      // 1dc: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 1df: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 1e2: aload 0
      // 1e3: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 1e6: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 1e9: dmul
      // 1ea: dcmpl
      // 1eb: ifle 1f2
      // 1ee: aload 10
      // 1f0: astore 11
      // 1f2: aload 0
      // 1f3: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2475 Ldev/boze/client/settings/EnumSetting;
      // 1f6: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 1f9: getstatic dev/boze/client/enums/TrackMode.Attack Ldev/boze/client/enums/TrackMode;
      // 1fc: if_acmpeq 236
      // 1ff: aload 0
      // 200: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2475 Ldev/boze/client/settings/EnumSetting;
      // 203: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 206: getstatic dev/boze/client/enums/TrackMode.Off Ldev/boze/client/enums/TrackMode;
      // 209: if_acmpeq 222
      // 20c: aload 5
      // 20e: invokeinterface java/util/List.stream ()Ljava/util/stream/Stream; 1
      // 213: aload 8
      // 215: invokedynamic test (Lnet/minecraft/entity/Entity;)Ljava/util/function/Predicate; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Z, dev/boze/client/systems/modules/combat/Aura/AuraGhost.lambda$onRotate$2 (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Pair;)Z, (Lnet/minecraft/util/Pair;)Z ]
      // 21a: invokeinterface java/util/stream/Stream.noneMatch (Ljava/util/function/Predicate;)Z 2
      // 21f: ifne 236
      // 222: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 225: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 228: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 22b: aload 11
      // 22d: invokestatic mapped/Class5924.method117 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Z
      // 230: ifne 236
      // 233: goto 0ab
      // 236: aload 0
      // 237: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2496 Ljava/util/concurrent/CopyOnWriteArrayList;
      // 23a: new net/minecraft/util/Pair
      // 23d: dup
      // 23e: aload 8
      // 240: aload 11
      // 242: invokespecial net/minecraft/util/Pair.<init> (Ljava/lang/Object;Ljava/lang/Object;)V
      // 245: invokevirtual java/util/concurrent/CopyOnWriteArrayList.add (Ljava/lang/Object;)Z
      // 248: pop
      // 249: goto 0ab
      // 24c: aload 0
      // 24d: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2496 Ljava/util/concurrent/CopyOnWriteArrayList;
      // 250: invokevirtual java/util/concurrent/CopyOnWriteArrayList.isEmpty ()Z
      // 253: ifne 323
      // 256: aload 0
      // 257: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2496 Ljava/util/concurrent/CopyOnWriteArrayList;
      // 25a: invokedynamic apply ()Ljava/util/function/Function; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Ljava/lang/Object;, dev/boze/client/systems/modules/combat/Aura/AuraGhost.lambda$onRotate$3 (Lnet/minecraft/util/Pair;)Ljava/lang/Boolean;, (Lnet/minecraft/util/Pair;)Ljava/lang/Boolean; ]
      // 25f: invokestatic java/util/Comparator.comparing (Ljava/util/function/Function;)Ljava/util/Comparator;
      // 262: aload 0
      // 263: invokedynamic apply (Ldev/boze/client/systems/modules/combat/Aura/AuraGhost;)Ljava/util/function/Function; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Ljava/lang/Object;, dev/boze/client/systems/modules/combat/Aura/AuraGhost.lambda$onRotate$4 (Lnet/minecraft/util/Pair;)Ljava/lang/Boolean;, (Lnet/minecraft/util/Pair;)Ljava/lang/Boolean; ]
      // 268: invokeinterface java/util/Comparator.thenComparing (Ljava/util/function/Function;)Ljava/util/Comparator; 2
      // 26d: aload 0
      // 26e: invokedynamic apply (Ldev/boze/client/systems/modules/combat/Aura/AuraGhost;)Ljava/util/function/Function; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Ljava/lang/Object;, dev/boze/client/systems/modules/combat/Aura/AuraGhost.lambda$onRotate$5 (Lnet/minecraft/util/Pair;)Ljava/lang/Boolean;, (Lnet/minecraft/util/Pair;)Ljava/lang/Boolean; ]
      // 273: invokeinterface java/util/Comparator.thenComparing (Ljava/util/function/Function;)Ljava/util/Comparator; 2
      // 278: aload 0
      // 279: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2500 Ljava/util/Comparator;
      // 27c: invokeinterface java/util/Comparator.thenComparing (Ljava/util/Comparator;)Ljava/util/Comparator; 2
      // 281: invokevirtual java/util/concurrent/CopyOnWriteArrayList.sort (Ljava/util/Comparator;)V
      // 284: aload 0
      // 285: aload 1
      // 286: aload 6
      // 288: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1430 (Ldev/boze/client/events/eJ;Ldev/boze/client/utils/RotationHelper;)Z
      // 28b: ifeq 28f
      // 28e: return
      // 28f: aload 0
      // 290: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2496 Ljava/util/concurrent/CopyOnWriteArrayList;
      // 293: bipush 0
      // 294: invokevirtual java/util/concurrent/CopyOnWriteArrayList.get (I)Ljava/lang/Object;
      // 297: checkcast net/minecraft/util/Pair
      // 29a: astore 7
      // 29c: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 29f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2a2: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 2a5: aload 7
      // 2a7: invokevirtual net/minecraft/util/Pair.getRight ()Ljava/lang/Object;
      // 2aa: checkcast net/minecraft/util/math/Vec3d
      // 2ad: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 2b0: astore 8
      // 2b2: aload 6
      // 2b4: aload 8
      // 2b6: aload 0
      // 2b7: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2470 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 2ba: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1287 ()[D
      // 2bd: invokevirtual dev/boze/client/utils/RotationHelper.method603 (Ldev/boze/client/utils/RotationHelper;[D)Ldev/boze/client/utils/RotationHelper;
      // 2c0: astore 9
      // 2c2: aload 0
      // 2c3: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 2c6: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 2c9: aload 9
      // 2cb: bipush 1
      // 2cc: invokestatic mapped/Class5924.method73 (DLdev/boze/client/utils/RotationHelper;Z)Lnet/minecraft/util/hit/HitResult;
      // 2cf: astore 10
      // 2d1: aload 0
      // 2d2: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2477 Ldev/boze/client/settings/BooleanSetting;
      // 2d5: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 2d8: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 2db: ifeq 311
      // 2de: aload 10
      // 2e0: instanceof net/minecraft/util/hit/EntityHitResult
      // 2e3: ifne 311
      // 2e6: aload 7
      // 2e8: invokevirtual net/minecraft/util/Pair.getRight ()Ljava/lang/Object;
      // 2eb: checkcast net/minecraft/util/math/Vec3d
      // 2ee: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 2f1: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2f4: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 2f7: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 2fa: aload 0
      // 2fb: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 2fe: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 301: aload 0
      // 302: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 305: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 308: dmul
      // 309: dcmpg
      // 30a: ifgt 311
      // 30d: aload 8
      // 30f: astore 9
      // 311: aload 1
      // 312: aload 9
      // 314: aload 0
      // 315: invokedynamic apply (Ldev/boze/client/systems/modules/combat/Aura/AuraGhost;)Ljava/util/function/Function; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Ljava/lang/Object;, dev/boze/client/systems/modules/combat/Aura/AuraGhost.lambda$onRotate$6 (Ldev/boze/client/utils/RotationHelper;)Ljava/lang/Boolean;, (Ldev/boze/client/utils/RotationHelper;)Ljava/lang/Boolean; ]
      // 31a: invokevirtual dev/boze/client/utils/RotationHelper.method600 (Ljava/util/function/Function;)Ldev/boze/client/utils/RotationHelper;
      // 31d: invokevirtual dev/boze/client/events/eJ.method1099 (Ldev/boze/client/utils/RotationHelper;)V
      // 320: goto 32f
      // 323: aload 0
      // 324: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1416 ()V
      // 327: aload 0
      // 328: aload 1
      // 329: aload 6
      // 32b: invokevirtual dev/boze/client/systems/modules/combat/Aura/AuraGhost.method1430 (Ldev/boze/client/events/eJ;Ldev/boze/client/utils/RotationHelper;)Z
      // 32e: pop
      // 32f: return
   }

   public void method1418(OpenScreenEvent event) {
      if (event.screen instanceof DeathScreen && this.field2488.method419()) {
         this.field2460.setEnabled(false);
      }
   }

   public void method1419(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && this.field2489.method419() && MinecraftUtils.isClientActive()) {
         this.field2460.setEnabled(false);
      }
   }

   public void method1420(PreTickEvent event) {
      if (this.field2497) {
         this.field2461.method2172();
      }

      this.field2497 = true;
   }

   public void method1421(RotationEvent event) {
      this.field2497 = false;
      if (this.field2465.method419() || !(mc.currentScreen instanceof HandledScreen)) {
         if (!this.field2466.method419()
            || mc.player.getMainHandStack().getItem() instanceof SwordItem
            || mc.player.getMainHandStack().getItem() instanceof AxeItem) {
            if (!this.field2467.method419() || mc.options.attackKey.isPressed()) {
               ArrayList<Pair> var5 = new ArrayList<>();
               this.field2496.stream().filter(this::lambda$onInteract$7).forEach(this::lambda$onInteract$8);
               if (!var5.isEmpty() && !event.method2114()) {
                  int var6 = this.field2461.method2171();
                  if (this.field2486.method461() == AuraAntiBlock.Always
                     && var6 == 0
                     && mc.player.getMainHandStack().getItem() instanceof AxeItem
                     && var5.stream().anyMatch(AuraGhost::lambda$onInteract$9)) {
                     var6 = 1;
                  }

                  if (!mc.player.isUsingItem() && !this.field2468.method461().method2114()) {
                     boolean var7 = false;

                     for (Pair var9 : var5) {
                        Entity var10 = (Entity)var9.getLeft();
                        Vec3d var11 = (Vec3d)var9.getRight();
                        this.method1423(var10, var6, var11);
                        event.method2142();
                        var7 = true;
                        if (this.field2483.method461() == Targeting.Single) {
                           break;
                        }
                     }

                     if (!var7 && this.field2482.method419()) {
                        HitResult var12 = Class5924.method73(
                           this.field2469.method1295(),
                           GhostRotations.INSTANCE.field760 != null ? GhostRotations.INSTANCE.field760 : new RotationHelper(mc.player),
                           false
                        );
                        if (var12 instanceof Class3089) {
                           this.method1423(null, var6, null);
                           event.method2142();
                        }
                     }

                     this.field2469.method1296();
                  }
               } else {
                  this.field2461.method2172();
               }
            }
         }
      }
   }

   public void method1422(Render3DEvent event) {
      if (!this.field2499.hasElapsed(2500.0) && GhostRotations.INSTANCE.field760 != null && this.field2498 != null) {
         double var4 = MathHelper.lerp(event.field1951, this.field2498.lastRenderX, this.field2498.getX()) - this.field2498.getX();
         double var6 = MathHelper.lerp(event.field1951, this.field2498.lastRenderY, this.field2498.getY()) - this.field2498.getY();
         double var8 = MathHelper.lerp(event.field1951, this.field2498.lastRenderZ, this.field2498.getZ()) - this.field2498.getZ();
         Box var10 = this.field2498.getBoundingBox();
         if (var10 != null) {
            event.field1950
               .method1271(
                  var4 + var10.minX,
                  var6 + var10.minY,
                  var8 + var10.minZ,
                  var4 + var10.maxX,
                  var6 + var10.maxY,
                  var8 + var10.maxZ,
                  this.field2463.method1362(),
                  this.field2464.method1362(),
                  ShapeMode.Full,
                  0
               );
         }
      }
   }

   private void method1423(Entity var1, int var2, Vec3d var3) {
      for (int var7 = 0; var7 < var2; var7++) {
         Class5924.method72(var1, var3);
      }

      if (var1 != null) {
         this.field2498 = var1;
         this.field2499.reset();
      }
   }

   private Vec3d method1424(Box param1, Entity param2, RotationHelper param3) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 1
      // 001: invokevirtual net/minecraft/util/math/Box.getCenter ()Lnet/minecraft/util/math/Vec3d;
      // 004: astore 7
      // 006: aload 0
      // 007: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2473 Ldev/boze/client/settings/EnumSetting;
      // 00a: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 00d: checkcast dev/boze/client/enums/AimPoint
      // 010: invokevirtual dev/boze/client/enums/AimPoint.ordinal ()I
      // 013: tableswitch 71 0 2 25 47 65
      // 02c: aload 1
      // 02d: aload 3
      // 02e: invokevirtual dev/boze/client/utils/RotationHelper.method1954 ()Lnet/minecraft/util/math/Vec3d;
      // 031: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 034: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 037: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 03a: invokestatic mapped/Class5917.method136 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 03d: astore 7
      // 03f: goto 05a
      // 042: aload 1
      // 043: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 046: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 049: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 04c: invokestatic mapped/Class5917.method123 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 04f: astore 7
      // 051: goto 05a
      // 054: aload 1
      // 055: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 058: astore 7
      // 05a: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 05d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 060: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 063: aload 7
      // 065: invokestatic mapped/Class5924.method117 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Z
      // 068: istore 8
      // 06a: bipush 0
      // 06b: istore 9
      // 06d: aload 0
      // 06e: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2476 Ldev/boze/client/settings/BooleanSetting;
      // 071: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 074: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 077: ifeq 17b
      // 07a: iload 8
      // 07c: ifne 17b
      // 07f: aconst_null
      // 080: astore 10
      // 082: ldc2_w Infinity
      // 085: dstore 11
      // 087: dconst_0
      // 088: dstore 13
      // 08a: dload 13
      // 08c: dconst_1
      // 08d: dcmpg
      // 08e: ifgt 16f
      // 091: iload 9
      // 093: iinc 9 1
      // 096: sipush 1000
      // 099: if_icmpge 16f
      // 09c: dconst_0
      // 09d: dstore 15
      // 09f: dload 15
      // 0a1: dconst_1
      // 0a2: dcmpg
      // 0a3: ifgt 15b
      // 0a6: iload 9
      // 0a8: iinc 9 1
      // 0ab: sipush 1000
      // 0ae: if_icmpge 15b
      // 0b1: dconst_0
      // 0b2: dstore 17
      // 0b4: dload 17
      // 0b6: dconst_1
      // 0b7: dcmpg
      // 0b8: ifgt 147
      // 0bb: iload 9
      // 0bd: iinc 9 1
      // 0c0: sipush 1000
      // 0c3: if_icmpge 147
      // 0c6: new net/minecraft/util/math/Vec3d
      // 0c9: dup
      // 0ca: aload 1
      // 0cb: getfield net/minecraft/util/math/Box.minX D
      // 0ce: aload 1
      // 0cf: getfield net/minecraft/util/math/Box.maxX D
      // 0d2: aload 1
      // 0d3: getfield net/minecraft/util/math/Box.minX D
      // 0d6: dsub
      // 0d7: dload 13
      // 0d9: dmul
      // 0da: dadd
      // 0db: aload 1
      // 0dc: getfield net/minecraft/util/math/Box.minY D
      // 0df: aload 1
      // 0e0: getfield net/minecraft/util/math/Box.maxY D
      // 0e3: aload 1
      // 0e4: getfield net/minecraft/util/math/Box.minY D
      // 0e7: dsub
      // 0e8: dload 15
      // 0ea: dmul
      // 0eb: dadd
      // 0ec: aload 1
      // 0ed: getfield net/minecraft/util/math/Box.minZ D
      // 0f0: aload 1
      // 0f1: getfield net/minecraft/util/math/Box.maxZ D
      // 0f4: aload 1
      // 0f5: getfield net/minecraft/util/math/Box.minZ D
      // 0f8: dsub
      // 0f9: dload 17
      // 0fb: dmul
      // 0fc: dadd
      // 0fd: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 100: astore 19
      // 102: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 105: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 108: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 10b: aload 19
      // 10d: invokestatic mapped/Class5924.method117 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Z
      // 110: ifeq 133
      // 113: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 116: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 119: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 11c: aload 19
      // 11e: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 121: dstore 20
      // 123: dload 20
      // 125: dload 11
      // 127: dcmpg
      // 128: ifge 133
      // 12b: aload 19
      // 12d: astore 10
      // 12f: dload 20
      // 131: dstore 11
      // 133: dload 17
      // 135: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 138: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 13b: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 13e: invokevirtual java/lang/Double.doubleValue ()D
      // 141: dadd
      // 142: dstore 17
      // 144: goto 0b4
      // 147: dload 15
      // 149: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 14c: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 14f: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 152: invokevirtual java/lang/Double.doubleValue ()D
      // 155: dadd
      // 156: dstore 15
      // 158: goto 09f
      // 15b: dload 13
      // 15d: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 160: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 163: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 166: invokevirtual java/lang/Double.doubleValue ()D
      // 169: dadd
      // 16a: dstore 13
      // 16c: goto 08a
      // 16f: aload 10
      // 171: ifnull 17b
      // 174: aload 10
      // 176: astore 7
      // 178: bipush 1
      // 179: istore 8
      // 17b: new net/minecraft/util/math/Vec3d
      // 17e: dup
      // 17f: aload 7
      // 181: getfield net/minecraft/util/math/Vec3d.x D
      // 184: aload 7
      // 186: getfield net/minecraft/util/math/Vec3d.y D
      // 189: aload 7
      // 18b: getfield net/minecraft/util/math/Vec3d.z D
      // 18e: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 191: astore 10
      // 193: aload 1
      // 194: invokevirtual net/minecraft/util/math/Box.getCenter ()Lnet/minecraft/util/math/Vec3d;
      // 197: astore 11
      // 199: dconst_1
      // 19a: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 19d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1a0: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 1a3: aload 10
      // 1a5: invokevirtual net/minecraft/util/math/Vec3d.distanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 1a8: aload 0
      // 1a9: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 1ac: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 1af: aload 0
      // 1b0: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 1b3: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 1b6: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 1b9: getfield dev/boze/client/systems/modules/client/GhostRotations.field749 Ldev/boze/client/settings/MinMaxSetting;
      // 1bc: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 1bf: invokevirtual java/lang/Double.doubleValue ()D
      // 1c2: dadd
      // 1c3: invokestatic java/lang/Math.max (DD)D
      // 1c6: ddiv
      // 1c7: dconst_1
      // 1c8: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 1cb: getfield dev/boze/client/systems/modules/client/GhostRotations.field748 Ldev/boze/client/settings/MinMaxSetting;
      // 1ce: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 1d1: invokevirtual java/lang/Double.doubleValue ()D
      // 1d4: dsub
      // 1d5: invokestatic mapped/Class5917.method32 (DD)D
      // 1d8: dsub
      // 1d9: dstore 12
      // 1db: aload 10
      // 1dd: aload 11
      // 1df: getfield net/minecraft/util/math/Vec3d.x D
      // 1e2: aload 10
      // 1e4: getfield net/minecraft/util/math/Vec3d.x D
      // 1e7: dsub
      // 1e8: dload 12
      // 1ea: dmul
      // 1eb: aload 11
      // 1ed: getfield net/minecraft/util/math/Vec3d.y D
      // 1f0: aload 10
      // 1f2: getfield net/minecraft/util/math/Vec3d.y D
      // 1f5: dsub
      // 1f6: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 1f9: getfield dev/boze/client/systems/modules/client/GhostRotations.field758 Ldev/boze/client/settings/BooleanSetting;
      // 1fc: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 1ff: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 202: ifeq 20c
      // 205: dconst_1
      // 206: dload 12
      // 208: dsub
      // 209: goto 20d
      // 20c: dconst_1
      // 20d: dmul
      // 20e: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 211: getfield dev/boze/client/systems/modules/client/GhostRotations.field757 Ldev/boze/client/settings/MinMaxSetting;
      // 214: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 217: invokevirtual java/lang/Double.doubleValue ()D
      // 21a: dmul
      // 21b: aload 11
      // 21d: getfield net/minecraft/util/math/Vec3d.z D
      // 220: aload 10
      // 222: getfield net/minecraft/util/math/Vec3d.z D
      // 225: dsub
      // 226: dload 12
      // 228: dmul
      // 229: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 22c: astore 10
      // 22e: new net/minecraft/util/math/Vec3d
      // 231: dup
      // 232: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 235: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 238: getfield net/minecraft/client/network/ClientPlayerEntity.prevX D
      // 23b: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 23e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 241: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 244: dsub
      // 245: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 248: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 24b: getfield net/minecraft/client/network/ClientPlayerEntity.prevY D
      // 24e: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 251: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 254: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 257: dsub
      // 258: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 25b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 25e: getfield net/minecraft/client/network/ClientPlayerEntity.prevZ D
      // 261: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 264: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 267: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 26a: dsub
      // 26b: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 26e: astore 14
      // 270: aload 14
      // 272: aload 2
      // 273: getfield net/minecraft/entity/Entity.prevX D
      // 276: aload 2
      // 277: invokevirtual net/minecraft/entity/Entity.getX ()D
      // 27a: dsub
      // 27b: aload 2
      // 27c: getfield net/minecraft/entity/Entity.prevY D
      // 27f: aload 2
      // 280: invokevirtual net/minecraft/entity/Entity.getY ()D
      // 283: dsub
      // 284: aload 2
      // 285: getfield net/minecraft/entity/Entity.prevZ D
      // 288: aload 2
      // 289: invokevirtual net/minecraft/entity/Entity.getZ ()D
      // 28c: dsub
      // 28d: invokevirtual net/minecraft/util/math/Vec3d.subtract (DDD)Lnet/minecraft/util/math/Vec3d;
      // 290: astore 15
      // 292: aload 15
      // 294: invokevirtual net/minecraft/util/math/Vec3d.lengthSquared ()D
      // 297: dconst_0
      // 298: dcmpl
      // 299: ifle 321
      // 29c: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 29f: getfield dev/boze/client/systems/modules/client/GhostRotations.field752 Ldev/boze/client/settings/BooleanSetting;
      // 2a2: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 2a5: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 2a8: ifeq 2b3
      // 2ab: aload 15
      // 2ad: invokevirtual net/minecraft/util/math/Vec3d.horizontalLength ()D
      // 2b0: goto 2b4
      // 2b3: dconst_1
      // 2b4: dstore 16
      // 2b6: aload 10
      // 2b8: invokestatic java/lang/System.currentTimeMillis ()J
      // 2bb: l2d
      // 2bc: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2bf: getfield dev/boze/client/systems/modules/client/GhostRotations.field755 Ldev/boze/client/settings/MinMaxSetting;
      // 2c2: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 2c5: invokevirtual java/lang/Double.doubleValue ()D
      // 2c8: dmul
      // 2c9: ldc2_w 0.01
      // 2cc: dmul
      // 2cd: invokestatic java/lang/Math.sin (D)D
      // 2d0: dload 16
      // 2d2: dmul
      // 2d3: invokestatic java/lang/System.currentTimeMillis ()J
      // 2d6: l2d
      // 2d7: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2da: getfield dev/boze/client/systems/modules/client/GhostRotations.field756 Ldev/boze/client/settings/MinMaxSetting;
      // 2dd: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 2e0: invokevirtual java/lang/Double.doubleValue ()D
      // 2e3: dmul
      // 2e4: ldc2_w 0.01
      // 2e7: dmul
      // 2e8: invokestatic java/lang/Math.cos (D)D
      // 2eb: aload 15
      // 2ed: getfield net/minecraft/util/math/Vec3d.y D
      // 2f0: dload 16
      // 2f2: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2f5: getfield dev/boze/client/systems/modules/client/GhostRotations.field753 Ldev/boze/client/settings/MinMaxSetting;
      // 2f8: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 2fb: invokevirtual java/lang/Double.doubleValue ()D
      // 2fe: dmul
      // 2ff: dadd
      // 300: dmul
      // 301: invokestatic java/lang/System.currentTimeMillis ()J
      // 304: l2d
      // 305: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 308: getfield dev/boze/client/systems/modules/client/GhostRotations.field755 Ldev/boze/client/settings/MinMaxSetting;
      // 30b: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 30e: invokevirtual java/lang/Double.doubleValue ()D
      // 311: dmul
      // 312: ldc2_w 0.01
      // 315: dmul
      // 316: invokestatic java/lang/Math.sin (D)D
      // 319: dload 16
      // 31b: dmul
      // 31c: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 31f: astore 10
      // 321: aload 10
      // 323: aload 15
      // 325: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 328: getfield dev/boze/client/systems/modules/client/GhostRotations.field751 Ldev/boze/client/settings/MinMaxSetting;
      // 32b: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 32e: invokevirtual java/lang/Double.doubleValue ()D
      // 331: invokevirtual net/minecraft/util/math/Vec3d.multiply (D)Lnet/minecraft/util/math/Vec3d;
      // 334: invokevirtual net/minecraft/util/math/Vec3d.subtract (Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 337: astore 10
      // 339: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 33c: getfield dev/boze/client/systems/modules/client/GhostRotations.field760 Ldev/boze/client/utils/RotationHelper;
      // 33f: ifnull 34b
      // 342: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 345: getfield dev/boze/client/systems/modules/client/GhostRotations.field760 Ldev/boze/client/utils/RotationHelper;
      // 348: goto 358
      // 34b: new dev/boze/client/utils/RotationHelper
      // 34e: dup
      // 34f: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 352: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 355: invokespecial dev/boze/client/utils/RotationHelper.<init> (Lnet/minecraft/entity/Entity;)V
      // 358: astore 16
      // 35a: aload 16
      // 35c: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 35f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 362: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 365: aload 10
      // 367: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 36a: invokevirtual dev/boze/client/utils/RotationHelper.method605 (Ldev/boze/client/utils/RotationHelper;)F
      // 36d: f2d
      // 36e: ldc2_w 255.0
      // 371: ddiv
      // 372: dstore 17
      // 374: aload 10
      // 376: dconst_0
      // 377: dload 17
      // 379: dneg
      // 37a: aload 1
      // 37b: invokevirtual net/minecraft/util/math/Box.getLengthY ()D
      // 37e: dmul
      // 37f: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 382: getfield dev/boze/client/systems/modules/client/GhostRotations.field750 Ldev/boze/client/settings/MinMaxSetting;
      // 385: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 388: invokevirtual java/lang/Double.doubleValue ()D
      // 38b: dconst_1
      // 38c: dsub
      // 38d: dmul
      // 38e: dconst_0
      // 38f: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 392: astore 10
      // 394: iload 8
      // 396: ifeq 419
      // 399: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 39c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 39f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 3a2: aload 10
      // 3a4: invokestatic mapped/Class5924.method117 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Z
      // 3a7: ifne 419
      // 3aa: aload 0
      // 3ab: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2476 Ldev/boze/client/settings/BooleanSetting;
      // 3ae: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 3b1: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 3b4: ifeq 419
      // 3b7: iload 9
      // 3b9: iinc 9 1
      // 3bc: sipush 1000
      // 3bf: if_icmpge 419
      // 3c2: new net/minecraft/util/math/Vec3d
      // 3c5: dup
      // 3c6: aload 10
      // 3c8: getfield net/minecraft/util/math/Vec3d.x D
      // 3cb: aload 7
      // 3cd: getfield net/minecraft/util/math/Vec3d.x D
      // 3d0: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 3d3: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 3d6: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 3d9: invokevirtual java/lang/Double.doubleValue ()D
      // 3dc: invokestatic mapped/Class5917.method35 (DDD)D
      // 3df: aload 10
      // 3e1: getfield net/minecraft/util/math/Vec3d.y D
      // 3e4: aload 7
      // 3e6: getfield net/minecraft/util/math/Vec3d.y D
      // 3e9: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 3ec: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 3ef: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 3f2: invokevirtual java/lang/Double.doubleValue ()D
      // 3f5: invokestatic mapped/Class5917.method35 (DDD)D
      // 3f8: aload 10
      // 3fa: getfield net/minecraft/util/math/Vec3d.z D
      // 3fd: aload 7
      // 3ff: getfield net/minecraft/util/math/Vec3d.z D
      // 402: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 405: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 408: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 40b: invokevirtual java/lang/Double.doubleValue ()D
      // 40e: invokestatic mapped/Class5917.method35 (DDD)D
      // 411: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 414: astore 10
      // 416: goto 394
      // 419: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 41c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 41f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 422: aload 7
      // 424: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 427: dstore 19
      // 429: dload 19
      // 42b: aload 0
      // 42c: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 42f: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 432: aload 0
      // 433: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 436: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 439: dmul
      // 43a: dcmpg
      // 43b: ifgt 500
      // 43e: iload 8
      // 440: ifeq 500
      // 443: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 446: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 449: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 44c: aload 10
      // 44e: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 451: aload 0
      // 452: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 455: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 458: aload 0
      // 459: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 45c: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 45f: dmul
      // 460: dcmpl
      // 461: ifle 4c6
      // 464: iload 9
      // 466: iinc 9 1
      // 469: sipush 1000
      // 46c: if_icmpge 4c6
      // 46f: new net/minecraft/util/math/Vec3d
      // 472: dup
      // 473: aload 10
      // 475: getfield net/minecraft/util/math/Vec3d.x D
      // 478: aload 7
      // 47a: getfield net/minecraft/util/math/Vec3d.x D
      // 47d: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 480: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 483: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 486: invokevirtual java/lang/Double.doubleValue ()D
      // 489: invokestatic mapped/Class5917.method35 (DDD)D
      // 48c: aload 10
      // 48e: getfield net/minecraft/util/math/Vec3d.y D
      // 491: aload 7
      // 493: getfield net/minecraft/util/math/Vec3d.y D
      // 496: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 499: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 49c: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 49f: invokevirtual java/lang/Double.doubleValue ()D
      // 4a2: invokestatic mapped/Class5917.method35 (DDD)D
      // 4a5: aload 10
      // 4a7: getfield net/minecraft/util/math/Vec3d.z D
      // 4aa: aload 7
      // 4ac: getfield net/minecraft/util/math/Vec3d.z D
      // 4af: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 4b2: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 4b5: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 4b8: invokevirtual java/lang/Double.doubleValue ()D
      // 4bb: invokestatic mapped/Class5917.method35 (DDD)D
      // 4be: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 4c1: astore 10
      // 4c3: goto 443
      // 4c6: aload 0
      // 4c7: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 4ca: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 4cd: getstatic dev/boze/client/systems/modules/combat/Aura/AuraGhost.mc Lnet/minecraft/client/MinecraftClient;
      // 4d0: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 4d3: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 4d6: aload 10
      // 4d8: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 4db: bipush 1
      // 4dc: invokestatic mapped/Class5924.method73 (DLdev/boze/client/utils/RotationHelper;Z)Lnet/minecraft/util/hit/HitResult;
      // 4df: astore 21
      // 4e1: aload 21
      // 4e3: ifnull 4f9
      // 4e6: aload 21
      // 4e8: instanceof net/minecraft/util/hit/EntityHitResult
      // 4eb: ifeq 4f9
      // 4ee: aload 21
      // 4f0: checkcast net/minecraft/util/hit/EntityHitResult
      // 4f3: invokevirtual net/minecraft/util/hit/EntityHitResult.getEntity ()Lnet/minecraft/entity/Entity;
      // 4f6: ifnonnull 4fd
      // 4f9: aload 7
      // 4fb: astore 10
      // 4fd: goto 568
      // 500: aload 0
      // 501: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 504: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 507: aload 0
      // 508: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 50b: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1295 ()D
      // 50e: dcmpl
      // 50f: ifle 568
      // 512: dload 19
      // 514: invokestatic java/lang/Math.sqrt (D)D
      // 517: aload 0
      // 518: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 51b: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1294 ()D
      // 51e: dsub
      // 51f: aload 0
      // 520: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 523: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1294 ()D
      // 526: aload 0
      // 527: getfield dev/boze/client/systems/modules/combat/Aura/AuraGhost.field2469 Ldev/boze/client/settings/MinMaxDoubleSetting;
      // 52a: invokevirtual dev/boze/client/settings/MinMaxDoubleSetting.method1293 ()D
      // 52d: dsub
      // 52e: ddiv
      // 52f: dstore 19
      // 531: aload 10
      // 533: aload 7
      // 535: getfield net/minecraft/util/math/Vec3d.x D
      // 538: aload 10
      // 53a: getfield net/minecraft/util/math/Vec3d.x D
      // 53d: dsub
      // 53e: dconst_1
      // 53f: dload 19
      // 541: dsub
      // 542: dmul
      // 543: aload 7
      // 545: getfield net/minecraft/util/math/Vec3d.y D
      // 548: aload 10
      // 54a: getfield net/minecraft/util/math/Vec3d.y D
      // 54d: dsub
      // 54e: dconst_1
      // 54f: dload 19
      // 551: dsub
      // 552: dmul
      // 553: aload 7
      // 555: getfield net/minecraft/util/math/Vec3d.z D
      // 558: aload 10
      // 55a: getfield net/minecraft/util/math/Vec3d.z D
      // 55d: dsub
      // 55e: dconst_1
      // 55f: dload 19
      // 561: dsub
      // 562: dmul
      // 563: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 566: astore 10
      // 568: aload 10
      // 56a: aload 1
      // 56b: invokestatic mapped/Class5917.method33 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 56e: astore 7
      // 570: aload 7
      // 572: areturn
   }

   private boolean method1425(Entity var1) {
      if (!(mc.player.getMainHandStack().getItem() instanceof AxeItem)) {
          return this.field2487.method419()
                  || !(var1 instanceof LivingEntity)
                  || !((LivingEntity) var1).isBlocking()
                  || !((LivingEntity) var1).blockedByShield(var1.getDamageSources().playerAttack(mc.player));
      } else return this.field2486.method461() != AuraAntiBlock.On
              || !(var1 instanceof PlayerEntity var5)
              || !(var5.getOffHandStack().getItem() instanceof ShieldItem)
              || (var5.isBlocking() && var5.blockedByShield(var1.getDamageSources().playerAttack(mc.player)));
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1426(Entity var1) {
      if (var1.isSpectator()) {
         return false;
      } else if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else if (Friends.method2055(var1)) {
            return this.field2492.method419();
         } else {
            return !AntiBots.method2055(var1) && this.field2491.method419();
         }
      } else if (var1 instanceof FireballEntity) {
         return this.field2495.method419();
      } else {
         switch (eI.field2096[var1.getType().getSpawnGroup().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return this.field2493.method419();
            case 5:
               return this.field2494.method419();
            default:
               return false;
         }
      }
   }

   private RotationHelper method1427() {
      return this.field2471.method461() == AuraReference.Server && GhostRotations.INSTANCE.field760 != null
         ? GhostRotations.INSTANCE.field760
         : new RotationHelper(mc.player);
   }

   private boolean method1428(Entity var1) {
      return var1 instanceof PlayerEntity var5 && (var5.getInventory().getMainHandStack().getItem() == Items.SHIELD || var5.getInventory().offHand.get(0).getItem() == Items.SHIELD);
   }

   private boolean method1429(Predicate<LivingEntity> var1) {
      if (this.field2483.method461() == Targeting.Single) {
         Optional<Entity> var5 = this.field2496.stream().findFirst().map(AuraGhost::lambda$allAttackedLivingEntities$10);
         if (var5.isPresent() && var5.get() instanceof LivingEntity var6) {
             return var1.test(var6);
         } else {
            return false;
         }
      } else {
         return this.field2496.stream().allMatch(var0 -> ((Pair<Entity, Vec3d>) null).getLeft() instanceof LivingEntity && ((Predicate<Entity>) var0).test(((Pair<Entity, Vec3d>) null).getLeft()));
      }
   }

   public boolean method1430(eJ event, RotationHelper currentRot) {
      if (!this.field2465.method419() && mc.currentScreen instanceof HandledScreen) {
         if (!event.method1101() && GhostRotations.INSTANCE.field760 != null) {
            event.method1099(currentRot);
         }

         return true;
      } else {
         return false;
      }
   }

   private static Entity lambda$allAttackedLivingEntities$10(Pair<Entity, Vec3d> var0) {
      return var0.getLeft();
   }

   private static boolean lambda$onInteract$9(Pair<Entity, Vec3d> var0) {
      return var0.getLeft() instanceof LivingEntity
         && ((LivingEntity)var0.getLeft()).isBlocking()
         && ((LivingEntity)var0.getLeft()).blockedByShield(((Entity)var0.getLeft()).getDamageSources().playerAttack(mc.player));
   }

   private void lambda$onInteract$8(ArrayList<Pair<Entity, Vec3d>> var1, Pair<Entity, Vec3d> var2) {
      Entity var6 = var2.getLeft();
      Vec3d var7 = var2.getRight();
      double var8 = var7.squaredDistanceTo(mc.player.getEyePos());
      if (this.field2474.method419()) {
         if (GhostRotations.INSTANCE.field760 == null) {
            return;
         }

         HitResult var10 = Class5924.method73(
            this.field2469.method1295(),
            this.field2483.method461() == Targeting.Single
               ? (
                  this.field2481.method419()
                     ? new RotationHelper(((ClientPlayerEntityAccessor)mc.player).getLastYaw(), ((ClientPlayerEntityAccessor)mc.player).getLastPitch())
                     : GhostRotations.INSTANCE.field760
               )
               : Class1202.method2391(mc.player.getEyePos(), var7),
            this.field2475.method461() == TrackMode.Attack
         );
         if (!(var10 instanceof EntityHitResult) || ((EntityHitResult) var10).getEntity() == null) {
            return;
         }

         var6 = ((EntityHitResult)var10).getEntity();
      } else if (var8 > this.field2469.method1295() * this.field2469.method1295()) {
         return;
      }

      var1.add(new Pair<>(var6, var7));
   }

   private boolean lambda$onInteract$7(Pair<Entity, Vec3d> var1) {
      return this.method1425(var1.getLeft());
   }

   private Boolean lambda$onRotate$6(RotationHelper var1) {
      return Class5924.method73(this.field2469.method1295(), var1, this.field2475.method461() == TrackMode.Attack) instanceof EntityHitResult;
   }

   private Boolean lambda$onRotate$5(Pair<Entity, Vec3d> var1) {
      return mc.player.getEyePos().squaredDistanceTo(var1.getRight()) > this.field2469.method1295() * this.field2469.method1295();
   }

   private Boolean lambda$onRotate$4(Pair<Entity, Vec3d> var1) {
      return !this.method1425(var1.getLeft());
   }

   private static Boolean lambda$onRotate$3(Pair<Entity, Vec3d> var0) {
      return !(var0.getLeft() instanceof LivingEntity) || ((LivingEntity) var0.getLeft()).isDead();
   }

   private static boolean lambda$onRotate$2(Entity var0, Pair<Entity, Vec3d> var1) {
      return var1.getLeft() == var0;
   }

   private Double lambda$new$1(Pair<?, ?> pair) {
      if (this.field2485.method419() && pair.getLeft() == this.field2498) {
         return 0.0;
      }

      AttackPriority priority = this.field2484.method461();
      switch (priority.ordinal()) {
         case 0 -> {
            Vec3d eyePos = AuraGhost.mc.player.getEyePos();
            Entity entity = (Entity) pair.getLeft();
            Box box = entity.getBoundingBox();
            double margin = entity.getTargetingMargin();
            Box expandedBox = box.expand(margin);
            Vec3d targetPos = Class5917.method34(expandedBox);
             return eyePos.squaredDistanceTo(targetPos);
         }
         case 1 -> {
            Object obj = pair.getLeft();
            if (obj instanceof LivingEntity living) {
                return (double) living.getHealth();
            }
            return 0.0;
         }
         case 2 -> {
            Vec3d eyePos = AuraGhost.mc.player.getEyePos();
            Entity entity = (Entity) pair.getLeft();
            Box box = entity.getBoundingBox();
            Vec3d targetPos = Class5917.method34(box);
            RotationHelper computedRotation = Class1202.method2391(eyePos, targetPos);
            return (double) computedRotation.method605(this.method1427());
         }
          default -> {
              return 0.0;
          }
      }
   }

   private boolean lambda$new$0() {
      return this.field2478.method461() == ClickMethod.Vanilla;
   }
}
