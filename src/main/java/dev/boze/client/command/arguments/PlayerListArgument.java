package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PlayerListArgument implements ArgumentType<PlayerListEntry>, IMinecraft {
    private static Collection<String> field1523;

    public static PlayerListArgument method678() {
        return new PlayerListArgument();
    }

    public static PlayerListEntry method679(CommandContext<?> context) {
        return context.getArgument("player", PlayerListEntry.class);
    }

    @Override
    public PlayerListEntry parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        PlayerListEntry var6 = null;

        for (PlayerListEntry var8 : mc.getNetworkHandler().getPlayerList()) {
            if (var8.getProfile().getName().equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        return var6;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(mc.getNetworkHandler().getPlayerList().stream().map(PlayerListArgument::lambda$listSuggestions$1), builder);
    }

    public Collection<String> getExamples() {
        return field1523;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method680(stringReader);
    //}

    private static String lambda$listSuggestions$1(PlayerListEntry var0) {
        return var0.getProfile().getName();
    }

    private static String lambda$static$0(PlayerListEntry var0) {
        return var0.getProfile().getName();
    }

    static {
        if (mc.getNetworkHandler() != null) {
            field1523 = mc.getNetworkHandler()
                    .getPlayerList()
                    .stream()
                    .limit(3L)
                    .map(PlayerListArgument::lambda$static$0)
                    .collect(Collectors.toList());
        }
    }
}
