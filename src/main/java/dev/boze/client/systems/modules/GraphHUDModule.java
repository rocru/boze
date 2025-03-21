package dev.boze.client.systems.modules;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3071;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GraphHUDModule extends HUDModule {
    public final BooleanSetting field2300 = new BooleanSetting("FromZero", false, "Start graph from zero");
    public final BooleanSetting field2301 = new BooleanSetting("Custom", false, "Use custom theme settings");
    public final IntSetting field2302 = new IntSetting("Length", 100, 10, 1200, 1, "Scale modifier for this HUD module", this.field2301);
    public final RGBASetting field2303 = new RGBASetting("LineTop", new RGBAColor(-1), "Line top color", this.field2301);
    public final RGBASetting field2304 = new RGBASetting("LineBottom", new RGBAColor(-1), "Line bottom color", this.field2301);
    public final BooleanSetting field2305 = new BooleanSetting("Minimalistic", false, "No text, just draw the line", this.field2301);
    public final ColorSetting field2306 = new ColorSetting("Header", new BozeDrawColor(-1), "Header color", this::lambda$new$0, this.field2301);
    public final ColorSetting field2307 = new ColorSetting("Values", new BozeDrawColor(-1), "Values color", this::lambda$new$1, this.field2301);
    public final BooleanSetting field2308 = new BooleanSetting("Shadow", true, "Text shadow", this::lambda$new$2, this.field2301);
    public static final int field2309 = 100;
    public static final int field2310 = 60;
    private final CopyOnWriteArrayList<Double> field2311 = new CopyOnWriteArrayList();

    public GraphHUDModule(String name, String description) {
        super(name, description, Category.Graph, 100 + HUD.INSTANCE.field2377.getValue() * 2, 60 + HUD.INSTANCE.field2378.getValue() * 3);
    }

    protected void method1324(double value) {
        this.field2311.add(value);

        while (this.field2311.size() > (this.field2301.getValue() ? this.field2302.getValue() : HUD.INSTANCE.field2387.getValue())) {
            this.field2311.remove(0);
        }
    }

    @Override
    public void method295(DrawContext context) {
        this.method314((double) (100 + HUD.INSTANCE.field2377.getValue() * 2) * this.method1327());
        this.method316((double) (60 + HUD.INSTANCE.field2378.getValue() * ((this.field2301.getValue() ? this.field2305.getValue() : HUD.INSTANCE.field2390.getValue()) ? 2 : 3)) * this.method1327());
        double var13 = this.method1325();
        double var15 = this.method1326();
        double var17 = var15 - var13;
        double var9;
        double var11;
        double var34;
        double var35;
        if (!this.field2301.getValue() ? this.field2305.getValue() : HUD.INSTANCE.field2390.getValue()) {
            double var19 = IFontRender.method499().getFontScale();
            IFontRender.method499().setFontScale(var19 * 0.5);
            IFontRender.method499()
                    .drawShadowedText(
                            this.getName(),
                            this.method1391() + (double) HUD.INSTANCE.field2377.getValue().intValue() * this.method1327(),
                            this.method305() + (double) HUD.INSTANCE.field2378.getValue().intValue() * this.method1327(),
                            this.field2301.getValue() ? this.field2306.getValue() : HUD.INSTANCE.field2391.getValue(),
                            this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()
                    );
            if (!this.field2311.isEmpty()) {
                String var21 = String.format("%.1f", this.field2311.get(this.field2311.size() - 1));
                IFontRender.method499()
                        .drawShadowedText(
                                var21,
                                this.method1391()
                                        + this.method313()
                                        - (double) HUD.INSTANCE.field2377.getValue().intValue() * this.method1327()
                                        - IFontRender.method499().method501(var21),
                                this.method305() + (double) HUD.INSTANCE.field2378.getValue().intValue() * this.method1327(),
                                this.field2301.getValue() ? this.field2306.getValue() : HUD.INSTANCE.field2391.getValue(),
                                this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()
                        );
            }

            var34 = this.method1391() + (double) HUD.INSTANCE.field2377.getValue().intValue() * this.method1327();
            var35 = (double) (HUD.INSTANCE.field2378.getValue() * 2) * this.method1327()
                    + IFontRender.method499().method502(this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue());
            var11 = this.method315() - var35 - (double) HUD.INSTANCE.field2378.getValue().intValue() * this.method1327();
            var35 += this.method305();
            IFontRender.method499().setFontScale(var19 * 0.3);
            if (!this.field2311.isEmpty()) {
                IFontRender.method499()
                        .drawShadowedText(
                                String.format("%.1f", var15),
                                var34 + this.method1327(),
                                var35 + this.method1327(),
                                this.field2301.getValue() ? this.field2307.getValue() : HUD.INSTANCE.field2392.getValue(),
                                this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()
                        );
                IFontRender.method499()
                        .drawShadowedText(
                                String.format("%.1f", var13),
                                var34 + this.method1327(),
                                var35
                                        + var11
                                        - this.method1327()
                                        - IFontRender.method499().method502(this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()),
                                this.field2301.getValue() ? this.field2307.getValue() : HUD.INSTANCE.field2392.getValue(),
                                this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()
                        );
            } else {
                IFontRender.method499()
                        .drawShadowedText(
                                "intermediary",
                                var34 + this.method1327(),
                                var35 + this.method1327(),
                                this.field2301.getValue() ? this.field2307.getValue() : HUD.INSTANCE.field2392.getValue(),
                                this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()
                        );
                IFontRender.method499()
                        .drawShadowedText(
                                "intermediary",
                                var34 + this.method1327(),
                                var35
                                        + var11
                                        - this.method1327()
                                        - IFontRender.method499().method502(this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()),
                                this.field2301.getValue() ? this.field2307.getValue() : HUD.INSTANCE.field2392.getValue(),
                                this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()
                        );
            }

            var34 += Math.max(
                    IFontRender.method499()
                            .measureTextHeight(String.format("%.1f", var15), this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue()),
                    IFontRender.method499()
                            .measureTextHeight(String.format("%.1f", var13), this.field2301.getValue() ? this.field2308.getValue() : HUD.INSTANCE.field2393.getValue())
            )
                    + this.method1327() * 2.0;
            IFontRender.method499().setFontScale(var19);
            var9 = this.method313() - (var34 - this.method1391()) - (double) HUD.INSTANCE.field2377.getValue().intValue() * this.method1327();
        } else {
            var34 = this.method1391() + (double) HUD.INSTANCE.field2377.getValue().intValue() * this.method1327();
            var35 = this.method305() + (double) HUD.INSTANCE.field2378.getValue().intValue() * this.method1327();
            var9 = this.method313() - (double) HUD.INSTANCE.field2377.getValue().intValue() * this.method1327() * 2.0;
            var11 = this.method315() - (double) HUD.INSTANCE.field2378.getValue().intValue() * this.method1327() * 2.0;
        }

        if (this.field2311.size() >= 2 && var17 > 0.0) {
            double var36 = -1.0;
            double var37 = -1.0;
            double var23 = -1.0;

            for (int var25 = 0; var25 < this.field2311.size(); var25++) {
                double var26 = this.field2311.get(var25);
                double var28 = (double) var25 / (double) (this.field2311.size() - 1) * var9 + var34;
                double var30 = var35 + var11 - (var26 - var13) / var17 * var11;
                var28 = MathHelper.clamp(var28, var34, var34 + var9);
                var30 = MathHelper.clamp(var30, var35, var35 + var11);
                if (var36 != -1.0 && var37 != -1.0) {
                    RGBAColor var32 = Class3071.method6016(
                            this.field2301.getValue() ? this.field2304.getValue() : HUD.INSTANCE.field2389.getValue(),
                            this.field2301.getValue() ? this.field2303.getValue() : HUD.INSTANCE.field2388.getValue(),
                            (var23 - var13) / var17
                    );
                    RGBAColor var33 = Class3071.method6016(
                            this.field2301.getValue() ? this.field2304.getValue() : HUD.INSTANCE.field2389.getValue(),
                            this.field2301.getValue() ? this.field2303.getValue() : HUD.INSTANCE.field2388.getValue(),
                            (var26 - var13) / var17
                    );
                    RenderUtil.field3963.method2243(var36, var37, var28, var30, var32, var33);
                }

                var36 = var28;
                var37 = var30;
                var23 = var26;
            }
        } else {
            RenderUtil.field3963
                    .method2242(
                            var34,
                            var35 + var11,
                            var34 + var9,
                            var35 + var11,
                            this.field2301.getValue() ? this.field2304.getValue() : HUD.INSTANCE.field2389.getValue()
                    );
        }

        if (HUD.INSTANCE.field2394.getValue()) {
            HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
        }
    }

    private double method1325() {
        if (this.field2300.getValue()) {
            return 0.0;
        } else {
            double var4 = Double.MAX_VALUE;

            for (double var7 : this.field2311) {
                if (var7 < var4) {
                    var4 = var7;
                }
            }

            return var4;
        }
    }

    private double method1326() {
        double var4 = 0.0;

        for (double var7 : this.field2311) {
            if (var7 > var4) {
                var4 = var7;
            }
        }

        return var4;
    }

    protected double method1327() {
        return HUD.INSTANCE.field2375.getValue() * this.field595.getValue();
    }

    private boolean lambda$new$2() {
        return !this.field2301.getValue() ? this.field2305.getValue() : HUD.INSTANCE.field2390.getValue();
    }

    private boolean lambda$new$1() {
        return !this.field2301.getValue() ? this.field2305.getValue() : HUD.INSTANCE.field2390.getValue();
    }

    private boolean lambda$new$0() {
        return !this.field2301.getValue() ? this.field2305.getValue() : HUD.INSTANCE.field2390.getValue();
    }
}
