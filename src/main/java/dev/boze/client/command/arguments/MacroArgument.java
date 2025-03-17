package dev.boze.client.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.Boze;
import dev.boze.client.utils.Macro;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MacroArgument implements ArgumentType<Macro> {
    private static final Collection<String> field1861 = Boze.getMacros()
            .field2140
            .stream()
            .limit(3L)
            .map(Macro::method210)
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType field1862 = new DynamicCommandExceptionType(MacroArgument::lambda$static$0);

    public static MacroArgument method1000() {
        return new MacroArgument();
    }

    public static Macro method1001(CommandContext<?> context, String name) {
        return context.getArgument(name, Macro.class);
    }

    @Override
    public Macro parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        Macro var6 = null;

        for (Macro var8 : Boze.getMacros().field2140) {
            if (var8.method210().equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        if (var6 == null) {
            throw field1862.create(var5);
        } else {
            return var6;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Boze.getMacros().field2140.stream().map(Macro::method210).collect(Collectors.toList()), builder);
    }

    public Collection<String> getExamples() {
        return field1861;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method1002(stringReader);
    //}

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Macro with name " + var0 + " doesn't exist");
    }
}
