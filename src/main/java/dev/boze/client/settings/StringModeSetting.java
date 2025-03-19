package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.BlockArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class StringModeSetting extends Setting<List<String>> implements IMinecraft {
   private List<Block> field949 = new ArrayList();
   private List<String> field950 = new ArrayList();
   public boolean field951 = true;

   public StringModeSetting(String name, String description) {
      super(name, description);
   }

   public StringModeSetting(String name, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
   }

   public StringModeSetting(String name, String description, Setting parent) {
      super(name, description, parent);
   }

   public StringModeSetting(String name, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
   }

   public void method439(ArrayList<String> blockNames) {
      for (String var6 : blockNames) {
         if (!this.field950.contains(var6.toUpperCase(Locale.ENGLISH)) && method445(var6) != null) {
            this.field950.add(var6.toUpperCase(Locale.ENGLISH));
         }
      }
   }

   public boolean method1701(String blockName) {
      if (!this.field950.contains(blockName.toUpperCase(Locale.ENGLISH)) && method445(blockName) != null) {
         this.field950.add(blockName.toUpperCase(Locale.ENGLISH));
         return true;
      } else {
         return false;
      }
   }

   public boolean method346(String blockName) {
      return this.field950.remove(blockName.toUpperCase(Locale.ENGLISH));
   }

   public void method1416() {
      this.field949.clear();
      this.field950.forEach(this::lambda$refreshBlocks$0);
   }

   public List<String> method1144() {
      ArrayList var1 = new ArrayList();
      this.field949.forEach(StringModeSetting::lambda$getBlocksAsString$1);
      return var1;
   }

   public static Block method445(String name) {
      for (Block var5 : Registries.BLOCK) {
         if (var5.getName().getString().equalsIgnoreCase(name)) {
            return var5;
         }
      }

      return null;
   }

   public List<Block> method2032() {
      return this.field949;
   }

   public List<String> method2033() {
      this.field949.clear();
      this.field950.clear();
      return this.field950;
   }

   public List<String> method442() {
      return this.field950;
   }

   public List<String> method443(List<String> newVal) {
      this.field950 = newVal;
      this.method1416();
      if (this.callback != null) {
         this.callback.accept(newVal);
      }

      return this.field950;
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("add").then(method402("block", BlockArgument.method975()).executes(this::lambda$build$2)))
      );
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("del").then(method402("block", BlockArgument.method975()).executes(this::lambda$build$3)))
      );
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("list").executes(this::lambda$build$5)));
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("clear").executes(this::lambda$build$6)));
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      NbtList var4 = new NbtList();
      this.field950.forEach(StringModeSetting::lambda$addValueToTag$7);
      tag.put("Blocks", var4);
      return tag;
   }

   public List<String> method444(NbtCompound tag) {
      if (tag.contains("Blocks")) {
         NbtList var5 = tag.getList("Blocks", 8);
         this.field950.clear();

         for (NbtElement var7 : var5) {
            if (var7 instanceof NbtString) {
               this.field950.add(var7.asString());
            }
         }

         this.method1416();
      }

      return this.field950;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method444(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method443((List<String>)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method2033();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method442();
   }

   private static void lambda$addValueToTag$7(NbtList var0, String var1) {
      if (!var0.contains(NbtString.of(var1))) {
         var0.add(NbtString.of(var1));
      }
   }

   private int lambda$build$6(CommandContext var1) throws CommandSyntaxException {
      ChatInstance.method624("Clearing all blocks...");
      this.method2033();
      if (this.field951) {
         mc.worldRenderer.reload();
      }

      return 1;
   }

   private int lambda$build$5(CommandContext var1) throws CommandSyntaxException {
      ChatInstance.method624("Blocks: " + this.method2032().size());
      this.method2032().forEach(StringModeSetting::lambda$build$4);
      return 1;
   }

   private static void lambda$build$4(Block var0) {
      ChatInstance.method624(" - (highlight)" + var0.getName().getString());
   }

   private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
      try {
         Block var5 = BlockArgument.method977(var1, "block");
         ChatInstance.method624("Removed " + var5.getName().getString());
         this.method346(var5.getName().getString());
         this.method1416();
         if (this.field951) {
            mc.worldRenderer.reload();
         }

         return 1;
      } catch (Exception var6) {
         ChatInstance.method626("Block not found!");
         return 0;
      }
   }

   private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
      try {
         Block var5 = BlockArgument.method977(var1, "block");
         ChatInstance.method624("Added " + var5.getName().getString());
         this.method1701(var5.getName().getString());
         this.method1416();
         if (this.field951) {
            mc.worldRenderer.reload();
         }

         return 1;
      } catch (Exception var6) {
         ChatInstance.method626("Block not found!");
         return 0;
      }
   }

   private static void lambda$getBlocksAsString$1(List var0, Block var1) {
      var0.add(var1.getName().getString());
   }

   private void lambda$refreshBlocks$0(String var1) {
      Block var4 = method445(var1);
      if (var4 != null) {
         this.field949.add(var4);
      }
   }
}
