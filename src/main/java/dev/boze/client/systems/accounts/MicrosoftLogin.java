package dev.boze.client.systems.accounts;

import com.google.gson.annotations.SerializedName;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.utils.http.HttpUtil;
import dev.boze.client.utils.network.BozeExecutor;
import net.minecraft.util.Util;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

public class MicrosoftLogin {
    public static class Handler implements HttpHandler {
        private Handler() {
        }

        public void handle(HttpExchange req) throws IOException {
            if (req.getRequestMethod().equals("GET")) {
                List<NameValuePair> var5 = URLEncodedUtils.parse(req.getRequestURI(), StandardCharsets.UTF_8.name());
                boolean var6 = false;

                for (NameValuePair var8 : var5) {
                    if (var8.getName().equals("code")) {
                        this.method1318(var8.getValue());
                        var6 = true;
                        break;
                    }
                }

                if (!var6) {
                    this.method1319(req, "Authentication failed");
                } else {
                    this.method1319(req, "Please close this page");
                }
            }

            MicrosoftLogin.stopServer();
        }

        private void method1318(String var1) {
            AuthTokenResponse var5 = HttpUtil.post("https://login.live.com/oauth20_token.srf")
                    .method2181("client_id=2cd47301-d033-4784-8f9c-a616ff9070ec&code=" + var1 + "&grant_type=authorization_code&redirect_uri=http://127.0.0.1:30222")
                    .method2189(AuthTokenResponse.class);
            if (var5 == null) {
                MicrosoftLogin.field2277.accept(null);
            } else {
                MicrosoftLogin.field2277.accept(var5.field2279);
            }
        }

        private void method1319(HttpExchange var1, String var2) throws IOException {
            OutputStream var3 = var1.getResponseBody();
            var1.sendResponseHeaders(200, var2.length());
            var3.write(var2.getBytes(StandardCharsets.UTF_8));
            var3.flush();
            var3.close();
        }
    }

    class GameOwnershipResponse {
        class Item {
            @SerializedName("name")
            private String name;

            private Item() {
            }
        }


        @SerializedName("items")
        private Item[] field2280;

        private GameOwnershipResponse() {
        }

        private boolean method1317() {
            boolean var4 = false;
            boolean var5 = false;

            for (Item var9 : this.field2280) {
                if (var9.name.equals("product_minecraft")) {
                    var4 = true;
                } else if (var9.name.equals("game_minecraft")) {
                    var5 = true;
                }
            }

            return var4 && var5;
        }
    }


    class XblXstsResponse {
        class DisplayClaims {
            class Claim {
                @SerializedName("uhs")
                private String field2291;

                private Claim() {
                }
            }


            @SerializedName("xui")
            private Claim[] field2290;

            private DisplayClaims() {
            }
        }


        @SerializedName("Token")
        public String field2288;
        @SerializedName("DisplayClaims")
        public DisplayClaims field2289;

        private XblXstsResponse() {
        }
    }

    public static class LoginData {
        public String field2281;
        public String field2282;
        public String field2283;
        public String field2284;

        public LoginData() {
        }

        public LoginData(String mcToken, String newRefreshToken, String uuid, String username) {
            this.field2281 = mcToken;
            this.field2282 = newRefreshToken;
            this.field2283 = uuid;
            this.field2284 = username;
        }

        public boolean method1320() {
            return this.field2281 != null;
        }
    }

    class McResponse {
        @SerializedName("access_token")
        public String field2285;

        private McResponse() {
        }
    }

    class ProfileResponse {
        @SerializedName("id")
        public String field2286;
        @SerializedName("name")
        public String field2287;

        private ProfileResponse() {
        }
    }


    class AuthTokenResponse {
        @SerializedName("access_token")
        public String field2278;
        @SerializedName("refresh_token")
        public String field2279;

        private AuthTokenResponse() {
        }
    }


    private static final String field2274 = "2cd47301-d033-4784-8f9c-a616ff9070ec";
    private static final int field2275 = 30222;
    private static HttpServer field2276;
    private static Consumer<String> field2277;

    public static void method1316(Consumer<String> callback) {
        field2277 = callback;
        startServer();
        Util.getOperatingSystem()
                .open(
                        "https://login.live.com/oauth20_authorize.srf?client_id=2cd47301-d033-4784-8f9c-a616ff9070ec&response_type=code&redirect_uri=http://127.0.0.1:30222&scope=XboxLive.signin%20offline_access&prompt=select_account"
                );
    }

    public static LoginData login(String refreshToken) {
        AuthTokenResponse var4 = HttpUtil.post("https://login.live.com/oauth20_token.srf")
                .method2181(
                        "client_id=2cd47301-d033-4784-8f9c-a616ff9070ec&refresh_token=" + refreshToken + "&grant_type=refresh_token&redirect_uri=http://127.0.0.1:30222"
                )
                .method2189(AuthTokenResponse.class);
        if (var4 == null) {
            return new LoginData();
        } else {
            String var5 = var4.field2278;
            short var10000 = 5199;
            refreshToken = var4.field2279;
            XblXstsResponse var6 = HttpUtil.post("https://user.auth.xboxlive.com/user/authenticate")
                    .method2182(
                            "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d="
                                    + var5
                                    + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}"
                    )
                    .method2189(XblXstsResponse.class);
            if (var6 == null) {
                return new LoginData();
            } else {
                XblXstsResponse var7 = HttpUtil.post("https://xsts.auth.xboxlive.com/xsts/authorize")
                        .method2182(
                                "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\""
                                        + var6.field2288
                                        + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}"
                        )
                        .method2189(XblXstsResponse.class);
                if (var7 == null) {
                    return new LoginData();
                } else {
                    McResponse var8 = HttpUtil.post("https://api.minecraftservices.com/authentication/login_with_xbox")
                            .method2182("{\"identityToken\":\"XBL3.0 x=" + var6.field2289.field2290[0].field2291 + ";" + var7.field2288 + "\"}")
                            .method2189(McResponse.class);
                    if (var8 == null) {
                        return new LoginData();
                    } else {
                        GameOwnershipResponse var9 = HttpUtil.get("https://api.minecraftservices.com/entitlements/mcstore")
                                .method2178(var8.field2285)
                                .method2189(GameOwnershipResponse.class);
                        if (var9 != null && var9.method1317()) {
                            ProfileResponse var10 = HttpUtil.get("https://api.minecraftservices.com/minecraft/profile")
                                    .method2178(var8.field2285)
                                    .method2189(ProfileResponse.class);
                            return var10 == null ? new LoginData() : new LoginData(var8.field2285, refreshToken, var10.field2286, var10.field2287);
                        } else {
                            return new LoginData();
                        }
                    }
                }
            }
        }
    }

    private static void startServer() {
        if (field2276 == null) {
            try {
                field2276 = HttpServer.create(new InetSocketAddress("127.0.0.1", 30222), 0);
                field2276.createContext("/", new Handler());
                field2276.setExecutor(BozeExecutor.field3948);
                field2276.start();
            } catch (IOException var4) {
                ErrorLogger.log(var4);
            }
        }
    }

    private static void stopServer() {
        if (field2276 != null) {
            field2276.stop(0);
            field2276 = null;
            field2277 = null;
        }
    }
}
