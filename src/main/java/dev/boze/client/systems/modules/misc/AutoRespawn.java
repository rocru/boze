package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.OpenScreenEvent;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.MacroSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Macro;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public static final AutoRespawn INSTANCE = new AutoRespawn();
    private final MacroSetting macro = new MacroSetting("Macro", "The macro to run when auto respawning");
    private final FloatSetting macroDelay = new FloatSetting("MacroDelay", 0.0F, 0.0F, 10.0F, 1.0F, "Delay in seconds before running macro", this::lambda$new$0);
    private final dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();
    private Macro field2896 = null;

    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawn", Category.Misc);
    }

    @Override
    public void onEnable() {
        this.field2896 = null;
    }

    @EventHandler
    public void method1672(MovementEvent event) {
        if (this.field2896 != null && this.timer.hasElapsed(this.macroDelay.getValue() * 1000.0F)) {
            this.field2896.method2142();
            this.field2896 = null;
        }
    }

    @EventHandler
    private void method1673(OpenScreenEvent var1) {
        if (var1.screen instanceof DeathScreen) {
            mc.player.requestRespawn();
            var1.method1020();
            if (!this.macro.getValue().isEmpty()) {
                Macro var5 = this.macro.method467();
                if (var5 != null) {
                    if (this.macroDelay.getValue() > 0.0F) {
                        this.timer.reset();
                        this.field2896 = var5;
                    } else {
                        var5.method2142();
                    }
                }
            }
        }
    }

    private boolean lambda$new$0() {
        return !this.macro.getValue().isEmpty();
    }
}
