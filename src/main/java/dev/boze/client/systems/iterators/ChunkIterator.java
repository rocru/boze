package dev.boze.client.systems.iterators;

import dev.boze.client.mixin.ClientChunkManagerAccessor;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ChunkIterator implements Iterator<Chunk>, IMinecraft {
    private final ClientChunkManager.ClientChunkMap field1258 = ((ClientChunkManagerAccessor) mc.world.getChunkManager()).getChunks();
    private final boolean field1259;
    private int field1260 = 0;
    private Chunk field1261;

    public ChunkIterator(boolean onlyWithLoadedNeighbours) {
        this.field1259 = onlyWithLoadedNeighbours;
        this.method541();
    }

    public static Iterable<Chunk> method544(boolean onlyWithLoadedNeighbours) {
        return () -> new ChunkIterator(onlyWithLoadedNeighbours);
    }

    private Chunk method541() {
        Chunk var4 = this.field1261;
        this.field1261 = null;

        AtomicReferenceArray<WorldChunk> chunks = getChunks(this.field1258);
        if (chunks == null) return var4;

        while (this.field1260 < chunks.length()) {
            this.field1261 = chunks.get(this.field1260++);
            if (this.field1261 != null && (!this.field1259 || this.method542(this.field1261))) {
                break;
            }
        }

        return var4;
    }

    // I'm too lazy to fix it using accessors
    @SuppressWarnings("unchecked")
    private AtomicReferenceArray<WorldChunk> getChunks(ClientChunkManager.ClientChunkMap chunkMap) {
        try {
            Field chunksField;
            chunksField = ClientChunkManager.ClientChunkMap.class.getDeclaredField("chunks");
            chunksField.setAccessible(true);
            return (AtomicReferenceArray<WorldChunk>) chunksField.get(chunkMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean method542(Chunk var1) {
        int var5 = var1.getPos().x;
        int var6 = var1.getPos().z;
        return mc.world.getChunkManager().isChunkLoaded(var5 + 1, var6)
                && mc.world.getChunkManager().isChunkLoaded(var5 - 1, var6)
                && mc.world.getChunkManager().isChunkLoaded(var5, var6 + 1)
                && mc.world.getChunkManager().isChunkLoaded(var5, var6 - 1);
    }

    @Override
    public boolean hasNext() {
        return this.field1261 != null;
    }

    @Override
    public Chunk next() {
        return this.method541();
    }

    // $VF: synthetic method
    // $VF: bridge method
    //p/ublic Object next() {
    //  return this.method543();
    //}

    //private static Iterator<Chunk> lambda$chunks$0(boolean var0) {
    //   return new ChunkIterator(var0);
    //}
}
