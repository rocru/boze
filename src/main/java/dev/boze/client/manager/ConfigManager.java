package dev.boze.client.manager;

import com.google.gson.*;
import dev.boze.client.Boze;
import dev.boze.client.command.arguments.ConfigArgument;
import dev.boze.client.core.Cache;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.enums.ConfigType;
import mapped.Class1201;
import mapped.Class5906;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;

public class ConfigManager {
    public static final String field2137 = "MAIN_PROFILE";
    public static final File fonts = new File(Boze.FOLDER, "fonts");
    public static final File images = new File(Boze.FOLDER, "images");
    public static final File spammer = new File(Boze.FOLDER, "spammer");
    public static final File sounds = new File(Boze.FOLDER, "sounds");
    public static final File kits = new File(Boze.FOLDER, "kits");
    public static final File capes = new File(Boze.FOLDER, "capes");
    public static final File addons = new File(Boze.FOLDER, "addons");
    public static final File news = new File(Boze.FOLDER, "news");
    public static String field2138 = "";
    public static boolean field2139 = false;

    public static void method1146() {
        try {
            File var3 = new File(Cache.get(), "at");
            if (var3.exists()) {
                String var4 = Files.readString(var3.toPath());
                if (var4.length() > 1) {
                    field2138 = var4;
                }
            }
        } catch (Exception var5) {
        }
    }

    public static void createDirs() {
        Boze.FOLDER.mkdir();
        fonts.mkdir();
        images.mkdir();
        spammer.mkdir();
        sounds.mkdir();
        kits.mkdir();
        capes.mkdir();
        addons.mkdir();
        news.mkdir();
    }

    public static void save() {
        Class1201.method2383(false);
        writeFile(Boze.FOLDER, "accounts", Boze.getAccounts().serialize());
        writeFile(Boze.FOLDER, "macros", Boze.getMacros().serialize());
        writeFile(Boze.FOLDER, "local-storage", Class5906.field1.serialize());
    }

    public static void load() {
        Boze.getAccounts().deserialize(readFile(Boze.FOLDER, "accounts"));
        Boze.getMacros().deserialize(readFile(Boze.FOLDER, "macros"));
        Class5906.field1.deserialize(readFile(Boze.FOLDER, "local-storage"));
    }

    public static void writeFile(File folder, String name, JsonObject object) {
        if (object != null) {
            GsonBuilder var5 = new GsonBuilder();
            Gson var6 = var5.setPrettyPrinting().create();

            FileWriter var7;
            try {
                var7 = new FileWriter(new File(folder, name + ".json"));
            } catch (IOException var22) {
                return;
            }

            try {
                JsonParser var8 = new JsonParser();
                JsonElement var9 = var8.parse(object.toString());
                String var10 = var6.toJson(var9);
                var7.write(var10);
            } catch (IOException var20) {
            } finally {
                try {
                    var7.flush();
                    var7.close();
                } catch (IOException var19) {
                }
            }
        }
    }

    public static JsonObject readFile(File folder, String name) {
        try {
            JsonParser var4 = new JsonParser();
            FileReader var5 = new FileReader(new File(folder, name + ".json"));
            return (JsonObject) var4.parse(var5);
        } catch (Exception var6) {
            return null;
        }
    }

    public static void uploadConfig(String name, NbtCompound tag, ConfigType tagType) {
        try {
            URL var6 = new URL("https://config.boze.dev/upload_" + tagType.name().toLowerCase(Locale.ROOT));
            HttpsURLConnection var7 = (HttpsURLConnection) var6.openConnection();
            var7.setRequestMethod("POST");
            var7.setDoOutput(true);
            var7.setUseCaches(false);
            var7.setRequestProperty("Content-Type", "application/octet-stream");
            var7.setRequestProperty("User-Agent", "Boze");
            var7.setRequestProperty("Token", field2138);
            var7.setRequestProperty("Name", name);
            OutputStream var8 = var7.getOutputStream();
            DataOutputStream var9 = new DataOutputStream(var8);
            NbtIo.writeCompound(tag, var9);
            var8.flush();
            var8.close();
            int var10 = var7.getResponseCode();
            if (var10 != 200) {
                Boze.LOG.warn("Unable to save " + name + ", this might result in some settings being reset/reverted");
            }

            var7.disconnect();
            if (tagType != ConfigType.PROFILE && tagType == ConfigType.CONFIG) {
                ConfigArgument.method988(name);
            }
        } catch (IOException var11) {
            ErrorLogger.log(var11);
            Boze.LOG.warn("Error saving " + name + ", this might result in some settings being reset/reverted");
        }
    }

