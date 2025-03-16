package dev.boze.client.systems.modules;

import dev.boze.client.settings.Setting;
import java.io.FileWriter;
import java.io.IOException;
import mapped.Class27;

public class ModulePrinter {
   private final FileWriter field2312;

   public ModulePrinter(FileWriter writer) {
      this.field2312 = writer;
   }

   public void method1328() {
      this.method1332("# Features");
      this.method1329(Category.Combat);
      this.method1329(Category.Misc);
      this.method1329(Category.Movement);
      this.method1329(Category.Legit);
      this.method1329(Category.Render);
      this.method1329(Category.Hud);
      this.method1329(Category.Graph);
      this.method1329(Category.Client);
   }

   private void method1329(Category var1) {
      this.method1332("<details class=\"module-category-details\">");
      this.method1332("<summary class=\"module-category\">%s</summary>", var1.toString());

      for (Module var6 : Class27.getModules().modules) {
         if (var6.category == var1) {
            this.method1330(var6);
         }
      }

      this.method1332("\n</details>");
   }

   private void method1330(Module var1) {
      this.method1332("<details class=\"module-details\">");
      this.method1332("<summary class=\"module\">%s</summary>", var1.internalName);
      String var5 = var1.description;
      if (var5.contains("\n")) {
         var5 = var5.substring(0, var5.indexOf("\n"));
      }

      this.method1332("\n%s\n", var5);

      for (Setting var7 : var1.method1144()) {
         this.method1331(var7);
      }

      this.method1332("\n</details>");
   }

   private void method1331(Setting<?> var1) {
      int var5 = 0;
      if (var1.block != null) {
         var5++;
      }

      if (var1.parent != null) {
         var5++;
      }

      String var6 = var1.desc;
      if (var6.contains("\n")) {
         var6 = var6.substring(0, var6.indexOf("\n"));
      }

      this.method1332("    ".repeat(var5) + "- %s: %s", var1.name, var6);
   }

   private void method1332(String var1, Object... var2) {
      try {
         this.field2312.write(String.format(var1, var2) + "\n");
      } catch (IOException var4) {
      }
   }
}
