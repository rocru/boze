package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.systems.modules.hud.ColorHUDModule;
import net.minecraft.item.Items;

public class Crystals extends ColorHUDModule {
    public static final Crystals INSTANCE = new Crystals();

    public Crystals() {
        super("Crystals", "Shows your crystal count");
    }

    @Override
    protected String method1562() {
        return "Crystals:";
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
                if (mc.player.getInventory().getStack(var5).getItem() == Items.END_CRYSTAL) {
                    var4 += mc.player.getInventory().getStack(var5).getCount();
                }
            }

            return var4;
        }
    }
}
