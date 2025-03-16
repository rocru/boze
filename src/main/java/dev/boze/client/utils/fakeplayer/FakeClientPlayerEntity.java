package dev.boze.client.utils.fakeplayer;

import com.mojang.authlib.GameProfile;
import dev.boze.client.mixininterfaces.ILivingEntityClientAttack;
import dev.boze.client.mixininterfaces.IOtherClientPlayerEntity;
import dev.boze.client.utils.IMinecraft;
import java.util.function.BooleanSupplier;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.damage.DamageSource;

public class FakeClientPlayerEntity extends OtherClientPlayerEntity implements IMinecraft, IOtherClientPlayerEntity, ILivingEntityClientAttack {
   private BooleanSupplier field1304 = FakeClientPlayerEntity::lambda$new$0;
   public FakePositions field1305;

   public FakeClientPlayerEntity(ClientWorld clientWorld) {
      this(clientWorld, mc.player.getGameProfile());
   }

   public FakeClientPlayerEntity(ClientWorld clientWorld, GameProfile gameProfile) {
      super(clientWorld, gameProfile);
   }

   protected void scheduleVelocityUpdate() {
   }

   public float getYaw() {
      return super.getYaw();
   }

   public float getPitch() {
      return super.getPitch();
   }

   public float getHeadYaw() {
      return super.getHeadYaw();
   }

   public void tickMovement() {
      if (this.field1305 != null) {
         this.prevX = this.getX();
         this.prevY = this.getY();
         this.prevZ = this.getZ();
         this.setPosition(this.field1305.method2174().x, this.field1305.method2174().y, this.field1305.method2174().z);
         this.setRotation(this.field1305.method2175(), this.field1305.method2176());
         this.setHeadYaw(this.field1305.method2177());
         this.field1305 = null;
      }

      this.prevStrideDistance = this.strideDistance;
      this.tickHandSwing();
      float var4 = this.isOnGround() && !this.isDead() ? (float)Math.min(0.1, this.getVelocity().horizontalLength()) : 0.0F;
      this.strideDistance = this.strideDistance + (var4 - this.strideDistance) * 0.4F;
      this.getWorld().getProfiler().push("push");
      this.tickCramming();
      this.getWorld().getProfiler().pop();
   }

   @Override
   public boolean shouldClientAttack() {
      return this.field1304.getAsBoolean();
   }

   @Override
   public boolean returnDamage(DamageSource source, float amount) {
      IOtherClientPlayerEntity.super.returnDamage(source, amount);
      return true;
   }

   @Override
   public boolean shouldDamage() {
      return true;
   }

   @Override
   public boolean boze$hasMoved() {
      return true;
   }

   public void method561(BooleanSupplier shouldClientAttack) {
      this.field1304 = shouldClientAttack;
   }

   private static boolean lambda$new$0() {
      return true;
   }
}
