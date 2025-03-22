package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.AutoUpgradeMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.player.SlotUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;

public class AutoUpgrade extends Module {
    public static final AutoUpgrade INSTANCE = new AutoUpgrade();
    private final EnumSetting<AutoUpgradeMode> field2911 = new EnumSetting<AutoUpgradeMode>(
            "Mode", AutoUpgradeMode.Anarchy, "Mode for AutoUpgrade", AutoUpgrade::lambda$new$0
    );
    private final MinMaxDoubleSetting field2912 = new MinMaxDoubleSetting(
            "Delay", new double[]{5.0, 7.5}, 0.0, 20.0, 0.1, "Delay in ticks between each click", this::method1685
    );
    private final dev.boze.client.utils.Timer field2913 = new dev.boze.client.utils.Timer();

    private AutoUpgrade() {
        super("AutoUpgrade", "Automatically upgrades all tools/armor in inventory, when you open smithing table\n", Category.Misc);
        this.field435 = true;
    }

    private static boolean lambda$handle$3(ItemStack var0) {
        Item var4 = var0.getItem();
        if (var4 instanceof ToolItem var5) {
            return var5.getMaterial() == ToolMaterials.DIAMOND;
        } else {
            return var4 instanceof ArmorItem var6 && var6.getMaterial() == ArmorMaterials.DIAMOND;
        }
    }

    private static boolean lambda$handle$2(ItemStack var0) {
        return var0.getItem() == Items.NETHERITE_INGOT;
    }

    private static boolean lambda$handle$1(ItemStack var0) {
        return var0.getItem() == Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }

    private boolean method1685() {
        return Options.INSTANCE.method1971() || this.field2911.getValue() == AutoUpgradeMode.Ghost;
    }

    @EventHandler
    public void method1686(FlipFrameEvent event) {
        if (!event.method1022() && this.method1685() && this.field2913.hasElapsed(this.field2912.method1295() * 50.0)) {
            if (this.method1688()) {
                this.field2913.reset();
                this.field2912.method1296();
            }
        }
    }

    @EventHandler
    public void method1687(RotationEvent event) {
        if (!event.method554(RotationMode.Sequential) && !this.method1685()) {
            this.method1688();
        }
    }

    private boolean method1688() {
        if (mc.currentScreen instanceof SmithingScreen var4) {
            if (var4.getScreenHandler().getSlot(0).getStack().getItem() != Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
                int var8 = InventoryHelper.method169(AutoUpgrade::lambda$handle$1);
                if (var8 == -1) {
                    return false;
                } else {
                    mc.interactionManager
                            .clickSlot(var4.getScreenHandler().syncId, SlotUtils.method1541(var8), 0, SlotActionType.QUICK_MOVE, mc.player);
                    return true;
                }
            } else if (var4.getScreenHandler().getSlot(2).getStack().getItem() != Items.NETHERITE_INGOT) {
                int var7 = InventoryHelper.method169(AutoUpgrade::lambda$handle$2);
                if (var7 == -1) {
                    return false;
                } else {
                    mc.interactionManager
                            .clickSlot(var4.getScreenHandler().syncId, SlotUtils.method1541(var7), 0, SlotActionType.QUICK_MOVE, mc.player);
                    return true;
                }
            } else if (!var4.getScreenHandler().getSlot(3).getStack().isEmpty()) {
                mc.interactionManager.clickSlot(var4.getScreenHandler().syncId, 3, 0, SlotActionType.QUICK_MOVE, mc.player);
                return true;
            } else {
                int var6 = InventoryHelper.method169(AutoUpgrade::lambda$handle$3);
                if (var6 == -1) {
                    return false;
                } else {
                    mc.interactionManager
                            .clickSlot(var4.getScreenHandler().syncId, SlotUtils.method1541(var6), 0, SlotActionType.QUICK_MOVE, mc.player);
                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
