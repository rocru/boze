package dev.boze.client.font;

import dev.boze.client.core.ErrorLogger;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FontReader {
   public static byte[] read(File file) {
      try {
         FileInputStream var4 = new FileInputStream(file);
         ByteArrayOutputStream var5 = new ByteArrayOutputStream();
         byte[] var6 = new byte[256];

         int var7;
         while ((var7 = var4.read(var6)) > 0) {
            var5.write(var6, 0, var7);
         }

         var4.close();
         return var5.toByteArray();
      } catch (IOException var8) {
         ErrorLogger.log(var8);
         return new byte[0];
      }
   }
}
