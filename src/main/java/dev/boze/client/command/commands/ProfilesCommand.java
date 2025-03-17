package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.boze.client.command.Command;
import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.settings.ProfileSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.systems.modules.client.Profiles;
import net.minecraft.command.CommandSource;

public class ProfilesCommand extends Command {
    public ProfilesCommand() {
        super("profiles", "Profiles", "Manage profiles");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        for (Setting var6 : Profiles.INSTANCE.method1144()) {
            if (var6 instanceof CurrentProfileSetting var7) {
                var7.buildCommand(builder);
            } else if (var6 instanceof ProfileSetting var8) {
                var8.buildCommand(builder);
            }
        }
    }
}
