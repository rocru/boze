package dev.boze.client.enums;

enum InteractionAwaitMode {
    Off,
    Semi,
    Strict;

    private static final InteractionAwaitMode[] field1715 = method833();

    private static InteractionAwaitMode[] method833() {
        return new InteractionAwaitMode[]{Off, Semi, Strict};
    }
}
