package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.systems.modules.hud.ColorHUDModule;
import mapped.Class5914;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;

public class ChestCount extends ColorHUDModule {
   public static final ChestCount INSTANCE = new ChestCount();
   private int field612;

   public ChestCount() {
      super("ChestCount", "Shows chest count in visual range");
   }

   @Override
   protected String method1562() {
      return Integer.toString(this.field612);
   }

   @Override
   protected String method1563() {
      return this.field612 == 1 ? " chest" : " chests";
   }

   @EventHandler
   private void method2041(MovementEvent var1) {
      this.field612 = 0;

      for (BlockEntity var6 : Class5914.method19()) {
         if (var6 instanceof ChestBlockEntity) {
            this.field612++;
         }
      }
   }
}
