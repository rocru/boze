package dev.boze.client.gui.renderer;

import dev.boze.client.gui.renderer.packer.GuiTexture;
import dev.boze.client.gui.renderer.packer.TexturePacker;
import dev.boze.client.utils.render.ByteTexture;
import net.minecraft.util.Identifier;

public class IconLoader {
   private static final TexturePacker field2058 = new TexturePacker();
   public static ByteTexture field2059 = null;
   public static GuiTexture field2060;
   public static GuiTexture field2061;
   public static GuiTexture field2062;
   public static GuiTexture field2063;
   public static GuiTexture field2064;
   public static GuiTexture field2065;
   public static GuiTexture field2066;
   public static GuiTexture field2067;

   public static void method1121() {
      if (field2059 == null) {
         field2060 = method1122(Identifier.of("boze", "textures/toggle_on.png"));
         field2061 = method1122(Identifier.of("boze", "textures/toggle_off.png"));
         field2062 = method1122(Identifier.of("boze", "textures/visible.png"));
         field2063 = method1122(Identifier.of("boze", "textures/visible_off.png"));
         field2064 = method1122(Identifier.of("boze", "textures/priority.png"));
         field2065 = method1122(Identifier.of("boze", "textures/warning.png"));
         field2066 = method1122(Identifier.of("boze", "textures/player_add.png"));
         field2067 = method1122(Identifier.of("boze", "textures/player_remove.png"));
         field2059 = field2058.method564();
      }
   }

   private static GuiTexture method1122(Identifier var0) {
      return field2058.method562(var0);
   }
}
