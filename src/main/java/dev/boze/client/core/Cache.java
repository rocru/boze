package dev.boze.client.core;

import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.io.File;

public class Cache {
    public static File get() {
        File var3 = new File(System.getProperty("user.home"), "Boze" + File.separator + "cache");
        String var4 = System.getProperty("os.name").toLowerCase();
        boolean var5 = var4.contains("nix") || var4.contains("nux") || var4.contains("aix");
        if (var5 && (!var3.exists() || !new File(var3, "at").exists())) {
            var3 = new File(FabricLoaderImpl.INSTANCE.getGameDirectory(), "cache");
        }

        return var3;
    }
}
