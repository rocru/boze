package dev.boze.client.systems.modules.hud.graph;

import dev.boze.client.enums.CountMode;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.GraphHUDModule;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;

public class ClientPackets extends GraphHUDModule {
    public static final ClientPackets INSTANCE = new ClientPackets();
    private final EnumSetting<CountMode> field2668 = new EnumSetting<CountMode>("Mode", CountMode.Second, "Count per tick or per second");
    private int field2669;

    public ClientPackets() {
        super("ClientPackets", "Graphs outbound packets");
        this.field2300.setValue(true);
    }

    @EventHandler
    public void method1564(PrePacketSendEvent event) {
        if (this.field2668.getValue() == CountMode.Tick) {
            this.field2669++;
        }
    }

    @EventHandler
    public void method1565(PostTickEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (this.field2668.getValue() == CountMode.Second) {
                if (mc.player.age % 20 == 0) {
                    if (mc.getNetworkHandler() == null || mc.getNetworkHandler().getConnection() == null) {
                        return;
                    }

                    this.method1324(mc.getNetworkHandler().getConnection().getAveragePacketsSent());
                }
            } else {
                this.method1324(this.field2669);
                this.field2669 = 0;
            }
        }
    }
}
