package dev.boze.client.systems.modules.hud.text;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.systems.modules.hud.TextHUDModule;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class Lag extends TextHUDModule {
   public static final Lag INSTANCE = new Lag();
   private final Timer field2584 = new Timer();

   public Lag() {
      super("Lag", "Shows lag spikes");
   }

   @Override
   public void method295(DrawContext context) {
      if (this.field2584.hasElapsed(2500.0) || mc.currentScreen instanceof ClickGUI && ClickGUI.field1335.field1336) {
         long var5 = System.currentTimeMillis() - this.field2584.getLastTime();
         StringBuilder var7 = new StringBuilder(Long.toString(var5));
         if (!this.field2584.hasElapsed(1000.0)) {
            while (var7.length() < 4) {
               var7.insert(0, "intermediary");
            }
         }

         String var8 = "Server not responding for " + var7 + " ms";
         this.method296(
            var8,
            this.field2581.getValue() ? this.field2582.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field2581.getValue() ? this.field2583.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      }
   }

   @Override
   protected String method1544() {
      return "";
   }

   @EventHandler
   public void method1545(PacketBundleEvent event) {
      if (!(event.packet instanceof GameMessageS2CPacket)) {
         this.field2584.reset();
      }
   }
}
