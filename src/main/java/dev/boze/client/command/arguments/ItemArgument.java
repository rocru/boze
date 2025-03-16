package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.settings.ItemSetting;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class ItemArgument implements ArgumentType<Item> {
   public static ItemArgument method997() {
      return new ItemArgument();
   }

   public Item method998(StringReader reader) {
      String var2 = reader.getRemaining();
      reader.setCursor(reader.getTotalLength());
      return ItemSetting.method446(var2);
   }

   public static Item method999(CommandContext<?> context, String name) {
      return (Item)context.getArgument(name, Item.class);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching((Iterable)Registries.ITEM.stream().map(ItemArgument::lambda$listSuggestions$0).collect(Collectors.toList()), builder);
   }

   public Collection<String> getExamples() {
      return Arrays.asList(
         ((Item)Registries.ITEM.get(0)).getName().getString(),
         ((Item)Registries.ITEM.get(1)).getName().getString(),
         ((Item)Registries.ITEM.get(2)).getName().getString()
      );
   }

   // $VF: synthetic method
   // $VF: bridge method
   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.method998(stringReader);
   }

   private static String lambda$listSuggestions$0(Item var0) {
      return var0.getName().getString();
   }
}
