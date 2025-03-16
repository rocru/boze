package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.core.Version;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.hud.ColorHUDModule;
import mapped.Class27;

public class Watermark extends ColorHUDModule {
   private final BooleanSetting field613 = new BooleanSetting("Version", true, "Show client version in watermark");
   private final BooleanSetting field614 = new BooleanSetting("Build", false, "Show client build in watermark", Watermark::lambda$new$0);
   public static final Watermark INSTANCE = new Watermark();

   public Watermark() {
      super("Watermark", "Shows the client watermark", 0.0, 0.0, 1);
      this.setEnabled(true);
   }

   @Override
   protected String method1562() {
      return Options.method1562();
   }

   @Override
   protected String method1563() {
      return (this.field613.method419() ? Version.tag : "")
         + (this.field614.method419() && !Class27.BUILD.isEmpty() ? (this.field613.method419() ? "+" + Class27.BUILD : Class27.BUILD) : "");
   }

   private static boolean lambda$new$0() {
      return !Class27.BUILD.isEmpty();
   }
}
