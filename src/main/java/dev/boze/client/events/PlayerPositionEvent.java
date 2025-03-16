package dev.boze.client.events;

public class PlayerPositionEvent {
   private static final PlayerPositionEvent field1939 = new PlayerPositionEvent();
   private double field1940;

   public static PlayerPositionEvent method1081(double stepHeight) {
      field1939.field1940 = stepHeight;
      return field1939;
   }

   public double method1082() {
      return this.field1940;
   }
}
