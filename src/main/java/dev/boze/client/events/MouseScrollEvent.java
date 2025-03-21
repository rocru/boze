package dev.boze.client.events;

public class MouseScrollEvent extends CancelableEvent {
    private static final MouseScrollEvent INSTANCE = new MouseScrollEvent();
    public double vertical;

    public static MouseScrollEvent method1072(double value) {
        INSTANCE.method1021(false);
        INSTANCE.vertical = value;
        return INSTANCE;
    }
}
