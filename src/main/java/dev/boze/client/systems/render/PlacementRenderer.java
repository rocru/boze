package dev.boze.client.systems.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.render.Placement;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.render.PlaceRender;
import java.util.ArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.BlockHitResult;

public class PlacementRenderer implements SettingsGroup {
   private final BooleanSetting field221 = new BooleanSetting("Render", true, "Render placements");
   private final ColorSetting field222 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill", this.field221);
   private final ColorSetting field223 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline", this.field221);
   public final SettingBlock field224 = new SettingBlock("Renderer", "Placement renderer settings", this.field221, this.field222, this.field223);
   private final ArrayList<Placement> field225 = new ArrayList();

   @Override
   public Setting<?>[] get() {
      return this.field224.method472();
   }

   @EventHandler
   public void method2071(Render3DEvent event) {
      if (this.field221.method419()) {
         for (Placement var6 : this.field225) {
            PlaceRender.INSTANCE.method2015(event, var6, this.field222.method1362(), this.field223.method1362());
         }
      }
   }

   @EventHandler
   public void onSendMovementPackets(MovementEvent event) {
      this.field225.removeIf(PlacementRenderer::lambda$onSendMovementPackets$0);
   }

   public void method146(BlockHitResult result) {
      if (this.field221.method419()) {
         this.field225.add(PlaceRender.INSTANCE.method2017(result.getBlockPos().offset(result.getSide())));
      }
   }

   private static boolean lambda$onSendMovementPackets$0(Placement var0) {
      return System.currentTimeMillis() - var0.method1159() > (long)PlaceRender.method2010();
   }
}
