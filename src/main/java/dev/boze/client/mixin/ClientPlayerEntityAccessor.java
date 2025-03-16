package dev.boze.client.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientPlayerEntity.class})
public interface ClientPlayerEntityAccessor {
   @Accessor("autoJumpEnabled")
   void setAutoJumpEnabled(boolean var1);

   @Accessor("lastOnGround")
   void setLastOnGround(boolean var1);

   @Accessor("lastOnGround")
   boolean getLastOnGround();

   @Accessor("lastX")
   void setLastX(double var1);

   @Accessor("lastX")
   double getLastX();

   @Accessor("lastBaseY")
   void setLastY(double var1);

   @Accessor("lastBaseY")
   double getLastY();

   @Accessor("lastZ")
   void setLastZ(double var1);

   @Accessor("lastZ")
   double getLastZ();

   @Accessor("lastYaw")
   void setLastYaw(float var1);

   @Accessor("lastYaw")
   float getLastYaw();

   @Accessor("lastPitch")
   void setLastPitch(float var1);

   @Accessor("lastPitch")
   float getLastPitch();

   @Accessor("ticksSinceLastPositionPacketSent")
   void setTicksSinceLastPositionPacketSent(int var1);

   @Accessor("ticksSinceLastPositionPacketSent")
   int getTicksSinceLastPositionPacketSent();

   @Accessor("lastSneaking")
   boolean getLastSneaking();

   @Accessor("lastSneaking")
   void setLastSneaking(boolean var1);

   @Accessor("lastSprinting")
   boolean getLastSprinting();

   @Accessor("lastSprinting")
   void setLastSprinting(boolean var1);
}
