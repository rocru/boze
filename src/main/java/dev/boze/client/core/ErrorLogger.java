package dev.boze.client.core;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public class ErrorLogger {
   private static List<Exception> errors = new ArrayList();

   public static void log(Exception e) {
      Log.error(LogCategory.LOG, "Non-Fatal Error in mod Boze: ", e);
      errors.add(e);
   }
}
