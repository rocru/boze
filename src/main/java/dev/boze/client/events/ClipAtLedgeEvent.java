package dev.boze.client.events;

public class ClipAtLedgeEvent extends CancelableEvent {
   private static ClipAtLedgeEvent INSTANCE = new ClipAtLedgeEvent();

   public static ClipAtLedgeEvent method1055() {
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
