package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.WTapMode;
import dev.boze.client.events.PostAttackEntityEvent;
import dev.boze.client.events.TickInputPostEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class WTap extends Module {
   public static final WTap INSTANCE = new WTap();
   private final EnumSetting<WTapMode> field2858 = new EnumSetting<WTapMode>("Mode", WTapMode.field1761, "Button to tap");
   private final MinMaxSetting field2859 = new MinMaxSetting("Chance", 1.0, 0.01, 1.0, 0.01, "Chance to tap");
   private final IntSetting field2860 = new IntSetting("Ticks", 5, 1, 20, 1, "Max ticks to wait after attack");
   private boolean field2861 = false;
   private double field2862 = Math.random();

   private WTap() {
      super("WTap", "Automatically W/S-taps", Category.Legit);
   }

   public void method1626() {
      this.field2861 = false;
   }

   @EventHandler
   public void method1627(PostAttackEntityEvent event) {
      if (event.entity instanceof LivingEntity var5 && var5.hurtTime < this.field2860.getValue()) {
         Vec3d var12 = mc.player.getPos();
         Vec3d var7 = new Vec3d(mc.player.prevX, mc.player.prevY, mc.player.prevZ);
         Vec3d var8 = var5.getPos();
         Vec3d var9 = new Vec3d(var5.prevX, var5.prevY, var5.prevZ);
         boolean var10 = var8.subtract(var7).length() > var8.subtract(var12).length();
         boolean var11 = var12.subtract(var9).length() > var12.subtract(var8).length();
         if (var10 && var11) {
            if (this.field2862 <= this.field2859.getValue()) {
               this.field2861 = true;
            }
         } else {
            this.field2862 = Math.random();
         }

         return;
      }
   }

   @EventHandler(
      priority = 3500
   )
   public void method1628(TickInputPostEvent event) {
      if (this.field2861) {
         if (this.field2858.getValue() == WTapMode.field1761) {
            event.field1954 = 0.0F;
            event.field1953 = 0.0F;
         } else {
            event.field1954 *= -1.0F;
            event.field1953 *= -1.0F;
         }
      }
   }
}
