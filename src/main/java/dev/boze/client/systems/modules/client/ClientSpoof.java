package dev.boze.client.systems.modules.client;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.BrandCustomPayloadAccessor;
import dev.boze.client.mixin.ResourcePackSendS2CPacketAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;

public class ClientSpoof extends Module {
    public static final ClientSpoof INSTANCE = new ClientSpoof();
    private final BooleanSetting field2341 = new BooleanSetting("FakeVanilla", true, "Pretends to be a vanilla client");
    private final BooleanSetting field2342 = new BooleanSetting("NoRequired", false, "Prevent servers from forcing a resource pack");

    private ClientSpoof() {
        super("ClientSpoof", "Spoofs client info to the server", Category.Client);
    }

    @EventHandler
    private void method1333(PrePacketSendEvent var1) {
        if (var1.packet instanceof CustomPayloadC2SPacket var5) {
            if (this.field2341.getValue() && var5.payload() instanceof BrandCustomPayload) {
                BrandCustomPayloadAccessor var6 = (BrandCustomPayloadAccessor) var5.payload();
                var6.setBrand("vanilla");
            }
        }
    }

    @EventHandler
    private void method1334(PacketBundleEvent var1) {
        if (this.field2342.getValue()) {
            if (!(var1.packet instanceof ResourcePackSendS2CPacket var5)) {
                return;
            }

            if (var5.required()) {
                ((ResourcePackSendS2CPacketAccessor) var1.packet).setRequired(false);
            }
        }
    }
}
