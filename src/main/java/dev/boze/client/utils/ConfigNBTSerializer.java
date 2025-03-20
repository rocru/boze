package dev.boze.client.utils;

import dev.boze.client.core.Version;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.enums.CriticalMode;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import net.minecraft.nbt.NbtCompound;

public class ConfigNBTSerializer {
    public static boolean field3913 = false;

    public static NbtCompound method2136(Module module) {
        NbtCompound var4 = new NbtCompound();
        NbtCompound var5 = module.method225();
        if (var5 != null) {
            var4.put("main", var5);
        }

        NbtCompound var6 = module.method226();
        if (var6 != null) {
            var4.put("visuals", var6);
        }

        if (module.configCategory == ConfigCategory.Client) {
            NbtCompound var7 = module.method228();
            if (var7 != null) {
                var4.put("client", var7);
            }
        }

        return var4;
    }

    public static void method2137(Module module, NbtCompound config) {
        field3913 = true;
        if (config.contains("main")) {
            module.method232(config.getCompound("main"));
        }

        if (config.contains("visuals")) {
            module.method394(config.getCompound("visuals"));
        }

        if (config.contains("client")) {
            module.method234(config.getCompound("client"));
        }

        field3913 = false;
    }

    public static void method2138(Module module, String title) {
        NbtCompound var4 = method2139(title, module);
        NbtCompound var5 = method2136(module);
        NbtCompound var6 = new NbtCompound();
        var6.put("v2.info", var4);
        var6.put("v2.data", var5);
        ConfigManager.uploadConfig(title + "." + module.internalName, var6, ConfigType.CONFIG);
    }

    public static NbtCompound method2139(String name, Module module) {
        NbtCompound var4 = new NbtCompound();
        var4.putString("name", name);
        var4.putString("type", CriticalMode.Module.name());
        var4.putString("module", module.internalName);
        var4.putString("version", Version.tag);
        var4.putLong("timestamp", System.currentTimeMillis());
        return var4;
    }

    public static NbtCompound method2140(String name, Category category) {
        NbtCompound var4 = new NbtCompound();
        var4.putString("name", name);
        var4.putString("type", CriticalMode.Category.name());
        var4.putString("category", category.name());
        var4.putString("version", Version.tag);
        var4.putLong("timestamp", System.currentTimeMillis());
        return var4;
    }

    public static NbtCompound method2141(String name) {
        NbtCompound var3 = new NbtCompound();
        var3.putString("name", name);
        var3.putString("type", CriticalMode.Full.name());
        var3.putString("version", Version.tag);
        var3.putLong("timestamp", System.currentTimeMillis());
        return var3;
    }
}
