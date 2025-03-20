package dev.boze.client.gui.components;

import dev.boze.client.Boze;
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
import mapped.Class2782;
import mapped.Class3084;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ModulesCategoryComponent extends BaseComponent implements IMinecraft {
    private final double field322;
    private Category field323 = Category.Combat;
    private final HashMap<Category, ArrayList<BaseComponent>> field324 = new HashMap();
    private final HashMap<Category, Class3084> field325 = new HashMap();
    private boolean field326 = false;
    private ModuleComponent field327 = null;
    private double field328;
    private double field329;
    private double field330;
    private final double field331;
    private final double field332;
    private final double field333;

    public ModulesCategoryComponent(BaseComponent parent, double x, double y, double width, double scrollOffset) {
        super("Modules", parent, x, y, width, width * 0.22);
        this.field322 = width * 0.22;
        double var13 = width * 0.22;
        IconManager.applyScale(0.8);
        this.field331 = IconManager.method1116();
        IconManager.method1115();
        this.field332 = (width - this.field331 * 6.0) / 7.0;
        this.field333 = (this.field322 - this.field331) * 0.5;

        for (Category var18 : Category.values()) {
            if (var18 != Category.Hud && var18 != Category.Graph) {
                this.field324.put(var18, new ArrayList());
            }
        }

        this.field325.put(Category.Client, new Class3084(Category.Client, this.field332, this.field333, Notifications.CATEGORY_CLIENT));
        this.field325.put(Category.Combat, new Class3084(Category.Combat, this.field332 * 2.0 + this.field331, this.field333, Notifications.CATEGORY_COMBAT));
        this.field325.put(Category.Legit, new Class3084(Category.Legit, this.field332 * 3.0 + this.field331 * 2.0, this.field333, Notifications.CATEGORY_LEGIT));
        this.field325.put(Category.Misc, new Class3084(Category.Misc, this.field332 * 4.0 + this.field331 * 3.0, this.field333, Notifications.CATEGORY_MISC));
        this.field325
                .put(Category.Movement, new Class3084(Category.Movement, this.field332 * 5.0 + this.field331 * 4.0, this.field333, Notifications.CATEGORY_MOVEMENT));
        this.field325
                .put(Category.Render, new Class3084(Category.Render, this.field332 * 6.0 + this.field331 * 5.0, this.field333, Notifications.CATEGORY_RENDER));

        for (Module var20 : Boze.getModules().modules) {
            if (var20.category != Category.Hud && var20.category != Category.Graph) {
                ModuleComponent var21 = new ModuleComponent(
                        var20, this, x + (double) Theme.method1365() * scaleFactor, var13, width - (double) Theme.method1365() * scaleFactor * 2.0
                );
                this.field324.get(var20.category).add(var21);
                var13 += var21.field321;
            }
        }
    }

    private List<BaseComponent> method1144() {
        return ClickGUI.field1335.method2114()
                ? this.field324.values().stream().flatMap(Collection::stream).collect(Collectors.toList())
                : this.field324.get(this.field323);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.field318 + this.field320 > (double) mc.getWindow().getScaledWidth()) {
            this.field318 = (double) mc.getWindow().getScaledWidth() - this.field320;
        } else if (this.field318 < 0.0) {
            this.field318 = 0.0;
        }

        if (this.field319 + this.field322 > (double) mc.getWindow().getScaledHeight()) {
            this.field319 = (double) mc.getWindow().getScaledHeight() - this.field322;
        } else if (this.field319 < 0.0) {
            this.field319 = 0.0;
        }

        Class2782.field91 = this.field318;
        Class2782.field92 = this.field319;
        Class2782.field93 = this.field329;
        this.field328 = 0.0;
        if (Gui.INSTANCE.field2366.getValue() == MaxHeight.Absolute) {
            this.field328 = Gui.INSTANCE.field2367.getValue() * scaleFactor;
        } else if (Gui.INSTANCE.field2366.getValue() == MaxHeight.Relative) {
            this.field328 = (double) mc.getWindow().getScaledHeight() * Gui.INSTANCE.field2368.getValue();
        }

        Scissor.enableScissor(this.field318, this.field319 + this.field322 - scaleFactor, this.field320, this.field328 + scaleFactor * 2.0);
        RenderUtil.field3965.method2233();
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(scaleFactor * 0.5);
        IconManager.setScale(scaleFactor * 0.4);
        this.field330 = 0.0;
        this.field327 = null;
        double var8 = this.field322;
        double var10 = this.field322 + this.field328;
        double var12 = this.field322 - this.field329;
        if (Gui.INSTANCE.field2364.getValue()) {
            for (BaseComponent var15 : this.method1144()) {
                if (!ClickGUI.field1335.method582(var15) && var15 instanceof ModuleComponent var16 && var16.field338) {
                    this.field327 = var16;
                    break;
                }
            }
        }

        if (Theme.method1366() > 0) {
            RenderUtil.field3963
                    .method2252(
                            this.field318 + (double) Theme.method1365() * scaleFactor,
                            this.field319 + var12,
                            this.field320 - (double) (Theme.method1365() * 2) * scaleFactor,
                            scaleFactor * (double) Theme.method1366(),
                            Theme.method1349()
                    );
            var12 += scaleFactor * (double) Theme.method1366();
            this.field330 = this.field330 + scaleFactor * (double) Theme.method1366();
        }

        for (int var17 = 0; var17 < this.method1144().size(); var17++) {
            BaseComponent var19 = this.method1144().get(var17);
            if ((this.field327 == null || var19 == this.field327) && !ClickGUI.field1335.method582(var19)) {
                var19.field318 = this.field318 + (double) Theme.method1365() * scaleFactor;
                var19.field319 = this.field319 + var12;
                if (var12 + var19.field321 >= var8 || var12 <= var10) {
                    var19.render(context, mouseX, mouseY, delta);
                }

                var12 += var19.field321;
                this.field330 = this.field330 + var19.field321;
                if (Theme.method1366() > 0) {
                    RenderUtil.field3963
                            .method2252(
                                    this.field318 + (double) Theme.method1365() * scaleFactor,
                                    this.field319 + var12,
                                    this.field320 - (double) (Theme.method1366() * 2) * scaleFactor,
                                    scaleFactor * (double) Theme.method1366(),
                                    Theme.method1349()
                            );
                    var12 += scaleFactor * (double) Theme.method1366();
                    this.field330 = this.field330 + scaleFactor * (double) Theme.method1366();
                }
            }
        }

        if (Theme.method1365() > 0) {
            RenderUtil.field3963
                    .method2252(this.field318, this.field319 + this.field322, (double) Theme.method1365() * scaleFactor, this.field330, Theme.method1349());
            RenderUtil.field3963
                    .method2252(
                            this.field318 + this.field320 - (double) Theme.method1365() * scaleFactor,
                            this.field319 + this.field322,
                            (double) Theme.method1365() * scaleFactor,
                            this.field330,
                            Theme.method1349()
                    );
        }

        this.field321 = var12;
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
        IconManager.setScale(0.8);
        if (Theme.method1382()) {
            ClickGUI.field1335
                    .field1333
                    .method2252(this.field318, this.field319 + this.field322, this.field320, Math.min(this.field330, this.field328), RGBAColor.field402);
        }

        if (Theme.method1387()) {
            if (Theme.method1361()) {
                RenderUtil.field3965.method2258(this.field318, this.field319, this.field320, this.field322, 3, 24, scaleFactor * 6.0, Theme.method1362());
            } else {
                RenderUtil.field3963.method2257(this.field318, this.field319, this.field320, this.field322, 3, 24, scaleFactor * 6.0, Theme.method1352());
            }

            if (Theme.method1382()) {
                ClickGUI.field1335.field1333.method2257(this.field318, this.field319, this.field320, this.field322, 3, 24, scaleFactor * 6.0, RGBAColor.field402);
            }
        } else {
            if (Theme.method1361()) {
                RenderUtil.field3965.method2253(this.field318, this.field319, this.field320, this.field322, Theme.method1362());
            } else {
                RenderUtil.field3963.method2252(this.field318, this.field319, this.field320, this.field322, Theme.method1352());
            }

            if (Theme.method1382()) {
                ClickGUI.field1335.field1333.method2252(this.field318, this.field319, this.field320, this.field322, RGBAColor.field402);
            }
        }

        for (Class3084 var20 : this.field325.values()) {
            var20.field205
                    .render(
                            this.field318 + var20.field203,
                            this.field319 + var20.field204,
                            var20.field202 == this.field323 ? Theme.method1350() : Theme.method1350().copy().method197(0.5F)
                    );
        }

        if ((double) Theme.method1379() > 0.0) {
            if (Theme.method1387()) {
                if (Theme.method1380()) {
                    RenderUtil.field3965
                            .method2260(
                                    this.field318,
                                    this.field319 + this.field322 + Math.min(this.field330, this.field328),
                                    this.field320,
                                    scaleFactor * (double) Theme.method1379(),
                                    24,
                                    Theme.method1381()
                            );
                } else {
                    RenderUtil.field3963
                            .method2259(
                                    this.field318,
                                    this.field319 + this.field322 + Math.min(this.field330, this.field328),
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
                                    this.field319 + this.field322 + Math.min(this.field330, this.field328),
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
                                    this.field319 + this.field322 + Math.min(this.field330, this.field328),
                                    this.field320,
                                    scaleFactor * (double) Theme.method1379(),
                                    Theme.method1381()
                            );
                } else {
                    RenderUtil.field3963
                            .method2252(
                                    this.field318,
                                    this.field319 + this.field322 + Math.min(this.field330, this.field328),
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
                                    this.field319 + this.field322 + Math.min(this.field330, this.field328),
                                    this.field320,
                                    scaleFactor * (double) Theme.method1379(),
                                    RGBAColor.field402
                            );
                }
            }
        }

        this.field321 = this.field321 + scaleFactor * (double) Theme.method1379();
        if (!Theme.method1361() || !Theme.method1380()) {
            RenderUtil.field3963.method2235(context);
        }

        if (Theme.method1361() || Theme.method1380()) {
            RenderUtil.field3965.method2235(context);
        }

        IFontRender.method499().endBuilding();
        IconManager.method1115();
        if (this.field330 <= this.field328 & this.field329 != 0.0) {
            this.field329 = 0.0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field322)) {
            for (Class3084 var10 : this.field325.values()) {
                if (isMouseWithinBounds(
                        mouseX,
                        mouseY,
                        this.field318 + var10.field203 - this.field332 * 0.25,
                        this.field319 + var10.field204 - this.field333 * 0.25,
                        this.field331 + this.field332 * 0.5,
                        this.field331 + this.field333 * 0.5
                )) {
                    this.field323 = var10.field202;
                    return true;
                }
            }

            if (button == 0) {
                this.field326 = true;
                return true;
            }
        } else if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319 + this.field322, this.field320, this.field328)) {
            for (BaseComponent var12 : this.method1144()) {
                if (!ClickGUI.field1335.method582(var12) && (this.field327 == null || var12 == this.field327) && var12.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int button) {
        this.field326 = false;

        for (BaseComponent var10 : this.method1144()) {
            if (!ClickGUI.field1335.method582(var10)) {
                var10.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.field326) {
            this.field318 += deltaX;
            this.field319 += deltaY;
            return true;
        } else {
            for (BaseComponent var14 : this.method1144()) {
                if (!ClickGUI.field1335.method582(var14)
                        && (this.field327 == null || var14 == this.field327)
                        && var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }

            return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319 + this.field322, this.field320, this.field328) && this.field330 > this.field328) {
            this.field329 = this.field329 + amount * (double) (-Gui.INSTANCE.field2369.getValue());
            this.field329 = MathHelper.clamp(this.field329, 0.0, this.field330 - this.field328);
            return true;
        } else {
            return super.onMouseScroll(mouseX, mouseY, amount);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Gui.INSTANCE.field2365.getValue() && keyCode == 256 && this.field327 != null) {
            this.field327.field338 = false;
            this.field327 = null;
            return true;
        } else {
            for (BaseComponent var8 : this.method1144()) {
                if (!ClickGUI.field1335.method582(var8) && (this.field327 == null || var8 == this.field327) && var8.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }

            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (BaseComponent var8 : this.method1144()) {
            if (!ClickGUI.field1335.method582(var8) && (this.field327 == null || var8 == this.field327) && var8.keyReleased(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}
