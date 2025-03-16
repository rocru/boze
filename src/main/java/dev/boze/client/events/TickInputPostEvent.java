package dev.boze.client.events;

public class TickInputPostEvent {
   private static final TickInputPostEvent field1952 = new TickInputPostEvent();
   public float field1953;
   public float field1954;
   public boolean field1955;
   public boolean field1956;

   public static TickInputPostEvent method1096(float movementSideways, float movementForward, boolean jumping, boolean sneaking) {
      field1952.field1953 = movementSideways;
      field1952.field1954 = movementForward;
      field1952.field1955 = jumping;
      field1952.field1956 = sneaking;
      return field1952;
   }
}
