package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AntiSpam extends Module {
    public static final AntiSpam INSTANCE = new AntiSpam();
    private static final String[] discordLinks = new String[]{"discord.gg"};
    private static final String[] domainLinks = new String[]{
            "http",
            ".com",
            ".ru",
            ".net",
            ".in",
            ".ir",
            ".au",
            ".uk",
            ".de",
            ".br",
            ".xyz",
            ".org",
            ".co",
            ".cc",
            ".me",
            ".tk",
            ".us",
            ".bar",
            ".gq",
            ".nl",
            ".space"
    };
    private static final String[] announcerLinks = new String[]{
            "Looking for new anarchy servers?",
            "I just walked",
            "I just flew",
            "I just placed",
            "I just ate",
            "I just healed",
            "I just took",
            "I just spotted",
            "I walked",
            "I flew",
            "I placed",
            "I ate",
            "I healed",
            "I took",
            "I gained",
            "I mined",
            "I lost",
            "I moved"
    };
    private final BooleanSetting discord = new BooleanSetting("Discord", true, "Hide discord invites");
    private final BooleanSetting hideUrls = new BooleanSetting("URLs", false, "Hide URLs");
    private final BooleanSetting accouncer = new BooleanSetting("Announcer", true, "Hide announcer messages");

    public AntiSpam() {
        super("AntiSpam", "Tries to hide chat spam", Category.Misc);
        this.field435 = true;
    }

    @EventHandler
    private void method1635(PacketBundleEvent var1) {
        if (MinecraftUtils.isClientActive()) {
            if (var1.packet instanceof GameMessageS2CPacket var5) {
                if (this.method1636(var5.content().getString())) {
                    var1.method1020();
                }
            }
        }
    }

    private boolean method1636(String var1) {
        if (this.discord.getValue()) {
            for (String var8 : discordLinks) {
                if (var1.contains(var8)) {
                    return true;
                }
            }
        }

        if (this.accouncer.getValue()) {
            for (String var15 : announcerLinks) {
                if (var1.contains(var15)) {
                    return true;
                }
            }
        }

        if (this.hideUrls.getValue()) {
            for (String var16 : domainLinks) {
                if (var1.contains(var16)) {
                    return true;
                }
            }
        }

        return false;
    }
}
