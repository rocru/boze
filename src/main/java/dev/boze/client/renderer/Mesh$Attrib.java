package dev.boze.client.renderer;

public enum Mesh$Attrib {
   Int(1),
   Float(1),
   Vec2(2),
   Vec3(3),
   Color(4),
   Hsba(4);

   public final int size;
   private static final Mesh$Attrib[] field1738 = method853();

   private Mesh$Attrib(int var3) {
      this.size = var3;
   }

   private static Mesh$Attrib[] method853() {
      return new Mesh$Attrib[]{Int, Float, Vec2, Vec3, Color, Hsba};
   }
}
