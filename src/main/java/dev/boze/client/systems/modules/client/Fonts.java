package dev.boze.client.systems.modules.client;

import dev.boze.client.enums.FontShadowMode;
import dev.boze.client.gui.components.scaled.bottomrow.FontSelectComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.FontSetting;
import dev.boze.client.settings.OpacityColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class3031;

public class Fonts extends Module {
    public static final Fonts INSTANCE = new Fonts();
    public final FontSetting field2345 = new FontSetting("Font", "lexend", "Font to use");
    public final EnumSetting<FontShadowMode> field2346 = new EnumSetting<FontShadowMode>(
            "Shadow",
            FontShadowMode.Off,
            "Font shadow (DISABLED TILL RENDER SYSTEM PORT)\n - Off: Don't show shadow\n - Mod: Modify text color for shadow\n - Color: Use custom color for shadow\n"
    );
    public final FloatSetting field2347 = new FloatSetting(
            "Opacity", 0.25F, 0.0F, 1.0F, 0.01F, "Opacity modifier for shadow", this::lambda$new$0, this.field2346
    );
    public final FloatSetting field2348 = new FloatSetting(
            "Saturation", 0.75F, 0.0F, 1.0F, 0.01F, "Saturation modifier for shadow", this::lambda$new$1, this.field2346
    );
    public final OpacityColorSetting field2349 = new OpacityColorSetting("Color", Class3031.field131, "Shadow color", this::lambda$new$2, this.field2346);

    public Fonts() {
        super("Fonts", "Choose font for client", Category.Client);
        this.setNotificationLengthLimited();
    }

    @Override
    public boolean setEnabled(boolean newState) {
        this.enabled = false;
        if (mc.currentScreen == ClickGUI.field1335 && newState) {
            ClickGUI.field1335.method580(new FontSelectComponent(this.field2345));
        }

        return false;
    }

    private boolean lambda$new$2() {
        return this.field2346.getValue() == FontShadowMode.Color;
    }

    private boolean lambda$new$1() {
        return this.field2346.getValue() == FontShadowMode.Mod;
    }

    private boolean lambda$new$0() {
        return this.field2346.getValue() == FontShadowMode.Mod;
    }
}
