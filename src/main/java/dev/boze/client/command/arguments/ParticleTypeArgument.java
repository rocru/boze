package dev.boze.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.boze.client.settings.StringListSetting;
import net.minecraft.command.CommandSource;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ParticleTypeArgument implements ArgumentType<ParticleType<?>> {
    private ParticleTypeArgument() {
    }

    public static ParticleTypeArgument method1006() {
        return new ParticleTypeArgument();
    }

    @Override
    public ParticleType<?> parse(StringReader reader) {
        String var2 = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return StringListSetting.method453(var2);
    }

    public static ParticleType<?> method1008(CommandContext<?> context, String name) {
        return (ParticleType<?>) context.getArgument(name, ParticleType.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(
                Registries.PARTICLE_TYPE.stream().map(ParticleTypeArgument::lambda$listSuggestions$0).collect(Collectors.toList()), builder
        );
    }

    public Collection<String> getExamples() {
        return Arrays.asList(
                Registries.PARTICLE_TYPE.getId(Registries.PARTICLE_TYPE.get(0)).getPath(),
                Registries.PARTICLE_TYPE.getId(Registries.PARTICLE_TYPE.get(1)).getPath(),
                Registries.PARTICLE_TYPE.getId(Registries.PARTICLE_TYPE.get(2)).getPath()
        );
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Object parse(StringReader stringReader) throws CommandSyntaxException {
    //   return this.method1007(stringReader);
    //}

    private static String lambda$listSuggestions$0(ParticleType var0) {
        return Registries.PARTICLE_TYPE.getId(var0).getPath();
    }
}
