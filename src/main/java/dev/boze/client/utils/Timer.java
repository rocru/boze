package dev.boze.client.utils;

public final class Timer {
   private long field3929 = System.currentTimeMillis();

   public boolean hasElapsed(double ms) {
      return (double)(System.currentTimeMillis() - this.field3929) >= ms;
   }

   public void reset() {
      this.field3929 = System.currentTimeMillis();
   }

   public long getLastTime() {
      return this.field3929;
   }

   public long getElapsedTime() {
      return System.currentTimeMillis() - this.field3929;
   }

   public void setLastTime(long time) {
      this.field3929 = time;
   }
}
