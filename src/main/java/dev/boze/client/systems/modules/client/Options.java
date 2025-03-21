package dev.boze.client.systems.modules.client;

import dev.boze.client.Boze;
import dev.boze.client.enums.ModuleState;
import dev.boze.client.enums.PacketRenderMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.WeirdSettingString;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;

import java.io.File;

public class Options extends Module {
    public static final Options INSTANCE = new Options();
    private final EnumSetting<PacketRenderMode> field983 = new EnumSetting<PacketRenderMode>(
            "PacketRender", PacketRenderMode.Auto, "Render rotations in third person"
    );
    private final BooleanSetting field984 = new BooleanSetting("GhostMode", false, "Ghost Mode - Only show Ghost modules");
    public final BooleanSetting field985 = new BooleanSetting("MainMenu", true, "Show the Boze button in the main menu");
    public final BooleanSetting field986 = new BooleanSetting("ServerMenu", true, "Show the Accounts button in the multiplayer menu and disconnected screen");
    public final BooleanSetting field987 = new BooleanSetting("NoPause", false, "Don't pause the game when tabbed out");
    public final WeirdSettingString field988 = new WeirdSettingString("Watermark", "Boze", "Client's watermark, displayed in HUD and Chat");
    public final WeirdSettingString field989 = new WeirdSettingString("Prefix", ".", "Prefix for client commands");
    public final FloatSetting field990 = new FloatSetting("MineDuration", 1.0F, 0.5F, 1.1F, 0.05F, "Duration to assume for predicting mining");
    public final BooleanSetting field991 = new BooleanSetting("CmdToggleNotis", true, "Show chat notifications when toggling toggle settings with commands");
    public final BooleanSetting field992 = new BooleanSetting(
            "NoSwapKeys", false, "Disable swap keys for hotbar\nPrevents number keys from swapping items into hotbar\n"
    );
    public static boolean field993 = false;
    public static final Timer field994 = new Timer();
    private static final boolean field995 = Math.random() < 0.001;

    public Options() {
        super("Options", "Change client options", Category.Client, ConfigCategory.Main);
        if (!new File(System.getProperty("user.home"), "Boze").exists() && !new File(mc.runDirectory, "cache").exists()) {
            this.field989.setValue("qwertyuiop[]");
        }

        this.field984.method401(Options::lambda$new$0);
        this.setNotificationLengthLimited();
    }

    public static String method1562() {
        return field995 ? "Bozo" : INSTANCE.field988.getValue();
    }

    public static String method1563() {
        return INSTANCE.field989.getValue();
    }

    @EventHandler(priority = 9999)
    private void method2041(MovementEvent movementEvent) {
        switch (this.field983.getValue().ordinal()) {
            case 0: {
                field993 = true;
                break;
            }
            case 1: {
                field993 = false;
                break;
            }
            case 2: {
                field993 = field994.hasElapsed(100.0);
            }
        }
    }

    @Override
    public boolean setEnabled(boolean newState) {
        return false;
    }

    public static boolean method477(boolean multitask) {
        try {
            Options.class.getProtectionDomain().getCodeSource().getLocation().toString();
            return true;
        } catch (Throwable var5) {
            if (multitask) {
                return false;
            } else {
                return mc.player.isUsingItem() || AntiCheat.INSTANCE.field2319.getValue() && mc.interactionManager.isBreakingBlock();
            }
        }
    }

    public boolean method1971() {
        return this.field984.getValue();
    }

    public BooleanSetting method478() {
        return this.field984;
    }

    private static void lambda$new$0(Boolean var0) {
        if (var0) {
            for (Module var5 : Boze.getModules().modules) {
                if (!var5.field435) {
                    if (var5.moduleState == null) {
                        var5.moduleState = var5.isEnabled() ? ModuleState.On : ModuleState.Off;
                    }

                    var5.setEnabled(false);
                }
            }
        } else {
            for (Module var7 : Boze.getModules().modules) {
                if (!var7.field435 && var7.moduleState != null) {
                    var7.setEnabled(var7.moduleState == ModuleState.On);
                    var7.moduleState = null;
                }
            }
        }
    }
}
