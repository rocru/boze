package dev.boze.client.events;

public class MouseUpdateEvent extends CancelableEvent {
   private static final MouseUpdateEvent INSTANCE = new MouseUpdateEvent();
   public double deltaX;
   public double deltaY;

   public static MouseUpdateEvent method1073(double deltaX, double deltaY) {
      INSTANCE.deltaX = deltaX;
      INSTANCE.deltaY = deltaY;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
