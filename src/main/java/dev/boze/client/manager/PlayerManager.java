package dev.boze.client.manager;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.utils.IMinecraft;
import meteordevelopment.orbit.EventHandler;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

public class PlayerManager implements IMinecraft {
    private static final Pattern field1301 = Pattern.compile("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)");
    private final Set<UUID> field1299 = new HashSet();
    private final List<UUID> field1300 = new ArrayList();
    private ScheduledExecutorService field1297;
    private ScheduledExecutorService field1298;

    public static String method1341(String uuid) {
        return uuid.contains("-") ? uuid : field1301.matcher(uuid).replaceFirst("$1-$2-$3-$4-$5");
    }

    public Set<UUID> method560() {
        return this.field1299;
    }

    public void method2142() {
    }

    @EventHandler
    public void method2042(PacketBundleEvent event) {
    }
}
