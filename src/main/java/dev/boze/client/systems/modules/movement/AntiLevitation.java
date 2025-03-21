package dev.boze.client.systems.modules.movement;

import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class AntiLevitation extends Module {
    public static final AntiLevitation INSTANCE = new AntiLevitation();

    public AntiLevitation() {
        super("AntiLevitation", "Prevents levitation from making you go up", Category.Movement);
    }
}
