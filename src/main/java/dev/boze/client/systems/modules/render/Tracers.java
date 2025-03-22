package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.PositionMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.player.RotationHelper;
import mapped.Class3071;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {
    public static final Tracers INSTANCE = new Tracers();
    private final EnumSetting<PositionMode> field662 = new EnumSetting<PositionMode>("Position", PositionMode.Feet, "Position to draw tracers to");
    private final FloatSetting field663 = new FloatSetting("Width", 1.0F, 1.0F, 5.0F, 0.1F, "Line width", Tracers::lambda$new$0);
    private final IntArraySetting field664 = new IntArraySetting("Range", new int[]{0, 250}, 0, 500, 1, "Range to entities within to draw tracers to");
    private final BooleanSetting field665 = new BooleanSetting("DistanceColor", false, "Color code tracers by their distance");
    private final RGBASetting field666 = new RGBASetting("Minimum", new RGBAColor(-65536), "Color for minimum distance", this.field665);
    private final RGBASetting field667 = new RGBASetting("Maximum", new RGBAColor(-16711936), "Color for maximum distance", this.field665);
    private final BooleanSetting field668 = new BooleanSetting("Fade", false, "Fade tracer opacity");
    private final FloatSetting field669 = new FloatSetting("MinDist", 8.0F, 0.0F, 50.0F, 0.5F, "Min distance for fading", this.field668);
    private final FloatSetting field670 = new FloatSetting("Factor", 0.005F, 0.002F, 0.1F, 0.002F, "Factor for fading", this.field668);
    private final BooleanSetting field671 = new BooleanSetting("Players", true, "Draw tracers to players");
    private final ColorSetting field672 = new ColorSetting("Color", new BozeDrawColor(-12858419), "Color for player tracers", this.field671);
    private final BooleanSetting field673 = new BooleanSetting("Friends", true, "Draw tracers to friends");
    private final ColorSetting field674 = new ColorSetting("Color", new BozeDrawColor(-15277290), "Color for friend tracers", this.field673);
    private final BooleanSetting field675 = new BooleanSetting("Animals", false, "Draw tracers to animals");
    private final ColorSetting field676 = new ColorSetting("Color", new BozeDrawColor(-15277196), "Color for animal tracers", this.field675);
    private final BooleanSetting field677 = new BooleanSetting("Monsters", false, "Draw tracers to monsters");
    private final ColorSetting field678 = new ColorSetting("Color", new BozeDrawColor(-1894890), "Color for monster tracers", this.field677);
    private final BooleanSetting field679 = new BooleanSetting("Crystals", false, "Draw tracers to crystals");
    private final ColorSetting field680 = new ColorSetting("Color", new BozeDrawColor(-56932), "Color for item tracers", this.field679);
    private int field681;
    private Renderer3D field682 = null;

    public Tracers() {
        super("Tracers", "Draws lines to entities", Category.Render);
    }

    private static boolean lambda$new$0() {
        return !MinecraftClient.IS_SYSTEM_MAC;
    }

    @Override
    public String method1322() {
        return Integer.toString(this.field681);
    }

    @EventHandler
    public void method2071(Render3DEvent event) {
        if (!mc.options.hudHidden) {
            this.field681 = 0;
            if (this.field682 == null) {
                this.field682 = new Renderer3D();
            }

            this.field682.field2166.field1594 = this.field663.getValue();
            this.field682.field2170.field1594 = this.field663.getValue();
            this.field682.method1217();

            for (Entity var6 : mc.world.getEntities()) {
                if (this.method2055(var6)) {
                    Vec3d var7 = Class3071.method6019(var6)
                            .add(
                                    0.0,
                                    this.field662.getValue() == PositionMode.Feet
                                            ? 0.0
                                            : (this.field662.getValue() == PositionMode.Body ? (double) var6.getHeight() * 0.5 : (double) var6.getEyeHeight(var6.getPose())),
                                    0.0
                            );
                    double var8 = var7.distanceTo(RotationHelper.field3956);
                    if (!(var8 > (double) this.field664.method1547()) && !(var8 < (double) this.field664.method2010())) {
                        this.field681++;
                        RGBAColor var10 = this.method342(var6, var8);
                        if (this.field668.getValue()) {
                            double var11 = this.method1389(var8);
                            var10 = var10.copy().method196((int) ((double) var10.field411 * var11));
                        }

                        if (var10 instanceof BozeDrawColor) {
                            this.field682
                                    .method1241(
                                            RotationHelper.field3956.x, RotationHelper.field3956.y, RotationHelper.field3956.z, var7.x, var7.y, var7.z, (BozeDrawColor) var10
                                    );
                        } else {
                            this.field682
                                    .method1236(RotationHelper.field3956.x, RotationHelper.field3956.y, RotationHelper.field3956.z, var7.x, var7.y, var7.z, var10);
                        }
                    }
                }
            }

            this.field682.method1219(event.matrix);
        }
    }

    private double method1389(double var1) {
        if (var1 <= (double) this.field669.getValue().floatValue()) {
            return 1.0;
        } else {
            var1 -= this.field669.getValue().floatValue();
            return 1.0 - MathHelper.clamp(var1 / (1.0 / (double) this.field670.getValue().floatValue()), 0.0, 1.0);
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private boolean method2055(Entity var1) {
        if (var1 instanceof PlayerEntity) {
            if (var1 == mc.player) {
                return false;
            } else {
                return Friends.method2055(var1) ? this.field673.getValue() : true;
            }
        } else if (var1 instanceof EndCrystalEntity) {
            return this.field679.getValue();
        } else {
            return switch (var1.getType().getSpawnGroup()) {
                case SpawnGroup.MONSTER -> this.field677.getValue();
                case SpawnGroup.MISC -> false;
                default -> this.field675.getValue();
            };
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private RGBAColor method342(Entity var1, double var2) {
        if (this.field665.getValue()) {
            return Class3071.method6016(
                    this.field666.getValue(), this.field667.getValue(), (var2 - (double) this.field664.method2010()) / (double) this.field664.method1547()
            );
        } else if (var1 instanceof PlayerEntity) {
            return Friends.method2055(var1) ? this.field674.getValue() : this.field672.getValue();
        } else if (var1 instanceof EndCrystalEntity) {
            return this.field680.getValue();
        } else {
            return switch (var1.getType().getSpawnGroup()) {
                case SpawnGroup.MONSTER -> this.field678.getValue();
                case SpawnGroup.MISC -> RGBAColor.field402;
                default -> this.field676.getValue();
            };
        }
    }
}
