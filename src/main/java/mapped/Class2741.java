package mapped;

import dev.boze.client.utils.misc.ISerializable;
import net.minecraft.nbt.NbtCompound;

public class Class2741 implements ISerializable<Class2741> {
    private static final int field67 = 1;

    @Override
    public NbtCompound toTag() {
        NbtCompound var3 = new NbtCompound();
        var3.putInt("_v", 1);
        return var3;
    }

    public Class2741 method5349(NbtCompound tag) {
        return this;
    }

    // $VF: synthetic method
    // $VF: bridge method
    @Override
    public Class2741 fromTag(NbtCompound nbtCompound) {
        return this.method5349(nbtCompound);
    }
}
