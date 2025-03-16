package dev.boze.client.utils;

import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SlotSwapMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.combat.OffHand;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import mapped.Class2839;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtil implements IMinecraft {
   private static Module field1249 = null;
   private static int field1250 = -1;
   private static SlotSwapMode field1251 = null;
   private static int field1252 = -1;
   public static int field1253 = -1;
   public static int[] field1254 = null;
   public static int[] field1255 = null;

   public static boolean method2114() {
      return field1250 != -1;
   }

   public static int method2010() {
      return field1250;
   }

   public static int method1547() {
      return field1252;
   }

   public static ItemStack method1774() {
      return field1250 != -1 && field1252 != -1 ? mc.player.getInventory().getStack(field1252) : mc.player.getMainHandStack();
   }

   public static Module method532() {
      return field1249;
   }

   public static void method1649(int param0) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: getstatic dev/boze/client/utils/InventoryUtil.field1250 I
      // 03: bipush -1
      // 04: if_icmpne 08
      // 07: return
      // 08: getstatic dev/boze/client/utils/InventoryUtil.field1251 Ldev/boze/client/enums/SlotSwapMode;
      // 0b: invokevirtual dev/boze/client/enums/SlotSwapMode.ordinal ()I
      // 0e: lookupswitch 70 2 0 26 1 52
      // 28: iload 0
      // 29: getstatic dev/boze/client/utils/InventoryUtil.field1252 I
      // 2c: if_icmpeq 54
      // 2f: aconst_null
      // 30: putstatic dev/boze/client/utils/InventoryUtil.field1249 Ldev/boze/client/systems/modules/Module;
      // 33: bipush -1
      // 34: putstatic dev/boze/client/utils/InventoryUtil.field1250 I
      // 37: aconst_null
      // 38: putstatic dev/boze/client/utils/InventoryUtil.field1251 Ldev/boze/client/enums/SlotSwapMode;
      // 3b: bipush -1
      // 3c: putstatic dev/boze/client/utils/InventoryUtil.field1252 I
      // 3f: goto 54
      // 42: getstatic dev/boze/client/utils/InventoryUtil.field1254 [I
      // 45: ifnull 54
      // 48: iload 0
      // 49: getstatic dev/boze/client/utils/InventoryUtil.field1254 [I
      // 4c: bipush 1
      // 4d: iaload
      // 4e: if_icmpeq 54
      // 51: invokestatic dev/boze/client/utils/InventoryUtil.method1904 ()V
      // 54: return
   }

   public static boolean method533(Module var0, int var1, AutoMineSwapMode var2, int var3) {
      return var2 == AutoMineSwapMode.Off ? false : method534(var0, var1, var2.swapMode, var3);
   }

   public static boolean method534(Module var0, int var1, SwapMode var2, int var3) {
      if (!OffHand.INSTANCE.isEnabled() || OffHand.INSTANCE.ab == null || var3 != 45 && var3 != OffHand.INSTANCE.ac) {
         if (field1250 != -1) {
            if (field1249 != null && field1249.isEnabled() && field1250 >= var1) {
               return false;
            }

            if (field1251 != SlotSwapMode.Normal || var2 != SwapMode.Normal && var2 != SwapMode.Silent) {
               method2142();
            }
         }

         if (var2 != SwapMode.Normal && var3 != mc.player.getInventory().selectedSlot) {
            return method535(var0, var1, var2.field8, var3);
         } else if (var3 >= 0) {
            mc.player.getInventory().selectedSlot = var3;
            Class2839.field111 = var3;
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean method535(Module param0, int param1, SlotSwapMode param2, int param3) {
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
      // 00: aload 2
      // 01: invokevirtual dev/boze/client/enums/SlotSwapMode.ordinal ()I
      // 04: lookupswitch 28 2 0 36 1 45
      // 20: new java/lang/IncompatibleClassChangeError
      // 23: dup
      // 24: invokespecial java/lang/IncompatibleClassChangeError.<init> ()V
      // 27: athrow
      // 28: aload 0
      // 29: iload 1
      // 2a: iload 3
      // 2b: invokestatic dev/boze/client/utils/InventoryUtil.method536 (Ldev/boze/client/systems/modules/Module;II)Z
      // 2e: goto 37
      // 31: aload 0
      // 32: iload 1
      // 33: iload 3
      // 34: invokestatic dev/boze/client/utils/InventoryUtil.method537 (Ldev/boze/client/systems/modules/Module;II)Z
      // 37: ireturn
   }

   public static void method396(Module var0) {
      if (var0 == field1249) {
         method1416();
      }
   }

   private static void method2142() {
      if (field1250 != -1) {
         method1416();
      }
   }

   private static void method1416() {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: getstatic dev/boze/client/utils/InventoryUtil.field1251 Ldev/boze/client/enums/SlotSwapMode;
      // 03: ifnonnull 07
      // 06: return
      // 07: getstatic dev/boze/client/utils/InventoryUtil.field1251 Ldev/boze/client/enums/SlotSwapMode;
      // 0a: invokevirtual dev/boze/client/enums/SlotSwapMode.ordinal ()I
      // 0d: lookupswitch 36 2 0 27 1 33
      // 28: invokestatic dev/boze/client/utils/InventoryUtil.method1198 ()V
      // 2b: goto 31
      // 2e: invokestatic dev/boze/client/utils/InventoryUtil.method1904 ()V
      // 31: aconst_null
      // 32: putstatic dev/boze/client/utils/InventoryUtil.field1249 Ldev/boze/client/systems/modules/Module;
      // 35: bipush -1
      // 36: putstatic dev/boze/client/utils/InventoryUtil.field1250 I
      // 39: aconst_null
      // 3a: putstatic dev/boze/client/utils/InventoryUtil.field1251 Ldev/boze/client/enums/SlotSwapMode;
      // 3d: bipush -1
      // 3e: putstatic dev/boze/client/utils/InventoryUtil.field1252 I
      // 41: return
   }

   private static boolean method536(Module var0, int var1, int var2) {
      if (var2 >= 0) {
         field1253 = mc.player.getInventory().selectedSlot;
         field1249 = var0;
         field1250 = var1;
         field1252 = var2;
         field1251 = SlotSwapMode.Normal;
         mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(var2));
         return true;
      } else {
         return false;
      }
   }

   private static void method1198() {
      if (field1253 >= 0) {
         Class2839.field111 = mc.player.getInventory().selectedSlot;
         if (AntiCheat.INSTANCE.field2320.method419()) {
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
         }

         field1253 = -1;
      }
   }

   private static boolean method537(Module var0, int var1, int var2) {
      if (var2 >= 0) {
         field1249 = var0;
         field1250 = var1;
         field1252 = var2;
         field1251 = SlotSwapMode.Alt;
         ScreenHandler var6 = mc.player.currentScreenHandler;
         Int2ObjectArrayMap var7 = new Int2ObjectArrayMap();
         int var8 = ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).getLastSelectedSlot();
         ItemStack var9 = var6.getSlot(var2).getStack();
         ItemStack var10 = var6.getSlot(var8).getStack();
         var7.put(var2, var9);
         if (var2 > 8) {
            ItemTracker.method1964(var2, var8);
            mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(var6.syncId, var6.getRevision(), var2, var8, SlotActionType.SWAP, var9, var7));
         } else {
            int var11 = 36 + var8;
            ItemTracker.method1964(var11, var2);
            mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(var6.syncId, var6.getRevision(), var11, var2, SlotActionType.SWAP, var9, var7));
         }

         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
         field1254 = new int[]{var2, var8};
         return true;
      } else {
         return false;
      }
   }

   private static void method1904() {
      ItemTracker.method2142();
      if (field1254 != null) {
         ScreenHandler var3 = mc.player.currentScreenHandler;
         Int2ObjectArrayMap var4 = new Int2ObjectArrayMap();
         ItemStack var5 = var3.getSlot(field1254[0]).getStack();
         ItemStack var6 = var3.getSlot(field1254[1]).getStack();
         if (field1254[0] > 8) {
            var4.put(field1254[1], var6);
            mc.getNetworkHandler()
               .sendPacket(new ClickSlotC2SPacket(var3.syncId, var3.getRevision(), field1254[0], field1254[1], SlotActionType.SWAP, var6.copy(), var4));
         } else {
            var4.put(field1254[0], var5);
            int var7 = 36 + field1254[1];
            mc.getNetworkHandler()
               .sendPacket(new ClickSlotC2SPacket(var3.syncId, var3.getRevision(), var7, field1254[0], SlotActionType.SWAP, var5.copy(), var4));
         }

         field1254 = null;
      }
   }

   public static boolean method159(int var0) {
      return field1254 == null || field1254[0] != var0 && field1254[1] != var0
         ? OffHand.INSTANCE.isEnabled() && OffHand.INSTANCE.ab != null && (var0 == 45 || var0 == OffHand.INSTANCE.ac)
         : true;
   }
}
