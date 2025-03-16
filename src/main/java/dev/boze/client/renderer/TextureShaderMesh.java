package dev.boze.client.renderer;

import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.shaders.ShaderProgram;
import mapped.Class3032;

public class TextureShaderMesh extends ShaderMesh {
   public TextureShaderMesh(ShaderProgram shader, DrawMode drawMode, Attrib... attributes) {
      super(shader, drawMode, attributes);
   }

   @Override
   protected void beforeRender() {
      super.beforeRender();
      GL.method1210(Class3032.method2010(), 1);
      this.field2175.method690("u_Texture", 1);
   }
}
