package dev.boze.client.utils;

import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.events.SoundPlayEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.sound.SoundInstance;

public class PiglinAggressiveness {
   public static boolean field3917 = false;

   @EventHandler
   public static void method2153(SoundPlayEvent event) {
      if (MinecraftUtils.isClientActive()) {
         SoundInstance var4 = event.sound;
         if (var4.getId().getPath().equals("entity.zombified_piglin.angry")) {
            field3917 = true;
         }
      }
   }

   @EventHandler
   public static void method2154(GameJoinEvent event) {
      field3917 = false;
   }
}
