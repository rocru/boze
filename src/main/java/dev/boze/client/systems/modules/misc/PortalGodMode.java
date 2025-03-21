package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;

public class PortalGodMode extends Module {
    public static final PortalGodMode INSTANCE = new PortalGodMode();
    private TeleportConfirmC2SPacket field3054 = null;

    public PortalGodMode() {
        super("PortalGodMode", "Makes you invincible after going through a portal", Category.Misc);
    }

    @Override
    public void onEnable() {
        this.field3054 = null;
    }

    @Override
    public void onDisable() {
        if (this.field3054 != null && mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(this.field3054);
        }
    }

    @EventHandler
    private void method1747(PrePacketSendEvent var1) {
        if (var1.packet instanceof TeleportConfirmC2SPacket) {
            var1.method1021(true);
            this.field3054 = (TeleportConfirmC2SPacket) var1.packet;
        }
    }

    @EventHandler
    public void method1748(PacketBundleEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket var5 && MinecraftUtils.isClientActive()) {
            ((PlayerPositionLookS2CPacketAccessor) var5).setYaw(mc.player.getYaw());
            ((PlayerPositionLookS2CPacketAccessor) var5).setPitch(mc.player.getPitch());
            var5.getFlags().remove(PositionFlag.X_ROT);
            var5.getFlags().remove(PositionFlag.Y_ROT);
        }
    }
}
