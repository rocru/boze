package dev.boze.client.enums;

public enum InteractionMode {
   NCP(AnticheatMode.NCP),
   Grim(AnticheatMode.Grim),
   Ghost(AnticheatMode.Grim);

   public final AnticheatMode interactMode;

   private InteractionMode(AnticheatMode var3) {
      this.interactMode = var3;
   }

   // $VF: synthetic method
   private static InteractionMode[] method11() {
      return new InteractionMode[]{NCP, Grim, Ghost};
   }
}
