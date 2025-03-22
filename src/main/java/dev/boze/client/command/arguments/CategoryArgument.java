package dev.boze.client.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.systems.modules.Category;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryArgument implements ArgumentType<Category> {
    private static final Collection<String> field1852 = Stream.of(Category.values()).limit(3L).map(Enum::name).collect(Collectors.toList());
    private static final DynamicCommandExceptionType field1853 = new DynamicCommandExceptionType(CategoryArgument::lambda$static$0);

    public static CategoryArgument method981() {
        return new CategoryArgument();
    }

    public static Category method982(CommandContext<?> context, String name) {
        return context.getArgument(name, Category.class);
    }

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Category with name " + var0 + " doesn't exist.");
    }

    @Override
    public Category parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        Category var6 = null;

        for (Category var10 : Category.values()) {
            if (var10.name().equalsIgnoreCase(var5)) {
                var6 = var10;
                break;
            }
        }

        if (var6 == null) {
            throw field1853.create(var5);
        } else {
            return var6;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Stream.of(Category.values()).map(Enum::name), builder);
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method983(stringReader);
    //}

    public Collection<String> getExamples() {
        return field1852;
    }
}
