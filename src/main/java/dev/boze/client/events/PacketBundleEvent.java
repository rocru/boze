package dev.boze.client.events;

import net.minecraft.network.packet.Packet;

public class PacketBundleEvent extends PacketEvent {
    public PacketBundleEvent(Packet<?> packet) {
        this.packet = packet;
        this.method1021(false);
    }
}
