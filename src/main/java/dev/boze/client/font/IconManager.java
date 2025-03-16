package dev.boze.client.font;

import mapped.Class27;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

public class IconManager {
    private static FontLoader field1979 = null;

    public static void initialize() {
        if (field1979 == null) {
            try {
                InputStream var3 = MinecraftClient.getInstance().getResourceManager().getResource(Identifier.of("boze", "fonts/icons.ttf")).get()
                        .getInputStream();
                field1979 = new FontLoader(var3);
            } catch (IOException var4) {
                Class27.LOG.error("Failed to load icons font");
            }
        }
    }

    public static void setScale(double scale) {
        if (field1979 == null) {
            initialize();
        }

        field1979.startBuilding(scale);
    }

    public static void applyScale(double scale) {
        if (field1979 == null) {
            initialize();
        }

        field1979.startBuilding(scale, true);
    }

    public static void method1115() {
        field1979.endBuilding();
    }

    public static double method1116() {
        return field1979.method1390();
    }
}
