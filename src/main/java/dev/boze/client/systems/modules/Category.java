package dev.boze.client.systems.modules;

import dev.boze.client.gui.notification.Notifications;

public enum Category {
    Hud(ConfigCategory.Visuals, null, 300.0, 0.2),
    Graph(ConfigCategory.Visuals, null, 300.0, 0.2),
    Combat(ConfigCategory.Main, Notifications.CATEGORY_COMBAT, 0.0, 0.5),
    Movement(ConfigCategory.Main, Notifications.CATEGORY_MOVEMENT, 60.0, 0.45),
    Misc(ConfigCategory.Main, Notifications.CATEGORY_MISC, 120.0, 0.4),
    Render(ConfigCategory.Visuals, Notifications.CATEGORY_RENDER, 180.0, 0.35),
    Legit(ConfigCategory.Main, Notifications.CATEGORY_LEGIT, 240.0, 0.45),
    Client(ConfigCategory.Client, Notifications.CATEGORY_CLIENT, 300.0, 0.3);

    public final ConfigCategory configCategory;
    public final Notifications icon;
    public double hue;
    public double saturation;
    public boolean extended = true;
    public double field42;
    public double field43 = -1.0;
    public boolean locked = true;
    public double scrollOffset = 0.0;
    private static final Category[] field44 = method37();

    Category(ConfigCategory var3, Notifications var4, double var5, double var7) {
        this.configCategory = var3;
        this.icon = var4;
        this.hue = var5;
        this.saturation = var7;
    }

    private static Category[] method37() {
        return new Category[]{Hud, Graph, Combat, Movement, Misc, Render, Legit, Client};
    }
}
