package dev.boze.client.mixin;

import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ScreenHandler.class})
public interface ScreenHandlerAccessor {
    @Accessor("revision")
    void setRevision(int var1);
}
