package dev.boze.client.systems.modules.client;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.MacroManagerComponent;
import dev.boze.client.settings.MacroManagerSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class Macros extends Module {
    public static final Macros INSTANCE = new Macros();
    public final MacroManagerSetting field2400 = new MacroManagerSetting("Manage", "Manage macros");

    public Macros() {
        super("Macros", "Manage macros", Category.Client);
        this.method219(this::lambda$new$0);
    }

    @Override
    public boolean setEnabled(boolean newState) {
        return false;
    }

    private ScaledBaseComponent lambda$new$0() {
        return new MacroManagerComponent(this.field2400);
    }
}
