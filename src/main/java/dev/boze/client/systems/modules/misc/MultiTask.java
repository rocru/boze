package dev.boze.client.systems.modules.misc;

import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class MultiTask extends Module {
   public static final MultiTask INSTANCE = new MultiTask();

   public MultiTask() {
      super("MultiTask", "Lets you mine and eat at the same time", Category.Misc);
   }
}
