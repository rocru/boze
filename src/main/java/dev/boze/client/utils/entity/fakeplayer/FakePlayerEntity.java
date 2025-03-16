package dev.boze.client.utils.entity.fakeplayer;

import com.mojang.authlib.GameProfile;
import dev.boze.client.utils.IMinecraft;
import java.util.UUID;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayerEntity extends OtherClientPlayerEntity implements IMinecraft {
   public boolean field1265 = false;

   public FakePlayerEntity(PlayerEntity player, String name, float health, boolean copyInventory) {
      super(mc.world, new GameProfile(UUID.randomUUID(), name));
      this.copyPositionAndRotation(player);
      this.prevYaw = this.getYaw();
      this.prevPitch = this.getPitch();
      this.headYaw = player.headYaw;
      this.prevHeadYaw = this.headYaw;
      this.bodyYaw = player.bodyYaw;
      this.prevBodyYaw = this.bodyYaw;
      Byte var8 = (Byte)player.getDataTracker().get(PlayerEntity.PLAYER_MODEL_PARTS);
      this.dataTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, var8);
      this.getAttributes().setFrom(player.getAttributes());
      this.setPose(player.getPose());
      this.capeX = this.getX();
      this.capeY = this.getY();
      this.capeZ = this.getZ();
      if (health <= 20.0F) {
         this.setHealth(health);
      } else {
         this.setHealth(health);
         this.setAbsorptionAmount(health - 20.0F);
      }

      if (copyInventory) {
         this.getInventory().clone(player.getInventory());
      }
   }

   public void method547(PlayerEntity player) {
      this.copyPositionAndRotation(player);
      this.prevYaw = this.getYaw();
      this.prevPitch = this.getPitch();
      this.headYaw = player.headYaw;
      this.prevHeadYaw = this.headYaw;
      this.bodyYaw = player.bodyYaw;
      this.prevBodyYaw = this.bodyYaw;
   }

   public void method548(PlayerEntity player) {
      this.setYaw(player.getYaw());
      this.setPitch(player.getPitch());
      this.prevYaw = player.prevYaw;
      this.prevPitch = player.prevPitch;
      this.headYaw = player.headYaw;
      this.prevHeadYaw = player.prevHeadYaw;
      this.bodyYaw = player.bodyYaw;
      this.prevBodyYaw = player.prevBodyYaw;
   }

   public void method2142() {
      this.unsetRemoved();
      mc.world.addEntity(this);
   }

   public void method1416() {
      mc.world.removeEntity(this.getId(), RemovalReason.DISCARDED);
      this.setRemoved(RemovalReason.DISCARDED);
   }
}
