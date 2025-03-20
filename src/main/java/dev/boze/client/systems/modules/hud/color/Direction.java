package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.hud.ColorHUDModule;
import net.minecraft.util.math.Vec3d;

public class Direction extends ColorHUDModule {
   public static final Direction INSTANCE = new Direction();
   private final BooleanSetting field610 = new BooleanSetting("InterCardinal", false, "Show intercardinal direction");
   private final double field611 = 0.5;

   public Direction() {
      super("Direction", "Shows which direction you're currently facing");
   }

   @Override
   protected String method1562() {
      return this.field610.getValue() ? this.method329() : this.method330();
   }

   @Override
   protected String method1563() {
      return "[" + this.method328() + "]";
   }

   private String method328() {
      String var4 = "";
      String var5 = mc.player.getHorizontalFacing().getName().toLowerCase();
      switch (var5) {
         case "north":
            var4 = "-Z";
            break;
         case "south":
            var4 = "+Z";
            break;
         case "west":
            var4 = "-X";
            break;
         case "east":
            var4 = "+X";
      }

      return var4;
   }

   private String method329() {
      Vec3d var4 = mc.player.getRotationVec(1.0F);
      if (var4.x <= -0.5) {
         if (var4.z > 0.5) {
            return "South West";
         } else {
            return var4.z < -0.5 ? "North West" : "West";
         }
      } else if (var4.x >= 0.5) {
         if (var4.z > 0.5) {
            return "South East";
         } else {
            return var4.z < -0.5 ? "North East" : "East";
         }
      } else if (var4.z > 0.5) {
         return "South";
      } else {
         return var4.z < -0.5 ? "North" : "South";
      }
   }

   private String method330() {
      String var4 = mc.player.getHorizontalFacing().getName().toLowerCase();
      switch (var4) {
         case "north":
            var4 = "North";
            break;
         case "south":
            var4 = "South";
            break;
         case "west":
            var4 = "West";
            break;
         case "east":
            var4 = "East";
      }

      return var4;
   }
}
