package dev.boze.client.systems.modules.combat.surround;

import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.systems.modules.combat.Surround;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.TrapUtil;
import java.util.Arrays;
import java.util.HashSet;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

class AutoEnable implements IMinecraft {
   @EventHandler(
      priority = 10000
   )
   public void method1540(ACRotationEvent event) {
      if (!Surround.INSTANCE.isEnabled() && Surround.INSTANCE.autoEnable.getValue()) {
         Box var5 = Surround.INSTANCE.field2571.method1953();
         BlockPos[] var6 = TrapUtil.method587(var5);
         HashSet var7 = new HashSet(Arrays.asList(var6));
         int var8 = this.method1541(var7.size());

         for (BlockPos var10 : var7) {
            if (mc.world.getBlockState(var10).getBlock().getBlastResistance() >= 600.0F) {
               if (--var8 <= 0) {
                  Surround.INSTANCE.setEnabled(true);
                  return;
               }
            }
         }
      }
   }

   private int method1541(int var1) {
      if (var1 == 4) {
         return Surround.INSTANCE.min1x1.getValue();
      } else {
         return var1 == 6 ? Surround.INSTANCE.min2x1.getValue() : Surround.INSTANCE.min2x2.getValue();
      }
   }
}
