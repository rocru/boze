package dev.boze.client.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.systems.modules.client.Friends;
import mapped.Class3063;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FriendArgument implements ArgumentType<Class3063> {
    private static final Collection<String> field1859 = Friends.method2120()
            .stream()
            .limit(3L)
            .map(Class3063::method5992)
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType field1860 = new DynamicCommandExceptionType(FriendArgument::lambda$static$0);

    public static FriendArgument method994() {
        return new FriendArgument();
    }

    public static Class3063 method995(CommandContext<?> context, String name) {
        return context.getArgument(name, Class3063.class);
    }

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Friend with name " + var0 + " doesn't exist");
    }

    @Override
    public Class3063 parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        Class3063 var6 = null;

        for (Class3063 var8 : Friends.method2120()) {
            if (var8.method5992().equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        if (var6 == null) {
            throw field1860.create(var5);
        } else {
            return var6;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Friends.method2120().stream().map(Class3063::method5992).collect(Collectors.toList()), builder);
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method996(stringReader);
    //}

    public Collection<String> getExamples() {
        return field1859;
    }
}
