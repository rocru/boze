package dev.boze.client.events;

import net.minecraft.client.sound.SoundInstance;

public class SoundPlayEvent extends CancelableEvent {
   private static SoundPlayEvent INSTANCE = new SoundPlayEvent();
   public SoundInstance sound;

   public static SoundPlayEvent method1095(SoundInstance instance) {
      INSTANCE.sound = instance;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
