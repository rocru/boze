package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.movement.elytraboost.nh;
import dev.boze.client.utils.DirectionUtil;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import mapped.Class3076;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraBoost extends Module {
    public static final ElytraBoost INSTANCE = new ElytraBoost();
    public final MinMaxSetting field1005 = new MinMaxSetting("Factor", 1.0, 0.1, 10.0, 0.1, "Boost factor");
    public final MinMaxSetting field1006 = new MinMaxSetting("MaxSpeed", 1.8, 0.1, 10.0, 0.1, "Max speed");
    public final nh field1011 = new nh(this);
    private final BooleanSetting field1000 = new BooleanSetting("ChestPlate", false, "Fly even with ChestPlate on\nRequires Elytra in Inventory\n");
    private final BooleanSetting field1001 = new BooleanSetting("AutoTakeoff", false, "Take off automatically");
    private final BooleanSetting field1002 = new BooleanSetting("UseTimer", true, "Use timer while taking off", this.field1001);
    private final IntSetting field1003 = new IntSetting("Delay", 5, 0, 20, 1, "Tick delay for taking off", this.field1001);
    private final BooleanSetting field1004 = new BooleanSetting("AutoBoost", false, "Automatically boost even when not holding W");
    private final BooleanSetting field1007 = new BooleanSetting(
            "YControl", false, "Control Y level with keys, not mouse\nUseful for AFK flight in Overworld/End\nAutoBoost is highly recommended for this\n"
    );
    private final FloatSetting field1008 = new FloatSetting("UpSpeed", 1.0F, 0.1F, 3.0F, 0.1F, "Upwards speed", this.field1007::getValue);
    private final FloatSetting field1009 = new FloatSetting("DownSpeed", 1.0F, 0.1F, 3.0F, 0.1F, "Downwards speed", this.field1007::getValue);
    private final FloatSetting field1010 = new FloatSetting(
            "Glide",
            0.5F,
            0.0F,
            5.0F,
            0.1F,
            "Glide speed\nAt higher values, makes you go down over time\nIn exchange for higher cruise speed\n",
            this.field1007::getValue
    );
    private final Timer field1012 = new Timer();
    private final Timer field1014 = new Timer();
    private int field1013 = 0;
    private boolean field1015 = false;

    public ElytraBoost() {
        super("ElytraBoost", "ElytraFly for stricter servers", Category.Movement);
    }

    @Override
    public void onEnable() {
        if (MinecraftUtils.isClientActive() && mc.player.isOnGround() && this.field1001.getValue()) {
            mc.player.jump();
        }

        this.field1011.field3209 = this.field1000.getValue();
        this.field1011.field3210 = false;
        this.field1011.method1819();
    }

    @Override
    public void onDisable() {
        Class3076.method6025(this);
        this.field1013 = 0;
        this.field1011.method1820();
    }

    @EventHandler
    public void method1942(PostPlayerTickEvent event) {
        if (MinecraftUtils.isClientActive()) {
            this.field1011.field3209 = this.field1000.getValue();
            this.field1011.field3210 = false;
            if (!this.field1011.field3212) {
                this.field1011.method1819();
            }

            boolean var5 = mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA;
            if (!var5 || this.field1011.field3209 && this.field1011.method1822() != -1) {
                if (mc.player.isFallFlying()) {
                    Class3076.method6025(this);
                    if (this.field1004.getValue() || mc.options.forwardKey.isPressed()) {
                        if (!mc.player.isUsingItem()) {
                            this.field1011.method1821();
                        }

                        if (mc.player.isOnGround()) {
                            this.field1011.field3208 = false;
                            mc.player.stopFallFlying();
                        } else if (var5) {
                            this.field1011.field3208 = true;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1944(KeyEvent event) {
        if (mc.options.jumpKey.matchesKey(event.key, 0)
                && event.action == KeyAction.Press
                && MinecraftUtils.isClientActive()
                && !mc.player.isOnGround()
                && mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA
                && this.field1011.method1822() != -1
                && this.field1000.getValue()) {
            this.field1011.field3208 = true;
        }
    }

    @EventHandler(
            priority = 35
    )
    public void method1695(MouseUpdateEvent event) {
        if (this.field1007.getValue() && MinecraftUtils.isClientActive() && mc.player.isFallFlying()) {
            float var5 = 0.0F;
            double var6 = Double.MAX_VALUE;

            for (float var8 = -20.0F; var8 <= 20.0F; var8 += 0.1F) {
                Vec3d var9 = mc.player.getVelocity();

                for (int var10 = 0; var10 < 20; var10++) {
                    var9 = this.method479(var9, mc.player.getYaw(), var8);
                }

                if (Math.abs(var9.y) < var6) {
                    var5 = var8;
                    var6 = Math.abs(var9.y);
                }
            }

            var5 += this.field1010.getValue();
            if (mc.options.sneakKey.isPressed()) {
                var5 = 25.0F * this.field1009.getValue();
            } else if (mc.options.forwardKey.isPressed() && this.field1004.getValue()) {
                var5 += 5.0F * this.field1009.getValue();
            } else if (mc.options.backKey.isPressed()) {
                var5 -= 5.0F * this.field1008.getValue();
            } else if (mc.options.jumpKey.isPressed()) {
                var5 = -25.0F * this.field1008.getValue();
            }

            RotationHelper var23 = new RotationHelper(mc.player);
            RotationHelper var24 = new RotationHelper(mc.player.getYaw(), var5);
            var24 = var23.method604(var24, 1.0).method1600();
            RotationHelper var26 = var24.method606(var23);
            Pair[] var11 = RotationHelper.method614(var26);
            Pair var12 = var11[0];
            double var13 = Float.MAX_VALUE;

            for (Pair var18 : var11) {
                RotationHelper var19 = RotationHelper.method613(var23, var18);
                double var20 = var19.method1954().distanceTo(var24.method1954());
                if (var20 < var13) {
                    var12 = var18;
                    var13 = var20;
                }
            }

            event.deltaY = (Double) var12.getRight();
        }
    }

    public void method1904() {
        if (this.field1015) {
            this.field1015 = false;
            mc.player.input.jumping = true;
        }
    }

    public void method1854() {
        if (this.isEnabled()
                && this.field1001.getValue()
                && !mc.player.isFallFlying()
                && !mc.player.isOnGround()
                && mc.world.isSpaceEmpty(mc.player.getBoundingBox().offset(0.0, -1.09, 0.0))) {
            if (mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
                if (!this.field1014.hasElapsed(2500.0)) {
                    return;
                }

                if (this.field1002.getValue()) {
                    Class3076.method6024(this, 12, 0.125F);
                }

                if (this.field1012.hasElapsed(this.field1003.getValue() * 50)) {
                    if (this.field1013 >= 5) {
                        ChatInstance.method740(this.getName(), "Failed to takeoff, waiting...");
                        this.field1013 = 0;
                        this.field1014.reset();
                        return;
                    }

                    mc.player.input.jumping = false;
                    this.field1015 = true;
                    this.field1013++;
                    this.field1012.reset();
                }
            } else if (this.field1000.getValue()) {
                this.field1011.field3208 = true;
            }
        }
    }

    private Vec3d method479(Vec3d var1, float var2, float var3) {
        Vec3d var9 = DirectionUtil.getLookVector(var3, var2);
        float var10 = var3 * (float) (Math.PI / 180.0);
        double var11 = Math.sqrt(var9.x * var9.x + var9.z * var9.z);
        double var13 = var1.horizontalLength();
        double var15 = var9.length();
        double var17 = Math.cos(var10);
        var17 = var17 * var17 * Math.min(1.0, var15 / 0.4);
        var1 = var1.add(0.0, 0.08 * (-1.0 + var17 * 0.75), 0.0);
        if (var1.y < 0.0 && var11 > 0.0) {
            double var7 = var1.y * -0.1 * var17;
            var1 = var1.add(var9.x * var7 / var11, var7, var9.z * var7 / var11);
        }

        if (var10 < 0.0F && var11 > 0.0) {
            double var20 = var13 * (double) (-MathHelper.sin(var10)) * 0.04;
            var1 = var1.add(-var9.x * var20 / var11, var20 * 3.2, -var9.z * var20 / var11);
        }

        if (var11 > 0.0) {
            var1 = var1.add((var9.x / var11 * var13 - var1.x) * 0.1, 0.0, (var9.z / var11 * var13 - var1.z) * 0.1);
        }

        return var1.multiply(0.99F, 0.98F, 0.99F);
    }
}
