package dev.boze.client.utils;

import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public class ItemEnchantmentUtils {
   public static boolean hasEnchantments(Item item) {
      return item.getComponents().contains(DataComponentTypes.FOOD) || item == Items.BOW || item == Items.POTION;
   }

   public static void populateEnchantments(ItemStack itemStack, Object2IntMap<RegistryEntry<Enchantment>> enchantments) {
      enchantments.clear();
      if (!itemStack.isEmpty()) {
         for (Entry var7 : itemStack.getItem() == Items.ENCHANTED_BOOK
            ? ((ItemEnchantmentsComponent)itemStack.get(DataComponentTypes.STORED_ENCHANTMENTS)).getEnchantmentEntries()
            : itemStack.getEnchantments().getEnchantmentEntries()) {
            enchantments.put((RegistryEntry)var7.getKey(), var7.getIntValue());
         }
      }
   }

   public static Object2IntMap<RegistryEntry<Enchantment>> getEnchantments(ItemStack itemStack) {
      Object2IntOpenHashMap var4 = new Object2IntOpenHashMap();
      populateEnchantments(itemStack, var4);
      return var4;
   }

   public static int getEnchantmentLevel(ItemStack itemStack, RegistryKey<Enchantment> enchantment) {
      if (itemStack.isEmpty()) {
         return 0;
      } else {
         Object2IntArrayMap var5 = new Object2IntArrayMap();
         populateEnchantments(itemStack, var5);
         return getEnchantmentLevel(var5, enchantment);
      }
   }

   public static int getEnchantmentLevel(Object2IntMap<RegistryEntry<Enchantment>> itemEnchantments, RegistryKey<Enchantment> enchantment) {
      ObjectIterator var5 = Object2IntMaps.fastIterable(itemEnchantments).iterator();

      while (var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         if (((RegistryEntry)var6.getKey()).matchesKey(enchantment)) {
            return var6.getIntValue();
         }
      }

      return 0;
   }

   public static boolean hasEnchantment(ItemStack itemStack, RegistryKey<Enchantment> enchantmentKey) {
      if (itemStack.isEmpty()) {
         return false;
      } else {
         Object2IntArrayMap var5 = new Object2IntArrayMap();
         populateEnchantments(itemStack, var5);
         return hasEnchantment(var5, enchantmentKey);
      }
   }

   private static boolean hasEnchantment(Object2IntMap<RegistryEntry<Enchantment>> var0, RegistryKey<Enchantment> var1) {
      ObjectIterator var5 = var0.keySet().iterator();

      while (var5.hasNext()) {
         RegistryEntry var6 = (RegistryEntry)var5.next();
         if (var6.matchesKey(var1)) {
            return true;
         }
      }

      return false;
   }
}
