package dev.boze.client.systems.modules.client;

import dev.boze.client.enums.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;

public class Gui extends Module {
    public static final Gui INSTANCE = new Gui();
    public final EnumSetting<GUILayout> field2350 = new EnumSetting<GUILayout>("Layout", GUILayout.Classic, "Layout for the ClickGUI");
    public final BooleanSetting field2351 = new BooleanSetting("Blur", true, "Blur background when in ClickGUI");
    public final IntSetting field2352 = new IntSetting("Passes", 2, 1, 5, 1, "Blur passes", this.field2351);
    public final MinMaxSetting field2353 = new MinMaxSetting("Strength", 3.5, 1.5, 10.0, 0.25, "Blur strength", this.field2351);
    public final BooleanSetting field2354 = new BooleanSetting("Descriptions", true, "Draws descriptions for settings and modules");
    public final MinMaxSetting field2355 = new MinMaxSetting("Delay", 0.0, 0.0, 10.0, 1.0, "Delay for showing description upon hovering", this.field2354);
    public final RGBASetting field2356 = new RGBASetting("DescriptionColor", new RGBAColor(-13027015), "Color for descriptions", this.field2354);
    public final BooleanSetting field2357 = new BooleanSetting("SaveScroll", true, "Save scroll to config, so it doesn't reset when you re-open gui");
    public final BooleanSetting field2358 = new BooleanSetting("SearchBar", true, "Search bar for modules");
    public final BooleanSetting field2359 = new BooleanSetting("AnyKeySearch", false, "Start searching with any key, disables invmove in gui", this.field2358);
    public final BooleanSetting field2360 = new BooleanSetting(
            "DoubleEsc", true, "Press escape once to stop searching\nPress escape twice to close the gui\n", this.field2358
    );
    private final BooleanSetting field2361 = new BooleanSetting("Advanced", false, "Advanced settings for the ClickGUI");
    public final EnumSetting<AAMode> field2362 = new EnumSetting<AAMode>("AA", AAMode.MSAA4x, "Anti-Aliasing mode", this.field2361::getValue);
    public final EnumSetting<ToggleStyle> field2371 = new EnumSetting<ToggleStyle>(
            "Toggles", ToggleStyle.Switch, "Render mode for toggle elements", this.field2361::getValue
    );
    public final BooleanSetting field2372 = new BooleanSetting("ModeBox", false, "Draws a box around the mode selector", this.field2361::getValue);
    private final SettingCategory field2363 = new SettingCategory("Categories", "Options for category gui elements", this.field2361::getValue);
    public final BooleanSetting field2364 = new BooleanSetting(
            "SingleModule", false, "Hides all other modules when displaying settings for a module", this.field2363
    );
    public final BooleanSetting field2365 = new BooleanSetting(
            "DoubleEsc",
            false,
            "Press escape once to close the current module\nPress escape twice to close the entire gui\n",
            this.field2364::getValue,
            this.field2363
    );
    public final EnumSetting<MaxHeight> field2366 = new EnumSetting<MaxHeight>(
            "Height", MaxHeight.Relative, "Max height mode for category elements", this.field2363
    );
    public final MinMaxSetting field2367 = new MinMaxSetting(
            "HeightPx", 400.0, 100.0, 800.0, 5.0, "Max height for category elements", this::lambda$new$0, this.field2363
    );
    public final MinMaxSetting field2368 = new MinMaxSetting(
            "HeightPct", 0.9, 0.1, 1.0, 0.01, "Max height relative to screen height for category elements", this::lambda$new$1, this.field2363
    );
    public final IntSetting field2369 = new IntSetting("ScrollSpeed", 10, -20, 20, 1, "Scroll speed in category elements", this.field2363);
    public final IntSetting field2370 = new IntSetting("AnimTime", 150, 0, 1000, 10, "Opening/closing animation time (ms) for category elements", this.field2363);

    public Gui() {
        super("Gui", "Boze Gui", Category.Client, ConfigCategory.Visuals);
        this.bind.set(true, 344);
        this.field2350.method401(Gui::lambda$new$2);
    }

    private static void lambda$new$2(GUILayout var0) {
        ClickGUI.field1335.close();
    }

    @Override
    public boolean setEnabled(boolean newState) {
        if (newState) {
            ClickGUI.field1335.field1332 = GUIMenu.Normal;
            ClickGUI.field1335.field1336 = false;
            mc.setScreen(ClickGUI.field1335);
        }

        return false;
    }

    private boolean lambda$new$1() {
        return this.field2366.getValue() == MaxHeight.Relative;
    }

    private boolean lambda$new$0() {
        return this.field2366.getValue() == MaxHeight.Absolute;
    }
}
