package dev.boze.client.gui.notification;

public enum NotificationPriority {
    Normal,
    Yellow,
    Red;

    private static final NotificationPriority[] field1664 = method783();

    private static NotificationPriority[] method783() {
        return new NotificationPriority[]{Normal, Yellow, Red};
    }
}
