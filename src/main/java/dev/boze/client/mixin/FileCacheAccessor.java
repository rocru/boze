package dev.boze.client.mixin;

import net.minecraft.client.texture.PlayerSkinProvider.FileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.file.Path;

@Mixin(FileCache.class)
public interface FileCacheAccessor {
    @Accessor
    Path getDirectory();
}
