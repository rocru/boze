package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.settings.Setting;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;
import net.minecraft.command.CommandSource;

import java.util.Locale;

public class SetSettingsCommand extends Command {
    public SetSettingsCommand() {
        super("set", "Set", "Set settings");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        for (Module var6 : Boze.getModules().modules) {
            LiteralArgumentBuilder<CommandSource> var7 = LiteralArgumentBuilder.literal(var6.internalName.toLowerCase(Locale.ROOT));
            var7.then(method403("reset").executes(arg_0 -> SetSettingsCommand.lambda$build$0(var6, arg_0)));

            for (Setting<?> var9 : var6.method1144()) {
                var9.buildCommand(var7);
            }

            builder.then(var7);
        }
    }

    private static int lambda$build$0(Module var0, CommandContext var1) throws CommandSyntaxException {
        for (Setting<?> var6 : var0.method1144()) {
            var6.resetValue();
            var6.setExpanded(false);
            var0.bind.copy(Bind.create());
            var0.setHoldBind(false);
            var0.setNotify(false);
            var0.setVisibility(true);
            var0.setName(var0.internalName);
        }

        return 1;
    }
}
