package dev.boze.client.systems.modules.misc;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.OreScannerShader;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.render.ByteTexture;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class OreScanner extends Module {
    public static final OreScanner INSTANCE = new OreScanner();
    private final IntSetting field3027 = new IntSetting("HRange", 5, 1, 30, 1, "Horizontal range");
    private final IntSetting field3028 = new IntSetting("VRange", 5, 1, 30, 1, "Vertical range");
    private final IntSetting field3029 = new IntSetting("ScanInterval", 0, 0, 20, 1, "Scan interval (in ticks)");
    private final IntSetting field3030 = new IntSetting("ScanAmount", 3, 1, 20, 1, "Amount of blocks to check per scan");
    private final BooleanSetting field3031 = new BooleanSetting("Render", true, "Render blocks being scanned");
    public final ColorSetting field3032 = new ColorSetting("Color", new BozeDrawColor(-1606228235), "Color for fill", this.field3031::getValue);
    public final ColorSetting field3033 = new ColorSetting("Outline", new BozeDrawColor(-12392715), "Color for outline", this.field3031::getValue);
    private final BooleanSetting field3034 = new BooleanSetting("Shader", false, "Use a shader", this.field3031::getValue);
    public final EnumSetting<OreScannerShader> field3035 = new EnumSetting<OreScannerShader>("Shader", OreScannerShader.Normal, "Shader to use", this.field3034);
    public final StringSetting field3042 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3034);
    public final BooleanSetting field3036 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3034);
    public final IntSetting field3037 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3034);
    public final FloatSetting field3038 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3034);
    public final FloatSetting field3039 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3034);
    private final IntSetting field3040 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3034);
    private final FloatSetting field3041 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3034);
    private final dev.boze.client.utils.Timer field3046 = new dev.boze.client.utils.Timer();
    private final ArrayList<BlockPos> field3047 = new ArrayList();
    private final dev.boze.client.utils.Timer field3049 = new dev.boze.client.utils.Timer();
    private Renderer3D field3043 = null;
    private ByteTexture field3044;
    private String field3045 = "";
    private BlockPos field3048 = null;

    public OreScanner() {
        super("OreScanner", "Anti ore obfuscation", Category.Misc);
        this.addSettings(this.field3031, this.field3034);
    }

    private static boolean lambda$onSendMovementPackets$3(BlockPos var0) {
        return !mc.world.isChunkLoaded(ChunkSectionPos.getSectionCoord(var0.getX()), ChunkSectionPos.getSectionCoord(var0.getZ()));
    }

    @Override
    public void onDisable() {
        this.field3047.clear();
        this.field3048 = null;
    }

    @EventHandler
    public void method1741(Render3DEvent event) {
        if (this.field3048 != null && !this.field3049.hasElapsed(500.0) && this.field3031.getValue()) {
            if (this.field3034.getValue()) {
                if (this.field3043 == null) {
                    this.field3043 = new Renderer3D(false, true);
                }

                this.field3043.method1217();
            }

            (this.field3034.getValue() ? this.field3043 : event.field1950)
                    .method1272(this.field3048, this.field3032.getValue(), this.field3033.getValue(), ShapeMode.Full, 0);
            if (this.field3034.getValue()) {
                ChamsShaderRenderer.method1310(
                        () -> lambda$onRender3D$2(event),
                        this.method1743(),
                        this.field3036.getValue(),
                        this.field3032,
                        this.field3033,
                        this.field3040.getValue(),
                        this.field3041.getValue(),
                        this.field3038.getValue(),
                        this.field3039.getValue(),
                        this.field3037.getValue(),
                        this.field3044
                );
            }
        }
    }

    @EventHandler
    public void method1742(MovementEvent event) {
        if (this.field3046.hasElapsed(this.field3029.getValue() * 50)) {
            int var5 = (int) mc.player.getX();
            int var6 = (int) mc.player.getY();
            int var7 = (int) mc.player.getZ();
            int var8 = 0;
            Hand var9 = mc.player.getMainHandStack().isEmpty() ? Hand.MAIN_HAND : Hand.OFF_HAND;

            for (int var10 = var5 - this.field3027.getValue(); var10 <= var5 + this.field3027.getValue(); var10++) {
                for (int var11 = var7 - this.field3027.getValue(); var11 <= var7 + this.field3027.getValue(); var11++) {
                    for (int var12 = Math.max(mc.world.getBottomY(), var6 - this.field3028.getValue());
                         var12 <= var6 + this.field3028.getValue() && var12 <= mc.world.getTopY();
                         var12++
                    ) {
                        if (var8 >= this.field3030.getValue()) {
                            return;
                        }

                        BlockPos var13 = new BlockPos(var10, var12, var11);
                        if (!this.field3047.contains(var13)) {
                            Class5913.method17(
                                    var9,
                                    new BlockHitResult(
                                            new Vec3d((double) var13.getX() + 0.5, (double) var13.getY() + 0.5, (double) var13.getZ() + 0.5), Direction.UP, var13, false
                                    )
                            );
                            this.field3047.add(var13);
                            this.field3048 = var13;
                            this.field3049.reset();
                            var8++;
                        }
                    }
                }
            }

            this.field3047.removeIf(OreScanner::lambda$onSendMovementPackets$3);
        }
    }

    private ShaderMode method1743() {
        if (this.field3035.getValue() == OreScannerShader.Image) {
            if (!this.field3042.getValue().isEmpty() && (!this.field3042.getValue().equals(this.field3045) || this.field3044 == null)) {
                File var4 = new File(ConfigManager.images, this.field3042.getValue() + ".png");

                try {
                    FileInputStream var5 = new FileInputStream(var4);
                    this.field3044 = ByteTexturePacker.method493(var5);
                    if (this.field3044 != null) {
                        this.field3045 = this.field3042.getValue();
                    } else {
                        this.field3045 = "";
                    }
                } catch (Exception var6) {
                    NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
                    this.field3042.setValue("");
                    this.field3045 = "";
                }
            }

            if (this.field3044 != null) {
                return ShaderMode.Image;
            }
        }

        return ShaderMode.Rainbow;
    }

    private void lambda$onRender3D$2(Render3DEvent var1) {
        this.field3043.method1219(var1.matrix);
    }

    private boolean lambda$new$1() {
        return this.field3035.getValue() == OreScannerShader.Image;
    }

    private boolean lambda$new$0() {
        return this.field3038.getValue() > 0.0F;
    }
}
