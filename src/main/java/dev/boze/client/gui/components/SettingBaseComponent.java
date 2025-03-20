package dev.boze.client.gui.components;

import dev.boze.api.setting.SettingBase;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import net.minecraft.client.gui.DrawContext;

public class SettingBaseComponent extends BaseComponent {
    public final SettingBase field394;
    public RGBAColor field395;
    protected Timer field396 = new Timer();

    public SettingBaseComponent(SettingBase setting, BaseComponent parent, double x, double y, double width, double height) {
        super(setting.name, parent, x, y, width, height);
        this.field394 = setting;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field321)) {
            this.field396.reset();
        } else if (Gui.INSTANCE.field2355.getValue() == 0.0 || this.field396.hasElapsed(Gui.INSTANCE.field2355.getValue() * 1000.0)) {
            ClickGUI.field1335.method581(this.field394.description, this.field318, this.field318 + this.field320);
        }
    }
}
