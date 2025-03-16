package dev.boze.client.mixininterfaces;

public interface ILivingEntityClientAttack {
   default boolean shouldClientAttack() {
      return false;
   }
}
