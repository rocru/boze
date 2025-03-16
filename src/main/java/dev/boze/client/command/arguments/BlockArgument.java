package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.settings.StringModeSetting;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;

public class BlockArgument implements ArgumentType<Block> {
   public static BlockArgument method975() {
      return new BlockArgument();
   }

   public Block method976(StringReader reader) {
      String var2 = reader.getRemaining();
      reader.setCursor(reader.getTotalLength());
      return StringModeSetting.method445(var2);
   }

   public static Block method977(CommandContext<?> context, String name) {
      return (Block)context.getArgument(name, Block.class);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(
         (Iterable)Registries.BLOCK.stream().map(BlockArgument::lambda$listSuggestions$0).collect(Collectors.toList()), builder
      );
   }

   public Collection<String> getExamples() {
      return Arrays.asList(
         ((Block)Registries.BLOCK.get(0)).getName().getString(),
         ((Block)Registries.BLOCK.get(1)).getName().getString(),
         ((Block)Registries.BLOCK.get(2)).getName().getString()
      );
   }

   // $VF: synthetic method
   // $VF: bridge method
   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.method976(stringReader);
   }

   private static String lambda$listSuggestions$0(Block var0) {
      return var0.getName().getString();
   }
}
