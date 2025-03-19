package dev.boze.client.systems.iterators;

import dev.boze.client.mixin.ChunkAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Iterator;
import java.util.Map;

public class BlockEntityIterator implements Iterator<BlockEntity> {
    private final Iterator<Chunk> field1262 = new ChunkIterator(false);
    private Iterator<BlockEntity> field1263;

    public BlockEntityIterator() {
        this.method2142();
    }

    private void method2142() {
        while (this.field1262.hasNext()) {
            Map<BlockPos, BlockEntity> var4 = ((ChunkAccessor) this.field1262.next()).getBlockEntities();
            if (!var4.isEmpty()) {
                this.field1263 = var4.values().iterator();
                break;
            }
        }
    }

    public boolean hasNext() {
        if (this.field1263 == null) {
            return false;
        } else if (this.field1263.hasNext()) {
            return true;
        } else {
            this.method2142();
            return this.field1263.hasNext();
        }
    }

    @Override
    public BlockEntity next() {
        return this.field1263.next();
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object next() {
    //   return this.method545();
    //}
}
