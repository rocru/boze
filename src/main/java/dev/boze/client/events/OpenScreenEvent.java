package dev.boze.client.events;

import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends CancelableEvent {
    private static final OpenScreenEvent INSTANCE = new OpenScreenEvent();
    public Screen screen;

    public static OpenScreenEvent method1037(Screen screen) {
        INSTANCE.screen = screen;
        INSTANCE.method1021(false);
        return INSTANCE;
    }
}
