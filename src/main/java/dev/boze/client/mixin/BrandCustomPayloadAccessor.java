package dev.boze.client.mixin;

import net.minecraft.network.packet.BrandCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrandCustomPayload.class)
public interface BrandCustomPayloadAccessor {
    @Mutable
    @Accessor
    void setBrand(String var1);
}
