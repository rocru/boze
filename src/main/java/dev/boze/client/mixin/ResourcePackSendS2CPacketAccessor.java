package dev.boze.client.mixin;

import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ResourcePackSendS2CPacket.class})
public interface ResourcePackSendS2CPacketAccessor {
    @Mutable
    @Accessor
    void setRequired(boolean var1);
}
