package dev.boze.client.enums;

import dev.boze.client.renderer.DrawMode;

public enum ShapeMode {
   Lines(DrawMode.Lines),
   Triangles(DrawMode.Triangles),
   Full(DrawMode.Triangles, DrawMode.Lines);

   public final DrawMode[] drawModes;

   private ShapeMode(DrawMode... var3) {
      this.drawModes = var3;
   }

   public boolean method795(DrawMode drawMode) {
      for (DrawMode var8 : this.drawModes) {
         if (var8 == drawMode) {
            return true;
         }
      }

      return false;
   }

   public boolean method2114() {
      return this == Lines || this == Full;
   }

   public boolean method2115() {
      return this == Triangles || this == Full;
   }

   // $VF: synthetic method
   private static ShapeMode[] method796() {
      return new ShapeMode[]{Lines, Triangles, Full};
   }
}
