package dev.boze.client.events;

import net.minecraft.network.packet.Packet;

public class PacketEvent extends CancelableEvent {
    public Packet<?> packet;
}
