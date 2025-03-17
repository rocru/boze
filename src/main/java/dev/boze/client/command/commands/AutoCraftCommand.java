package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ItemArgument;
import dev.boze.client.systems.modules.misc.AutoCraft;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;

public class AutoCraftCommand extends Command {
    public AutoCraftCommand() {
        super("autocraft", "AutoCraft", "Set item to craft");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("item").then(method402("item", ItemArgument.method997()).executes(this::lambda$build$0)));
        builder.then(method403("clear").executes(this::lambda$build$1));
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        AutoCraft.INSTANCE.item.method1341("");
        this.method624("Cleared AutoCraft item");
        return 1;
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        Item var4 = ItemArgument.method999(var1, "item");
        AutoCraft.INSTANCE.item.method1341(var4.getName().getString());
        this.method624("Set AutoCraft item to " + var4.getName().getString());
        return 1;
    }
}
