package mapped;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.settings.FriendsSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class Class5915 extends BottomRowScaledComponent {
    private final FriendsSetting field5;
    private String field6;
    private final ArrayList<Class3063> field7;

    public Class5915(final FriendsSetting setting) {
        super(setting.name, BottomRow.TextAddClose, 0.1, 0.4);
        this.field5 = setting;
        this.field6 = "";
        this.field7 = new ArrayList<Class3063>(setting.getValue());
    }

    @Override
    protected int method2010() {
        return this.field7.size();
    }

    @Override
    protected void method639(final DrawContext context, final int index, final double itemX, final double itemY, final double itemWidth, final double itemHeight) {
        final String method5992 = this.field7.get(index).method5992();
        RenderUtil.field3963.method2257(itemX, itemY, itemWidth, itemHeight, 15, 24, Theme.method1387() ? (BaseComponent.scaleFactor * 2.0) : 0.0, Theme.method1347());
        IFontRender.method499().drawShadowedText(method5992, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350());
        Notifications.PLAYERS_REMOVE.render(itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.PLAYERS_REMOVE.method2091(), itemY + itemHeight * 0.5 - Notifications.PLAYERS_REMOVE.method1614() * 0.5, Theme.method1350());
    }

    @Override
    protected boolean handleItemClick(final int index, final int button, final double itemX, final double itemY, final double itemWidth, final double itemHeight, final double mouseX, final double mouseY) {
        final Class3063 class3063 = this.field7.get(index);
        if (ScaledBaseComponent.isMouseWithinBounds(mouseX, mouseY, itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.PLAYERS_REMOVE.method2091(), itemY + itemHeight * 0.5 - Notifications.PLAYERS_REMOVE.method1614() * 0.5, Notifications.PLAYERS_REMOVE.method2091(), Notifications.PLAYERS_REMOVE.method1614())) {
            this.field7.remove(index);
            this.field5.getValue().remove(index);
            Class5915.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            return true;
        }
        return false;
    }

    @Override
    protected void method1904() {
        this.method1800(this.field6);
    }

    private void method1800(final String name) {
        if (name == null || name.isEmpty()) {
            return;
        }
        final Class3063 e = new Class3063(name);
        if (!this.field7.contains(e)) {
            this.field7.add(e);
            this.field5.getValue().add(e);
            this.field6 = "";
            Class5915.mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        try {
            if (keyCode == 259 && this.field6.length() > 0) {
                this.field6 = this.field6.substring(0, this.field6.length() - 1);
            } else if (keyCode == 257) {
                if (this.field6.length() > 0) {
                    this.method1800(this.field6);
                }
            } else if (keyCode == 86 && Class5928.method159(341)) {
                final String glfwGetClipboardString = GLFW.glfwGetClipboardString(Class5915.mc.getWindow().getHandle());
                if (glfwGetClipboardString != null && !glfwGetClipboardString.isEmpty()) {
                    glfwGetClipboardString.chars().forEach(this::lambda$keyPressed$0);
                }
            }
        } catch (final Exception ex) {
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void method583(final char c) {
        if ((this.field6.length() < 26 && c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_') {
            this.field6 += c;
        }
    }

    private void lambda$keyPressed$0(final int n) {
        final char c = (char) n;
        if (c == '\n') {
            if (this.field6.length() > 0) {
                this.method1800(this.field6);
            }
        } else {
            this.method583(c);
        }
    }
}
