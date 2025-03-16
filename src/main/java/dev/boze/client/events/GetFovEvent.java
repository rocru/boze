package dev.boze.client.events;

public class GetFovEvent {
   private static final GetFovEvent field1921 = new GetFovEvent();
   public double field1922;

   public static GetFovEvent method1063(double fov) {
      field1921.field1922 = fov;
      return field1921;
   }
}
