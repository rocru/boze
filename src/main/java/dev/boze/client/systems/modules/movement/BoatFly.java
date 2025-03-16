package dev.boze.client.systems.modules.movement;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.BoatFlyMode;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerTravelEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.movement.BoatFly.nk;
import dev.boze.client.utils.Bind;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;

public class BoatFly extends Module {
   public static final BoatFly INSTANCE = new BoatFly();
   private final EnumSetting<BoatFlyMode> field3151 = new EnumSetting<BoatFlyMode>(
      "Mode", BoatFlyMode.NCP, "The mode to use\n - Grim: Grim BoatFly for 2b2t\n - NCP: 1.12.2 NCP BoatFly - may work on some servers\n"
   );
   public final FloatSetting field3152 = new FloatSetting("Speed", 1.0F, 0.1F, 50.0F, 0.1F, "Speed", this::lambda$new$0);
   public final FloatSetting field3153 = new FloatSetting("UpSpeed", 0.5F, 0.0F, 10.0F, 0.1F, "Upwards speed", this::lambda$new$1);
   public final FloatSetting field3154 = new FloatSetting("DownSpeed", 1.0F, 0.0F, 10.0F, 0.1F, "Downwards speed", this::lambda$new$2);
   public final BindSetting field3155 = new BindSetting("Down", Bind.fromKey(341), "Key to go down");
   public final BooleanSetting field3156 = new BooleanSetting("Sync", true, "Sync with server position", this::lambda$new$3);
   public final BooleanSetting field3157 = new BooleanSetting("Hover", true, "Hover", this::lambda$new$4);
   public final BooleanSetting field3158 = new BooleanSetting("Vanilla", false, "Vanilla boatfly", this::lambda$new$5);
   public final BooleanSetting field3159 = new BooleanSetting("Bypass", true, "Try bypass boat fly patch", this::lambda$new$6);
   public final BooleanSetting field3160 = new BooleanSetting("Strong", false, "Strong", this::lambda$new$7, this.field3159);
   public final BooleanSetting field3161 = new BooleanSetting("Semi", true, "Semi", this::lambda$new$8, this.field3159);
   public final IntSetting field3162 = new IntSetting("MaxFrequency", 21, 1, 21, 1, "Max setback frequency", this::lambda$new$9);
   final MinMaxSetting field3163 = new MinMaxSetting("HorizSpeed", 1.0, 0.01, 1.0, 0.01, "Horizontal speed factor", this::lambda$new$10);
   final MinMaxSetting field3164 = new MinMaxSetting("VerticalSpeed", 1.0, 0.01, 1.0, 0.01, "Vertical speed factor", this::lambda$new$11);
   final BindSetting field3165 = new BindSetting("SlowDown", Bind.fromKey(342), "Key to slow down", this::lambda$new$12);
   final MinMaxSetting field3166 = new MinMaxSetting("Factor", 0.95, 0.01, 1.0, 0.01, "Slow down factor", this::lambda$new$13, this.field3165);
   final IntSetting field3167 = new IntSetting(
      "SyncInterval", 10, 1, 100, 1, "Sync interval\nIf you get kicked, try lowering this\nIt's ping-dependent\n", this::lambda$new$14
   );
   final BooleanSetting field3168 = new BooleanSetting("RayCast", true, "RayCast to prevent teleporting into blocks or nether roof", this::lambda$new$15);
   final BooleanSetting field3169 = new BooleanSetting(
      "AutoPilot", false, "Automatically path-finds and avoids obstacles\nExperimental feature\nUse at y=121 for best results\n", this::lambda$new$16
   );
   final BooleanSetting field3170 = new BooleanSetting("LockOn", false, "Lock onto initial direction when enabled", this.field3169);
   final IntSetting field3171 = new IntSetting(
      "MaxBlocks", 5, 1, 20, 1, "Max blocks to steer off course for lock on", this.field3170::method419, this.field3169
   );
   public final BooleanSetting field3172 = new BooleanSetting(
      "AllowDiagonal", false, "Allow diagonal movement\nReduces consistency\nNeeded for diagonal highways\n", this.field3169
   );
   final IntSetting field3173 = new IntSetting("TicksAhead", 10, 5, 15, 1, "How many fly ticks ahead to pathfind", this.field3169);
   final IntSetting field3174 = new IntSetting("MaxFails", 10, 1, 20, 1, "Max pathfinding fails before autopilot is disabled", this.field3169);
   final MinMaxSetting field3175 = new MinMaxSetting(
      "RetryDelay",
      5.0,
      0.1,
      20.0,
      0.1,
      "Delay in seconds before retrying pathfinding on fail\nIf too many mobs are in the way to avoid\nThis helps wait long enough for them to move\n",
      this.field3169
   );
   public final MinMaxSetting field3176 = new MinMaxSetting(
      "BoxExpand", 0.0, 0.0, 1.0, 0.1, "Boat hitbox expand factor\nIncrease if you get stuck around mobs\n", this.field3169
   );
   final BooleanSetting field3177 = new BooleanSetting(
      "Cache",
      false,
      "Cache pathfinding data for future pathfinding\nReduces CPU usage\nWill break if mobs move in the way after caching\nNot recommended for highways\n",
      this.field3169
   );
   final BooleanSetting field3178 = new BooleanSetting("Render", true, "Render the path", this.field3169);
   final ColorSetting field3179 = new ColorSetting("Color", new BozeDrawColor(-7046189), "Color for path", this.field3178::method419, this.field3169);

   public BoatFly() {
      super(
         "BoatFly",
         "Fly with boats\nFor Grim mode:\n 1. Logout inside boat\n 2. Turn on module\n 3. Log back in\n 4. Turn off module before leaving boat\n",
         Category.Movement
      );
      this.field3169.method401(this::lambda$new$17);
   }

   @Override
   public void onEnable() {
      this.field3151.method461().method903(this).method2142();
   }

   @Override
   public void onDisable() {
      this.field3151.method461().method903(this).method1416();
   }

   @EventHandler
   private void method1791(PrePacketSendEvent var1) {
      this.field3151.method461().method903(this).method1853(var1);
   }

   public boolean method1792() {
      return !this.isEnabled() ? false : this.field3151.method461().method903(this).method2115();
   }

   @EventHandler
   private void method1793(PreTickEvent var1) {
      this.field3151.method461().method903(this).method2072(var1);
   }

   @EventHandler
   private void method1794(PlayerTravelEvent var1) {
      this.field3151.method461().method903(this).method1794(var1);
   }

   @EventHandler
   private void method1795(PacketBundleEvent var1) {
      this.field3151.method461().method903(this).method2042(var1);
   }

   @EventHandler
   private void method1796(Render3DEvent var1) {
      this.field3151.method461().method903(this).method2071(var1);
   }

   public boolean method1797() {
      return !this.isEnabled() ? false : this.field3151.method461().method903(this).method2114();
   }

   public boolean method1798(VehicleMoveS2CPacket packet) {
      return !this.isEnabled() ? false : this.field3151.method461().method903(this).method1798(packet);
   }

   private void lambda$new$17(Boolean var1) {
      ((nk)BoatFlyMode.Grim.method903(this)).method1904();
   }

   private boolean lambda$new$16() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$15() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$14() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$13() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$12() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$11() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$10() {
      return this.field3151.method461() == BoatFlyMode.Grim;
   }

   private boolean lambda$new$9() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$8() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$7() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$6() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$5() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$4() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$3() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$2() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$1() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }

   private boolean lambda$new$0() {
      return this.field3151.method461() == BoatFlyMode.NCP;
   }
}
