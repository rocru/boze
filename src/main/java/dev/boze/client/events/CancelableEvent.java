package dev.boze.client.events;

public class CancelableEvent {
   private boolean field1887 = false;

   public void method1020() {
      this.field1887 = true;
   }

   public void method1021(boolean cancelled) {
      this.field1887 = cancelled;
   }

   public boolean method1022() {
      return this.field1887;
   }
}
