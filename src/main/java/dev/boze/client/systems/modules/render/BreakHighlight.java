package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.BreakHighlightMode;
import dev.boze.client.enums.BreakHighlightShader;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.ByteTexture;
import dev.boze.client.utils.trackers.BlockBreakingTracker;
import mapped.Class5922;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3d;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BreakHighlight extends Module {
    public static final BreakHighlight INSTANCE = new BreakHighlight();
    private final BooleanSetting field3434 = new BooleanSetting("Predict", false, "Predicts block breaking");
    private final BooleanSetting field3435 = new BooleanSetting("BreakAlert", false, "Alerts you when a block besides is being broken");
    private final BooleanSetting field3436 = new BooleanSetting("Self", false, "Highlights blocks you're breaking");
    private final EnumSetting<BreakHighlightMode> field3437 = new EnumSetting<BreakHighlightMode>("Mode", BreakHighlightMode.Grow, "Render mode");
    public final ColorSetting field3438 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill");
    public final ColorSetting field3439 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline");
    private final BooleanSetting field3440 = new BooleanSetting("Shader", false, "Use a shader");
    public final EnumSetting<BreakHighlightShader> field3441 = new EnumSetting<BreakHighlightShader>(
            "Shader", BreakHighlightShader.Normal, "Shader to use", this.field3440
    );
    public final BooleanSetting field3442 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3440);
    public final IntSetting field3443 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3440);
    public final FloatSetting field3444 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3440);
    public final FloatSetting field3445 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3440);
    private final IntSetting field3446 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3440);
    private final FloatSetting field3447 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3440);
    public final StringSetting field3448 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3440);
    private final BooleanSetting field3449 = new BooleanSetting("Percentage", false, "Show mining percentage");
    private final BooleanSetting field3450 = new BooleanSetting("Player", false, "Show mining player's name");
    private final ScalingSetting field3451 = new ScalingSetting();
    private Renderer3D field3452;
    private ByteTexture field3453;
    private String field3454 = "";
    private final ConcurrentHashMap<BlockPos, Long> field3455 = new ConcurrentHashMap();
    private final ConcurrentHashMap<BlockPos, Long> field3456 = new ConcurrentHashMap();

    public BreakHighlight() {
        super("BreakHighlight", "Highlights blocks being broken", Category.Render);
        this.field3451.field2234.setVisibility(this::lambda$new$2);
    }

    @EventHandler
    public void method1914(MovementEvent event) {
        this.field3455.forEach(this::lambda$onSendMovementPackets$3);
    }

    @EventHandler
    public void method1915(Render2DEvent event) {
        if (this.field3449.getValue() || this.field3450.getValue()) {
            for (Entry var6 : BlockBreakingTracker.field1511.method666(this.field3434.getValue()).entrySet()) {
                if ((!this.field3436.getValue() || !this.field3449.getValue())
                        && var6.getKey().equals(((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getCurrentBreakingPos())) {
                    return;
                }

                BlockState var7 = mc.world.getBlockState((BlockPos) var6.getKey());
                VoxelShape var8 = var7.getOutlineShape(mc.world, (BlockPos) var6.getKey());
                if (var8.isEmpty()) {
                    return;
                }

                Box var9 = var8.getBoundingBox().offset((BlockPos) var6.getKey());
                Frustum var10 = ((WorldRendererAccessor) mc.worldRenderer).getFrustum();
                if (var10 == null || var10.isVisible(var9)) {
                    Vec3d var11 = var9.getCenter();
                    Vector3d var12 = new Vector3d(var11.x, var11.y, var11.z);
                    boolean var13 = Class5922.method59(var12, this.field3451);
                    if (var13) {
                        double var14 = -1.0;
                        int var16 = -1;
                        if (this.field3449.getValue()) {
                            var14 = ((Float) var6.getValue()).floatValue();
                        }

                        if (this.field3450.getValue()) {
                            var16 = BlockBreakingTracker.field1511.method667((BlockPos) var6.getKey());
                        }

                        this.method1917(var12, var14, var16);
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1916(final Render3DEvent event) {
        if (this.field3440.getValue()) {
            if (this.field3452 == null) {
                this.field3452 = new Renderer3D(false, true);
            }
            this.field3452.method1217();
        }
        for (Entry<BlockPos, Float> blockPosFloatEntry : BlockBreakingTracker.field1511.method666(this.field3434.getValue()).entrySet()) {
            final Entry<BlockPos, ?> entry = blockPosFloatEntry;
            if (!this.field3436.getValue() && entry.getKey().equals(((ClientPlayerInteractionManagerAccessor) BreakHighlight.mc.interactionManager).getCurrentBreakingPos())) {
                return;
            }
            final VoxelShape outlineShape = BreakHighlight.mc.world.getBlockState(entry.getKey()).getOutlineShape(BreakHighlight.mc.world, entry.getKey());
            if (outlineShape.isEmpty()) {
                return;
            }
            final Box boundingBox = outlineShape.getBoundingBox();
            if (this.field3435.getValue() && !entry.getKey().equals(((ClientPlayerInteractionManagerAccessor) BreakHighlight.mc.interactionManager).getCurrentBreakingPos()) && boundingBox.offset(entry.getKey()).getCenter().distanceTo(BreakHighlight.mc.player.getPos()) < 1.5 && (!this.field3455.containsKey(entry.getKey()) || System.currentTimeMillis() - this.field3455.get(entry.getKey()) > 10000L)) {
                NotificationManager.method1151(new Notification("BreakAlert", "Block facing " + Direction.getFacing((float) (MathHelper.floor(BreakHighlight.mc.player.getX()) - entry.getKey().getX()), (float) (MathHelper.floor(BreakHighlight.mc.player.getY()) - entry.getKey().getY()), (float) (MathHelper.floor(BreakHighlight.mc.player.getZ()) - entry.getKey().getZ())).getOpposite().name().toLowerCase(), Notifications.WARNING, NotificationPriority.Yellow));
                this.field3455.put(entry.getKey(), System.currentTimeMillis());
            }
            final float floatValue = (float) entry.getValue();
            switch (this.field3437.getValue().ordinal()) {
                case 0: {
                    final double n = 0.5 - floatValue * 0.5;
                    this.method1919(boundingBox.expand(-n, -n, -n).offset(entry.getKey()), this.field3440.getValue() ? this.field3452 : event.field1950);
                    continue;
                }
                case 1: {
                    final double n2 = floatValue * 0.5;
                    this.method1919(boundingBox.expand(-n2, -n2, -n2).offset(entry.getKey()), this.field3440.getValue() ? this.field3452 : event.field1950);
                    continue;
                }
                case 2: {
                    final double n3 = 0.5 - floatValue * 0.5;
                    this.method1919(boundingBox.expand(-n3, -n3, -n3).offset(entry.getKey()), this.field3440.getValue() ? this.field3452 : event.field1950);
                    final double n4 = floatValue * 0.5;
                    this.method1919(boundingBox.expand(-n4, -n4, -n4).offset(entry.getKey()), this.field3440.getValue() ? this.field3452 : event.field1950);
                    continue;
                }
                default:
                    this.method1919(boundingBox.offset(entry.getKey()), this.field3440.getValue() ? this.field3452 : event.field1950);
                    continue;
            }
        }
        if (this.field3440.getValue()) {
            ChamsShaderRenderer.method1310(() -> lambda$onRender3D$4(event), this.method1918(), this.field3442.getValue(), this.field3438, this.field3439, this.field3446.getValue(), this.field3447.getValue(), this.field3444.getValue(), this.field3445.getValue(), this.field3443.getValue(), this.field3453);
        }
    }

    private void method1917(Vector3d var1, double var2, int var4) {
        Class5922.method61(var1);
        String var8 = var2 >= 0.0 ? String.valueOf((int) MathHelper.clamp(var2 * 100.0, 0.0, 100.0)) : null;
        String var9 = null;
        if (var4 >= 0 && var4 != mc.player.getId() && mc.world.getEntityById(var4) != null) {
            var9 = mc.world.getEntityById(var4).getDisplayName().getString();
        }

        IFontRender.method499().startBuilding(1.5);
        double var10 = 0.0;
        if (var8 != null) {
            var10 -= IFontRender.method499().method1390() / 2.0;
        }

        if (var9 != null) {
            var10 -= IFontRender.method499().method1390() / 2.0;
        }

        if (var8 != null && var9 != null) {
            var10 -= IFontRender.method499().method1390() / 8.0;
        }

        if (var8 != null) {
            IFontRender.method499().drawShadowedText(var8, -IFontRender.method499().method501(var8) / 2.0, var10, RGBAColor.field402);
            var10 += IFontRender.method499().method1390() * 1.25;
        }

        if (var9 != null) {
            IFontRender.method499().drawShadowedText(var9, -IFontRender.method499().method501(var9) / 2.0, var10, RGBAColor.field402);
        }

        IFontRender.method499().endBuilding();
        Class5922.method2142();
    }

    private ShaderMode method1918() {
        if (this.field3441.getValue() == BreakHighlightShader.Image) {
            if (!this.field3448.getValue().isEmpty() && (!this.field3448.getValue().equals(this.field3454) || this.field3453 == null)) {
                File var4 = new File(ConfigManager.images, this.field3448.getValue() + ".png");

                try {
                    FileInputStream var5 = new FileInputStream(var4);
                    this.field3453 = ByteTexturePacker.method493(var5);
                    if (this.field3453 != null) {
                        this.field3454 = this.field3448.getValue();
                    } else {
                        this.field3454 = "";
                    }
                } catch (Exception var6) {
                    NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
                    this.field3448.setValue("");
                    this.field3454 = "";
                }
            }

            if (this.field3453 != null) {
                return ShaderMode.Image;
            }
        }

        return ShaderMode.Rainbow;
    }

    private void method1919(Box var1, Renderer3D var2) {
        if (this.field3440.getValue()) {
            var2.method1261(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ, RGBAColor.field402, 0);
        } else {
            var2.method1273(var1, this.field3438.getValue(), this.field3439.getValue(), ShapeMode.Full, 0);
        }
    }

    private void lambda$onRender3D$4(Render3DEvent var1) {
        this.field3452.method1219(var1.matrix);
    }

    private void lambda$onSendMovementPackets$3(BlockPos var1, Long var2) {
        if (System.currentTimeMillis() - var2 > 10000L || mc.world.getBlockState(var1).isAir()) {
            this.field3455.remove(var1);
        }
    }

    private boolean lambda$new$2() {
        return this.field3449.getValue() || this.field3450.getValue();
    }

    private boolean lambda$new$1() {
        return this.field3441.getValue() == BreakHighlightShader.Image;
    }

    private boolean lambda$new$0() {
        return this.field3444.getValue() > 0.0F;
    }
}
