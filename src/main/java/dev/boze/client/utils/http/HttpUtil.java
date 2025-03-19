package dev.boze.client.utils.http;

import com.google.gson.Gson;

import java.net.http.HttpClient;

public class HttpUtil {
   private static final HttpClient field3945 = HttpClient.newHttpClient();
   private static final Gson field3946 = new Gson();

   public static HttpRequestWrapper get(String url) {
      return new HttpRequestWrapper(RequestType.GET, url);
   }

   public static HttpRequestWrapper post(String url) {
      return new HttpRequestWrapper(RequestType.POST, url);
   }
}
