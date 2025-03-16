package dev.boze.client.systems.modules.misc;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class MiningTweaks extends Module {
   public static final MiningTweaks INSTANCE = new MiningTweaks();
   public final BooleanSetting noDesync = new BooleanSetting(
      "NoDesync", true, "Don't set block to air client-side when done mining\nAwait server's air packet\n"
   );
   public final FloatSetting speed = new FloatSetting("Speed", 1.0F, 0.1F, 10.0F, 0.1F, "Mining speed modifier (SpeedMine)");

   public MiningTweaks() {
      super("MiningTweaks", "Various tweaks for mining", Category.Misc);
   }
}
