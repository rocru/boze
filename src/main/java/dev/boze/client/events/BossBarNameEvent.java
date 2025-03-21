package dev.boze.client.events;

import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;

public class BossBarNameEvent {
    private static final BossBarNameEvent field1905 = new BossBarNameEvent();
    public ClientBossBar field1906;
    public Text text;

    public static BossBarNameEvent method1053(ClientBossBar bossBar, Text name) {
        field1905.field1906 = bossBar;
        field1905.text = name;
        return field1905;
    }
}
