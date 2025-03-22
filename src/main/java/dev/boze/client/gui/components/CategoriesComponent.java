package dev.boze.client.gui.components;

import dev.boze.api.BozeInstance;
import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class CategoriesComponent extends BaseComponent implements IMinecraft {
    private final ArrayList<BaseComponent> field349 = new ArrayList();
    Category field348 = Category.Combat;

    public CategoriesComponent(BaseComponent parent, double x, double y, double width, double height) {
        super("Categories", parent, x, y, width, height);
        boolean var13 = !BozeInstance.INSTANCE.getModules().isEmpty();
        double var14 = height / (double) (var13 ? 7 : 6);
        double var16 = y;

        for (Category var21 : Category.values()) {
            if (var21 != Category.Hud && var21 != Category.Graph) {
                this.field349.add(new CategoryComponent(this, x, var16, width, var14, var21));
                var16 += var14;
            }
        }

        if (var13) {
            this.field349.add(new CategoryComponent(this, x, var16, width, var14, null));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        RenderUtil.field3963.method2233();
        IFontRender.method499().startBuilding(scaleFactor);

        for (BaseComponent var9 : this.field349) {
            var9.render(context, mouseX, mouseY, delta);
        }

        RenderUtil.field3963.method2235(context);
        IFontRender.method499().endBuilding();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (BaseComponent var10 : this.field349) {
            if (var10.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
