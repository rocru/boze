package dev.boze.client.auth;

import dev.boze.client.core.ErrorLogger;
import dev.boze.client.core.Version;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.utils.IMinecraft;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import net.minecraft.SharedConstants;

public class ShaderCheck implements IMinecraft {
   public static String method970(String path, boolean[] blocks) {
      try {
         Socket var5 = new Socket("auth.boze.dev", 3000);
         DataInputStream var6 = new DataInputStream(var5.getInputStream());
         DataOutputStream var7 = new DataOutputStream(var5.getOutputStream());
         var7.writeUTF(Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest("client0x0002".getBytes())));
         String var8 = var6.readUTF();
         String var9 = var6.readUTF();
         String var10 = method971(var9, var8);
         var7.writeUTF(method971(ConfigManager.field2138, var10));
         String var10001 = SharedConstants.getGameVersion().getName();
         String var15 = Version.isBeta ? "-beta" : "";
          var7.writeUTF(method971("boze-" + var10001 + var15, var10));
         var7.writeUTF(method971(path, var10));
         var7.writeInt(blocks.length);

         for (boolean var14 : blocks) {
            var7.writeBoolean(var14);
         }

         String var18 = method971(var6.readUTF(), var10);
         if (var18.equals("error")) {
            return "";
         } else {
            var5.close();
            return var18;
         }
      } catch (NoSuchAlgorithmException | IOException var17) {
         ErrorLogger.log(var17);
         return "";
      }
   }

   public static String method971(String input, String cipherKey) {
      StringBuilder var5 = new StringBuilder();

      for (int var6 = 0; var6 < input.length(); var6++) {
         var5.append((char)(input.charAt(var6) ^ cipherKey.charAt(var6 % cipherKey.length())));
      }

      return var5.toString();
   }
}
