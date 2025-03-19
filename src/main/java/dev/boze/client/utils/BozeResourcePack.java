package dev.boze.client.utils;

import net.minecraft.registry.VersionedIdentifier;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

public class BozeResourcePack implements ResourcePack {
   @Nullable
   public InputSupplier<InputStream> openRoot(String... segments) {
      return null;
   }

   @Nullable
   public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
      return null;
   }

   public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
   }

   public Set<String> getNamespaces(ResourceType type) {
      return null;
   }

   @Nullable
   public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
      return null;
   }

   public ResourcePackInfo getInfo() {
      return null;
   }

   public String getId() {
      return "Boze";
   }

   public Optional<VersionedIdentifier> getKnownPackInfo() {
      return super.getKnownPackInfo();
   }

   public void close() {
   }
}