    public static String publishConfig(String name, NbtCompound tag, String type) {
        try {
            URL var5 = new URL("https://config.boze.dev/share");
            HttpsURLConnection var6 = (HttpsURLConnection) var5.openConnection();
            var6.setRequestMethod("POST");
            var6.setDoOutput(true);
            var6.setUseCaches(false);
            var6.setRequestProperty("Content-Type", "application/octet-stream");
            var6.setRequestProperty("User-Agent", "Boze");
            var6.setRequestProperty("Token", field2138);
            var6.setRequestProperty("Name", name);
            var6.setRequestProperty("Type", type);
            OutputStream var7 = var6.getOutputStream();
            DataOutputStream var8 = new DataOutputStream(var7);
            NbtIo.writeCompound(tag, var8);
            var7.flush();
            var7.close();
            int var9 = var6.getResponseCode();
            if (var9 != 200) {
                Boze.LOG.warn("Unable to share " + name);
            }

            BufferedReader var10 = new BufferedReader(new InputStreamReader(var6.getInputStream()));
            String var11 = var10.readLine();
            var6.disconnect();
            return var11;
        } catch (IOException var12) {
            ErrorLogger.log(var12);
            Boze.LOG.warn("Unable to share " + name);
            return null;
        }
    }

    public static NbtCompound downloadConfig(String name, ConfigType tagType) {
        try {
            URL var4 = new URL("https://config.boze.dev/download_" + tagType.name().toLowerCase(Locale.ROOT));
            HttpsURLConnection var5 = (HttpsURLConnection) var4.openConnection();
            var5.setRequestMethod("GET");
            var5.setDoOutput(true);
            var5.setUseCaches(false);
            var5.setRequestProperty("User-Agent", "Boze");
            var5.setRequestProperty("Token", field2138);
            var5.setRequestProperty("Name", name);
            NbtCompound var6 = NbtIo.readCompound(new DataInputStream(var5.getInputStream()));
            var5.disconnect();
            return var6;
        } catch (IOException var7) {
            ErrorLogger.log(var7);
            Boze.LOG.warn("Error loading " + name + ", this might result in some settings being reset/reverted");
            return new NbtCompound();
        }
    }

    public static NbtCompound method1147(String code) {
        try {
            URL var4 = new URL("https://config.boze.dev/load");
            HttpsURLConnection var5 = (HttpsURLConnection) var4.openConnection();
            var5.setRequestMethod("GET");
            var5.setDoOutput(true);
            var5.setUseCaches(false);
            var5.setRequestProperty("User-Agent", "Boze");
            var5.setRequestProperty("Token", field2138);
            var5.setRequestProperty("CID", code);
            int var6 = var5.getResponseCode();
            if (var6 != 200) {
                Boze.LOG.warn("Unable to load " + code);
                var5.disconnect();
                return null;
            } else {
                NbtCompound var7 = NbtIo.readCompound(new DataInputStream(var5.getInputStream()));
                var5.disconnect();
                return var7;
            }
        } catch (IOException var8) {
            ErrorLogger.log(var8);
            Boze.LOG.warn("Error loading " + code);
            return new NbtCompound();
        }
    }

    public static String[] get(ConfigType tagType) {
        try {
            URL var3 = new URL("https://config.boze.dev/get_" + tagType.name().toLowerCase(Locale.ROOT));
            HttpsURLConnection var4 = (HttpsURLConnection) var3.openConnection();
            var4.setRequestMethod("GET");
            var4.setDoOutput(true);
            var4.setUseCaches(false);
            var4.setRequestProperty("User-Agent", "Boze");
            var4.setRequestProperty("Token", field2138);
            String var5 = IOUtils.toString(var4.getInputStream());
            var4.disconnect();
            return var5.split(",");
        } catch (IOException var6) {
            ErrorLogger.log(var6);
            Boze.LOG
                    .warn("Error loading list of " + tagType.name().toLowerCase(Locale.ROOT) + "s, this might result in some commands/GUIs not working properly");
            return new String[0];
        }
    }

    public static void delete(String name, ConfigType tagType) {
        try {
            URL var5 = new URL("https://config.boze.dev/delete_" + tagType.name().toLowerCase(Locale.ROOT));
            HttpsURLConnection var6 = (HttpsURLConnection) var5.openConnection();
            var6.setRequestMethod("GET");
            var6.setDoOutput(true);
            var6.setUseCaches(false);
            var6.setRequestProperty("User-Agent", "Boze");
            var6.setRequestProperty("Token", field2138);
            var6.setRequestProperty("Name", name);
            int var7 = var6.getResponseCode();
            if (var7 != 200) {
                Boze.LOG.warn("Unable to delete " + name);
            }

            var6.disconnect();
            if (tagType != ConfigType.PROFILE && tagType == ConfigType.CONFIG) {
                ConfigArgument.method989(name);
            }
        } catch (IOException var8) {
            ErrorLogger.log(var8);
            Boze.LOG.warn("Error deleting " + name);
        }
    }

    public static void linked() {
        try {
            URL var2 = new URL("https://boze.dev/api/discordlinked");
            HttpsURLConnection var3 = (HttpsURLConnection) var2.openConnection();
            var3.setRequestMethod("GET");
            var3.setDoOutput(true);
            var3.setUseCaches(false);
            var3.setRequestProperty("User-Agent", "Boze");
            var3.setRequestProperty("Token", field2138);
            String var4 = IOUtils.toString(var3.getInputStream());
            var3.disconnect();
            field2139 = var4.contains("true");
        } catch (IOException var5) {
            ErrorLogger.log(var5);
        }
    }
}
