package dev.boze.client.events;

public class PlayerGrimV3BypassEvent extends CancelableEvent {
    private static final PlayerGrimV3BypassEvent INSTANCE = new PlayerGrimV3BypassEvent();
    public float yaw;

    public static PlayerGrimV3BypassEvent method1079() {
        INSTANCE.method1021(false);
        return INSTANCE;
    }
}
