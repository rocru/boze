package dev.boze.client.utils.render;

import com.google.gson.Gson;
import dev.boze.client.systems.accounts.TexturesJson;
import dev.boze.client.systems.accounts.UuidToProfileResponse;
import dev.boze.client.utils.http.HttpUtil;
import java.util.Base64;
import java.util.UUID;

public class PlayerHeadUtils {
   public static PlayerHeadTexture field3957;

   public static void method2229() {
      field3957 = new PlayerHeadTexture();
   }

   public static PlayerHeadTexture method2230(UUID id) {
      if (id == null) {
         return null;
      } else {
         String var4 = method2231(id);
         return var4 != null ? new PlayerHeadTexture(var4) : null;
      }
   }

   public static String method2231(UUID id) {
      UuidToProfileResponse var4 = HttpUtil.get("https://sessionserver.mojang.com/session/minecraft/profile/" + id)
         .<UuidToProfileResponse>method2189(UuidToProfileResponse.class);
      if (var4 == null) {
         return null;
      } else {
         String var5 = var4.method1321("textures");
         if (var5 == null) {
            return null;
         } else {
            TexturesJson var6 = (TexturesJson)new Gson().fromJson(new String(Base64.getDecoder().decode(var5)), TexturesJson.class);
            return var6.field2292.field2293 == null ? null : var6.field2292.field2293.field2294;
         }
      }
   }
}
