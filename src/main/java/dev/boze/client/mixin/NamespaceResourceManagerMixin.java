package dev.boze.client.mixin;

import dev.boze.client.utils.BozeResourcePack;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({NamespaceResourceManager.class})
public class NamespaceResourceManagerMixin {
   @Inject(
      method = {"getResource"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetResource(Identifier var1, CallbackInfoReturnable<Optional<Resource>> var2) {
      if (var1.getNamespace().equals("boze")) {
         var2.setReturnValue(Optional.of(new Resource(new BozeResourcePack(), NamespaceResourceManagerMixin::lambda$onGetResource$0)));
      }
   }

   @Unique
   private static InputStream lambda$onGetResource$0(Identifier var0) throws IOException {
      return MinecraftClient.class.getClassLoader().getResourceAsStream("assets/boze/" + var0.getPath());
   }
}
