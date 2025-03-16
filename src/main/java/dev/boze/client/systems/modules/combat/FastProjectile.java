package dev.boze.client.systems.modules.combat;

import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BowItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class FastProjectile extends Module {
   public static final FastProjectile INSTANCE = new FastProjectile();
   private final BooleanSetting rotate = new BooleanSetting("Rotate", false, "Rotate");
   private final BooleanSetting minimize = new BooleanSetting("Minimize", false, "Minimizes movement");
   public final MinMaxSetting factor = new MinMaxSetting("Factor", 1.0, 0.1, 20.0, 0.1, "Factor");
   private final MinMaxSetting cooldown = new MinMaxSetting("Cooldown", 5.0, 0.0, 10.0, 0.1, "Cooldown in seconds");
   private SettingCategory selection = new SettingCategory("Selection", "Projectile selection");
   private BooleanSetting bows = new BooleanSetting("Bows", true, "Apply on bows", this.selection);
   private BooleanSetting tridents = new BooleanSetting("Tridents", true, "Apply on tridents", this.selection);
   private BooleanSetting pearls = new BooleanSetting("EPearls", false, "Apply on ender pearls", this.selection);
   private BooleanSetting xp = new BooleanSetting("Xp", false, "Apply on xp", this.selection);
   private BooleanSetting eggs = new BooleanSetting("Eggs", false, "Apply on eggs", this.selection);
   private BooleanSetting splashPotions = new BooleanSetting("SplashPotions", false, "Apply on splash potions", this.selection);
   private BooleanSetting snowballs = new BooleanSetting("Snowballs", false, "Apply on snowballs", this.selection);
   private final Timer field2556 = new Timer();

   public FastProjectile() {
      super("FastProjectile", "Shoot and throw projectiles faster", Category.Combat);
   }

   @EventHandler
   public void method1504(PrePacketSendEvent event) {
      if (event.packet instanceof PlayerActionC2SPacket
            && ((PlayerActionC2SPacket)event.packet).getAction().equals(Action.RELEASE_USE_ITEM)
            && this.method1506()
         || event.packet instanceof PlayerInteractItemC2SPacket
            && this.method1505(mc.player.getStackInHand(((PlayerInteractItemC2SPacket)event.packet).getHand()).getItem())) {
         if (!this.field2556.hasElapsed(this.cooldown.getValue() * 1000.0)) {
            return;
         }

         for (int var5 = 0; var5 < (int)(this.factor.getValue() * 10.0); var5++) {
            if (this.rotate.method419()) {
               mc.player
                  .networkHandler
                  .sendPacket(
                     new Full(
                        mc.player.getX(),
                        mc.player.getY() + (this.minimize.method419() ? 1.3E-13 : 4.3E-13),
                        mc.player.getZ(),
                        ((ClientPlayerEntityAccessor)mc.player).getLastYaw(),
                        ((ClientPlayerEntityAccessor)mc.player).getLastPitch(),
                        true
                     )
                  );
               mc.player
                  .networkHandler
                  .sendPacket(
                     new Full(
                        mc.player.getX(),
                        mc.player.getY() + (this.minimize.method419() ? 2.7E-13 : 8.4E-13),
                        mc.player.getZ(),
                        ((ClientPlayerEntityAccessor)mc.player).getLastYaw(),
                        ((ClientPlayerEntityAccessor)mc.player).getLastPitch(),
                        false
                     )
                  );
            } else {
               mc.player
                  .networkHandler
                  .sendPacket(
                     new PositionAndOnGround(mc.player.getX(), mc.player.getY() + (this.minimize.method419() ? 1.3E-13 : 4.3E-13), mc.player.getZ(), true)
                  );
               mc.player
                  .networkHandler
                  .sendPacket(
                     new PositionAndOnGround(mc.player.getX(), mc.player.getY() + (this.minimize.method419() ? 2.7E-13 : 8.4E-13), mc.player.getZ(), false)
                  );
            }
         }
      }
   }

   private boolean method1505(Item var1) {
      return var1 instanceof EnderPearlItem && this.pearls.method419()
         || var1 instanceof ExperienceBottleItem && this.xp.method419()
         || var1 instanceof EggItem && this.eggs.method419()
         || var1 instanceof SplashPotionItem && this.splashPotions.method419()
         || var1 instanceof SnowballItem && this.snowballs.method419();
   }

   private boolean method1506() {
      return !this.bows.method419()
            || !(mc.player.getMainHandStack().getItem() instanceof BowItem) && !(mc.player.getOffHandStack().getItem() instanceof BowItem)
         ? this.tridents.method419()
            && (mc.player.getMainHandStack().getItem() instanceof TridentItem || mc.player.getOffHandStack().getItem() instanceof TridentItem)
         : true;
   }
}
