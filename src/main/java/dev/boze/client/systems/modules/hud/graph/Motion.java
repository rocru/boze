package dev.boze.client.systems.modules.hud.graph;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.GraphHUDModule;
import meteordevelopment.orbit.EventHandler;

public class Motion extends GraphHUDModule {
   public static final Motion INSTANCE = new Motion();
   private final BooleanSetting field2675 = new BooleanSetting("Vertical", true, "Graph y velocity");

   public Motion() {
      super("Motion", "Graphs your speed");
   }

   @EventHandler
   public void method1571(MovementEvent event) {
      double var5 = mc.player.getX() - mc.player.prevX;
      double var7 = mc.player.getY() - mc.player.prevY;
      double var9 = mc.player.getZ() - mc.player.prevZ;
      if (this.field2675.method419()) {
         this.method1324(Math.sqrt(var5 * var5 + var7 * var7 + var9 * var9));
      } else {
         this.method1324(Math.sqrt(var5 * var5 + var9 * var9));
      }
   }
}
