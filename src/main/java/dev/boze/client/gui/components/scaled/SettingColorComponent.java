package dev.boze.client.gui.components.scaled;

import dev.boze.api.setting.SettingColor;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.rotation.fB;
import dev.boze.client.gui.components.rotation.fC;
import dev.boze.client.gui.components.rotation.fD;
import dev.boze.client.gui.components.slider.doubles.fk;
import dev.boze.client.gui.components.slider.doubles.fl;
import dev.boze.client.gui.components.slider.floats.array.gb;
import dev.boze.client.gui.components.slider.ints.*;
import dev.boze.client.gui.components.text.e0;
import dev.boze.client.gui.components.text.e1;
import dev.boze.client.gui.components.text.e2;
import dev.boze.client.gui.components.text.eZ;
import dev.boze.client.gui.components.toggle.ToggleSettingColorComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.RenderUtil;
import mapped.ColorComponentPosition;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class SettingColorComponent extends ScaledBaseComponent {
    private static final double field1404 = 6.0;
    private final SettingColor field1405;
    private final ArrayList<InputBaseComponent> field1406 = new ArrayList<>();
    private final ArrayList<InputBaseComponent> field1407 = new ArrayList<>();
    private final ArrayList<InputBaseComponent> field1408 = new ArrayList<>();

    public SettingColorComponent(SettingColor colorPicker) {
        super(colorPicker.name, 324.0 * BaseComponent.scaleFactor, 154.0 * BaseComponent.scaleFactor, true);
        this.field1405 = colorPicker;
        this.field1406
                .add(
                        new ToggleSettingColorComponent(
                                this,
                                "Gradient",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 6.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                14.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new fk(
                                this,
                                "Speed",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 26.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new fl(
                                this,
                                "Offset",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 50.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new ColorComponentPosition(
                                this,
                                "Strength",
                                this.field1388 + 112.0 * BaseComponent.scaleFactor,
                                this.field1389 + 6.0 * BaseComponent.scaleFactor,
                                68.0 * BaseComponent.scaleFactor,
                                68.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new gb(
                                this,
                                "SB",
                                this.field1388 + 186.0 * BaseComponent.scaleFactor,
                                this.field1389 + 6.0 * BaseComponent.scaleFactor,
                                132.0 * BaseComponent.scaleFactor,
                                68.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1407
                .add(
                        new fB(
                                this,
                                "Hue",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                150.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1407
                .add(
                        new fT(
                                this,
                                "Opacity",
                                this.field1388 + 162.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                150.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1408
                .add(
                        new fC(
                                this,
                                "Min Hue",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1408
                .add(
                        new fD(
                                this,
                                "Max Hue",
                                this.field1388 + 112.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1408
                .add(
                        new fP(
                                this,
                                "Opacity",
                                this.field1388 + 218.0 * BaseComponent.scaleFactor,
                                this.field1389 + 80.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new fQ(
                                this,
                                "Red",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 104.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new fR(
                                this,
                                "Green",
                                this.field1388 + 112.0 * BaseComponent.scaleFactor,
                                this.field1389 + 104.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new fS(
                                this,
                                "Blue",
                                this.field1388 + 218.0 * BaseComponent.scaleFactor,
                                this.field1389 + 104.0 * BaseComponent.scaleFactor,
                                100.0 * BaseComponent.scaleFactor,
                                18.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new eZ(
                                this,
                                "Copy",
                                this.field1388 + 6.0 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new e0(
                                this,
                                "Paste",
                                this.field1388 + 85.5 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new e1(
                                this,
                                "Reset",
                                this.field1388 + 165.0 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor
                        )
                );
        this.field1406
                .add(
                        new e2(
                                this,
                                "Apply",
                                this.field1388 + 244.5 * BaseComponent.scaleFactor,
                                this.field1389 + 128.0 * BaseComponent.scaleFactor,
                                73.5 * BaseComponent.scaleFactor,
                                20.0 * BaseComponent.scaleFactor
                        )
                );
    }

    public BozeDrawColor method1362() {
        return (BozeDrawColor) this.field1405.getValue();
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

        for (InputBaseComponent var9 : this.field1406) {
            var9.render(context, mouseX, mouseY, delta);
        }

        if (!this.method1362().field1842 && this.method1362().field1843 == 0.0) {
            for (InputBaseComponent var13 : this.field1407) {
                var13.render(context, mouseX, mouseY, delta);
            }
        } else {
            for (InputBaseComponent var12 : this.field1408) {
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
            for (InputBaseComponent var10 : this.field1406) {
                if (var10.mouseClicked(mouseX, mouseY, button)) {
                    break;
                }
            }

            if (!this.method1362().field1842 && this.method1362().field1843 == 0.0) {
                for (InputBaseComponent var14 : this.field1407) {
                    var14.mouseClicked(mouseX, mouseY, button);
                }
            } else {
                for (InputBaseComponent var13 : this.field1408) {
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
            for (InputBaseComponent var14 : this.field1406) {
                if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                    break;
                }
            }

            if (!this.method1362().field1842 && this.method1362().field1843 == 0.0) {
                for (InputBaseComponent var18 : this.field1407) {
                    var18.onDrag(mouseX, mouseY, button, deltaX, deltaY);
                }
            } else {
                for (InputBaseComponent var17 : this.field1408) {
                    var17.onDrag(mouseX, mouseY, button, deltaX, deltaY);
                }
            }

            return true;
        }
    }
}
