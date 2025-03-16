package dev.boze.client.enums;

enum AntiAimYaw {
   Off,
   Jitter,
   FOVJitter,
   Random,
   Lock,
   Offset,
   Stare,
   Spin;

   private static final AntiAimYaw[] field1782 = method893();

   private static AntiAimYaw[] method893() {
      return new AntiAimYaw[]{Off, Jitter, FOVJitter, Random, Lock, Offset, Stare, Spin};
   }
}
