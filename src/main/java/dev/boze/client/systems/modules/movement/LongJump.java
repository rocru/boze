package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LongJump extends Module {
    public static final LongJump INSTANCE = new LongJump();
    private final MinMaxSetting field3297 = new MinMaxSetting("Speed", 1.0, 1.0, 3.0, 0.1, "Speed");
    private final Timer field3298 = new Timer();
    private boolean field3299;
    private int field3300 = 0;
    private double field3301;
    private int field3302;
    private double field3303;

    public LongJump() {
        super("LongJump", "Jump further", Category.Movement);
    }

    @EventHandler
    private void method1846(MovementEvent var1) {
        double var2 = mc.player.getX() - mc.player.prevX;
        double var4 = mc.player.getZ() - mc.player.prevZ;
        this.field3301 = Math.sqrt(var2 * var2 + var4 * var4);
    }

    @EventHandler
    public void method1847(PlayerMoveEvent event) {
        if (this.field3299) {
            double var5 = event.vec3.x;
            double var7 = event.vec3.y;
            double var9 = event.vec3.z;
            if (mc.player.isOnGround()) {
                this.field3298.reset();
            }

            if (this.method1848(mc.player.getY() - (double) ((int) mc.player.getY()), 3) == 0.41) {
                mc.player.setVelocity(mc.player.getVelocity().getX(), 0.0, mc.player.getVelocity().getZ());
            }

            if (mc.player.sidewaysSpeed == 0.0F && mc.player.forwardSpeed == 0.0F) {
                this.field3302 = 1;
            }

            if (this.method1848(mc.player.getY() - (double) ((int) mc.player.getY()), 3) == 0.943) {
                mc.player.setVelocity(mc.player.getVelocity().getX(), 0.0, mc.player.getVelocity().getZ());
            }

            if (this.field3302 == 1) {
                if (mc.player.forwardSpeed != 0.0F || mc.player.sidewaysSpeed != 0.0F) {
                    this.field3302 = 2;
                    this.field3303 = Class5924.method2091() - 0.01;
                }
            } else if (this.field3302 == 2) {
                this.field3302 = 3;
                mc.player.setVelocity(mc.player.getVelocity().getX(), 0.424, mc.player.getVelocity().getZ());
                var7 = 0.424;
                this.field3303 = this.field3303 * 2.149802 * this.field3297.getValue();
            } else if (this.field3302 == 3) {
                this.field3302 = 4;
                double var11 = 0.66 * (this.field3301 - Class5924.method2091());
                this.field3303 = this.field3301 - var11;
            } else {
                if (!mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, mc.player.getVelocity().y, 0.0)) || mc.player.verticalCollision) {
                    this.field3302 = 1;
                }

                this.field3303 = this.field3301 - this.field3301 / 159.0;
            }

            this.field3303 = Math.max(this.field3303, Class5924.method2091());
            float var22 = mc.player.input.movementForward;
            float var12 = mc.player.input.movementSideways;
            float var13 = mc.player.getYaw();
            if (var22 != 0.0F && var12 != 0.0F) {
                if (var12 >= 1.0F) {
                    var13 += (float) (var22 > 0.0F ? -45 : 45);
                    var12 = 0.0F;
                } else if (var12 <= -1.0F) {
                    var13 += (float) (var22 > 0.0F ? 45 : -45);
                    var12 = 0.0F;
                }

                if (var22 > 0.0F) {
                    var22 = 1.0F;
                } else if (var22 < 0.0F) {
                    var22 = -1.0F;
                }
            } else {
                var5 = 0.0;
                var9 = 0.0;
            }

            double var14 = Math.cos(Math.toRadians(var13 + 90.0F));
            double var16 = Math.sin(Math.toRadians(var13 + 90.0F));
            if (var22 == 0.0F && var12 == 0.0F) {
                event.vec3 = new Vec3d(0.0, var7, 0.0);
            } else {
                var5 = (double) var22 * this.field3303 * var14 + (double) var12 * this.field3303 * var16;
                var9 = (double) var22 * this.field3303 * var16 - (double) var12 * this.field3303 * var14;
                event.vec3 = new Vec3d(var5, var7, var9);
            }
        }

        if (mc.player.isOnGround()) {
            this.field3300++;
        } else if (!mc.player.isOnGround() && this.field3300 != 0) {
            this.field3300--;
        }

        if (this.field3298.hasElapsed(480.0)) {
            mc.player.setVelocity(0.0, mc.player.getVelocity().getY(), 0.0);
            this.field3299 = false;
        }

        if (this.field3298.hasElapsed(780.0)) {
            mc.player.setVelocity(0.0, mc.player.getVelocity().getY(), 0.0);
            this.field3299 = true;
            this.field3298.reset();
        }
    }

    private double method1848(double var1, int var3) {
        if (var3 < 0) {
            throw new IllegalArgumentException();
        } else {
            BigDecimal var6 = new BigDecimal(var1);
            var6 = var6.setScale(var3, RoundingMode.HALF_UP);
            return var6.doubleValue();
        }
    }
}
