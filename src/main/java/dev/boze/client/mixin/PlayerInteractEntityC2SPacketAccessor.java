package dev.boze.client.mixin;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerInteractEntityC2SPacket.class)
public interface PlayerInteractEntityC2SPacketAccessor {
    @Accessor("type")  // Explicitly reference the private 'type' field
    InteractType getType();

    @Accessor("entityId") // Explicitly reference 'entityId'
    int getEntityId();

    @Accessor("entityId") // Ensure correct field name
    void setEntityId(int var1);
}
