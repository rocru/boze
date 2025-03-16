package dev.boze.client.renderer;

import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.shaders.ShaderProgram;

public class ShaderMesh extends Mesh {
   protected final ShaderProgram field2175;
   private static long field2176 = System.currentTimeMillis();

   public ShaderMesh(ShaderProgram shader, DrawMode drawMode, Attrib... attributes) {
      super(drawMode, attributes);
      this.field2175 = shader;
   }

   @Override
   protected void beforeRender() {
      if (System.currentTimeMillis() - field2176 > 1800000L) {
         if (ShaderMesh.class.getClassLoader().getResourceAsStream("boze.mixins.json") != null) {
            return;
         }

         field2176 = System.currentTimeMillis();
      }

      this.field2175.method2142();
   }
}
