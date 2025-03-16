package dev.boze.client.enums;

public enum SwapMode {
   Normal(SlotSwapMode.Normal),
   Silent(SlotSwapMode.Normal),
   Alt(SlotSwapMode.Alt);

   final SlotSwapMode field8;
   private static final SwapMode[] field9 = method4();

   private SwapMode(SlotSwapMode var3) {
      this.field8 = var3;
   }

   private static SwapMode[] method4() {
      return new SwapMode[]{Normal, Silent, Alt};
   }
}
