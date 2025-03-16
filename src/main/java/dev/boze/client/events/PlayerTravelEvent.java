package dev.boze.client.events;

public class PlayerTravelEvent extends CancelableEvent {
   private static final PlayerTravelEvent INSTANCE = new PlayerTravelEvent();

   public static PlayerTravelEvent method1047() {
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
