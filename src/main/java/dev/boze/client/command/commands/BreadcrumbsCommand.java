package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.systems.modules.render.Breadcrumbs;
import net.minecraft.command.CommandSource;

public class BreadcrumbsCommand extends Command {
    public BreadcrumbsCommand() {
        super("breadcrumbs", "Breadcrumbs", "Breadcrumbs command");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("clear").executes(BreadcrumbsCommand::lambda$build$0));
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        Breadcrumbs.INSTANCE.field3423.clear();
        Breadcrumbs.INSTANCE.field3422.clear();
        return 1;
    }
}
