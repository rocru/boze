package dev.boze.client.mixin;

import dev.boze.client.utils.BozeResourcePack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Mixin({NamespaceResourceManager.class})
public class NamespaceResourceManagerMixin {
    @Inject(method = {"getResource"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void onGetResource(Identifier identifier, CallbackInfoReturnable callbackInfoReturnable) {
        if (identifier.getNamespace().equals("boze")) {
            callbackInfoReturnable.setReturnValue(Optional.of(new Resource(new BozeResourcePack(), () -> NamespaceResourceManagerMixin.$lambda$onGetResource$0(identifier))));
        }
    }

    @Unique
    private static InputStream $lambda$onGetResource$0(Identifier var0) throws IOException {
        return MinecraftClient.class.getClassLoader().getResourceAsStream("assets/boze/" + var0.getPath());
    }
}
