package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.PlayerListArgument;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class LocateCommand extends Command {
    public LocateCommand() {
        super("locateplayer", "LocatePlayer", "Shows the coords of a player in visual range");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("player", PlayerListArgument.method678()).executes(this::lambda$build$0));
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        PlayerListEntry var5 = PlayerListArgument.method679(var1);
        UUID var6 = var5.getProfile().getId();
        PlayerEntity var7 = mc.world.getPlayerByUuid(var6);
        if (var7 == null) {
            this.method624("Player is not in visual range");
        } else {
            int var10001 = (int) var7.getX();
            int var8 = (int) var7.getZ();
            int var9 = (int) var7.getY();
            int var10 = var10001;
            this.method624("Player is at " + var10 + ", " + var9 + ", " + var8);
        }

        return 1;
    }
}
