package mapped;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.components.scaled.AddColorComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.gui.components.scaled.bottomrow.EditGradientColorComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Colors;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Class5916 extends BottomRowScaledComponent {
    private final HashMap<String, Class5903<?>> field8;

    public Class5916(final String title, final HashMap<String, Class5903<?>> colors) {
        super(title, BottomRow.TextAddClose, 0.125, 0.4);
        this.field8 = colors;
    }

    private List<String> method1144() {
        return this.field8.keySet().stream().sorted().collect(Collectors.toList());
    }

    @Override
    protected int method2010() {
        return this.field8.size();
    }

    @Override
    protected void method639(final DrawContext context, final int index, final double itemX, final double itemY, final double itemWidth, final double itemHeight) {
        final String key = this.method1144().get(index);
        final Class5903 color = this.field8.get(key);
        Class2776.method5432(itemX, itemY, itemWidth, itemHeight, false);
        Class2776.method5430(itemX, itemY, itemHeight, color);
        Class2776.method5431(key + ((color.method2010() > 0) ? (" [" + color.method2010() + "]") : ""), itemX, itemY, itemHeight);
        Class2777.method5434(itemX, itemWidth, Class2778.method5440(itemY, itemHeight), Notifications.DELETE, Notifications.DUPLICATE, Notifications.EDIT, Notifications.SHARE);
    }

    @Override
    protected boolean handleItemClick(final int index, final int button, final double itemX, final double itemY, final double itemWidth, final double itemHeight, final double mouseX, final double mouseY) {
        final String s = this.method1144().get(index);
        final double method5440 = Class2778.method5440(itemY, itemHeight);
        if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 0), method5440)) {
            this.field8.get(s).method2142();
            Colors.INSTANCE.field2343.remove(s);
            return true;
        }
        if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 1), method5440)) {
            String field1427 = this.field1427;
            if (this.field1427.isEmpty()) {
                for (field1427 = s; Colors.INSTANCE.field2343.containsKey(field1427); field1427 = field1427) {
                }
            }
            if (!Colors.INSTANCE.field2343.containsKey(field1427)) {
                final Class5903 class5903 = this.field8.get(s);
                Object value;
                if (class5903 instanceof final StaticColor staticColor) {
                    value = staticColor.method217();
                } else if (class5903 instanceof final ChangingColor changingColor) {
                    value = changingColor.method212();
                } else {
                    if (!(class5903 instanceof GradientColor)) {
                        this.field1427 = "";
                        return true;
                    }
                    value = ((GradientColor) class5903).method214(field1427);
                }
                Colors.INSTANCE.field2343.put(field1427, (Class5903<?>) value);
                Class2778.method5437();
            }
            this.field1427 = "";
            return true;
        }
        if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 2), method5440)) {
            final Class5903 class5904 = this.field8.get(s);
            if (class5904 instanceof final StaticColor staticColor2) {
                ClickGUI.field1335.method580(new NewColorComponent(s, staticColor2.method217(), this));
            } else if (class5904 instanceof final ChangingColor changingColor2) {
                ClickGUI.field1335.method580(new ColorPickerComponent(s, changingColor2.method212(), this));
            } else if (class5904 instanceof final GradientColor gradientColor) {
                ClickGUI.field1335.method580(new EditGradientColorComponent(s, gradientColor.method214(s), this));
            }
            return true;
        }
        return Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 3), method5440);
    }

    @Override
    protected void method1904() {
        if (!this.field1427.isEmpty() && !Colors.INSTANCE.field2343.containsKey(this.field1427)) {
            ClickGUI.field1335.method580(new AddColorComponent(this, this.field1427));
            this.field1427 = "";
        }
    }
}
