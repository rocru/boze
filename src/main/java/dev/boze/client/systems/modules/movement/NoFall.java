package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.NoFallMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.utils.*;
import mapped.Class1202;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;

public class NoFall extends Module {
   public static final NoFall INSTANCE = new NoFall();
   private EnumSetting<NoFallMode> field527 = new EnumSetting<NoFallMode>("Mode", NoFallMode.Grim, "Mode for no fall", NoFall::lambda$new$0);
   private final MinMaxDoubleSetting field528 = new MinMaxDoubleSetting("AimSpeed", new double[]{1.0, 2.0}, 0.1, 5.0, 0.1, "Aim speed", this::method1971);
   public final MinMaxDoubleSetting field529 = new MinMaxDoubleSetting(
      "SwapDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to swap in ticks", this::method1971
   );
   public final MinMaxDoubleSetting field530 = new MinMaxDoubleSetting(
      "PlaceDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to place in ticks", this::method1971
   );
   private final BooleanSetting field531 = new BooleanSetting("MultiTask", false, "Place blocks while already using items", this::method1971);
   private MinMaxSetting field532 = new MinMaxSetting("Distance", 3.0, 0.1, 20.0, 0.1, "Minimum fall distance to activate no fall");
   private BooleanSetting field533 = new BooleanSetting("Proximity", false, "Only activate no fall when close to ground", this::lambda$new$1);
   private MinMaxSetting field534 = new MinMaxSetting("Height", 4.0, 0.1, 10.0, 0.1, "Height above ground to activate no fall", this.field533);
   private Timer field535 = new Timer();
   private boolean field536 = false;
   private boolean field537 = false;
   private final Timer field538 = new Timer();
   private final Timer field539 = new Timer();

   private NoFallMode method275() {
      return Options.INSTANCE.method1971() ? NoFallMode.Ghost : this.field527.getValue();
   }

   private boolean method1971() {
      return this.method275() == NoFallMode.Ghost;
   }

   public NoFall() {
      super("NoFall", "Prevent falling or fall damage", Category.Movement);
      this.field435 = true;
   }

   @Override
   public void onEnable() {
      this.field536 = false;
      this.field537 = false;
   }

