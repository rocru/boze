package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.Boze;
import dev.boze.client.core.Version;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.hud.ColorHUDModule;

public class Watermark extends ColorHUDModule {
    public static final Watermark INSTANCE = new Watermark();
    private final BooleanSetting field613 = new BooleanSetting("Version", true, "Show client version in watermark");
    private final BooleanSetting field614 = new BooleanSetting("Build", false, "Show client build in watermark", Watermark::lambda$new$0);

    public Watermark() {
        super("Watermark", "Shows the client watermark", 0.0, 0.0, 1);
        this.setEnabled(true);
    }

    private static boolean lambda$new$0() {
        return !Boze.BUILD.isEmpty();
    }

    @Override
    protected String method1562() {
        return Options.method1562();
    }

    @Override
    protected String method1563() {
        return (this.field613.getValue() ? Version.tag : "")
                + (this.field614.getValue() && !Boze.BUILD.isEmpty() ? (this.field613.getValue() ? "+" + Boze.BUILD : Boze.BUILD) : "");
    }
}
