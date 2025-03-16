package dev.boze.client.systems.modules.graph;

import dev.boze.client.enums.ServerPacketsMode;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.GraphHUDModule;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;

public class ServerPackets extends GraphHUDModule {
   public static final ServerPackets INSTANCE = new ServerPackets();
   private final EnumSetting<ServerPacketsMode> field2579 = new EnumSetting<ServerPacketsMode>("Mode", ServerPacketsMode.Second, "Count per tick or per second");
   private int field2580;

   public ServerPackets() {
      super("ServerPackets", "Graphs inbound packets");
      this.field2300.method421(true);
   }

   @EventHandler
   public void method1542(PacketBundleEvent event) {
      if (this.field2579.method461() == ServerPacketsMode.Tick) {
         this.field2580++;
      }
   }

   @EventHandler
   public void method1543(PostTickEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (this.field2579.method461() == ServerPacketsMode.Second) {
            if (mc.player.age % 20 == 0) {
               if (mc.getNetworkHandler() == null || mc.getNetworkHandler().getConnection() == null) {
                  return;
               }

               this.method1324((double)mc.getNetworkHandler().getConnection().getAveragePacketsReceived());
            }
         } else {
            this.method1324((double)this.field2580);
            this.field2580 = 0;
         }
      }
   }
}
