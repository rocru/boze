package dev.boze.client.systems.modules.client;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.authlib.GameProfile;
import dev.boze.api.client.cape.CapeLoadResult;
import dev.boze.api.client.cape.CapeSource;
import dev.boze.api.internal.Instances;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.instances.impl.CapesInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.http.HttpUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Capes extends Module implements IMinecraft {
    public static final Capes INSTANCE = new Capes();
    private final BooleanSetting field1289 = new BooleanSetting("Anonymous", false, "Hide your cape from other users");
    public static final HashMap<String, String> field1290 = new HashMap();
    public static final List<String> field1291 = new ArrayList();
    public static final List<GameProfile> field1292 = new ArrayList();
    public static ScheduledExecutorService field1293;
    public static Identifier field1294 = Identifier.of("boze", "capes/default");
    public static Identifier field1295 = Identifier.of("boze", "capes/beta");
    public static HashMap<String, Identifier> field1296 = new HashMap();

    private Capes() {
        super("Capes", "Shows capes on other Boze users", Category.Client);
        field1293 = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).build());
        field1293.scheduleAtFixedRate(Capes::method1854, 0L, 10L, TimeUnit.SECONDS);
        this.setEnabled(true);
        this.field1289.method401(this::lambda$new$0);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            this.method1966(null);
            field1291.add(mc.player.getUuidAsString());
            field1292.add(mc.player.getGameProfile());
        }
    }

    public static void method1904() {
        method558("default", field1294);
        method558("beta", field1295);
    }

    private static void method558(String var0, Identifier var1) {
        File var5 = new File(ConfigManager.capes, var0 + ".png");
        if (!var5.exists()) {
            try {
                URLConnection var6 = new URL("https://boze.dev/webfiles/downloads/capes/" + var0 + ".png").openConnection();
                var6.addRequestProperty("User-Agent", "Mozilla/4.0");

                try {
                    BufferedInputStream var7 = new BufferedInputStream(var6.getInputStream());

                    try {
                        FileOutputStream var8 = new FileOutputStream(var5);

                        try {
                            byte[] var9 = new byte[1024];

                            int var10;
                            while ((var10 = var7.read(var9, 0, 1024)) != -1) {
                                var8.write(var9, 0, var10);
                            }
                        } catch (Throwable var14) {
                            try {
                                var8.close();
                            } catch (Throwable var13) {
                                var14.addSuppressed(var13);
                            }

                            throw var14;
                        }

                        var8.close();
                    } catch (Throwable var15) {
                        try {
                            var7.close();
                        } catch (Throwable var12) {
                            var15.addSuppressed(var12);
                        }

                        throw var15;
                    }

                    var7.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            } catch (IOException var17) {
                var17.printStackTrace();
            }
        }

        try {
            NativeImage var18 = NativeImage.read(new FileInputStream(var5));
            mc.getTextureManager().registerTexture(var1, new NativeImageBackedTexture(var18));
        } catch (IOException var11) {
            var11.printStackTrace();
        }
    }

    private static void method1854() {
        if (!mc.isInSingleplayer()) {
            if (!field1291.isEmpty()) {
                HashMap var3 = HttpUtil.get("https://config.boze.dev/get_capes")
                        .method2179("Token", ConfigManager.field2138)
                        .method2180(String.join(",", field1291))
                        .method2189(HashMap.class);
                if (var3 == null) {
                    return;
                }

                for (String var5 : field1291) {
                    if (var3.containsKey(var5)) {
                        if (!field1290.containsKey(var5)) {
                            field1290.put(var5, (String) var3.get(var5));
                        }
                    } else {
                        field1290.remove(var5);
                    }
                }

                field1291.clear();
            }

            if (!field1292.isEmpty()) {
                for (GameProfile var13 : field1292) {
                    if (!field1290.containsKey(var13.getId().toString())) {
                        for (CapeSource var6 : ((CapesInstance) Instances.getCapes()).field2092) {
                            URL var7 = var6.getUrl(var13);
                            if (var7 != null) {
                                try {
                                    HttpURLConnection var8 = (HttpURLConnection) var7.openConnection(mc.getNetworkProxy());
                                    var8.addRequestProperty("User-Agent", "Mozilla/4.0");
                                    var8.setDoInput(true);
                                    var8.setDoOutput(false);
                                    var8.connect();
                                    if (var8.getResponseCode() / 100 == 2) {
                                        NativeImage var9 = NativeImage.read(var8.getInputStream());
                                        Identifier var10 = Identifier.of("boze", "capes/addon/" + var6.name + "/" + var13.getId().toString());
                                        mc.getTextureManager().registerTexture(var10, new NativeImageBackedTexture(method559(var9)));
                                        field1296.put(var13.getId().toString(), var10);
                                        var6.callback(var13, CapeLoadResult.Success, var10);
                                    } else {
                                        var6.callback(var13, CapeLoadResult.InvalidResponse, null);
                                    }
                                    break;
                                } catch (Exception var11) {
                                    var6.callback(var13, CapeLoadResult.Error, null);
                                }
                            }
                        }
                    }
                }

                field1292.clear();
            }
        }
    }

    private static NativeImage method559(NativeImage var0) {
        byte var4 = 64;
        byte var5 = 32;
        int var6 = var0.getWidth();

        int var7;
        for (var7 = var0.getHeight(); var4 < var6 || var5 < var7; var5 *= 2) {
            var4 *= 2;
        }

        NativeImage var8 = new NativeImage(var4, var5, true);

        for (int var9 = 0; var9 < var6; var9++) {
            for (int var10 = 0; var10 < var7; var10++) {
                var8.setColor(var9, var10, var0.getColor(var9, var10));
            }
        }

        var0.close();
        return var8;
    }

    @EventHandler
    public void method1966(GameJoinEvent event) {
        if (this.field1289.getValue()) {
            HttpUtil.get("https://config.boze.dev/ping")
                    .method2179("Token", ConfigManager.field2138)
                    .method2179("Uuid", MathHelper.randomUuid(Random.createLocal()).toString())
                    .method2185();
        } else if (this.isEnabled()) {
            HttpUtil.get("https://config.boze.dev/ping").method2179("Token", ConfigManager.field2138).method2179("Uuid", mc.player.getUuidAsString()).method2185();
        }
    }

    private void lambda$new$0(Boolean var1) {
        if (MinecraftUtils.isClientActive()) {
            this.method1966(null);
        }
    }
}
