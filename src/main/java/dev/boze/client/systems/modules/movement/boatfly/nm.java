package dev.boze.client.systems.modules.movement.boatfly;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

class nm implements IMinecraft {
   static boolean method1799(Entity var0, Vec3d var1, double var2) {
      if (var0 != null && var1 != null) {
         BlockPos var7 = BlockPos.ofFloored(var1.x, var1.y, var1.z);
         World var8 = var0.getWorld();

         while (var8.getBlockState(var7).isAir()) {
            var7 = var7.down();
            if (var7.getY() == var8.getBottomY()) {
               break;
            }
         }

         return var1.y - (double)var7.getY() > var2;
      } else {
         return false;
      }
   }

   static void method1800(String var0) {
      if (mc.player != null) {
         mc.player.sendMessage(Text.literal("[BoatFly] " + var0));
      }
   }
}
