package dev.boze.client.utils;

import dev.boze.client.events.PrePacketSendEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;

public class InventoryDebugger implements IMinecraft {
   public static boolean field1628 = false;

   @EventHandler
   public static void method1853(PrePacketSendEvent event) {
      if (event.packet instanceof ClickSlotC2SPacket var4) {
         System.out
            .printf(
               "[Inventory @ %d] syncId: %d | revision: %d | slot: %d | button: %d | actionType: %s | stack: %s | modifiedStacks: %s \n",
               mc.player.age,
               var4.getSyncId(),
               var4.getRevision(),
               var4.getSlot(),
               var4.getButton(),
               var4.getActionType(),
               var4.getStack().getItem(),
               var4.getModifiedStacks()
                  .int2ObjectEntrySet()
                  .stream()
                  .map(InventoryDebugger::lambda$onPacketSend$0)
                  .reduce(InventoryDebugger::lambda$onPacketSend$1)
                  .orElse("empty")
            );
      }
   }

   private static String lambda$onPacketSend$1(String var0, String var1) {
      return var0 + ", " + var1;
   }

   private static String lambda$onPacketSend$0(Entry var0) {
      return String.format("%d: %s", var0.getIntKey(), ((ItemStack)var0.getValue()).getItem());
   }
}
