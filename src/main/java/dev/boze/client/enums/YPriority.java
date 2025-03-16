package dev.boze.client.enums;

enum YPriority {
   EyeLevel(1.5),
   Up(6.0),
   Down(-6.0);

   final double field1704;
   private static final YPriority[] field1705 = method824();

   private YPriority(double var3) {
      this.field1704 = var3;
   }

   private static YPriority[] method824() {
      return new YPriority[]{EyeLevel, Up, Down};
   }
}
