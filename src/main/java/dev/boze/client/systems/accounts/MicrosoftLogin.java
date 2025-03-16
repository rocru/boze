package dev.boze.client.systems.accounts;

import com.sun.net.httpserver.HttpServer;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.systems.accounts.MicrosoftLogin.AuthTokenResponse;
import dev.boze.client.systems.accounts.MicrosoftLogin.GameOwnershipResponse;
import dev.boze.client.systems.accounts.MicrosoftLogin.Handler;
import dev.boze.client.systems.accounts.MicrosoftLogin.LoginData;
import dev.boze.client.systems.accounts.MicrosoftLogin.McResponse;
import dev.boze.client.systems.accounts.MicrosoftLogin.ProfileResponse;
import dev.boze.client.systems.accounts.MicrosoftLogin.XblXstsResponse;
import dev.boze.client.utils.http.HttpUtil;
import dev.boze.client.utils.network.BozeExecutor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;
import net.minecraft.util.Util;

public class MicrosoftLogin {
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
         .<AuthTokenResponse>method2189(AuthTokenResponse.class);
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
            .<XblXstsResponse>method2189(XblXstsResponse.class);
         if (var6 == null) {
            return new LoginData();
         } else {
            XblXstsResponse var7 = HttpUtil.post("https://xsts.auth.xboxlive.com/xsts/authorize")
               .method2182(
                  "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\""
                     + var6.field2288
                     + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}"
               )
               .<XblXstsResponse>method2189(XblXstsResponse.class);
            if (var7 == null) {
               return new LoginData();
            } else {
               McResponse var8 = HttpUtil.post("https://api.minecraftservices.com/authentication/login_with_xbox")
                  .method2182("{\"identityToken\":\"XBL3.0 x=" + var6.field2289.field2290[0].field2291 + ";" + var7.field2288 + "\"}")
                  .<McResponse>method2189(McResponse.class);
               if (var8 == null) {
                  return new LoginData();
               } else {
                  GameOwnershipResponse var9 = HttpUtil.get("https://api.minecraftservices.com/entitlements/mcstore")
                     .method2178(var8.field2285)
                     .<GameOwnershipResponse>method2189(GameOwnershipResponse.class);
                  if (var9 != null && var9.method1317()) {
                     ProfileResponse var10 = HttpUtil.get("https://api.minecraftservices.com/minecraft/profile")
                        .method2178(var8.field2285)
                        .<ProfileResponse>method2189(ProfileResponse.class);
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
