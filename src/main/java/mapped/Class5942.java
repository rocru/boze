package mapped;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import netutil.BozeClassLoader;
import netutil.Count;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Class5942 {
    static String method11538(String var0) {
        boolean var3;
        JsonObject var4;
        File var5;
        boolean var21;
        var21 = Count.field4012;
        var4 = method11541("https://boze.dev/webfiles/servers.json");
        var5 = new File(System.getProperty("user.home"), "Boze" + File.separator + "cache");
        String var6 = System.getProperty("os.name").toLowerCase();
        var3 = var21;
        var21 = var6.contains("nix");
        label95:
        if (!var3) {
            if (!var21) {
                var21 = var6.contains("nux");
                if (var3) {
                    break label95;
                }

                if (!var21) {
                    var21 = var6.contains("aix");
                    if (var3) {
                        break label95;
                    }

                    if (!var21) {
                        var21 = false;
                        break label95;
                    }
                }
            }

            var21 = true;
        }

        boolean var7 = var21;
        if (!var3) {
            if (var7) {
                var5 = new File(var0, "cache");
            }

            var5.mkdirs();
        }

        File var8 = new File(var5, "bestServer");
        String var9 = var4.get("FALLBACK").getAsString();

        try {
            label98:
            {
                if (!var3) {
                    if (var8.exists()) {
                        var9 = new String(Files.readAllBytes(var8.toPath()));
                        if (!var3) {
                            break label98;
                        }
                    }

                    var9 = method11539();
                }

                PrintWriter var10 = new PrintWriter(new File(var5, "bestServer"));

                try {
                    var10.print(var9);
                } catch (Throwable var17) {
                    try {
                        var10.close();
                    } catch (Throwable var16) {
                        var17.addSuppressed(var16);
                    }

                    throw var17;
                }

                var10.close();
            }
        } catch (Exception var18) {
        }

        String var22 = var9;
        if (!var3) {
            if (method11542(var9, 3000)) {
                return var9;
            }

            var22 = method11539();
        }

        var9 = var22;

        try {
            PrintWriter var20 = new PrintWriter(new File(var5, "bestServer"));

            try {
                var20.print(var9);
            } catch (Throwable var14) {
                try {
                    var20.close();
                } catch (Throwable var13) {
                    var14.addSuppressed(var13);
                }

                throw var14;
            }

            var20.close();
            return var9;
        } catch (FileNotFoundException var15) {
            throw new RuntimeException(var15);
        }
    }

    private static String method11539() {
        JsonObject jsonObject = Class5942.method11541("https://boze.dev/webfiles/servers.json");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("best", "NONE");
        AtomicLong atomicLong = new AtomicLong(999L);
        jsonObject.keySet().forEach(arg_0 -> Class5942.lambda$requestBestServer$0(jsonObject, hashMap, atomicLong, arg_0));
        return jsonObject.get(hashMap.get("best")).getAsString();
    }

    private static long method11540(String var0) {
        try {
            Socket var3 = new Socket(var0, 3000);
            DataInputStream var4 = new DataInputStream(var3.getInputStream());
            DataOutputStream var5 = new DataOutputStream(var3.getOutputStream());
            long var6 = System.currentTimeMillis();
            var5.writeUTF("bandwidth");
            var4.readByte();
            long var8 = System.currentTimeMillis();
            long var10 = var8 - var6;
            var3.close();
            return var10;
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        }
    }

    public static JsonObject method11541(String var0) {
        boolean var3 = Count.field4012;

        JsonObject var10000;
        try {
            URLConnection var4 = new URL(var0).openConnection();
            var4.addRequestProperty("User-Agent", "Mozilla/4.0");
            InputStream var5 = var4.getInputStream();
            JsonReader var6 = new Gson().newJsonReader(new InputStreamReader(var5));
            var10000 = JsonParser.parseReader(var6).getAsJsonObject();
        } catch (MalformedURLException var7) {
            throw new RuntimeException(var7);
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }

        if (var3) {
            BozeClassLoader.field4010 = !BozeClassLoader.field4010;
        }

        return var10000;
    }

    private static boolean method11542(String var0, int var1) {
        boolean var4 = false;
        InetSocketAddress var5 = new InetSocketAddress(var0, var1);
        Socket var6 = new Socket();

        try {
            var6.connect(var5, 2000);
            var6.close();
            var4 = true;
        } catch (SocketTimeoutException var9) {
        } catch (IOException var10) {
        }

        return var4;
    }

    private static void lambda$requestBestServer$0(JsonObject var0, HashMap var1, AtomicLong var2, String var3) {
        boolean var6;
        String var11;
        label43:
        {
            boolean var10000 = Count.field4012;
            String var7 = var0.get(var3).getAsString();
            var6 = var10000;
            boolean var10 = var3.equals("FALLBACK");
            if (!var6) {
                if (var10) {
                    return;
                }

                var11 = var7;
                if (var6) {
                    break label43;
                }

                var10 = method11542(var7, 3000);
            }

            if (!var10) {
                return;
            }

            var11 = var7;
        }

        long var8;
        label45:
        {
            var8 = method11540(var11);
            int var12 = var1.get("best").equals("NONE") ? 1 : 0;
            if (!var6) {
                if (var12 == 0) {
                    var2.set(Long.parseLong((String) var1.get(var1.get("best"))));
                }

                System.out.println(var3 + " " + var8);
                if (var6) {
                    break label45;
                }

                long var13;
                var12 = (var13 = var8 - var2.get()) == 0L ? 0 : (var13 < 0L ? -1 : 1);
            }

            if (var12 >= 0) {
                return;
            }

            var1.put("best", var3);
        }

        var1.put(var3, String.valueOf(var8));
    }
}
