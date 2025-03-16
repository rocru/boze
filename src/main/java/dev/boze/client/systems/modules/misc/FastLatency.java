package dev.boze.client.systems.modules.misc;

import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.LatencyTracker;

public class FastLatency extends Module {
   public static final FastLatency INSTANCE = new FastLatency();

   public FastLatency() {
      super("FastLatency", "Checks your ping more often\nFor anarchy only, this may flag on legit servers", Category.Misc);
   }

   @Override
   public String method1322() {
      return String.valueOf(LatencyTracker.INSTANCE.field1308);
   }
}
