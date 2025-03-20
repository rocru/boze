package dev.boze.client.utils;

import dev.boze.client.core.Version;
import dev.boze.client.systems.modules.client.Options;

public class DebugHandler {
    public static void execute(String str) {
        String[] var4 = str.split(" ");
        if (var4.length >= 2) {
            if (var4[0].equals("--debug-mode")) {
                String var5 = var4[1];
                switch (var5) {
                    case "resetprefix":
                        Options.INSTANCE.field989.setValue(".");
                        break;
                    case "printversion":
                        System.out.println(Version.tag);
                }
            }
        }
    }
}
