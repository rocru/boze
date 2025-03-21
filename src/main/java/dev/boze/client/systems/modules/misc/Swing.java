package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.SwingCancel;
import dev.boze.client.enums.SwingMode;
import dev.boze.client.enums.SwingModeMode;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.HandSwingC2SPacketAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

public class Swing extends Module {
    public static final Swing INSTANCE = new Swing();
    private final EnumSetting<SwingMode> field3131 = new EnumSetting<SwingMode>("Mode", SwingMode.Anarchy, "Mode for Swing", Swing::lambda$new$0);
    private final BooleanSetting field3132 = new BooleanSetting("Render", true, "Render swinging");
    private final EnumSetting<SwingModeMode> field3133 = new EnumSetting<SwingModeMode>("Mode", SwingModeMode.Shuffle, "Swing mode");
    private final EnumSetting<SwingCancel> field3134 = new EnumSetting<SwingCancel>("Cancel", SwingCancel.Off, "Cancel swing packets", this::method1778);
    private boolean field3135 = false;

    private boolean method1777() {
        return Options.INSTANCE.method1971() || this.field3131.getValue() == SwingMode.Ghost;
    }

    private boolean method1778() {
        return !this.method1777();
    }

    public Swing() {
        super("Swing", "Modifies swinging", Category.Misc);
        this.field435 = true;
    }

    @EventHandler
    public void method1779(PrePacketSendEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (event.packet instanceof HandSwingC2SPacket var5) {
                if (this.field3134.getValue() != SwingCancel.Off
                        && this.method1778()
                        && (this.field3134.getValue() == SwingCancel.Normal || mc.interactionManager.isBreakingBlock())) {
                    event.method1020();
                }

                Hand var6 = var5.getHand();
                if (this.field3133.getValue() == SwingModeMode.Offhand) {
                    var6 = Hand.OFF_HAND;
                } else if (this.field3133.getValue() == SwingModeMode.Mainhand) {
                    var6 = Hand.MAIN_HAND;
                } else if (this.field3133.getValue() == SwingModeMode.Opposite) {
                    var6 = var5.getHand() == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
                } else if (this.field3133.getValue() == SwingModeMode.Shuffle) {
                    var6 = this.field3135 ? Hand.OFF_HAND : Hand.MAIN_HAND;
                    this.field3135 = !this.field3135;
                }

                if (this.method1778()) {
                    ((HandSwingC2SPacketAccessor) var5).setHand(var6);
                }

                if (this.field3132.getValue()) {
                    mc.player.swingHand(var6, false);
                }
            }
        }
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }
}
