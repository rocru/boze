package dev.boze.client.settings;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

public class KitSetting extends Setting<String> implements IMinecraft {
   private String field2195;
   private final String field2196;
   private HashMap<String, Integer> field2197 = null;

   public KitSetting(String name, String value, String description) {
      super(name, description);
      this.field2195 = value;
      this.field2196 = value;
   }

   public KitSetting(String name, String value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field2195 = value;
      this.field2196 = value;
   }

   public KitSetting(String name, String value, String description, Setting parent) {
      super(name, description, parent);
      this.field2195 = value;
      this.field2196 = value;
   }

   public KitSetting(String name, String value, String description, BooleanSupplier visibility, Setting parent) {
      super(name, description, visibility, parent);
      this.field2195 = value;
      this.field2196 = value;
   }

   public HashMap<String, Integer> method1282() {
      return this.field2197;
   }

   public String method1283() {
      return this.field2195;
   }

   public String method1284() {
      return this.field2195 = this.field2196;
   }

   public String method1285(String newVal) {
      File var5 = new File(ConfigManager.kits, newVal + ".json");

      try {
         BufferedReader var6 = Files.newBufferedReader(var5.toPath());

         String var13;
         try {
            JsonObject var7 = new JsonParser().parse(var6).getAsJsonObject();
            this.field2197 = new HashMap();

            for (String var9 : var7.keySet()) {
               this.field2197.put(var9, var7.get(var9).getAsInt());
            }

            var13 = this.field2195 = newVal;
         } catch (Throwable var11) {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (var6 != null) {
            var6.close();
         }

         return var13;
      } catch (Exception var12) {
         return this.field2195;
      }
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method403("save").then(method402("title", StringArgumentType.string()).executes(KitSetting::lambda$build$0)));
      builder.then(method403("select").then(method402("title", StringArgumentType.string()).executes(this::lambda$build$1)));
      builder.then(method403("delete").then(method402("title", StringArgumentType.string()).executes(KitSetting::lambda$build$2)));
      builder.then(method403("list").executes(KitSetting::lambda$build$3));
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putString("Value", this.field2195);
      return tag;
   }

   public String method1286(NbtCompound tag) {
      if (tag.contains("Value")) {
         this.method1285(tag.getString("Value"));
      }

      return this.field2195;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method1286(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method1285((String)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1284();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1283();
   }

   private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
      ChatInstance.method624("Kits: " + ConfigManager.kits.listFiles().length);

      for (File var7 : ConfigManager.kits.listFiles()) {
         ChatInstance.method624(" - (highlight)" + com.google.common.io.Files.getNameWithoutExtension(var7.getName()));
      }

      return 1;
   }

   private static int lambda$build$2(CommandContext var0) throws CommandSyntaxException {
      File var4 = new File(ConfigManager.kits, (String)var0.getArgument("title", String.class) + ".json");
      if (var4.exists()) {
         var4.delete();
         ChatInstance.method624("Deleted kit " + (String)var0.getArgument("title", String.class));
      } else {
         ChatInstance.method625("Kit " + (String)var0.getArgument("title", String.class) + " does not exist");
      }

      return 1;
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      File var5 = new File(ConfigManager.kits, (String)var1.getArgument("title", String.class) + ".json");
      if (var5.exists()) {
         String var6 = this.method1285((String)var1.getArgument("title", String.class));
         if (var6.equals(var1.getArgument("title", String.class))) {
            ChatInstance.method624("Loaded kit " + (String)var1.getArgument("title", String.class));
         } else {
            ChatInstance.method626("Error loading kit " + (String)var1.getArgument("title", String.class));
         }
      } else {
         ChatInstance.method625("Kit " + (String)var1.getArgument("title", String.class) + " does not exist");
      }

      return 1;
   }

   private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
      JsonObject var4 = new JsonObject();

      for (int var5 = 0; var5 < mc.player.getInventory().size(); var5++) {
         ItemStack var6 = mc.player.getInventory().getStack(var5);
         if (!var6.isEmpty()) {
            String var7 = var6.getItem().getName().getString();
            if (var7 != null) {
               for (int var8 = 0; var8 <= 36; var8++) {
                  if (!var4.has(var7 + ">" + var8)) {
                     var4.add(var7 + ">" + var8, new JsonPrimitive(var5));
                     break;
                  }
               }
            }
         }
      }

      try {
         BufferedWriter var12 = Files.newBufferedWriter(new File(ConfigManager.kits, (String)var0.getArgument("title", String.class) + ".json").toPath());

         try {
            new GsonBuilder().setPrettyPrinting().create().toJson(var4, var12);
            ChatInstance.method624("Saved kit " + (String)var0.getArgument("title", String.class));
         } catch (Throwable var10) {
            if (var12 != null) {
               try {
                  var12.close();
               } catch (Throwable var9) {
                  var10.addSuppressed(var9);
               }
            }

            throw var10;
         }

         if (var12 != null) {
            var12.close();
         }
      } catch (IOException var11) {
         ChatInstance.method626("Error saving kit " + (String)var0.getArgument("title", String.class));
      }

      return 1;
   }
}
