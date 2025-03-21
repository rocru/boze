package dev.boze.client.systems.modules.client;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.render.BaseFramebuffer;
import dev.boze.client.settings.*;
import dev.boze.client.shaders.HudShaderProcessor;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class1147;
import mapped.Class1174;

public class HUD extends Module {
    public static final HUD INSTANCE = new HUD();
    public final BooleanSetting field2373 = new BooleanSetting("Render", true, "Render the HUD");
    private final SettingCategory field2374 = new SettingCategory("Layout", "HUD layout settings");
    public final MinMaxSetting field2375 = new MinMaxSetting("Scale", 1.0, 0.25, 2.25, 0.05, "Global HUD Scale", this.field2374);
    public final MinMaxSetting field2376 = new MinMaxSetting(
            "Margin", 0.0, 0.0, 0.1, 0.001, "Margin between HUD and screen edge\nAs a percentage of screen width/height\n", this.field2374
    );
    public final IntSetting field2377 = new IntSetting("HPadding", 2, 0, 5, 1, "Horizontal Padding", this.field2374);
    public final IntSetting field2378 = new IntSetting("VPadding", 1, 0, 5, 1, "Vertical Padding", this.field2374);
    public final BooleanSetting field2379 = new BooleanSetting("ChatOffset", true, "Offset HUD modules when typing in chat", this.field2374);
    public final BooleanSetting field2380 = new BooleanSetting("ToastOffset", true, "Offset HUD modules when toasts pop up", this.field2374);
    public final BooleanSetting field2381 = new BooleanSetting("Overlap", false, "Allow HUD modules to overlap", this.field2374);
    private final SettingCategory field2382 = new SettingCategory("Theme", "Global HUD theme settings\nEach HUD element has an option to use a custom theme\n");
    public final ColorSetting field2383 = new ColorSetting(
            "Text", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field2382
    );
    public final BooleanSetting field2384 = new BooleanSetting("Shadow", false, "Text shadow", this.field2382);
    public final MinMaxSetting field2385 = new MinMaxSetting(
            "LineSpacing", 1.5, 0.0, 3.0, 0.05, "Spacing between lines\nFor multi-line HUD elements\n", this.field2382
    );
    private final SettingCategory field2386 = new SettingCategory(
            "Graph", "Global HUD graph theme settings\nEach HUD graph has an option to use a custom theme\n"
    );
    public final IntSetting field2387 = new IntSetting("Length", 100, 10, 1200, 1, "Length of the line", this.field2386);
    public final RGBASetting field2388 = new RGBASetting("LineTop", new RGBAColor(-1), "Line top color", this.field2386);
    public final RGBASetting field2389 = new RGBASetting("LineBottom", new RGBAColor(-1), "Line bottom color", this.field2386);
    public final BooleanSetting field2390 = new BooleanSetting("Minimalistic", false, "No text, just draw the line", this.field2386);
    public final ColorSetting field2391 = new ColorSetting(
            "Header",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Header color",
            this::lambda$new$0,
            this.field2386
    );
    public final ColorSetting field2392 = new ColorSetting(
            "Values",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Values color",
            this::lambda$new$1,
            this.field2386
    );
    public final BooleanSetting field2393 = new BooleanSetting("Shadow", false, "Text shadow", this::lambda$new$2, this.field2386);
    public final BooleanSetting field2394 = new BooleanSetting("Background", true, "Draw background for HUD");
    public final ShaderSetting shader = new ShaderSetting(
            new HudShaderProcessor(),
            "Options",
            new Class1147(new BozeDrawColor(10, 10, 10, 40), 0),
            new Class1174(new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), true, 2, 0.0F, 0.0F),
            "Background colors and options for HUD",
            this.field2394
    );
    public final RGBASetting field2395 = new RGBASetting("Shadow", new RGBAColor(60, 60, 60, 180), "Shadow color");
    public final ColorSetting field2396 = new ColorSetting("ShadowGradient", new BozeDrawColor(60, 60, 60, 180), "Gradient shadow color");
    public RenderUtil field2397;
    public BaseFramebuffer field2398;
    public BaseFramebuffer field2399;

    public HUD() {
        super("HUD", "Boze HUD", Category.Client, ConfigCategory.Visuals);
        this.bind.set(true, 259);
    }

    @Override
    public boolean setEnabled(boolean newState) {
        if (newState && mc.player != null) {
            if (mc.currentScreen == null) {
                Gui.INSTANCE.setEnabled(true);
            }

            ClickGUI.field1335.field1336 = true;
        }

        return false;
    }

    public void method1340(int width, int height) {
        if (this.field2398 != null) {
            this.field2398.resize(width, height, false);
        }

        if (this.field2399 != null) {
            this.field2399.resize(width, height, false);
        }
    }

    private boolean lambda$new$2() {
        return !this.field2390.getValue();
    }

    private boolean lambda$new$1() {
        return !this.field2390.getValue();
    }

    private boolean lambda$new$0() {
        return !this.field2390.getValue();
    }
}
