package dev.boze.client.enums;

enum NukerPriority {
   Closest,
   Furthest,
   Highest,
   Random;

   private static final NukerPriority[] field49 = method42();

   private static NukerPriority[] method42() {
      return new NukerPriority[]{Closest, Furthest, Highest, Random};
   }
}
