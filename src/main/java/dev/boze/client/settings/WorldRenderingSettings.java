package dev.boze.client.settings;

import dev.boze.client.enums.WorldRenderingFog;
import dev.boze.client.settings.generic.SettingsGroup;
import net.minecraft.client.render.BackgroundRenderer.FogType;

class WorldRenderingSettings implements SettingsGroup {
   private final EnumSetting<WorldRenderingFog> field2225 = new EnumSetting<>(
           "Fog",
           WorldRenderingFog.Both,
           "Don't render fog\nOff: Don't NoRender Fog\nTerrain: Disables fog rendering for terrain\nSky: Disables fog rendering for sky\nBoth: Disables fog rendering for both terrain and sky"
   );
   final BooleanSetting field2226 = new BooleanSetting("Weather", true, "Don't render weather");
   final BooleanSetting field2227 = new BooleanSetting("WorldBorder", false, "Don't render world border");
   final BooleanSetting field2228 = new BooleanSetting("Signs", false, "Don't render signs");
   final BooleanSetting field2229 = new BooleanSetting(
      "CustomBlockEntities",
      false,
      "Don't render custom block entities\nUse '.set norender blockentities add <block entity>' to add block entities to the list\nUse '.set norender blockentities del <block entity>' to remove block entities from the list\nUse '.set norender blockentities list' to list all block entities in the list"
   );
   final BooleanSetting field2230 = new BooleanSetting("EnchantGlint", false, "Don't render enchant glint");
   final BooleanSetting field2231 = new BooleanSetting("Blindness", true, "Don't render blindness overlay");
   final BooleanSetting field2232 = new BooleanSetting("Darkness", true, "Don't render darkness");
   final SettingBlock field2233 = new SettingBlock(
      "World",
      "World rendering settings",
      this.field2225,
      this.field2226,
      this.field2227,
      this.field2228,
      this.field2229,
      this.field2230,
      this.field2231,
      this.field2232
   );

   @Override
   public Setting<?>[] get() {
      return this.field2233.method472();
   }

   boolean method1302(FogType var1) {
      return this.field2225.method461() == WorldRenderingFog.Both
         || var1 == FogType.FOG_TERRAIN && this.field2225.method461() == WorldRenderingFog.Terrain
         || var1 == FogType.FOG_SKY && this.field2225.method461() == WorldRenderingFog.Sky;
   }
}
