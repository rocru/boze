package dev.boze.client.events;

public class BossBarSpacingEvent {
   private static final BossBarSpacingEvent field1907 = new BossBarSpacingEvent();
   public int field1908;

   public static BossBarSpacingEvent method1054(int spacing) {
      field1907.field1908 = spacing;
      return field1907;
   }
}
