package mapped;

import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

public class Class5911 extends InputBaseComponent {
    public Class5911(String name, double x, double y, double width, double height) {
        super(name, x, y, width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963
                .method2257(this.field1133, this.field1134, this.field1135, this.field1136, 15, 24, Theme.method1387() ? field1131 * 6.0 : 0.0, Theme.method1348());
    }
}
