package dev.boze.client.enums;

enum AutoMineSuccessMode {
   Fail,
   Normal,
   Instant;

   private static final AutoMineSuccessMode[] field1654 = method773();

   private static AutoMineSuccessMode[] method773() {
      return new AutoMineSuccessMode[]{Fail, Normal, Instant};
   }
}
