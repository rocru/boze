package dev.boze.client.events;

import net.minecraft.network.packet.Packet;

public class PrePacketSendEvent extends PacketEvent {
    public PrePacketSendEvent(Packet<?> packet) {
        this.packet = packet;
        this.method1021(false);
    }
}
