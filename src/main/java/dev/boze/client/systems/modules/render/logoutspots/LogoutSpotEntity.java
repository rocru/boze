package dev.boze.client.systems.modules.render.logoutspots;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.systems.modules.render.LogoutSpots;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class5923;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class LogoutSpotEntity extends FakePlayerEntity {
    private final UUID field1266;
    private final long field1267;
    private final double field1268;
    private final float field1269;
    private final float field1270;
    private final float field1271;
    final LogoutSpots field1272;

    public LogoutSpotEntity(final LogoutSpots arg, PlayerEntity player) {
        super(player, player.getName().toString() + "'s Logout Spot", 20.0F, false);
        this.field1272 = arg;
        this.field1266 = player.getUuid();
        this.field1267 = System.currentTimeMillis();
        this.field1268 = player.getY();
        this.method547(player);
        this.handSwinging = player.handSwinging;
        this.handSwingProgress = player.handSwingProgress;
        this.handSwingTicks = player.handSwingTicks;
        this.field1269 = player.getHandSwingProgress(1.0F);
        this.field1270 = player.limbAnimator.getSpeed(1.0F);
        this.field1271 = player.limbAnimator.getPos(1.0F);
        this.setSneaking(player.isSneaking());
        this.setSprinting(player.isSprinting());
    }

    public void method549(Renderer3D renderer, BozeDrawColor fillColor, BozeDrawColor outlineColor, float tickDelta) {
        this.lastRenderY = this.getY();
        Class5923.method67(renderer, tickDelta, this, fillColor, outlineColor, ShapeMode.Full, this.field1269, this.field1270, this.field1271);
    }
}
