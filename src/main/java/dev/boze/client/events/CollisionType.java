package dev.boze.client.events;

public enum CollisionType {
   BLOCK,
   FLUID;

   private static final CollisionType[] field1790 = method901();

   private static CollisionType[] method901() {
      return new CollisionType[]{BLOCK, FLUID};
   }
}
