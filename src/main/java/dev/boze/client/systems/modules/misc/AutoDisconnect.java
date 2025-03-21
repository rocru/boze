package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.DisconnectMode;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.MacroSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.Macro;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class3069;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class AutoDisconnect extends Module {
    public static final AutoDisconnect INSTANCE = new AutoDisconnect();
    private final BooleanSetting autoDisable = new BooleanSetting("AutoDisable", true, "Auto disable after disconnecting");
    private final EnumSetting<DisconnectMode> mode = new EnumSetting<DisconnectMode>(
            "Mode",
            DisconnectMode.LogOut,
            "Mode of disconnection\n - LogOut: Disconnect normally\n - Macro: Run a macro\n - Both: Run a macro, then disconnect normally\n"
    );
    private final BooleanSetting exploit = new BooleanSetting("Exploit", true, "Disconnect quicker using exploit", this::lambda$new$0);
    private final MacroSetting macro = new MacroSetting("Macro", "Macro to run before disconnecting", this::lambda$new$1);
    private final FloatSetting health = new FloatSetting("Health", 10.0F, -0.5F, 22.0F, 0.1F, "Health at which to disconnect");
    private final BooleanSetting crystals = new BooleanSetting("Crystals", false, "Disconnect when lethal crystals are nearby");
    private final BooleanSetting players = new BooleanSetting("Players", false, "Disconnect when un-friended players are nearby");
    private final BooleanSetting ignoreTotem = new BooleanSetting("IgnoreTotem", false, "Don't disconnect if you have totems");

    public AutoDisconnect() {
        super("AutoDisconnect", "Automatically log out when at risk of dying", Category.Misc);
    }

    @EventHandler
    public void method1659(PostPlayerTickEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (!this.ignoreTotem.getValue() || !this.hasTotem()) {
                if (this.health.getValue() > -0.5F && mc.player.getHealth() <= this.health.getValue()) {
                    this.method1660("Health below AutoDisconnect threshold");
                } else {
                    if (this.players.getValue()) {
                        int var5 = 0;

                        for (PlayerEntity var7 : mc.world.getPlayers()) {
                            if (var7 != mc.player && !Friends.method346(var7.getNameForScoreboard())) {
                                var5++;
                            }
                        }

                        if (var5 > 1) {
                            this.method1660(var5 + " Un-friended players entered visual range");
                            return;
                        }

                        if (var5 > 0) {
                            this.method1660("Un-friended player entered visual range");
                            return;
                        }
                    }

                    if (this.crystals.getValue()) {
                        float var9 = 0.0F;
                        int var10 = 0;

                        for (Entity var8 : mc.world.getEntities()) {
                            if (var8 instanceof EndCrystalEntity && var8.distanceTo(mc.player) <= 12.0F) {
                                var9 = (float) ((double) var9 + Class3069.method6004(mc.player, var8.getPos()));
                                var10++;
                            }
                        }

                        if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= var9) {
                            this.method1660("Lethal crystal" + (var10 > 1 ? "s" : "") + " nearby");
                        }
                    }
                }
            }
        }
    }

    private boolean hasTotem() {
        for (int var4 = 0; var4 < 36; var4++) {
            if (mc.player.getInventory().getStack(var4).getItem() == Items.TOTEM_OF_UNDYING) {
                return true;
            }
        }

        return false;
    }

    private void method1660(String var1) {
        if (this.autoDisable.getValue()) {
            this.setEnabled(false);
        }

        if (this.mode.getValue() != DisconnectMode.LogOut && !this.macro.getValue().isEmpty()) {
            Macro var5 = this.macro.method467();
            if (var5 != null) {
                var5.method2142();
            }
        }

        if (this.mode.getValue() != DisconnectMode.Macro) {
            if (this.exploit.getValue()) {
                mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(1000));
            }

            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(var1)));
        }
    }

    private boolean lambda$new$1() {
        return this.mode.getValue() != DisconnectMode.LogOut;
    }

    private boolean lambda$new$0() {
        return this.mode.getValue() != DisconnectMode.Macro;
    }
}
