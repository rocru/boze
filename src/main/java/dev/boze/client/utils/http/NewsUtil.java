package dev.boze.client.utils.http;

import dev.boze.client.core.ErrorLogger;
import dev.boze.client.manager.ConfigManager;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class NewsUtil {
   public static List<String> getNews() {
      try {
         URL var2 = new URL("https://news.boze.dev/get_stories");
         HttpURLConnection var3 = (HttpURLConnection)var2.openConnection();
         var3.setRequestMethod("GET");
         var3.setDoOutput(true);
         var3.setUseCaches(false);
         var3.setRequestProperty("User-Agent", "Boze");
         var3.setRequestProperty("Token", ConfigManager.field2138);
         String var4 = IOUtils.toString(var3.getInputStream());
         var3.disconnect();
         return new ArrayList(Arrays.asList(var4.split("\n")));
      } catch (IOException var5) {
         ErrorLogger.log(var5);
         return null;
      }
   }

   public static String method2190(String url) throws NoSuchAlgorithmException {
      MessageDigest var4 = MessageDigest.getInstance("MD5");
      var4.update(url.getBytes());
      byte[] var5 = var4.digest();
      BigInteger var6 = new BigInteger(1, var5);
      String var7 = var6.toString(16);

      while (var7.length() < 32) {
         var7 = "0" + var7;
      }

      return var7;
   }
}
