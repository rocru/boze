package dev.boze.client.utils.http;

import com.google.gson.Gson;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.systems.accounts.TexturesJson;
import dev.boze.client.systems.accounts.UuidToProfileResponse;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class SkinUtil {
   public static void method2191(YggdrasilMinecraftSessionService service, String url) {
      try {
         Field var4 = service.getClass().getDeclaredField("baseUrl");
         var4.setAccessible(true);
         var4.set(service, url);
      } catch (NoSuchFieldException | IllegalAccessException var5) {
         ErrorLogger.log(var5);
      }
   }

   public static void method2192(YggdrasilMinecraftSessionService service, String url) {
      try {
         Field var4 = service.getClass().getDeclaredField("joinUrl");
         var4.setAccessible(true);
         var4.set(service, new URL(url));
      } catch (NoSuchFieldException | MalformedURLException | IllegalAccessException var5) {
         ErrorLogger.log(var5);
      }
   }

   public static void method2193(YggdrasilMinecraftSessionService service, String url) {
      try {
         Field var4 = service.getClass().getDeclaredField("checkUrl");
         var4.setAccessible(true);
         var4.set(service, new URL(url));
      } catch (NoSuchFieldException | MalformedURLException | IllegalAccessException var5) {
         ErrorLogger.log(var5);
      }
   }

   public static String method2194(String username) {
      try {
         ProfileResponse var4 = HttpUtil.get("https://api.mojang.com/users/profiles/minecraft/" + username).<ProfileResponse>method2189(ProfileResponse.class);
         if (var4 == null) {
            Log.error(LogCategory.LOG, "Profile res is null");
            return null;
         } else {
            UuidToProfileResponse var5 = HttpUtil.get("https://sessionserver.mojang.com/session/minecraft/profile/" + var4.field3947)
               .<UuidToProfileResponse>method2189(UuidToProfileResponse.class);
            if (var5 == null) {
               Log.error(LogCategory.LOG, "Skin res is null");
               return null;
            } else {
               String var6 = var5.method1321("textures");
               if (var6 == null) {
                  Log.error(LogCategory.LOG, "Textrures is null");
                  return null;
               } else {
                  TexturesJson var7 = (TexturesJson)new Gson().fromJson(new String(Base64.getDecoder().decode(var6)), TexturesJson.class);
                  if (var7.field2292.field2293 == null) {
                     Log.error(LogCategory.LOG, "Skin texture is null");
                     return null;
                  } else {
                     return var7.field2292.field2293.field2294;
                  }
               }
            }
         }
      } catch (Exception var8) {
         ErrorLogger.log(var8);
         return null;
      }
   }
}
