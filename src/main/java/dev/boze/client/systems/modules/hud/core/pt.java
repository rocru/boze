package dev.boze.client.systems.modules.hud.core;

import net.minecraft.entity.player.PlayerEntity;

public class pt {
    private static final String[] field2655 = new String[]{"soon_1", "soon_2", "soon_3", "soon_4", "soon_5", "help_desk_npc"};

    public static boolean method1561(PlayerEntity entity) {
        for (String var7 : field2655) {
            if (entity.getName().getString().equalsIgnoreCase(var7)) {
                return true;
            }
        }

        return false;
    }
}
