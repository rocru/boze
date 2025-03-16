package dev.boze.client.systems.modules.hud.core;

import java.util.Objects;

public class ArrayListModuleInfo {
   private final String field2593;
   private final String field2594;
   private final boolean field2595;

   public ArrayListModuleInfo(String title, String info, boolean state) {
      this.field2593 = title;
      this.field2594 = info;
      this.field2595 = state;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ArrayListModuleInfo var5 = (ArrayListModuleInfo)o;
         return Objects.equals(this.field2593, var5.field2593);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.field2593);
   }
}
