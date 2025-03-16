package dev.boze.client.systems.modules.misc;

import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class AutoReconnect extends Module {
   public static final AutoReconnect INSTANCE = new AutoReconnect();
   public final MinMaxSetting delay = new MinMaxSetting("Delay", 5.0, 0.0, 30.0, 0.1, "Delay for reconnecting in seconds");

   public AutoReconnect() {
      super("AutoReconnect", "Automatically reconnect to servers", Category.Misc);
   }
}
