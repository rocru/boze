package dev.boze.client.systems.modules.render;

import dev.boze.client.events.*;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.entry.RegistryEntry;

public class NoRender extends Module {
   public static final NoRender INSTANCE = new NoRender();
   private final PerformanceSettings field3608 = new PerformanceSettings();
   private final HudSettings field3609 = new HudSettings();
   private final WorldRenderingSettings field3610 = new WorldRenderingSettings();
   private final BlockEntitySetting field3611 = new BlockEntitySetting("BlockEntities", "Don't render these block entities");
   private final EntitySettings field3612 = new EntitySettings();
   private final ParticleSettings field3613 = new ParticleSettings();
   private final StringListSetting field3614 = new StringListSetting("Particles", "Don't render these particles");
   private final MiscSettings field3615 = new MiscSettings();

   public static boolean method1971() {
      return INSTANCE.isEnabled() && INSTANCE.field3608.field2218.method419();
   }

   public static boolean method1972() {
      return INSTANCE.isEnabled() && INSTANCE.field3608.field2219.method419();
   }

   public static boolean method1973() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2179.method419();
   }

   public static boolean method1974() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2181.method419();
   }

   public static boolean method1975() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2182.method419();
   }

   public static boolean method1976() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2184.method419();
   }

   public static boolean method1977() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2185.method419();
   }

   public static boolean method1978() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2186.method419();
   }

   public static boolean method1979() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2187.method419();
   }

   public static boolean method1980(RegistryEntry<StatusEffect> effect) {
      return INSTANCE.isEnabled() && INSTANCE.field3609.method1280(effect);
   }

   public static boolean method1981() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2180.method419();
   }

   public static boolean method1982() {
      return INSTANCE.isEnabled() && INSTANCE.field3609.field2192.method419();
   }

   public static boolean method1983(FogType fogType) {
      return INSTANCE.isEnabled() && INSTANCE.field3610.method1302(fogType);
   }

   public static boolean method1984() {
      return INSTANCE.isEnabled() && INSTANCE.field3610.field2226.method419();
   }

   public static boolean method1985() {
      return INSTANCE.isEnabled() && INSTANCE.field3610.field2227.method419();
   }

   public static boolean method1986() {
      return INSTANCE.isEnabled() && INSTANCE.field3610.field2228.method419();
   }

   public static boolean method1987() {
      return INSTANCE.isEnabled() && INSTANCE.field3610.field2230.method419();
   }

   public static boolean method1988() {
      return INSTANCE.isEnabled() && INSTANCE.field3610.field2231.method419();
   }

   public static boolean method1989() {
      return INSTANCE.isEnabled() && INSTANCE.field3610.field2232.method419();
   }

   public static boolean method1990() {
      return INSTANCE.isEnabled() && INSTANCE.field3612.field95.method419();
   }

   public static boolean method1991() {
      return INSTANCE.isEnabled() && INSTANCE.field3612.field96.method419();
   }

   public static boolean method1992() {
      return INSTANCE.isEnabled() && INSTANCE.field3612.field98.method419();
   }

   public static boolean method1993(LivingEntity entity, EquipmentSlot equipmentSlot) {
      return INSTANCE.isEnabled() && INSTANCE.field3612.method1993(entity, equipmentSlot);
   }

   public static boolean method1994() {
      return INSTANCE.isEnabled() && INSTANCE.field3615.field2205.method419();
   }

   public static boolean method1995() {
      return INSTANCE.isEnabled() && INSTANCE.field3615.field2206.method419();
   }

   public static boolean method1996() {
      return INSTANCE.isEnabled() && INSTANCE.field3615.field2209.method419();
   }

   public static boolean method1997() {
      return INSTANCE.isEnabled() && INSTANCE.field3615.field2208.method419();
   }

   public static boolean method1998(ParticleEffect parameters) {
      if (INSTANCE.isEnabled() && INSTANCE.field3613.field2216.method419()) {
         ParticleType var4 = parameters.getType();
         if (INSTANCE.field3614.method2032().contains(var4)) {
            return true;
         }
      }

      return false;
   }

   private NoRender() {
      super("NoRender", "Removes specified things from rendering", Category.Render);
   }

   public static boolean method1999(Entity entity) {
      return INSTANCE.isEnabled() && (INSTANCE.field3608.method1300(entity) || INSTANCE.field3612.method2055(entity));
   }

   public static boolean method2000(Particle particle) {
      return INSTANCE.isEnabled() && (INSTANCE.field3608.method1301(particle) || INSTANCE.field3613.method1299(particle));
   }

   @EventHandler
   public void method2001(PacketBundleEvent event) {
      if (event.packet instanceof EntitySpawnS2CPacket var5
         && var5.getEntityType() == EntityType.WARDEN
         && this.field3612.field92.method419()
         && this.field3612.field93.method419()) {
         event.method1020();
      }
   }

   @EventHandler
   public void method2002(PlayerOverlayEvent event) {
      this.field3609.method1281(event);
   }

   @EventHandler
   public void method2003(PreRenderEvent event) {
      if (this.field3610.field2229.method419() && this.field3611.method2032().contains(event.blockEntity.getType())) {
         event.method1020();
      }
   }

   @EventHandler
   public void method2004(SoundPlayEvent event) {
      this.field3615.method1298(event);
   }

   @EventHandler
   public void method2005(PreTickEvent event) {
      if (this.field3615.field2210.method419() && mc.options.getNarrator().getValue() != NarratorMode.OFF) {
         mc.options.getNarrator().setValue(NarratorMode.OFF);
      }
   }
}
