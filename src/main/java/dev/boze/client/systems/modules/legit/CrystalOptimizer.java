package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.CrystalOptimizerMode;
import dev.boze.client.events.PostAttackEntityEvent;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.decoration.EndCrystalEntity;

public class CrystalOptimizer extends Module {
   public static final CrystalOptimizer INSTANCE = new CrystalOptimizer();
   public final EnumSetting<CrystalOptimizerMode> field2789 = new EnumSetting<CrystalOptimizerMode>(
      "Mode",
      CrystalOptimizerMode.EntityTrace,
      "Mode for the crystal optimizer\n - EntityTrace: Lets you right-click through the crystal\n - SetDead: Kills the crystal client-side\n"
   );

   private CrystalOptimizer() {
      super("CrystalOptimizer", "Lets you re-place crystals faster", Category.Legit);
   }

   @EventHandler
   public void method1602(PostAttackEntityEvent event) {
      if (event.entity instanceof EndCrystalEntity) {
         if (this.field2789.getValue() == CrystalOptimizerMode.SetDead) {
            event.entity.kill();
         } else if (this.field2789.getValue() == CrystalOptimizerMode.EntityTrace) {
            ((IEndCrystalEntity)event.entity).boze$setLastAttackTime(System.currentTimeMillis());
         }
      }
   }
}
