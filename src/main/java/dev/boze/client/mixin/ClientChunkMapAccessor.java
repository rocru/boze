package dev.boze.client.mixin;

import java.util.concurrent.atomic.AtomicReferenceArray;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientChunkManager.ClientChunkMap.class})
public interface ClientChunkMapAccessor {
   @Accessor("chunks")
   AtomicReferenceArray<WorldChunk> getChunks();
}
