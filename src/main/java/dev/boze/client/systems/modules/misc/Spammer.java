package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ListSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import meteordevelopment.orbit.EventHandler;

public class Spammer extends Module {
   public static final Spammer INSTANCE = new Spammer();
   private final ListSetting field3123 = new ListSetting("Files", new ArrayList(), "Spammer files");
   private final MinMaxSetting field3124 = new MinMaxSetting("Delay", 5.0, 0.1, 100.0, 0.1, "Delay in seconds for sending messages");
   private final BooleanSetting field3125 = new BooleanSetting("Random", false, "Send messages in random order");
   private final BooleanSetting field3126 = new BooleanSetting("Numbers", false, "Append random numbers to the end of messages");
   private final ArrayList<String> field3127 = new ArrayList();
   private int field3128 = 0;
   private final dev.boze.client.utils.Timer field3129 = new dev.boze.client.utils.Timer();
   private final Random field3130 = new Random();

   public Spammer() {
      super("Spammer", "Spams messages in chat", Category.Misc);
   }

   @EventHandler
   private void method1776(PostPlayerTickEvent var1) {
      if (this.field3129.hasElapsed(this.field3124.getValue() * 1000.0) && !this.field3123.method2120().isEmpty()) {
         if (this.field3123.method2118()) {
            this.field3128 = 0;
            this.field3127.clear();

            for (String var6 : this.field3123.method2120()) {
               File var7 = new File(ConfigManager.spammer, var6 + ".txt");
               if (var7.exists()) {
                  try {
                     BufferedReader var8 = new BufferedReader(new FileReader(var7));

                     String var9;
                     while ((var9 = var8.readLine()) != null) {
                        this.field3127.add(var9);
                     }
                  } catch (Exception var10) {
                  }
               }
            }

            this.field3123.method206(false);
         }

         if (!this.field3127.isEmpty()) {
            String var11;
            if (this.field3125.method419()) {
               int var12 = this.field3130.nextInt(this.field3127.size());
               var11 = (String)this.field3127.get(var12);
            } else {
               if (this.field3128 >= this.field3127.size()) {
                  this.field3128 = 0;
               }

               var11 = (String)this.field3127.get(this.field3128);
               this.field3128++;
            }

            if (this.field3126.method419()) {
               var11 = var11 + " " + this.field3130.nextInt(1000000);
            }

            ChatInstance.method1800(var11);
            this.field3129.reset();
         }
      }
   }
}
