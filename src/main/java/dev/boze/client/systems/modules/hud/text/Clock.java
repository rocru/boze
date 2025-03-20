package dev.boze.client.systems.modules.hud.text;

import dev.boze.client.systems.modules.hud.TextHUDModule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Clock extends TextHUDModule {
    public static final Clock INSTANCE = new Clock();

    public Clock() {
        super("Clock", "Shows the time");
    }

    @Override
    protected String method1544() {
        return new SimpleDateFormat("H:mm").format(new Date());
    }
}
