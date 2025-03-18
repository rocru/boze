package dev.boze.client.systems.modules.client;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class Baritone extends Module {
   public static final Baritone field2324 = new Baritone();
   private final BooleanSetting field2325 = new BooleanSetting("Legit", false, "Legit mining mode");
   private final BooleanSetting field2326 = new BooleanSetting("Backfill", false, "Backfill tunnels");
   private final SettingCategory field2327 = new SettingCategory("Allow", "What baritone is allowed to do");
   private final BooleanSetting field2328 = new BooleanSetting("Breaking", true, "Allow breaking blocks", this.field2327);
   private final BooleanSetting field2329 = new BooleanSetting("Placing", true, "Allow placing blocks", this.field2327);
   private final BooleanSetting field2330 = new BooleanSetting("Sprinting", true, "Allow sprinting", this.field2327);
   private final BooleanSetting field2331 = new BooleanSetting("Parkour", false, "Allow parkour", this.field2327);
   private final BooleanSetting field2332 = new BooleanSetting("ParkourPlace", false, "Allow placing blocks to parkour/jump onto", this.field2327);
   private final MinMaxSetting field2333 = new MinMaxSetting("BlockPlacement", 20.0, 1.0, 40.0, 0.1, "Cost (for algorithm) to place a block");
   private final MinMaxSetting field2334 = new MinMaxSetting(
      "CostHeuristic", 3.563, 3.5, 5.0, 0.01, "Cost heuristic, higher = faster algorithm but less optimal paths"
   );
   private final BooleanSetting field2335 = new BooleanSetting("RenderCached", false, "Render cached chunks");
   private final FloatSetting field2336 = new FloatSetting("Opacity", 0.5F, 0.05F, 1.0F, 0.05F, "Opacity for rendering cached chunks", this.field2335);
   private final BooleanSetting field2337 = new BooleanSetting("Avoid", false, "Avoid mobs and spawners");
   private final IntSetting field2338 = new IntSetting("FollowRadius", 3, 1, 10, 1, "Radius for following others");
   private final BooleanSetting field2339 = new BooleanSetting("ScanDroppedItems", true, "Scan and pickup dropped items");
   private final Settings field2340 = BaritoneAPI.getSettings();

   private Baritone() {
      super("Baritone", "Configure Baritone from your GUI", Category.Client);
      this.setEnabled(true);
      this.field2325.method401(var14 -> this.field2340.legitMine.value = var14);
      this.field2326.method401(var13 -> this.field2340.backfill.value = var13);
      this.field2328.method401(var12 -> this.field2340.allowBreak.value = var12);
      this.field2329.method401(var11 -> this.field2340.allowPlace.value = var11);
      this.field2330.method401(var10 -> this.field2340.allowSprint.value = var10);
      this.field2331.method401(var9 -> this.field2340.allowParkour.value = var9);
      this.field2332.method401(var8 -> this.field2340.allowParkourPlace.value = var8);
      this.field2333.method401(var7 -> this.field2340.blockPlacementPenalty.value = var7);
      this.field2334.method401(var6 -> this.field2340.costHeuristic.value = var6);
      this.field2335.method401(var5 -> this.field2340.renderCachedChunks.value = var5);
      this.field2336.method401(var4 -> this.field2340.cachedChunksOpacity.value = var4);
      this.field2337.method401(var3 -> this.field2340.avoidance.value = var3);
      this.field2338.method401(var2 -> this.field2340.followRadius.value = var2);
      this.field2339.method401(var1 -> this.field2340.mineScanDroppedItems.value = var1);
   }

   @Override
   public boolean setEnabled(boolean newState) {
      return false;
   }
}
