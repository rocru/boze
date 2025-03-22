package dev.boze.client.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.manager.ConfigManager;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ConfigArgument implements ArgumentType<String> {
    private static final DynamicCommandExceptionType field1857 = new DynamicCommandExceptionType(ConfigArgument::lambda$static$0);
    private static CopyOnWriteArrayList<String> field1855;
    private static Collection<String> field1856;

    private ConfigArgument() {
        field1855 = new CopyOnWriteArrayList(Arrays.asList(ConfigManager.get(ConfigType.CONFIG)));
        field1856 = field1855.stream().limit(3L).collect(Collectors.toList());
    }

    public static ConfigArgument method987() {
        return new ConfigArgument();
    }

    public static void method988(String config) {
        if (!field1855.contains(config)) {
            field1855.add(config);
        }
    }

    public static void method989(String config) {
        if (field1855.remove(config)) {
            try {
                field1856 = field1855.stream().limit(3L).collect(Collectors.toList());
            } catch (Exception var5) {
            }
        }
    }

    public static String method990(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Config with name " + var0 + " doesn't exist");
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        String var6 = null;

        for (String var8 : field1855) {
            if (var8.equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        if (var6 == null) {
            throw field1857.create(var5);
        } else {
            return var6;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(field1855, builder);
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method991(stringReader);
    //}

    public Collection<String> getExamples() {
        return field1856;
    }
}
