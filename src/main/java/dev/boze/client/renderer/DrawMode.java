package dev.boze.client.renderer;

public enum DrawMode {
   Lines(2),
   Triangles(3);

   public final int indicesCount;

   private DrawMode(int var3) {
      this.indicesCount = var3;
   }

   public int method2010() {
      return switch (this) {
         case Lines -> 1;
         case Triangles -> 4;
      };
   }

   // $VF: synthetic method
   private static DrawMode[] method791() {
      return new DrawMode[]{Lines, Triangles};
   }
}
