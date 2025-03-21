package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.StepMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerPositionEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.Surround;
import dev.boze.client.utils.Timer;
import mapped.Class3076;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Step extends Module {
    public static final Step INSTANCE = new Step();
    private final LinkedHashMap<Double, double[]> field3349 = new LinkedHashMap();
    private final LinkedHashMap<Double, double[]> field3350 = new LinkedHashMap();
    private final EnumSetting<StepMode> field3351 = new EnumSetting<StepMode>("Mode", StepMode.Vanilla, "Mode for step");
    private final BooleanSetting field3352 = new BooleanSetting("Upwards", true, "Step upwards");
    private final FloatSetting field3353 = new FloatSetting("Height", 1.0F, 1.0F, 3.0F, 0.5F, "Upwards step height", this.field3352);
    private final IntSetting field3354 = new IntSetting("Delay", 0, 0, 500, 50, "Delays each step", this.field3352);
    private final BooleanSetting field3355 = new BooleanSetting("UseTimer", false, "Use timer to minimize flagging");
    private final BooleanSetting field3356 = new BooleanSetting("SurroundDisable", false, "Disables step when surrounding");
    private boolean field3357 = false;
    private final Timer field3358 = new Timer();
    private final Timer field3359 = new Timer();

    public Step() {
        super("Step", "Automatically steps up blocks", Category.Movement);
        this.field3349.put(1.0, new double[]{0.42, 0.753});
        this.field3349.put(1.5, new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2});
        this.field3349.put(2.0, new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43});
        this.field3349.put(2.5, new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907});
        this.field3350.put(1.0, new double[]{0.42, 0.753});
    }

    @EventHandler
    public void method1876(MovementEvent event) {
        if (mc.player != null) {
            if (this.field3356.getValue() && Surround.INSTANCE.isEnabled()) {
                this.setEnabled(false);
            }
        }
    }

    @EventHandler
    public void method1877(PlayerPositionEvent event) {
        if (mc.player != null) {
            if (this.field3356.getValue() && Surround.INSTANCE.isEnabled()) {
                this.setEnabled(false);
            } else if (this.field3352.getValue()) {
                if (this.field3351.getValue() != StepMode.Vanilla) {
                    if (!mc.player.isInsideWaterOrBubbleColumn() && (double) mc.player.fallDistance < 0.1 && mc.player.getVelocity().y < 0.5) {
                        if (event.method1082() > (double) this.field3353.getValue().floatValue() || event.method1082() < 0.0 || event.method1082() < 0.6) {
                            return;
                        }

                        if (this.field3351.getValue() == StepMode.NCPStrict && event.method1082() > 1.0) {
                            return;
                        }

                        LinkedHashMap var5 = null;
                        if (this.field3351.getValue() == StepMode.NCP) {
                            var5 = this.field3349;
                        } else if (this.field3351.getValue() == StepMode.NCPStrict) {
                            var5 = this.field3350;
                        }

                        if (var5 == null) {
                            return;
                        }

                        double[] var6 = this.method1880(var5, this.method1882(var5, event.method1082()));
                        if (this.field3355.getValue()) {
                            Class3076.method6024(
                                    this, 15, 1.0F / ((float) var6.length + 1.0F + (float) (this.field3351.getValue() == StepMode.NCPStrict ? 1 : 0))
                            );
                        }

                        this.field3357 = true;

                        for (double var10 : var6) {
                            mc.player
                                    .networkHandler
                                    .sendPacket(
                                            new PositionAndOnGround(
                                                    mc.player.getX(), mc.player.getY() + (event.method1082() <= 1.0 ? var10 * event.method1082() : var10), mc.player.getZ(), true
                                            )
                                    );
                        }

                        if (this.field3351.getValue() == StepMode.NCPStrict) {
                            mc.player
                                    .networkHandler
                                    .sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY() + event.method1082(), mc.player.getZ(), true));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1878(PreTickEvent event) {
        if (mc.player != null) {
            if (this.field3356.getValue() && Surround.INSTANCE.isEnabled()) {
                this.setEnabled(false);
            } else {
                float var5 = this.field3353.getValue();
                if (this.field3351.getValue() == StepMode.NCPStrict) {
                    var5 = 1.0F;
                }

                boolean var6 = this.field3354.getValue() == 0 || this.field3359.hasElapsed(this.field3354.getValue().intValue());
                boolean var7 = !mc.player.isInsideWaterOrBubbleColumn() && (double) mc.player.fallDistance < 0.1 && mc.player.getVelocity().y < 0.5;
                if ((this.field3358.hasElapsed(80.0) || this.field3351.getValue() == StepMode.Vanilla) && var6 && this.field3352.getValue() && var7) {
                    mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT).setBaseValue(var5);
                } else {
                    mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT).setBaseValue(0.6F);
                }

                if (this.field3351.getValue() != StepMode.Vanilla) {
                    if (mc.player.isOnGround() && this.field3357) {
                        this.field3357 = false;
                        this.field3359.reset();
                        if (this.field3355.getValue()) {
                            Class3076.method6025(this);
                        }
                    }

                    if (!mc.player.isOnGround()) {
                        this.field3358.reset();
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1879(PacketBundleEvent event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket) {
            this.field3358.reset();
        }
    }

    @Override
    public void onDisable() {
        Class3076.method6025(this);
        this.field3358.reset();
        this.field3357 = false;
        if (mc.player != null) {
            mc.player.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT).setBaseValue(0.6F);
        }
    }

    private double[] method1880(LinkedHashMap<Double, double[]> var1, int var2) {
        Entry var3 = (Entry) var1.entrySet().toArray()[var2];
        return (double[]) var3.getValue();
    }

    private double method1881(LinkedHashMap<Double, double[]> var1, int var2) {
        Entry var3 = (Entry) var1.entrySet().toArray()[var2];
        return (Double) var3.getKey();
    }

    private int method1882(LinkedHashMap<Double, double[]> var1, double var2) {
        double var7 = Math.abs(this.method1881(var1, 0) - var2);
        int var9 = 0;

        for (int var10 = 1; var10 < var1.size(); var10++) {
            double var11 = Math.abs(this.method1881(var1, var10) - var2);
            if (var11 < var7) {
                var9 = var10;
                var7 = var11;
            }
        }

        return var9;
    }
}
