package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.ParticleTypeArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class StringListSetting extends Setting<List<String>> implements IMinecraft {
   private List<ParticleType<?>> field955 = new ArrayList();
   private List<String> field956 = new ArrayList();

   public StringListSetting(String name, String description) {
      super(name, description);
   }

   public StringListSetting(String name, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
   }

   public StringListSetting(String name, String description, Setting parent) {
      super(name, description, parent);
   }

   public StringListSetting(String name, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
   }

   public void method439(ArrayList<String> particleTypes) {
      for (String var6 : particleTypes) {
         if (!this.field956.contains(var6.toUpperCase(Locale.ENGLISH)) && method453(var6) != null) {
            this.field956.add(var6.toUpperCase(Locale.ENGLISH));
         }
      }
   }

   public boolean method1701(String particleType) {
      if (!this.field956.contains(particleType.toUpperCase(Locale.ENGLISH)) && method453(particleType) != null) {
         this.field956.add(particleType.toUpperCase(Locale.ENGLISH));
         return true;
      } else {
         return false;
      }
   }

   public boolean method346(String particleType) {
      return this.field956.remove(particleType.toUpperCase(Locale.ENGLISH));
   }

   public void method1416() {
      this.field955.clear();
      this.field956.forEach(this::lambda$refreshParticleTypes$0);
   }

   public List<String> method1144() {
      return this.field956;
   }

   public static ParticleType<?> method453(String name) {
      for (ParticleType var5 : Registries.PARTICLE_TYPE) {
         if (Registries.PARTICLE_TYPE.getId(var5).getPath().equalsIgnoreCase(name)) {
            return var5;
         }
      }

      return null;
   }

   public List<ParticleType<?>> method2032() {
      return this.field955;
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method403("particles").then(method403("add").then(method402("particle", ParticleTypeArgument.method1006()).executes(this::lambda$build$1))));
      builder.then(method403("particles").then(method403("del").then(method402("particle", ParticleTypeArgument.method1006()).executes(this::lambda$build$2))));
      builder.then(method403("particles").then(method403("list").executes(this::lambda$build$4)));
      builder.then(method403("particles").then(method403("clear").executes(this::lambda$build$5)));
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      NbtList var4 = new NbtList();
      this.field956.forEach((var1) -> {
         if (!var4.contains(NbtString.of(var1))) {
            var4.add(NbtString.of(var1));
         }
      });
      tag.put("Particles", var4);
      return tag;
   }

   @Override
   public List<String> load(NbtCompound nbtCompound) {
      if (nbtCompound.contains("Particles")) {
         NbtList var5 = nbtCompound.getList("Particles", 8);
         this.field956.clear();

         for (NbtElement var7 : var5) {
            if (var7 instanceof NbtString) {
               this.field956.add(var7.asString());
            }
         }

         this.method1416();
      }

      return this.field956;
   }

   @Override
   public List<String> setValue(List<String> list) {
      this.field956 = list;
      this.method1416();
      if (this.callback != null) {
         this.callback.accept(list);
      }

      return this.field956;
   }

   @Override
   public List<String> resetValue() {
      this.field955.clear();
      this.field956.clear();
      return this.field956;
   }

   @Override
   public List<String> getValue() {
      return this.field956;
   }

   private int lambda$build$5(CommandContext var1) throws CommandSyntaxException {
      ChatInstance.method624("Clearing all particles...");
      this.field955.clear();
      this.field956.clear();
      return 1;
   }

   private int lambda$build$4(CommandContext var1) throws CommandSyntaxException {
      ChatInstance.method624("Particles: " + this.method2032().size());
      this.method2032().forEach(StringListSetting::lambda$build$3);
      return 1;
   }

   private static void lambda$build$3(ParticleType var0) {
      ChatInstance.method624(" - (highlight)" + Registries.PARTICLE_TYPE.getId(var0).getPath());
   }

   private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
      try {
         ParticleType var4 = ParticleTypeArgument.method1008(var1, "particle");
         ChatInstance.method624("Removed " + Registries.PARTICLE_TYPE.getId(var4).getPath());
         this.method346(Registries.PARTICLE_TYPE.getId(var4).getPath());
         this.method1416();
         return 1;
      } catch (Exception var5) {
         ChatInstance.method626("Particle not found!");
         return 0;
      }
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      try {
         ParticleType var4 = ParticleTypeArgument.method1008(var1, "particle");
         ChatInstance.method624("Added " + Registries.PARTICLE_TYPE.getId(var4).getPath());
         this.method1701(Registries.PARTICLE_TYPE.getId(var4).getPath());
         this.method1416();
         return 1;
      } catch (Exception var5) {
         ChatInstance.method626("Particle not found!");
         return 0;
      }
   }

   private void lambda$refreshParticleTypes$0(String var1) {
      ParticleType var4 = method453(var1);
      if (var4 != null) {
         this.field955.add(var4);
      }
   }
}
