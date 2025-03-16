package dev.boze.client.systems.pathfinding;

public record PathRules() {
   private final boolean flying;
   private final boolean jesus;
   private final boolean noFall;
   private final boolean boat;

   public PathRules(boolean flying, boolean jesus, boolean noFall, boolean boat) {
      this.flying = flying;
      this.jesus = jesus;
      this.noFall = noFall;
      this.boat = boat;
   }

   public boolean method1475() {
      return this.flying;
   }

   public boolean method1476() {
      return this.jesus;
   }

   public boolean method1477() {
      return this.noFall;
   }

   public boolean method1478() {
      return this.boat;
   }
}
