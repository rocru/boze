package dev.boze.client.enums;

import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.systems.modules.movement.boatfly.nj;
import dev.boze.client.systems.modules.movement.boatfly.nk;
import dev.boze.client.systems.modules.movement.boatfly.nl;

enum BoatFlyMode {
   Grim,
   NCP;

   private nj field1792;

   nj method903(BoatFly var1) {
      if (this.field1792 == null) {
         this.field1792 = (nj)(switch (this) {
            case Grim -> new nk(var1);
            case NCP -> new nl(var1);
         });
      }

      return this.field1792;
   }

   // $VF: synthetic method
   private static BoatFlyMode[] method904() {
      return new BoatFlyMode[]{Grim, NCP};
   }
}
