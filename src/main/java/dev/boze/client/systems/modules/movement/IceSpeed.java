package dev.boze.client.systems.modules.movement;

import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class IceSpeed extends Module {
   public static final IceSpeed INSTANCE = new IceSpeed();
   public final FloatSetting field3283 = new FloatSetting("Multiplier", 1.2F, 0.1F, 10.0F, 0.1F, "Multiplier for ice slipperiness");

   public IceSpeed() {
      super("IceSpeed", "Slip faster on ice", Category.Movement);
   }
}
