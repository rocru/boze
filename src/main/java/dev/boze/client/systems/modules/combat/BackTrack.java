package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.BacktrackMode;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostAttackEntityEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.legit.AntiBots;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.fakeplayer.FakeClientPlayerEntity;
import mapped.Class5918;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackTrack extends Module {
    public static final BackTrack INSTANCE = new BackTrack();
    private final EnumSetting<BacktrackMode> mode = new EnumSetting<BacktrackMode>(
            "Mode",
            BacktrackMode.Once,
            "Mode for BackTrack\n - Once: BackTrack on enable\n - Smart: BackTrack when chasing\nOnce is recommended, smart guesses your next moves\nWith once you can enable when you know it's the best time\n"
    );
    private final IntSetting ticks = new IntSetting("Ticks", 10, 1, 100, 1, "Max ticks for backtracking\nHigh ticks can get you kicked\n");
    private final IntSetting cooldown = new IntSetting("Cooldown", 100, 1, 1000, 1, "Cooldown in ticks for smart mode", this::lambda$new$0);
    private final BooleanSetting friends = new BooleanSetting("Friends", false, "Target friends");
    private final Queue<Packet<?>> field1025 = new LinkedList();
    private final Queue<Packet<ClientPlayPacketListener>> field1026 = new LinkedList();
    private final AtomicBoolean field1027 = new AtomicBoolean(false);
    public int field1028 = 0;

    public BackTrack() {
        super("BackTrack", "Simulates lag spike to let you hit players' past positions", Category.Combat);
    }

    @Override
    public String method1322() {
        return this.field1028 >= 0 ? this.field1025.size() + "," + this.field1026.size() : super.method1322();
    }

    @Override
    public void onEnable() {
        this.field1028 = this.mode.getValue() == BacktrackMode.Once ? 0 : -1;
    }

    @Override
    public void onDisable() {
        if (MinecraftUtils.isClientActive() && (!this.field1025.isEmpty() || !this.field1026.isEmpty())) {
            this.method1904();
        }
    }

    private void method1904() {
        if (this.mode.getValue() == BacktrackMode.Once) {
            this.setEnabled(false);
        }

        this.field1027.set(true);

        while (!this.field1025.isEmpty()) {
            mc.player.networkHandler.sendPacket(this.field1025.poll());
        }

        while (!this.field1026.isEmpty()) {
            Packet var4 = this.field1026.poll();
            var4.apply(mc.player.networkHandler);
        }

        this.field1027.set(false);
        this.field1028 = -this.cooldown.getValue();
    }

    @EventHandler
    private void method2072(PreTickEvent var1) {
        if (!MinecraftUtils.isClientActive()) {
            if (this.mode.getValue() == BacktrackMode.Smart) {
                this.field1028 = -1;
            } else {
                this.setEnabled(false);
            }
        } else {
            if (this.field1028 < -1) {
                if (this.mode.getValue() == BacktrackMode.Smart) {
                    this.field1028++;
                } else {
                    this.field1028 = 0;
                }
            } else if (this.field1028 == -1) {
                if (this.mode.getValue() == BacktrackMode.Smart) {
                    boolean var5 = false;

                    for (PlayerEntity var7 : mc.world.getPlayers()) {
                        if (this.method2055(var7) && (double) mc.player.distanceTo(var7) <= Reach.method1613()) {
                            var5 = true;
                            break;
                        }
                    }

                    if (!var5) {
                        Pair var15 = Class5918.method38(this.ticks.getValue(), mc.player);
                        ArrayList var16 = (ArrayList) var15.getRight();

                        for (PlayerEntity var9 : mc.world.getPlayers()) {
                            if (this.method2055(var9)) {
                                Vec3d var10 = var9.getPos();
                                if (!(var10.squaredDistanceTo(var9.prevX, var9.prevY, var9.prevZ) < 1.0E-4)) {
                                    Pair<ClientPlayerEntity, ArrayList<Vec3d>> var11 = null;

                                    for (Object o : var16) {
                                        Vec3d var13 = (Vec3d) o;
                                        if (var13.distanceTo(var10) <= Reach.method1613()) {
                                            var11 = Class5918.method38(this.ticks.getValue(), var9);

                                            for (int var14 = 0; var14 < this.ticks.getValue(); var14++) {
                                                if (((Vec3d) var16.get(var14)).distanceTo((Vec3d) ((ArrayList<?>) var11.getRight()).get(var14)) <= Reach.method1613()) {
                                                    return;
                                                }
                                            }

                                            this.field1028 = 0;
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    this.field1028 = 0;
                }
            } else if (this.field1028 >= this.ticks.getValue()) {
                this.method1904();
            } else {
                this.field1028++;
            }
        }
    }

    @EventHandler
    public void method1627(PostAttackEntityEvent event) {
        if (this.field1028 >= 0) {
            if (event.entity instanceof FakeClientPlayerEntity) {
                this.method1904();
            }
        }
    }

    @EventHandler
    public void method2042(PacketBundleEvent event) {
        if (!this.field1027.get() && this.field1028 >= 0 && MinecraftUtils.isClientActive()) {
            event.method1020();
            this.field1026.add((Packet<ClientPlayPacketListener>) event.packet);
        }
    }

    @EventHandler
    public void method1853(PrePacketSendEvent event) {
        if (!this.field1027.get() && this.field1028 >= 0 && MinecraftUtils.isClientActive()) {
            event.method1020();
            this.field1025.add(event.packet);
            if (event.packet instanceof PlayerInteractEntityC2SPacket) {
                this.method1904();
            }
        }
    }

    private boolean method2055(Entity var1) {
        if (var1 instanceof PlayerEntity) {
            if (var1 == mc.player) {
                return false;
            } else if (var1 instanceof FakePlayerEntity) {
                return false;
            } else {
                return Friends.method2055(var1) ? this.friends.getValue() : !AntiBots.method2055(var1);
            }
        } else {
            return false;
        }
    }

    private boolean lambda$new$0() {
        return this.mode.getValue() == BacktrackMode.Smart;
    }
}
