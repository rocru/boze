package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.systems.modules.client.Options;
import net.minecraft.command.CommandSource;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix", "Prefix", "Sets your command prefix");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("prefix", StringArgumentType.greedyString()).executes(this::lambda$build$0));
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        String var5 = (String) var1.getArgument("prefix", String.class);
        if (var5.contains(" ")) {
            this.method625("Prefix not changed, prefix cannot contain spaces");
            return 1;
        } else {
            Options.INSTANCE.field989.setValue(var5);
            this.method624("Prefix set to: " + var5);
            return 1;
        }
    }
}
