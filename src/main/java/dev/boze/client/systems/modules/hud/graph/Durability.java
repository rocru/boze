package dev.boze.client.systems.modules.hud.graph;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.systems.modules.GraphHUDModule;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;

public class Durability extends GraphHUDModule {
    public static final Durability INSTANCE = new Durability();

    public Durability() {
        super("Durability", "Graphs your total armor durability");
        this.field2300.setValue(true);
    }

    @EventHandler
    public void method1567(MovementEvent event) {
        int var5 = 0;

        for (ItemStack var7 : mc.player.getInventory().armor) {
            var5 += var7.getMaxDamage() - var7.getDamage();
        }

        this.method1324(var5);
    }
}
