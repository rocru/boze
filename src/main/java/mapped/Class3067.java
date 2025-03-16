package mapped;

import dev.boze.client.utils.misc.ISerializable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class Class3067 {
   public static <T extends ISerializable<?>> NbtList method5999(Iterable<T> list) {
      NbtList var4 = new NbtList();

      for (ISerializable var6 : var3177) {
         var4.add(var6.toTag());
      }

      return var4;
   }

   public static <T> List<T> method6000(NbtList tag, Class3068<T> toItem) {
      ArrayList var5 = new ArrayList(var3178.size());

      for (NbtElement var7 : var3178) {
         Object var8 = var3179.method6001(var7);
         if (var8 != null) {
            var5.add(var8);
         }
      }

      return var5;
   }
}
