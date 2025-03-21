package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.BoxDrawMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.render.RenderUtil;
import dev.boze.client.utils.trackers.TargetTracker;
import mapped.Class5922;
import mapped.Class5923;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3d;

public class ESP extends Module {
    public static final ESP INSTANCE = new ESP();
    private final EnumSetting<BoxDrawMode> field3499 = new EnumSetting<BoxDrawMode>("Mode", BoxDrawMode.Complex, "Mode for drawing boxes");
    private final FloatSetting field3500 = new FloatSetting("Width", 1.0F, 1.0F, 5.0F, 0.1F, "Line width", ESP::lambda$new$0);
    private final BooleanSetting field3501 = new BooleanSetting("Fade", false, "Fade box opacity");
    private final FloatSetting field3502 = new FloatSetting("MinDist", 8.0F, 0.0F, 20.0F, 0.5F, "Min distance for fading", this.field3501);
    private final FloatSetting field3503 = new FloatSetting("Factor", 0.05F, 0.01F, 0.5F, 0.01F, "Factor for fading", this.field3501);
    private final BooleanSetting field3504 = new BooleanSetting("Players", true, "Apply ESP to players");
    private final BooleanSetting field3505 = new BooleanSetting("Self", false, "Apply ESP to yourself", this.field3504);
    private final ColorSetting field3506 = new ColorSetting("Color", new BozeDrawColor(1077660621), "Color for player boxes", this.field3504);
    private final ColorSetting field3507 = new ColorSetting("Outline", new BozeDrawColor(-12858419), "Color for player box outlines", this.field3504);
    private final BooleanSetting field3508 = new BooleanSetting("Friends", true, "Apply ESP to friends");
    private final ColorSetting field3509 = new ColorSetting("Color", new BozeDrawColor(1075241750), "Color for friend boxes", this.field3508);
    private final ColorSetting field3510 = new ColorSetting("Outline", new BozeDrawColor(-15277290), "Color for friend box outlines", this.field3508);
    private final BooleanSetting field3511 = new BooleanSetting("Targets", true, "Apply ESP to targets");
    private final ColorSetting field3512 = new ColorSetting("Color", new BozeDrawColor(1088624150), "Color for target boxes", this.field3511);
    private final ColorSetting field3513 = new ColorSetting("Outline", new BozeDrawColor(-1894890), "Color for target box outlines", this.field3511);
    private final BooleanSetting field3514 = new BooleanSetting("Animals", false, "Apply ESP to animals");
    private final ColorSetting field3515 = new ColorSetting("Color", new BozeDrawColor(1075241844), "Color for animal boxes", this.field3514);
    private final ColorSetting field3516 = new ColorSetting("Outline", new BozeDrawColor(-15277196), "Color for animal box outlines", this.field3514);
    private final BooleanSetting field3517 = new BooleanSetting("Monsters", false, "Apply ESP to monsters");
    private final ColorSetting field3518 = new ColorSetting("Color", new BozeDrawColor(1088624150), "Color for monster boxes", this.field3517);
    private final ColorSetting field3519 = new ColorSetting("Outline", new BozeDrawColor(-1894890), "Color for monster box outlines", this.field3517);
    private final BooleanSetting field3520 = new BooleanSetting("Crystals", false, "Apply ESP to crystals");
    private final ColorSetting field3521 = new ColorSetting("Color", new BozeDrawColor(1090462108), "Color for item boxes", this.field3520);
    private final ColorSetting field3522 = new ColorSetting("Outline", new BozeDrawColor(-56932), "Color for item box outlines", this.field3520);
    private final BooleanSetting field3523 = new BooleanSetting("Items", false, "Apply ESP to items");
    private final ColorSetting field3524 = new ColorSetting("Color", new BozeDrawColor(1087494682), "Color for item boxes", this.field3523);
    private final ColorSetting field3525 = new ColorSetting("Outline", new BozeDrawColor(-3024358), "Color for item box outlines", this.field3523);
    private final BooleanSetting field3526 = new BooleanSetting("Vehicles", false, "Apply ESP to vehicles");
    private final ColorSetting field3527 = new ColorSetting("Color", new BozeDrawColor(1083974052), "Color for vehicle boxes", this.field3526);
    private final ColorSetting aa = new ColorSetting("Outline", new BozeDrawColor(-6544988), "Color for vehicle box outlines", this.field3526);
    private final BooleanSetting ab = new BooleanSetting("Pearls", false, "Apply ESP to pearls");
    private final ColorSetting ac = new ColorSetting("Color", new BozeDrawColor(1075946605), "Color for pearl boxes", this.ab);
    private final ColorSetting ad = new ColorSetting("Outline", new BozeDrawColor(-14572435), "Color for pearl box outlines", this.ab);
    private final BooleanSetting ae = new BooleanSetting("EndEyes", false, "Apply ESP to EndEyes");
    private final ColorSetting af = new ColorSetting("Color", new BozeDrawColor(1073807210), "Color for EndEye boxes", this.ae);
    private final ColorSetting ag = new ColorSetting("Outline", new BozeDrawColor(-16711830), "Color for EndEye box outlines", this.ae);
    private final SettingCategory ah = new SettingCategory("NoTexture", "Don't render the textures for these entities - incompatible with chams");
    private final BooleanSetting ai = new BooleanSetting("Players", false, "Don't render the textures for players", this.ah);
    private final BooleanSetting aj = new BooleanSetting("Animals", false, "Don't render the textures for animals", this.ah);
    private final BooleanSetting ak = new BooleanSetting("Monsters", false, "Don't render the textures for monsters", this.ah);
    private final BooleanSetting al = new BooleanSetting("Crystals", false, "Don't render the textures for crystals", this.ah);
    private final Vector3d am = new Vector3d();
    private final Vector3d an = new Vector3d();
    private final Vector3d ao = new Vector3d();
    private int ap;
    private Renderer3D aq = null;

