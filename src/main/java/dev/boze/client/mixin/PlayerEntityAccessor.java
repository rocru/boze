package dev.boze.client.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
    @Mutable
    @Accessor
    void setInventory(PlayerInventory var1);

    @Mutable
    @Accessor
    void setPlayerScreenHandler(PlayerScreenHandler var1);
}
