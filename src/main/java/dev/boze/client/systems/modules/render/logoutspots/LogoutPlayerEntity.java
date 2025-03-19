package dev.boze.client.systems.modules.render.logoutspots;

import dev.boze.client.systems.modules.render.LogoutSpots;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class LogoutPlayerEntity {
   public final Vec3d field3586;
   public final double field3587;
   public final double field3588;
   public final UUID field3589;
   public final String field3590;
   public final float field3591;
   private LogoutSpotEntity field3592;
   final LogoutSpots field3593;

   LogoutPlayerEntity(LogoutSpots var1, PlayerEntity var2) {
      this.field3593 = var1;
      this.field3586 = var2.getPos();
      this.field3587 = (double)var2.getWidth();
      this.field3588 = (double)var2.getHeight();
      this.field3589 = var2.getUuid();
      this.field3590 = var2.getNameForScoreboard();
      this.field3591 = var2.getHealth() + var2.getAbsorptionAmount();
      this.field3592 = new LogoutSpotEntity(var1, var2);
   }
}