    public ESP() {
        super("ESP", "Draws boxes around entities", Category.Render);
    }

    @Override
    public String method1322() {
        return Integer.toString(this.ap);
    }

    @EventHandler
    private void method1928(Render3DEvent var1) {
        if (this.field3499.getValue() != BoxDrawMode.Flat) {
            this.ap = 0;
            Renderer3D var5 = var1.field1950;
            if (this.field3500.getValue() > 1.0F) {
                if (this.aq == null) {
                    this.aq = new Renderer3D(false, true);
                }

                var5 = this.aq;
                var5.field2166.field1594 = this.field3500.getValue();
                var5.field2168.field1594 = this.field3500.getValue();
                var5.field2170.field1594 = this.field3500.getValue();
                var5.method1217();
            }

            for (Entity var7 : mc.world.getEntities()) {
                if (this.method1934(var7)) {
                    this.method1929(var5, var1.field1951, var7);
                    this.ap++;
                }
            }

            if (this.field3500.getValue() > 1.0F) {
                this.aq.method1219(var1.matrix);
            }
        }
    }

    private void method1929(Renderer3D var1, float var2, Entity var3) {
        double var7 = this.method1932(var3);
        BozeDrawColor var9 = this.method1935(var3).copy();
        BozeDrawColor var10 = this.method1936(var3).copy();
        var9.field411 = (int) ((double) var9.field411 * var7);
        var10.field411 = (int) ((double) var10.field411 * var7);
        if (this.field3499.getValue() != BoxDrawMode.Simple && !(var3 instanceof FlyingItemEntity)) {
            Class5923.method68(var1, var2, var3, var9, var10, ShapeMode.Full);
        } else {
            double var11 = MathHelper.lerp(var2, var3.lastRenderX, var3.getX()) - var3.getX();
            double var13 = MathHelper.lerp(var2, var3.lastRenderY, var3.getY()) - var3.getY();
            double var15 = MathHelper.lerp(var2, var3.lastRenderZ, var3.getZ()) - var3.getZ();
            Box var17 = var3.getBoundingBox();
            var1.method1271(
                    var11 + var17.minX,
                    var13 + var17.minY,
                    var15 + var17.minZ,
                    var11 + var17.maxX,
                    var13 + var17.maxY,
                    var15 + var17.maxZ,
                    var9,
                    var10,
                    ShapeMode.Full,
                    0
            );
        }
    }

