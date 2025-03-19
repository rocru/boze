package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.ItemArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;

import java.util.Locale;

public class ItemSetting extends Setting<String> implements IMinecraft {
   private String field952 = "";

   public ItemSetting(String name, String description) {
      super(name, description);
   }

   public static Item method446(String name) {
      for (Item var5 : Registries.ITEM) {
         if (var5.getName().getString().equalsIgnoreCase(name)) {
            return var5;
         }
      }

      return null;
   }

   public Item method447() {
      return this.field952 != null && !this.field952.isEmpty() ? method446(this.field952) : null;
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method402("item", ItemArgument.method997()).executes(this::lambda$build$0)));
      return true;
   }

   public String method1322() {
      return this.field952;
   }

   public String method1562() {
      this.field952 = null;
      return null;
   }

   public String method1341(String newVal) {
      this.field952 = newVal;
      return newVal;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      if (this.field952 != null) {
         tag.putString(this.name, this.field952);
      }

      return tag;
   }

   public String method1286(NbtCompound tag) {
      if (tag.contains(this.name)) {
         this.field952 = tag.getString(this.name);
      }

      return this.field952;
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
      return this.method1341((String)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1562();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1322();
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      try {
         Item var4 = ItemArgument.method999(var1, "item");
         ChatInstance.method624("Set item to " + var4.getName().getString());
         this.field952 = var4.getName().getString();
         return 1;
      } catch (Exception var5) {
         ChatInstance.method626("Item not found!");
         return 0;
      }
   }
}
