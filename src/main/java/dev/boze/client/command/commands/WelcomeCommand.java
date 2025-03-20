package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.systems.modules.hud.core.Welcomer;
import net.minecraft.command.CommandSource;

public class WelcomeCommand extends Command {
    public WelcomeCommand() {
        super("welcomer", "Welcomer", "Sets your welcomer text");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("text").then(method402("text", StringArgumentType.greedyString()).executes(this::lambda$build$0)));
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        String var4 = (String) var1.getArgument("text", String.class);
        Welcomer.INSTANCE.field2663.setValue(var4);
        this.method624("Welcomer text set to: " + var4);
        return 1;
    }
}
