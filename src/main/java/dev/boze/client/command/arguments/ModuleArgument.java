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
import dev.boze.client.systems.modules.Module;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModuleArgument implements ArgumentType<Module> {
    private static final Collection<String> field1863 = Boze.getModules()
            .modules
            .stream()
            .limit(3L)
            .map(Module::getName)
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType field1864 = new DynamicCommandExceptionType(ModuleArgument::lambda$static$0);

    public static ModuleArgument method1003() {
        return new ModuleArgument();
    }

    public static Module method1004(CommandContext<?> context, String name) {
        return context.getArgument(name, Module.class);
    }

    @Override
    public Module parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        Module var6 = null;

        for (Module var8 : Boze.getModules().modules) {
            if (var8.getName().equalsIgnoreCase(var5) || var8.internalName.equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        if (var6 == null) {
            throw field1864.create(var5);
        } else {
            return var6;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(Boze.getModules().method2120(), builder);
    }

    public Collection<String> getExamples() {
        return field1863;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method1005(stringReader);
    //}

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Module with name " + var0 + " doesn't exist.");
    }
}
