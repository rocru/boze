package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.systems.modules.misc.ExtraChat;
import net.minecraft.command.CommandSource;

public class SuffixCommand extends Command {
    public SuffixCommand() {
        super("suffix", "Suffix", "Sets your client suffix");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("text", StringArgumentType.greedyString()).executes(this::lambda$build$0));
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        String var4 = (String) var1.getArgument("text", String.class);
        ExtraChat.INSTANCE.field2931.method1341(var4);
        this.method624("Suffix set to: " + var4);
        return 1;
    }
}
