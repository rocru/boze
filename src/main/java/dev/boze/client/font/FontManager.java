package dev.boze.client.font;

import dev.boze.client.manager.ConfigManager;
import dev.boze.client.utils.files.StreamUtils;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

public class FontManager {
    private static FontLoader field1966 = null;
    private static String field1967 = null;
    private static String field1968 = "lexend";
    private static boolean field1969 = false;

    public static FontLoader method1102() {
        if (field1966 == null || !field1968.equals(field1967)) {
            method1107();
        }

        return field1966;
    }

    public static void method1103(String name) {
        field1968 = name;
        field1969 = false;
    }

    public static void method1104() {
        field1969 = true;
    }

    public static FontRenderer method1105() {
        return FontRenderer.field1075;
    }

    public static boolean method1106() {
        return field1969;
    }

    public static void method1107() {
        File var3 = new File(ConfigManager.fonts, field1968 + ".ttf");
        if (!var3.exists()) {
            if (field1968.equals("lexend")) {
                try (InputStream is = FontManager.class.getResourceAsStream("assets/boze/fonts/lexend.ttf")) {
                    Files.write(var3.toPath(), Objects.requireNonNull(is).readAllBytes());
                } catch (Exception var5) {
                    var5.printStackTrace(System.err);
                }
            } else {
                field1968 = "lexend";
                method1107();
            }
        }

        field1966 = new FontLoader(new File(ConfigManager.fonts, field1968 + ".ttf"));
        field1967 = field1968;
        IconManager.initialize();
    }
}
