package dev.boze.client.systems.modules.client;

import dev.boze.client.Boze;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.WeirdSettingString;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.network.BozeExecutor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Chat extends Module {
   public static final Chat INSTANCE = new Chat();
   public final WeirdSettingString field773 = new WeirdSettingString("ChatPrefix", "-", "Prefix for Boze Chat");
   private Queue<String> field774 = new LinkedList();
   private Socket field775 = null;
   private DataInputStream field776 = null;
   private DataOutputStream field777 = null;
   private SecretKey field778 = null;
   private AtomicBoolean field779 = new AtomicBoolean(false);
   private AtomicBoolean field780 = new AtomicBoolean(false);

   public Chat() {
      super("Chat", "Private chat for Boze users", Category.Client);
      this.setEnabled(true);
   }

   @Override
   public void onEnable() {
      this.field779.set(false);
      this.field780.set(false);
   }

   @Override
   public void onDisable() {
      if (this.field775 != null) {
         try {
            this.field775.close();
         } catch (IOException var5) {
         }
      }

      this.field775 = null;
      this.field776 = null;
      this.field777 = null;
      this.field778 = null;
   }

   @EventHandler
   public void method2072(PreTickEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (!this.field780.get()) {
            if (this.field775 == null) {
               this.field780.set(true);
               BozeExecutor.method2200(this::lambda$onClientTick$0);
            }

            if (!this.field779.get()) {
               BozeExecutor.method2200(this::lambda$onClientTick$1);
            }
         }
      }
   }

   private void method1904() throws Exception {
      this.field780.set(true);
      this.field775 = new Socket("auth.boze.dev", 3001);
      this.field775.setSoTimeout(1500);
      this.field776 = new DataInputStream(this.field775.getInputStream());
      this.field777 = new DataOutputStream(this.field775.getOutputStream());
      int var4 = this.field776.readInt();
      byte[] var5 = this.field776.readNBytes(var4);
      KeyFactory var6 = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec var7 = new X509EncodedKeySpec(var5);
      Cipher var8 = Cipher.getInstance("RSA");
      var8.init(1, var6.generatePublic(var7));
      this.field778 = this.method367();
      byte[] var9 = var8.doFinal(this.field778.getEncoded());
      this.field777.writeInt(var9.length);
      this.field777.write(var9);
      this.method368("auth".getBytes(StandardCharsets.UTF_8), this.field777, this.field778);
      this.method368(ConfigManager.field2138.getBytes(StandardCharsets.UTF_8), this.field777, this.field778);
      String var10 = new String(this.method369(this.field776, this.field778));
      if (var10.equals("unauthorized")) {
         this.method1750("Couldn't authorize with the server. Disabling...");
         this.setEnabled(false);
         this.field780.set(false);
      } else if (this.field775 != null && this.field776 != null && this.field777 != null && this.field778 != null) {
         this.field780.set(false);
      } else {
         this.field780.set(false);
         this.setEnabled(false);
         this.field780.set(false);
         throw new Exception("Not all variables are initialized");
      }
   }

   @EventHandler
   private void method1853(PrePacketSendEvent var1) {
      if (var1.packet instanceof ChatMessageC2SPacket var5 && var5.chatMessage().startsWith(this.field773.getValue())) {
         if (var5.chatMessage().length() > 1) {
            this.field774.add(var5.chatMessage().substring(1));
         }

         var1.method1020();
      }
   }

   public void method363(String sender, String recipient, String message) {
      String var8 = recipient.equals("everyone") ? "" : " -> " + recipient;
      String var7 = "<" + sender + var8 + "> ";
      ChatInstance.method744("Chat", Text.literal(var7 + message).formatted(Formatting.AQUA));
   }

   public void method1750(String error) {
      if (MinecraftUtils.isClientActive()) {
         ChatInstance.method743(this.getName(), error);
      } else {
         Boze.LOG.error("[Boze] [Chat] - " + error);
      }
   }

   public void method1337(String info) {
      if (MinecraftUtils.isClientActive()) {
         ChatInstance.method740(this.getName(), info);
      } else {
         Boze.LOG.info("[Boze] [Chat] - " + info);
      }
   }

   private byte[] method365(byte[] var1, SecretKey var2) throws Exception {
      byte[] var5 = new byte[12];
      SecureRandom var6 = new SecureRandom();
      var6.nextBytes(var5);
      Cipher var7 = Cipher.getInstance("AES/GCM/NoPadding");
      SecretKeySpec var8 = new SecretKeySpec(var2.getEncoded(), "AES");
      GCMParameterSpec var9 = new GCMParameterSpec(128, var5);
      var7.init(1, var8, var9);
      byte[] var10 = var7.doFinal(var1);
      byte[] var11 = Arrays.copyOf(var5, var5.length + var10.length);
      System.arraycopy(var10, 0, var11, var5.length, var10.length);
      return var11;
   }

   private byte[] method366(byte[] var1, byte[] var2, SecretKey var3) throws Exception {
      Cipher var6 = Cipher.getInstance("AES/GCM/NoPadding");
      SecretKeySpec var7 = new SecretKeySpec(var3.getEncoded(), "AES");
      GCMParameterSpec var8 = new GCMParameterSpec(128, var1);
      var6.init(2, var7, var8);
      return var6.doFinal(var2);
   }

   private SecretKey method367() throws NoSuchAlgorithmException {
      KeyGenerator var3 = KeyGenerator.getInstance("AES");
      var3.init(256);
      return var3.generateKey();
   }

   private void method368(byte[] var1, DataOutputStream var2, SecretKey var3) throws Exception {
      byte[] var6 = this.method365(var1, var3);
      var2.writeInt(var6.length);
      var2.write(var6);
      var2.flush();
   }

   private byte[] method369(DataInputStream var1, SecretKey var2) throws Exception {
      int var5 = var1.readInt();
      byte[] var6 = var1.readNBytes(var5);
      return this.method366(Arrays.copyOfRange(var6, 0, 12), Arrays.copyOfRange(var6, 12, var6.length), var2);
   }

   private void lambda$onClientTick$1() {
      this.field779.set(true);

      try {
         if (this.field776.available() > 0) {
            String var4 = new String(this.method369(this.field776, this.field778));
            switch (var4) {
               case "message":
                  String var13 = new String(this.method369(this.field776, this.field778));
                  String var8 = new String(this.method369(this.field776, this.field778));
                  String var9 = new String(this.method369(this.field776, this.field778));
                  INSTANCE.method363(var13, var8, var9);
                  break;
               case "error":
                  String var12 = new String(this.method369(this.field776, this.field778));
                  this.method1750(var12);
                  break;
               case "info":
                  String var7 = new String(this.method369(this.field776, this.field778));
                  this.method1337(var7);
            }
         }

         if (!this.field774.isEmpty()) {
            this.method368("message".getBytes(StandardCharsets.UTF_8), this.field777, this.field778);
            this.method368(((String)this.field774.poll()).getBytes(StandardCharsets.UTF_8), this.field777, this.field778);
         }
      } catch (SocketTimeoutException var10) {
         this.method1750("Connection timed out. Disabling...");
         this.setEnabled(false);
      } catch (Exception var11) {
         var11.printStackTrace();
      }

      this.field779.set(false);
   }

   private void lambda$onClientTick$0() {
      try {
         this.method1904();
      } catch (Exception var4) {
         var4.printStackTrace();
         this.method1750("Couldn't connect to the server. Disabling...");
         this.setEnabled(false);
         this.field780.set(false);
      }
   }
}
