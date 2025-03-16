package dev.boze.client.systems.modules.client;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.eJ;
import dev.boze.client.mixin.PlayerMoveC2SPacketAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import dev.boze.client.Boze;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class GhostRotations extends Module {
   public static final GhostRotations INSTANCE = new GhostRotations();
   private final BooleanSetting field744 = new BooleanSetting("Silent", true, "Silent rotation");
   private final MinMaxDoubleSetting field745 = new MinMaxDoubleSetting(
      "BackSpeed", new double[]{0.15, 0.3}, 0.1, 10.0, 0.1, "Aim speed to rotate back (for silent rotations)"
   );
   public final MinMaxSetting field746 = new MinMaxSetting("PointScale", 0.1, 0.01, 1.0, 0.01, "Aim point scaling");
   public final SettingCategory field747 = new SettingCategory("Tracking", "Tracking options");
   public final MinMaxSetting field748 = new MinMaxSetting("TrackStrength", 0.4, 0.0, 1.0, 0.01, "Tracking strength", this.field747);
   public final MinMaxSetting field749 = new MinMaxSetting("Offset", 0.5, 0.0, 1.0, 0.01, "Tracking offset", this.field747);
   public final MinMaxSetting field750 = new MinMaxSetting("Deviation", 2.0, 0.0, 2.0, 0.1, "Tracking deviation from straight line", this.field747);
   public final MinMaxSetting field751 = new MinMaxSetting("Lag", 0.5, 0.0, 1.0, 0.01, "Tracking lag", this.field747);
   public final BooleanSetting field752 = new BooleanSetting("AccuracyGradient", false, "Tracking accuracy gradient based on distance", this.field747);
   public final MinMaxSetting field753 = new MinMaxSetting(
      "VerticalFactor", 0.2, 0.0, 1.0, 0.01, "Vertical distance scale factor for accuracy gradient", this.field752::method419, this.field747
   );
   public final SettingCategory field754 = new SettingCategory("Noise", "Noise options");
   public final MinMaxSetting field755 = new MinMaxSetting("Horizontal", 0.5, 0.0, 1.0, 0.01, "Horizontal noise", this.field754);
   public final MinMaxSetting field756 = new MinMaxSetting("Vertical", 0.5, 0.0, 1.0, 0.01, "Vertical noise", this.field754);
   public final MinMaxSetting field757 = new MinMaxSetting("VDeviation", 0.2, 0.0, 1.0, 0.01, "Vertical noise caused deviation from eyes", this.field754);
   public final BooleanSetting field758 = new BooleanSetting("DistanceScale", false, "Scale noise with distance", this.field754);
   private final IntSetting field759 = new IntSetting("Timeout", 20, 5, 50, 1, "Timeout for rotations  - if ghost rotating fails, set client-side rotation");
   public RotationHelper field760 = null;
   private final Timer field761 = new Timer();

   public GhostRotations() {
      super("GhostRotations", "Ghost rotations", Category.Client, ConfigCategory.Main);
      Boze.EVENT_BUS.subscribe(this);
      this.enabled = false;
      this.setNotificationLengthLimited();
   }

   @Override
   public boolean isEnabled() {
      return false;
   }

   @Override
   public boolean setEnabled(boolean newState) {
      this.enabled = false;
      return false;
   }

   public void method1904() {
      if (mc.player != null && mc.interactionManager != null && mc.currentScreen == null) {
         RotationHelper var4 = new RotationHelper(mc.player);
         eJ var5 = eJ.method1098(var4);
         Boze.EVENT_BUS.post(var5);
         if (var5.method1101()) {
            this.field760 = var5.method1100();
            this.field761.reset();
         } else if (this.field760 != null) {
            if (this.field761.hasElapsed((double)(this.field759.method434() * 100))) {
               this.field760.method488(mc.player);
               mc.player.renderYaw = this.field760.method1384();
               mc.player.lastRenderYaw = this.field760.method1384();
               mc.player.prevYaw = this.field760.method1384();
               this.field760 = null;
               return;
            }

            this.method358(var4);
         }

         if (!this.field744.method419() && this.field760 != null && !var5.field1963) {
            this.field760.method488(mc.player);
         }
      }
   }

   @EventHandler(
      priority = 9999
   )
   public void method1885(ACRotationEvent event) {
      if (this.field760 != null && this.field744.method419()) {
         if (event.method1017() == AnticheatMode.Grim) {
            event.yaw = this.field760.method1384();
            event.pitch = this.field760.method1385();
            event.method1021(true);
         }
      }
   }

   @EventHandler(
      priority = -9999
   )
   public void method1886(ACRotationEvent event) {
      if (this.field760 != null && this.field744.method419()) {
         if (event.method1017() == AnticheatMode.Grim) {
            event.yaw = this.field760.method1384();
            event.pitch = this.field760.method1385();
            event.method1021(true);
         }
      }
   }

   @EventHandler
   public void method1853(PrePacketSendEvent event) {
      if (this.field760 != null && this.field744.method419()) {
         if (event.packet instanceof PlayerMoveC2SPacket var5) {
            ((PlayerMoveC2SPacketAccessor)var5).setYaw(this.field760.method1384());
            ((PlayerMoveC2SPacketAccessor)var5).setPitch(this.field760.method1385());
         }
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (this.field760 != null) {
         if (mc.player != null && mc.interactionManager != null) {
            if (event.packet instanceof EntityS2CPacket var5 && var5.getEntity(mc.world) == mc.player
               || event.packet instanceof EntityPositionS2CPacket var6 && var6.getEntityId() == mc.player.getId()) {
               mc.player.setYaw(this.field760.method1384());
               mc.player.setPitch(this.field760.method1385());
               this.field760 = null;
               return;
            }

            if (event.packet instanceof PlayerPositionLookS2CPacket) {
               if (mc.currentScreen instanceof DownloadingTerrainScreen) {
                  this.field760 = null;
                  return;
               }

               mc.player.setYaw(this.field760.method1384());
               mc.player.setPitch(this.field760.method1385());
               this.field760 = null;
            }
         } else {
            this.field760 = null;
         }
      }
   }

   private void method358(RotationHelper var1) {
      if (this.field760 != null) {
         RotationHelper var5 = this.field760.method603(var1, this.field745.method1287()).method1600();
         if (this.field760.equals(var5)) {
            RotationHelper var6 = var1.method1600();
            this.field760 = null;
            if (mc.player != null) {
               var6.method488(mc.player);
               mc.player.renderYaw = var6.method1384();
               mc.player.lastRenderYaw = var6.method1384();
               mc.player.prevYaw = var6.method1384();
            }
         } else {
            this.field760 = var5;
         }
      }
   }
}
