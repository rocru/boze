package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.systems.accounts.Account;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import mapped.Class27;
import net.minecraft.command.CommandSource;

class AccountArgument implements ArgumentType<Account<?>> {
   private AccountArgument() {
   }

   public static AccountArgument method972() {
      return new AccountArgument();
   }

   public Account<?> method973(StringReader reader) throws CommandSyntaxException {
      String var5 = reader.readString();

      for (Account var7 : Class27.getAccounts().method1135()) {
         if (var7.method210().equalsIgnoreCase(var5)) {
            return var7;
         }
      }

      return null;
   }

   public static Account<?> method974(CommandContext<?> context, String name) {
      return (Account<?>)context.getArgument(name, Account.class);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      return CommandSource.suggestMatching(
         (Iterable)Class27.getAccounts().method1135().stream().map(AccountArgument::lambda$listSuggestions$0).collect(Collectors.toList()), builder
      );
   }

   public Collection<String> getExamples() {
      return (Collection<String>)Class27.getAccounts().method1135().stream().limit(3L).map(AccountArgument::lambda$getExamples$1).collect(Collectors.toList());
   }

   // $VF: synthetic method
   // $VF: bridge method
   public Object parse(StringReader stringReader) throws CommandSyntaxException {
      return this.method973(stringReader);
   }

   private static String lambda$getExamples$1(Account var0) {
      return var0.method210();
   }

   private static String lambda$listSuggestions$0(Account var0) {
      return var0.method210();
   }
}