    @EventHandler
    public void method1930(Render2DEvent event) {
        if (this.field3499.getValue() == BoxDrawMode.Flat) {
            RenderUtil.field3965.method2233();
            if (this.field3500.getValue() > 1.0F) {
                RenderUtil.field3965.field3972.field1594 = this.field3500.getValue();
            }

            this.ap = 0;

            for (Entity var6 : mc.world.getEntities()) {
                if (this.method1934(var6)) {
                    Box var7 = var6.getBoundingBox();
                    double var8 = MathHelper.lerp(event.tickDelta, var6.lastRenderX, var6.getX()) - var6.getX();
                    double var10 = MathHelper.lerp(event.tickDelta, var6.lastRenderY, var6.getY()) - var6.getY();
                    double var12 = MathHelper.lerp(event.tickDelta, var6.lastRenderZ, var6.getZ()) - var6.getZ();
                    this.am.set(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
                    this.an.set(0.0, 0.0, 0.0);
                    if (!this.method1931(var7.minX + var8, var7.minY + var10, var7.minZ + var12, this.am, this.an)
                            && !this.method1931(var7.maxX + var8, var7.minY + var10, var7.minZ + var12, this.am, this.an)
                            && !this.method1931(var7.minX + var8, var7.minY + var10, var7.maxZ + var12, this.am, this.an)
                            && !this.method1931(var7.maxX + var8, var7.minY + var10, var7.maxZ + var12, this.am, this.an)
                            && !this.method1931(var7.minX + var8, var7.maxY + var10, var7.minZ + var12, this.am, this.an)
                            && !this.method1931(var7.maxX + var8, var7.maxY + var10, var7.minZ + var12, this.am, this.an)
                            && !this.method1931(var7.minX + var8, var7.maxY + var10, var7.maxZ + var12, this.am, this.an)
                            && !this.method1931(var7.maxX + var8, var7.maxY + var10, var7.maxZ + var12, this.am, this.an)) {
                        this.ap++;
                        double var14 = this.method1932(var6);
                        BozeDrawColor var16 = this.method1935(var6).copy();
                        BozeDrawColor var17 = this.method1936(var6).copy();
                        var16.field411 = (int) ((double) var16.field411 * var14);
                        var17.field411 = (int) ((double) var17.field411 * var14);
                        RenderUtil.field3965.method2253(this.am.x, this.am.y, this.an.x - this.am.x, this.an.y - this.am.y, var16);
                        RenderUtil.field3965.method2244(this.am.x, this.am.y, this.am.x, this.an.y, var17);
                        RenderUtil.field3965.method2244(this.an.x, this.am.y, this.an.x, this.an.y, var17);
                        RenderUtil.field3965.method2244(this.am.x, this.am.y, this.an.x, this.am.y, var17);
                        RenderUtil.field3965.method2244(this.am.x, this.an.y, this.an.x, this.an.y, var17);
                    }
                }
            }

            RenderUtil.field3965.method2235(null);
            if (this.field3500.getValue() > 1.0F) {
                RenderUtil.field3965.field3972.field1594 = 1.0F;
            }
        }
    }

    private boolean method1931(double var1, double var3, double var5, Vector3d var7, Vector3d var8) {
        this.ao.set(var1, var3, var5);
        if (!Class5922.method60(this.ao, 1.0)) {
            return true;
        } else {
            if (this.ao.x < var7.x) {
                var7.x = this.ao.x;
            }

            if (this.ao.y < var7.y) {
                var7.y = this.ao.y;
            }

            if (this.ao.z < var7.z) {
                var7.z = this.ao.z;
            }

            if (this.ao.x > var8.x) {
                var8.x = this.ao.x;
            }

            if (this.ao.y > var8.y) {
                var8.y = this.ao.y;
            }

            if (this.ao.z > var8.z) {
                var8.z = this.ao.z;
            }

            return false;
        }
    }

    private double method1932(Entity var1) {
        if (!this.field3501.getValue()) {
            return 1.0;
        } else {
            double var5 = var1.getEyePos().distanceTo(mc.player.getEyePos());
            if (var5 <= (double) this.field3502.getValue().floatValue()) {
                return 1.0;
            } else {
                var5 -= this.field3502.getValue().floatValue();
                return 1.0 - MathHelper.clamp(var5 / (1.0 / (double) this.field3503.getValue().floatValue()), 0.0, 1.0);
            }
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    public boolean method1933(Entity e) {
        if (e instanceof PlayerEntity) {
            return this.ai.getValue();
        } else if (e instanceof EndCrystalEntity) {
            return this.al.getValue();
        } else {
            return switch (e.getType().getSpawnGroup()) {
                default -> this.aj.getValue();
                case SpawnGroup.MONSTER -> this.ak.getValue();
                case SpawnGroup.MISC -> false;
            };
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    public boolean method1934(Entity e) {
        if (e instanceof PlayerEntity && this.field3504.getValue()) {
            if (e == mc.player) {
                return this.field3505.getValue() && !mc.options.getPerspective().isFirstPerson();
            } else if (Friends.method2055(e)) {
                return this.field3508.getValue();
            } else {
                return TargetTracker.method2055(e) ? this.field3511.getValue() : true;
            }
        } else if (e instanceof EndCrystalEntity) {
            return this.field3520.getValue();
        } else if (e instanceof ItemEntity) {
            return this.field3523.getValue();
        } else if (e instanceof MinecartEntity || e instanceof BoatEntity) {
            return this.field3526.getValue();
        } else if (e instanceof EnderPearlEntity) {
            return this.ab.getValue();
        } else if (e instanceof EyeOfEnderEntity) {
            return this.ae.getValue();
        } else {
            return switch (e.getType().getSpawnGroup()) {
                default -> this.field3514.getValue();
                case SpawnGroup.MONSTER -> this.field3517.getValue();
                case SpawnGroup.MISC -> false;
            };
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private BozeDrawColor method1935(Entity var1) {
        if (var1 instanceof PlayerEntity) {
            if (Friends.method2055(var1)) {
                return this.field3509.getValue();
            } else {
                return TargetTracker.method2055(var1) ? this.field3512.getValue() : this.field3506.getValue();
            }
        } else if (var1 instanceof EndCrystalEntity) {
            return this.field3521.getValue();
        } else if (var1 instanceof ItemEntity) {
            return this.field3524.getValue();
        } else if (var1 instanceof MinecartEntity || var1 instanceof BoatEntity) {
            return this.field3527.getValue();
        } else if (var1 instanceof EnderPearlEntity) {
            return this.ac.getValue();
        } else if (var1 instanceof EyeOfEnderEntity) {
            return this.af.getValue();
        } else {
            return switch (var1.getType().getSpawnGroup()) {
                default -> this.field3515.getValue();
                case SpawnGroup.MONSTER -> this.field3518.getValue();
                case SpawnGroup.MISC -> this.field3515.getValue();
            };
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    private BozeDrawColor method1936(Entity var1) {
        if (var1 instanceof PlayerEntity) {
            if (Friends.method2055(var1)) {
                return this.field3510.getValue();
            } else {
                return TargetTracker.method2055(var1) ? this.field3513.getValue() : this.field3507.getValue();
            }
        } else if (var1 instanceof EndCrystalEntity) {
            return this.field3522.getValue();
        } else if (var1 instanceof ItemEntity) {
            return this.field3525.getValue();
        } else if (var1 instanceof MinecartEntity || var1 instanceof BoatEntity) {
            return this.aa.getValue();
        } else if (var1 instanceof EnderPearlEntity) {
            return this.ad.getValue();
        } else if (var1 instanceof EyeOfEnderEntity) {
            return this.ag.getValue();
        } else {
            return switch (var1.getType().getSpawnGroup()) {
                default -> this.field3516.getValue();
                case SpawnGroup.MONSTER -> this.field3519.getValue();
                case SpawnGroup.MISC -> this.field3515.getValue();
            };
        }
    }

    private static boolean lambda$new$0() {
        return !MinecraftClient.IS_SYSTEM_MAC;
    }
}
