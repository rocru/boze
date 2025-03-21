package dev.boze.client.mixin;

import dev.boze.client.utils.BozeResourcePack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Mixin(value = {ReloadableResourceManagerImpl.class})
public class ReloadableResourceManagerImplMixin {
    @Inject(method = {"getResource"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void onGetResource(Identifier identifier, CallbackInfoReturnable<Optional<Resource>> callbackInfoReturnable) {
        if (identifier.getNamespace().equals("boze")) {
            callbackInfoReturnable.setReturnValue(Optional.of(new Resource(new BozeResourcePack(), () -> ReloadableResourceManagerImplMixin.$lambda$onGetResource$0(identifier))));
        }
    }

    private static InputStream $lambda$onGetResource$0(Identifier identifier) throws IOException {
        return MinecraftClient.class.getClassLoader().getResourceAsStream("assets/boze/" + identifier.getPath());
    }
}
