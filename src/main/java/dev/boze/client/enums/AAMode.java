package dev.boze.client.enums;

import net.minecraft.client.MinecraftClient;

public enum AAMode {
   Off(0),
   MSAA2x(2),
   MSAA4x(4),
   MSAA8x(MinecraftClient.IS_SYSTEM_MAC ? 4 : 8);

   public int samples;
   private static final AAMode[] field13 = method8();

   private AAMode(int var3) {
      this.samples = var3;
   }

   private static AAMode[] method8() {
      return new AAMode[]{Off, MSAA2x, MSAA4x, MSAA8x};
   }
}
