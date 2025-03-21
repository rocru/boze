package mapped;

import dev.boze.client.Boze;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.systems.modules.client.Profiles;
import dev.boze.client.utils.network.BozeExecutor;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class Class1201 {
    public static final HashSet<String> field57;
    public static final HashSet<String> field58;
    public static final HashSet<String> field59;
    public static final HashSet<String> field60;
    public static final HashSet<String> field61;
    private static boolean field62;

    public Class1201() {
        super();
    }

    public static void method2378() {
        final Iterator iterator = new ArrayList(Arrays.asList(ConfigManager.get(ConfigType.PROFILE))).iterator();
        while (iterator.hasNext()) {
            final String e = (String) iterator.next();
            if (e.equals("v2.special.selection")) {
                System.out.println("Selection exists");
                Class1201.field62 = true;
            } else if (!e.startsWith("v2.")) {
                Class1201.field57.add(e);
            } else if (e.contains(".main.")) {
                Class1201.field58.add(e);
            } else if (e.contains(".visuals.")) {
                Class1201.field59.add(e);
            } else if (e.contains(".binds.")) {
                Class1201.field60.add(e);
            } else {
                if (!e.contains(".client.")) {
                    continue;
                }
                Class1201.field61.add(e);
            }
        }
        if (Class1201.field62) {
            System.out.println("Loading from selection");
            method2387();
        } else {
            if (Class1201.field58.isEmpty() && !Class1201.field57.isEmpty()) {
                final NbtCompound method2389 = method2389("MAIN_PROFILE");
                if (method2389 != null) {
                    Boze.getModules().fromTag(method2389);
                }
            }
            method2383(false);
        }
    }

    private static NbtCompound method2379(String string) {
        return switch (string) {
            case "v2.main." -> Boze.getModules().method225();
            case "v2.visuals." -> Boze.getModules().method226();
            case "v2.binds." -> Boze.getModules().method227();
            case "v2.client." -> Boze.getModules().method228();
            default -> throw new IllegalStateException("Unexpected value: " + string);
        };
    }

    public static void method2380(String var1253, NbtCompound var1254) {
        if (var1254 == null) {
            return;
        }
        switch (var1253) {
            case "v2.main.": {
                Boze.getModules().method232(var1254);
                break;
            }
            case "v2.visuals.": {
                Boze.getModules().method394(var1254);
                break;
            }
            case "v2.binds.": {
                Boze.getModules().method233(var1254);
                break;
            }
            case "v2.client.": {
                Boze.getModules().method234(var1254);
            }
        }
    }

    public static void method2381(CurrentProfileSetting var1255, String var1256) {
        NbtCompound nbtCompound;
        NbtCompound nbtCompound2 = Class1201.method2379(var1255.field969);
        if (nbtCompound2 != null) {
            nbtCompound = nbtCompound2;
            String string = var1255.getValue();
            NbtCompound finalNbtCompound = nbtCompound;
            BozeExecutor.method2200(() -> Class1201.lambda$swap$0(string, finalNbtCompound));
        }
        var1255.setValue(var1256);
        nbtCompound = Class1201.method2389(var1256);
        Class1201.method2380(var1255.field969, nbtCompound);
    }

    public static void method2382(boolean var1257, CurrentProfileSetting var1258, String var1259) {
        NbtCompound nbtCompound = Class1201.method2379(var1258.field969);
        if (nbtCompound == null) {
            return;
        }
        var1258.field968.add(var1259);
        if (var1257) {
            BozeExecutor.method2200(() -> Class1201.lambda$save$1(var1259, nbtCompound));
        } else {
            Class1201.method2386(var1259, nbtCompound);
        }
    }

    public static void method2383(final boolean thread) {
        String method1322 = Profiles.INSTANCE.field762.getValue();
        String method1323 = Profiles.INSTANCE.field763.getValue();
        String method1324 = Profiles.INSTANCE.field764.getValue();
        String method1325 = Profiles.INSTANCE.field765.getValue();
        if (method1322.isEmpty()) {
            method1322 = "v2.main.default";
            Profiles.INSTANCE.field762.setValue(method1322);
        }
        if (method1323.isEmpty()) {
            method1323 = "v2.visuals.default";
            Profiles.INSTANCE.field763.setValue(method1323);
        }
        if (method1324.isEmpty()) {
            method1324 = "v2.binds.default";
            Profiles.INSTANCE.field764.setValue(method1324);
        }
        if (method1325.isEmpty()) {
            method1325 = "v2.client.default";
            Profiles.INSTANCE.field765.setValue(method1325);
        }
        method2384(thread, method1322, method1323, method1324, method1325);
    }

    public static void method2384(boolean var1261, String var1262, String var1263, String var1264, String var1265) {
        NbtCompound nbtCompound = Boze.getModules().method225();
        NbtCompound nbtCompound2 = Boze.getModules().method226();
        NbtCompound nbtCompound3 = Boze.getModules().method227();
        NbtCompound nbtCompound4 = Boze.getModules().method228();
        field58.add(var1262);
        field59.add(var1263);
        field60.add(var1264);
        field61.add(var1265);
        if (var1261) {
            BozeExecutor.method2200(() -> Class1201.lambda$save$2(var1262, var1263, var1264, var1265, nbtCompound, nbtCompound2, nbtCompound3, nbtCompound4));
        } else {
            Class1201.method2385(var1262, var1263, var1264, var1265, nbtCompound, nbtCompound2, nbtCompound3, nbtCompound4);
        }
    }

    private static void method2385(final String name, final String name2, final String name3, final String name4, final NbtCompound tag, final NbtCompound tag2, final NbtCompound tag3, final NbtCompound tag4) {
        final NbtCompound tag5 = new NbtCompound();
        tag5.putString("main", name);
        tag5.putString("visuals", name2);
        tag5.putString("binds", name3);
        tag5.putString("client", name4);
        method2386("v2.special.selection", tag5);
        method2386(name, tag);
        method2386(name2, tag2);
        method2386(name3, tag3);
        method2386(name4, tag4);
    }

    public static void method2386(final String name, final NbtCompound tag) {
        ConfigManager.uploadConfig(name, tag, ConfigType.PROFILE);
    }

    public static void method2387() {
        final NbtCompound method2389 = method2389("v2.special.selection");
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        if (method2389.contains("main")) {
            string = method2389.getString("main");
            Profiles.INSTANCE.field762.setValue(string);
        } else {
            Profiles.INSTANCE.field762.setValue("v2.main.default");
        }
        if (method2389.contains("visuals")) {
            string2 = method2389.getString("visuals");
            Profiles.INSTANCE.field763.setValue(string2);
        } else {
            Profiles.INSTANCE.field763.setValue("v2.visuals.default");
        }
        if (method2389.contains("binds")) {
            string3 = method2389.getString("binds");
            Profiles.INSTANCE.field764.setValue(string3);
        } else {
            Profiles.INSTANCE.field764.setValue("v2.binds.default");
        }
        if (method2389.contains("client")) {
            string4 = method2389.getString("client");
            Profiles.INSTANCE.field765.setValue(string4);
        } else {
            Profiles.INSTANCE.field765.setValue("v2.client.default");
        }
        method2388(string, string2, string3, string4);
    }

    public static void method2388(final String main, final String visuals, final String binds, final String client) {
        final NbtCompound method2389 = method2389(main);
        final NbtCompound method2390 = method2389(visuals);
        final NbtCompound method2391 = method2389(binds);
        final NbtCompound method2392 = method2389(client);
        if (method2389 != null) {
            Boze.getModules().method232(method2389);
            Class1201.field58.add(main);
        }
        if (method2390 != null) {
            Boze.getModules().method394(method2390);
            Class1201.field59.add(visuals);
        }
        if (method2391 != null) {
            Boze.getModules().method233(method2391);
            Class1201.field60.add(binds);
        }
        if (method2392 != null) {
            Boze.getModules().method234(method2392);
            Class1201.field61.add(client);
        }
    }

    public static NbtCompound method2389(final String name) {
        if (name == null) {
            return null;
        }
        return ConfigManager.downloadConfig(name, ConfigType.PROFILE);
    }

    public static boolean method2390() {
        return !Class1201.field57.isEmpty();
    }

    private static void lambda$save$2(final String s, final String s2, final String s3, final String s4, final NbtCompound nbtCompound, final NbtCompound nbtCompound2, final NbtCompound nbtCompound3, final NbtCompound nbtCompound4) {
        method2385(s, s2, s3, s4, nbtCompound, nbtCompound2, nbtCompound3, nbtCompound4);
    }

    private static void lambda$save$1(final String name, final NbtCompound tag) {
        method2386(name, tag);
    }

    private static void lambda$swap$0(final String name, final NbtCompound tag) {
        method2386(name, tag);
    }

    static {
        field57 = new HashSet<String>();
        field58 = new HashSet<String>();
        field59 = new HashSet<String>();
        field60 = new HashSet<String>();
        field61 = new HashSet<String>();
        Class1201.field62 = false;
    }
}
