package dev.boze.client.core;

import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.IMinecraft;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class BozeLogger
        implements IMinecraft {
    private static final ConcurrentHashMap<Module, LinkedList<LogMessage>> field1235 = new ConcurrentHashMap();
    private static final long field1236 = TimeUnit.MINUTES.toMillis(3L);
    public static final BozeLogger field1237 = new BozeLogger();

    public static void method522(Module module, String source, String message) {
        Object object = source;
        if (BozeLogger.mc.player != null) {
            object = source + "@" + BozeLogger.mc.player.age;
        }
        field1235.computeIfAbsent(module, BozeLogger::lambda$log$0).add(new LogMessage((String)object, message));
        BozeLogger.method396(module);
    }

    public static String method523(Module module, long timeSeconds) {
        long l = System.currentTimeMillis() - timeSeconds * 1000L;
        StringBuilder stringBuilder = new StringBuilder("\n");
        LinkedList<LogMessage> linkedList = field1235.get(module);
        if (linkedList != null) {
            for (LogMessage logMessage : linkedList) {
                if (logMessage.field1869 < l) continue;
                stringBuilder.append("[").append(logMessage.field1871).append("] ").append(logMessage.field1870).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static void method396(Module module) {
        long l = System.currentTimeMillis() - field1236;
        BozeLogger.method524(module, l);
    }

    private static void method524(Module module, long l) {
        LinkedList<LogMessage> linkedList = field1235.get(module);
        if (linkedList != null) {
            linkedList.removeIf(arg_0 -> BozeLogger.lambda$cleanOldEntriesWithCutoff$1(l, arg_0));
            if (linkedList.isEmpty()) {
                field1235.remove(module);
            }
        }
    }

    public static void method525(Module module, String source, String message) {
        BozeLogger.method522(module, source, "ERROR: " + message);
    }

    public static void method1338(String source, String message) {
        BozeLogger.method525(null, source, message);
    }

    public static void method527(Module module, String source, String message) {
        BozeLogger.method522(module, source, "CRASH: " + message);
        throw new RuntimeException(String.format("BOZE CRASHED [%s]: %s", source, message));
    }

    public static void method1339(String source, String message) {
        BozeLogger.method527(null, source, message);
    }

    public static void method529(Module module, String message) {
        BozeLogger.method522(module, module.getName(), message);
    }

    private static boolean lambda$cleanOldEntriesWithCutoff$1(long l, LogMessage logMessage) {
        return logMessage.field1869 < l;
    }

    private static LinkedList lambda$log$0(Module module) {
        return new LinkedList();
    }
}