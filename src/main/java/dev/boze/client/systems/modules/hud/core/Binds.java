package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.Boze;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Binds extends HUDModule {
    private final BooleanSetting field2596 = new BooleanSetting("Custom", false, "Use custom theme settings");
    private final ColorSetting field2597 = new ColorSetting(
            "OffText",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Text color for enabled modules",
            this.field2596
    );
    private final ColorSetting field2598 = new ColorSetting(
            "OffBind",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Bind color for enabled modules",
            this.field2596
    );
    private final ColorSetting field2599 = new ColorSetting(
            "OffBrackets",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Info color for enabled modules",
            this.field2596
    );
    private final ColorSetting field2600 = new ColorSetting(
            "OnText",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Text color for disabled modules",
            this.field2596
    );
    private final ColorSetting field2601 = new ColorSetting(
            "OnBind",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Bind color for disabled modules",
            this.field2596
    );
    private final ColorSetting field2602 = new ColorSetting(
            "OnBrackets",
            new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}),
            "Info color for disabled modules",
            this.field2596
    );
    private final BooleanSetting field2603 = new BooleanSetting("Shadow", false, "Text shadow", this.field2596);
    private final MinMaxSetting field2604 = new MinMaxSetting("Spacing", 1.5, 0.0, 3.0, 0.1, "Spacing between lines", this.field2596);
    public static final Binds INSTANCE = new Binds();
    float field2605 = 0.0F;

    public Binds() {
        super("Binds", "Shows a list of module binds", Category.Hud, 0.0, 0.0, 1, 40.0, 40.0);
        this.field595.setValue(0.75);
        this.setEnabled(true);
        this.method312(true);
    }

    @Override
    public void method295(DrawContext context) {
        int[] var5 = new int[]{0};
        int[] var6 = new int[]{1};
        boolean var7 = this.method2010() == 1 || this.method2010() == 2;
        boolean var8 = this.method2010() == 2 || this.method2010() == 4;
        List<Module> var9 = Boze.getModules().modules.stream().filter(Binds::lambda$onRender$0).collect(Collectors.toList());
        this.field2605 = (float) var9.stream().mapToDouble(this::lambda$onRender$1).max().orElse(0.0);
        this.method314(this.field2605);
        var9.stream().sorted(Comparator.comparingDouble(arg_0 -> this.lambda$onRender$2(var7, arg_0))).forEach(arg_0 -> this.lambda$onRender$3(var8, var5, var6, arg_0));
        this.method316(var5[0]);
    }

    private String method1550(Module var1) {
        return var1 == HUD.INSTANCE ? "Edit HUD" : var1.getName();
    }

    private String method1551(Module var1) {
        return this.method1550(var1) + " [" + var1.bind.toString() + "]";
    }

    private void lambda$onRender$3(boolean var1, int[] var2, int[] var3, Module var4) {
        boolean var8 = var4.isEnabled();
        String var9 = this.method1551(var4);
        double var10 = IFontRender.method499()
                .measureTextHeight(var9, this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue());
        if (HUD.INSTANCE.field2394.getValue()) {
            HUD.INSTANCE
                    .field2397
                    .method2252(
                            this.method1391() + (var1 ? this.method313() - var10 - 4.0 : 0.0),
                            this.method305() + (double) var2[0],
                            var10 + 4.0,
                            IFontRender.method499().method502(this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue())
                                    + (this.field2596.getValue() ? this.field2604.getValue() : HUD.INSTANCE.field2385.getValue()),
                            RGBAColor.field402
                    );
        }

        IFontRender.method499()
                .drawShadowedText(
                        this.method1550(var4),
                        this.method1391() + (var1 ? this.method313() - var10 - 2.0 : 2.0),
                        this.method305() + (double) var2[0] + 0.5,
                        this.field2596.getValue() ? (var8 ? this.field2600.getValue() : this.field2597.getValue()) : HUD.INSTANCE.field2383.getValue(),
                        this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                );
        if (!var4.bind.toString().isEmpty()) {
            IFontRender.method499()
                    .drawShadowedText(
                            "[",
                            this.method1391()
                                    + (var1 ? this.method313() - var10 - 2.0 : 2.0)
                                    + IFontRender.method499()
                                    .measureTextHeight(
                                            this.method1550(var4) + " ", this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                                    ),
                            this.method305() + (double) var2[0] + 0.5,
                            this.field2596.getValue() ? (var8 ? this.field2602.getValue() : this.field2599.getValue()) : HUD.INSTANCE.field2383.getValue(),
                            this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                    );
            IFontRender.method499()
                    .drawShadowedText(
                            var4.bind.toString(),
                            this.method1391()
                                    + (var1 ? this.method313() - var10 - 2.0 : 2.0)
                                    + IFontRender.method499()
                                    .measureTextHeight(
                                            this.method1550(var4) + " [", this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                                    ),
                            this.method305() + (double) var2[0] + 0.5,
                            this.field2596.getValue() ? (var8 ? this.field2601.getValue() : this.field2598.getValue()) : HUD.INSTANCE.field2383.getValue(),
                            this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                    );
            IFontRender.method499()
                    .drawShadowedText(
                            "]",
                            this.method1391()
                                    + (var1 ? this.method313() - var10 - 2.0 : 2.0)
                                    + IFontRender.method499()
                                    .measureTextHeight(
                                            this.method1550(var4) + " [" + var4.bind,
                                            this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                                    ),
                            this.method305() + (double) var2[0] + 0.5,
                            this.field2596.getValue() ? (var8 ? this.field2602.getValue() : this.field2599.getValue()) : HUD.INSTANCE.field2383.getValue(),
                            this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()
                    );
        }

        var2[0] = (int) (
                (double) var2[0]
                        + (IFontRender.method499().method502(this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue()))
                        + (this.field2596.getValue() ? this.field2604.getValue() : HUD.INSTANCE.field2385.getValue())
        );
        var3[0]++;
    }

    private double lambda$onRender$2(boolean var1, Module var2) {
        return var1
                ? -IFontRender.method499()
                .measureTextHeight(this.method1551(var2), this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue())
                : IFontRender.method499()
                .measureTextHeight(this.method1551(var2), this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue());
    }

    private double lambda$onRender$1(Module var1) {
        return IFontRender.method499()
                .measureTextHeight(this.method1551(var1), this.field2596.getValue() ? this.field2603.getValue() : HUD.INSTANCE.field2384.getValue());
    }

    private static boolean lambda$onRender$0(Module var0) {
        return var0.bind.isValid();
    }
}
