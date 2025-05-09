package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.LogoutSpotsShader;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.*;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.logoutspots.LogoutPlayerEntity;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.ByteTexture;
import mapped.Class5922;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogoutSpots extends Module {
    public static final LogoutSpots INSTANCE = new LogoutSpots();
    public final ColorSetting field812 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill");
    public final ColorSetting field813 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline");
    private final BooleanSetting field810 = new BooleanSetting("Notify", true, "Notify when a player logs back in");
    private final BooleanSetting field811 = new BooleanSetting("Model", true, "Use player model instead of box");
    private final BooleanSetting field814 = new BooleanSetting("Shader", false, "Use a shader");
    public final EnumSetting<LogoutSpotsShader> field815 = new EnumSetting<LogoutSpotsShader>("Shader", LogoutSpotsShader.Normal, "Shader to use", this.field814);
    public final StringSetting field822 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field814);
    public final BooleanSetting field816 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field814);
    public final IntSetting field817 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field814);
    public final FloatSetting field818 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field814);
    public final FloatSetting field819 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field814);
    private final IntSetting field820 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field814);
    private final FloatSetting field821 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field814);
    private final SettingCategory field823 = new SettingCategory("Nametag", "Nametag settings");
    private final BooleanSetting field824 = new BooleanSetting("ShowHealth", true, "Show logout health on nametag", this.field823);
    private final RGBASetting field825 = new RGBASetting("Color", new RGBAColor(-49088), "Color for name", this.field823);
    private final RGBASetting field826 = new RGBASetting("FriendColor", new RGBAColor(-24512), "Color for name", this.field823);
    private final ScalingSetting field827 = new ScalingSetting();
    private final ArrayList<String> field831 = new ArrayList(Arrays.asList("soon_1", "soon_2", "soon_3", "soon_4", "soon_5", "help_desk_npc"));
    private final CopyOnWriteArrayList<LogoutPlayerEntity> field832 = new CopyOnWriteArrayList();
    private final List<PlayerListEntry> field833 = new ArrayList();
    private final List<PlayerEntity> field834 = new ArrayList();
    private Renderer3D field828;
    private ByteTexture field829;
    private String field830 = "";
    private String field835;
    private boolean field836 = false;

    public LogoutSpots() {
        super("LogoutSpots", "Shows where players have logged out", Category.Render);
    }

    private static boolean lambda$add$4(LogoutPlayerEntity var0, LogoutPlayerEntity var1) {
        return var1.field3589.equals(var0.field3589);
    }

    private static boolean lambda$onPlayerTick$3(PlayerListEntry var0, PlayerListEntry var1) {
        return var1.getProfile().equals(var0.getProfile());
    }

    @Override
    public void onEnable() {
        if (MinecraftUtils.isClientActive()) {
            this.field833.addAll(mc.getNetworkHandler().getPlayerList());
            this.method1904();
            this.field835 = mc.world.getRegistryKey().getValue().getPath();
            this.field836 = false;
        }
    }

    @Override
    public void onDisable() {
        this.field832.clear();
        this.field833.clear();
    }

    private void method1904() {
        this.field834.clear();

        for (Entity var5 : mc.world.getEntities()) {
            if (var5 instanceof PlayerEntity) {
                this.field834.add((PlayerEntity) var5);
            }
        }
    }

    @EventHandler
    private void method1966(GameJoinEvent var1) {
        this.onDisable();
        this.field836 = true;
    }

    @EventHandler
    private void method2040(Render2DEvent var1) {
        for (LogoutPlayerEntity var6 : this.field832) {
            Frustum var7 = ((WorldRendererAccessor) mc.worldRenderer).getFrustum();
            if (var7 != null) {
                double var8 = var6.field3587 / 2.0;
                if (!var7.isVisible(
                        new Box(
                                var6.field3586.x - var8,
                                var6.field3586.y,
                                var6.field3586.z - var8,
                                var6.field3586.x + var8,
                                var6.field3586.y + var6.field3588,
                                var6.field3586.z + var8
                        )
                )) {
                    continue;
                }
            }

            Vec3d var11 = var6.field3586.add(0.0, var6.field3588 + 0.2, 0.0);
            Vector3d var9 = new Vector3d(var11.x, var11.y, var11.z);
            boolean var10 = Class5922.method59(var9, this.field827);
            if (var10) {
                NameTags.INSTANCE.method374(var6, var9, this.field824.getValue(), this.field825.getValue(), this.field826.getValue());
            }
        }
    }

    @EventHandler
    private void method2071(Render3DEvent var1) {
        if (!this.field832.isEmpty()) {
            if (this.field814.getValue()) {
                if (this.field828 == null) {
                    this.field828 = new Renderer3D(false, true);
                }

                this.field828.method1217();
            }

            for (LogoutPlayerEntity var6 : this.field832) {
                double var7 = var6.field3587 / 2.0;
                if (this.field811.getValue()) {
                    if (this.field814.getValue()) {
                        var6.field3592.method549(this.field828, BozeDrawColor.field1841, BozeDrawColor.field1841, var1.field1951);
                    } else {
                        var6.field3592.method549(var1.field1950, this.field812.getValue(), this.field813.getValue(), var1.field1951);
                    }
                } else if (this.field814.getValue()) {
                    this.field828
                            .method1261(
                                    var6.field3586.x - var7,
                                    var6.field3586.y,
                                    var6.field3586.z - var7,
                                    var6.field3586.x + var7,
                                    var6.field3586.y + var6.field3588,
                                    var6.field3586.z + var7,
                                    RGBAColor.field402,
                                    0
                            );
                } else {
                    var1.field1950
                            .method1271(
                                    var6.field3586.x - var7,
                                    var6.field3586.y,
                                    var6.field3586.z - var7,
                                    var6.field3586.x + var7,
                                    var6.field3586.y + var6.field3588,
                                    var6.field3586.z + var7,
                                    this.field812.getValue(),
                                    this.field813.getValue(),
                                    ShapeMode.Full,
                                    0
                            );
                }
            }

            if (this.field814.getValue()) {
                ChamsShaderRenderer.method1310(
                        () -> lambda$onRender3D$2(var1),
                        this.method1921(),
                        this.field816.getValue(),
                        this.field812,
                        this.field813,
                        this.field820.getValue(),
                        this.field821.getValue(),
                        this.field818.getValue(),
                        this.field819.getValue(),
                        this.field817.getValue(),
                        this.field829
                );
            }
        }
    }

    private ShaderMode method1921() {
        if (this.field815.getValue() == LogoutSpotsShader.Image) {
            if (!this.field822.getValue().isEmpty() && (!this.field822.getValue().equals(this.field830) || this.field829 == null)) {
                File var4 = new File(ConfigManager.images, this.field822.getValue() + ".png");

                try {
                    FileInputStream var5 = new FileInputStream(var4);
                    this.field829 = ByteTexturePacker.method493(var5);
                    if (this.field829 != null) {
                        this.field830 = this.field822.getValue();
                    } else {
                        this.field830 = "";
                    }
                } catch (Exception var6) {
                    NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
                    this.field822.setValue("");
                    this.field830 = "";
                }
            }

            if (this.field829 != null) {
                return ShaderMode.Image;
            }
        }

        return ShaderMode.Rainbow;
    }

    @EventHandler
    private void method380(EntityAddedEvent var1) {
        if (this.field836) {
            this.onEnable();
        }

        if (var1.field1913 instanceof PlayerEntity) {
            int var5 = -1;

            for (int var6 = 0; var6 < this.field832.size(); var6++) {
                if (this.field832.get(var6).field3589.equals(var1.field1913.getUuid())) {
                    var5 = var6;
                    break;
                }
            }

            if (var5 != -1) {
                this.field832.remove(var5);
            }
        }
    }

    @EventHandler
    private void method2042(PacketBundleEvent var1) {
        try {
            if (var1.packet instanceof PlayerListS2CPacket var5) {
                Iterator var13 = var5.getActions().iterator();
                Iterator var7 = var5.getEntries().iterator();

                while (var13.hasNext() && var7.hasNext()) {
                    Action var8 = (Action) var13.next();
                    Entry var9 = (Entry) var7.next();
                    if (var8 == Action.ADD_PLAYER) {
                        for (LogoutPlayerEntity var11 : this.field832) {
                            if (var11.field3589.equals(var9.profileId())) {
                                this.field832.remove(var11);
                                if (this.field810.getValue()) {
                                    ChatInstance.method740(
                                            this.getName(),
                                            "%s logged back in at (%d, %d, %d)",
                                            var9.displayName().getString(),
                                            (int) var11.field3586.x,
                                            (int) var11.field3586.y,
                                            (int) var11.field3586.z
                                    );
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception var12) {
        }
    }

    @EventHandler
    private void method1942(PostPlayerTickEvent postPlayerTickEvent) {
        if (this.field836) {
            this.onEnable();
        }
        if (mc.getNetworkHandler().getPlayerList().size() != this.field833.size()) {
            for (PlayerListEntry playerListEntry : this.field833) {
                if (mc.getNetworkHandler().getPlayerList().stream().anyMatch(arg_0 -> LogoutSpots.lambda$onPlayerTick$3(playerListEntry, arg_0)))
                    continue;
                for (PlayerEntity playerEntity : this.field834) {
                    if (!playerEntity.getUuid().equals(playerListEntry.getProfile().getId())) continue;
                    this.method381(new LogoutPlayerEntity(this, playerEntity));
                }
            }
            this.field833.clear();
            this.field833.addAll(mc.getNetworkHandler().getPlayerList());
            this.method1904();
        }
        if (LogoutSpots.mc.player.age % 40 == 0) {
            this.method1904();
        }
        String path = LogoutSpots.mc.world.getRegistryKey().getValue().getPath();
        if (!path.equals(this.field835)) {
            this.field832.clear();
        }
        this.field835 = path;
    }

    private void method381(LogoutPlayerEntity logoutPlayerEntity) {
        if (this.field831.contains(logoutPlayerEntity.field3590)) {
            return;
        }
        this.field832.removeIf(arg_0 -> LogoutSpots.lambda$add$4(logoutPlayerEntity, arg_0));
        this.field832.add(logoutPlayerEntity);
    }

    private void lambda$onRender3D$2(Render3DEvent var1) {
        this.field828.method1219(var1.matrix);
    }

    private boolean lambda$new$1() {
        return this.field815.getValue() == LogoutSpotsShader.Image;
    }

    private boolean lambda$new$0() {
        return this.field818.getValue() > 0.0F;
    }
}
