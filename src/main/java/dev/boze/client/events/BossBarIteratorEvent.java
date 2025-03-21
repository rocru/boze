package dev.boze.client.events;

import net.minecraft.client.gui.hud.ClientBossBar;

import java.util.Iterator;

public class BossBarIteratorEvent {
    private static final BossBarIteratorEvent field1903 = new BossBarIteratorEvent();
    public Iterator<ClientBossBar> field1904;

    public static BossBarIteratorEvent method1052(Iterator<ClientBossBar> iterator) {
        field1903.field1904 = iterator;
        return field1903;
    }
}
