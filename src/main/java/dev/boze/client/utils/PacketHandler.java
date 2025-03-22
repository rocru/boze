package dev.boze.client.utils;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixin.PlayerMoveC2SPacketAccessor;
import dev.boze.client.mixin.ScreenHandlerAccessor;
import dev.boze.client.utils.trackers.ItemTracker;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

public class PacketHandler implements IMinecraft {
    public final Timer field1617 = new Timer();
    public int field1616 = -1;

    @EventHandler
    public void method1853(PrePacketSendEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (event.packet instanceof UpdateSelectedSlotC2SPacket var5) {
                this.field1616 = var5.getSelectedSlot();
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).setLastSelectedSlot(var5.getSelectedSlot());
                this.field1617.reset();
                InventoryUtil.method1649(this.field1616);
            } else if (event.packet instanceof PlayerMoveC2SPacket var6 && var6.getPitch(0.0F) > 100.0F) {
                ((PlayerMoveC2SPacketAccessor) var6).setYaw(var6.getYaw(mc.player.getYaw()) + 3.6E7F);
                ((PlayerMoveC2SPacketAccessor) var6).setPitch(mc.player.getPitch());
            }
        }
    }

    @EventHandler
    public void method2042(PacketBundleEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (event.packet instanceof UpdateSelectedSlotS2CPacket) {
                this.field1616 = ((UpdateSelectedSlotS2CPacket) event.packet).getSlot();
                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).setLastSelectedSlot(this.field1616);
            } else if (event.packet instanceof ScreenHandlerSlotUpdateS2CPacket var5
                    && var5.getSyncId() == mc.player.playerScreenHandler.syncId
                    && ItemTracker.method686(var5.getSlot(), var5.getStack())) {
                event.method1020();
                ((ScreenHandlerAccessor) mc.player.playerScreenHandler).setRevision(var5.getRevision());
            }
        }
    }
}
