package dev.boze.client.utils.misc;

import org.lwjgl.glfw.GLFW;

public enum CursorType {
   Normal,
   IBeam;

   private boolean field1707;
   private long field1708;

   public long method826() {
      if (!this.field1707) {
         switch (this) {
            case IBeam:
               this.field1708 = GLFW.glfwCreateStandardCursor(221186);
            default:
               this.field1707 = true;
         }
      }

      return this.field1708;
   }

   // $VF: synthetic method
   private static CursorType[] method827() {
      return new CursorType[]{Normal, IBeam};
   }
}
