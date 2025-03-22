package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.settings.ItemSetting;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ItemArgument implements ArgumentType<Item> {
    public static ItemArgument method997() {
        return new ItemArgument();
    }

    public static Item method999(CommandContext<?> context, String name) {
        return context.getArgument(name, Item.class);
    }

    private static String lambda$listSuggestions$0(Item var0) {
        return var0.getName().getString();
    }

    @Override
    public Item parse(StringReader reader) {
        String var2 = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return ItemSetting.method446(var2);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Registries.ITEM.stream().map(ItemArgument::lambda$listSuggestions$0).collect(Collectors.toList()), builder);
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method998(stringReader);
    //}

    public Collection<String> getExamples() {
        return Arrays.asList(
                Registries.ITEM.get(0).getName().getString(),
                Registries.ITEM.get(1).getName().getString(),
                Registries.ITEM.get(2).getName().getString()
        );
    }
}
