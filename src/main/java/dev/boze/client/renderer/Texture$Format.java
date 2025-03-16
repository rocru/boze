package dev.boze.client.renderer;

public enum Texture$Format {
   field1754,
   RGB,
   RGBA;

   private static final Texture$Format[] field1755 = method869();

   public int glID() {
      return switch (this) {
         case field1754 -> 6403;
         case RGB -> 6407;
         case RGBA -> 6408;
      };
   }

   private static Texture$Format[] method869() {
      return new Texture$Format[]{field1754, RGB, RGBA};
   }
}
