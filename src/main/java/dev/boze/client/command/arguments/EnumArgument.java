package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.settings.EnumSetting;
import net.minecraft.command.CommandSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class EnumArgument implements ArgumentType<Enum> {
    final EnumSetting field1858;

    private EnumArgument(EnumSetting var1) {
        this.field1858 = var1;
    }

    @Override
    public Enum parse(StringReader reader) {
        String var2 = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return this.field1858.method465(var2);
    }

    public static Enum method993(CommandContext<?> context, String name) {
        return context.getArgument(name, Enum.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ArrayList var6 = new ArrayList();

        for (Enum var10 : this.field1858.field972.getClass().getEnumConstants()) {
            var6.add(var10.name().toLowerCase(Locale.ROOT));
        }

        return CommandSource.suggestMatching(var6, builder);
    }

    public Collection<String> getExamples() {
        ArrayList var4 = new ArrayList();

        for (Enum var8 : this.field1858.field972.getClass().getEnumConstants()) {
            var4.add(var8.name().toLowerCase(Locale.ROOT));
        }

        return (Collection<String>) var4.stream().limit(3L).collect(Collectors.toList());
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method992(stringReader);
    //}
}
