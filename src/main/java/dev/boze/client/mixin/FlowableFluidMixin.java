package dev.boze.client.mixin;

import dev.boze.client.events.PlayerVelocityEvent;
import java.util.Iterator;
import dev.boze.client.Boze;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FlowableFluid.class})
public class FlowableFluidMixin {
   @Redirect(
      method = {"getVelocity"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/util/Iterator;hasNext()Z",
         ordinal = 0
      )
   )
   public boolean onGetVelocity(Iterator<Direction> var9) {
      PlayerVelocityEvent var2 = (PlayerVelocityEvent) Boze.EVENT_BUS.post(PlayerVelocityEvent.method1048(true));
      return var2.method1022() ? false : var9.hasNext();
   }
}
