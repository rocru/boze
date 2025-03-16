package dev.boze.client.utils.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BozeExecutor {
   public static ExecutorService field3948;

   public static void method2199() {
      AtomicInteger var0 = new AtomicInteger(1);
      field3948 = Executors.newCachedThreadPool(BozeExecutor::lambda$init$0);
   }

   public static void method2200(Runnable task) {
      field3948.execute(task);
   }

   private static Thread lambda$init$0(AtomicInteger var0, Runnable var1) {
      Thread var4 = new Thread(var1);
      var4.setDaemon(true);
      var4.setName("Boze-" + var0.getAndIncrement());
      return var4;
   }
}
