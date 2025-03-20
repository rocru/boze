package dev.boze.client.systems.modules.hud.graph;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.systems.modules.GraphHUDModule;
import meteordevelopment.orbit.EventHandler;

public class Health extends GraphHUDModule {
   public static final Health INSTANCE = new Health();

   public Health() {
      super("Health", "Graphs your health");
      this.field2300.setValue(true);
   }

   @EventHandler
   public void method1570(MovementEvent event) {
      this.method1324((double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()));
   }
}
