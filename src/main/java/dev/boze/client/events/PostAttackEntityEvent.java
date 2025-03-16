package dev.boze.client.events;

import net.minecraft.entity.Entity;

public class PostAttackEntityEvent extends EntityEvent {
   private static final PostAttackEntityEvent INSTANCE = new PostAttackEntityEvent();

   public static PostAttackEntityEvent method1084(Entity entity) {
      INSTANCE.entity = entity;
      return INSTANCE;
   }
}
