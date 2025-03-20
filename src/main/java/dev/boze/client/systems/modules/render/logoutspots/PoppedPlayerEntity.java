package dev.boze.client.systems.modules.render.logoutspots;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.systems.modules.render.PopChams;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class5923;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

class PoppedPlayerEntity extends FakePlayerEntity {
   private final UUID field1273;
   private final long field1274;
   private final double field1275;
   private float field1276;
   private final float field1277;
   private final float field1278;
   final PopChams field1279;

   public PoppedPlayerEntity(final PopChams arg, PlayerEntity player) {
      super(player, "Popped Player", 20.0F, false);
      this.field1279 = arg;
      this.field1273 = player.getUuid();
      this.field1274 = System.currentTimeMillis();
      this.field1275 = player.getY();
      this.method547(player);
      this.handSwinging = player.handSwinging;
      this.handSwingProgress = player.handSwingProgress;
      this.handSwingTicks = player.handSwingTicks;
      this.field1276 = player.getHandSwingProgress(1.0F);
      this.field1277 = player.limbAnimator.getSpeed(1.0F);
      this.field1278 = player.limbAnimator.getPos(1.0F);
      this.setSneaking(player.isSneaking());
      this.setSprinting(player.isSprinting());
   }

   public boolean method550(Renderer3D renderer, float tickDelta) {
      if (this.field1279.field3642.getValue()) {
         PlayerEntity var6 = mc.world.getPlayerByUuid(this.field1273);
         if (var6 != null) {
            this.method548(var6);
            this.handSwinging = var6.handSwinging;
            this.handSwingProgress = var6.handSwingProgress;
            this.handSwingTicks = var6.handSwingTicks;
            this.setSneaking(var6.isSneaking());
            this.setSprinting(var6.isSprinting());
         }
      }

      this.lastRenderY = this.getY();
      ((IVec3d)this.getPos()).boze$setY(this.field1275 + (double)(System.currentTimeMillis() - this.field1274) * 0.01 * this.field1279.field3657.getValue());
      double var10 = 1.0 - (double)(System.currentTimeMillis() - this.field1274) * 0.001 * this.field1279.field3656.getValue();
      if (var10 <= 0.0) {
         return true;
      } else {
         BozeDrawColor var8 = this.field1279.field3643.getValue().copy();
         BozeDrawColor var9 = this.field1279.field3644.getValue().copy();
         var8.field411 = (int)((double)var8.field411 * var10);
         var9.field411 = (int)((double)var9.field411 * var10);
         if (this.field1279.field3642.getValue()) {
            Class5923.method68(renderer, tickDelta, this, var8, var9, ShapeMode.Full);
         } else {
            Class5923.method67(renderer, tickDelta, this, var8, var9, ShapeMode.Full, this.field1276, this.field1277, this.field1278);
         }

         return false;
      }
   }
}
