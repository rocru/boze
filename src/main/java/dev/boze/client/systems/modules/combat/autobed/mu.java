package dev.boze.client.systems.modules.combat.autobed;

import dev.boze.client.systems.modules.combat.AutoBed;
import dev.boze.client.utils.PlaceAction;
import net.minecraft.util.math.BlockPos;

public class mu {
   public final BlockPos field2509;
   public final BlockPos field2510;
   public final PlaceAction field2511;
   final double field2512;
   final AutoBed field2513;

   public mu(final AutoBed this$0, BlockPos pos1, BlockPos pos2, PlaceAction placement, double selfDamage) {
      this.field2513 = this$0;
      this.field2509 = pos1;
      this.field2510 = pos2;
      this.field2511 = placement;
      this.field2512 = selfDamage;
   }
}
