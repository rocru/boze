package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PlayerArgument implements ArgumentType<AbstractClientPlayerEntity>, IMinecraft {
    private static Collection<String> field1629;

    static {
        if (mc.getNetworkHandler() != null) {
            field1629 = mc.world.getPlayers().stream().limit(3L).map(PlayerArgument::lambda$static$0).collect(Collectors.toList());
        }
    }

    public static PlayerArgument method731() {
        return new PlayerArgument();
    }

    public static AbstractClientPlayerEntity method732(CommandContext<?> context) {
        return context.getArgument("player", AbstractClientPlayerEntity.class);
    }

    private static String lambda$listSuggestions$1(AbstractClientPlayerEntity var0) {
        return var0.getName().getString();
    }

    private static String lambda$static$0(AbstractClientPlayerEntity var0) {
        return var0.getName().getString();
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method733(stringReader);
    //}

    @Override
    public AbstractClientPlayerEntity parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        AbstractClientPlayerEntity var6 = null;

        for (AbstractClientPlayerEntity var8 : mc.world.getPlayers()) {
            if (var8.getName().getString().equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        return var6;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(mc.world.getPlayers().stream().map(PlayerArgument::lambda$listSuggestions$1), builder);
    }

    public Collection<String> getExamples() {
        return field1629;
    }
}
