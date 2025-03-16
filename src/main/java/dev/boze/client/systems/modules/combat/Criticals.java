package dev.boze.client.systems.modules.combat;

import dev.boze.client.events.PostPacketSendEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixininterfaces.IPlayerInteractEntityC2SPacket;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class Criticals extends Module {
   public static Criticals INSTANCE = new Criticals();
   private final BooleanSetting ncpStrict = new BooleanSetting("NCPStrict", true, "NCP Strict mode");
   private final BooleanSetting still = new BooleanSetting("Still", true, "Only crit when you're still");
   private final IntSetting ticks = new IntSetting("Ticks", 0, 0, 5, 1, "Additional crit ticks");
   private final BooleanSetting onlyAura = new BooleanSetting("OnlyAura", false, "Only do crits when aura is enabled");
   private boolean field444 = false;

   public Criticals() {
      super("Criticals", "Always crit enemies", Category.Combat);
   }

   @EventHandler
   public void method1853(PrePacketSendEvent event) {
      if (event.packet instanceof IPlayerInteractEntityC2SPacket var5
         && var5.boze$getType() == InteractType.ATTACK
         && var5.boze$getEntity() instanceof LivingEntity
         && mc.player.isOnGround()
         && !mc.player.isInLava()
         && !mc.player.isSubmergedInWater()) {
         if (this.still.method419() && Class5924.method2115()) {
            return;
         }

         if (this.onlyAura.method419() && !Aura.INSTANCE.isEnabled()) {
            return;
         }

         if (this.ncpStrict.method419() && mc.world.getBlockState(mc.player.getBlockPos()).getBlock() != Blocks.COBWEB) {
            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.062602401692772, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0726023996066094, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
         } else {
            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.3, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
         }

         this.field444 = true;
      }
   }

   @EventHandler
   public void method240(PostPacketSendEvent event) {
      if (this.field444) {
         this.field444 = false;

         for (int var5 = 0; var5 < this.ticks.method434(); var5++) {
            mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
         }
      }
   }
}
