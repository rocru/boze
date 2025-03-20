package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class3090;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PacketFly extends Module {
   public static final PacketFly INSTANCE = new PacketFly();
   private final MinMaxSetting field499 = new MinMaxSetting("Speed", 1.0, 0.1, 10.0, 0.1, "Speed");
   private final EnumSetting<PacketFlyMode> field500 = new EnumSetting<PacketFlyMode>("Mode", PacketFlyMode.Factor, "Mode for packet flying");
   private final MinMaxSetting field501 = new MinMaxSetting("Factor", 1.0, 0.1, 10.0, 0.1, "Factor for packetfly", this::lambda$new$0);
   private final BooleanSetting field502 = new BooleanSetting("Thrust", false, "Thrust forwards");
   private final EnumSetting<PacketFlyBounds> field503 = new EnumSetting<PacketFlyBounds>("Bounds", PacketFlyBounds.World, "Bounds mode");
   private final EnumSetting<PacketFlyType> field504 = new EnumSetting<PacketFlyType>("Type", PacketFlyType.Down, "Type of bounds packets to send");
   private final EnumSetting<PacketFlyLimit> field505 = new EnumSetting<PacketFlyLimit>("Limit", PacketFlyLimit.Off, "Limit for packets");
   private final BooleanSetting field506 = new BooleanSetting("Conceal", false, "Conceal packet flying");
   private final BooleanSetting field507 = new BooleanSetting("AntiKick", true, "Slowly go down to not get kicked");
   private final EnumSetting<PacketFlyPhase> field508 = new EnumSetting<PacketFlyPhase>("Phase", PacketFlyPhase.Off, "Phase");
   private final BooleanSetting field509 = new BooleanSetting("Constrict", false, "Constrict flight");
   private int field510 = 0;
   private TeleportConfirmC2SPacket field511 = null;
   private PositionAndOnGround field512 = null;
   private final ArrayList<PlayerMoveC2SPacket> field513 = new ArrayList();
   private final ConcurrentHashMap<Integer, Class3090> field514 = new ConcurrentHashMap();
   private int field515 = 0;
   private int field516 = 0;
   private int field517 = 0;
   private boolean field518 = false;
   private int field519 = 0;
   private int field520 = 0;
   private int field521 = 0;
   private boolean field522 = false;
   private double field523 = 0.0;
   private double field524 = 0.0;
   private double field525 = 0.0;
   private final Random field526 = new Random();

   public PacketFly() {
      super("PacketFly", "Fly using packets", Category.Movement);
   }

   @EventHandler
   public void method2072(PreTickEvent event) {
      if (!MinecraftUtils.isClientActive() || mc.currentScreen instanceof DownloadingTerrainScreen) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   private void method1831(PrePlayerTickEvent param1) {
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
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 003: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 006: getfield net/minecraft/client/network/ClientPlayerEntity.age I
      // 009: bipush 20
      // 00b: irem
      // 00c: ifne 013
      // 00f: aload 0
      // 010: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method1904 ()V
      // 013: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 016: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 019: getstatic net/minecraft/util/math/Vec3d.ZERO Lnet/minecraft/util/math/Vec3d;
      // 01c: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (Lnet/minecraft/util/math/Vec3d;)V
      // 01f: aload 0
      // 020: getfield dev/boze/client/systems/modules/movement/PacketFly.field500 Ldev/boze/client/settings/EnumSetting;
      // 023: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 026: getstatic dev/boze/client/enums/PacketFlyMode.Slow Ldev/boze/client/enums/PacketFlyMode;
      // 029: if_acmpne 02d
      // 02c: return
      // 02d: aload 0
      // 02e: getfield dev/boze/client/systems/modules/movement/PacketFly.field510 I
      // 031: ifgt 07b
      // 034: aload 0
      // 035: getfield dev/boze/client/systems/modules/movement/PacketFly.field500 Ldev/boze/client/settings/EnumSetting;
      // 038: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 03b: getstatic dev/boze/client/enums/PacketFlyMode.Setback Ldev/boze/client/enums/PacketFlyMode;
      // 03e: if_acmpeq 07b
      // 041: aload 0
      // 042: new net/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$PositionAndOnGround
      // 045: dup
      // 046: aload 0
      // 047: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method2091 ()D
      // 04a: dconst_1
      // 04b: aload 0
      // 04c: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method2091 ()D
      // 04f: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 052: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 055: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 058: invokespecial net/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$PositionAndOnGround.<init> (DDDZ)V
      // 05b: putfield dev/boze/client/systems/modules/movement/PacketFly.field512 Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$PositionAndOnGround;
      // 05e: aload 0
      // 05f: getfield dev/boze/client/systems/modules/movement/PacketFly.field513 Ljava/util/ArrayList;
      // 062: aload 0
      // 063: getfield dev/boze/client/systems/modules/movement/PacketFly.field512 Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$PositionAndOnGround;
      // 066: invokevirtual java/util/ArrayList.add (Ljava/lang/Object;)Z
      // 069: pop
      // 06a: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 06d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 070: getfield net/minecraft/client/network/ClientPlayerEntity.networkHandler Lnet/minecraft/client/network/ClientPlayNetworkHandler;
      // 073: aload 0
      // 074: getfield dev/boze/client/systems/modules/movement/PacketFly.field512 Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$PositionAndOnGround;
      // 077: invokevirtual net/minecraft/client/network/ClientPlayNetworkHandler.sendPacket (Lnet/minecraft/network/packet/Packet;)V
      // 07a: return
      // 07b: aload 0
      // 07c: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method1972 ()Z
      // 07f: istore 5
      // 081: aload 0
      // 082: dconst_0
      // 083: putfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 086: aload 0
      // 087: dconst_0
      // 088: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 08b: aload 0
      // 08c: dconst_0
      // 08d: putfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 090: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 093: getfield net/minecraft/client/MinecraftClient.options Lnet/minecraft/client/option/GameOptions;
      // 096: getfield net/minecraft/client/option/GameOptions.jumpKey Lnet/minecraft/client/option/KeyBinding;
      // 099: invokevirtual net/minecraft/client/option/KeyBinding.isPressed ()Z
      // 09c: ifeq 0e8
      // 09f: aload 0
      // 0a0: getfield dev/boze/client/systems/modules/movement/PacketFly.field517 I
      // 0a3: bipush 1
      // 0a4: if_icmplt 0ac
      // 0a7: iload 5
      // 0a9: ifeq 0e8
      // 0ac: aload 0
      // 0ad: ldc2_w 0.062
      // 0b0: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 0b3: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 0b6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0b9: getfield net/minecraft/client/network/ClientPlayerEntity.age I
      // 0bc: aload 0
      // 0bd: getfield dev/boze/client/systems/modules/movement/PacketFly.field500 Ldev/boze/client/settings/EnumSetting;
      // 0c0: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0c3: getstatic dev/boze/client/enums/PacketFlyMode.Setback Ldev/boze/client/enums/PacketFlyMode;
      // 0c6: if_acmpne 0ce
      // 0c9: bipush 10
      // 0cb: goto 0d0
      // 0ce: bipush 20
      // 0d0: irem
      // 0d1: ifne 0db
      // 0d4: aload 0
      // 0d5: ldc2_w -0.032
      // 0d8: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 0db: aload 0
      // 0dc: bipush 0
      // 0dd: putfield dev/boze/client/systems/modules/movement/PacketFly.field515 I
      // 0e0: aload 0
      // 0e1: bipush 5
      // 0e2: putfield dev/boze/client/systems/modules/movement/PacketFly.field516 I
      // 0e5: goto 115
      // 0e8: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 0eb: getfield net/minecraft/client/MinecraftClient.options Lnet/minecraft/client/option/GameOptions;
      // 0ee: getfield net/minecraft/client/option/GameOptions.sneakKey Lnet/minecraft/client/option/KeyBinding;
      // 0f1: invokevirtual net/minecraft/client/option/KeyBinding.isPressed ()Z
      // 0f4: ifeq 115
      // 0f7: aload 0
      // 0f8: getfield dev/boze/client/systems/modules/movement/PacketFly.field517 I
      // 0fb: bipush 1
      // 0fc: if_icmplt 104
      // 0ff: iload 5
      // 101: ifeq 115
      // 104: aload 0
      // 105: ldc2_w -0.062
      // 108: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 10b: aload 0
      // 10c: bipush 0
      // 10d: putfield dev/boze/client/systems/modules/movement/PacketFly.field515 I
      // 110: aload 0
      // 111: bipush 5
      // 112: putfield dev/boze/client/systems/modules/movement/PacketFly.field516 I
      // 115: iload 5
      // 117: ifne 138
      // 11a: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 11d: getfield net/minecraft/client/MinecraftClient.options Lnet/minecraft/client/option/GameOptions;
      // 120: getfield net/minecraft/client/option/GameOptions.sneakKey Lnet/minecraft/client/option/KeyBinding;
      // 123: invokevirtual net/minecraft/client/option/KeyBinding.isPressed ()Z
      // 126: ifne 20b
      // 129: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 12c: getfield net/minecraft/client/MinecraftClient.options Lnet/minecraft/client/option/GameOptions;
      // 12f: getfield net/minecraft/client/option/GameOptions.jumpKey Lnet/minecraft/client/option/KeyBinding;
      // 132: invokevirtual net/minecraft/client/option/KeyBinding.isPressed ()Z
      // 135: ifne 20b
      // 138: aload 0
      // 139: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method1971 ()Z
      // 13c: ifeq 1c4
      // 13f: iload 5
      // 141: ifeq 179
      // 144: aload 0
      // 145: getfield dev/boze/client/systems/modules/movement/PacketFly.field508 Ldev/boze/client/settings/EnumSetting;
      // 148: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 14b: getstatic dev/boze/client/enums/PacketFlyPhase.Off Ldev/boze/client/enums/PacketFlyPhase;
      // 14e: if_acmpeq 179
      // 151: aload 0
      // 152: getfield dev/boze/client/systems/modules/movement/PacketFly.field508 Ldev/boze/client/settings/EnumSetting;
      // 155: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 158: getstatic dev/boze/client/enums/PacketFlyPhase.Fast Ldev/boze/client/enums/PacketFlyPhase;
      // 15b: if_acmpne 173
      // 15e: aload 0
      // 15f: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 162: dconst_0
      // 163: dcmpl
      // 164: ifeq 16d
      // 167: ldc2_w 0.0465
      // 16a: goto 17c
      // 16d: ldc2_w 0.062
      // 170: goto 17c
      // 173: ldc2_w 0.031
      // 176: goto 17c
      // 179: ldc2_w 0.26
      // 17c: aload 0
      // 17d: getfield dev/boze/client/systems/modules/movement/PacketFly.field499 Ldev/boze/client/settings/MinMaxSetting;
      // 180: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 183: invokevirtual java/lang/Double.doubleValue ()D
      // 186: dmul
      // 187: invokestatic mapped/Class5924.method93 (D)Lnet/minecraft/util/math/Vec3d;
      // 18a: astore 6
      // 18c: aload 6
      // 18e: getfield net/minecraft/util/math/Vec3d.x D
      // 191: dconst_0
      // 192: dcmpl
      // 193: ifne 1a0
      // 196: aload 6
      // 198: getfield net/minecraft/util/math/Vec3d.z D
      // 19b: dconst_0
      // 19c: dcmpl
      // 19d: ifeq 1c4
      // 1a0: aload 0
      // 1a1: getfield dev/boze/client/systems/modules/movement/PacketFly.field516 I
      // 1a4: bipush 1
      // 1a5: if_icmplt 1ad
      // 1a8: iload 5
      // 1aa: ifeq 1c4
      // 1ad: aload 0
      // 1ae: aload 6
      // 1b0: getfield net/minecraft/util/math/Vec3d.x D
      // 1b3: putfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 1b6: aload 0
      // 1b7: aload 6
      // 1b9: getfield net/minecraft/util/math/Vec3d.z D
      // 1bc: putfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 1bf: aload 0
      // 1c0: bipush 5
      // 1c1: putfield dev/boze/client/systems/modules/movement/PacketFly.field517 I
      // 1c4: aload 0
      // 1c5: getfield dev/boze/client/systems/modules/movement/PacketFly.field507 Ldev/boze/client/settings/BooleanSetting;
      // 1c8: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 1cb: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 1ce: ifeq 20b
      // 1d1: aload 0
      // 1d2: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 1d5: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 1d8: getstatic dev/boze/client/enums/PacketFlyLimit.Off Ldev/boze/client/enums/PacketFlyLimit;
      // 1db: if_acmpeq 1e5
      // 1de: aload 0
      // 1df: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 1e2: ifeq 20b
      // 1e5: aload 0
      // 1e6: getfield dev/boze/client/systems/modules/movement/PacketFly.field515 I
      // 1e9: bipush 3
      // 1ea: if_icmpge 1fa
      // 1ed: aload 0
      // 1ee: aload 0
      // 1ef: getfield dev/boze/client/systems/modules/movement/PacketFly.field515 I
      // 1f2: bipush 1
      // 1f3: iadd
      // 1f4: putfield dev/boze/client/systems/modules/movement/PacketFly.field515 I
      // 1f7: goto 20b
      // 1fa: aload 0
      // 1fb: bipush 0
      // 1fc: putfield dev/boze/client/systems/modules/movement/PacketFly.field515 I
      // 1ff: iload 5
      // 201: ifne 20b
      // 204: aload 0
      // 205: ldc2_w -0.04
      // 208: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 20b: iload 5
      // 20d: ifeq 250
      // 210: aload 0
      // 211: getfield dev/boze/client/systems/modules/movement/PacketFly.field508 Ldev/boze/client/settings/EnumSetting;
      // 214: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 217: getstatic dev/boze/client/enums/PacketFlyPhase.Off Ldev/boze/client/enums/PacketFlyPhase;
      // 21a: if_acmpeq 22c
      // 21d: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 220: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 223: getfield net/minecraft/client/network/ClientPlayerEntity.forwardSpeed F
      // 226: f2d
      // 227: dconst_0
      // 228: dcmpl
      // 229: ifne 244
      // 22c: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 22f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 232: getfield net/minecraft/client/network/ClientPlayerEntity.sidewaysSpeed F
      // 235: f2d
      // 236: dconst_0
      // 237: dcmpl
      // 238: ifeq 250
      // 23b: aload 0
      // 23c: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 23f: dconst_0
      // 240: dcmpl
      // 241: ifeq 250
      // 244: aload 0
      // 245: aload 0
      // 246: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 249: ldc2_w 2.5
      // 24c: ddiv
      // 24d: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 250: aload 0
      // 251: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 254: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 257: getstatic dev/boze/client/enums/PacketFlyLimit.Tick Ldev/boze/client/enums/PacketFlyLimit;
      // 25a: if_acmpeq 26a
      // 25d: aload 0
      // 25e: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 261: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 264: getstatic dev/boze/client/enums/PacketFlyLimit.Both Ldev/boze/client/enums/PacketFlyLimit;
      // 267: if_acmpne 2c1
      // 26a: aload 0
      // 26b: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 26e: ifne 283
      // 271: aload 0
      // 272: dconst_0
      // 273: putfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 276: aload 0
      // 277: dconst_0
      // 278: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 27b: aload 0
      // 27c: dconst_0
      // 27d: putfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 280: goto 2e6
      // 283: aload 0
      // 284: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 287: bipush 2
      // 288: if_icmpne 2e6
      // 28b: aload 0
      // 28c: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 28f: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 292: getstatic dev/boze/client/enums/PacketFlyLimit.Both Ldev/boze/client/enums/PacketFlyLimit;
      // 295: if_acmpne 2e6
      // 298: aload 0
      // 299: getfield dev/boze/client/systems/modules/movement/PacketFly.field522 Z
      // 29c: ifeq 2ae
      // 29f: aload 0
      // 2a0: dconst_0
      // 2a1: putfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 2a4: aload 0
      // 2a5: dconst_0
      // 2a6: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 2a9: aload 0
      // 2aa: dconst_0
      // 2ab: putfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 2ae: aload 0
      // 2af: aload 0
      // 2b0: getfield dev/boze/client/systems/modules/movement/PacketFly.field522 Z
      // 2b3: ifne 2ba
      // 2b6: bipush 1
      // 2b7: goto 2bb
      // 2ba: bipush 0
      // 2bb: putfield dev/boze/client/systems/modules/movement/PacketFly.field522 Z
      // 2be: goto 2e6
      // 2c1: aload 0
      // 2c2: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 2c5: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 2c8: getstatic dev/boze/client/enums/PacketFlyLimit.Speed Ldev/boze/client/enums/PacketFlyLimit;
      // 2cb: if_acmpne 2e6
      // 2ce: aload 0
      // 2cf: getfield dev/boze/client/systems/modules/movement/PacketFly.field520 I
      // 2d2: bipush 7
      // 2d4: if_icmpne 2e6
      // 2d7: aload 0
      // 2d8: dconst_0
      // 2d9: putfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 2dc: aload 0
      // 2dd: dconst_0
      // 2de: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 2e1: aload 0
      // 2e2: dconst_0
      // 2e3: putfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 2e6: aload 0
      // 2e7: getfield dev/boze/client/systems/modules/movement/PacketFly.field500 Ldev/boze/client/settings/EnumSetting;
      // 2ea: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 2ed: checkcast dev/boze/client/enums/PacketFlyMode
      // 2f0: invokevirtual dev/boze/client/enums/PacketFlyMode.ordinal ()I
      // 2f3: tableswitch 372 0 2 133 25 79
      // 30c: aload 0
      // 30d: getfield dev/boze/client/systems/modules/movement/PacketFly.field506 Ldev/boze/client/settings/BooleanSetting;
      // 310: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 313: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 316: ifne 32e
      // 319: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 31c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 31f: aload 0
      // 320: getfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 323: aload 0
      // 324: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 327: aload 0
      // 328: getfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 32b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 32e: aload 0
      // 32f: aload 0
      // 330: getfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 333: aload 0
      // 334: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 337: aload 0
      // 338: getfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 33b: bipush 1
      // 33c: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method270 (DDDZ)V
      // 33f: goto 467
      // 342: aload 0
      // 343: getfield dev/boze/client/systems/modules/movement/PacketFly.field506 Ldev/boze/client/settings/BooleanSetting;
      // 346: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 349: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 34c: ifne 364
      // 34f: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 352: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 355: aload 0
      // 356: getfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 359: aload 0
      // 35a: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 35d: aload 0
      // 35e: getfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 361: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 364: aload 0
      // 365: aload 0
      // 366: getfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 369: aload 0
      // 36a: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 36d: aload 0
      // 36e: getfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 371: bipush 0
      // 372: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method270 (DDDZ)V
      // 375: goto 467
      // 378: aload 0
      // 379: getfield dev/boze/client/systems/modules/movement/PacketFly.field501 Ldev/boze/client/settings/MinMaxSetting;
      // 37c: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 37f: invokevirtual java/lang/Double.doubleValue ()D
      // 382: dstore 6
      // 384: dload 6
      // 386: invokestatic java/lang/Math.floor (D)D
      // 389: d2i
      // 38a: istore 8
      // 38c: dload 6
      // 38e: iload 8
      // 390: i2d
      // 391: dsub
      // 392: dstore 9
      // 394: invokestatic java/lang/Math.random ()D
      // 397: dload 9
      // 399: dcmpg
      // 39a: ifge 3a0
      // 39d: iinc 8 1
      // 3a0: aload 0
      // 3a1: getfield dev/boze/client/systems/modules/movement/PacketFly.field502 Ldev/boze/client/settings/BooleanSetting;
      // 3a4: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 3a7: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 3aa: ifeq 3b8
      // 3ad: aload 0
      // 3ae: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 3b1: bipush 1
      // 3b2: if_icmpne 3b8
      // 3b5: iinc 8 1
      // 3b8: bipush 1
      // 3b9: istore 11
      // 3bb: iload 11
      // 3bd: iload 8
      // 3bf: if_icmpgt 437
      // 3c2: aload 0
      // 3c3: getfield dev/boze/client/systems/modules/movement/PacketFly.field506 Ldev/boze/client/settings/BooleanSetting;
      // 3c6: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 3c9: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 3cc: ifne 3f0
      // 3cf: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 3d2: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 3d5: aload 0
      // 3d6: getfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 3d9: iload 11
      // 3db: i2d
      // 3dc: dmul
      // 3dd: aload 0
      // 3de: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 3e1: iload 11
      // 3e3: i2d
      // 3e4: dmul
      // 3e5: aload 0
      // 3e6: getfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 3e9: iload 11
      // 3eb: i2d
      // 3ec: dmul
      // 3ed: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 3f0: aload 0
      // 3f1: aload 0
      // 3f2: getfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 3f5: iload 11
      // 3f7: i2d
      // 3f8: dmul
      // 3f9: aload 0
      // 3fa: getfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 3fd: iload 11
      // 3ff: i2d
      // 400: dmul
      // 401: aload 0
      // 402: getfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 405: iload 11
      // 407: i2d
      // 408: dmul
      // 409: bipush 1
      // 40a: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method270 (DDDZ)V
      // 40d: aload 0
      // 40e: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 411: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 414: getstatic dev/boze/client/enums/PacketFlyLimit.Tick Ldev/boze/client/enums/PacketFlyLimit;
      // 417: if_acmpeq 427
      // 41a: aload 0
      // 41b: getfield dev/boze/client/systems/modules/movement/PacketFly.field505 Ldev/boze/client/settings/EnumSetting;
      // 41e: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 421: getstatic dev/boze/client/enums/PacketFlyLimit.Both Ldev/boze/client/enums/PacketFlyLimit;
      // 424: if_acmpne 431
      // 427: aload 0
      // 428: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 42b: ifne 431
      // 42e: goto 437
      // 431: iinc 11 1
      // 434: goto 3bb
      // 437: aload 0
      // 438: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 43b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 43e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 441: invokevirtual net/minecraft/util/math/Vec3d.getX ()D
      // 444: putfield dev/boze/client/systems/modules/movement/PacketFly.field523 D
      // 447: aload 0
      // 448: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 44b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 44e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 451: invokevirtual net/minecraft/util/math/Vec3d.getY ()D
      // 454: putfield dev/boze/client/systems/modules/movement/PacketFly.field524 D
      // 457: aload 0
      // 458: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 45b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 45e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 461: invokevirtual net/minecraft/util/math/Vec3d.getZ ()D
      // 464: putfield dev/boze/client/systems/modules/movement/PacketFly.field525 D
      // 467: aload 0
      // 468: aload 0
      // 469: getfield dev/boze/client/systems/modules/movement/PacketFly.field516 I
      // 46c: bipush 1
      // 46d: isub
      // 46e: putfield dev/boze/client/systems/modules/movement/PacketFly.field516 I
      // 471: aload 0
      // 472: aload 0
      // 473: getfield dev/boze/client/systems/modules/movement/PacketFly.field517 I
      // 476: bipush 1
      // 477: isub
      // 478: putfield dev/boze/client/systems/modules/movement/PacketFly.field517 I
      // 47b: aload 0
      // 47c: aload 0
      // 47d: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 480: bipush 1
      // 481: iadd
      // 482: putfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 485: aload 0
      // 486: aload 0
      // 487: getfield dev/boze/client/systems/modules/movement/PacketFly.field520 I
      // 48a: bipush 1
      // 48b: iadd
      // 48c: putfield dev/boze/client/systems/modules/movement/PacketFly.field520 I
      // 48f: aload 0
      // 490: getfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 493: bipush 3
      // 494: if_icmple 4ac
      // 497: aload 0
      // 498: bipush 0
      // 499: putfield dev/boze/client/systems/modules/movement/PacketFly.field519 I
      // 49c: aload 0
      // 49d: aload 0
      // 49e: getfield dev/boze/client/systems/modules/movement/PacketFly.field518 Z
      // 4a1: ifne 4a8
      // 4a4: bipush 1
      // 4a5: goto 4a9
      // 4a8: bipush 0
      // 4a9: putfield dev/boze/client/systems/modules/movement/PacketFly.field518 Z
      // 4ac: aload 0
      // 4ad: getfield dev/boze/client/systems/modules/movement/PacketFly.field520 I
      // 4b0: bipush 7
      // 4b2: if_icmple 4ba
      // 4b5: aload 0
      // 4b6: bipush 0
      // 4b7: putfield dev/boze/client/systems/modules/movement/PacketFly.field520 I
      // 4ba: return
   }

   private void method270(double var1, double var3, double var5, boolean var7) {
      if (this.field511 != null) {
         mc.player.networkHandler.sendPacket(this.field511);
         this.field511 = null;
      }

      Vec3d var11 = new Vec3d(mc.player.getX() + var1, mc.player.getY() + var3, mc.player.getZ() + var5);
      Vec3d var12 = this.method271(var1, var3, var5);
      PositionAndOnGround var13 = new PositionAndOnGround(var11.x, var11.y, var11.z, mc.player.isOnGround());
      this.field513.add(var13);
      mc.player.networkHandler.sendPacket(var13);
      if (this.field505.getValue() != PacketFlyLimit.Tick && this.field505.getValue() != PacketFlyLimit.Both || this.field519 != 0) {
         PositionAndOnGround var14 = new PositionAndOnGround(var12.x, var12.y, var12.z, mc.player.isOnGround());
         this.field513.add(var14);
         mc.player.networkHandler.sendPacket(var14);
         if (this.field509.getValue()) {
            for (int var15 = 0; var15 <= 6; var15++) {
               var13 = new PositionAndOnGround(var11.x, var11.y, var11.z, mc.player.isOnGround());
               this.field513.add(var13);
               mc.player.networkHandler.sendPacket(var13);
            }
         }

         if (var7) {
            this.field510++;
            this.field514.put(this.field510, new Class3090(var11.x, var11.y, var11.z, System.currentTimeMillis()));
            this.field511 = new TeleportConfirmC2SPacket(this.field510);
         }
      }
   }

   private Vec3d method271(double param1, double param3, double param5) {
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
      // 000: aload 0
      // 001: getfield dev/boze/client/systems/modules/movement/PacketFly.field504 Ldev/boze/client/settings/EnumSetting;
      // 004: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 007: checkcast dev/boze/client/enums/PacketFlyType
      // 00a: invokevirtual dev/boze/client/enums/PacketFlyType.ordinal ()I
      // 00d: tableswitch 321 0 3 31 116 321 234
      // 02c: new net/minecraft/util/math/Vec3d
      // 02f: dup
      // 030: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 033: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 036: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 039: dload 1
      // 03a: dadd
      // 03b: aload 0
      // 03c: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 03f: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 042: getstatic dev/boze/client/enums/PacketFlyBounds.Normal Ldev/boze/client/enums/PacketFlyBounds;
      // 045: if_acmpeq 062
      // 048: aload 0
      // 049: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 04c: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 04f: getstatic dev/boze/client/enums/PacketFlyBounds.Strict Ldev/boze/client/enums/PacketFlyBounds;
      // 052: if_acmpne 05b
      // 055: sipush 255
      // 058: goto 05e
      // 05b: sipush 256
      // 05e: i2d
      // 05f: goto 06f
      // 062: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 065: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 068: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 06b: ldc2_w 420.0
      // 06e: dadd
      // 06f: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 072: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 075: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 078: dload 5
      // 07a: dadd
      // 07b: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 07e: goto 19c
      // 081: new net/minecraft/util/math/Vec3d
      // 084: dup
      // 085: aload 0
      // 086: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 089: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 08c: getstatic dev/boze/client/enums/PacketFlyBounds.Normal Ldev/boze/client/enums/PacketFlyBounds;
      // 08f: if_acmpeq 0a3
      // 092: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 095: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 098: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 09b: aload 0
      // 09c: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method2091 ()D
      // 09f: dadd
      // 0a0: goto 0a7
      // 0a3: aload 0
      // 0a4: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method2091 ()D
      // 0a7: aload 0
      // 0a8: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 0ab: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0ae: getstatic dev/boze/client/enums/PacketFlyBounds.Strict Ldev/boze/client/enums/PacketFlyBounds;
      // 0b1: if_acmpne 0c6
      // 0b4: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 0b7: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0ba: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 0bd: ldc2_w 2.0
      // 0c0: invokestatic java/lang/Math.max (DD)D
      // 0c3: goto 0cf
      // 0c6: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 0c9: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0cc: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 0cf: aload 0
      // 0d0: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 0d3: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0d6: getstatic dev/boze/client/enums/PacketFlyBounds.Normal Ldev/boze/client/enums/PacketFlyBounds;
      // 0d9: if_acmpeq 0ed
      // 0dc: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 0df: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0e2: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 0e5: aload 0
      // 0e6: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method2091 ()D
      // 0e9: dadd
      // 0ea: goto 0f1
      // 0ed: aload 0
      // 0ee: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method2091 ()D
      // 0f1: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 0f4: goto 19c
      // 0f7: new net/minecraft/util/math/Vec3d
      // 0fa: dup
      // 0fb: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 0fe: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 101: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 104: aload 0
      // 105: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 108: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 10b: getstatic dev/boze/client/enums/PacketFlyBounds.Strict Ldev/boze/client/enums/PacketFlyBounds;
      // 10e: if_acmpne 115
      // 111: dload 1
      // 112: goto 119
      // 115: aload 0
      // 116: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method1614 ()D
      // 119: dadd
      // 11a: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 11d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 120: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 123: aload 0
      // 124: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method1614 ()D
      // 127: dadd
      // 128: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 12b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 12e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 131: aload 0
      // 132: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 135: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 138: getstatic dev/boze/client/enums/PacketFlyBounds.Strict Ldev/boze/client/enums/PacketFlyBounds;
      // 13b: if_acmpne 143
      // 13e: dload 5
      // 140: goto 147
      // 143: aload 0
      // 144: invokevirtual dev/boze/client/systems/modules/movement/PacketFly.method1614 ()D
      // 147: dadd
      // 148: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 14b: goto 19c
      // 14e: new net/minecraft/util/math/Vec3d
      // 151: dup
      // 152: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 155: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 158: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 15b: dload 1
      // 15c: dadd
      // 15d: aload 0
      // 15e: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 161: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 164: getstatic dev/boze/client/enums/PacketFlyBounds.Normal Ldev/boze/client/enums/PacketFlyBounds;
      // 167: if_acmpeq 180
      // 16a: aload 0
      // 16b: getfield dev/boze/client/systems/modules/movement/PacketFly.field503 Ldev/boze/client/settings/EnumSetting;
      // 16e: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 171: getstatic dev/boze/client/enums/PacketFlyBounds.Strict Ldev/boze/client/enums/PacketFlyBounds;
      // 174: if_acmpne 17b
      // 177: bipush 1
      // 178: goto 17c
      // 17b: bipush 0
      // 17c: i2d
      // 17d: goto 18d
      // 180: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 183: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 186: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 189: ldc2_w 1337.0
      // 18c: dsub
      // 18d: getstatic dev/boze/client/systems/modules/movement/PacketFly.mc Lnet/minecraft/client/MinecraftClient;
      // 190: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 193: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 196: dload 5
      // 198: dadd
      // 199: invokespecial net/minecraft/util/math/Vec3d.<init> (DDD)V
      // 19c: areturn
   }

   private double method2091() {
      int var4 = this.field526.nextInt(this.field503.getValue() != PacketFlyBounds.Normal ? 80 : 29000000)
         + (this.field503.getValue() != PacketFlyBounds.Normal ? 5 : 500);
      return this.field526.nextBoolean() ? (double)var4 : (double)(-var4);
   }

   private double method1614() {
      int var4 = this.field526.nextInt(22);
      var4 += 70;
      return this.field526.nextBoolean() ? (double)var4 : (double)(-var4);
   }

   private boolean method1971() {
      if (mc.options.jumpKey.isPressed()) {
         return true;
      } else if (mc.options.forwardKey.isPressed()) {
         return true;
      } else if (mc.options.backKey.isPressed()) {
         return true;
      } else {
         return mc.options.leftKey.isPressed() ? true : mc.options.rightKey.isPressed();
      }
   }

   private boolean method1972() {
      return mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox()).iterator().hasNext();
   }

   private void method1904() {
      this.field514.forEach(this::lambda$cleanPosLooks$1);
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field513.clear();
         this.field514.clear();
         this.field510 = 0;
         this.field516 = 0;
         this.field517 = 0;
         this.field515 = 0;
         this.field519 = 0;
         this.field520 = 0;
         this.field521 = 0;
         this.field523 = 0.0;
         this.field524 = 0.0;
         this.field525 = 0.0;
         this.field522 = false;
         this.field512 = new PositionAndOnGround(this.method2091(), 1.0, this.method2091(), mc.player.isOnGround());
         this.field513.add(this.field512);
         mc.player.networkHandler.sendPacket(this.field512);
      }
   }

   @Override
   public void onDisable() {
      if (mc.player != null) {
         mc.player.setVelocity(Vec3d.ZERO);
      }
   }

   @EventHandler
   private void method2042(PacketBundleEvent var1) {
      if (var1.packet instanceof PlayerPositionLookS2CPacket) {
         if (mc.currentScreen instanceof DownloadingTerrainScreen) {
            this.field510 = 0;
         } else {
            PlayerPositionLookS2CPacket var5 = (PlayerPositionLookS2CPacket)var1.packet;
            if (mc.player.isAlive()) {
               if (this.field510 <= 0) {
                  this.field510 = var5.getTeleportId();
               } else if (this.field500.getValue() != PacketFlyMode.Setback
                  && mc.world.isChunkLoaded(ChunkSectionPos.getSectionCoord(var5.getX()), ChunkSectionPos.getSectionCoord(var5.getZ()))
                  && this.field514.containsKey(var5.getTeleportId())) {
                  Vec3d var6 = (Vec3d)this.field514.get(var5.getTeleportId());
                  if (var6.x == var5.getX() && var6.y == var5.getY() && var6.z == var5.getZ()) {
                     this.field514.remove(var5.getTeleportId());
                     var1.method1020();
                     if (this.field506.getValue()) {
                        mc.player.setPosition(var5.getX(), var5.getY(), var5.getZ());
                     }

                     return;
                  }
               }
            }

            ((PlayerPositionLookS2CPacketAccessor)var1.packet).setYaw(mc.player.getYaw());
            ((PlayerPositionLookS2CPacketAccessor)var1.packet).setPitch(mc.player.getPitch());
            var5.getFlags().remove(PositionFlag.X_ROT);
            var5.getFlags().remove(PositionFlag.Y_ROT);
            this.field510 = var5.getTeleportId();
         }
      }
   }

   @EventHandler
   public void method1893(PlayerMoveEvent event) {
      if (this.field500.getValue() == PacketFlyMode.Slow) {
         double var5 = this.field501.getValue();
         int var7 = (int)Math.floor(var5);
         double var8 = var5 - (double)var7;
         if (Math.random() < var8) {
            var7++;
         }

         if (this.field502.getValue() && this.field519 == 1) {
            var7++;
         }

         for (int var10 = 1; var10 <= var7; var10++) {
            if (!this.field506.getValue()) {
               mc.player.setVelocity(this.field523, this.field524, this.field525);
            }

            this.method270(this.field523, this.field524, this.field525, true);
            if ((this.field505.getValue() == PacketFlyLimit.Tick || this.field505.getValue() == PacketFlyLimit.Both) && this.field519 == 0) {
               break;
            }
         }

         this.field523 = mc.player.getVelocity().getX();
         this.field524 = mc.player.getVelocity().getY();
         this.field525 = mc.player.getVelocity().getZ();
         event.vec3 = new Vec3d(0.0, mc.options.jumpKey.isPressed() ? 0.05 : 0.0, 0.0);
      } else {
         if (this.field500.getValue() != PacketFlyMode.Setback && this.field510 <= 0) {
            return;
         }

         if (this.field506.getValue()) {
            event.vec3 = Vec3d.ZERO;
         } else {
            event.vec3 = new Vec3d(this.field523, this.field524, this.field525);
         }

         if (this.field508.getValue() != PacketFlyPhase.Off && this.method1972()) {
            mc.player.noClip = true;
         }
      }
   }

   @EventHandler
   private void method1853(PrePacketSendEvent var1) {
      if (var1.packet instanceof PlayerMoveC2SPacket) {
         if (var1.packet instanceof PositionAndOnGround) {
            PlayerMoveC2SPacket var5 = (PlayerMoveC2SPacket)var1.packet;
            if (this.field513.contains(var5)) {
               this.field513.remove(var5);
            } else {
               var1.method1020();
            }
         } else {
            var1.method1020();
         }
      }
   }

   @EventHandler
   public void method1890(PlayerVelocityEvent event) {
      event.method1020();
   }

   private void lambda$cleanPosLooks$1(Integer var1, Class3090 var2) {
      if (System.currentTimeMillis() - var2.field216 > TimeUnit.SECONDS.toMillis(30L)) {
         this.field514.remove(var1);
      }
   }

   private boolean lambda$new$0() {
      return this.field500.getValue() == PacketFlyMode.Factor || this.field500.getValue() == PacketFlyMode.Slow;
   }
}
