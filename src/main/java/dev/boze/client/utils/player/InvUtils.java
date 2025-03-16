package dev.boze.client.utils.player;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.player.InvUtils.Action;
import net.minecraft.screen.slot.SlotActionType;

public class InvUtils implements IMinecraft {
   private static final Action field3949 = new Action();

   public static Action method2201() {
      field3949.field3950 = SlotActionType.PICKUP;
      field3949.field3951 = true;
      return field3949;
   }

   public static Action method2202() {
      field3949.field3950 = SlotActionType.SWAP;
      field3949.field3951 = true;
      return field3949;
   }

   public static Action method2203() {
      field3949.field3950 = SlotActionType.PICKUP;
      return field3949;
   }

   public static Action method2204() {
      field3949.field3950 = SlotActionType.QUICK_MOVE;
      return field3949;
   }

   public static Action method2205() {
      field3949.field3950 = SlotActionType.THROW;
      field3949.field3954 = 1;
      return field3949;
   }

   public static void dropHand() {
      if (!mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
         mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, -999, 0, SlotActionType.PICKUP, mc.player);
      }
   }
}
