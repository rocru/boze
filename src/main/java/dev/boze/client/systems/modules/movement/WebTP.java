package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.WebTPMode;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class3091;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;

public class WebTP extends Module {
    public static final WebTP INSTANCE = new WebTP();
    private final EnumSetting<WebTPMode> field540 = new EnumSetting<WebTPMode>("Mode", WebTPMode.Strict, "Mode for WebTP");
    private final IntSetting field541 = new IntSetting("ShiftTicks", 3, 0, 10, 1, "Amount of ticks to shift per tick while moving through webs");

    public WebTP() {
        super("WebTP", "Makes you go through webs faster", Category.Movement);
    }

    @EventHandler(
            priority = 100
    )
    public void method1893(PlayerMoveEvent event) {
        if (Class5924.method87(Blocks.COBWEB) && Math.floor(mc.player.getPos().y) != mc.player.getPos().y) {
            if (this.field540.getValue() == WebTPMode.Strict) {
                event.vec3 = new Vec3d(event.vec3.x, Math.min(event.vec3.y, 0.0), event.vec3.z);
                event.field1892 = true;
            } else if (this.field540.getValue() == WebTPMode.Horizontal) {
                event.vec3 = event.vec3.multiply(4.0, 1.0, 4.0);
                event.field1892 = true;
            } else {
                event.vec3 = event.vec3.multiply(4.0, 20.0, 4.0);
                event.field1892 = true;
            }

            if (this.field541.getValue() > 0) {
                Class3091.field217 = true;

                for (int var5 = 1; var5 < this.field541.getValue(); var5++) {
                    mc.player.move(event.movementType, event.vec3);
                    ((IClientPlayerEntity) mc.player).boze$sendMovementPackets(mc.player.isOnGround());
                }

                Class3091.field217 = false;
            }
        }
    }
}
