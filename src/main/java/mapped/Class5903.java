package mapped;

import dev.boze.client.settings.WeirdColorSetting;
import dev.boze.client.utils.misc.ISerializable;
import dev.boze.client.utils.render.color.StaticColor;
import java.util.HashSet;
import java.util.Set;

public abstract class Class5903<T> implements ISerializable<T> {
   private final Set<WeirdColorSetting> field129 = new HashSet();

   public void method5875(WeirdColorSetting choice) {
      this.field129.add(var3117);
   }

   public void method5876(WeirdColorSetting choice) {
      this.field129.remove(var3118);
   }

   public void method2142() {
      for (WeirdColorSetting var5 : this.field129) {
         var5.method431();
      }
   }

   public boolean method2114() {
      return !this.field129.isEmpty();
   }

   public int method2010() {
      return this.field129.size();
   }

   public abstract StaticColor method208();
}
