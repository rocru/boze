package dev.boze.client.utils;

import dev.boze.client.enums.AutoMineSwapMode;
import dev.boze.client.enums.SlotSwapMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.combat.OffHand;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.trackers.ItemTracker;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import mapped.Class2839;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtil
        implements IMinecraft {
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
        if (field1250 != -1 && field1252 != -1) {
            return InventoryUtil.mc.player.getInventory().getStack(field1252);
        }
        return InventoryUtil.mc.player.getMainHandStack();
    }

    public static Module method532() {
        return field1249;
    }

    public static void method1649(int n) {
        if (field1250 == -1) {
            return;
        }
        switch (field1251.ordinal()) {
            case 0: {
                if (n == field1252) break;
                field1249 = null;
                field1250 = -1;
                field1251 = null;
                field1252 = -1;
                break;
            }
            case 1: {
                if (field1254 == null || n == field1254[1]) break;
                InventoryUtil.method1904();
            }
        }
    }

    public static boolean method533(Module module, int n, AutoMineSwapMode autoMineSwapMode, int n2) {
        if (autoMineSwapMode == AutoMineSwapMode.Off) {
            return false;
        }
        return InventoryUtil.method534(module, n, autoMineSwapMode.swapMode, n2);
    }

    public static boolean method534(Module module, int n, SwapMode swapMode, int n2) {
        if (OffHand.INSTANCE.isEnabled() && OffHand.INSTANCE.ab != null && (n2 == 45 || n2 == OffHand.INSTANCE.ac)) {
            return false;
        }
        if (field1250 != -1) {
            if (field1249 == null || !field1249.isEnabled() || field1250 < n) {
                if (field1251 != SlotSwapMode.Normal || swapMode != SwapMode.Normal && swapMode != SwapMode.Silent) {
                    InventoryUtil.method2142();
                }
            } else {
                return false;
            }
        }
        if (swapMode == SwapMode.Normal || n2 == InventoryUtil.mc.player.getInventory().selectedSlot) {
            if (n2 >= 0) {
                InventoryUtil.mc.player.getInventory().selectedSlot = n2;
                Class2839.field111 = n2;
                ((ClientPlayerInteractionManagerAccessor)InventoryUtil.mc.interactionManager).callSyncSelectedSlot();
                return true;
            }
            return false;
        }
        return InventoryUtil.method535(module, n, swapMode.field8, n2);
    }

    private static boolean method535(Module module, int n, SlotSwapMode slotSwapMode, int n2) {
        return switch (slotSwapMode.ordinal()) {
            default -> throw new IncompatibleClassChangeError();
            case 0 -> InventoryUtil.method536(module, n, n2);
            case 1 -> InventoryUtil.method537(module, n, n2);
        };
    }

    public static void method396(Module module) {
        if (module != field1249) {
            return;
        }
        InventoryUtil.method1416();
    }

    private static void method2142() {
        if (field1250 == -1) {
            return;
        }
        InventoryUtil.method1416();
    }

    private static void method1416() {
        if (field1251 == null) {
            return;
        }
        switch (field1251.ordinal()) {
            case 0: {
                InventoryUtil.method1198();
                break;
            }
            case 1: {
                InventoryUtil.method1904();
            }
        }
        field1249 = null;
        field1250 = -1;
        field1251 = null;
        field1252 = -1;
    }

    private static boolean method536(Module module, int n, int n2) {
        if (n2 >= 0) {
            field1253 = InventoryUtil.mc.player.getInventory().selectedSlot;
            field1249 = module;
            field1250 = n;
            field1252 = n2;
            field1251 = SlotSwapMode.Normal;
            mc.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n2));
            return true;
        }
        return false;
    }

    private static void method1198() {
        if (field1253 >= 0) {
            Class2839.field111 = InventoryUtil.mc.player.getInventory().selectedSlot;
            if (AntiCheat.INSTANCE.field2320.method419().booleanValue()) {
                ((ClientPlayerInteractionManagerAccessor)InventoryUtil.mc.interactionManager).callSyncSelectedSlot();
            }
            field1253 = -1;
        }
    }

    private static boolean method537(Module module, int n, int n2) {
        if (n2 >= 0) {
            field1249 = module;
            field1250 = n;
            field1252 = n2;
            field1251 = SlotSwapMode.Alt;
            ScreenHandler screenHandler = InventoryUtil.mc.player.currentScreenHandler;
            Int2ObjectArrayMap<ItemStack> int2ObjectArrayMap = new Int2ObjectArrayMap<ItemStack>();
            int n3 = ((ClientPlayerInteractionManagerAccessor)InventoryUtil.mc.interactionManager).getLastSelectedSlot();
            ItemStack itemStack = screenHandler.getSlot(n2).getStack();
            ItemStack itemStack2 = screenHandler.getSlot(n3).getStack();
            int2ObjectArrayMap.put(n2, itemStack);
            if (n2 > 8) {
                int n4 = n2;
                int n5 = n3;
                ItemTracker.method1964(n4, n5);
                mc.getNetworkHandler().sendPacket((Packet)new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), n4, n5, SlotActionType.SWAP, itemStack, int2ObjectArrayMap));
            } else {
                int n6 = 36 + n3;
                int n7 = n2;
                ItemTracker.method1964(n6, n7);
                mc.getNetworkHandler().sendPacket((Packet)new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), n6, n7, SlotActionType.SWAP, itemStack, int2ObjectArrayMap));
            }
            ((ClientPlayerInteractionManagerAccessor)InventoryUtil.mc.interactionManager).callSyncSelectedSlot();
            field1254 = new int[]{n2, n3};
            return true;
        }
        return false;
    }

    private static void method1904() {
        ItemTracker.method2142();
        if (field1254 != null) {
            ScreenHandler screenHandler = InventoryUtil.mc.player.currentScreenHandler;
            Int2ObjectArrayMap<ItemStack> int2ObjectArrayMap = new Int2ObjectArrayMap<ItemStack>();
            ItemStack itemStack = screenHandler.getSlot(field1254[0]).getStack();
            ItemStack itemStack2 = screenHandler.getSlot(field1254[1]).getStack();
            if (field1254[0] > 8) {
                int2ObjectArrayMap.put(field1254[1], itemStack2);
                mc.getNetworkHandler().sendPacket((Packet)new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), field1254[0], field1254[1], SlotActionType.SWAP, itemStack2.copy(), int2ObjectArrayMap));
            } else {
                int2ObjectArrayMap.put(field1254[0], itemStack);
                int n = 36 + field1254[1];
                mc.getNetworkHandler().sendPacket((Packet)new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), n, field1254[0], SlotActionType.SWAP, itemStack.copy(), int2ObjectArrayMap));
            }
            field1254 = null;
        }
    }

    public static boolean method159(int n) {
        if (field1254 != null && (field1254[0] == n || field1254[1] == n)) {
            return true;
        }
        return OffHand.INSTANCE.isEnabled() && OffHand.INSTANCE.ab != null && (n == 45 || n == OffHand.INSTANCE.ac);
    }
}
