package dev.boze.client.systems.modules.render;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.render.RenderUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.Random;

public class Tint extends Module {
   public static final Tint INSTANCE = new Tint();
   private final BooleanSetting field3728 = new BooleanSetting("TimeChanger", false, "Change time");
   private final IntSetting field3729 = new IntSetting("Time", 0, -24000, 24000, 1, "Time", this.field3728);
   public final BooleanSetting field3730 = new BooleanSetting("PopParticles", false, "Tint totem pop particles");
   public final FloatSetting field3731 = new FloatSetting("YellowR", 0.8F, 0.0F, 1.0F, 0.1F, "Redness for yellow particles", this.field3730);
   public final FloatSetting field3732 = new FloatSetting("YellowRRand", 0.25F, 0.0F, 1.0F, 0.05F, "Randomness for redness of yellow particles", this.field3730);
   public final FloatSetting field3733 = new FloatSetting("YellowG", 0.9F, 0.0F, 1.0F, 0.1F, "Greenness for yellow particles", this.field3730);
   public final FloatSetting field3734 = new FloatSetting(
      "YellowGRand", 0.33F, 0.0F, 1.0F, 0.05F, "Randomness for greenness of yellow particles", this.field3730
   );
   public final FloatSetting field3735 = new FloatSetting("YellowB", 0.2F, 0.0F, 1.0F, 0.1F, "Blueness for yellow particles", this.field3730);
   public final FloatSetting field3736 = new FloatSetting("YellowBRand", 1.0F, 0.0F, 1.0F, 0.05F, "Randomness for blueness of yellow particles", this.field3730);
   public final FloatSetting field3737 = new FloatSetting("GreenR", 0.3F, 0.0F, 1.0F, 0.1F, "Redness for green particles", this.field3730);
   public final FloatSetting field3738 = new FloatSetting("GreenRRand", 0.66F, 0.0F, 1.0F, 0.05F, "Randomness for redness of green particles", this.field3730);
   public final FloatSetting field3739 = new FloatSetting("GreenG", 0.7F, 0.0F, 1.0F, 0.1F, "Greenness for green particles", this.field3730);
   public final FloatSetting field3740 = new FloatSetting("GreenGRand", 0.42F, 0.0F, 1.0F, 0.05F, "Randomness for greenness of green particles", this.field3730);
   public final FloatSetting field3741 = new FloatSetting("GreenB", 0.2F, 0.0F, 1.0F, 0.1F, "Blueness for green particles", this.field3730);
   public final FloatSetting field3742 = new FloatSetting("GreenBRand", 1.0F, 0.0F, 1.0F, 0.05F, "Randomness for blueness of green particles", this.field3730);
   public final BooleanSetting field3743 = new BooleanSetting("Fog", false, "Tint fog");
   public final RGBASetting field3744 = new RGBASetting("Color", new RGBAColor(200, 160, 255, 255), true, "Fog tint color", this.field3743);
   public final BooleanSetting field3745 = new BooleanSetting("Sky", true, "Tint sky");
   public final RGBASetting field3746 = new RGBASetting("Overworld", new RGBAColor(200, 160, 255, 255), "Overworld sky color", this.field3745);
   public final RGBASetting field3747 = new RGBASetting("Nether", new RGBAColor(200, 160, 255, 255), "Nether sky color", this.field3745);
   public final RGBASetting field3748 = new RGBASetting("End", new RGBAColor(200, 160, 255, 255), "End sky color", this.field3745);
   public final BooleanSetting field3749 = new BooleanSetting("Clouds", false, "Tint clouds");
   public final RGBASetting field3750 = new RGBASetting("Color", new RGBAColor(200, 160, 255, 255), "Cloud tint color", this.field3749);
   public final BooleanSetting field3751 = new BooleanSetting("Lightning", false, "Tint lightning");
   public final RGBASetting field3752 = new RGBASetting("Color", new RGBAColor(200, 160, 255, 76), "Lightning tint color", this.field3751);
   public final BooleanSetting field3753 = new BooleanSetting("Blocks", false, "Tint blocks");
   public final RGBASetting field3754 = new RGBASetting("Color", new RGBAColor(200, 160, 255, 255), "Block tint color", this.field3753);
   public final BooleanSetting field3755 = new BooleanSetting("Water", false, "Tint water");
   public final RGBASetting field3756 = new RGBASetting("Color", new RGBAColor(128, 0, 255, 25), "Water tint color", this.field3755);
   public final BooleanSetting aa = new BooleanSetting("Lava", false, "Tint lava");
   public final RGBASetting ab = new RGBASetting("Color", new RGBAColor(128, 0, 255, 25), "Lava tint color", this.aa);
   public final BooleanSetting ac = new BooleanSetting("Grass", false, "Tint grass");
   public final RGBASetting ad = new RGBASetting("Color", new RGBAColor(128, 0, 255, 25), "Grass tint color", this.ac);
   public final BooleanSetting ae = new BooleanSetting("Foliage", false, "Tint foliage");
   public final RGBASetting af = new RGBASetting("Color", new RGBAColor(128, 0, 255, 25), "Foliage tint color", this.ae);
   private final BooleanSetting ag = new BooleanSetting("Screen", false, "Tint the screen");
   private final RGBASetting ah = new RGBASetting("Color", new RGBAColor(128, 0, 255, 25), "Screen tint color", this.ag);
   public final Random ai = new Random();
   private boolean aj;
   private int ak;
   private Timer al = new Timer();
   private long am;

   public Tint() {
      super("Tint", "Change tint for various things", Category.Render);
      this.field3728.method401(this::lambda$new$0);
   }

   @Override
   public void onEnable() {
      if (mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }

      if (mc.world != null) {
         this.am = mc.world.getTime();
      }
   }

   @Override
   public void onDisable() {
      if (mc.worldRenderer != null) {
         mc.worldRenderer.reload();
      }

      if (mc.world != null && this.field3728.method419()) {
         mc.world.setTimeOfDay(this.am);
      }
   }

   @EventHandler
   private void method2039(Render3DEvent var1) {
      if (mc.worldRenderer != null && this.al.hasElapsed(1000.0)) {
         if (this.aj != this.field3753.method419() || this.ak != this.field3754.method1347().method2010()) {
            mc.worldRenderer.reload();
            this.al.reset();
         }

         this.aj = this.field3753.method419();
         this.ak = this.field3754.method1347().method2010();
      }
   }

   @EventHandler(
      priority = -190
   )
   private void method2040(Render2DEvent var1) {
      if (this.ag.method419()) {
         RenderUtil.field3963.method2233();
         RenderUtil.field3963.method2252(0.0, 0.0, (double)mc.getWindow().getScaledWidth(), (double)mc.getWindow().getScaledHeight(), this.ah.method1347());
         RenderUtil.field3963.method2235(null);
      }
   }

   @EventHandler
   private void method2041(MovementEvent var1) {
      if (this.field3728.method419()) {
         mc.world.setTimeOfDay((long)this.field3729.method434().intValue());
      }
   }

   @EventHandler
   private void method2042(PacketBundleEvent var1) {
      if (var1.packet instanceof WorldTimeUpdateS2CPacket) {
         this.am = ((WorldTimeUpdateS2CPacket)var1.packet).getTime();
         if (this.field3728.method419()) {
            var1.method1021(true);
         }
      }
   }

   private void lambda$new$0(Boolean var1) {
      if (mc.world != null && this.isEnabled()) {
         if (var1) {
            this.am = mc.world.getTime();
         } else {
            mc.world.setTimeOfDay(this.am);
         }
      }
   }
}
