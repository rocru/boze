package dev.boze.client.renderer;

public enum Texture$Filter {
   Nearest,
   Linear,
   LinearMipmapLinear;

   private static final Texture$Filter[] field1685 = method804();

   public int glID() {
      return switch (this) {
         case Nearest -> 9728;
         case Linear -> 9729;
         case LinearMipmapLinear -> 9987;
      };
   }

   private static Texture$Filter[] method804() {
      return new Texture$Filter[]{Nearest, Linear, LinearMipmapLinear};
   }
}
