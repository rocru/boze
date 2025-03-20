package dev.boze.client.systems.modules.client;

import dev.boze.client.core.Version;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;

import java.time.Instant;

public class DiscordPresence extends Module {
   public static DiscordPresence INSTANCE = new DiscordPresence();
   private RichPresence field2344;

   public DiscordPresence() {
      super("DiscordPresence", "Boze Discord Presence", Category.Client);
   }

   @Override
   public void onEnable() {
      if (!DiscordIPC.method2283() && !DiscordIPC.method2282(Version.isBeta ? 1085603982304608337L : 853740617964322826L, DiscordPresence::lambda$onEnable$0)) {
         this.setEnabled(false);
      } else {
         this.field2344 = new RichPresence();
         if (Version.isBeta) {
            this.field2344.method2291("Boze Client Beta");
            this.field2344.method2292("https://boze.dev/");
            this.field2344.method2293("boze-beta", "Boze Beta");
         } else {
            this.field2344.method2291("Boze Client");
            this.field2344.method2292("https://boze.dev/");
            this.field2344.method2293("boze-logo", "Boze Client");
         }

         this.field2344.method2295(Instant.now().getEpochSecond());
         DiscordIPC.method2285(this.field2344);
      }
   }

   @Override
   public void onDisable() {
      DiscordIPC.method2286();
   }

   public void method1336(String details) {
      if (this.field2344 != null) {
         this.field2344.method2291(details);
      }
   }

   public void method1337(String state) {
      if (this.field2344 != null) {
         this.field2344.method2292(state);
      }
   }

   public void method1338(String key, String text) {
      if (this.field2344 != null) {
         this.field2344.method2293(key, text);
      }
   }

   public void method1339(String key, String text) {
      if (this.field2344 != null) {
         this.field2344.method2294(key, text);
      }
   }

   private static void lambda$onEnable$0() {
      System.out.println("Started Discord Presence Controller");
   }
}
