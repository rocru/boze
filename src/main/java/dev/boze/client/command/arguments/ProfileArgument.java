package dev.boze.client.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mapped.Class1201;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProfileArgument
        implements ArgumentType<String> {
    private static final DynamicCommandExceptionType field1865 = new DynamicCommandExceptionType(ProfileArgument::lambda$static$0);

    private ProfileArgument() {
    }

    public static ProfileArgument method1009() {
        return new ProfileArgument();
    }

    public static String method1010(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    private static Message lambda$static$0(Object object) {
        return Text.literal("Profile with name " + object + " doesn't exist");
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        String string2 = null;
        for (String string3 : Class1201.field57) {
            if (!string3.equalsIgnoreCase(string)) continue;
            string2 = string3;
            break;
        }
        if (string2 == null) {
            throw field1865.create(string);
        }
        return string2;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Class1201.field57, builder);
    }

    public Collection<String> getExamples() {
        return Class1201.field57.stream().limit(3L).collect(Collectors.toList());
    }
}