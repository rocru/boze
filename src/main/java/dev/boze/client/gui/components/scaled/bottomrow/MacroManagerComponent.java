package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.Boze;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.components.scaled.BindComponent;
import dev.boze.client.gui.components.scaled.ChooseMacroComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.MacroManager;
import dev.boze.client.settings.MacroManagerSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.Macro;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class MacroManagerComponent extends BottomRowScaledComponent {
    private final MacroManagerSetting field1449;
    private final MacroManager field1450;
    private final ArrayList<Macro> field1451;

    public MacroManagerComponent(MacroManagerSetting setting) {
        super("Manage Macros", BottomRow.AddClose, 0.15, 0.4);
        this.field1449 = setting;
        this.field1450 = Boze.getMacros();
        this.field1451 = new ArrayList(this.field1450.field2140);
    }

    @Override
    protected int method2010() {
        return this.field1451.size();
    }

    @Override
    protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
        String var10000 = this.field1451.get(index).field1049.isKey()
                ? KeyboardUtil.getKeyName(this.field1451.get(index).field1049.getBind())
                : KeyboardUtil.getButtonName(this.field1451.get(index).field1049.getBind());
        String var10001 = this.field1451.get(index).field1048;
        String var15 = this.field1451.get(index).field1050 ? "Active" : "Inactive";
        String var16 = var10001;
        String var17 = var10000;
        String var14 = "[" + var17 + "] " + var16 + " - " + var15;
        RenderUtil.field3963
                .method2257(itemX, itemY, itemWidth, itemHeight, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0, Theme.method1347());
        IFontRender.method499()
                .drawShadowedText(
                        var14, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
                );
        Notifications.DELETE
                .render(
                        itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0,
                        itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5,
                        Theme.method1350()
                );
        Notifications.EDIT
                .render(
                        itemX + itemWidth - Notifications.DELETE.method2091() - Notifications.EDIT.method2091() - BaseComponent.scaleFactor * 12.0,
                        itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5,
                        Theme.method1350()
                );
    }

    @Override
    protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
        double var18 = itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0;
        double var20 = itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5;
        double var22 = itemX + itemWidth - Notifications.DELETE.method2091() - Notifications.EDIT.method2091() - BaseComponent.scaleFactor * 12.0;
        if (isMouseWithinBounds(mouseX, mouseY, var18, var20, Notifications.DELETE.method2091(), IconManager.method1116())) {
            this.field1450.field2140.remove(this.field1451.get(index));
            this.field1451.remove(index);
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        } else if (isMouseWithinBounds(mouseX, mouseY, var22, var20, Notifications.EDIT.method2091(), IconManager.method1116())) {
            ClickGUI.field1335.method580(new BindComponent(this.field1451.get(index), this.field1449));
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        } else {
            if (mc.player != null) {
                for (String var25 : this.field1451.get(index).field1051) {
                    ChatInstance.method1800(var25);
                }

                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }

            return true;
        }
    }

    @Override
    protected void method1904() {
        ClickGUI.field1335.method580(new ChooseMacroComponent(this.field1449));
    }
}
