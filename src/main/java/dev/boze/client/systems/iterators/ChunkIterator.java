package dev.boze.client.systems.iterators;

import dev.boze.client.mixin.ClientChunkManagerAccessor;
import dev.boze.client.mixin.ClientChunkMapAccessor;
import dev.boze.client.utils.IMinecraft;
import java.util.Iterator;
import net.minecraft.world.chunk.Chunk;

public class ChunkIterator implements Iterator<Chunk>, IMinecraft {
   private final ClientChunkMapAccessor field1258 = (ClientChunkMapAccessor)(((ClientChunkManagerAccessor)mc.world.getChunkManager()).getChunks());
   private final boolean field1259;
   private int field1260 = 0;
   private Chunk field1261;

   public ChunkIterator(boolean onlyWithLoadedNeighbours) {
      this.field1259 = onlyWithLoadedNeighbours;
      this.method541();
   }

   private Chunk method541() {
      Chunk var4 = this.field1261;
      this.field1261 = null;

      while (this.field1260 < this.field1258.getChunks().length()) {
         this.field1261 = (Chunk)this.field1258.getChunks().get(this.field1260++);
         if (this.field1261 != null && (!this.field1259 || this.method542(this.field1261))) {
            break;
         }
      }

      return var4;
   }

   private boolean method542(Chunk var1) {
      int var5 = var1.getPos().x;
      int var6 = var1.getPos().z;
      return mc.world.getChunkManager().isChunkLoaded(var5 + 1, var6)
         && mc.world.getChunkManager().isChunkLoaded(var5 - 1, var6)
         && mc.world.getChunkManager().isChunkLoaded(var5, var6 + 1)
         && mc.world.getChunkManager().isChunkLoaded(var5, var6 - 1);
   }

   public boolean hasNext() {
      return this.field1261 != null;
   }

   @Override
   public Chunk next() {
      return this.method541();
   }

   public static Iterable<Chunk> method544(boolean onlyWithLoadedNeighbours) {
      return () -> new ChunkIterator(onlyWithLoadedNeighbours);
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
