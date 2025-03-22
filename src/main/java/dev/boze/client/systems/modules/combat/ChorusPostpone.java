package dev.boze.client.systems.modules.combat;

import dev.boze.client.events.*;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;

import java.util.LinkedList;
import java.util.Queue;

public class ChorusPostpone extends Module {
    public static final ChorusPostpone INSTANCE = new ChorusPostpone();
    private final BindSetting teleportBind = new BindSetting("Bind", Bind.fromKey(341), "Bind to teleport");
    private final BooleanSetting itemSwap = new BooleanSetting("ItemSwap", false, "Teleport when you swap items");
    private final Queue<TeleportConfirmC2SPacket> field2555 = new LinkedList();
    private int field2552;
    private boolean field2553;
    private boolean field2554;

    public ChorusPostpone() {
        super("ChorusPostpone", "Delay chorus teleports", Category.Combat);
    }

    @Override
    public String method1322() {
        return this.field2553 ? "Postponing" : "";
    }

    @Override
    public void onEnable() {
        this.field2553 = false;
        this.field2555.clear();
    }

    @Override
    public void onDisable() {
        if (MinecraftUtils.isClientActive() && this.field2553) {
            this.method1503();
        }

        this.field2555.clear();
    }

    @EventHandler
    private void method1498(PrePacketSendEvent var1) {
        if (var1.packet instanceof TeleportConfirmC2SPacket var5 && this.field2553 && !this.field2554) {
            this.field2555.add(var5);
            var1.method1020();
        }
    }

    @EventHandler
    private void method1499(KeyEvent var1) {
        if (this.teleportBind.getValue().matches(true, var1.key)) {
            this.method1503();
        }
    }

    @EventHandler
    private void method1500(MouseButtonEvent var1) {
        if (this.teleportBind.getValue().matches(false, var1.button)) {
            this.method1503();
        }
    }

    @EventHandler
    private void method1501(PreTickEvent var1) {
        if (this.field2553 && this.itemSwap.getValue() && this.field2552 != mc.player.getInventory().selectedSlot) {
            this.method1503();
        }
    }

    @EventHandler
    private void method1502(FinishUsingEvent var1) {
        if (var1.field1919.getItem().equals(Items.CHORUS_FRUIT)) {
            this.field2553 = true;
            this.field2552 = mc.player.getInventory().selectedSlot;
        }
    }

    private void method1503() {
        this.field2554 = true;

        while (!this.field2555.isEmpty()) {
            mc.getNetworkHandler().sendPacket(this.field2555.poll());
        }

        this.field2553 = false;
        this.field2554 = false;
    }
}
