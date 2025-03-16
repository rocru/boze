package mapped;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import java.util.ArrayList;
import java.util.List;

public class Class5927 implements IMinecraft {
   private static final List<FakePlayerEntity> field43 = new ArrayList();

   public static void method102(String name, float health, boolean copyInventory) {
      FakePlayerEntity var3 = new FakePlayerEntity(mc.player, var108, var109, var110);
      var3.method2142();
      field43.add(var3);
   }

   public static void method2142() {
      if (!field43.isEmpty()) {
         field43.forEach(FakePlayerEntity::method1416);
         field43.clear();
      }
   }

   public static List<FakePlayerEntity> method1144() {
      return field43;
   }

   public static int method2010() {
      return field43.size();
   }
}
