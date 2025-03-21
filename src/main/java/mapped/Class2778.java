package mapped;

import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.notification.Notifications;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class Class2778 {
    public Class2778() {
        super();
    }

    public static double method5435() {
        return BaseComponent.scaleFactor * 4.0;
    }

    public static double method5436() {
        return Notifications.DELETE.method2091();
    }

    public static void method5437() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }

    public static boolean method5438(final double mouseX, final double mouseY, final double x, final double y, final double width, final double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static double method5439(final double itemX, final double itemWidth, final int buttonIndex) {
        return itemX + itemWidth - BaseComponent.scaleFactor * 4.0 - method5436() - buttonIndex * (method5436() + method5435());
    }

    public static double method5440(final double itemY, final double itemHeight) {
        return itemY + itemHeight * 0.5;
    }

    public static boolean method5441(final double mouseX, final double mouseY, final double buttonX, final double buttonY) {
        final double method5436 = method5436();
        return method5438(mouseX, mouseY, buttonX, buttonY - method5436 * 0.5, method5436, method5436);
    }
}
