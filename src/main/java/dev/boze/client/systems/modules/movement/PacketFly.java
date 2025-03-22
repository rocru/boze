package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.mixin.PlayerPositionLookS2CPacketAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class3090;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PacketFly extends Module {
    public static final PacketFly INSTANCE = new PacketFly();
    private final MinMaxSetting field499 = new MinMaxSetting("Speed", 1.0, 0.1, 10.0, 0.1, "Speed");
    private final EnumSetting<PacketFlyMode> field500 = new EnumSetting<PacketFlyMode>("Mode", PacketFlyMode.Factor, "Mode for packet flying");
    private final MinMaxSetting field501 = new MinMaxSetting("Factor", 1.0, 0.1, 10.0, 0.1, "Factor for packetfly", this::lambda$new$0);
    private final BooleanSetting field502 = new BooleanSetting("Thrust", false, "Thrust forwards");
    private final EnumSetting<PacketFlyBounds> field503 = new EnumSetting<PacketFlyBounds>("Bounds", PacketFlyBounds.World, "Bounds mode");
    private final EnumSetting<PacketFlyType> field504 = new EnumSetting<PacketFlyType>("Type", PacketFlyType.Down, "Type of bounds packets to send");
    private final EnumSetting<PacketFlyLimit> field505 = new EnumSetting<PacketFlyLimit>("Limit", PacketFlyLimit.Off, "Limit for packets");
    private final BooleanSetting field506 = new BooleanSetting("Conceal", false, "Conceal packet flying");
    private final BooleanSetting field507 = new BooleanSetting("AntiKick", true, "Slowly go down to not get kicked");
    private final EnumSetting<PacketFlyPhase> field508 = new EnumSetting<PacketFlyPhase>("Phase", PacketFlyPhase.Off, "Phase");
    private final BooleanSetting field509 = new BooleanSetting("Constrict", false, "Constrict flight");
    private final ArrayList<PlayerMoveC2SPacket> field513 = new ArrayList();
    private final ConcurrentHashMap<Integer, Class3090> field514 = new ConcurrentHashMap();
    private final Random field526 = new Random();
    private int field510 = 0;
    private TeleportConfirmC2SPacket field511 = null;
    private PositionAndOnGround field512 = null;
    private int field515 = 0;
    private int field516 = 0;
    private int field517 = 0;
    private boolean field518 = false;
    private int field519 = 0;
    private int field520 = 0;
    private int field521 = 0;
    private boolean field522 = false;
    private double field523 = 0.0;
    private double field524 = 0.0;
    private double field525 = 0.0;

    public PacketFly() {
        super("PacketFly", "Fly using packets", Category.Movement);
    }

    @EventHandler
    public void method2072(PreTickEvent event) {
        if (!MinecraftUtils.isClientActive() || mc.currentScreen instanceof DownloadingTerrainScreen) {
            this.setEnabled(false);
        }
    }

    @EventHandler
    private void method1831(PrePlayerTickEvent prePlayerTickEvent) {
        if (PacketFly.mc.player.age % 20 == 0) {
            this.method1904();
        }
        PacketFly.mc.player.setVelocity(Vec3d.ZERO);
        if (this.field500.getValue() == PacketFlyMode.Slow) {
            return;
        }
        if (this.field510 <= 0 && this.field500.getValue() != PacketFlyMode.Setback) {
            this.field512 = new PlayerMoveC2SPacket.PositionAndOnGround(this.method2091(), 1.0, this.method2091(), PacketFly.mc.player.isOnGround());
            this.field513.add(this.field512);
            PacketFly.mc.player.networkHandler.sendPacket(this.field512);
            return;
        }
        boolean bl = this.method1972();
        this.field523 = 0.0;
        this.field524 = 0.0;
        this.field525 = 0.0;
        if (PacketFly.mc.options.jumpKey.isPressed() && (this.field517 < 1 || bl)) {
            this.field524 = 0.062;
            if (PacketFly.mc.player.age % (this.field500.getValue() == PacketFlyMode.Setback ? 10 : 20) == 0) {
                this.field524 = -0.032;
            }
            this.field515 = 0;
            this.field516 = 5;
        } else if (PacketFly.mc.options.sneakKey.isPressed() && (this.field517 < 1 || bl)) {
            this.field524 = -0.062;
            this.field515 = 0;
            this.field516 = 5;
        }
        if (bl || !PacketFly.mc.options.sneakKey.isPressed() && !PacketFly.mc.options.jumpKey.isPressed()) {
            if (this.method1971()) {
                Vec3d vec3d = Class5924.method93((bl && this.field508.getValue() != PacketFlyPhase.Off ? (this.field508.getValue() == PacketFlyPhase.Fast ? (this.field524 != 0.0 ? 0.0465 : 0.062) : 0.031) : 0.26) * this.field499.getValue());
                if (!(vec3d.x == 0.0 && vec3d.z == 0.0 || this.field516 >= 1 && !bl)) {
                    this.field523 = vec3d.x;
                    this.field525 = vec3d.z;
                    this.field517 = 5;
                }
            }
            if (this.field507.getValue() && (this.field505.getValue() == PacketFlyLimit.Off || this.field519 != 0)) {
                if (this.field515 < 3) {
                    ++this.field515;
                } else {
                    this.field515 = 0;
                    if (!bl) {
                        this.field524 = -0.04;
                    }
                }
            }
        }
        if (bl && (this.field508.getValue() != PacketFlyPhase.Off && (double) PacketFly.mc.player.forwardSpeed != 0.0 || (double) PacketFly.mc.player.sidewaysSpeed != 0.0 && this.field524 != 0.0)) {
            this.field524 /= 2.5;
        }
        if (this.field505.getValue() == PacketFlyLimit.Tick || this.field505.getValue() == PacketFlyLimit.Both) {
            if (this.field519 == 0) {
                this.field523 = 0.0;
                this.field524 = 0.0;
                this.field525 = 0.0;
            } else if (this.field519 == 2 && this.field505.getValue() == PacketFlyLimit.Both) {
                if (this.field522) {
                    this.field523 = 0.0;
                    this.field524 = 0.0;
                    this.field525 = 0.0;
                }
                this.field522 = !this.field522;
            }
        } else if (this.field505.getValue() == PacketFlyLimit.Speed && this.field520 == 7) {
            this.field523 = 0.0;
            this.field524 = 0.0;
            this.field525 = 0.0;
        }
        switch (this.field500.getValue().ordinal()) {
            case 1: {
                if (!this.field506.getValue()) {
                    PacketFly.mc.player.setVelocity(this.field523, this.field524, this.field525);
                }
                this.method270(this.field523, this.field524, this.field525, true);
                break;
            }
            case 2: {
                if (!this.field506.getValue()) {
                    PacketFly.mc.player.setVelocity(this.field523, this.field524, this.field525);
                }
                this.method270(this.field523, this.field524, this.field525, false);
                break;
            }
            case 0: {
                double d = this.field501.getValue();
                int n = (int) Math.floor(d);
                double d2 = d - (double) n;
                if (Math.random() < d2) {
                    ++n;
                }
                if (this.field502.getValue() && this.field519 == 1) {
                    ++n;
                }
                for (int i = 1; i <= n; ++i) {
                    if (!this.field506.getValue()) {
                        PacketFly.mc.player.setVelocity(this.field523 * (double) i, this.field524 * (double) i, this.field525 * (double) i);
                    }
                    this.method270(this.field523 * (double) i, this.field524 * (double) i, this.field525 * (double) i, true);
                    if ((this.field505.getValue() == PacketFlyLimit.Tick || this.field505.getValue() == PacketFlyLimit.Both) && this.field519 == 0)
                        break;
                }
                this.field523 = PacketFly.mc.player.getVelocity().getX();
                this.field524 = PacketFly.mc.player.getVelocity().getY();
                this.field525 = PacketFly.mc.player.getVelocity().getZ();
            }
        }
        --this.field516;
        --this.field517;
        ++this.field519;
        ++this.field520;
        if (this.field519 > 3) {
            this.field519 = 0;
            boolean bl2 = this.field518 = !this.field518;
        }
        if (this.field520 > 7) {
            this.field520 = 0;
        }
    }

    private void method270(double var1, double var3, double var5, boolean var7) {
        if (this.field511 != null) {
            mc.player.networkHandler.sendPacket(this.field511);
            this.field511 = null;
        }

        Vec3d var11 = new Vec3d(mc.player.getX() + var1, mc.player.getY() + var3, mc.player.getZ() + var5);
        Vec3d var12 = this.method271(var1, var3, var5);
        PositionAndOnGround var13 = new PositionAndOnGround(var11.x, var11.y, var11.z, mc.player.isOnGround());
        this.field513.add(var13);
        mc.player.networkHandler.sendPacket(var13);
        if (this.field505.getValue() != PacketFlyLimit.Tick && this.field505.getValue() != PacketFlyLimit.Both || this.field519 != 0) {
            PositionAndOnGround var14 = new PositionAndOnGround(var12.x, var12.y, var12.z, mc.player.isOnGround());
            this.field513.add(var14);
            mc.player.networkHandler.sendPacket(var14);
            if (this.field509.getValue()) {
                for (int var15 = 0; var15 <= 6; var15++) {
                    var13 = new PositionAndOnGround(var11.x, var11.y, var11.z, mc.player.isOnGround());
                    this.field513.add(var13);
                    mc.player.networkHandler.sendPacket(var13);
                }
            }

            if (var7) {
                this.field510++;
                this.field514.put(this.field510, new Class3090(var11.x, var11.y, var11.z, System.currentTimeMillis()));
                this.field511 = new TeleportConfirmC2SPacket(this.field510);
            }
        }
    }

    private Vec3d method271(double d, double d2, double d3) {
        return switch (this.field504.getValue().ordinal()) {
            case 0 ->
                    new Vec3d(PacketFly.mc.player.getX() + d, this.field503.getValue() != PacketFlyBounds.Normal ? (double) (this.field503.getValue() == PacketFlyBounds.Strict ? 255 : 256) : PacketFly.mc.player.getY() + 420.0, PacketFly.mc.player.getZ() + d3);
            case 1 ->
                    new Vec3d(this.field503.getValue() != PacketFlyBounds.Normal ? PacketFly.mc.player.getX() + this.method2091() : this.method2091(), this.field503.getValue() == PacketFlyBounds.Strict ? Math.max(PacketFly.mc.player.getY(), 2.0) : PacketFly.mc.player.getY(), this.field503.getValue() != PacketFlyBounds.Normal ? PacketFly.mc.player.getZ() + this.method2091() : this.method2091());
            case 3 ->
                    new Vec3d(PacketFly.mc.player.getX() + (this.field503.getValue() == PacketFlyBounds.Strict ? d : this.method1614()), PacketFly.mc.player.getY() + this.method1614(), PacketFly.mc.player.getZ() + (this.field503.getValue() == PacketFlyBounds.Strict ? d3 : this.method1614()));
            default ->
                    new Vec3d(PacketFly.mc.player.getX() + d, this.field503.getValue() != PacketFlyBounds.Normal ? (double) (this.field503.getValue() == PacketFlyBounds.Strict ? 1 : 0) : PacketFly.mc.player.getY() - 1337.0, PacketFly.mc.player.getZ() + d3);
        };
    }

    private double method2091() {
        int var4 = this.field526.nextInt(this.field503.getValue() != PacketFlyBounds.Normal ? 80 : 29000000)
                + (this.field503.getValue() != PacketFlyBounds.Normal ? 5 : 500);
        return this.field526.nextBoolean() ? (double) var4 : (double) (-var4);
    }

    private double method1614() {
        int var4 = this.field526.nextInt(22);
        var4 += 70;
        return this.field526.nextBoolean() ? (double) var4 : (double) (-var4);
    }

    private boolean method1971() {
        if (mc.options.jumpKey.isPressed()) {
            return true;
        } else if (mc.options.forwardKey.isPressed()) {
            return true;
        } else if (mc.options.backKey.isPressed()) {
            return true;
        } else {
            return mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed();
        }
    }

    private boolean method1972() {
        return mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox()).iterator().hasNext();
    }

    private void method1904() {
        this.field514.forEach(this::lambda$cleanPosLooks$1);
    }

    @Override
    public void onEnable() {
        if (!MinecraftUtils.isClientActive()) {
            this.setEnabled(false);
        } else {
            this.field513.clear();
            this.field514.clear();
            this.field510 = 0;
            this.field516 = 0;
            this.field517 = 0;
            this.field515 = 0;
            this.field519 = 0;
            this.field520 = 0;
            this.field521 = 0;
            this.field523 = 0.0;
            this.field524 = 0.0;
            this.field525 = 0.0;
            this.field522 = false;
            this.field512 = new PositionAndOnGround(this.method2091(), 1.0, this.method2091(), mc.player.isOnGround());
            this.field513.add(this.field512);
            mc.player.networkHandler.sendPacket(this.field512);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setVelocity(Vec3d.ZERO);
        }
    }

    @EventHandler
    private void method2042(PacketBundleEvent var1) {
        if (var1.packet instanceof PlayerPositionLookS2CPacket) {
            if (mc.currentScreen instanceof DownloadingTerrainScreen) {
                this.field510 = 0;
            } else {
                PlayerPositionLookS2CPacket var5 = (PlayerPositionLookS2CPacket) var1.packet;
                if (mc.player.isAlive()) {
                    if (this.field510 <= 0) {
                        this.field510 = var5.getTeleportId();
                    } else if (this.field500.getValue() != PacketFlyMode.Setback
                            && mc.world.isChunkLoaded(ChunkSectionPos.getSectionCoord(var5.getX()), ChunkSectionPos.getSectionCoord(var5.getZ()))
                            && this.field514.containsKey(var5.getTeleportId())) {
                        Vec3d var6 = this.field514.get(var5.getTeleportId());
                        if (var6.x == var5.getX() && var6.y == var5.getY() && var6.z == var5.getZ()) {
                            this.field514.remove(var5.getTeleportId());
                            var1.method1020();
                            if (this.field506.getValue()) {
                                mc.player.setPosition(var5.getX(), var5.getY(), var5.getZ());
                            }

                            return;
                        }
                    }
                }

                ((PlayerPositionLookS2CPacketAccessor) var1.packet).setYaw(mc.player.getYaw());
                ((PlayerPositionLookS2CPacketAccessor) var1.packet).setPitch(mc.player.getPitch());
                var5.getFlags().remove(PositionFlag.X_ROT);
                var5.getFlags().remove(PositionFlag.Y_ROT);
                this.field510 = var5.getTeleportId();
            }
        }
    }

    @EventHandler
    public void method1893(PlayerMoveEvent event) {
        if (this.field500.getValue() == PacketFlyMode.Slow) {
            double var5 = this.field501.getValue();
            int var7 = (int) Math.floor(var5);
            double var8 = var5 - (double) var7;
            if (Math.random() < var8) {
                var7++;
            }

            if (this.field502.getValue() && this.field519 == 1) {
                var7++;
            }

            for (int var10 = 1; var10 <= var7; var10++) {
                if (!this.field506.getValue()) {
                    mc.player.setVelocity(this.field523, this.field524, this.field525);
                }

                this.method270(this.field523, this.field524, this.field525, true);
                if ((this.field505.getValue() == PacketFlyLimit.Tick || this.field505.getValue() == PacketFlyLimit.Both) && this.field519 == 0) {
                    break;
                }
            }

            this.field523 = mc.player.getVelocity().getX();
            this.field524 = mc.player.getVelocity().getY();
            this.field525 = mc.player.getVelocity().getZ();
            event.vec3 = new Vec3d(0.0, mc.options.jumpKey.isPressed() ? 0.05 : 0.0, 0.0);
        } else {
            if (this.field500.getValue() != PacketFlyMode.Setback && this.field510 <= 0) {
                return;
            }

            if (this.field506.getValue()) {
                event.vec3 = Vec3d.ZERO;
            } else {
                event.vec3 = new Vec3d(this.field523, this.field524, this.field525);
            }

            if (this.field508.getValue() != PacketFlyPhase.Off && this.method1972()) {
                mc.player.noClip = true;
            }
        }
    }

    @EventHandler
    private void method1853(PrePacketSendEvent var1) {
        if (var1.packet instanceof PlayerMoveC2SPacket) {
            if (var1.packet instanceof PositionAndOnGround var5) {
                if (this.field513.contains(var5)) {
                    this.field513.remove(var5);
                } else {
                    var1.method1020();
                }
            } else {
                var1.method1020();
            }
        }
    }

    @EventHandler
    public void method1890(PlayerVelocityEvent event) {
        event.method1020();
    }

    private void lambda$cleanPosLooks$1(Integer var1, Class3090 var2) {
        if (System.currentTimeMillis() - var2.field216 > TimeUnit.SECONDS.toMillis(30L)) {
            this.field514.remove(var1);
        }
    }

    private boolean lambda$new$0() {
        return this.field500.getValue() == PacketFlyMode.Factor || this.field500.getValue() == PacketFlyMode.Slow;
    }
}
