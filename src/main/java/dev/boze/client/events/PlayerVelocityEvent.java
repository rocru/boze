package dev.boze.client.events;

public class PlayerVelocityEvent extends CancelableEvent {
   private static final PlayerVelocityEvent INSTANCE = new PlayerVelocityEvent();
   public boolean field1898;

   public static PlayerVelocityEvent method1048(boolean liquid) {
      INSTANCE.field1898 = liquid;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
