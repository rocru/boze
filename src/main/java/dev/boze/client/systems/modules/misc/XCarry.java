package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.CloseHandledScreenC2SPacketAccessor;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class XCarry extends Module {
   public static final XCarry INSTANCE = new XCarry();
   private boolean field3137;

   public XCarry() {
      super("XCarry", "Lets you keep items in your crafting grid", Category.Misc);
   }

   @Override
   public void onEnable() {
      this.field3137 = false;
   }

   @Override
   public void onDisable() {
      if (this.field3137 && MinecraftUtils.isClientActive()) {
         mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.playerScreenHandler.syncId));
      }
   }

   @EventHandler
   public void method1781(PrePacketSendEvent event) {
      if (event.packet instanceof CloseHandledScreenC2SPacket
         && ((CloseHandledScreenC2SPacketAccessor)event.packet).getSyncId() == mc.player.playerScreenHandler.syncId) {
         this.field3137 = true;
         event.method1020();
      }
   }
}
