package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.Render2DEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.ShulkerBoxScreenHandlerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.KitSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.InventoryUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.ShulkerBoxScreenHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class Regear extends Module {
    public static final Regear INSTANCE = new Regear();
    private final KitSetting field3055 = new KitSetting("Kit", "", "Current kit");
    private final FloatSetting field3056 = new FloatSetting("Delay", 0.5F, 0.0F, 5.0F, 0.1F, "Delay for moving items");
    private final BooleanSetting field3057 = new BooleanSetting("ReplaceItems", false, "Replace items in inventory if they don't match kit");
    private final dev.boze.client.utils.Timer field3058 = new dev.boze.client.utils.Timer();

    public Regear() {
        super("ReGear", "Automatically re-gears you when you open a shulker", Category.Misc);
    }

    @Override
    public void onEnable() {
        if (this.field3055.getValue().isEmpty()) {
            ChatInstance.method740(this.internalName, "Please select a kit using the regear command before using the module");
            this.setEnabled(false);
        }
    }

    @EventHandler
    private void method1749(Render2DEvent render2DEvent) {
        if (Regear.mc.currentScreen instanceof ShulkerBoxScreen shulkerBoxScreen) {
            if (!this.field3055.getValue().isEmpty() && this.field3055.method1282() != null && !this.field3055.method1282().isEmpty()) {
                ShulkerBoxScreenHandler screen = shulkerBoxScreen.getScreenHandler();
                AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                for (int i = 0; i < ((ShulkerBoxScreenHandlerAccessor) screen).getInventory().size(); ++i) {
                    ItemStack itemStack = ((ShulkerBoxScreenHandlerAccessor) screen).getInventory().getStack(i);
                    if (itemStack.isEmpty()) continue;
                    int n = i;
                    this.field3055.method1282().forEach((arg_0, arg_1) -> this.lambda$onRender2D$0(itemStack, n, atomicBoolean, arg_0, arg_1));
                }
                if (atomicBoolean.get() && AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                    Regear.mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                }
            }
        }
    }

    private void lambda$onRender2D$0(ItemStack var1, int var2, AtomicBoolean var3, String var4, Integer var5) {
        if (this.field3058.hasElapsed(this.field3056.getValue() * 50.0F)) {
            ItemStack var9 = mc.player.getInventory().getStack(var5);
            if (var9.isEmpty() || var9.getItem() == Items.AIR || this.field3057.getValue()) {
                if (!this.field3057.getValue()
                        || var9.isEmpty() && var9.getItem() == Items.AIR
                        || var9.getCount() < var9.getMaxCount()
                        || !var9.getItem().equals(var1.getItem())) {
                    int var10 = var4.indexOf(">");
                    if (var4.substring(0, var10).equalsIgnoreCase(var1.getItem().getName().getString())) {
                        if (var9.getCount() >= var9.getMaxCount() && var9.getItem().equals(var1.getItem())) {
                            return;
                        }

                        InvUtils.method2201().method2206(var2).method2213(var5);
                        var3.set(true);
                        this.field3058.reset();
                    }
                }
            }
        }
    }
}
