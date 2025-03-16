package dev.boze.client.mixin;

import dev.boze.client.utils.BozeResourcePack;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ReloadableResourceManagerImpl.class})
public class ReloadableResourceManagerImplMixin {
   @Inject(
      method = {"getResource"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetResource(Identifier var1, CallbackInfoReturnable<Optional<Resource>> var2) {
      if (var1.getNamespace().equals("boze")) {
         var2.setReturnValue(Optional.of(new Resource(new BozeResourcePack(), ReloadableResourceManagerImplMixin::lambda$onGetResource$0)));
      }
   }

   private static InputStream lambda$onGetResource$0(Identifier var0) throws IOException {
      return MinecraftClient.class.getClassLoader().getResourceAsStream("assets/boze/" + var0.getPath());
   }
}
