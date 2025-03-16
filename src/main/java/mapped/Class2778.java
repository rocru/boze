package mapped;

import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.notification.Notifications;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class Class2778 {
   public static double method5435() {
      return BaseComponent.scaleFactor * 4.0;
   }

   public static double method5436() {
      return Notifications.DELETE.method2091();
   }

   public static void method5437() {
      MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   public static boolean method5438(double mouseX, double mouseY, double x, double y, double width, double height) {
      return var2837 >= var2839 && var2837 <= var2839 + var2841 && var2838 >= var2840 && var2838 <= var2840 + var2842;
   }

   public static double method5439(double itemX, double itemWidth, int buttonIndex) {
      return var2843 + var2844 - BaseComponent.scaleFactor * 4.0 - method5436() - (double)var2845 * (method5436() + method5435());
   }

   public static double method5440(double itemY, double itemHeight) {
      return var2846 + var2847 * 0.5;
   }

   public static boolean method5441(double mouseX, double mouseY, double buttonX, double buttonY) {
      double var8 = method5436();
      return method5438(var2848, var2849, var2850, var2851 - var8 * 0.5, var8, var8);
   }
}
