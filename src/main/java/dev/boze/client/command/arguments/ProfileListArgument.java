package dev.boze.client.command.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.settings.CurrentProfileSetting;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProfileListArgument implements ArgumentType<String> {
    private static final DynamicCommandExceptionType field1866 = new DynamicCommandExceptionType(ProfileListArgument::lambda$static$0);
    private final CurrentProfileSetting field1867;

    public static ProfileListArgument method1012(CurrentProfileSetting setting) {
        return new ProfileListArgument(setting);
    }

    private ProfileListArgument(CurrentProfileSetting var1) {
        this.field1867 = var1;
    }

    private Set<String> method1013() {
        return this.field1867.field968.stream().map(ProfileListArgument::lambda$getProfiles$1).collect(Collectors.toUnmodifiableSet());
    }

    public static String method1014(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        String var5 = reader.readString();
        String var6 = null;

        for (String var8 : this.method1013()) {
            if (var8.equalsIgnoreCase(var5)) {
                var6 = var8;
                break;
            }
        }

        if (var6 == null) {
            throw field1866.create(var5);
        } else {
            return var6;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(this.method1013(), builder);
    }

    public Collection<String> getExamples() {
        return this.method1013().stream().limit(3L).collect(Collectors.toList());
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method1015(stringReader);
    //}

    private static String lambda$getProfiles$1(String var0) {
        return var0.substring(var0.lastIndexOf(46) + 1);
    }

    private static Message lambda$static$0(Object var0) {
        return Text.literal("Profile with name " + var0 + " doesn't exist");
    }
}
