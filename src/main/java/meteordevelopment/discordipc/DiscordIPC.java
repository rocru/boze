package meteordevelopment.discordipc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import meteordevelopment.discordipc.connection.Connection;

import java.lang.management.ManagementFactory;
import java.util.function.BiConsumer;

public class DiscordIPC {
    private static final Gson field3983 = new Gson();
    private static BiConsumer<Integer, String> field3984 = DiscordIPC::method2290;
    private static Connection field3985;
    private static Runnable field3986;
    private static boolean field3987;
    private static JsonObject field3988;
    private static IPCUser field3989;

    public static void method2281(BiConsumer<Integer, String> onError) {
        field3984 = onError;
    }

    public static boolean method2282(long appId, Runnable onReady) {
        field3985 = Connection.method2298(DiscordIPC::method2288);
        if (field3985 == null) {
            return false;
        } else {
            field3986 = onReady;
            JsonObject var6 = new JsonObject();
            var6.addProperty("v", 1);
            var6.addProperty("client_id", Long.toString(appId));
            field3985.method2299(Opcode.Handshake, var6);
            return true;
        }
    }

    public static boolean method2283() {
        return field3985 != null;
    }

    public static IPCUser method2284() {
        return field3989;
    }

    public static void method2285(RichPresence presence) {
        if (field3985 != null) {
            field3988 = presence.method2297();
            if (field3987) {
                method2287();
            }
        }
    }

    public static void method2286() {
        if (field3985 != null) {
            field3985.method2301();
            field3985 = null;
            field3986 = null;
            field3987 = false;
            field3988 = null;
            field3989 = null;
        }
    }

    private static void method2287() {
        JsonObject var2 = new JsonObject();
        var2.addProperty("pid", method2289());
        var2.add("activity", field3988);
        JsonObject var3 = new JsonObject();
        var3.addProperty("cmd", "SET_ACTIVITY");
        var3.add("args", var2);
        field3985.method2299(Opcode.Frame, var3);
        field3988 = null;
    }

    private static void method2288(Packet var0) {
        if (var0.method1473() == Opcode.Close) {
            if (field3984 != null) {
                field3984.accept(var0.method1474().get("code").getAsInt(), var0.method1474().get("message").getAsString());
            }

            method2286();
        } else if (var0.method1473() == Opcode.Frame) {
            if (var0.method1474().has("evt") && var0.method1474().get("evt").getAsString().equals("ERROR")) {
                JsonObject var4 = var0.method1474().getAsJsonObject("data");
                if (field3984 != null) {
                    field3984.accept(var4.get("code").getAsInt(), var4.get("message").getAsString());
                }
            } else if (var0.method1474().has("cmd") && var0.method1474().get("cmd").getAsString().equals("DISPATCH")) {
                field3987 = true;
                field3989 = field3983.fromJson(var0.method1474().getAsJsonObject("data").getAsJsonObject("user"), IPCUser.class);
                if (field3986 != null) {
                    field3986.run();
                }

                if (field3988 != null) {
                    method2287();
                }
            }
        }
    }

    private static int method2289() {
        String var0 = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(var0.substring(0, var0.indexOf(64)));
    }

    private static void method2290(int var0, String var1) {
        System.err.println("Discord IPC error " + var0 + " with message: " + var1);
    }
}
