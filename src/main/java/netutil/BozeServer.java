package netutil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BozeServer {
    static String method27(final String parent) {
        final JsonObject method30 = method30("https://boze.dev/webfiles/servers.json");
        File parent2 = new File(System.getProperty("user.home"), "Boze" + File.separator + "cache");
        final String lowerCase = System.getProperty("os.name").toLowerCase();
        if (lowerCase.contains("nix") || lowerCase.contains("nux") || lowerCase.contains("aix")) {
            parent2 = new File(parent, "cache");
        }
        parent2.mkdirs();
        final File file = new File(parent2, "bestServer");
        String s = method30.get("FALLBACK").getAsString();
        try {
            if (file.exists()) {
                s = new String(Files.readAllBytes(file.toPath()));
            } else {
                s = method28();
                try (final PrintWriter printWriter = new PrintWriter(new File(parent2, "bestServer"))) {
                    printWriter.print(s);
                }
            }
        } catch (final Exception ex) {
        }
        if (method31(s, 3000)) {
            return s;
        }
        final String method31 = method28();
        try (final PrintWriter printWriter2 = new PrintWriter(new File(parent2, "bestServer"))) {
            printWriter2.print(method31);
        } catch (final FileNotFoundException cause) {
            throw new RuntimeException(cause);
        }
        return method31;
    }

    private static String method28() {
        JsonObject jsonObject = BozeServer.method30("https://boze.dev/webfiles/servers.json");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("best", "NONE");
        AtomicLong atomicLong = new AtomicLong(999L);
        jsonObject.keySet().forEach(arg_0 -> BozeServer.method32(jsonObject, hashMap, atomicLong, arg_0));
        return jsonObject.get(hashMap.get("best")).getAsString();
    }

    private static long method29(final String host) {
        try {
            final Socket socket = new Socket(host, 3000);
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            final long currentTimeMillis = System.currentTimeMillis();
            dataOutputStream.writeUTF("bandwidth");
            dataInputStream.readByte();
            final long n = System.currentTimeMillis() - currentTimeMillis;
            socket.close();
            return n;
        } catch (final IOException cause) {
            throw new RuntimeException(cause);
        }
    }

    public static JsonObject method30(final String spec) {
        try {
            final URLConnection openConnection = new URL(spec).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/4.0");
            return JsonParser.parseReader(new Gson().newJsonReader(new InputStreamReader(openConnection.getInputStream()))).getAsJsonObject();
        } catch (final MalformedURLException cause) {
            throw new RuntimeException(cause);
        } catch (final IOException cause2) {
            throw new RuntimeException(cause2);
        }
    }

    private static boolean method31(final String hostname, final int port) {
        boolean b = false;
        final InetSocketAddress endpoint = new InetSocketAddress(hostname, port);
        final Socket socket = new Socket();
        try {
            socket.connect(endpoint, 2000);
            socket.close();
            b = true;
        } catch (final SocketTimeoutException ex) {
        } catch (final IOException ex2) {
        }
        return b;
    }

    private static void method32(final JsonObject jsonObject, final HashMap hashMap, final AtomicLong atomicLong, final String key) {
        final String asString = jsonObject.get(key).getAsString();
        if (!key.equals("FALLBACK") && method31(asString, 3000)) {
            final long method29 = method29(asString);
            if (!hashMap.get("best").equals("NONE")) {
                atomicLong.set(Long.parseLong((String) hashMap.get(hashMap.get("best"))));
            }
            System.out.println(key + " " + method29);
            if (method29 < atomicLong.get()) {
                hashMap.put("best", key);
                hashMap.put(key, String.valueOf(method29));
            }
        }
    }
}
