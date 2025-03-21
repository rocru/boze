package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.TunnelShader;
import dev.boze.client.events.PreTickEvent;
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
import dev.boze.client.systems.iterators.ChunkIterator;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.tunnelesp.TunnelRenderer;
import dev.boze.client.utils.network.BozeExecutor;
import dev.boze.client.utils.render.ByteTexture;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;

import java.io.File;
import java.io.FileInputStream;

public class TunnelESP extends Module {
    public static final TunnelESP INSTANCE = new TunnelESP();
    public final ColorSetting field3815 = new ColorSetting("Color", new BozeDrawColor(1691587707), "Color for fill");
    public final ColorSetting field3816 = new ColorSetting("Outline", new BozeDrawColor(-2911109), "Color for outline");
    public final MinMaxSetting field3817 = new MinMaxSetting("Height", 2.0, 0.0, 2.0, 0.1, "Height of ESP");
    public final BooleanSetting field3818 = new BooleanSetting("Shader", false, "Use a shader");
    public final EnumSetting<TunnelShader> field3819 = new EnumSetting<TunnelShader>("Shader", TunnelShader.Normal, "Shader to use", this.field3818);
    public final BooleanSetting field3820 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3818);
    public final IntSetting field3821 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3818);
    public final FloatSetting field3822 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3818);
    public final FloatSetting field3823 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3818);
    private final IntSetting field3824 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3818);
    private final FloatSetting field3825 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3818);
    public final StringSetting field3826 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3818);
    private Renderer3D field3827 = null;
    private ByteTexture field3828;
    private String field3829 = "";
    private final Mutable field3830 = new Mutable();
    private final Long2ObjectMap<TunnelRenderer> field3831 = new Long2ObjectOpenHashMap();
    private Chunk field3832 = null;

    public TunnelESP() {
        super("TunnelESP", "Shows tunnels in terrain", Category.Render);
    }

    private static int method2067(int var0, int var1, int var2) {
        return (var0 & 0xFF) << 24 | (var1 & 65535) << 8 | var2 & 0xFF;
    }

    public static byte method2068(int var0) {
        return (byte) (var0 >> 24 & 0xFF);
    }

    public static short method2069(int var0) {
        return (short) (var0 >> 8 & 65535);
    }

    public static byte method2070(int var0) {
        return (byte) (var0 & 0xFF);
    }

    @Override
    public void onDisable() {
        this.field3831.clear();
        this.field3832 = null;
    }

    @EventHandler
    private void method2071(Render3DEvent var1) {
        if (this.field3818.getValue()) {
            if (this.field3827 == null) {
                this.field3827 = new Renderer3D(false, true);
            }

            this.field3827.method1217();
        }

        synchronized (this.field3831) {
            ObjectIterator var6 = this.field3831.values().iterator();

            while (var6.hasNext()) {
                TunnelRenderer var7 = (TunnelRenderer) var6.next();
                var7.method2082(this.field3818.getValue() ? this.field3827 : var1.field1950);
            }
        }

        if (this.field3818.getValue()) {
            ChamsShaderRenderer.method1310(
                    () -> lambda$onRender3D$2(var1),
                    this.method2081(),
                    this.field3820.getValue(),
                    this.field3815,
                    this.field3816,
                    this.field3824.getValue(),
                    this.field3825.getValue(),
                    this.field3822.getValue(),
                    this.field3823.getValue(),
                    this.field3821.getValue(),
                    this.field3828
            );
        }
    }

    @EventHandler
    private void method2072(PreTickEvent var1) {
        try {
            synchronized (this.field3831) {

                for (TunnelRenderer var7 : this.field3831.values()) {
                    var7.field3836 = false;
                }

                int var15 = 0;

                for (Chunk var8 : ChunkIterator.method544(true)) {
                    long var9 = ChunkPos.toLong(var8.getPos().x, var8.getPos().z);
                    if (this.field3831.containsKey(var9)) {
                        this.field3831.get(var9).field3836 = true;
                    } else if (var15 < 48) {
                        TunnelRenderer var11 = new TunnelRenderer(this, var8.getPos().x, var8.getPos().z);
                        this.field3831.put(var11.method2083(), var11);
                        BozeExecutor.method2200(() -> lambda$onTick$3(var8, var11));
                        var15++;
                    }
                }

                this.field3831.values().removeIf(TunnelESP::lambda$onTick$4);
            }
        } catch (Exception var14) {
        }
    }

    private void method2073(Chunk var1, TunnelRenderer var2) {
        IntOpenHashSet var6 = new IntOpenHashSet();
        this.field3832 = null;
        int var7 = var1.getPos().getStartX();
        int var8 = var1.getPos().getStartZ();
        int var9 = var1.getPos().getEndX();
        int var10 = var1.getPos().getEndZ();

        for (int var11 = var7; var11 <= var9; var11++) {
            for (int var12 = var8; var12 <= var10; var12++) {
                int var13 = var1.getHeightmap(Type.WORLD_SURFACE).get(var11 - var7, var12 - var8);

                for (short var14 = (short) mc.world.getBottomY(); var14 < var13; var14++) {
                    if (this.method2074(var11, var14, var12)) {
                        var6.add(method2067(var11 - var7, var14, var12 - var8));
                    }
                }
            }
        }

        IntOpenHashSet var22 = new IntOpenHashSet();
        IntIterator var23 = var6.iterator();

        while (var23.hasNext()) {
            int var24 = var23.nextInt();
            byte var25 = method2068(var24);
            short var15 = method2069(var24);
            byte var16 = method2070(var24);
            if (var25 != 0 && var25 != 15 && var16 != 0 && var16 != 15) {
                boolean var17 = false;

                for (Direction var21 : Direction.values()) {
                    if (var21.getAxis() != Axis.Y && var6.contains(method2067(var25 + var21.getOffsetX(), var15, var16 + var21.getOffsetZ()))) {
                        var17 = true;
                        break;
                    }
                }

                if (var17) {
                    var22.add(var24);
                }
            } else {
                var22.add(var24);
            }
        }

        var2.field3835 = var22;
    }

    private boolean method2074(int var1, int var2, int var3) {
        if (!this.method2078(var1, var2, var3)) {
            return false;
        } else {
            int var7 = this.method2075(var1 + 1, var2, var3);
            if (var7 == 1) {
                return false;
            } else {
                int var8 = this.method2075(var1 - 1, var2, var3);
                if (var8 == 1) {
                    return false;
                } else {
                    int var9 = this.method2075(var1, var2, var3 + 1);
                    if (var9 == 1) {
                        return false;
                    } else {
                        int var10 = this.method2075(var1, var2, var3 - 1);
                        return var10 != 1 && (var7 == 0 && var8 == 0 && var9 == 2 && var10 == 2 || var7 == 2 && var8 == 2 && var9 == 0 && var10 == 0);
                    }
                }
            }
        }
    }

    private int method2075(int var1, int var2, int var3) {
        if (this.method2078(var1, var2, var3)) {
            return 0;
        } else {
            return !this.method2077(var1, var2, var3) && !this.method2077(var1, var2 + 1, var3) ? 2 : 1;
        }
    }

    private boolean method2076(int var1, int var2, int var3) {
        BlockState var7 = this.method2080(var1, var2, var3);
        if (var7.isAir()) {
            return false;
        } else {
            return var7.getFluidState().isEmpty() && !var7.getCollisionShape(mc.world, this.field3830.set(var1, var2, var3)).isEmpty();
        }
    }

    private boolean method2077(int var1, int var2, int var3) {
        BlockState var7 = this.method2080(var1, var2, var3);
        if (var7.isAir()) {
            return true;
        } else {
            return var7.getFluidState().isEmpty() && var7.getCollisionShape(mc.world, this.field3830.set(var1, var2, var3)).isEmpty();
        }
    }

    private boolean method2078(int var1, int var2, int var3) {
        if (!this.method2076(var1, var2 - 1, var3)) {
            return false;
        } else if (!this.method2077(var1, var2, var3)) {
            return false;
        } else {
            return !this.method2077(var1, var2 + 2, var3) && this.method2077(var1, var2 + 1, var3);
        }
    }

    public boolean method2079(TunnelRenderer var1, int var2, int var3, int var4) {
        int var8;
        if (var2 == -1) {
            var1 = this.field3831.get(ChunkPos.toLong(var1.field3833 - 1, var1.field3834));
            var8 = method2067(15, var3, var4);
        } else if (var2 == 16) {
            var1 = this.field3831.get(ChunkPos.toLong(var1.field3833 + 1, var1.field3834));
            var8 = method2067(0, var3, var4);
        } else if (var4 == -1) {
            var1 = this.field3831.get(ChunkPos.toLong(var1.field3833, var1.field3834 - 1));
            var8 = method2067(var2, var3, 15);
        } else if (var4 == 16) {
            var1 = this.field3831.get(ChunkPos.toLong(var1.field3833, var1.field3834 + 1));
            var8 = method2067(var2, var3, 0);
        } else {
            var8 = method2067(var2, var3, var4);
        }

        return var1 != null && var1.field3835 != null && var1.field3835.contains(var8);
    }

    private BlockState method2080(int var1, int var2, int var3) {
        if (mc.world.isOutOfHeightLimit(var2)) {
            return Blocks.VOID_AIR.getDefaultState();
        } else {
            int var7 = var1 >> 4;
            int var8 = var3 >> 4;
            Chunk var9;
            if (this.field3832 != null && this.field3832.getPos().x == var7 && this.field3832.getPos().z == var8) {
                var9 = this.field3832;
            } else {
                var9 = mc.world.getChunk(var7, var8, ChunkStatus.FULL, false);
            }

            if (var9 == null) {
                return Blocks.VOID_AIR.getDefaultState();
            } else {
                ChunkSection var10 = var9.getSectionArray()[var9.getSectionIndex(var2)];
                if (var10 == null) {
                    return Blocks.VOID_AIR.getDefaultState();
                } else {
                    this.field3832 = var9;
                    return var10.getBlockState(var1 & 15, var2 & 15, var3 & 15);
                }
            }
        }
    }

    private ShaderMode method2081() {
        if (this.field3819.getValue() == TunnelShader.Image) {
            if (!this.field3826.getValue().isEmpty() && (!this.field3826.getValue().equals(this.field3829) || this.field3828 == null)) {
                File var4 = new File(ConfigManager.images, this.field3826.getValue() + ".png");

                try {
                    FileInputStream var5 = new FileInputStream(var4);
                    this.field3828 = ByteTexturePacker.method493(var5);
                    if (this.field3828 != null) {
                        this.field3829 = this.field3826.getValue();
                    } else {
                        this.field3829 = "";
                    }
                } catch (Exception var6) {
                    NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
                    this.field3826.setValue("");
                    this.field3829 = "";
                }
            }

            if (this.field3828 != null) {
                return ShaderMode.Image;
            }
        }

        return ShaderMode.Rainbow;
    }

    private static boolean lambda$onTick$4(TunnelRenderer var0) {
        return !var0.field3836;
    }

    private void lambda$onTick$3(Chunk var1, TunnelRenderer var2) {
        this.method2073(var1, var2);
    }

    private void lambda$onRender3D$2(Render3DEvent var1) {
        this.field3827.method1219(var1.matrix);
    }

    private boolean lambda$new$1() {
        return this.field3819.getValue() == TunnelShader.Image;
    }

    private boolean lambda$new$0() {
        return this.field3822.getValue() > 0.0F;
    }
}
