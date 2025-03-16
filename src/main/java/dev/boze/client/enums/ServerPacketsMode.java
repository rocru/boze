package dev.boze.client.enums;

public enum ServerPacketsMode {
   Tick,
   Second;

   private static final ServerPacketsMode[] field1773 = method884();

   private static ServerPacketsMode[] method884() {
      return new ServerPacketsMode[]{Tick, Second};
   }
}
