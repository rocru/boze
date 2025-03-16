package dev.boze.client.systems.accounts;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.boze.client.systems.accounts.MicrosoftLogin.AuthTokenResponse;
import dev.boze.client.utils.http.HttpUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

class MicrosoftLogin$Handler implements HttpHandler {
   private MicrosoftLogin$Handler() {
   }

   public void handle(HttpExchange req) throws IOException {
      if (req.getRequestMethod().equals("GET")) {
         List var5 = URLEncodedUtils.parse(req.getRequestURI(), StandardCharsets.UTF_8.name());
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
         .<AuthTokenResponse>method2189(AuthTokenResponse.class);
      if (var5 == null) {
         MicrosoftLogin.field2277.accept(null);
      } else {
         MicrosoftLogin.field2277.accept(var5.field2279);
      }
   }

   private void method1319(HttpExchange var1, String var2) throws IOException {
      OutputStream var3 = var1.getResponseBody();
      var1.sendResponseHeaders(200, (long)var2.length());
      var3.write(var2.getBytes(StandardCharsets.UTF_8));
      var3.flush();
      var3.close();
   }
}
