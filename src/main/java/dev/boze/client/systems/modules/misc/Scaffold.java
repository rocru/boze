package dev.boze.client.systems.modules.misc;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.*;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.render.Placement;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.PlaceRender;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.world.BlockInteraction;
import mapped.Class2784;
import mapped.Class3076;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    public static final Scaffold INSTANCE = new Scaffold();
    private final BooleanSetting field3088 = new BooleanSetting("Render", true, "Render placements");
    private final ColorSetting field3089 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill", this.field3088);
    private final ColorSetting field3090 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline", this.field3088);
    public final StringModeSetting field3091 = new StringModeSetting("Blocks", "Blocks to keep opaque");
    private final EnumSetting<ScaffoldFilter> field3092 = new EnumSetting<ScaffoldFilter>("Filter", ScaffoldFilter.Off, "Block filter");
    public final EnumSetting<AnticheatMode> field3093 = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Interaction mode");
    private final BooleanSetting field3094 = new BooleanSetting("Rotate", true, "Rotate");
    private final BooleanSetting field3095 = new BooleanSetting("RayCast", false, "Ray Cast");
    private final BooleanSetting field3096 = new BooleanSetting("Strict", false, "Strict");
    private final MinMaxSetting field3097 = new MinMaxSetting("Delay", 1.5, 0.0, 3.5, 0.5, "Block placement delay");
    private final MinMaxSetting field3098 = new MinMaxSetting("Expand", 1.0, 0.0, 6.0, 0.1, "Blocks to expand forward");
    private final EnumSetting<SwapMode> field3099 = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Auto Swap mode");
    private final EnumSetting<ScaffoldTower> field3100 = new EnumSetting<ScaffoldTower>("Tower", ScaffoldTower.Normal, "Tower mode");
    private final BooleanSetting field3101 = new BooleanSetting("Center", true, "Center when towering");
    private final BooleanSetting field3102 = new BooleanSetting("Safe", true, "Prevent walking off edges");
    private final BooleanSetting field3103 = new BooleanSetting("KeepY", false, "Maintain placement Y level");
    public final BindSetting field3104 = new BindSetting("Down", Bind.fromKey(342), "Keybind to go down");
    private final List<Block> field3105 = Arrays.asList(
            Blocks.ANVIL,
            Blocks.AIR,
            Blocks.COBWEB,
            Blocks.TRIPWIRE,
            Blocks.WATER,
            Blocks.FIRE,
            Blocks.LAVA,
            Blocks.CHEST,
            Blocks.ENCHANTING_TABLE,
            Blocks.TRAPPED_CHEST,
            Blocks.ENDER_CHEST,
            Blocks.GRAVEL,
            Blocks.LADDER,
            Blocks.VINE,
            Blocks.BEACON,
            Blocks.JUKEBOX,
            Blocks.ACACIA_DOOR,
            Blocks.BIRCH_DOOR,
            Blocks.DARK_OAK_DOOR,
            Blocks.IRON_DOOR,
            Blocks.JUNGLE_DOOR,
            Blocks.OAK_DOOR,
            Blocks.SPRUCE_DOOR,
            Blocks.IRON_TRAPDOOR,
            Blocks.ACACIA_TRAPDOOR,
            Blocks.BIRCH_TRAPDOOR,
            Blocks.CRIMSON_TRAPDOOR,
            Blocks.OAK_TRAPDOOR,
            Blocks.DARK_OAK_TRAPDOOR,
            Blocks.JUNGLE_TRAPDOOR,
            Blocks.SPRUCE_TRAPDOOR,
            Blocks.WARPED_TRAPDOOR,
            Blocks.BLACK_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.WHITE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.REDSTONE_WIRE
    );
    private int field3106;
    private final dev.boze.client.utils.Timer field3107 = new dev.boze.client.utils.Timer();
    private boolean field3108;
    private final dev.boze.client.utils.Timer field3109 = new dev.boze.client.utils.Timer();
    private BlockInteraction field3110;
    private final ArrayList<Placement> field3111 = new ArrayList();
    public boolean field3112 = false;

    public Scaffold() {
        super("Scaffold", "Places blocks under you", Category.Misc);
        this.addSettings(this.field3088);
    }

    @EventHandler
    public void method1763(Render3DEvent event) {
        if (this.field3088.getValue()) {
            for (Placement var6 : this.field3111) {
                PlaceRender.INSTANCE.method2015(event, var6, this.field3089.getValue(), this.field3090.getValue());
            }
        }
    }

    private boolean method1764(Block var1) {
        if (this.field3105.contains(var1)) {
            return false;
        } else {
            if (this.field3092.getValue() == ScaffoldFilter.Blacklist) {
                return !this.field3091.method2032().contains(var1);
            } else
                return this.field3092.getValue() != ScaffoldFilter.Whitelist || this.field3091.method2032().contains(var1);
        }
    }

    @EventHandler
    public void method1765(ACRotationEvent event) {
        if (!event.method1018(this.field3093.getValue(), this.field3094.getValue())) {
            this.field3111.removeIf(Scaffold::lambda$onRotate$0);
            if (this.field3107.hasElapsed(this.field3097.getValue() * 50.0)) {
                byte var5;
                if (this.field3104.getValue().isPressed()) {
                    var5 = 2;
                } else {
                    var5 = 1;
                }

                if (this.field3103.getValue()) {
                    if (!Class5924.method2116() && mc.options.jumpKey.isPressed() || mc.player.verticalCollision || mc.player.isOnGround()) {
                        this.field3106 = MathHelper.floor(mc.player.getY());
                    }
                } else {
                    this.field3106 = MathHelper.floor(mc.player.getY());
                }

                this.field3110 = null;
                double var6 = mc.player.getX();
                double var8 = mc.player.getZ();
                if (this.field3103.getValue()) {
                    double var10000 = this.field3106;
                } else {
                    mc.player.getY();
                }

                double var12 = mc.player.input.movementForward;
                double var14 = mc.player.input.movementSideways;
                float var16 = mc.player.getYaw();
                BlockInteraction var17 = null;
                if (Class2784.method2101(BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - (double) var5, mc.player.getZ()))) {
                    var17 = BlockInteraction.method2272(BlockPos.ofFloored(mc.player.getX(), mc.player.getY() - (double) var5, mc.player.getZ()))
                            .method2275(this.field3095.getValue());
                } else if (!mc.player.horizontalCollision && this.field3098.getValue() > 0.0) {
                    var17 = this.method1769(var6, mc.player.getY() - (double) var5, var8, var12, var14, var16);
                }

                if (var17 != null && mc.world.getBlockState(var17.method2278()).getBlock() == Blocks.AIR) {
                    if (this.field3096.getValue()
                            && (
                            Math.floor(mc.player.getEyePos().x) != (double) var17.method2278().getX()
                                    || Math.floor(mc.player.getEyePos().z) != (double) var17.method2278().getZ()
                    )) {
                        return;
                    }

                    int var18 = this.method1767();
                    if (var18 == -1) {
                        return;
                    }

                    this.field3110 = var17;
                    if (this.field3110 != null && this.field3110.method2279() != null && this.field3094.getValue()) {
                        float[] var19 = EntityUtil.method2146(this.field3110.method2279().getPos());
                        event.method1021(true);
                        event.yaw = var19[0];
                        event.pitch = var19[1];
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = 100
    )
    public void method1766(RotationEvent event) {
        if (!event.method554(RotationMode.Sequential)) {
            if (this.field3110 != null && this.field3110.method2279() != null) {
                int var5 = this.method1767();
                if (var5 == -1) {
                    return;
                }

                if (this.field3100.getValue() != ScaffoldTower.Off) {
                    if (mc.options.jumpKey.isPressed()
                            && mc.player.forwardSpeed == 0.0F
                            && mc.player.sidewaysSpeed == 0.0F
                            && !mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                        if (!this.field3108 && this.field3101.getValue()) {
                            this.field3108 = true;
                            BlockPos var6 = BlockPos.ofFloored(mc.player.getX(), mc.player.getY(), mc.player.getZ());
                            mc.player.setPosition((double) var6.getX() + 0.5, var6.getY(), (double) var6.getZ() + 0.5);
                        }

                        if (this.field3101.getValue() && !this.field3108) {
                            return;
                        }

                        if (this.field3100.getValue() == ScaffoldTower.Fast) {
                            Class3076.method6024(this, 25, mc.player.age % 10 == 0 ? 1.0F : 1.5782F);
                        }

                        mc.player.setVelocity(0.0, 0.42F, 0.0);
                        if (this.field3109.hasElapsed(1500.0)) {
                            Class3076.method6025(this);
                            this.field3109.reset();
                            mc.player.setVelocity(0.0, -0.28, 0.0);
                        }
                    } else {
                        Class3076.method6025(this);
                        this.field3109.reset();
                        if (this.field3108 && this.field3101.getValue()) {
                            this.field3108 = false;
                        }
                    }
                } else {
                    Class3076.method6025(this);
                }

                ((ClientPlayerInteractionManagerAccessor) mc.interactionManager).callSyncSelectedSlot();
                event.method556(
                        this, PlaceMode.Vanilla, this.field3099.getValue(), 20, var5, this.field3110.method2279(), var5 >= 0 ? Hand.MAIN_HAND : Hand.OFF_HAND
                );
                Class2784.method1801(this.field3110.method2278());
                if (this.field3088.getValue()) {
                    this.field3111.add(PlaceRender.INSTANCE.method2017(this.field3110.method2278()));
                }

                this.field3110 = null;
                this.field3107.reset();
            }
        }
    }

    private int method1767() {
        int var4 = -1;
        ItemStack var5 = mc.player.getMainHandStack();
        if (var5 != ItemStack.EMPTY && var5.getItem() instanceof BlockItem) {
            Block var6 = ((BlockItem) var5.getItem()).getBlock();
            if (this.method1764(var6)) {
                var4 = mc.player.getInventory().selectedSlot;
            }
        }

        ItemStack var10 = mc.player.getOffHandStack();
        if (var10 != ItemStack.EMPTY && var10.getItem() instanceof BlockItem) {
            Block var7 = ((BlockItem) var10.getItem()).getBlock();
            if (this.method1764(var7)) {
                var4 = -2;
            }
        }

        if (var4 == -1) {
            for (int var11 = 0; var11 < 9; var11++) {
                ItemStack var8 = mc.player.getInventory().getStack(var11);
                if (var8 != ItemStack.EMPTY && var8.getItem() instanceof BlockItem) {
                    Block var9 = ((BlockItem) var8.getItem()).getBlock();
                    if (this.method1764(var9)) {
                        var4 = var11;
                        break;
                    }
                }
            }
        }

        return var4;
    }

    @EventHandler
    public void method1768(PlayerMoveEvent event) {
        double var5 = event.vec3.x;
        double var7 = event.vec3.z;
        this.field3112 = false;
        if (mc.player.isOnGround() && !mc.player.noClip && this.field3102.getValue() && !this.field3104.getValue().isPressed()) {
            while (var5 != 0.0 && mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().expand(-0.15, 0.0, -0.15).offset(var5, -1.0, 0.0))) {
                if (var5 < 0.05 && var5 >= -0.05) {
                    var5 = 0.0;
                } else if (var5 > 0.0) {
                    var5 -= 0.05;
                } else {
                    var5 += 0.05;
                }
            }

            while (var7 != 0.0 && mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().expand(-0.15, 0.0, -0.15).offset(0.0, -1.0, var7))) {
                if (var7 < 0.05 && var7 >= -0.05) {
                    var7 = 0.0;
                } else if (var7 > 0.0) {
                    var7 -= 0.05;
                } else {
                    var7 += 0.05;
                }
            }

            while (var5 != 0.0 && var7 != 0.0 && mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().expand(-0.15, 0.0, -0.15).offset(var5, -1.0, var7))) {
                if (var5 < 0.05 && var5 >= -0.05) {
                    var5 = 0.0;
                } else if (var5 > 0.0) {
                    var5 -= 0.05;
                } else {
                    var5 += 0.05;
                }

                if (var7 < 0.05 && var7 >= -0.05) {
                    var7 = 0.0;
                } else if (var7 > 0.0) {
                    var7 -= 0.05;
                } else {
                    var7 += 0.05;
                }
            }
        }

        if (var5 != event.vec3.x || var7 != event.vec3.z) {
            this.field3112 = true;
        }

        if (this.field3093.getValue() == AnticheatMode.NCP) {
            event.vec3 = new Vec3d(var5, event.vec3.y, var7);
        }
    }

    @Override
    public void onEnable() {
        if (mc.world != null) {
            this.field3109.reset();
            this.field3106 = MathHelper.floor(mc.player.getY());
        }
    }

    @Override
    public void onDisable() {
        Class3076.method6025(this);
    }

    public BlockInteraction method1769(double x, double y, double z, double forward, double strafe, float YAW) {
        BlockInteraction var15 = null;
        double var16 = -999.0;
        double var18 = -999.0;
        double var20 = 0.0;
        double var22 = this.field3098.getValue() * 2.0;

        while (var15 == null) {
            if (++var20 > var22) {
                var20 = var22;
            }

            var16 = x
                    + (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0F)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0F))) * var20;
            var18 = z
                    + (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0F)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0F))) * var20;
            if (var20 == var22) {
                break;
            }

            if (Class2784.method2101(BlockPos.ofFloored(var16, y, var18))) {
                var15 = BlockInteraction.method2272(BlockPos.ofFloored(var16, y, var18)).method2275(this.field3095.getValue());
            }
        }

        return var15;
    }

    private static boolean lambda$onRotate$0(Placement var0) {
        return System.currentTimeMillis() - var0.method1159() > (long) PlaceRender.method2010();
    }
}
