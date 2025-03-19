package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.core.Version;
import dev.boze.client.utils.IMinecraft;

import java.text.DecimalFormat;

class AutoCrystalDisplayInfo implements IMinecraft {
   private final AutoCrystal field1353;

   AutoCrystalDisplayInfo(AutoCrystal var1) {
      this.field1353 = var1;
   }

   String method210() {
      if (this.field1353.autoCrystalTracker.field1538 == null) {
         return "";
      } else {
         StringBuilder var4 = new StringBuilder();
         DecimalFormat var5 = new DecimalFormat("#.#");
         this.method588(var4, this.field1353.autoCrystalTracker.field1538.getName().getString());
         this.method588(var4, var5.format(this.field1353.autoCrystalTracker.field1536));
         this.method588(var4, var5.format((double)this.field1353.autoCrystalTracker.method1384()));
         if (Version.isBeta) {
            float var6 = this.field1353.autoCrystalTracker.method1385() + this.field1353.autoCrystalTracker.method215();
            this.method588(var4, var5.format((double)var6));
            this.method588(var4, var5.format(Boze.getModules().field905.field1519));
         }

         String var7 = var4.toString();
         return var7.isEmpty() ? var7 : var7.substring(0, var7.length() - 1);
      }
   }

   private void method588(StringBuilder var1, String var2) {
      var1.append(var2);
      var1.append(",");
   }
}
