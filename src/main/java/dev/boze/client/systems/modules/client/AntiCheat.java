package dev.boze.client.systems.modules.client;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class AntiCheat extends Module {
   public static final AntiCheat INSTANCE = new AntiCheat();
   public final BooleanSetting field2314 = new BooleanSetting(
      "RayCastFix",
      false,
      "=When rotating, modify cross-hair ray casts\nMake them use server rotation, not client rotation\nBlockHighlight is highly recommended if this is on\n"
   );
   public final BooleanSetting field2315 = new BooleanSetting(
      "SensFix", false, "=Vanilla rotations depend on mouse sensitivity\nThis will adjust Boze rotations to match\nMost anarchy servers don't check for this\n"
   );
   public final BooleanSetting field2316 = new BooleanSetting(
      "StaticFix",
      false,
      "=In some edge-cases, Boze rotations may get de-synced\nOnly happens when rotating to same yaw/pitch for a while\nThis will prevent that from happening\nIt will slightly adjust your rotation to avoid de-sync\n"
   );
   public final IntSetting field2317 = new IntSetting(
      "HoldRotations", 0, 0, 20, 1, "=Most modules normally rotate for 1 tick\nThis will hold that rotation for this many ticks after\n"
   );
   public final BooleanSetting field2318 = new BooleanSetting(
      "PlaceOnGhost", true, "=Place on ghost blocks\nThis will place on blocks before they're confirmed\n"
   );
   final BooleanSetting field2319 = new BooleanSetting(
      "StrictMultiTask", false, "=When MultiTask is off in modules, they pause while eating\nWith this on, they'll also pause while breaking blocks\n"
   );
   public final BooleanSetting field2320 = new BooleanSetting(
      "SlotSync",
      false,
      "=Sync selected slot after Boze silent swap\nOff by default, Vanilla takes care of this\nThis is only needed when running alongside bad clients\nThat don't check your current server slot\n"
   );
   public final BooleanSetting field2321 = new BooleanSetting(
      "KickDisconnect", false, "=Makes server kick you when you press disconnect\nPrevents logout de-sync\n"
   );
   public final BooleanSetting field2322 = new BooleanSetting(
      "StrictInventory",
      false,
      "Strict Inventory option for inventory-affecting modules\nThis is not needed on most servers\nIf you get stuck after modules move around items in inventory, enable\n"
   );
   public final BooleanSetting field2323 = new BooleanSetting(
      "CancelExpected", false, "Cancel expected slot update packets\nThis can cause desyncs with alt swaps\n"
   );

   private AntiCheat() {
      super("AntiCheat", "Global Anti-Cheat related options for non-ghost mode\nAdvanced options, only change if you know what you're doing\n", Category.Client);
      this.setNotificationLengthLimited();
   }
}
