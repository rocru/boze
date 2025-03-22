package dev.boze.client.gui.components.scaled;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.rotation.fF;
import dev.boze.client.gui.components.rotation.fG;
import dev.boze.client.gui.components.rotation.fH;
import dev.boze.client.gui.components.slider.doubles.fo;
import dev.boze.client.gui.components.slider.doubles.fp;
import dev.boze.client.gui.components.slider.floats.array.gd;
import dev.boze.client.gui.components.slider.ints.*;
import dev.boze.client.gui.components.text.e_;
import dev.boze.client.gui.components.text.fa;
import dev.boze.client.gui.components.text.fb;
import dev.boze.client.gui.components.text.fc;
import dev.boze.client.gui.components.toggle.ToggleColorSettingComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class5910;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class ColorSettingComponent extends ScaledBaseComponent {
    private static final double field1397 = 6.0;
    public static BozeDrawColor field1396 = null;
    private final ColorSetting field1398;
    private final ArrayList<InputBaseComponent> field1399 = new ArrayList();
    private final ArrayList<InputBaseComponent> field1400 = new ArrayList();
    private final ArrayList<InputBaseComponent> field1401 = new ArrayList();

    public ColorSettingComponent(ColorSetting setting) {
        super(setting.name, 324.0 * BaseComponent.scaleFactor, 154.0 * BaseComponent.scaleFactor, true);
        this.field1398 = setting;
        this.field1399
                .add(
                        new ToggleColorSettingComponent(
                                this,
                                "Gradient",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 6.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                14.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new fo(
                                this,
                                "Speed",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 26.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new fp(
                                this,
                                "Offset",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 50.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new Class5910(
                                this,
                                "Strength",
                                this.field1388 + 112.0 * BaseComponent.scaleFactor + BaseComponent.scaleFactor,
                                this.field1389 + 6.0 * BaseComponent.scaleFactor + BaseComponent.scaleFactor,
                                66.0 * BaseComponent.scaleFactor,
                                66.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new gd(
                                this,
                                "SB",
                                this.field1388 + 186.0 * BaseComponent.scaleFactor,
                                this.field1389 + 6.0 * BaseComponent.scaleFactor,
                                132.0 * BaseComponent.scaleFactor,
                                68.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1400
                .add(
                        new fF(
                                this,
                                "Hue",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                153.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1400
                .add(
                        new f6(
                                this,
                                "Opacity",
                                this.field1388 + 165.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                153.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1401
                .add(
                        new fG(
                                this,
                                "Min Hue",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1401
                .add(
                        new fH(
                                this,
                                "Max Hue",
                                this.field1388 + 112.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1401
                .add(
                        new f2(
                                this,
                                "Opacity",
                                this.field1388 + 218.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new f3(
                                this,
                                "Red",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 104.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new f4(
                                this,
                                "Green",
                                this.field1388 + 112.0 * BaseComponent.scaleFactor,
                                this.field1389 + 104.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new f5(
                                this,
                                "Blue",
                                this.field1388 + 218.0 * BaseComponent.scaleFactor,
                                this.field1389 + 104.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new e_(
                                this,
                                "Copy",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new fa(
                                this,
                                "Paste",
                                this.field1388 + 85.5 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new fb(
                                this,
                                "Reset",
                                this.field1388 + 165.0 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor,
                                setting
                        )
                );
        this.field1399
                .add(
                        new fc(
                                this,
                                "Apply",
                                this.field1388 + 244.5 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor
                        )
                );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963.method2233();
        RenderUtil.field3966.method2233();
        IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5);
        RenderUtil.field3963
                .method2257(
                        this.field1388,
                        this.field1389,
                        this.field1390,
                        this.field1391,
                        15,
                        24,
                        Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
                        Theme.method1349()
                );
        if (Theme.method1382()) {
            ClickGUI.field1335
                    .field1333
                    .method2257(
                            this.field1388,
                            this.field1389,
                            this.field1390,
                            this.field1391,
                            15,
                            24,
                            Theme.method1387() ? BaseComponent.scaleFactor * 6.0 : 0.0,
                            RGBAColor.field402
                    );
        }

        for (InputBaseComponent var9 : this.field1399) {
            var9.render(context, mouseX, mouseY, delta);
        }

        if (!this.field1398.method1374().field1842 && this.field1398.method1374().field1843 == 0.0) {
            for (InputBaseComponent var13 : this.field1400) {
                var13.render(context, mouseX, mouseY, delta);
            }
        } else {
            for (InputBaseComponent var12 : this.field1401) {
                var12.render(context, mouseX, mouseY, delta);
            }
        }

        RenderUtil.field3963.method2235(context);
        RenderUtil.field3966.method2235(context);
        IFontRender.method499().endBuilding();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY, int button) {
        if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
            return false;
        } else {
            for (InputBaseComponent var10 : this.field1399) {
                if (var10.mouseClicked(mouseX, mouseY, button)) {
                    break;
                }
            }

            if (!this.field1398.method1374().field1842 && this.field1398.method1374().field1843 == 0.0) {
                for (InputBaseComponent var14 : this.field1400) {
                    var14.mouseClicked(mouseX, mouseY, button);
                }
            } else {
                for (InputBaseComponent var13 : this.field1401) {
                    var13.mouseClicked(mouseX, mouseY, button);
                }
            }

            return true;
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!isMouseWithinBounds(mouseX, mouseY, this.field1388, this.field1389, this.field1390, this.field1391)) {
            return false;
        } else {
            for (InputBaseComponent var14 : this.field1399) {
                if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                    break;
                }
            }

            if (!this.field1398.method1374().field1842 && this.field1398.method1374().field1843 == 0.0) {
                for (InputBaseComponent var18 : this.field1400) {
                    var18.onDrag(mouseX, mouseY, button, deltaX, deltaY);
                }
            } else {
                for (InputBaseComponent var17 : this.field1401) {
                    var17.onDrag(mouseX, mouseY, button, deltaX, deltaY);
                }
            }

            return true;
        }
    }
}