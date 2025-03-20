package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.systems.modules.hud.ColorHUDModule;
import net.minecraft.item.Items;

public class GoldenApples extends ColorHUDModule {
    public static final GoldenApples INSTANCE = new GoldenApples();

    public GoldenApples() {
        super("GApples", "Shows your golden apple count");
    }

    @Override
    protected String method1562() {
        return "GApples:";
    }

    @Override
    protected String method1563() {
        return Integer.toString(this.method1547());
    }

    private int method1547() {
        if (mc.player == null) {
            return 0;
        } else {
            int var4 = 0;

            for (int var5 = 0; var5 < 45; var5++) {
                if (mc.player.getInventory().getStack(var5).getItem() == Items.GOLDEN_APPLE
                        || mc.player.getInventory().getStack(var5).getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                    var4 += mc.player.getInventory().getStack(var5).getCount();
                }
            }

            return var4;
        }
    }
}
