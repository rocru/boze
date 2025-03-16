package dev.boze.client.core;

import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.IMinecraft;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BozeLogger implements IMinecraft {
   private static final ConcurrentHashMap<Module, LinkedList<LogMessage>> field1235 = new ConcurrentHashMap();
   private static final long field1236 = TimeUnit.MINUTES.toMillis(3L);
   public static final BozeLogger field1237 = new BozeLogger();

   public static void method522(Module module, String source, String message) {
      String var6 = source;
      if (mc.player != null) {
         var6 = source + "@" + mc.player.age;
      }

      ((LinkedList)field1235.computeIfAbsent(module, BozeLogger::lambda$log$0)).add(new LogMessage(var6, message));
      method396(module);
   }

   public static String method523(Module module, long timeSeconds) {
      long var6 = System.currentTimeMillis() - timeSeconds * 1000L;
      StringBuilder var8 = new StringBuilder("\n");
      LinkedList var9 = (LinkedList)field1235.get(module);
      if (var9 != null) {
         for (LogMessage var11 : var9) {
            if (var11.field1869 >= var6) {
               var8.append("[").append(var11.field1871).append("] ").append(var11.field1870).append("\n");
            }
         }
      }

      return var8.toString();
   }

   public static void method396(Module module) {
      long var1 = System.currentTimeMillis() - field1236;
      method524(module, var1);
   }

   private static void method524(Module var0, long var1) {
      LinkedList var6 = (LinkedList)field1235.get(var0);
      if (var6 != null) {
         var6.removeIf(BozeLogger::lambda$cleanOldEntriesWithCutoff$1);
         if (var6.isEmpty()) {
            field1235.remove(var0);
         }
      }
   }

   public static void method525(Module module, String source, String message) {
      method522(module, source, "ERROR: " + message);
   }

   public static void method1338(String source, String message) {
      method525(null, source, message);
   }

   public static void method527(Module module, String source, String message) {
      method522(module, source, "CRASH: " + message);
      throw new RuntimeException(String.format("BOZE CRASHED [%s]: %s", source, message));
   }

   public static void method1339(String source, String message) {
      method527(null, source, message);
   }

   public static void method529(Module module, String message) {
      method522(module, module.getName(), message);
   }

   private static boolean lambda$cleanOldEntriesWithCutoff$1(long var0, LogMessage var2) {
      return var2.field1869 < var0;
   }

   private static LinkedList lambda$log$0(Module var0) {
      return new LinkedList();
   }
}
