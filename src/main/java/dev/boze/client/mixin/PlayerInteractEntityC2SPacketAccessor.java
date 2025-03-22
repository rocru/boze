package dev.boze.client.mixin;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInteractEntityC2SPacket.class)
public interface PlayerInteractEntityC2SPacketAccessor {
    // Accessor for the 'type' field, assuming it is of type InteractType
    @Accessor
    InteractType getType();
    
    // Accessor for entityId field (already confirmed to exist)
    @Accessor
    int getEntityId();

    // Mutable accessor to set entityId
    @Accessor
    void setEntityId(int entityId);
}

