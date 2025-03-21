package dev.boze.client.systems.modules.render;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.Objects;

public class FOV extends Module {
    public static final FOV INSTANCE = new FOV();
    public final IntSetting field3528 = new IntSetting("FOV", 120, 1, 179, 1, "In-game FOV");

    public FOV() {
        super("FOV", "Change your FOV", Category.Render);
        this.field3528.method401(this::lambda$new$0);
    }

    @EventHandler
    public void method1937(Render3DEvent event) {
        if (!Objects.equals(mc.options.getFov().getValue(), this.field3528.getValue()) && !Zoom.INSTANCE.isEnabled()) {
            mc.options.getFov().setValue(this.field3528.getValue());
            //((ISimpleOption)mc.options.getFov()).boze$setOptionValue(this.field3528.getValue());
        }
    }

    private void lambda$new$0(Integer var1) {
        if (this.isEnabled()) {
            mc.options.getFov().setValue(var1);
//         ((ISimpleOption)mc.options.getFov()).boze$setOptionValue(var1);
        }
    }
}
