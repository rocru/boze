package dev.boze.client.command.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.FriendArgument;
import dev.boze.client.command.arguments.PlayerListArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Friends;
import mapped.Class3063;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class FriendsCommand extends Command {
    public FriendsCommand() {
        super("friends", "Friends", "Manage friends");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("add").then(method402("player", PlayerListArgument.method678()).executes(FriendsCommand::lambda$build$0)));
        builder.then(method403("del").then(method402("player", FriendArgument.method994()).executes(FriendsCommand::lambda$build$1)));
        builder.then(method403("list").executes(FriendsCommand::lambda$build$3));
    }

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        ChatInstance.method624("Friends: " + Friends.method2120().size());
        Friends.method2120().forEach(FriendsCommand::lambda$build$2);
        return 1;
    }

    private static void lambda$build$2(Class3063 var0) {
        ChatInstance.method740("Friends", " - (highlight)%s", var0.method5992());
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        try {
            Class3063 var4 = FriendArgument.method995(var0, "player");
            if (Friends.method345(var4)) {
                ChatInstance.method740("Friends", "Unfriended (highlight)%s", var4.method5992());
            } else {
                ChatInstance.method626("That person is already unfriended.");
            }
        } catch (Exception var5) {
            ChatInstance.method626("That person is already unfriended.");
        }

        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        try {
            GameProfile var4 = PlayerListArgument.method679(var0).getProfile();
            Class3063 var5 = new Class3063(var4.getName());
            if (var5.method5992().equalsIgnoreCase(MinecraftClient.getInstance().player.getName().getString())) {
                ChatInstance.method626("You cannot friend yourself!");
            } else if (Friends.method343(var5)) {
                ChatInstance.method740("Friends", "Friended (highlight)%s", var5.method5992());
            } else {
                ChatInstance.method626("That person is already friended.");
            }
        } catch (Exception var6) {
            ChatInstance.method626("Error friending player");
        }

        return 1;
    }
}
