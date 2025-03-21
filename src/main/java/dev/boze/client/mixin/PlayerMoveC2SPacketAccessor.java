package dev.boze.client.mixin;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({PlayerMoveC2SPacket.class})
public interface PlayerMoveC2SPacketAccessor {
    @Mutable
    @Accessor
    void setYaw(float var1);

    @Mutable
    @Accessor
    void setPitch(float var1);
}
