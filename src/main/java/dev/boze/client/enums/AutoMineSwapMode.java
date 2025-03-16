package dev.boze.client.enums;

public enum AutoMineSwapMode {
   Off(null, false),
   Normal(SwapMode.Normal, false),
   Silent(SwapMode.Silent, true),
   Alt(SwapMode.Alt, true);

   public final SwapMode swapMode;
   public final boolean swapBack;
   private static final AutoMineSwapMode[] field1783 = method894();

   private AutoMineSwapMode(SwapMode var3, boolean var4) {
      this.swapMode = var3;
      this.swapBack = var4;
   }

   private static AutoMineSwapMode[] method894() {
      return new AutoMineSwapMode[]{Off, Normal, Silent, Alt};
   }
}
