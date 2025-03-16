package dev.boze.client.utils.files;

import dev.boze.client.core.ErrorLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
   public static void copyFile(File from, File to) {
      try {
         FileInputStream var2 = new FileInputStream(from);
         FileOutputStream var3 = new FileOutputStream(to);
         copyStream(var2, var3);
         var2.close();
         var3.close();
      } catch (IOException var4) {
         ErrorLogger.log(var4);
      }
   }

   public static void copyStream(InputStream in, File to) {
      try {
         FileOutputStream var2 = new FileOutputStream(to);
         copyStream(in, var2);
         in.close();
         var2.close();
      } catch (IOException var3) {
         ErrorLogger.log(var3);
      }
   }

   public static void copyStream(InputStream in, OutputStream out) {
      byte[] var5 = new byte[512];

      int var6;
      try {
         while ((var6 = in.read(var5)) != -1) {
            out.write(var5, 0, var6);
         }
      } catch (IOException var8) {
         ErrorLogger.log(var8);
      }
   }
}
