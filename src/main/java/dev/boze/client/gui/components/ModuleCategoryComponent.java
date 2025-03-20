package dev.boze.client.gui.components;

import dev.boze.client.Boze;
import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.LetterMode;
import dev.boze.client.enums.MaxHeight;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.render.Scissor;
import mapped.Class3071;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class ModuleCategoryComponent extends BaseComponent implements IMinecraft {
    public final Category field375;
    private final double field376;
    private final double field377;
    private final ArrayList<BaseComponent> field378 = new ArrayList();
    private boolean field379 = false;
    private ModuleComponent field380 = null;
    private double field381;
    private long field382;
    private double field383;
    private double field384;
    private final double field385;
    private final double field386;

    public ModuleCategoryComponent(Category category, BaseComponent parent, double x, double y, double width, double scrollOffset) {
        this(category, parent, x, y, x, y, width, scrollOffset);
    }

    public ModuleCategoryComponent(
            Category category, BaseComponent parent, double defaultX, double defaultY, double x, double y, double width, double scrollOffset
    ) {
        super(category.name(), parent, category.locked ? defaultX : x, category.locked ? defaultY : y, width, (double) Theme.method1357() * scaleFactor);
        this.field375 = category;
        this.field385 = defaultX;
        this.field386 = defaultY;
        this.field376 = (double) Theme.method1357() * scaleFactor;
        this.field377 = (double) (Theme.method1357() + 2) * scaleFactor;
        this.field383 = scrollOffset;
        double var18 = (double) Theme.method1357() * scaleFactor;
        var18 += (double) Theme.method1366() * scaleFactor;

        for (Module var21 : Boze.getModules().modules) {
            if (var21.category == category) {
                ModuleComponent var22 = new ModuleComponent(
                        var21, this, x + (double) Theme.method1365() * scaleFactor, var18, width - (double) Theme.method1365() * scaleFactor * 2.0
                );
                this.field378.add(var22);
                var18 += var22.field321 + (double) Theme.method1366() * scaleFactor;
            }
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        double var8 = !this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue()
                ? this.field376
                : this.field377;
        if (this.field375.locked) {
            this.field318 = this.field385;
            this.field319 = this.field386;
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

        this.field375.field42 = this.field318;
        this.field375.field43 = this.field319;
        this.field375.scrollOffset = this.field383;
        this.field381 = 0.0;
        if (Gui.INSTANCE.field2366.getValue() == MaxHeight.Absolute) {
            this.field381 = Gui.INSTANCE.field2367.getValue() * scaleFactor;
        } else if (Gui.INSTANCE.field2366.getValue() == MaxHeight.Relative) {
            this.field381 = (double) mc.getWindow().getScaledHeight() * Gui.INSTANCE.field2368.getValue();
        }

        if (System.currentTimeMillis() - this.field382 < (long) Gui.INSTANCE.field2370.getValue().intValue()) {
            this.field381 = this.field381
                    * Class3071.method6022(
                    this.field375.extended ? 0.0 : 1.0,
                    this.field375.extended ? 1.0 : 0.0,
                    (double) (System.currentTimeMillis() - this.field382) / (double) Gui.INSTANCE.field2370.getValue().intValue()
            );
        }

        Scissor.enableScissor(this.field318, this.field319 + var8, this.field320, this.field381);
        RenderUtil.field3965.method2233();
        RenderUtil.field3963.method2233();
        RenderUtil.field3966.method2233();
        IFontRender.method499().startBuilding(scaleFactor * 0.5);
        IconManager.setScale(scaleFactor * 0.4);
        this.field384 = 0.0;
        this.field380 = null;
        if (!this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue()) {
            this.field321 = var8;
        } else {
            double var10 = var8;
            double var12 = var8 + this.field381;
            double var14 = var8 - this.field383;
            if (Gui.INSTANCE.field2364.getValue()) {
                for (BaseComponent var17 : this.field378) {
                    if (!ClickGUI.field1335.method582(var17) && var17 instanceof ModuleComponent var18 && var18.field338) {
                        this.field380 = var18;
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
                                scaleFactor * (double) Theme.method1366() * 2.0,
                                Theme.method1349()
                        );
                var14 += scaleFactor * (double) Theme.method1366() * 2.0;
                this.field384 = this.field384 + scaleFactor * (double) Theme.method1366() * 2.0;
            }

            for (int var24 = 0; var24 < this.field378.size(); var24++) {
                BaseComponent var26 = this.field378.get(var24);
                if ((this.field380 == null || var26 == this.field380) && !ClickGUI.field1335.method582(var26)) {
                    var26.field318 = this.field318 + (double) Theme.method1365() * scaleFactor;
                    var26.field319 = this.field319 + var14;
                    if (var14 + var26.field321 >= var10 || var14 <= var12) {
                        var26.render(context, mouseX, mouseY, delta);
                    }

                    var14 += var26.field321;
                    this.field384 = this.field384 + var26.field321;
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
                        this.field384 = this.field384 + scaleFactor * (double) Theme.method1366();
                    }
                }
            }

            if (Theme.method1365() > 0) {
                RenderUtil.field3963.method2252(this.field318, this.field319 + var8, (double) Theme.method1365() * scaleFactor, this.field384, Theme.method1349());
                RenderUtil.field3963
                        .method2252(
                                this.field318 + this.field320 - (double) Theme.method1365() * scaleFactor,
                                this.field319 + var8,
                                (double) Theme.method1365() * scaleFactor,
                                this.field384,
                                Theme.method1349()
                        );
            }

            this.field321 = var14;
        }

        RenderUtil.field3963.method2235(context);
        RenderUtil.field3965.method2235(context);
        RenderUtil.field3966.method2235(context);
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
                    .method2252(this.field318, this.field319 + var8, this.field320, Math.min(this.field384, this.field381), RGBAColor.field402);
        }

        if (Theme.method1387()) {
            if (Theme.method1361()) {
                RenderUtil.field3965
                        .method2258(
                                this.field318,
                                this.field319,
                                this.field320,
                                var8,
                                !this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue() ? 15 : 3,
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
                                !this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue() ? 15 : 3,
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
                                !this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue() ? 15 : 3,
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

        boolean var20 = false;
        String var11 = Theme.method1392(Theme.method1359(), this.field316);
        double var21 = 0.0;
        if (Theme.method1360() != LetterMode.Text && this.field375.icon != null) {
            IconManager.setScale(scaleFactor * 0.4);
            var20 = true;
            var21 += scaleFactor * 6.0 + this.field375.icon.method2091();
        }

        if (!var20 || Theme.method1360() != LetterMode.Icon) {
            var21 += IFontRender.method499().method501(var11);
        }
        double var22 = switch (Theme.method1358()) {
            case AlignMode.Left -> this.field318 + scaleFactor * 6.0;
            case AlignMode.Center -> this.field318 + this.field320 / 2.0 - var21 / 2.0;
            case AlignMode.Right -> this.field318 + this.field320 - scaleFactor * 6.0 - var21;
            default -> throw new IncompatibleClassChangeError();
        };
        if ((Theme.method1358() != AlignMode.Right || Theme.method1360() == LetterMode.Icon) && var20) {
            this.field375.icon.render(var22, this.field319 + var8 / 2.0 - this.field375.icon.method1614() / 2.0, Theme.method1354());
            var22 += this.field375.icon.method2091() + scaleFactor * 6.0;
        }

        if (!var20 || Theme.method1360() != LetterMode.Icon) {
            IFontRender.method499().drawShadowedText(var11, var22, this.field319 + var8 / 2.0 - IFontRender.method499().method1390() / 2.0, Theme.method1354());
        }

        if (Theme.method1358() == AlignMode.Right && Theme.method1360() == LetterMode.Both && var20) {
            var22 += IFontRender.method499().method501(var11) + scaleFactor * 6.0;
            this.field375.icon.render(var22, this.field319 + var8 / 2.0 - this.field375.icon.method1614() / 2.0, Theme.method1354());
        }

        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, var8)) {
            if (!var20) {
                IconManager.setScale(scaleFactor * 0.4);
            }

            double var25 = Theme.method1358() == AlignMode.Right
                    ? this.field318 + scaleFactor * 6.0
                    : this.field318 + this.field320 - scaleFactor * 6.0 - Notifications.LOCK.method2091();
            double var27 = this.field319 + var8 / 2.0 - Notifications.LOCK.method1614() / 2.0;
            Notifications.LOCK.render(var25, var27, this.field375.locked ? Theme.method1354() : Theme.method1355());
            var20 = true;
        }

        if (this.field375.extended || System.currentTimeMillis() - this.field382 < (long) Gui.INSTANCE.field2370.getValue().intValue()) {
            if ((double) Theme.method1379() > 0.0) {
                if (Theme.method1387()) {
                    if (Theme.method1380()) {
                        RenderUtil.field3965
                                .method2260(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field384, this.field381),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        24,
                                        Theme.method1381()
                                );
                    } else {
                        RenderUtil.field3963
                                .method2259(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field384, this.field381),
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
                                        this.field319 + var8 + Math.min(this.field384, this.field381),
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
                                        this.field319 + var8 + Math.min(this.field384, this.field381),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        Theme.method1381()
                                );
                    } else {
                        RenderUtil.field3963
                                .method2252(
                                        this.field318,
                                        this.field319 + var8 + Math.min(this.field384, this.field381),
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
                                        this.field319 + var8 + Math.min(this.field384, this.field381),
                                        this.field320,
                                        scaleFactor * (double) Theme.method1379(),
                                        RGBAColor.field402
                                );
                    }
                }
            }

            this.field321 = this.field321 + scaleFactor * (double) Theme.method1379();
        }

        if (this.field383 > this.field384 - this.field381) {
            this.field383 = MathHelper.clamp(this.field383, 0.0, this.field384 - this.field381 > 0.0 ? this.field384 - this.field381 : 0.0);
        }

        if (!Theme.method1361() || !Theme.method1380()) {
            RenderUtil.field3963.method2235(context);
        }

        if (Theme.method1361() || Theme.method1380()) {
            RenderUtil.field3965.method2235(context);
        }

        if (var20) {
            IconManager.method1115();
        }

        IFontRender.method499().endBuilding();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        double var9 = !this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue()
                ? this.field376
                : this.field377;
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, var9)) {
            if (button == 0) {
                IconManager.applyScale(0.4);
                double var11 = Theme.method1358() == AlignMode.Right
                        ? this.field318 + scaleFactor * 6.0
                        : this.field318 + this.field320 - scaleFactor * 6.0 - Notifications.LOCK.method2091();
                if (isMouseWithinBounds(
                        mouseX, mouseY, var11, this.field319 + var9 / 2.0 - IconManager.method1116() / 2.0, Notifications.LOCK.method2091(), IconManager.method1116()
                )) {
                    this.field375.locked = !this.field375.locked;
                    IconManager.method1115();
                    return true;
                }

                IconManager.method1115();
                if (!this.field375.locked) {
                    this.field379 = true;
                }

                return true;
            }

            if (button == 1) {
                this.field375.extended = !this.field375.extended;
                this.field382 = System.currentTimeMillis();
                return true;
            }
        } else if (this.field375.extended && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319 + var9, this.field320, this.field381)) {
            for (BaseComponent var12 : this.field378) {
                if (!ClickGUI.field1335.method582(var12) && (this.field380 == null || var12 == this.field380) && var12.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int button) {
        this.field379 = false;
        if (this.field375.extended) {
            for (BaseComponent var10 : this.field378) {
                if (!ClickGUI.field1335.method582(var10)) {
                    var10.onMouseClicked(mouseX, mouseY, button);
                }
            }
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.field379 && !this.field375.locked) {
            this.field318 += deltaX;
            this.field319 += deltaY;
            return true;
        } else {
            if (this.field375.extended) {
                for (BaseComponent var14 : this.field378) {
                    if (!ClickGUI.field1335.method582(var14)
                            && (this.field380 == null || var14 == this.field380)
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
        double var10 = !this.field375.extended && System.currentTimeMillis() - this.field382 >= (long) Gui.INSTANCE.field2370.getValue().intValue()
                ? this.field376
                : this.field377;
        if (this.field375.extended
                && isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319 + var10, this.field320, this.field381)
                && this.field384 > this.field381) {
            this.field383 = this.field383 + amount * (double) (-Gui.INSTANCE.field2369.getValue());
            this.field383 = MathHelper.clamp(this.field383, 0.0, this.field384 - this.field381);
            return true;
        } else {
            return super.onMouseScroll(mouseX, mouseY, amount);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.field375.extended) {
            if (Gui.INSTANCE.field2365.getValue() && keyCode == 256 && this.field380 != null) {
                this.field380.field338 = false;
                this.field380 = null;
                return true;
            }

            for (BaseComponent var8 : this.field378) {
                if (!ClickGUI.field1335.method582(var8) && (this.field380 == null || var8 == this.field380) && var8.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.field375.extended) {
            for (BaseComponent var8 : this.field378) {
                if (!ClickGUI.field1335.method582(var8) && (this.field380 == null || var8 == this.field380) && var8.keyReleased(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
