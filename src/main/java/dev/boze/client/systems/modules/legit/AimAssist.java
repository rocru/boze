package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AimAssistAimPoint;
import dev.boze.client.enums.AimAssistGunPoint;
import dev.boze.client.enums.AimAssistPriority;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.m_;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class1202;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class AimAssist extends Module {
   public static final AimAssist INSTANCE = new AimAssist();
   private final ItemSetting field2676 = new ItemSetting("Gun", "Gun item");
   private final EnumSetting<AimAssistAimPoint> field2677 = new EnumSetting<AimAssistAimPoint>("AimPoint", AimAssistAimPoint.MinAngle, "Aim point to use");
   private final BooleanSetting field2678 = new BooleanSetting(
      "UseGhostRot",
      true,
      "Use ghost rotation algorithm\nThis will use the PointScale, Tracking, and Noise options from GhostRotations, but Min/Max Speed from here"
   );
   private final BooleanSetting field2679 = new BooleanSetting(
      "StopOverTarget",
      false,
      "Stop assisting when looking at target\nNote: If you are using AimAssist as an AimBot (high speed), having this on will make it look unnatural"
   );
   private final BooleanSetting field2680 = new BooleanSetting("OnlyWeapon", false, "Only assist when holding weapon");
   private final BooleanSetting field2681 = new BooleanSetting("OnlyWhenClicking", true, "Only assist when clicking attack key");
   private final IntSetting field2682 = new IntSetting("Ticks", 5, 1, 40, 1, "Ticks to assist for after clicking", this.field2681::method419);
   private final EnumSetting<AimAssistGunPoint> field2683 = new EnumSetting<AimAssistGunPoint>(
      "GunMode",
      AimAssistGunPoint.Off,
      "Aim assist for gun mini-games\n - Off: Disable gun mode\n - LeftClick: Enable gun mode, assume left click for OnlyWhenClicking\n - RightClick: Enable gun mode, assume right click for OnlyWhenClicking\nSet item for OnlyWeapon: .set aimassist gun <item name>\n"
   );
   private final MinMaxSetting field2684 = new MinMaxSetting("Range", 3.0, 1.0, 7.0, 0.1, "Range to target within", this::lambda$new$0);
   private final MinMaxSetting field2685 = new MinMaxSetting("GunRange", 100.0, 1.0, 300.0, 1.0, "Range to target within", this::lambda$new$1);
   private final BooleanSetting field2686 = new BooleanSetting("ThroughWalls", false, "Target through walls");
   private final MinMaxSetting field2687 = new MinMaxSetting("BoxScale", 0.8, 0.1, 1.0, 0.1, "Scale of the box to target within");
   private final MinMaxDoubleSetting field2688 = new MinMaxDoubleSetting("Speed", new double[]{1.0, 2.0}, 0.1, 10.0, 0.1, "Aim assist speed");
   private final MinMaxDoubleSetting field2689 = new MinMaxDoubleSetting(
      "Delta",
      new double[]{0.0, 5.0},
      0.0,
      10.0,
      0.1,
      "Mouse delta change per poll limits\nWon't assist if delta below min\nWill limit assists to max\nMin: 0 is recommended to avoid fast jitters\n"
   );
   private final BooleanSetting field2690 = new BooleanSetting("DontResist", false, "Don't resist mouse movement in opposite directions");
   private final BooleanSetting field2691 = new BooleanSetting("Vertical", true, "Assist vertical rotation\nThis may increase your chance of getting flagged");
   private final IntSetting field2692 = new IntSetting("FOV", 180, 1, 180, 1, "Maximum FOV to target within");
   private final EnumSetting<AimAssistPriority> field2693 = new EnumSetting<AimAssistPriority>(
      "Priority", AimAssistPriority.Distance, "The priority to target enemies with"
   );
   private final BooleanSetting field2694 = new BooleanSetting("Sticky", false, "Stick to the same target when possible");
   private final SettingCategory field2695 = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting field2696 = new BooleanSetting("Invisible", false, "Target invisible entities", this.field2695);
   private final BooleanSetting field2697 = new BooleanSetting("Players", true, "Target players", this.field2695);
   private final BooleanSetting field2698 = new BooleanSetting("Friends", false, "Target friends", this.field2695);
   private final BooleanSetting field2699 = new BooleanSetting("Animals", false, "Target animals", this.field2695);
   private final BooleanSetting field2700 = new BooleanSetting("Monsters", false, "Target monsters", this.field2695);
   private Entity field2701;
   private final Comparator<Entity> field2702 = Comparator.comparing(this::lambda$new$2);
   private final Timer field2703 = new Timer();

   public AimAssist() {
      super("AimAssist", "Assists your aim", Category.Legit);
   }

   @EventHandler(
      priority = 44
   )
   public void method1572(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive() && !event.method1022()) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
            if (this.field2680.method419()) {
               if (this.field2683.method461() != AimAssistGunPoint.Off) {
                  if (mc.player.getMainHandStack().getItem() != this.field2676.method447()) {
                     return;
                  }
               } else if (!(mc.player.getMainHandStack().getItem() instanceof SwordItem)
                  && !(mc.player.getMainHandStack().getItem() instanceof AxeItem)
                  && !(mc.player.getMainHandStack().getItem() instanceof TridentItem)) {
                  return;
               }
            }

            if (this.field2683.method461() == AimAssistGunPoint.RightClick) {
               if (mc.options.useKey.isPressed()) {
                  this.field2703.reset();
               }
            } else if (mc.options.attackKey.isPressed()) {
               this.field2703.reset();
            }

            if (!this.field2681.method419() || !this.field2703.hasElapsed((double)(this.field2682.method434() * 50))) {
               RotationHelper var5 = new RotationHelper(mc.player);
               Entity var6 = this.method1574();
               if (var6 != null) {
                  if (!this.field2679.method419() || !(mc.crosshairTarget instanceof EntityHitResult var7) || var7.getEntity() != var6) {
                     Box var18 = var6.getBoundingBox().expand((double)var6.getTargetingMargin()).contract(1.0 - this.field2687.getValue());
                     Vec3d var19 = this.method1573(var18, var6, var5);
                     RotationHelper var9 = Class1202.method2391(mc.player.getEyePos(), var19);
                     if (!((double)var9.method605(var5) > (double)this.field2692.method434().intValue() * 1.417)) {
                        RotationHelper var10 = var5.method603(var9, this.field2688.method1287()).method1600();
                        RotationHelper var11 = var10.method606(var5);
                        Pair[] var12 = RotationHelper.method614(var11);
                        Pair var13 = var12[0];

                        for (Pair var17 : var12) {
                           if (Class5924.method73(
                                 this.field2683.method461() != AimAssistGunPoint.Off ? this.field2685.getValue() : this.field2684.getValue(),
                                 RotationHelper.method613(var5, var17),
                                 false
                              )
                              != null) {
                              var13 = var17;
                              break;
                           }
                        }

                        double var20 = MathHelper.clamp((Double)var13.getLeft(), -this.field2689.method1294() * 10.0, this.field2689.method1294() * 10.0);
                        if ((!this.field2690.method419() || event.deltaX * var20 >= 0.0) && Math.abs(var20) > this.field2689.method1293() * 10.0) {
                           event.deltaX += var20;
                           event.method1021(true);
                        }

                        if (this.field2691.method419()) {
                           double var21 = MathHelper.clamp((Double)var13.getRight(), -this.field2689.method1294() * 10.0, this.field2689.method1294() * 10.0);
                           if ((!this.field2690.method419() || event.deltaY * var21 >= 0.0) && Math.abs(var21) > this.field2689.method1293() * 10.0) {
                              event.deltaY += var21;
                              event.method1021(true);
                           }
                        }

                        this.field2701 = var6;
                     }
                  }
               }
            }
         }
      }
   }

   private Vec3d method1573(Box param1, Entity param2, RotationHelper param3) {
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
      // 007: getfield dev/boze/client/systems/modules/legit/AimAssist.field2683 Ldev/boze/client/settings/EnumSetting;
      // 00a: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 00d: getstatic dev/boze/client/enums/AimAssistGunPoint.Off Ldev/boze/client/enums/AimAssistGunPoint;
      // 010: if_acmpeq 01d
      // 013: aload 0
      // 014: getfield dev/boze/client/systems/modules/legit/AimAssist.field2685 Ldev/boze/client/settings/MinMaxSetting;
      // 017: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 01a: goto 024
      // 01d: aload 0
      // 01e: getfield dev/boze/client/systems/modules/legit/AimAssist.field2684 Ldev/boze/client/settings/MinMaxSetting;
      // 021: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 024: invokevirtual java/lang/Double.doubleValue ()D
      // 027: dstore 8
      // 029: aload 0
      // 02a: getfield dev/boze/client/systems/modules/legit/AimAssist.field2677 Ldev/boze/client/settings/EnumSetting;
      // 02d: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 030: checkcast dev/boze/client/enums/AimAssistAimPoint
      // 033: invokevirtual dev/boze/client/enums/AimAssistAimPoint.ordinal ()I
      // 036: tableswitch 164 0 4 34 56 74 164 83
      // 058: aload 1
      // 059: aload 3
      // 05a: invokevirtual dev/boze/client/utils/RotationHelper.method1954 ()Lnet/minecraft/util/math/Vec3d;
      // 05d: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 060: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 063: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 066: invokestatic mapped/Class5917.method136 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 069: astore 7
      // 06b: goto 0da
      // 06e: aload 1
      // 06f: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 072: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 075: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 078: invokestatic mapped/Class5917.method123 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 07b: astore 7
      // 07d: goto 0da
      // 080: aload 1
      // 081: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 084: astore 7
      // 086: goto 0da
      // 089: aload 2
      // 08a: invokevirtual net/minecraft/entity/Entity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 08d: astore 10
      // 08f: new net/minecraft/util/math/Box
      // 092: dup
      // 093: aload 10
      // 095: getfield net/minecraft/util/math/Vec3d.x D
      // 098: ldc2_w 0.25
      // 09b: dsub
      // 09c: aload 10
      // 09e: getfield net/minecraft/util/math/Vec3d.y D
      // 0a1: ldc2_w 0.25
      // 0a4: dsub
      // 0a5: aload 10
      // 0a7: getfield net/minecraft/util/math/Vec3d.z D
      // 0aa: ldc2_w 0.25
      // 0ad: dsub
      // 0ae: aload 10
      // 0b0: getfield net/minecraft/util/math/Vec3d.x D
      // 0b3: ldc2_w 0.25
      // 0b6: dadd
      // 0b7: aload 10
      // 0b9: getfield net/minecraft/util/math/Vec3d.y D
      // 0bc: aload 10
      // 0be: getfield net/minecraft/util/math/Vec3d.z D
      // 0c1: ldc2_w 0.25
      // 0c4: dadd
      // 0c5: invokespecial net/minecraft/util/math/Box.<init> (DDDDDD)V
      // 0c8: astore 11
      // 0ca: aload 11
      // 0cc: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 0cf: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0d2: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 0d5: invokestatic mapped/Class5917.method123 (Lnet/minecraft/util/math/Box;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 0d8: astore 7
      // 0da: aload 0
      // 0db: getfield dev/boze/client/systems/modules/legit/AimAssist.field2678 Ldev/boze/client/settings/BooleanSetting;
      // 0de: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 0e1: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 0e4: ifne 0ea
      // 0e7: aload 7
      // 0e9: areturn
      // 0ea: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 0ed: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0f0: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 0f3: aload 7
      // 0f5: invokestatic mapped/Class5924.method117 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Z
      // 0f8: istore 10
      // 0fa: new net/minecraft/util/math/Vec3d
      // 0fd: dup
      // 0fe: aload 7
      // 100: getfield net/minecraft/util/math/Vec3d.x D
      // 103: aload 7
      // 105: getfield net/minecraft/util/math/Vec3d.y D
      // 108: aload 7
      // 10a: getfield net/minecraft/util/math/Vec3d.z D
      // 10d: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 110: astore 11
      // 112: aload 1
      // 113: invokevirtual net/minecraft/util/math/Box.getCenter ()Lnet/minecraft/util/math/Vec3d;
      // 116: astore 12
      // 118: dconst_1
      // 119: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 11c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 11f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 122: aload 11
      // 124: invokevirtual net/minecraft/util/math/Vec3d.distanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 127: aload 0
      // 128: getfield dev/boze/client/systems/modules/legit/AimAssist.field2683 Ldev/boze/client/settings/EnumSetting;
      // 12b: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 12e: getstatic dev/boze/client/enums/AimAssistGunPoint.Off Ldev/boze/client/enums/AimAssistGunPoint;
      // 131: if_acmpeq 13e
      // 134: aload 0
      // 135: getfield dev/boze/client/systems/modules/legit/AimAssist.field2685 Ldev/boze/client/settings/MinMaxSetting;
      // 138: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 13b: goto 145
      // 13e: aload 0
      // 13f: getfield dev/boze/client/systems/modules/legit/AimAssist.field2684 Ldev/boze/client/settings/MinMaxSetting;
      // 142: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 145: invokevirtual java/lang/Double.doubleValue ()D
      // 148: aload 0
      // 149: getfield dev/boze/client/systems/modules/legit/AimAssist.field2683 Ldev/boze/client/settings/EnumSetting;
      // 14c: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 14f: getstatic dev/boze/client/enums/AimAssistGunPoint.Off Ldev/boze/client/enums/AimAssistGunPoint;
      // 152: if_acmpeq 15f
      // 155: aload 0
      // 156: getfield dev/boze/client/systems/modules/legit/AimAssist.field2685 Ldev/boze/client/settings/MinMaxSetting;
      // 159: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 15c: goto 166
      // 15f: aload 0
      // 160: getfield dev/boze/client/systems/modules/legit/AimAssist.field2684 Ldev/boze/client/settings/MinMaxSetting;
      // 163: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 166: invokevirtual java/lang/Double.doubleValue ()D
      // 169: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 16c: getfield dev/boze/client/systems/modules/client/GhostRotations.field749 Ldev/boze/client/settings/MinMaxSetting;
      // 16f: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 172: invokevirtual java/lang/Double.doubleValue ()D
      // 175: dadd
      // 176: invokestatic java/lang/Math.max (DD)D
      // 179: ddiv
      // 17a: dconst_1
      // 17b: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 17e: getfield dev/boze/client/systems/modules/client/GhostRotations.field748 Ldev/boze/client/settings/MinMaxSetting;
      // 181: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 184: invokevirtual java/lang/Double.doubleValue ()D
      // 187: dsub
      // 188: invokestatic mapped/Class5917.method32 (DD)D
      // 18b: dsub
      // 18c: dstore 13
      // 18e: aload 11
      // 190: aload 12
      // 192: getfield net/minecraft/util/math/Vec3d.x D
      // 195: aload 11
      // 197: getfield net/minecraft/util/math/Vec3d.x D
      // 19a: dsub
      // 19b: dload 13
      // 19d: dmul
      // 19e: aload 12
      // 1a0: getfield net/minecraft/util/math/Vec3d.y D
      // 1a3: aload 11
      // 1a5: getfield net/minecraft/util/math/Vec3d.y D
      // 1a8: dsub
      // 1a9: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 1ac: getfield dev/boze/client/systems/modules/client/GhostRotations.field758 Ldev/boze/client/settings/BooleanSetting;
      // 1af: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 1b2: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 1b5: ifeq 1bf
      // 1b8: dconst_1
      // 1b9: dload 13
      // 1bb: dsub
      // 1bc: goto 1c0
      // 1bf: dconst_1
      // 1c0: dmul
      // 1c1: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 1c4: getfield dev/boze/client/systems/modules/client/GhostRotations.field757 Ldev/boze/client/settings/MinMaxSetting;
      // 1c7: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 1ca: invokevirtual java/lang/Double.doubleValue ()D
      // 1cd: dmul
      // 1ce: aload 12
      // 1d0: getfield net/minecraft/util/math/Vec3d.z D
      // 1d3: aload 11
      // 1d5: getfield net/minecraft/util/math/Vec3d.z D
      // 1d8: dsub
      // 1d9: dload 13
      // 1db: dmul
      // 1dc: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 1df: astore 11
      // 1e1: new net/minecraft/util/math/Vec3d
      // 1e4: dup
      // 1e5: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 1e8: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1eb: getfield net/minecraft/client/network/ClientPlayerEntity.prevX D
      // 1ee: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 1f1: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1f4: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 1f7: dsub
      // 1f8: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 1fb: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1fe: getfield net/minecraft/client/network/ClientPlayerEntity.prevY D
      // 201: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 204: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 207: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 20a: dsub
      // 20b: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 20e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 211: getfield net/minecraft/client/network/ClientPlayerEntity.prevZ D
      // 214: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 217: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 21a: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 21d: dsub
      // 21e: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 221: astore 15
      // 223: aload 15
      // 225: aload 2
      // 226: getfield net/minecraft/entity/Entity.prevX D
      // 229: aload 2
      // 22a: invokevirtual net/minecraft/entity/Entity.getX ()D
      // 22d: dsub
      // 22e: aload 2
      // 22f: getfield net/minecraft/entity/Entity.prevY D
      // 232: aload 2
      // 233: invokevirtual net/minecraft/entity/Entity.getY ()D
      // 236: dsub
      // 237: aload 2
      // 238: getfield net/minecraft/entity/Entity.prevZ D
      // 23b: aload 2
      // 23c: invokevirtual net/minecraft/entity/Entity.getZ ()D
      // 23f: dsub
      // 240: invokevirtual net/minecraft/util/math/Vec3d.subtract (DDD)Lnet/minecraft/util/math/Vec3d;
      // 243: astore 16
      // 245: aload 16
      // 247: invokevirtual net/minecraft/util/math/Vec3d.lengthSquared ()D
      // 24a: dconst_0
      // 24b: dcmpl
      // 24c: ifle 2d4
      // 24f: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 252: getfield dev/boze/client/systems/modules/client/GhostRotations.field752 Ldev/boze/client/settings/BooleanSetting;
      // 255: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 258: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 25b: ifeq 266
      // 25e: aload 16
      // 260: invokevirtual net/minecraft/util/math/Vec3d.horizontalLength ()D
      // 263: goto 267
      // 266: dconst_1
      // 267: dstore 17
      // 269: aload 11
      // 26b: invokestatic java/lang/System.currentTimeMillis ()J
      // 26e: l2d
      // 26f: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 272: getfield dev/boze/client/systems/modules/client/GhostRotations.field755 Ldev/boze/client/settings/MinMaxSetting;
      // 275: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 278: invokevirtual java/lang/Double.doubleValue ()D
      // 27b: dmul
      // 27c: ldc2_w 0.01
      // 27f: dmul
      // 280: invokestatic java/lang/Math.sin (D)D
      // 283: dload 17
      // 285: dmul
      // 286: invokestatic java/lang/System.currentTimeMillis ()J
      // 289: l2d
      // 28a: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 28d: getfield dev/boze/client/systems/modules/client/GhostRotations.field756 Ldev/boze/client/settings/MinMaxSetting;
      // 290: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 293: invokevirtual java/lang/Double.doubleValue ()D
      // 296: dmul
      // 297: ldc2_w 0.01
      // 29a: dmul
      // 29b: invokestatic java/lang/Math.cos (D)D
      // 29e: aload 16
      // 2a0: getfield net/minecraft/util/math/Vec3d.y D
      // 2a3: dload 17
      // 2a5: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2a8: getfield dev/boze/client/systems/modules/client/GhostRotations.field753 Ldev/boze/client/settings/MinMaxSetting;
      // 2ab: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 2ae: invokevirtual java/lang/Double.doubleValue ()D
      // 2b1: dmul
      // 2b2: dadd
      // 2b3: dmul
      // 2b4: invokestatic java/lang/System.currentTimeMillis ()J
      // 2b7: l2d
      // 2b8: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2bb: getfield dev/boze/client/systems/modules/client/GhostRotations.field755 Ldev/boze/client/settings/MinMaxSetting;
      // 2be: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 2c1: invokevirtual java/lang/Double.doubleValue ()D
      // 2c4: dmul
      // 2c5: ldc2_w 0.01
      // 2c8: dmul
      // 2c9: invokestatic java/lang/Math.sin (D)D
      // 2cc: dload 17
      // 2ce: dmul
      // 2cf: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 2d2: astore 11
      // 2d4: aload 11
      // 2d6: aload 16
      // 2d8: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2db: getfield dev/boze/client/systems/modules/client/GhostRotations.field751 Ldev/boze/client/settings/MinMaxSetting;
      // 2de: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 2e1: invokevirtual java/lang/Double.doubleValue ()D
      // 2e4: invokevirtual net/minecraft/util/math/Vec3d.multiply (D)Lnet/minecraft/util/math/Vec3d;
      // 2e7: invokevirtual net/minecraft/util/math/Vec3d.subtract (Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;
      // 2ea: astore 11
      // 2ec: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2ef: getfield dev/boze/client/systems/modules/client/GhostRotations.field760 Ldev/boze/client/utils/RotationHelper;
      // 2f2: ifnull 2fe
      // 2f5: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 2f8: getfield dev/boze/client/systems/modules/client/GhostRotations.field760 Ldev/boze/client/utils/RotationHelper;
      // 2fb: goto 30b
      // 2fe: new dev/boze/client/utils/RotationHelper
      // 301: dup
      // 302: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 305: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 308: invokespecial dev/boze/client/utils/RotationHelper.<init> (Lnet/minecraft/entity/Entity;)V
      // 30b: astore 17
      // 30d: aload 17
      // 30f: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 312: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 315: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 318: aload 11
      // 31a: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 31d: invokevirtual dev/boze/client/utils/RotationHelper.method605 (Ldev/boze/client/utils/RotationHelper;)F
      // 320: f2d
      // 321: ldc2_w 255.0
      // 324: ddiv
      // 325: dstore 18
      // 327: aload 11
      // 329: dconst_0
      // 32a: dload 18
      // 32c: dneg
      // 32d: aload 1
      // 32e: invokevirtual net/minecraft/util/math/Box.getLengthY ()D
      // 331: dmul
      // 332: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 335: getfield dev/boze/client/systems/modules/client/GhostRotations.field750 Ldev/boze/client/settings/MinMaxSetting;
      // 338: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 33b: invokevirtual java/lang/Double.doubleValue ()D
      // 33e: dconst_1
      // 33f: dsub
      // 340: dmul
      // 341: dconst_0
      // 342: invokevirtual net/minecraft/util/math/Vec3d.add (DDD)Lnet/minecraft/util/math/Vec3d;
      // 345: astore 11
      // 347: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 34a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 34d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 350: aload 7
      // 352: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 355: dstore 20
      // 357: dload 20
      // 359: dload 8
      // 35b: dload 8
      // 35d: dmul
      // 35e: dcmpg
      // 35f: ifgt 407
      // 362: iload 10
      // 364: ifeq 407
      // 367: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 36a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 36d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 370: aload 11
      // 372: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 375: dload 8
      // 377: dload 8
      // 379: dmul
      // 37a: dcmpl
      // 37b: ifle 3d5
      // 37e: new net/minecraft/util/math/Vec3d
      // 381: dup
      // 382: aload 11
      // 384: getfield net/minecraft/util/math/Vec3d.x D
      // 387: aload 7
      // 389: getfield net/minecraft/util/math/Vec3d.x D
      // 38c: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 38f: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 392: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 395: invokevirtual java/lang/Double.doubleValue ()D
      // 398: invokestatic mapped/Class5917.method35 (DDD)D
      // 39b: aload 11
      // 39d: getfield net/minecraft/util/math/Vec3d.y D
      // 3a0: aload 7
      // 3a2: getfield net/minecraft/util/math/Vec3d.y D
      // 3a5: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 3a8: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 3ab: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 3ae: invokevirtual java/lang/Double.doubleValue ()D
      // 3b1: invokestatic mapped/Class5917.method35 (DDD)D
      // 3b4: aload 11
      // 3b6: getfield net/minecraft/util/math/Vec3d.z D
      // 3b9: aload 7
      // 3bb: getfield net/minecraft/util/math/Vec3d.z D
      // 3be: getstatic dev/boze/client/systems/modules/client/GhostRotations.INSTANCE Ldev/boze/client/systems/modules/client/GhostRotations;
      // 3c1: getfield dev/boze/client/systems/modules/client/GhostRotations.field746 Ldev/boze/client/settings/MinMaxSetting;
      // 3c4: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 3c7: invokevirtual java/lang/Double.doubleValue ()D
      // 3ca: invokestatic mapped/Class5917.method35 (DDD)D
      // 3cd: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 3d0: astore 11
      // 3d2: goto 367
      // 3d5: dload 8
      // 3d7: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 3da: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 3dd: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 3e0: aload 11
      // 3e2: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 3e5: bipush 1
      // 3e6: invokestatic mapped/Class5924.method73 (DLdev/boze/client/utils/RotationHelper;Z)Lnet/minecraft/util/hit/HitResult;
      // 3e9: astore 22
      // 3eb: aload 22
      // 3ed: ifnull 403
      // 3f0: aload 22
      // 3f2: instanceof net/minecraft/util/hit/EntityHitResult
      // 3f5: ifeq 403
      // 3f8: aload 22
      // 3fa: checkcast net/minecraft/util/hit/EntityHitResult
      // 3fd: invokevirtual net/minecraft/util/hit/EntityHitResult.getEntity ()Lnet/minecraft/entity/Entity;
      // 400: ifnonnull 407
      // 403: aload 7
      // 405: astore 11
      // 407: aload 11
      // 409: aload 1
      // 40a: invokestatic mapped/Class5917.method33 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 40d: astore 7
      // 40f: aload 7
      // 411: areturn
   }

   private Entity method1574() {
      ArrayList var4 = new ArrayList();

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof LivingEntity
            && this.method1576(var6)
            && !(
               (double)var6.distanceTo(mc.player)
                  > this.field2683.method461() != AimAssistGunPoint.Off ? this.field2685.getValue() : this.field2684.getValue() + 1.0
            )
            && !((LivingEntity)var6).isDead()
            && !(((LivingEntity)var6).getHealth() + ((LivingEntity)var6).getAbsorptionAmount() <= 0.0F)
            && (this.field2686.method419() || mc.player.canSee(var6))) {
            var4.add((LivingEntity)var6);
         }
      }

      return (Entity)var4.stream().min(this.field2702).orElse(null);
   }

   private RotationHelper method1575() {
      return new RotationHelper(mc.player);
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1576(Entity var1) {
      if ((var1.isInvisible() || var1.isInvisibleTo(mc.player)) && !this.field2696.method419()) {
         return false;
      } else if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else if (Friends.method2055(var1)) {
            return this.field2698.method419();
         } else {
            return AntiBots.method2055(var1) ? false : this.field2697.method419();
         }
      } else {
         switch (m_.field2108[var1.getType().getSpawnGroup().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return this.field2699.method419();
            case 5:
               return this.field2700.method419();
            default:
               return false;
         }
      }
   }

   private Double lambda$new$2(Entity param1) {
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
      // 00: aload 0
      // 01: getfield dev/boze/client/systems/modules/legit/AimAssist.field2694 Ldev/boze/client/settings/BooleanSetting;
      // 04: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 07: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 0a: ifeq 1a
      // 0d: aload 1
      // 0e: aload 0
      // 0f: getfield dev/boze/client/systems/modules/legit/AimAssist.field2701 Lnet/minecraft/entity/Entity;
      // 12: if_acmpne 1a
      // 15: dconst_0
      // 16: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 19: areturn
      // 1a: aload 0
      // 1b: getfield dev/boze/client/systems/modules/legit/AimAssist.field2693 Ldev/boze/client/settings/EnumSetting;
      // 1e: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 21: checkcast dev/boze/client/enums/AimAssistPriority
      // 24: invokevirtual dev/boze/client/enums/AimAssistPriority.ordinal ()I
      // 27: tableswitch 111 0 2 25 80 56
      // 40: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 43: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 46: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 49: aload 1
      // 4a: invokevirtual net/minecraft/entity/Entity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 4d: aload 1
      // 4e: invokevirtual net/minecraft/entity/Entity.getTargetingMargin ()F
      // 51: f2d
      // 52: invokevirtual net/minecraft/util/math/Box.expand (D)Lnet/minecraft/util/math/Box;
      // 55: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 58: invokevirtual net/minecraft/util/math/Vec3d.squaredDistanceTo (Lnet/minecraft/util/math/Vec3d;)D
      // 5b: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 5e: areturn
      // 5f: aload 1
      // 60: instanceof net/minecraft/entity/LivingEntity
      // 63: ifeq 72
      // 66: aload 1
      // 67: checkcast net/minecraft/entity/LivingEntity
      // 6a: invokevirtual net/minecraft/entity/LivingEntity.getHealth ()F
      // 6d: f2d
      // 6e: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 71: areturn
      // 72: dconst_0
      // 73: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 76: areturn
      // 77: getstatic dev/boze/client/systems/modules/legit/AimAssist.mc Lnet/minecraft/client/MinecraftClient;
      // 7a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 7d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 80: aload 1
      // 81: invokevirtual net/minecraft/entity/Entity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 84: invokestatic mapped/Class5917.method34 (Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/math/Vec3d;
      // 87: invokestatic mapped/Class1202.method2391 (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Ldev/boze/client/utils/RotationHelper;
      // 8a: aload 0
      // 8b: invokevirtual dev/boze/client/systems/modules/legit/AimAssist.method1575 ()Ldev/boze/client/utils/RotationHelper;
      // 8e: invokevirtual dev/boze/client/utils/RotationHelper.method605 (Ldev/boze/client/utils/RotationHelper;)F
      // 91: f2d
      // 92: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 95: areturn
      // 96: dconst_0
      // 97: invokestatic java/lang/Double.valueOf (D)Ljava/lang/Double;
      // 9a: areturn
   }

   private boolean lambda$new$1() {
      return this.field2683.method461() != AimAssistGunPoint.Off;
   }

   private boolean lambda$new$0() {
      return this.field2683.method461() == AimAssistGunPoint.Off;
   }
}
