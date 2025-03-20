package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.PistonPushStage;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.jumptable.mz;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.PlaceAction;
import mapped.Class2811;
import mapped.Class2812;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;

public class PistonPush extends Module {
   public static final PistonPush INSTANCE = new PistonPush();
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "Multi Task");
   private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing");
   private final MinMaxSetting range = new MinMaxSetting("Range", 4.5, 1.0, 6.0, 0.1, "Range");
   private final BooleanSetting fillHole = new BooleanSetting("FillHole", false, "Fill hole after pushing player out");
   public static final BooleanSetting onlyWhileSneaking = new BooleanSetting("OnlyWhileSneaking", false, "Only push while sneaking");
   private PlayerEntity field2557;
   private BlockPos field2558;
   private BlockPos field2559;
   private BlockPos field2560;
   private BlockPos field2561;
   private Direction field2562;
   private PistonPushStage stage;

   public PistonPush() {
      super("PistonPush", "Pushes players out of holes with pistons", Category.Combat);
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field2558 = null;
         this.field2559 = null;
         this.field2560 = null;
         this.field2562 = null;
         this.stage = PistonPushStage.Initial;
         this.field2557 = this.method1520();
         if (this.field2557 == null) {
            this.setEnabled(false);
            ChatInstance.method624("No target in range");
         }
      }
   }

   @EventHandler
   public void method1507(MovementEvent event) {
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
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: getstatic dev/boze/client/systems/modules/combat/PistonPush.onlyWhileSneaking Ldev/boze/client/settings/BooleanSetting;
      // 003: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 006: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 009: ifeq 01c
      // 00c: getstatic dev/boze/client/systems/modules/combat/PistonPush.mc Lnet/minecraft/client/MinecraftClient;
      // 00f: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 012: getfield net/minecraft/client/network/ClientPlayerEntity.input Lnet/minecraft/client/input/Input;
      // 015: getfield net/minecraft/client/input/Input.sneaking Z
      // 018: ifne 01c
      // 01b: return
      // 01c: aload 0
      // 01d: getfield dev/boze/client/systems/modules/combat/PistonPush.multiTask Ldev/boze/client/settings/BooleanSetting;
      // 020: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 023: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 026: invokestatic dev/boze/client/systems/modules/client/Options.method477 (Z)Z
      // 029: ifeq 02d
      // 02c: return
      // 02d: aload 0
      // 02e: getfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 031: ifnonnull 03b
      // 034: aload 0
      // 035: getstatic dev/boze/client/enums/PistonPushStage.Initial Ldev/boze/client/enums/PistonPushStage;
      // 038: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 03b: aload 0
      // 03c: getfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 03f: invokevirtual dev/boze/client/enums/PistonPushStage.ordinal ()I
      // 042: tableswitch 342 0 4 34 206 247 282 336
      // 064: aload 0
      // 065: getfield dev/boze/client/systems/modules/combat/PistonPush.field2557 Lnet/minecraft/entity/player/PlayerEntity;
      // 068: ifnonnull 073
      // 06b: aload 0
      // 06c: getstatic dev/boze/client/enums/PistonPushStage.Done Ldev/boze/client/enums/PistonPushStage;
      // 06f: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 072: return
      // 073: aload 0
      // 074: aload 0
      // 075: getfield dev/boze/client/systems/modules/combat/PistonPush.field2557 Lnet/minecraft/entity/player/PlayerEntity;
      // 078: invokevirtual net/minecraft/entity/player/PlayerEntity.getBlockPos ()Lnet/minecraft/util/math/BlockPos;
      // 07b: invokevirtual net/minecraft/util/math/BlockPos.up ()Lnet/minecraft/util/math/BlockPos;
      // 07e: putfield dev/boze/client/systems/modules/combat/PistonPush.field2561 Lnet/minecraft/util/math/BlockPos;
      // 081: aload 0
      // 082: aload 0
      // 083: aload 0
      // 084: getfield dev/boze/client/systems/modules/combat/PistonPush.field2561 Lnet/minecraft/util/math/BlockPos;
      // 087: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1512 (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
      // 08a: putfield dev/boze/client/systems/modules/combat/PistonPush.field2558 Lnet/minecraft/util/math/BlockPos;
      // 08d: aload 0
      // 08e: aload 0
      // 08f: aload 0
      // 090: getfield dev/boze/client/systems/modules/combat/PistonPush.field2558 Lnet/minecraft/util/math/BlockPos;
      // 093: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1511 (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/BlockPos;
      // 096: putfield dev/boze/client/systems/modules/combat/PistonPush.field2559 Lnet/minecraft/util/math/BlockPos;
      // 099: aload 0
      // 09a: aload 0
      // 09b: getfield dev/boze/client/systems/modules/combat/PistonPush.field2561 Lnet/minecraft/util/math/BlockPos;
      // 09e: invokevirtual net/minecraft/util/math/BlockPos.down ()Lnet/minecraft/util/math/BlockPos;
      // 0a1: putfield dev/boze/client/systems/modules/combat/PistonPush.field2560 Lnet/minecraft/util/math/BlockPos;
      // 0a4: aload 0
      // 0a5: bipush 4
      // 0a6: anewarray 171
      // 0a9: dup
      // 0aa: bipush 0
      // 0ab: aload 0
      // 0ac: getfield dev/boze/client/systems/modules/combat/PistonPush.field2561 Lnet/minecraft/util/math/BlockPos;
      // 0af: aastore
      // 0b0: dup
      // 0b1: bipush 1
      // 0b2: aload 0
      // 0b3: getfield dev/boze/client/systems/modules/combat/PistonPush.field2558 Lnet/minecraft/util/math/BlockPos;
      // 0b6: aastore
      // 0b7: dup
      // 0b8: bipush 2
      // 0b9: aload 0
      // 0ba: getfield dev/boze/client/systems/modules/combat/PistonPush.field2559 Lnet/minecraft/util/math/BlockPos;
      // 0bd: aastore
      // 0be: dup
      // 0bf: bipush 3
      // 0c0: aload 0
      // 0c1: getfield dev/boze/client/systems/modules/combat/PistonPush.field2560 Lnet/minecraft/util/math/BlockPos;
      // 0c4: aastore
      // 0c5: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1517 ([Lnet/minecraft/util/math/BlockPos;)Z
      // 0c8: ifeq 0d5
      // 0cb: aload 0
      // 0cc: getstatic dev/boze/client/enums/PistonPushStage.Done Ldev/boze/client/enums/PistonPushStage;
      // 0cf: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 0d2: goto 198
      // 0d5: aload 0
      // 0d6: bipush 4
      // 0d7: anewarray 171
      // 0da: dup
      // 0db: bipush 0
      // 0dc: aload 0
      // 0dd: getfield dev/boze/client/systems/modules/combat/PistonPush.field2561 Lnet/minecraft/util/math/BlockPos;
      // 0e0: aastore
      // 0e1: dup
      // 0e2: bipush 1
      // 0e3: aload 0
      // 0e4: getfield dev/boze/client/systems/modules/combat/PistonPush.field2558 Lnet/minecraft/util/math/BlockPos;
      // 0e7: aastore
      // 0e8: dup
      // 0e9: bipush 2
      // 0ea: aload 0
      // 0eb: getfield dev/boze/client/systems/modules/combat/PistonPush.field2559 Lnet/minecraft/util/math/BlockPos;
      // 0ee: aastore
      // 0ef: dup
      // 0f0: bipush 3
      // 0f1: aload 0
      // 0f2: getfield dev/boze/client/systems/modules/combat/PistonPush.field2560 Lnet/minecraft/util/math/BlockPos;
      // 0f5: aastore
      // 0f6: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1518 ([Lnet/minecraft/util/math/BlockPos;)Z
      // 0f9: ifeq 106
      // 0fc: aload 0
      // 0fd: getstatic dev/boze/client/enums/PistonPushStage.Done Ldev/boze/client/enums/PistonPushStage;
      // 100: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 103: goto 198
      // 106: aload 0
      // 107: getstatic dev/boze/client/enums/PistonPushStage.Piston Ldev/boze/client/enums/PistonPushStage;
      // 10a: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 10d: goto 198
      // 110: aload 0
      // 111: bipush 2
      // 112: anewarray 194
      // 115: dup
      // 116: bipush 0
      // 117: getstatic net/minecraft/block/Blocks.PISTON Lnet/minecraft/block/Block;
      // 11a: aastore
      // 11b: dup
      // 11c: bipush 1
      // 11d: getstatic net/minecraft/block/Blocks.STICKY_PISTON Lnet/minecraft/block/Block;
      // 120: aastore
      // 121: invokestatic dev/boze/client/utils/InventoryHelper.method163 ([Lnet/minecraft/block/Block;)I
      // 124: aload 0
      // 125: getfield dev/boze/client/systems/modules/combat/PistonPush.field2558 Lnet/minecraft/util/math/BlockPos;
      // 128: aload 1
      // 129: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1508 (ILnet/minecraft/util/math/BlockPos;Ldev/boze/client/events/MovementEvent;)Z
      // 12c: ifeq 198
      // 12f: aload 0
      // 130: getstatic dev/boze/client/enums/PistonPushStage.Redstone Ldev/boze/client/enums/PistonPushStage;
      // 133: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 136: goto 198
      // 139: aload 0
      // 13a: bipush 1
      // 13b: anewarray 194
      // 13e: dup
      // 13f: bipush 0
      // 140: getstatic net/minecraft/block/Blocks.REDSTONE_BLOCK Lnet/minecraft/block/Block;
      // 143: aastore
      // 144: invokestatic dev/boze/client/utils/InventoryHelper.method163 ([Lnet/minecraft/block/Block;)I
      // 147: aload 0
      // 148: getfield dev/boze/client/systems/modules/combat/PistonPush.field2559 Lnet/minecraft/util/math/BlockPos;
      // 14b: aload 1
      // 14c: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1508 (ILnet/minecraft/util/math/BlockPos;Ldev/boze/client/events/MovementEvent;)Z
      // 14f: ifeq 198
      // 152: aload 0
      // 153: getstatic dev/boze/client/enums/PistonPushStage.Obsidian Ldev/boze/client/enums/PistonPushStage;
      // 156: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 159: goto 198
      // 15c: aload 0
      // 15d: getfield dev/boze/client/systems/modules/combat/PistonPush.fillHole Ldev/boze/client/settings/BooleanSetting;
      // 160: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 163: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 166: ifeq 188
      // 169: bipush 1
      // 16a: putstatic mapped/Class2811.field99 Z
      // 16d: aload 0
      // 16e: bipush 1
      // 16f: anewarray 194
      // 172: dup
      // 173: bipush 0
      // 174: getstatic net/minecraft/block/Blocks.OBSIDIAN Lnet/minecraft/block/Block;
      // 177: aastore
      // 178: invokestatic dev/boze/client/utils/InventoryHelper.method163 ([Lnet/minecraft/block/Block;)I
      // 17b: aload 0
      // 17c: getfield dev/boze/client/systems/modules/combat/PistonPush.field2560 Lnet/minecraft/util/math/BlockPos;
      // 17f: aload 1
      // 180: invokevirtual dev/boze/client/systems/modules/combat/PistonPush.method1508 (ILnet/minecraft/util/math/BlockPos;Ldev/boze/client/events/MovementEvent;)Z
      // 183: pop
      // 184: bipush 0
      // 185: putstatic mapped/Class2811.field99 Z
      // 188: aload 0
      // 189: getstatic dev/boze/client/enums/PistonPushStage.Done Ldev/boze/client/enums/PistonPushStage;
      // 18c: putfield dev/boze/client/systems/modules/combat/PistonPush.stage Ldev/boze/client/enums/PistonPushStage;
      // 18f: goto 198
      // 192: aload 0
      // 193: bipush 0
      // 194: invokevirtual dev/boze/client/systems/modules/Module.setEnabled (Z)Z
      // 197: pop
      // 198: return
   }

   private boolean method1508(int var1, BlockPos var2, MovementEvent var3) {
      if (var2 != null && var1 != -1) {
         PlaceAction var7 = Class2812.method5501(var2, true, this.swing.getValue(), false, Hand.MAIN_HAND, var1);
         if (var7 == null) {
            return false;
         } else {
            float var8 = (float)this.method1509(this.field2562);
            var7.method2158(var8);
            var3.method1074(new ActionWrapper(var7));
            return true;
         }
      } else {
         return false;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private int method1509(Direction var1) {
      if (var1 == null) {
         return (int)mc.player.getYaw();
      } else {
         return switch (mz.field2115[var1.ordinal()]) {
            case 1 -> 180;
            case 2 -> 0;
            case 3 -> 90;
            default -> -90;
         };
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private Direction method1510(Direction var1) {
      return switch (mz.field2115[var1.ordinal()]) {
         case 1 -> Direction.SOUTH;
         case 2 -> Direction.NORTH;
         case 3 -> Direction.EAST;
         default -> Direction.WEST;
      };
   }

   private BlockPos method1511(BlockPos var1) {
      ArrayList var5 = new ArrayList();
      if (var1 == null) {
         return null;
      } else {
         for (Direction var9 : Direction.values()) {
            if (!this.method1514(var1.offset(var9)) && this.method1513(var1.offset(var9))) {
               var5.add(var1.offset(var9));
            }
         }

         if (var5.isEmpty()) {
            return null;
         } else {
            var5.sort(Comparator.comparingDouble(PistonPush::method1515));
            return (BlockPos)var5.get(0);
         }
      }
   }

   private BlockPos method1512(BlockPos var1) {
      ArrayList var5 = new ArrayList();

      for (Direction var9 : Direction.values()) {
         if (var9 != Direction.DOWN && var9 != Direction.UP && !this.method1514(var1.offset(var9))) {
            boolean var10 = this.method1519(var1.up())
               && this.method1519(var1.offset(this.method1510(var9)))
               && this.method1519(var1.offset(this.method1510(var9)).up());
            if (Class2812.method2101(var1.offset(var9)) && var10) {
               var5.add(var1.offset(var9));
            }
         }
      }

      if (var5.isEmpty()) {
         return null;
      } else {
         var5.sort(Comparator.comparingDouble(PistonPush::method1515));
         this.field2562 = this.method1516(var1, (BlockPos)var5.get(0));
         return (BlockPos)var5.get(0);
      }
   }

   private boolean method1513(BlockPos var1) {
      if (Class2811.field104
         || mc.world.getBlockState(var1).isReplaceable()
         || Class2811.field105 && mc.world.getBlockState(var1).getBlock() instanceof BedBlock) {
         return Class2811.field99 ? true : mc.world.canPlace(Blocks.DIRT.getDefaultState(), var1, ShapeContext.absent());
      } else {
         return false;
      }
   }

   private boolean method1514(BlockPos var1) {
      return !mc.world.canPlace(Blocks.DIRT.getDefaultState(), var1, ShapeContext.absent());
   }

   private static double method1515(BlockPos var0) {
      return new Vec3d((double)var0.getX() + 0.5, (double)var0.getY() + 0.5, (double)var0.getZ() + 0.5).distanceTo(mc.player.getEyePos());
   }

   private Direction method1516(BlockPos var1, BlockPos var2) {
      for (Direction var9 : Direction.values()) {
         if (var9 != Direction.DOWN && var9 != Direction.UP && var1.offset(var9).equals(var2)) {
            return var9;
         }
      }

      return null;
   }

   private boolean method1517(BlockPos... var1) {
      for (BlockPos var8 : var1) {
         if (var8 == null) {
            return true;
         }
      }

      return false;
   }

   private boolean method1518(BlockPos... var1) {
      for (BlockPos var8 : var1) {
         if (var8 != null
            && new Vec3d((double)var8.getX() + 0.5, (double)var8.getY() + 0.5, (double)var8.getZ() + 0.5).distanceTo(mc.player.getEyePos())
               > this.range.getValue() + 0.5) {
            return true;
         }
      }

      return false;
   }

   private boolean method1519(BlockPos var1) {
      return mc.world.getBlockState(var1).isAir();
   }

   private PlayerEntity method1520() {
      ArrayList var4 = new ArrayList();

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof PlayerEntity
            && var6 != mc.player
            && !Friends.method2055(var6)
            && !(var6.getEyePos().distanceTo(mc.player.getEyePos()) > this.range.getValue() + 1.0)
            && !((PlayerEntity)var6).isDead()
            && !(((PlayerEntity)var6).getHealth() + ((LivingEntity)var6).getAbsorptionAmount() <= 0.0F)) {
            var4.add((PlayerEntity)var6);
         }
      }

      return (PlayerEntity)var4.stream().min(Comparator.comparing(PistonPush::lambda$getTarget$0)).orElse(null);
   }

   private static Float lambda$getTarget$0(PlayerEntity var0) {
      return var0.distanceTo(mc.player);
   }
}
