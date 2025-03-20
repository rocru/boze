package dev.boze.client.gui.components;

import dev.boze.api.BozeInstance;
import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.MaxHeight;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.Scissor;
import mapped.Class2779;
import mapped.Class3071;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class AddonCategoryComponent extends BaseComponent implements IMinecraft {
    private final double field350;
    private final double field351;
    private final ArrayList<BaseComponent> field352 = new ArrayList();
    private boolean field353 = false;
    private AddonComponent field354 = null;
    private double field355;
    private long field356;
    private double field357;
    private double field358;
    private final double field359;
    private final double field360;

    public AddonCategoryComponent(BaseComponent parent, double defaultX, double defaultY, double x, double y, double width, double scrollOffset) {
        super("Addons", parent, Class2779.field88 ? defaultX : x, Class2779.field88 ? defaultY : y, width, (double) Theme.method1357() * scaleFactor);
        this.field359 = defaultX;
        this.field360 = defaultY;
        this.field350 = (double) Theme.method1357() * scaleFactor;
        this.field351 = (double) (Theme.method1357() + 2) * scaleFactor;
        this.field357 = scrollOffset;
        double var17 = (double) Theme.method1357() * scaleFactor;

        for (ToggleableModule var20 : BozeInstance.INSTANCE.getModules()) {
            AddonComponent var21 = new AddonComponent(
                    var20, this, x + (double) Theme.method1365() * scaleFactor, var17, width - (double) Theme.method1365() * scaleFactor * 2.0
            );
            this.field352.add(var21);
            var17 += var21.field321;
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        double var8 = !Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue()
                ? this.field350
                : this.field351;
        if (Class2779.field88) {
            this.field318 = this.field359;
            this.field319 = this.field360;
        }

        if (this.field318 + this.field320 > (double) mc.getWindow().getScaledWidth()) {
            this.field318 = (double) mc.getWindow().getScaledWidth() - this.field320;
        } else if (this.field318 < 0.0) {
            this.field318 = 0.0;
        }

        if (this.field319 + var8 > (double) mc.getWindow().getScaledHeight()) {
            this.field319 = (double) mc.getWindow().getScaledHeight() - var8;
        } else if (this.field319 < 0.0) {
            this.field319 = 0.0;
        }

        Class2779.field86 = this.field318;
        Class2779.field87 = this.field319;
        Class2779.field89 = this.field357;
        this.field355 = 0.0;
        if (Gui.INSTANCE.field2366.getValue() == MaxHeight.Absolute) {
            this.field355 = Gui.INSTANCE.field2367.getValue() * scaleFactor;
        } else if (Gui.INSTANCE.field2366.getValue() == MaxHeight.Relative) {
            this.field355 = (double) mc.getWindow().getScaledHeight() * Gui.INSTANCE.field2368.getValue();
        }

        if (System.currentTimeMillis() - this.field356 < (long) Gui.INSTANCE.field2370.getValue().intValue()) {
            this.field355 = this.field355
                    * Class3071.method6022(
                    Class2779.field90 ? 0.0 : 1.0,
                    Class2779.field90 ? 1.0 : 0.0,
                    (double) (System.currentTimeMillis() - this.field356) / (double) Gui.INSTANCE.field2370.getValue().intValue()
            );
        }

        Scissor.enableScissor(this.field318, this.field319 + var8 - scaleFactor, this.field320, this.field355 + scaleFactor * 2.0);
        RenderUtil.field3965.method2233();
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(scaleFactor * 0.5);
        IconManager.setScale(scaleFactor * 0.4);
        this.field358 = 0.0;
        this.field354 = null;
        if (!Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue()) {
            this.field321 = var8;
        } else {
            double var10 = var8;
            double var12 = var8 + this.field355;
            double var14 = var8 - this.field357;
            if (Gui.INSTANCE.field2364.getValue()) {
                for (BaseComponent var17 : this.field352) {
                    if (var17 instanceof AddonComponent var18 && var18.field366) {
                        this.field354 = var18;
                        break;
                    }
                }
            }

            if (Theme.method1366() > 0) {
                RenderUtil.field3963
                        .method2252(
                                this.field318 + (double) Theme.method1365() * scaleFactor,
                                this.field319 + var14,
                                this.field320 - (double) (Theme.method1365() * 2) * scaleFactor,
                                scaleFactor * (double) Theme.method1366(),
                                Theme.method1349()
                        );
                var14 += scaleFactor * (double) Theme.method1366();
                this.field358 = this.field358 + scaleFactor * (double) Theme.method1366();
            }

            for (int var21 = 0; var21 < this.field352.size(); var21++) {
                BaseComponent var23 = this.field352.get(var21);
                if ((this.field354 == null || var23 == this.field354) && !ClickGUI.field1335.method582(var23)) {
                    var23.field318 = this.field318 + (double) Theme.method1365() * scaleFactor;
                    var23.field319 = this.field319 + var14;
                    if (var14 + var23.field321 >= var10 || var14 <= var12) {
                        var23.render(context, mouseX, mouseY, delta);
                    }

                    var14 += var23.field321;
                    this.field358 = this.field358 + var23.field321;
                    if (Theme.method1366() > 0) {
                        RenderUtil.field3963
                                .method2252(
                                        this.field318 + (double) Theme.method1365() * scaleFactor,
                                        this.field319 + var14,
                                        this.field320 - (double) (Theme.method1366() * 2) * scaleFactor,
                                        scaleFactor * (double) Theme.method1366(),
                                        Theme.method1349()
                                );
                        var14 += scaleFactor * (double) Theme.method1366();
                        this.field358 = this.field358 + scaleFactor * (double) Theme.method1366();
                    }
                }
            }

            if (Theme.method1365() > 0) {
                RenderUtil.field3963.method2252(this.field318, this.field319 + var8, (double) Theme.method1365() * scaleFactor, this.field358, Theme.method1349());
                RenderUtil.field3963
                        .method2252(
                                this.field318 + this.field320 - (double) Theme.method1365() * scaleFactor,
                                this.field319 + var8,
                                (double) Theme.method1365() * scaleFactor,
                                this.field358,
                                Theme.method1349()
                        );
            }

            this.field321 = var14;
        }

        RenderUtil.field3963.method2235(context);
        RenderUtil.field3965.method2235(context);
        IFontRender.method499().endBuilding();
        IconManager.method1115();
        Scissor.disableScissor();
        if (!Theme.method1361() || !Theme.method1380()) {
            RenderUtil.field3963.method2233();
        }

        if (Theme.method1361() || Theme.method1380()) {
            RenderUtil.field3965.method2233();
        }

        IFontRender.method499().startBuilding(scaleFactor * 0.5);
        if (Theme.method1382()) {
            ClickGUI.field1335
                    .field1333
                    .method2252(this.field318, this.field319 + var8, this.field320, Math.min(this.field358, this.field355), RGBAColor.field402);
        }

        if (Theme.method1387()) {
            if (Theme.method1361()) {
                RenderUtil.field3965
                        .method2258(
                                this.field318,
                                this.field319,
                                this.field320,
                                var8,
                                !Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue() ? 15 : 3,
                                24,
                                scaleFactor * 6.0,
                                Theme.method1362()
                        );
            } else {
                RenderUtil.field3963
                        .method2257(
                                this.field318,
                                this.field319,
                                this.field320,
                                var8,
                                !Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue() ? 15 : 3,
                                24,
                                scaleFactor * 6.0,
                                Theme.method1352()
                        );
            }

            if (Theme.method1382()) {
                ClickGUI.field1335
                        .field1333
                        .method2257(
                                this.field318,
                                this.field319,
                                this.field320,
                                var8,
                                !Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue() ? 15 : 3,
                                24,
                                scaleFactor * 6.0,
                                RGBAColor.field402
                        );
            }
        } else {
            if (Theme.method1361()) {
                RenderUtil.field3965.method2253(this.field318, this.field319, this.field320, var8, Theme.method1362());
            } else {
                RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, var8, Theme.method1352());
            }

            if (Theme.method1382()) {
                ClickGUI.field1335.field1333.method2252(this.field318, this.field319, this.field320, var8, RGBAColor.field402);
            }
        }

        String var19 = Theme.method1392(Theme.method1359(), this.field316);

        double var11 = switch (Theme.method1358()) {
            case AlignMode.Left -> this.field318 + scaleFactor * 6.0;
            case AlignMode.Center ->
                    this.field318 + this.field320 / 2.0 - IFontRender.method499().method501(var19) / 2.0;
            case AlignMode.Right ->
                    this.field318 + this.field320 - scaleFactor * 6.0 - IFontRender.method499().method501(var19);
            default -> throw new IncompatibleClassChangeError();
        };
        IFontRender.method499().drawShadowedText(var19, var11, this.field319 + var8 / 2.0 - IFontRender.method499().method1390() / 2.0, Theme.method1354());
        boolean var13 = false;
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, var8)) {
            IconManager.setScale(scaleFactor * 0.4);
            double var20 = Theme.method1358() == AlignMode.Right
                    ? this.field318 + scaleFactor * 6.0
                    : this.field318 + this.field320 - scaleFactor * 6.0 - Notifications.LOCK.method2091();
            double var22 = this.field319 + var8 / 2.0 - Notifications.LOCK.method1614() / 2.0;
            Notifications.LOCK.render(var20, var22, Class2779.field88 ? Theme.method1354() : Theme.method1355());
            var13 = true;
        }

        if (Class2779.field90 || System.currentTimeMillis() - this.field356 < (long) Gui.INSTANCE.field2370.getValue().intValue()) {
            if ((double) Theme.method1379() > 0.0) {
                if (Theme.method1387()) {
                    if (Theme.method1380()) {
                        RenderUtil.field3965
                                .method2260(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field358, this.field355),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        24,
                                        Theme.method1381()
                                );
                    } else {
                        RenderUtil.field3963
                                .method2259(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field358, this.field355),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        24,
                                        Theme.method1352()
                                );
                    }

                    if (Theme.method1382()) {
                        ClickGUI.field1335
                                .field1333
                                .method2259(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field358, this.field355),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        24,
                                        RGBAColor.field402
                                );
                    }
                } else {
                    if (Theme.method1380()) {
                        RenderUtil.field3965
                                .method2253(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field358, this.field355),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        Theme.method1381()
                                );
                    } else {
                        RenderUtil.field3963
                                .method2252(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field358, this.field355),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        Theme.method1352()
                                );
                    }

                    if (Theme.method1382()) {
                        ClickGUI.field1335
                                .field1333
                                .method2252(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field358, this.field355),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        RGBAColor.field402
                                );
                    }
                }
            }

            this.field321 = this.field321 + scaleFactor * (double) Theme.method1379();
        }

        if (!Theme.method1361() || !Theme.method1380()) {
            RenderUtil.field3963.method2235(context);
        }

        if (Theme.method1361() || Theme.method1380()) {
            RenderUtil.field3965.method2235(context);
        }

        if (var13) {
            IconManager.method1115();
        }

        IFontRender.method499().endBuilding();
        if (this.field358 <= this.field355 & this.field357 != 0.0) {
            this.field357 = 0.0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double var9 = !Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue()
                ? this.field350
                : this.field351;
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, var9)) {
            if (button == 0) {
                IconManager.applyScale(0.4);
                double var11 = Theme.method1358() == AlignMode.Right
                        ? this.field318 + scaleFactor * 6.0
                        : this.field318 + this.field320 - scaleFactor * 6.0 - Notifications.LOCK.method2091();
                if (isMouseWithinBounds(
                        mouseX, mouseY, var11, this.field319 + var9 / 2.0 - IconManager.method1116() / 2.0, Notifications.LOCK.method2091(), IconManager.method1116()
                )) {
                    Class2779.field88 = !Class2779.field88;
                    IconManager.method1115();
                    return true;
                }

                IconManager.method1115();
                if (!Class2779.field88) {
                    this.field353 = true;
                }

                return true;
            }

            if (button == 1) {
                Class2779.field90 = !Class2779.field90;
                this.field356 = System.currentTimeMillis();
                return true;
            }
        } else if (Class2779.field90 && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319 + var9, this.field320, this.field355)) {
            for (BaseComponent var12 : this.field352) {
                if (!ClickGUI.field1335.method582(var12) && (this.field354 == null || var12 == this.field354) && var12.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int button) {
        this.field353 = false;
        if (Class2779.field90) {
            for (BaseComponent var10 : this.field352) {
                if (!ClickGUI.field1335.method582(var10)) {
                    var10.onMouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.field353 && !Class2779.field88) {
            this.field318 += deltaX;
            this.field319 += deltaY;
            return true;
        } else {
            if (Class2779.field90) {
                for (BaseComponent var14 : this.field352) {
                    if (!ClickGUI.field1335.method582(var14)
                            && (this.field354 == null || var14 == this.field354)
                            && var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                        return true;
                    }
                }
            }

            return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        double var10 = !Class2779.field90 && System.currentTimeMillis() - this.field356 >= (long) Gui.INSTANCE.field2370.getValue().intValue()
                ? this.field350
                : this.field351;
        if (Class2779.field90
                && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319 + var10, this.field320, this.field355)
                && this.field358 > this.field355) {
            this.field357 = this.field357 + amount * (double) (-Gui.INSTANCE.field2369.getValue());
            this.field357 = MathHelper.clamp(this.field357, 0.0, this.field358 - this.field355);
            return true;
        } else {
            return super.onMouseScroll(mouseX, mouseY, amount);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Class2779.field90) {
            if (Gui.INSTANCE.field2365.getValue() && keyCode == 256 && this.field354 != null) {
                this.field354.field366 = false;
                this.field354 = null;
                return true;
            }

            for (BaseComponent var8 : this.field352) {
                if (!ClickGUI.field1335.method582(var8) && (this.field354 == null || var8 == this.field354) && var8.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (Class2779.field90) {
            for (BaseComponent var8 : this.field352) {
                if (!ClickGUI.field1335.method582(var8) && (this.field354 == null || var8 == this.field354) && var8.keyReleased(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
