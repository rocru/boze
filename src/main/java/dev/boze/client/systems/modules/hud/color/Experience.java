package dev.boze.client.systems.modules.hud.color;

import dev.boze.client.systems.modules.hud.ColorHUDModule;
import net.minecraft.item.Items;

public class Experience extends ColorHUDModule {
    public static final Experience INSTANCE = new Experience();

    public Experience() {
        super("EXp", "Shows your experience bottle count");
    }

    @Override
    protected String method1562() {
        return "Xp:";
    }

    @Override
    protected String method1563() {
        return Integer.toString(this.method1546());
    }

    private int method1546() {
        if (mc.player == null) {
            return 0;
        } else {
            int var4 = 0;

            for (int var5 = 0; var5 < 45; var5++) {
                if (mc.player.getInventory().getStack(var5).getItem() == Items.EXPERIENCE_BOTTLE) {
                    var4 += mc.player.getInventory().getStack(var5).getCount();
                }
            }

            return var4;
        }
    }
}
