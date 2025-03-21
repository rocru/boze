package dev.boze.client.systems.modules.misc;

import dev.boze.client.core.BozeLogger;
import dev.boze.client.enums.ReplenishMode;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.mixin.ItemStackAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.FoodUtil;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.InventoryUtil;
import dev.boze.client.utils.player.SlotUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Replenish extends Module {
    public static final Replenish INSTANCE = new Replenish();
    private final EnumSetting<ReplenishMode> field3059 = new EnumSetting<ReplenishMode>(
            "Mode", ReplenishMode.Anarchy, "Mode for replenish", Replenish::lambda$new$0
    );
    private final IntSetting field3060 = new IntSetting("Threshold", 16, 1, 63, 1, "Threshold for refilling items", this::lambda$new$1);
    private final BooleanSetting field3061 = new BooleanSetting("AwaitInventory", true, "Await open inventory before replenishing", this::lambda$new$2);
    private final MinMaxDoubleSetting field3062 = new MinMaxDoubleSetting(
            "Delay", new double[]{2.0, 5.0}, 0.0, 20.0, 0.01, "Delay between replenishing slots", this::lambda$new$3
    );
    private final MinMaxSetting field3063 = new MinMaxSetting("RandomDelay", 0.25, 0.0, 1.0, 0.01, "Random replenishing delay", this::lambda$new$4);
    private final MinMaxSetting field3064 = new MinMaxSetting(
            "InitialDelay", 2.0, 0.0, 20.0, 0.01, "Initial delay before replenishing slots", this::lambda$new$5
    );
    private final BooleanSetting field3065 = new BooleanSetting("Mainhand", true, "Replenish mainhand");
    private final BooleanSetting field3066 = new BooleanSetting("Offhand", false, "Replenish offhand", this::lambda$new$6);
    private final BooleanSetting field3067 = new BooleanSetting("Other", false, "Replenish other hotbar slots");
    private final BooleanSetting field3068 = new BooleanSetting("Crystals", true, "Replenish crystal stacks");
    private final BooleanSetting field3069 = new BooleanSetting("Food", true, "Replenish food stacks");
    private final BooleanSetting field3070 = new BooleanSetting("XP", true, "Replenish XP stacks");
    private final BooleanSetting field3071 = new BooleanSetting("Totems", false, "Replenish totems");
    private final BooleanSetting field3072 = new BooleanSetting("Stackable", true, "Replenish all other stackable stacks");
    private final BooleanSetting field3073 = new BooleanSetting("UnStackable", false, "Replenish all other unstackable stacks");
    private final ItemStack[] field3074 = new ItemStack[10];
    private boolean field3075 = false;
    private boolean field3076 = false;
    private final HashMap<Integer, Long> field3077 = new HashMap();
    private final dev.boze.client.utils.Timer field3078 = new dev.boze.client.utils.Timer();
    private boolean field3079 = true;
    private Vec2f field3080 = null;
    private long field3081 = 0L;
    public boolean field3082 = false;

    private static void method1750(String var0) {
        if (mc.player != null) {
            BozeLogger.method529(INSTANCE, var0);
        }
    }

    private ReplenishMode method1751() {
        return Options.INSTANCE.method1971() ? ReplenishMode.Ghost : this.field3059.getValue();
    }

    public Replenish() {
        super("Replenish", "Automatically refills stacks in your hotbar", Category.Misc);

        for (int var3 = 0; var3 < this.field3074.length; var3++) {
            this.field3074[var3] = new ItemStack(Items.AIR);
        }

        this.field435 = true;
    }

    @Override
    public void onEnable() {
        if (MinecraftUtils.isClientActive()) {
            this.method1759();
            this.field3076 = mc.currentScreen != null;
            this.field3078.reset();
            this.field3079 = true;
            this.field3080 = null;
            this.field3081 = 0L;
            this.field3082 = false;
        }
    }

    @EventHandler
    public void method1752(FlipFrameEvent event) {
        if (MinecraftUtils.isClientActive() && this.method1751() != ReplenishMode.Anarchy) {
            if (!event.method1022()) {
                if (!(mc.currentScreen instanceof DownloadingTerrainScreen)) {
                    if (!(mc.currentScreen instanceof AbstractInventoryScreen) || !(mc.currentScreen instanceof HandledScreen)) {
                        this.method1759();
                        if (this.field3061.getValue()) {
                            this.field3078.reset();
                            this.field3079 = true;
                            this.field3080 = null;
                            return;
                        }
                    } else if (this.field3082) {
                        this.method1759();
                    }

                    this.field3082 = false;
                    PlayerScreenHandler var5 = mc.player != null ? mc.player.playerScreenHandler : null;
                    if (var5 == null || var5.getCursorStack().isEmpty()) {
                        if (this.field3080 == null) {
                            this.field3080 = new Vec2f((float) mc.getWindow().getScaledWidth() / 2.0F, (float) mc.getWindow().getScaledHeight() / 2.0F);
                        }

                        Pair var6 = this.method1754();
                        if (this.field3078.hasElapsed(this.field3079 && !this.field3061.getValue() ? this.field3064.getValue() * 50.0 : (double) this.field3081)) {
                            this.field3079 = false;
                            this.field3078.reset();
                            if (var6 != null) {
                                Vec2f var7 = SlotUtils.method663(null, (Slot) var6.getLeft())
                                        .add(
                                                new Vec2f(
                                                        this.field3063.getValue() == 0.0
                                                                ? 0.0F
                                                                : (float) ThreadLocalRandom.current().nextDouble(-this.field3063.getValue() * 30.0, this.field3063.getValue() * 30.0),
                                                        this.field3063.getValue() == 0.0
                                                                ? 0.0F
                                                                : (float) ThreadLocalRandom.current().nextDouble(-this.field3063.getValue() * 30.0, this.field3063.getValue() * 30.0)
                                                )
                                        );
                                float var8 = this.field3080.distanceSquared(var7);
                                this.field3080 = var7;
                                float var9 = (float) Math.sqrt(var8) / new Vec2f(176.0F, 166.0F).length();
                                this.field3081 = (long) (this.field3062.method1296() * (double) var9);
                                mc.interactionManager
                                        .clickSlot(
                                                mc.player.currentScreenHandler.syncId, ((Slot) var6.getLeft()).id, ((Slot) var6.getRight()).id, SlotActionType.SWAP, mc.player
                                        );
                                event.method1021(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1753(PrePlayerTickEvent event) {
        if (this.method1751() != ReplenishMode.Ghost) {
            if (mc.currentScreen == null && this.field3076) {
                this.method1759();
            }

            this.field3076 = mc.currentScreen != null;
            if (mc.player.currentScreenHandler.getStacks().size() == 46 && mc.currentScreen == null) {
                this.field3075 = false;
                if (this.field3065.getValue()) {
                    ItemStack var5 = mc.player.getMainHandStack();
                    this.method1755(mc.player.getInventory().selectedSlot, var5);
                }

                if (this.field3066.getValue()) {
                    ItemStack var7 = mc.player.getOffHandStack();
                    this.method1755(45, var7);
                }

                if (this.field3067.getValue()) {
                    for (int var8 = 0; var8 < 9; var8++) {
                        if (var8 != mc.player.getInventory().selectedSlot) {
                            ItemStack var6 = mc.player.getInventory().getStack(var8);
                            this.method1755(var8, var6);
                        }
                    }
                }

                if (this.field3075 && AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                    mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                }
            }
        }
    }

    private Pair<Slot, Slot> method1754() {
        for (int var4 = 0; var4 < 9; var4++) {
            if (var4 == mc.player.getInventory().selectedSlot ? this.field3065.getValue() : this.field3067.getValue()) {
                ItemStack var5 = this.method1760(var4);
                ItemStack var6 = mc.player.getInventory().getStack(var4);
                if (var6.isEmpty() && this.method1756(var5)) {
                    int var7 = this.method1757(var5, var4, 1);
                    if (var7 != -1) {
                        return new Pair(mc.player.currentScreenHandler.getSlot(var7), mc.player.currentScreenHandler.getSlot(var4));
                    }

                    this.method1761(var4, ItemStack.EMPTY);
                }
            }
        }

        return null;
    }

    private void method1755(int var1, ItemStack var2) {
        if (var1 != -1 && !dev.boze.client.utils.InventoryUtil.method159(var1)) {
            if (System.currentTimeMillis() - this.field3077.getOrDefault(var1, 0L) >= 1000L) {
                ItemStack var6 = this.method1760(var1);
                if (this.method1756(var6)) {
                    if (var2.isStackable() && !var2.isEmpty()) {
                        if (var2.getCount() <= Math.min(var2.getItem().getMaxCount() - 1, this.field3060.getValue())) {
                            method1750("Replenishing stackable item " + var2.getItem().getName().getString() + " in slot " + var1);
                            this.method1758(
                                    var1, this.method1757(var2, var1, Math.min(var2.getItem().getMaxCount() - 1, this.field3060.getValue()) - var2.getCount() + 1), false
                            );
                        }
                    } else if (var2.isEmpty() && !var6.isEmpty()) {
                        if (var6.isStackable()) {
                            method1750("Replenishing empty slot " + var1 + " with stackable item " + var6.getItem().getName().getString());
                            this.method1758(
                                    var1, this.method1757(var6, var1, Math.min(var2.getItem().getMaxCount() - 1, this.field3060.getValue()) - var2.getCount() + 1), true
                            );
                        } else {
                            method1750("Replenishing empty slot " + var1 + " with unstackable item " + var6.getItem().getName().getString());
                            this.method1758(var1, this.method1757(var6, var1, 1), true);
                        }
                    }

                    this.method1761(var1, var2);
                }
            }
        }
    }

    private boolean method1756(ItemStack var1) {
        Item var5 = var1.getItem();
        if (FoodUtil.isFood(var1)) {
            return this.field3069.getValue();
        } else if (var5 instanceof EndCrystalItem) {
            return this.field3068.getValue();
        } else if (var5 instanceof ExperienceBottleItem) {
            return this.field3070.getValue();
        } else if (var5 == Items.TOTEM_OF_UNDYING) {
            return this.field3071.getValue();
        } else {
            return var1.isStackable() ? this.field3072.getValue() : this.field3073.getValue();
        }
    }

    private int method1757(ItemStack var1, int var2, int var3) {
        int var7 = -1;
        int var8 = 0;

        for (int var9 = mc.player.getInventory().size() - 2; var9 >= 9; var9--) {
            if (!dev.boze.client.utils.InventoryUtil.method159(var9)) {
                ItemStack var10 = mc.player.getInventory().getStack(var9);
                if (var9 != var2 && var10.getItem() == var1.getItem() && ItemStack.areItemsAndComponentsEqual(var1, var10) && var10.getCount() > var8) {
                    var7 = var9;
                    var8 = var10.getCount();
                    if (var8 >= var3) {
                        break;
                    }
                }
            }
        }

        return var7;
    }

    private void method1758(int var1, int var2, boolean var3) {
        if (var1 != -1 && var2 != -1) {
            if (mc.interactionManager != null) {
                if (var3) {
                    method1750("Swapping slot " + var2 + " to " + var1);
                    mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var2, var1, SlotActionType.SWAP, mc.player);
                } else {
                    method1750("Moving items from slot " + var2 + " to " + var1);
                    InvUtils.method2201().method2207(var2).method2213(var1);
                }

                this.field3077.put(var1, System.currentTimeMillis());
                this.field3075 = true;
            }
        }
    }

    private void method1759() {
        for (int var4 = 0; var4 < 9; var4++) {
            this.method1761(var4, mc.player.getInventory().getStack(var4));
        }

        this.method1761(45, mc.player.getOffHandStack());
    }

    private ItemStack method1760(int var1) {
        if (var1 == 45) {
            var1 = 9;
        }

        return this.field3074[var1];
    }

    private void method1761(int var1, ItemStack var2) {
        if (this.method1751() != ReplenishMode.Ghost || !var2.isEmpty() || this.field3082) {
            ItemStack var6 = this.field3074[9];
            ((ItemStackAccessor) (Object) var6).setItem(var2.getItem());
            var6.setCount(var2.getCount());
            var6.applyComponentsFrom(var2.getComponents());
            if (var2.isEmpty()) {
                ((ItemStackAccessor) (Object) var6).setItem(Items.AIR);
            }
        }
    }

    private boolean lambda$new$6() {
        return this.method1751() == ReplenishMode.Anarchy;
    }

    private boolean lambda$new$5() {
        return this.method1751() == ReplenishMode.Ghost;
    }

    private boolean lambda$new$4() {
        return this.method1751() == ReplenishMode.Ghost;
    }

    private boolean lambda$new$3() {
        return this.method1751() == ReplenishMode.Ghost;
    }

    private boolean lambda$new$2() {
        return this.method1751() == ReplenishMode.Ghost;
    }

    private boolean lambda$new$1() {
        return this.method1751() == ReplenishMode.Anarchy;
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }
}
