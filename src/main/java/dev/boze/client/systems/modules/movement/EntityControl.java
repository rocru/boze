package dev.boze.client.systems.modules.movement;

import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class EntityControl extends Module {
    public static final EntityControl INSTANCE = new EntityControl();

    public EntityControl() {
        super("EntityControl", "Control unsaddled entities", Category.Movement);
    }
}
