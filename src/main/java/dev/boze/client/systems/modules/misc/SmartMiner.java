package dev.boze.client.systems.modules.misc;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.process.IMineProcess;
import dev.boze.client.enums.SmartMineInventoryMode;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.StringModeSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.ItemEnchantmentUtils;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class2839;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class SmartMiner extends Module {
    public static final SmartMiner INSTANCE = new SmartMiner();
    private final StringModeSetting field475 = new StringModeSetting("Blocks", "Blocks to mine");
    private final EnumSetting<SmartMineInventoryMode> field476 = new EnumSetting<SmartMineInventoryMode>(
            "WhenDone", SmartMineInventoryMode.Logout, "What to do when inventory is full"
    );
    private final BooleanSetting field477 = new BooleanSetting("Repair", true, "Automatically mend tools");
    private final IntSetting field478 = new IntSetting("Durability", 30, 1, 90, 1, "Durability at which to start repairing tools", this.field477);
    private final BooleanSetting field479 = new BooleanSetting("Coal", true, "Mine coal ore to mend tools", this.field477);
    private final BooleanSetting field480 = new BooleanSetting("Redstone", true, "Mine redstone ore to mend tools", this.field477);
    private final BooleanSetting field481 = new BooleanSetting("NetherQuartz", true, "Mine quartz ore to mend tools", this.field477);
    private final IBaritone field482 = BaritoneAPI.getProvider().getPrimaryBaritone();
    private final Settings field483 = BaritoneAPI.getSettings();
    private BlockPos field484 = null;
    private boolean field485;
    private boolean field486;
    private final HashMap<Block, String> field487 = new HashMap();

    public SmartMiner() {
        super("SmartMiner", "Automatically mines and mends tools", Category.Misc);
        this.field475.field951 = false;
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            this.setEnabled(false);
        } else {
            this.field485 = this.field483.mineScanDroppedItems.value;
            this.field483.mineScanDroppedItems.value = true;
            this.field484 = mc.player.getBlockPos();
            this.field486 = false;
        }
    }

    @Override
    public void onDisable() {
        this.field482.getPathingBehavior().cancelEverything();
        this.field483.mineScanDroppedItems.value = this.field485;
    }

    @EventHandler
    private void method1810(PostTickEvent postTickEvent) {
        if (!MinecraftUtils.isClientActive()) {
            return;
        }
        if (this.method1975()) {
            switch (this.field476.getValue().ordinal()) {
                case 1: {
                    ChatInstance.method740(this.getName(), "Inventory full, done mining");
                    this.setEnabled(false);
                    break;
                }
                case 0: {
                    ChatInstance.method740(this.getName(), "Inventory full, logging out");
                    SmartMiner.mc.player.networkHandler.sendPacket(new DisconnectS2CPacket(Text.literal("[SmartMiner] Inventory full, logged out out")));
                    this.setEnabled(false);
                    break;
                }
                case 2:
                case 3: {
                    if (this.method1973() && this.field484 != null) {
                        ChatInstance.method740(this.getName(), "Returning to start position");
                        this.field482.getCustomGoalProcess().setGoalAndPath(new GoalBlock(this.field484));
                    }
                    if (!SmartMiner.mc.player.getBlockPos().equals(this.field484)) break;
                    ChatInstance.method740(this.getName(), "Returned to start position");
                    this.setEnabled(false);
                    if (this.field476.getValue() != SmartMineInventoryMode.ReturnLog) break;
                    ChatInstance.method740(this.getName(), "Returned to start position, logging out");
                    SmartMiner.mc.player.networkHandler.sendPacket(new DisconnectS2CPacket(Text.literal("[SmartMiner] Returned to start position, logged out")));
                }
            }
            return;
        }
        if (!this.method1971()) {
            ChatInstance.method625("No pickaxe found in hotbar, disabling");
            this.setEnabled(false);
            return;
        }
        if (this.field486) {
            if (!this.method1972()) {
                ChatInstance.method624("Done repairing, continuing to mine");
                this.field486 = false;
                this.method1904();
                return;
            }
            if (this.method1974()) {
                this.method1854();
            }
        } else {
            if (this.field477.getValue() && this.method1972()) {
                ChatInstance.method624("Repairing");
                this.field486 = true;
                this.method1854();
                return;
            }
            if (this.method1974()) {
                this.method1904();
            }
        }
    }

    private boolean method1971() {
        Predicate<ItemStack> var4 = this::lambda$findPickaxe$0;
        int var5 = InventoryHelper.method168(var4);
        if (var5 != -1) {
            if (var5 != mc.player.getInventory().selectedSlot) {
                Class2839.field111 = var5;
                mc.player.getInventory().selectedSlot = var5;
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).callSyncSelectedSlot();
            }

            return true;
        } else {
            return false;
        }
    }

    private void method1904() {
        Block[] var1 = new Block[this.field475.method2032().size()];
        this.field482.getPathingBehavior().cancelEverything();
        this.field482.getMineProcess().mine(this.field475.method2032().toArray(var1));
    }

    private void method1854() {
        ArrayList var4 = new ArrayList();
        if (this.field479.getValue()) {
            var4.add(Blocks.COAL_ORE);
        }

        if (this.field480.getValue()) {
            var4.add(Blocks.REDSTONE_ORE);
        }

        if (this.field481.getValue()) {
            var4.add(Blocks.NETHER_QUARTZ_ORE);
        }

        Block[] var5 = new Block[var4.size()];
        this.field482.getPathingBehavior().cancelEverything();
        this.field482.getMineProcess().mine((Block[]) var4.toArray(var5));
    }

    private boolean method1972() {
        ItemStack var4 = mc.player.getMainHandStack();
        double var5 = (float) (var4.getMaxDamage() - var4.getDamage()) * 100.0F / (float) var4.getMaxDamage();
        return !(var5 > 95.0) && (!(var5 > (double) this.field478.getValue().intValue()) || this.field486);
    }

    private boolean method1973() {
        return !(this.field482.getPathingControlManager().mostRecentInControl().orElse(null) instanceof ICustomGoalProcess);
    }

    private boolean method1974() {
        return !(this.field482.getPathingControlManager().mostRecentInControl().orElse(null) instanceof IMineProcess);
    }

    private boolean method1975() {
        StringBuilder v4 = new StringBuilder();

        for (Block v6 : this.field475.method2032()) {
            v4.append(this.method259(v6));
        }

        String var9 = v4.toString();

        for (int i6 = 0; i6 <= 35; i6++) {
            ItemStack v7 = mc.player.getInventory().getStack(i6);
            if (v7.isEmpty()) {
                return false;
            }

            if (v7.getCount() < v7.getMaxCount() && var9.contains(v7.getItem().getRegistryEntry().getKey().get().getValue().getPath())) {
                return false;
            }
        }

        return true;
    }

    private String method259(Block var1) {
        if (this.field487.containsKey(var1)) {
            return this.field487.get(var1);
        } else {
            try {
                Identifier var5 = Registries.BLOCK.getId(var1);
                String var6 = "data/minecraft/loot_tables/blocks/" + var5.getPath() + ".json";
                InputStream var7 = MinecraftClient.class.getClassLoader().getResourceAsStream(var6);
                BufferedReader var8 = new BufferedReader(new InputStreamReader(var7));
                StringBuilder var9 = new StringBuilder();

                while (var8.ready()) {
                    String var10 = var8.readLine();
                    if (var10.contains("\"name\"")) {
                        var9.append(var10, var10.lastIndexOf(":") + 1, var10.lastIndexOf("\"")).append(":");
                    }
                }

                String var12 = var9.toString();
                this.field487.put(var1, var12);
                return var12;
            } catch (Exception var11) {
                return "";
            }
        }
    }

    private boolean lambda$findPickaxe$0(ItemStack var1) {
        return var1.getItem() instanceof PickaxeItem
                && (
                !this.field477.getValue()
                        || ItemEnchantmentUtils.hasEnchantment(var1, Enchantments.MENDING) && !ItemEnchantmentUtils.hasEnchantment(var1, Enchantments.SILK_TOUCH)
        );
    }
}
