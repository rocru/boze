package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.AntiAimYaw;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class AntiAim extends Module {
   public static final AntiAim INSTANCE = new AntiAim();
   private final EnumSetting<AntiAimYaw> yaw = new EnumSetting<AntiAimYaw>("Yaw", AntiAimYaw.Jitter, "Mode for spoofing your yaw");
   private final FloatSetting rawRange = new FloatSetting("Range", 180.0F, 0.0F, 180.0F, 0.1F, "Range for yaw jitter", this::lambda$new$0, this.yaw);
   private final FloatSetting yawLock = new FloatSetting("YawLock", 0.0F, -180.0F, 180.0F, 0.1F, "Yaw to look at", this::lambda$new$1, this.yaw);
   private final FloatSetting yawAdd = new FloatSetting("YawAdd", 0.0F, -180.0F, 180.0F, 0.1F, "Amount of yaw to add to your yaw", this::lambda$new$2, this.yaw);
   private final EnumSetting<AntiAimYaw> pitch = new EnumSetting<AntiAimYaw>("Pitch", AntiAimYaw.Lock, "Mode for spoofing your pitch");
   private final FloatSetting pitchRange = new FloatSetting("Range", 90.0F, 0.0F, 90.0F, 0.1F, "Range for pitch jitter", this::lambda$new$3, this.pitch);
   private final FloatSetting pitchLock = new FloatSetting("PitchLock", 0.0F, -90.0F, 90.0F, 0.1F, "Pitch to look at", this::lambda$new$4, this.pitch);
   private final FloatSetting pitchAdd = new FloatSetting(
      "PitchAdd", 0.0F, -90.0F, 90.0F, 0.1F, "Amount of pitch to add to your pitch", this::lambda$new$5, this.pitch
   );
   private final IntSetting interval = new IntSetting("Interval", 0, 0, 20, 1, "Interval for rotation updates");
   private int field2863;
   private float field2864;
   private float field2865;
   private boolean field2866;

   public AntiAim() {
      super("AntiAim", "Spoof your rotations in various different ways", Category.Misc);
   }

   @Override
   public void onEnable() {
      this.field2863 = 0;
      if (mc.player != null) {
         this.field2864 = mc.player.getYaw();
         this.field2865 = mc.player.getPitch();
      }
   }

   @EventHandler(
      priority = -999
   )
   public void method1632(MovementEvent event) {
      if (!event.method1022()) {
         this.method1633(event);
      }
   }

   private void method1633(MovementEvent param1) {
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
      // 000: aload 0
      // 001: aload 0
      // 002: getfield dev/boze/client/systems/modules/misc/AntiAim.field2863 I
      // 005: bipush 1
      // 006: iadd
      // 007: putfield dev/boze/client/systems/modules/misc/AntiAim.field2863 I
      // 00a: aload 0
      // 00b: getfield dev/boze/client/systems/modules/misc/AntiAim.yaw Ldev/boze/client/settings/EnumSetting;
      // 00e: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 011: getstatic dev/boze/client/enums/AntiAimYaw.Off Ldev/boze/client/enums/AntiAimYaw;
      // 014: if_acmpne 025
      // 017: aload 0
      // 018: getfield dev/boze/client/systems/modules/misc/AntiAim.pitch Ldev/boze/client/settings/EnumSetting;
      // 01b: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 01e: getstatic dev/boze/client/enums/AntiAimYaw.Off Ldev/boze/client/enums/AntiAimYaw;
      // 021: if_acmpne 025
      // 024: return
      // 025: aload 0
      // 026: getfield dev/boze/client/systems/modules/misc/AntiAim.field2863 I
      // 029: aload 0
      // 02a: getfield dev/boze/client/systems/modules/misc/AntiAim.interval Ldev/boze/client/settings/IntSetting;
      // 02d: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 030: invokevirtual java/lang/Integer.intValue ()I
      // 033: if_icmple 36e
      // 036: aload 0
      // 037: bipush 0
      // 038: putfield dev/boze/client/systems/modules/misc/AntiAim.field2863 I
      // 03b: aload 1
      // 03c: getfield dev/boze/client/events/MovementEvent.yaw F
      // 03f: fstore 5
      // 041: aload 0
      // 042: getfield dev/boze/client/systems/modules/misc/AntiAim.yaw Ldev/boze/client/settings/EnumSetting;
      // 045: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 048: checkcast dev/boze/client/enums/AntiAimYaw
      // 04b: invokevirtual dev/boze/client/enums/AntiAimYaw.ordinal ()I
      // 04e: tableswitch 329 1 7 42 74 111 138 153 171 312
      // 078: aload 0
      // 079: getfield dev/boze/client/systems/modules/misc/AntiAim.field2864 F
      // 07c: invokestatic java/lang/Math.random ()D
      // 07f: ldc2_w 2.0
      // 082: dmul
      // 083: dconst_1
      // 084: dsub
      // 085: aload 0
      // 086: getfield dev/boze/client/systems/modules/misc/AntiAim.rawRange Ldev/boze/client/settings/FloatSetting;
      // 089: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 08c: invokevirtual java/lang/Float.floatValue ()F
      // 08f: f2d
      // 090: dmul
      // 091: d2f
      // 092: fadd
      // 093: fstore 5
      // 095: goto 197
      // 098: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 09b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 09e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // 0a1: invokestatic java/lang/Math.random ()D
      // 0a4: ldc2_w 2.0
      // 0a7: dmul
      // 0a8: dconst_1
      // 0a9: dsub
      // 0aa: aload 0
      // 0ab: getfield dev/boze/client/systems/modules/misc/AntiAim.rawRange Ldev/boze/client/settings/FloatSetting;
      // 0ae: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 0b1: invokevirtual java/lang/Float.floatValue ()F
      // 0b4: f2d
      // 0b5: dmul
      // 0b6: d2f
      // 0b7: fadd
      // 0b8: fstore 5
      // 0ba: goto 197
      // 0bd: invokestatic java/lang/Math.random ()D
      // 0c0: ldc2_w 2.0
      // 0c3: dmul
      // 0c4: dconst_1
      // 0c5: dsub
      // 0c6: aload 0
      // 0c7: getfield dev/boze/client/systems/modules/misc/AntiAim.rawRange Ldev/boze/client/settings/FloatSetting;
      // 0ca: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 0cd: invokevirtual java/lang/Float.floatValue ()F
      // 0d0: f2d
      // 0d1: dmul
      // 0d2: d2f
      // 0d3: fstore 5
      // 0d5: goto 197
      // 0d8: aload 0
      // 0d9: getfield dev/boze/client/systems/modules/misc/AntiAim.yawLock Ldev/boze/client/settings/FloatSetting;
      // 0dc: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 0df: invokevirtual java/lang/Float.floatValue ()F
      // 0e2: fstore 5
      // 0e4: goto 197
      // 0e7: fload 5
      // 0e9: aload 0
      // 0ea: getfield dev/boze/client/systems/modules/misc/AntiAim.yawAdd Ldev/boze/client/settings/FloatSetting;
      // 0ed: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 0f0: invokevirtual java/lang/Float.floatValue ()F
      // 0f3: fadd
      // 0f4: fstore 5
      // 0f6: goto 197
      // 0f9: aconst_null
      // 0fa: astore 6
      // 0fc: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 0ff: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 102: invokevirtual net/minecraft/client/world/ClientWorld.getEntities ()Ljava/lang/Iterable;
      // 105: invokeinterface java/lang/Iterable.iterator ()Ljava/util/Iterator; 1
      // 10a: astore 7
      // 10c: aload 7
      // 10e: invokeinterface java/util/Iterator.hasNext ()Z 1
      // 113: ifeq 163
      // 116: aload 7
      // 118: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
      // 11d: checkcast net/minecraft/entity/Entity
      // 120: astore 8
      // 122: aload 8
      // 124: instanceof net/minecraft/entity/player/PlayerEntity
      // 127: ifeq 160
      // 12a: aload 8
      // 12c: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 12f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 132: if_acmpeq 160
      // 135: aload 8
      // 137: invokestatic dev/boze/client/systems/modules/client/Friends.method2055 (Lnet/minecraft/entity/Entity;)Z
      // 13a: ifne 160
      // 13d: aload 6
      // 13f: ifnull 15c
      // 142: aload 6
      // 144: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 147: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 14a: invokevirtual net/minecraft/entity/Entity.distanceTo (Lnet/minecraft/entity/Entity;)F
      // 14d: aload 8
      // 14f: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 152: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 155: invokevirtual net/minecraft/entity/Entity.distanceTo (Lnet/minecraft/entity/Entity;)F
      // 158: fcmpl
      // 159: ifle 160
      // 15c: aload 8
      // 15e: astore 6
      // 160: goto 10c
      // 163: aload 6
      // 165: ifnull 183
      // 168: aload 6
      // 16a: invokevirtual net/minecraft/entity/Entity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 16d: invokestatic dev/boze/client/utils/EntityUtil.method2146 (Lnet/minecraft/util/math/Vec3d;)[F
      // 170: bipush 0
      // 171: faload
      // 172: fstore 5
      // 174: fload 5
      // 176: aload 0
      // 177: getfield dev/boze/client/systems/modules/misc/AntiAim.yawAdd Ldev/boze/client/settings/FloatSetting;
      // 17a: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 17d: invokevirtual java/lang/Float.floatValue ()F
      // 180: fadd
      // 181: fstore 5
      // 183: goto 197
      // 186: aload 0
      // 187: getfield dev/boze/client/systems/modules/misc/AntiAim.field2864 F
      // 18a: aload 0
      // 18b: getfield dev/boze/client/systems/modules/misc/AntiAim.yawAdd Ldev/boze/client/settings/FloatSetting;
      // 18e: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 191: invokevirtual java/lang/Float.floatValue ()F
      // 194: fadd
      // 195: fstore 5
      // 197: fload 5
      // 199: invokestatic net/minecraft/util/math/MathHelper.wrapDegrees (F)F
      // 19c: fstore 5
      // 19e: aload 0
      // 19f: fload 5
      // 1a1: putfield dev/boze/client/systems/modules/misc/AntiAim.field2864 F
      // 1a4: aload 1
      // 1a5: getfield dev/boze/client/events/MovementEvent.pitch F
      // 1a8: fstore 6
      // 1aa: aload 0
      // 1ab: getfield dev/boze/client/systems/modules/misc/AntiAim.pitch Ldev/boze/client/settings/EnumSetting;
      // 1ae: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 1b1: checkcast dev/boze/client/enums/AntiAimYaw
      // 1b4: invokevirtual dev/boze/client/enums/AntiAimYaw.ordinal ()I
      // 1b7: tableswitch 383 1 7 41 73 110 137 152 170 311
      // 1e0: aload 0
      // 1e1: getfield dev/boze/client/systems/modules/misc/AntiAim.field2865 F
      // 1e4: invokestatic java/lang/Math.random ()D
      // 1e7: ldc2_w 2.0
      // 1ea: dmul
      // 1eb: dconst_1
      // 1ec: dsub
      // 1ed: aload 0
      // 1ee: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchRange Ldev/boze/client/settings/FloatSetting;
      // 1f1: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 1f4: invokevirtual java/lang/Float.floatValue ()F
      // 1f7: f2d
      // 1f8: dmul
      // 1f9: d2f
      // 1fa: fadd
      // 1fb: fstore 6
      // 1fd: goto 336
      // 200: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 203: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 206: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getPitch ()F
      // 209: invokestatic java/lang/Math.random ()D
      // 20c: ldc2_w 2.0
      // 20f: dmul
      // 210: dconst_1
      // 211: dsub
      // 212: aload 0
      // 213: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchRange Ldev/boze/client/settings/FloatSetting;
      // 216: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 219: invokevirtual java/lang/Float.floatValue ()F
      // 21c: f2d
      // 21d: dmul
      // 21e: d2f
      // 21f: fadd
      // 220: fstore 6
      // 222: goto 336
      // 225: invokestatic java/lang/Math.random ()D
      // 228: ldc2_w 2.0
      // 22b: dmul
      // 22c: dconst_1
      // 22d: dsub
      // 22e: aload 0
      // 22f: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchRange Ldev/boze/client/settings/FloatSetting;
      // 232: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 235: invokevirtual java/lang/Float.floatValue ()F
      // 238: f2d
      // 239: dmul
      // 23a: d2f
      // 23b: fstore 6
      // 23d: goto 336
      // 240: aload 0
      // 241: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchLock Ldev/boze/client/settings/FloatSetting;
      // 244: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 247: invokevirtual java/lang/Float.floatValue ()F
      // 24a: fstore 6
      // 24c: goto 336
      // 24f: fload 6
      // 251: aload 0
      // 252: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchAdd Ldev/boze/client/settings/FloatSetting;
      // 255: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 258: invokevirtual java/lang/Float.floatValue ()F
      // 25b: fadd
      // 25c: fstore 6
      // 25e: goto 336
      // 261: aconst_null
      // 262: astore 7
      // 264: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 267: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 26a: invokevirtual net/minecraft/client/world/ClientWorld.getEntities ()Ljava/lang/Iterable;
      // 26d: invokeinterface java/lang/Iterable.iterator ()Ljava/util/Iterator; 1
      // 272: astore 8
      // 274: aload 8
      // 276: invokeinterface java/util/Iterator.hasNext ()Z 1
      // 27b: ifeq 2cb
      // 27e: aload 8
      // 280: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
      // 285: checkcast net/minecraft/entity/Entity
      // 288: astore 9
      // 28a: aload 9
      // 28c: instanceof net/minecraft/entity/player/PlayerEntity
      // 28f: ifeq 2c8
      // 292: aload 9
      // 294: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 297: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 29a: if_acmpeq 2c8
      // 29d: aload 9
      // 29f: invokestatic dev/boze/client/systems/modules/client/Friends.method2055 (Lnet/minecraft/entity/Entity;)Z
      // 2a2: ifne 2c8
      // 2a5: aload 7
      // 2a7: ifnull 2c4
      // 2aa: aload 7
      // 2ac: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 2af: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2b2: invokevirtual net/minecraft/entity/Entity.distanceTo (Lnet/minecraft/entity/Entity;)F
      // 2b5: aload 9
      // 2b7: getstatic dev/boze/client/systems/modules/misc/AntiAim.mc Lnet/minecraft/client/MinecraftClient;
      // 2ba: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 2bd: invokevirtual net/minecraft/entity/Entity.distanceTo (Lnet/minecraft/entity/Entity;)F
      // 2c0: fcmpl
      // 2c1: ifle 2c8
      // 2c4: aload 9
      // 2c6: astore 7
      // 2c8: goto 274
      // 2cb: aload 7
      // 2cd: ifnull 2eb
      // 2d0: aload 7
      // 2d2: invokevirtual net/minecraft/entity/Entity.getEyePos ()Lnet/minecraft/util/math/Vec3d;
      // 2d5: invokestatic dev/boze/client/utils/EntityUtil.method2146 (Lnet/minecraft/util/math/Vec3d;)[F
      // 2d8: bipush 1
      // 2d9: faload
      // 2da: fstore 6
      // 2dc: fload 6
      // 2de: aload 0
      // 2df: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchAdd Ldev/boze/client/settings/FloatSetting;
      // 2e2: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 2e5: invokevirtual java/lang/Float.floatValue ()F
      // 2e8: fadd
      // 2e9: fstore 6
      // 2eb: goto 336
      // 2ee: aload 0
      // 2ef: getfield dev/boze/client/systems/modules/misc/AntiAim.pitchAdd Ldev/boze/client/settings/FloatSetting;
      // 2f2: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
      // 2f5: invokevirtual java/lang/Float.floatValue ()F
      // 2f8: invokestatic java/lang/Math.abs (F)F
      // 2fb: fstore 7
      // 2fd: aload 0
      // 2fe: getfield dev/boze/client/systems/modules/misc/AntiAim.field2866 Z
      // 301: ifeq 310
      // 304: aload 0
      // 305: getfield dev/boze/client/systems/modules/misc/AntiAim.field2865 F
      // 308: fload 7
      // 30a: fsub
      // 30b: fstore 6
      // 30d: goto 319
      // 310: aload 0
      // 311: getfield dev/boze/client/systems/modules/misc/AntiAim.field2865 F
      // 314: fload 7
      // 316: fadd
      // 317: fstore 6
      // 319: fload 6
      // 31b: ldc 90.0
      // 31d: fcmpl
      // 31e: ifle 329
      // 321: aload 0
      // 322: bipush 1
      // 323: putfield dev/boze/client/systems/modules/misc/AntiAim.field2866 Z
      // 326: goto 336
      // 329: fload 6
      // 32b: ldc -90.0
      // 32d: fcmpg
      // 32e: ifge 336
      // 331: aload 0
      // 332: bipush 0
      // 333: putfield dev/boze/client/systems/modules/misc/AntiAim.field2866 Z
      // 336: fload 6
      // 338: ldc 90.0
      // 33a: fcmpl
      // 33b: ifle 345
      // 33e: ldc 90.0
      // 340: fstore 6
      // 342: goto 351
      // 345: fload 6
      // 347: ldc -90.0
      // 349: fcmpg
      // 34a: ifge 351
      // 34d: ldc -90.0
      // 34f: fstore 6
      // 351: aload 0
      // 352: fload 6
      // 354: putfield dev/boze/client/systems/modules/misc/AntiAim.field2865 F
      // 357: aload 1
      // 358: new dev/boze/client/utils/ActionWrapper
      // 35b: dup
      // 35c: invokedynamic run ()Ljava/lang/Runnable; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ ()V, dev/boze/client/systems/modules/misc/AntiAim.lambda$handleEvent$6 ()V, ()V ]
      // 361: fload 5
      // 363: fload 6
      // 365: invokespecial dev/boze/client/utils/ActionWrapper.<init> (Ljava/lang/Runnable;FF)V
      // 368: invokevirtual dev/boze/client/events/MovementEvent.method1074 (Ldev/boze/client/utils/ActionWrapper;)V
      // 36b: goto 386
      // 36e: aload 1
      // 36f: new dev/boze/client/utils/ActionWrapper
      // 372: dup
      // 373: invokedynamic run ()Ljava/lang/Runnable; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ ()V, dev/boze/client/systems/modules/misc/AntiAim.lambda$handleEvent$7 ()V, ()V ]
      // 378: aload 0
      // 379: getfield dev/boze/client/systems/modules/misc/AntiAim.field2864 F
      // 37c: aload 0
      // 37d: getfield dev/boze/client/systems/modules/misc/AntiAim.field2865 F
      // 380: invokespecial dev/boze/client/utils/ActionWrapper.<init> (Ljava/lang/Runnable;FF)V
      // 383: invokevirtual dev/boze/client/events/MovementEvent.method1074 (Ldev/boze/client/utils/ActionWrapper;)V
      // 386: return
   }

   private static void lambda$handleEvent$7() {
   }

   private static void lambda$handleEvent$6() {
   }

   private boolean lambda$new$5() {
      return this.pitch.method461() == AntiAimYaw.Offset || this.pitch.method461() == AntiAimYaw.Stare || this.pitch.method461() == AntiAimYaw.Spin;
   }

   private boolean lambda$new$4() {
      return this.pitch.method461() == AntiAimYaw.Lock;
   }

   private boolean lambda$new$3() {
      return this.pitch.method461() == AntiAimYaw.Jitter || this.pitch.method461() == AntiAimYaw.Random || this.pitch.method461() == AntiAimYaw.FOVJitter;
   }

   private boolean lambda$new$2() {
      return this.yaw.method461() == AntiAimYaw.Offset || this.yaw.method461() == AntiAimYaw.Stare || this.yaw.method461() == AntiAimYaw.Spin;
   }

   private boolean lambda$new$1() {
      return this.yaw.method461() == AntiAimYaw.Lock;
   }

   private boolean lambda$new$0() {
      return this.yaw.method461() == AntiAimYaw.Jitter || this.yaw.method461() == AntiAimYaw.Random || this.yaw.method461() == AntiAimYaw.FOVJitter;
   }
}
