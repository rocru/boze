package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;

public class Class5926 implements IMinecraft {
   public static float method99(LivingEntity entity) {
      return var105 == null ? 0.0F : var105.getHealth() + var105.getAbsorptionAmount();
   }

   public static int method100(PlayerEntity player) {
      if (mc.getNetworkHandler() == null) {
         return 0;
      } else {
         PlayerListEntry var4 = mc.getNetworkHandler().getPlayerListEntry(var106.getUuid());
         return var4 == null ? 0 : var4.getLatency();
      }
   }

   public static GameMode method101(PlayerEntity player) {
      if (var107 == null) {
         return null;
      } else {
         PlayerListEntry var4 = mc.getNetworkHandler().getPlayerListEntry(var107.getUuid());
         return var4 == null ? null : var4.getGameMode();
      }
   }
}
