package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.Server;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.mixin.HandledScreenAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.ItemEnchantmentUtils;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.player.SlotUtils;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ChestStealer extends Module {
    public static final ChestStealer INSTANCE = new ChestStealer();
    private final BooleanSetting field2760 = new BooleanSetting("Smart", false, "Steal only items that are better than yours");
    private final BooleanSetting field2761 = new BooleanSetting("Durability", false, "Prioritize items with higher durability", this.field2760);
    private final MinMaxSetting field2762 = new MinMaxSetting("SuccessRate", 1.0, 0.0, 1.0, 0.01, "Chance of stealing an item");
    private final MinMaxDoubleSetting field2763 = new MinMaxDoubleSetting("Delay", new double[]{2.0, 5.0}, 0.0, 20.0, 0.01, "Delay between stealing items");
    private final MinMaxSetting field2764 = new MinMaxSetting("RandomDelay", 0.25, 0.0, 1.0, 0.01, "Random stealing delay");
    private final MinMaxSetting field2765 = new MinMaxSetting("InitialDelay", 2.0, 0.0, 20.0, 0.01, "Initial delay before stealing items");
    private final MinMaxSetting field2766 = new MinMaxSetting("FinalDelay", 2.0, 0.0, 20.0, 0.01, "Delay after stealing before closing the chest");
    private final Timer field2767 = new Timer();
    private boolean field2768 = true;
    private Vec2f field2769 = null;
    private long field2770 = 0L;

    public ChestStealer() {
        super("ChestStealer", "Steals items from chests", Category.Legit);
    }

    public static boolean method1591(Slot slot, List<Slot> slotList, boolean considerDurability) {
        ArrayList<Slot> arrayList = new ArrayList<Slot>(slotList);
        arrayList.remove(slot);
        arrayList.removeIf(arg_0 -> ChestStealer.lambda$hasBetterEquivalent$2(slot, arg_0));
        List<ItemStack> list = arrayList.stream().map(Slot::getStack).distinct().filter(arg_0 -> ChestStealer.lambda$hasBetterEquivalent$3(slot, arg_0)).collect(Collectors.toList());
        List<Slot> list2 = SlotUtils.method664(ChestStealer.mc.player != null ? ChestStealer.mc.player.playerScreenHandler : null).stream().filter(Slot::hasStack).collect(Collectors.toList());
        ArrayList<ItemStack> arrayList2 = new ArrayList<ItemStack>();
        arrayList2.addAll(list);
        arrayList2.addAll(list2.stream().map(Slot::getStack).toList());
        return ChestStealer.method1595(slot.getStack(), arrayList2, considerDurability);
    }

    private static boolean method1592(Slot slot) {
        PlayerScreenHandler playerScreenHandler;
        PlayerScreenHandler playerScreenHandler2 = playerScreenHandler = ChestStealer.mc.player != null ? ChestStealer.mc.player.playerScreenHandler : null;
        if (playerScreenHandler == null) {
            return false;
        }
        return playerScreenHandler.slots.stream().filter(Slot::isEnabled).anyMatch(arg_0 -> ChestStealer.lambda$canTransfer$4(slot, arg_0));
    }

    private static float method1593(ItemStack var0) {
        if (var0.getItem() instanceof SwordItem) {
            return ((SwordItem) var0.getItem()).getMaterial().getAttackDamage();
        } else if (var0.getItem() instanceof ToolItem) {
            return (float) ((ToolItem) var0.getItem()).getMaterial().getDurability();
        } else if (var0.getItem() instanceof ArmorItem var4) {
            return (float) var4.getProtection() * (1.0F + var4.getMaterial().value().knockbackResistance());
        } else {
            return 0.0F;
        }
    }

    private static boolean method1594(ItemStack var0, ItemStack var1) {
        if (var0.getItem() instanceof SwordItem && var1.getItem() instanceof SwordItem) {
            return true;
        } else if (var0.getItem() instanceof AxeItem && var1.getItem() instanceof AxeItem) {
            return true;
        } else if (var0.getItem() instanceof PickaxeItem && var1.getItem() instanceof PickaxeItem) {
            return true;
        } else if (var0.getItem() instanceof HoeItem && var1.getItem() instanceof HoeItem) {
            return true;
        } else if (var0.getItem() instanceof ShovelItem && var1.getItem() instanceof ShovelItem) {
            return true;
        } else if (var0.getItem() instanceof ArmorItem var5 && var1.getItem() instanceof ArmorItem var6) {
            return var5.getSlotType() == var6.getSlotType();
        } else {
            return var0.getItem() == var1.getItem();
        }
    }

    public static boolean method1595(ItemStack stack, List<ItemStack> list, boolean considerDurability) {
        if (stack.getItem().getEnchantability() == 0) {
            return false;
        }
        float f = ChestStealer.method1593(stack);
        Object2IntArrayMap<RegistryEntry<Enchantment>> object2IntArrayMap = new Object2IntArrayMap<RegistryEntry<Enchantment>>();
        ItemEnchantmentUtils.populateEnchantments(stack, object2IntArrayMap);
        for (ItemStack itemStack : list) {
            boolean bl;
            float f2;
            boolean bl2;
            if (!ChestStealer.method1594(stack, itemStack) || considerDurability && itemStack.getDamage() > stack.getDamage() || !(bl2 = (f2 = ChestStealer.method1593(itemStack)) > f))
                continue;
            Object2IntMap<RegistryEntry<Enchantment>> object2IntMap = ItemEnchantmentUtils.getEnchantments(itemStack);
            boolean bl3 = bl = object2IntArrayMap.isEmpty() && !object2IntMap.isEmpty();
            if (bl) {
                return true;
            }
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            object2IntArrayMap.forEach((arg_0, arg_1) -> ChestStealer.lambda$hasBetterEquivalent$5(object2IntMap, atomicBoolean, arg_0, arg_1));
            if (!atomicBoolean.get()) continue;
            return true;
        }
        return false;
    }

    private static void lambda$hasBetterEquivalent$5(Object2IntMap var0, AtomicBoolean var1, RegistryEntry var2, Integer var3) {
        if (var0.containsKey(var2) && var0.getInt(var2) < var3) {
            var1.set(true);
        }
    }

    private static boolean lambda$canTransfer$4(Slot var0, Slot var1) {
        return !var1.hasStack()
                || ItemStack.areItemsAndComponentsEqual(var1.getStack(), var0.getStack()) && var1.getStack().getCount() < var1.getStack().getMaxCount();
    }

    private static boolean lambda$hasBetterEquivalent$3(Slot var0, ItemStack var1) {
        return var1 != var0.getStack();
    }

    private static boolean lambda$hasBetterEquivalent$2(Slot var0, Slot var1) {
        return ItemStack.areItemsAndComponentsEqual(var0.getStack(), var1.getStack());
    }

    private static boolean lambda$onScreenInput$1(Slot var0, List var1) {
        return !var0.hasStack();
    }

    @Override
    public void onEnable() {
        this.field2768 = true;
        this.field2769 = null;
        this.field2770 = 0L;
    }

    @EventHandler
    public void method1590(FlipFrameEvent event) {
        try {
            if (event.method1022()) {
                return;
            }
            if (Server.method539().method538().method2116()) {
                return;
            }
            if (ChestStealer.mc.currentScreen instanceof DownloadingTerrainScreen) {
                return;
            }
            if (!(ChestStealer.mc.currentScreen instanceof GenericContainerScreen genericContainerScreen) || !(ChestStealer.mc.currentScreen instanceof HandledScreen)) {
                this.field2767.reset();
                this.field2768 = true;
                this.field2769 = null;
                return;
            }
            if (!(ChestStealer.mc.currentScreen instanceof HandledScreen)) {
                return;
            }
            HandledScreenAccessor handledScreenAccessor = (HandledScreenAccessor) ChestStealer.mc.currentScreen;
            GenericContainerScreenHandler genericContainerScreenHandler = genericContainerScreen.getScreenHandler();
            if (genericContainerScreenHandler != null && !genericContainerScreenHandler.getCursorStack().isEmpty()) {
                return;
            }
            if (this.field2769 == null) {
                this.field2769 = new Vec2f((float) mc.getWindow().getScaledWidth() / 2.0f, (float) mc.getWindow().getScaledHeight() / 2.0f);
            }
            Slot slot = SlotUtils.method665(genericContainerScreenHandler, handledScreenAccessor, this.field2769, (arg_0, arg_1) -> this.lambda$onScreenInput$0(genericContainerScreenHandler, arg_0, arg_1));
            if (!this.field2767.hasElapsed(this.field2768 ? this.field2765.getValue() * 50.0 : (slot == null ? this.field2766.getValue() * 50.0 : (double) this.field2770))) {
                return;
            }
            this.field2768 = false;
            this.field2767.reset();
            if (slot == null) {
                ChestStealer.mc.currentScreen.close();
            } else {
                Vec2f vec2f;
                Slot slot2;
                if (ChestStealer.mc.player.getInventory().getEmptySlot() == -1) {
                    ChestStealer.mc.currentScreen.close();
                    return;
                }
                Vec2f vec2f2 = SlotUtils.method663(handledScreenAccessor, slot).add(new Vec2f(this.field2764.getValue() == 0.0 ? 0.0f : (float) ThreadLocalRandom.current().nextDouble(-this.field2764.getValue().doubleValue() * 30.0, this.field2764.getValue() * 30.0), this.field2764.getValue() == 0.0 ? 0.0f : (float) ThreadLocalRandom.current().nextDouble(-this.field2764.getValue().doubleValue() * 30.0, this.field2764.getValue() * 30.0)));
                if ((double) ThreadLocalRandom.current().nextInt(100) > this.field2762.getValue() * 100.0 && (slot2 = SlotUtils.method665(genericContainerScreenHandler, handledScreenAccessor, vec2f = this.field2769.add(vec2f2.add(this.field2769.negate()).multiply((float) ThreadLocalRandom.current().nextDouble(0.0, 1.0))), ChestStealer::lambda$onScreenInput$1)) != null) {
                    slot = slot2;
                }
                float f = this.field2769.distanceSquared(vec2f2);
                this.field2769 = vec2f2;
                float f2 = (float) Math.sqrt(f) / new Vec2f((float) handledScreenAccessor.getBackgroundWidth(), (float) handledScreenAccessor.getBackgroundHeight()).length();
                this.field2770 = (long) (this.field2763.method1296() * (double) f2 * 50.0);
                ChestStealer.mc.interactionManager.clickSlot(genericContainerScreenHandler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, ChestStealer.mc.player);
                event.method1021(true);
            }
        } catch (Exception exception) {
            // empty catch block
        }
    }

    private boolean lambda$onScreenInput$0(GenericContainerScreenHandler var1, Slot var2, List var3) {
        return var2.hasStack()
                && var2.id < var1.getInventory().size()
                && (!this.field2760.getValue() || !method1591(var2, var3, this.field2761.getValue()))
                && method1592(var2);
    }
}
