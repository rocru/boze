package dev.boze.client.events;

import net.minecraft.client.particle.Particle;

public class ParticleEvent extends dX {
   private static final ParticleEvent field1937 = new ParticleEvent();
   public Particle field1938;

   public static ParticleEvent method1078(Particle particle) {
      field1937.field1938 = particle;
      field1937.method1021(false);
      return field1937;
   }
}
