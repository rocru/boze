package dev.boze.client.utils.fakeplayer;

import com.mojang.authlib.GameProfile;
import dev.boze.client.systems.modules.render.PopChams;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class FakePlayerEntityWithTotem extends FakeClientPlayerEntity {
   public FakePlayerEntityWithTotem(ClientWorld clientWorld) {
      super(clientWorld);
   }

   public FakePlayerEntityWithTotem(ClientWorld clientWorld, GameProfile gameProfile) {
      super(clientWorld, gameProfile);
   }

   public ItemStack getEquippedStack(EquipmentSlot slot) {
      if (slot == EquipmentSlot.OFFHAND) {
         ItemStack var4 = new ItemStack(Items.TOTEM_OF_UNDYING);
         var4.setCount(1);
         return var4;
      } else {
         return super.getEquippedStack(slot);
      }
   }

   public void setHealth(float health) {
      if (health <= 0.0F) {
         this.method2142();
      } else {
         super.setHealth(health);
      }
   }

   public void remove(RemovalReason reason) {
   }

   public void method2142() {
      EntityStatusS2CPacket var4 = new EntityStatusS2CPacket(this, (byte)35);
      var4.apply(mc.player.networkHandler);
      if (PopChams.INSTANCE.isEnabled()) {
         PopChams.INSTANCE.method2019(this);
      }

      super.setHealth(1.0F);
      this.setAbsorptionAmount(8.0F);
      this.clearStatusEffects();
      this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
      this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
   }
}