   @EventHandler(
      priority = 300
   )
   public void method1695(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive() && this.method1971() && !event.method1022()) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
            if (this.method1972()) {
               int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
               if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                  RotationHelper var6 = new RotationHelper(mc.player.getYaw(), 90.0F);
                  BlockHitResult var7 = RaycastUtil.method574(this.field533.getValue() ? this.field534.getValue() : 20.0, var6);
                  if (var7 != null && var7.getType() != Type.MISS) {
                     RotationHelper var8 = Class1202.method2391(mc.player.getEyePos(), var7.getPos());
                     var6 = var6.method603(var8, this.field528.getValue());
                     RotationHelper var9 = new RotationHelper(mc.player);
                     RotationHelper var10 = var6.method1600();
                     RotationHelper var11 = var10.method606(var9);
                     Pair[] var12 = RotationHelper.method614(var11);
                     Pair var13 = var12[0];

                     for (Pair var17 : var12) {
                        BlockHitResult var18 = RaycastUtil.method574(
                           this.field533.getValue() ? this.field534.getValue() : 20.0, RotationHelper.method613(var9, var17)
                        );
                        if (var18.getType() != Type.MISS && var18.getBlockPos() == var7.getBlockPos() && var18.getSide() == var7.getSide()) {
                           var13 = var17;
                        }
                     }

                     event.deltaY = event.deltaY + (Double)var13.getRight();
                     event.method1021(true);
                  }
               } else {
                  this.field539.reset();
               }
            }
         }
      }
   }

   @EventHandler
   public void method1693(HandleInputEvent event) {
      if (this.method1971()) {
         if (this.method1972()) {
            int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
            if (this.field538.hasElapsed(this.field529.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
               ((KeyBindingAccessor)mc.options.hotbarKeys[var5]).setTimesPressed(1);
               this.field538.reset();
               this.field529.method1296();
            }
         } else {
            this.field538.reset();
         }
      }
   }

   @EventHandler(
      priority = 76
   )
   public void method1883(RotationEvent event) {
      if (!Options.method477(this.field531.getValue()) && !event.method554(RotationMode.Vanilla)) {
         if (this.method1971() && this.method1972()) {
            int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
            if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
               RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
               BlockHitResult var7 = RaycastUtil.method574(Reach.method1614(), var6);
               if (var7 != null && var7.getType() != Type.MISS) {
                  if (this.field539.hasElapsed(this.field530.method1295() * 50.0)) {
                     ((KeyBindingAccessor)mc.options.useKey).setTimesPressed(1);
                     this.field539.reset();
                     this.field530.method1296();
                  }
               } else {
                  this.field539.reset();
               }
            } else {
               this.field539.reset();
            }
         }
      }
   }

   private boolean method1972() {
      if ((double)mc.player.fallDistance >= this.field532.getValue()
         && (
            !this.field533.getValue()
               || !mc.world
                  .isSpaceEmpty(
                     new Box(
                        mc.player.getBoundingBox().minX,
                        mc.player.getBoundingBox().minY - this.field534.getValue(),
                        mc.player.getBoundingBox().minZ,
                        mc.player.getBoundingBox().maxX,
                        mc.player.getBoundingBox().maxY,
                        mc.player.getBoundingBox().maxZ
                     )
                  )
         )) {
         if (this.method1971()) {
            RotationHelper var4 = new RotationHelper(mc.player.getYaw(), 90.0F);
            BlockHitResult var5 = RaycastUtil.method574(Reach.method1614(), var4);
            if (var5 != null && var5.getType() != Type.MISS && mc.world.getBlockState(var5.getBlockPos()).getBlock() == Blocks.WATER) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @EventHandler
   private void method2041(MovementEvent param1) {
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
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 0
      // 001: invokevirtual dev/boze/client/systems/modules/movement/NoFall.method1971 ()Z
      // 004: ifeq 008
      // 007: return
      // 008: aload 0
      // 009: getfield dev/boze/client/systems/modules/movement/NoFall.field535 Ldev/boze/client/utils/Timer;
      // 00c: ldc2_w 500.0
      // 00f: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // 012: ifne 016
      // 015: return
      // 016: aload 0
      // 017: invokevirtual dev/boze/client/systems/modules/movement/NoFall.method275 ()Ldev/boze/client/enums/NoFallMode;
      // 01a: getstatic dev/boze/client/enums/NoFallMode.Grim Ldev/boze/client/enums/NoFallMode;
      // 01d: if_acmpne 0bd
      // 020: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 023: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 026: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isOnGround ()Z
      // 029: ifeq 034
      // 02c: aload 0
      // 02d: bipush 0
      // 02e: putfield dev/boze/client/systems/modules/movement/NoFall.field536 Z
      // 031: goto 0bc
      // 034: aload 0
      // 035: getfield dev/boze/client/systems/modules/movement/NoFall.field536 Z
      // 038: ifne 0bc
      // 03b: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 03e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 041: getfield net/minecraft/client/network/ClientPlayerEntity.fallDistance F
      // 044: f2d
      // 045: aload 0
      // 046: getfield dev/boze/client/systems/modules/movement/NoFall.field532 Ldev/boze/client/settings/MinMaxSetting;
      // 049: invokevirtual dev/boze/client/settings/MinMaxSetting.getValue ()Ljava/lang/Double;
      // 04c: invokevirtual java/lang/Double.doubleValue ()D
      // 04f: dcmpl
      // 050: iflt 0bc
      // 053: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 056: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
      // 059: new net/minecraft/util/math/Box
      // 05c: dup
      // 05d: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 060: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 063: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 066: getfield net/minecraft/util/math/Box.minX D
      // 069: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 06c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 06f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 072: getfield net/minecraft/util/math/Box.minY D
      // 075: ldc2_w 2.0
      // 078: dsub
      // 079: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 07c: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 07f: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 082: getfield net/minecraft/util/math/Box.minZ D
      // 085: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 088: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 08b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 08e: getfield net/minecraft/util/math/Box.maxX D
      // 091: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 094: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 097: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 09a: getfield net/minecraft/util/math/Box.maxY D
      // 09d: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 0a0: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0a3: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBoundingBox ()Lnet/minecraft/util/math/Box;
      // 0a6: getfield net/minecraft/util/math/Box.maxZ D
      // 0a9: invokespecial net/minecraft/util/math/Box.<init> (DDDDDD)V
      // 0ac: invokevirtual net/minecraft/client/world/ClientWorld.isSpaceEmpty (Lnet/minecraft/util/math/Box;)Z
      // 0af: ifne 0bc
      // 0b2: aload 0
      // 0b3: bipush 1
      // 0b4: putfield dev/boze/client/systems/modules/movement/NoFall.field537 Z
      // 0b7: aload 0
      // 0b8: bipush 1
      // 0b9: putfield dev/boze/client/systems/modules/movement/NoFall.field536 Z
      // 0bc: return
      // 0bd: aload 0
      // 0be: invokevirtual dev/boze/client/systems/modules/movement/NoFall.method1972 ()Z
      // 0c1: ifeq 251
      // 0c4: aload 0
      // 0c5: invokevirtual dev/boze/client/systems/modules/movement/NoFall.method275 ()Ldev/boze/client/enums/NoFallMode;
      // 0c8: invokevirtual dev/boze/client/enums/NoFallMode.ordinal ()I
      // 0cb: tableswitch 390 0 5 37 390 45 94 109 312
      // 0f0: aload 1
      // 0f1: bipush 1
      // 0f2: putfield dev/boze/client/events/MovementEvent.isOnGround Z
      // 0f5: goto 251
      // 0f8: aload 1
      // 0f9: aload 1
      // 0fa: getfield dev/boze/client/events/MovementEvent.field1931 D
      // 0fd: ldc2_w 0.001
      // 100: dadd
      // 101: putfield dev/boze/client/events/MovementEvent.field1931 D
      // 104: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 107: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 10a: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 10d: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 110: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 113: getfield net/minecraft/util/math/Vec3d.x D
      // 116: dconst_0
      // 117: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 11a: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 11d: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getVelocity ()Lnet/minecraft/util/math/Vec3d;
      // 120: getfield net/minecraft/util/math/Vec3d.z D
      // 123: invokevirtual net/minecraft/client/network/ClientPlayerEntity.setVelocity (DDD)V
      // 126: goto 251
      // 129: aload 1
      // 12a: aload 1
      // 12b: getfield dev/boze/client/events/MovementEvent.field1931 D
      // 12e: ldc2_w 3.0
      // 131: dadd
      // 132: putfield dev/boze/client/events/MovementEvent.field1931 D
      // 135: goto 251
      // 138: bipush 0
      // 139: istore 5
      // 13b: iload 5
      // 13d: bipush 10
      // 13f: if_icmpge 200
      // 142: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 145: invokevirtual net/minecraft/client/MinecraftClient.getNetworkHandler ()Lnet/minecraft/client/network/ClientPlayNetworkHandler;
      // 148: new net/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$Full
      // 14b: dup
      // 14c: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 14f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 152: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
      // 155: bipush 10
      // 157: invokestatic java/lang/Math.random ()D
      // 15a: ldc2_w 20.0
      // 15d: dmul
      // 15e: d2i
      // 15f: isub
      // 160: i2d
      // 161: dadd
      // 162: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 165: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 168: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
      // 16b: bipush 10
      // 16d: invokestatic java/lang/Math.random ()D
      // 170: ldc2_w 20.0
      // 173: dmul
      // 174: d2i
      // 175: isub
      // 176: i2d
      // 177: dadd
      // 178: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 17b: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 17e: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
      // 181: bipush 10
      // 183: invokestatic java/lang/Math.random ()D
      // 186: ldc2_w 20.0
      // 189: dmul
      // 18a: d2i
      // 18b: isub
      // 18c: i2d
      // 18d: dadd
      // 18e: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 191: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 194: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getYaw ()F
      // 197: sipush 180
      // 19a: invokestatic java/lang/Math.random ()D
      // 19d: ldc2_w 360.0
      // 1a0: dmul
      // 1a1: d2i
      // 1a2: isub
      // 1a3: i2f
      // 1a4: fadd
      // 1a5: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 1a8: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1ab: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getPitch ()F
      // 1ae: bipush 1
      // 1af: invokespecial net/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket$Full.<init> (DDDFFZ)V
      // 1b2: invokevirtual net/minecraft/client/network/ClientPlayNetworkHandler.sendPacket (Lnet/minecraft/network/packet/Packet;)V
      // 1b5: getstatic net/minecraft/util/Hand.MAIN_HAND Lnet/minecraft/util/Hand;
      // 1b8: new net/minecraft/util/hit/BlockHitResult
      // 1bb: dup
      // 1bc: getstatic net/minecraft/util/math/Vec3d.ZERO Lnet/minecraft/util/math/Vec3d;
      // 1bf: getstatic net/minecraft/util/math/Direction.UP Lnet/minecraft/util/math/Direction;
      // 1c2: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 1c5: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1c8: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getPos ()Lnet/minecraft/util/math/Vec3d;
      // 1cb: invokestatic net/minecraft/util/math/BlockPos.ofFloored (Lnet/minecraft/util/math/Position;)Lnet/minecraft/util/math/BlockPos;
      // 1ce: bipush 10
      // 1d0: invokestatic java/lang/Math.random ()D
      // 1d3: ldc2_w 20.0
      // 1d6: dmul
      // 1d7: d2i
      // 1d8: isub
      // 1d9: bipush 10
      // 1db: invokestatic java/lang/Math.random ()D
      // 1de: ldc2_w 20.0
      // 1e1: dmul
      // 1e2: d2i
      // 1e3: isub
      // 1e4: bipush 10
      // 1e6: invokestatic java/lang/Math.random ()D
      // 1e9: ldc2_w 20.0
      // 1ec: dmul
      // 1ed: d2i
      // 1ee: isub
      // 1ef: invokevirtual net/minecraft/util/math/BlockPos.add (III)Lnet/minecraft/util/math/BlockPos;
      // 1f2: bipush 0
      // 1f3: invokespecial net/minecraft/util/hit/BlockHitResult.<init> (Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;Lnet/minecraft/util/math/BlockPos;Z)V
      // 1f6: invokestatic mapped/Class5913.method17 (Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/network/packet/c2s/play/PlayerInteractBlockC2SPacket;
      // 1f9: pop
      // 1fa: iinc 5 1
      // 1fd: goto 13b
      // 200: goto 251
      // 203: invokedynamic test ()Ljava/util/function/Predicate; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Z, dev/boze/client/systems/modules/movement/NoFall.lambda$onSendMovementPackets$2 (Lnet/minecraft/item/ItemStack;)Z, (Lnet/minecraft/item/ItemStack;)Z ]
      // 208: invokestatic dev/boze/client/utils/InventoryHelper.method168 (Ljava/util/function/Predicate;)I
      // 20b: istore 5
      // 20d: iload 5
      // 20f: bipush -1
      // 210: if_icmpne 214
      // 213: return
      // 214: getstatic mapped/Class2811.field101 Z
      // 217: istore 6
      // 219: bipush 1
      // 21a: putstatic mapped/Class2811.field101 Z
      // 21d: getstatic dev/boze/client/systems/modules/movement/NoFall.mc Lnet/minecraft/client/MinecraftClient;
      // 220: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 223: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getPos ()Lnet/minecraft/util/math/Vec3d;
      // 226: invokestatic net/minecraft/util/math/BlockPos.ofFloored (Lnet/minecraft/util/math/Position;)Lnet/minecraft/util/math/BlockPos;
      // 229: invokevirtual net/minecraft/util/math/BlockPos.down ()Lnet/minecraft/util/math/BlockPos;
      // 22c: bipush 1
      // 22d: bipush 1
      // 22e: bipush 0
      // 22f: bipush 0
      // 230: getstatic net/minecraft/util/Hand.MAIN_HAND Lnet/minecraft/util/Hand;
      // 233: iload 5
      // 235: invokestatic mapped/Class2812.method5502 (Lnet/minecraft/util/math/BlockPos;ZZZZLnet/minecraft/util/Hand;I)Ldev/boze/client/utils/PlaceAction;
      // 238: astore 7
      // 23a: aload 7
      // 23c: ifnull 24c
      // 23f: aload 1
      // 240: new dev/boze/client/utils/ActionWrapper
      // 243: dup
      // 244: aload 7
      // 246: invokespecial dev/boze/client/utils/ActionWrapper.<init> (Ldev/boze/client/utils/PlaceAction;)V
      // 249: invokevirtual dev/boze/client/events/MovementEvent.method1074 (Ldev/boze/client/utils/ActionWrapper;)V
      // 24c: iload 6
      // 24e: putstatic mapped/Class2811.field101 Z
      // 251: return
   }

   @EventHandler
   private void method2042(PacketBundleEvent var1) {
      if (var1.packet instanceof PlayerPositionLookS2CPacket) {
         this.field535.reset();
      }
   }

   @EventHandler
   private void method240(PostPacketSendEvent var1) {
      if (var1.packet instanceof PlayerMoveC2SPacket var5 && this.field537 && !this.method1971()) {
         this.field537 = false;
         mc.getNetworkHandler()
            .sendPacket(
               new Full(
                  var5.getX(mc.player.getX()),
                  var5.getY(mc.player.getY()) + 1.00001E-9,
                  var5.getZ(mc.player.getZ()),
                  var5.getYaw(mc.player.getYaw()) + 1337.0F,
                  var5.getPitch(mc.player.getPitch()),
                  var5.isOnGround()
               )
            );
      }
   }

   private static boolean lambda$onSendMovementPackets$2(ItemStack var0) {
      return var0.getItem() instanceof BlockItem;
   }

   private boolean lambda$new$1() {
      return this.method275() != NoFallMode.Grim;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
