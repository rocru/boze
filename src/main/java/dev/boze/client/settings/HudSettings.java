package dev.boze.client.settings;

import dev.boze.client.enums.PlayerOverlay;
import dev.boze.client.events.PlayerOverlayEvent;
import dev.boze.client.settings.generic.SettingsGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;

class HudSettings implements SettingsGroup {
   final BooleanSetting field2179 = new BooleanSetting("Scoreboard", false, "Don't render scoreboard");
   final BooleanSetting field2180 = new BooleanSetting("PotionIcons", false, "Don't render potion icons");
   final BooleanSetting field2181 = new BooleanSetting("BossBar", false, "Don't render boss bars");
   final BooleanSetting field2182 = new BooleanSetting("HotbarNames", false, "Don't render selected item name above hotbar");
   private final SettingCategory field2183 = new SettingCategory("Overlays", "Options for not rendering overlays");
   final BooleanSetting field2184 = new BooleanSetting("Portal", true, "Don't render portal overlay", this.field2183);
   final BooleanSetting field2185 = new BooleanSetting("Spyglass", true, "Don't render spyglass overlay", this.field2183);
   final BooleanSetting field2186 = new BooleanSetting("PowderedSnow", true, "Don't render powdered snow overlay", this.field2183);
   final BooleanSetting field2187 = new BooleanSetting("GUIBackground", false, "Don't render GUI background", this.field2183);
   private final BooleanSetting field2188 = new BooleanSetting("Fire", true, "Don't render fire overlay", this.field2183);
   private final BooleanSetting field2189 = new BooleanSetting("Liquid", true, "Don't render liquid overlay", this.field2183);
   private final BooleanSetting field2190 = new BooleanSetting("Pumpkin", true, "Don't render pumpkin overlay", this.field2183);
   private final BooleanSetting field2191 = new BooleanSetting("Blindness", true, "Don't render blindness overlay", this.field2183);
   final BooleanSetting field2192 = new BooleanSetting("Nausea", true, "Don't render nausea wobble overlay", this.field2183);
   private final BooleanSetting field2193 = new BooleanSetting("Walls", true, "Don't render wall (in block) overlay", this.field2183);
   private final SettingBlock field2194 = new SettingBlock(
      "HUD",
      "HUD rendering settings",
      this.field2179,
      this.field2180,
      this.field2181,
      this.field2182,
      this.field2183,
      this.field2184,
      this.field2185,
      this.field2186,
      this.field2187,
      this.field2188,
      this.field2189,
      this.field2190,
      this.field2191,
      this.field2192,
      this.field2193
   );

   @Override
   public Setting<?>[] get() {
      return this.field2194.method472();
   }

   boolean method1280(RegistryEntry<StatusEffect> var1) {
      return var1 == StatusEffects.BLINDNESS && this.field2191.method419();
   }

   void method1281(PlayerOverlayEvent var1) {
      if (var1.overlay == PlayerOverlay.Fire && this.field2188.method419()
         || var1.overlay == PlayerOverlay.Pumpkin && this.field2190.method419()
         || var1.overlay == PlayerOverlay.Liquid && this.field2189.method419()
         || var1.overlay == PlayerOverlay.Wall && this.field2193.method419()) {
         var1.method1020();
      }
   }
}
