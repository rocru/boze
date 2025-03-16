package dev.boze.client.settings;

import dev.boze.client.settings.generic.SettingsGroup;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.FireworksSparkParticle.FireworkParticle;
import net.minecraft.client.particle.FireworksSparkParticle.Flash;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;

public class PerformanceSettings implements SettingsGroup {
   final BooleanSetting field2218 = new BooleanSetting("Unfocused", false, "Don't render when game is unfocused");
   final BooleanSetting field2219 = new BooleanSetting("Skylight", false, "Don't recalculate skylight");
   private final BooleanSetting field2220 = new BooleanSetting("Gravity", false, "Don't render falling blocks");
   private final BooleanSetting field2221 = new BooleanSetting("ArmorStands", false, "Don't render armor stands");
   private final BooleanSetting field2222 = new BooleanSetting("Fireworks", false, "Don't render fireworks, might mess with elytra flying");
   private final BooleanSetting field2223 = new BooleanSetting("CampFire", true, "Don't render campfires");
   final SettingBlock field2224 = new SettingBlock(
      "Performance", "Performance improving settings", this.field2218, this.field2219, this.field2220, this.field2221, this.field2222, this.field2223
   );

   @Override
   public Setting<?>[] get() {
      return this.field2224.method472();
   }

   public boolean method1300(Entity entity) {
      return entity instanceof ArmorStandEntity && this.field2221.method419()
         || entity instanceof FallingBlockEntity && this.field2220.method419()
         || entity instanceof FireworkRocketEntity && this.field2222.method419();
   }

   public boolean method1301(Particle particle) {
      return (particle instanceof FireworkParticle || particle instanceof Flash) && this.field2222.method419()
         ? true
         : particle instanceof CampfireSmokeParticle && this.field2223.method419();
   }
}
