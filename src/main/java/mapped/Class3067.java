package mapped;

import dev.boze.client.utils.misc.ISerializable;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Class3067 {
    public Class3067() {
        super();
    }

    public static <T extends ISerializable<?>> NbtList method5999(final Iterable<T> list) {
        final NbtList list2 = new NbtList();
        for (T t : list) {
            list2.add(t.toTag());
        }
        return list2;
    }

    public static <T> List<T> method6000(final NbtList tag, final Class3068<T> toItem) {
        final ArrayList list = new ArrayList(tag.size());
        final Iterator<NbtElement> iterator = tag.iterator();
        while (iterator.hasNext()) {
            final T method6001 = toItem.method6001(iterator.next());
            if (method6001 != null) {
                list.add(method6001);
            }
        }
        return list;
    }
}
