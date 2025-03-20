package dev.boze.client.utils.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class BozeExecutor {
    public static ExecutorService field3948;

    public static void method2199() {
        AtomicInteger var0 = new AtomicInteger(1);
        field3948 = Executors.newCachedThreadPool((runnable) -> {
            Thread var4 = new Thread(runnable);
            var4.setDaemon(true);
            var4.setName("Boze-" + var0.getAndIncrement());
            return var4;
        });
    }

    public static void method2200(Runnable task) {
        field3948.execute(task);
    }
}