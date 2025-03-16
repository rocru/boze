package dev.boze.client.mixininterfaces;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;

public interface IPlayerInteractEntityC2SPacket {
   InteractType getType();

   Entity getEntity();
}
