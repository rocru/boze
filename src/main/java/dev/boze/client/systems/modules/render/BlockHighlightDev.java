package dev.boze.client.systems.modules.render;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.WeirdColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class3031;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;

public class BlockHighlightDev extends Module {
    public static final BlockHighlightDev INSTANCE = new BlockHighlightDev();
    private final WeirdColorSetting field470 = new WeirdColorSetting("Color", Class3031.field130, "Color for block highlight");

    private BlockHighlightDev() {
        super("BlockHighlightDev", "Development version of block highlight", Category.Render);
    }

    @EventHandler
    public void method2071(Render3DEvent event) {
        if (mc.crosshairTarget instanceof BlockHitResult var5 && var5.getBlockPos() != null) {
            Box var7 = new Box(var5.getBlockPos());
            event.field1950.method1220(var7, this.field470.getValue());
        }
    }
}
