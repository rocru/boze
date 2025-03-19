package dev.boze.client.events;

import dev.boze.client.utils.ActionWrapper;

import java.util.LinkedList;

public class MovementEvent extends CancelableEvent {
   private static final MovementEvent INSTANCE = new MovementEvent();
   public double field1930;
   public double field1931;
   public double field1932;
   public float yaw;
   public float pitch;
   public boolean isOnGround;
   public boolean isSprinting;
   public boolean isSneaking;
   public final LinkedList<ActionWrapper> field1933 = new LinkedList();
   public boolean field1934 = false;

   public void method1074(ActionWrapper action) {
      if (action.field3900 && !this.method1022()) {
         this.method1021(true);
         this.yaw = action.field3902;
         this.pitch = action.field3903;
      }

      this.field1933.add(action);
   }

   public static MovementEvent method1075(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean sneaking) {
      INSTANCE.field1930 = x;
      INSTANCE.field1931 = y;
      INSTANCE.field1932 = z;
      INSTANCE.yaw = yaw;
      INSTANCE.pitch = pitch;
      INSTANCE.isOnGround = onGround;
      INSTANCE.isSprinting = sprinting;
      INSTANCE.isSneaking = sneaking;
      INSTANCE.field1933.clear();
      INSTANCE.field1934 = false;
      INSTANCE.method1021(false);
      return INSTANCE;
   }
}
