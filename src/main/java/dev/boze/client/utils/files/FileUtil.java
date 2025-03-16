package dev.boze.client.utils.files;

import java.io.File;
import java.util.Locale;

public class FileUtil {
   public static void openFile(File folder) {
      try {
         String var4 = System.getProperty("os.name").toLowerCase(Locale.ROOT);
         if (var4.contains("win")) {
            Runtime.getRuntime().exec("explorer " + folder.getAbsolutePath());
         } else if (var4.contains("mac")) {
            Runtime.getRuntime().exec("open " + folder.getAbsolutePath());
         } else {
            Runtime.getRuntime().exec("xdg-open " + folder.getAbsolutePath());
         }
      } catch (Exception var5) {
      }
   }
}
