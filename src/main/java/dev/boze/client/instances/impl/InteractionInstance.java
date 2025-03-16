package dev.boze.client.instances.impl;

import dev.boze.api.interaction.Rotation;
import dev.boze.api.internal.interfaces.IInteraction;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class InteractionInstance implements IInteraction, IMinecraft {
   public Rotation calculateAngle(Vec3d to) {
      return mc.player == null ? null : this.calculateAngle(EntityUtil.method2144(mc.player), to);
   }

   public Rotation calculateAngle(Vec3d from, Vec3d to) {
      double var6 = to.x - from.x;
      double var8 = (to.y - from.y) * -1.0;
      double var10 = to.z - from.z;
      double var12 = (double)MathHelper.sqrt((float)(var6 * var6 + var10 * var10));
      float var14 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var10, var6)) - 90.0);
      float var15 = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(var8, var12)));
      if (var15 > 90.0F) {
         var15 = 90.0F;
      } else if (var15 < -90.0F) {
         var15 = -90.0F;
      }

      return new Rotation(var14, var15);
   }

   public void sync() {
      if (mc.interactionManager != null) {
         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
      }
   }

   public int currentSlot() {
      return mc.interactionManager == null ? -1 : ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).getLastSelectedSlot();
   }
}
