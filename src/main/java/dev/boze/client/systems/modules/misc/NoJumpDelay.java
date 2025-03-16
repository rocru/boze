package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PostTickEvent;
import dev.boze.client.mixin.LivingEntityAccessor;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;

public class NoJumpDelay extends Module {
   public static final NoJumpDelay INSTANCE = new NoJumpDelay();

   private NoJumpDelay() {
      super("NoJumpDelay", "Removes the delay when jumping\nSome servers may have checks for this\nUse at your own risk\n", Category.Misc);
      this.field435 = true;
   }

   @EventHandler
   private void method1729(PostTickEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         ((LivingEntityAccessor)mc.player).setJumpCooldown(0);
      }
   }
}
