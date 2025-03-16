package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.EntityUtil;
import java.util.ArrayList;
import java.util.Comparator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.HorseScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class AutoMount extends Module {
   public static final AutoMount INSTANCE = new AutoMount();
   private final FloatSetting range = new FloatSetting("Range", 4.0F, 1.0F, 6.0F, 0.1F, "Entity range");
   private final FloatSetting delay = new FloatSetting("Delay", 1.0F, 0.0F, 10.0F, 0.1F, "Mount delay");
   private final BooleanSetting boats = new BooleanSetting("Boats", false, "Mount boats");
   private final BooleanSetting horses = new BooleanSetting("Horses", false, "Mount horses");
   private final BooleanSetting skeletonHorse = new BooleanSetting("SkeletonHorses", false, "Mount skeleton horses");
   private final BooleanSetting donkeys = new BooleanSetting("Donkeys", true, "Mount donkeys");
   private final BooleanSetting pigs = new BooleanSetting("Pigs", false, "Mount cops");
   private final BooleanSetting llamas = new BooleanSetting("Llamas", false, "Mount llamas");
   private dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();

   public AutoMount() {
      super("AutoMount", "Automatically mounts entities", Category.Misc);
   }

   @EventHandler
   public void method1669(PostPlayerTickEvent event) {
      if (mc.player != null) {
         if (mc.player.isRiding() || mc.player.getVehicle() != null || mc.currentScreen instanceof HorseScreen) {
            this.timer.reset();
         }
      }
   }

   @EventHandler
   public void method1670(MovementEvent event) {
      if (!mc.player.isRiding() && mc.player.getVehicle() == null && !(mc.currentScreen instanceof HorseScreen)) {
         if (this.timer.hasElapsed((double)(this.delay.method423() * 1000.0F))) {
            this.timer.reset();
            ArrayList var5 = new ArrayList();

            for (Entity var7 : mc.world.getEntities()) {
               if (this.method1671(var7)) {
                  var5.add(var7);
               }
            }

            Entity var8 = (Entity)var5.stream().min(Comparator.comparing(AutoMount::lambda$onUpdateWalkingPlayer$0)).orElse(null);
            if (var8 != null) {
               float[] var9 = EntityUtil.method2146(var8.getPos());
               event.method1074(new ActionWrapper(AutoMount::lambda$onUpdateWalkingPlayer$1, var9[0], var9[1]));
            }
         }
      } else {
         this.timer.reset();
      }
   }

   private boolean method1671(Entity var1) {
      if (var1.distanceTo(mc.player) > this.range.method423()) {
         return false;
      } else if (var1 instanceof HorseEntity && this.horses.method419()) {
         return true;
      } else if (var1 instanceof BoatEntity && this.boats.method419()) {
         return true;
      } else if (var1 instanceof SkeletonHorseEntity && this.skeletonHorse.method419()) {
         return true;
      } else if (var1 instanceof DonkeyEntity && this.donkeys.method419()) {
         return true;
      } else if (var1 instanceof PigEntity && this.pigs.method419()) {
         PigEntity var5 = (PigEntity)var1;
         return var5.isSaddled();
      } else {
         return var1 instanceof LlamaEntity && this.llamas.method419();
      }
   }

   private static void lambda$onUpdateWalkingPlayer$1(Entity var0) {
      mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(var0, false, Hand.MAIN_HAND));
   }

   private static Float lambda$onUpdateWalkingPlayer$0(Entity var0) {
      return mc.player.distanceTo(var0);
   }
}
