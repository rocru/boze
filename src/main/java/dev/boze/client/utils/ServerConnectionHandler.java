package dev.boze.client.utils;

import dev.boze.client.events.ConnectEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

public class ServerConnectionHandler implements IMinecraft {
    private final Timer field1614 = new Timer();
    private final Timer field1615 = new Timer();
    public ServerInfo field1613;

    @EventHandler
    private void method2042(PacketBundleEvent var1) {
        if (var1.packet instanceof GameJoinS2CPacket) {
            this.field1615.reset();
        }
    }

    @EventHandler
    private void method722(ConnectEvent var1) {
        this.field1613 = mc.isInSingleplayer() ? null : mc.getCurrentServerEntry();
    }

    @EventHandler
    private void method1942(PostPlayerTickEvent var1) {
        if (mc.currentScreen instanceof DownloadingTerrainScreen) {
            if (this.field1614.hasElapsed(1000.0) && !this.field1615.hasElapsed(20000.0)) {
                mc.setScreen(null);
            }
        } else {
            this.field1614.reset();
        }
    }
}
