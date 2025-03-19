package dev.boze.client.instances.impl;

import com.google.gson.*;
import dev.boze.api.addon.Addon;
import dev.boze.api.config.Serializable;
import dev.boze.api.internal.interfaces.IJson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonInstance implements IJson {
   public boolean saveObject(Addon addon, String fileName, Serializable<?> object) {
      GsonBuilder var6 = new GsonBuilder();
      Gson var7 = var6.setPrettyPrinting().create();

      FileWriter var8;
      try {
         var8 = new FileWriter(new File(addon.getDir(), fileName + ".json"));
      } catch (IOException var15) {
         return false;
      }

      try {
         JsonParser var9 = new JsonParser();
         JsonElement var10 = var9.parse(object.toJson().toString());
         String var11 = var7.toJson(var10);
         var8.write(var11);
      } catch (IOException var14) {
         try {
            var8.flush();
            var8.close();
         } catch (IOException var12) {
         }

         return false;
      }

      try {
         var8.flush();
         var8.close();
         return true;
      } catch (IOException var13) {
         return false;
      }
   }

   public <T extends Serializable<T>> T loadObject(Addon addon, String fileName, Serializable<T> object) {
      try {
         JsonParser var6 = new JsonParser();
         FileReader var7 = new FileReader(new File(addon.getDir(), fileName + ".json"));
         JsonObject var8 = (JsonObject)var6.parse(var7);
         object.fromJson(var8);
         return (T)object;
      } catch (Exception var9) {
         return null;
      }
   }
}
