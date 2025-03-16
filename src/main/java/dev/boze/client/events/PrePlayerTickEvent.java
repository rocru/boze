package dev.boze.client.events;

import net.minecraft.client.network.ClientPlayerEntity;

public class PrePlayerTickEvent extends PlayerTickEvent {
   private static final PrePlayerTickEvent INSTANCE = new PrePlayerTickEvent();

   public static PrePlayerTickEvent method1090(ClientPlayerEntity player) {
      INSTANCE.field1941 = player;
      return INSTANCE;
   }
}
