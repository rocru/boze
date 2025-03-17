package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.utils.files.FileUtil;
import net.minecraft.command.CommandSource;

public class OpenFolderCommand extends Command {
    public OpenFolderCommand() {
        super("folder", "Folder", "Open the Boze folder");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(OpenFolderCommand::lambda$build$0);
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        FileUtil.openFile(Boze.FOLDER);
        return 1;
    }
}
