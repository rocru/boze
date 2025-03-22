package dev.boze.client.utils.trackers;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.systems.modules.misc.FastLatency;
import dev.boze.client.utils.IMinecraft;
import mapped.Class5926;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket;
import net.minecraft.network.packet.c2s.play.AdvancementTabC2SPacket.Action;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.network.packet.s2c.play.SelectAdvancementTabS2CPacket;
import net.minecraft.util.Identifier;

public class LatencyTracker implements IMinecraft {
    public static final LatencyTracker INSTANCE = new LatencyTracker();
    public final Identifier[] field1309 = new Identifier[]{
            Identifier.of("minecraft:story/root"),
            Identifier.of("minecraft:recipes/root"),
            Identifier.of("minecraft:nether/root"),
            Identifier.of("minecraft:adventure/root"),
            Identifier.of("minecraft:end/root"),
            Identifier.of("minecraft:husbandry/root")
    };
    public final long[] field1310 = new long[6];
    public int field1308;
    public int field1311 = 0;
    public boolean field1312 = false;

    @EventHandler
    public void method2042(PacketBundleEvent event) {
        if (!(event.packet instanceof SelectAdvancementTabS2CPacket) || !FastLatency.INSTANCE.isEnabled() || mc.currentScreen instanceof AdvancementsScreen) {
            if (event.packet instanceof PlayerListHeaderS2CPacket var5 && !FastLatency.INSTANCE.isEnabled()) {
                try {
                    if (var5.footer() != null && var5.footer().getString().contains("ping")) {
                        String var14 = var5.footer().getString();
                        int var15 = var14.indexOf("online ") + 9;
                        int var8 = var14.indexOf(" ping");
                        String var9 = var14.substring(var15, var8);

                        try {
                            this.field1308 = Integer.parseInt(var9);
                        } catch (Exception var11) {
                            this.field1308 = Class5926.method100(mc.player);
                        }
                    }
                } catch (Exception var12) {
                }
            }
        } else if (((SelectAdvancementTabS2CPacket) event.packet).getTabId() != null) {
            Identifier var6 = ((SelectAdvancementTabS2CPacket) event.packet).getTabId();

            for (int var7 = 0; var7 < 6; var7++) {
                if (this.field1309[var7].equals(var6)) {
                    this.field1308 = (int) (System.currentTimeMillis() - this.field1310[var7]);
                }
            }
        }
    }

    @EventHandler
    private void method1942(PostPlayerTickEvent var1) {
        if (FastLatency.INSTANCE.isEnabled() && !(mc.currentScreen instanceof AdvancementsScreen)) {
            if (!this.field1312) {
                mc.player.networkHandler.sendPacket(new AdvancementTabC2SPacket(Action.OPENED_TAB, Identifier.of("minecraft:abc")));
                this.field1312 = true;
            }

            mc.player.networkHandler.sendPacket(new AdvancementTabC2SPacket(Action.OPENED_TAB, this.field1309[this.field1311]));
            this.field1310[this.field1311] = System.currentTimeMillis();
            this.field1311++;
            if (this.field1311 == 6) {
                this.field1311 = 0;
            }
        } else if (mc.getCurrentServerEntry() == null || mc.getCurrentServerEntry().address == null || !mc.getCurrentServerEntry().address.contains("2b2t.org")) {
            this.field1308 = Class5926.method100(mc.player);
        }
    }
}
