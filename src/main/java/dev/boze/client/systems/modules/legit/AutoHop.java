package dev.boze.client.systems.modules.legit;

import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.mixininterfaces.IEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.SlabBlock;

public class AutoHop extends Module {
   public static final AutoHop INSTANCE = new AutoHop();
   private final BooleanSetting field2752 = new BooleanSetting("NoSlabs", false, "Don't hop on slabs\nOn some servers, this can cause you to fly\n");

   private AutoHop() {
      super("AutoHop", "Automatically hops for you", Category.Legit);
   }

   @EventHandler
   public void method1588(TickInputPostEvent event) {
      if (!mc.player.isInLava() && !((IEntity)mc.player).boze$isInWater() && (event.field1954 != 0.0F || event.field1953 != 0.0F)) {
         if (mc.player.getAbilities().allowFlying && !mc.player.isOnGround()) {
            return;
         }

         if (this.field2752.method419() && mc.world.getBlockState(mc.player.getBlockPos()).getBlock() instanceof SlabBlock) {
            return;
         }

         event.field1955 = true;
      }
   }
}
